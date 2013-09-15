package org.totalboumboum.ai.v201011.ais.kesimalvarol.v6;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.successor.SuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Ali Baran Kesimal
 * @author Işıl Varol
 */
@SuppressWarnings("deprecation")
public class FastWallDestruction {
	/** */
	private static KesimalVarol monIA;

	/**
	 * 
	 * @param monIA
	 *            description manquante !
	 * @throws StopRequestException
	 *             description manquante !
	 */
	public static void setMonIA(KesimalVarol monIA) throws StopRequestException {
		monIA.checkInterruption();
		FastWallDestruction.monIA = monIA;
	}

	/**
	 * Determinera une chemin estimée vers l'adversaire : Si on a plus qu'une
	 * seule adversaire, on peut detruire les murs naturellement; c'est pas
	 * necessaire d'attaquer aux adversaires specifiques. Sinon, on peut estimer
	 * les murs les plus convenables (ie. qu'on peut detruire pour créer des
	 * chemins aux adversaires plus rapidement).
	 * 
	 * @param m
	 *            La matrice d'interet
	 * @param cc
	 *            Calcul. de cout
	 * @param hc
	 *            Calcul. d'heuristique
	 * @param sc
	 *            Calcul. successeur
	 * @return Oui si on peut laisser une bombe sur une case plus convenable
	 *         qu'une choisi aléatoirement
	 * @throws StopRequestException
	 *             description manquante !
	 */
	public static boolean canFindAndCreateBetterPathsIfThereIsOneEnemy(
			Matrix m, CostCalculator cc, HeuristicCalculator hc,
			SuccessorCalculator sc) throws StopRequestException {
		monIA.checkInterruption();

		Astar astar = new Astar(monIA, monIA.getSelfHero(), cc, hc, sc);
		boolean willPlayNaturally = true;

		/**
		 * L'adversaire va emettre une "signal" aux quatre sens qui augmentera
		 * les valeurs d'interet des murs destructibles grossierement. Celle-ci
		 * peut realiser en trois iterations : Si l'emission par la case
		 * actuelle d'adversaire ne nous renvoie pas des endroits accesibles,
		 * alors on considere qu'elle se trouve sur une case de meme colonne ou
		 * ligne que notre.
		 */
		if (monIA.getZone().getRemainingHeroes().size() <= 2) {
			int tries = 0;
			AiHero targetAdv = null;
			for (AiHero ah : monIA.getZone().getRemainingHeroes()) {
				monIA.checkInterruption();
				if (!ah.hasEnded() && ah != monIA.getSelfHero()) {
					targetAdv = ah;
					break;
				}
			}
			if (targetAdv != null) {
				while (tries < 3 && willPlayNaturally) {
					monIA.checkInterruption();
					AiTile target = targetAdv.getTile();
					switch (tries) {
					case 1:
						target = monIA.getZone().getTile(target.getLine(),
								monIA.getSelfHero().getCol());
						if (target.getBlocks().size() > 0)
							tries++;
						else
							break;
					case 2:
						target = monIA.getZone().getTile(
								monIA.getSelfHero().getLine(), target.getCol());
						if (target.getBlocks().size() > 0)
							tries++;
						else
							break;
						break;
					default:
						break;
					}
					if (tries == 3)
						break;
					Direction[] dirs = { Direction.LEFT, Direction.UP };
					for (Direction d : dirs) {
						monIA.checkInterruption();
						AiTile targTile = target.getNeighbor(d);
						while (targTile != target) {
							monIA.checkInterruption();
							if (targTile.getBlocks().size() > 0
									&& targTile.getBlocks().get(0)
											.isDestructible()
									&& targTile.getNeighbor(d).getBlocks()
											.size() == 0
									&& targTile.getNeighbor(d).getBombs()
											.size() == 0) {
								m.markForBombPlacementCandidate(targTile
										.getBlocks().get(0), targTile
										.getNeighbor(d).getLine(), targTile
										.getNeighbor(d).getCol(), 100.0);
							}
							if (willPlayNaturally) {
								AiPath pTmp = null;
								try {
									pTmp = astar.processShortestPath(monIA
											.getSelfHero().getTile(), targTile
											.getNeighbor(d));
									if (pTmp != null && pTmp.getLength() > 0) {
										willPlayNaturally = false;
									}
								} catch (NullPointerException e) {
									//
								} catch (LimitReachedException e) {
									//
								}
							}

							targTile = targTile.getNeighbor(d);
						}
					}
					tries++;
				}
			}
		}

		// Si on n'a pas encore trouvée un chemin, on va faire une deuxieme type
		// d'estimation.
		if (willPlayNaturally
				&& monIA.getZone().getRemainingHeroes().size() <= 2) {
			AiHero target = null;
			for (AiHero ah : monIA.getZone().getRemainingHeroes()) {
				monIA.checkInterruption();
				if (!ah.hasEnded() && ah != monIA.getSelfHero()) {
					target = ah;
					break;
				}
			}

			AiPath followCandidate = null;
			int tries = 0;
			if (monIA.verbose)
				System.out.println("cpath begin");
			try {
				while (tries < 4) {
					monIA.checkInterruption();
					AiTile referTile = monIA.getSelfHero().getTile();
					AiZone zone = monIA.getZone();
					Direction[] toTarget = {Direction.NONE, Direction.NONE};
					if(target!=null)
					{	AiTile targetTile = target.getTile();
						Direction dir = zone.getDirection(referTile, targetTile);
						toTarget = dir.getPrimaries();
					}
					followCandidate = new AiPath();
					boolean obstacleFound = false;

					if (monIA.verbose)
						System.out.println("Switching " + tries);
					switch (tries) {
					case 1:
						if (toTarget[0] != Direction.NONE) {
							toTarget[0] = toTarget[0].getOpposite();
							break;
						} else
							tries++;
					case 2:
						if (toTarget[1] != Direction.NONE) {
							toTarget[1] = toTarget[1].getOpposite();
							break;
						} else
							tries++;
					case 3:
						if (toTarget[0] != Direction.NONE
								&& toTarget[1] != Direction.NONE) {
							toTarget[0] = toTarget[0].getOpposite();
							toTarget[1] = toTarget[1].getOpposite();
							break;
						} else
							tries++;
					default:
						break;
					}
					if (monIA.verbose)
						System.out.println("Switching done " + tries);

					for (Direction d : toTarget) {
						monIA.checkInterruption();
						if (obstacleFound)
							break;

						if (monIA.verbose)
							System.out.println(d);

						Direction refA = null, refB = null;

						if (d == Direction.RIGHT || d == Direction.LEFT) {
							refA = Direction.UP;
							refB = Direction.DOWN;
						} else if (d == Direction.UP || d == Direction.DOWN) {
							refA = Direction.RIGHT;
							refB = Direction.LEFT;
						}
						if (refA != null && refB != null) {
							while (true) {
								monIA.checkInterruption();
								if (monIA.verbose)
									System.out.println(referTile);
								if (referTile == target.getTile().getNeighbor(
										refA)
										|| referTile == target.getTile()
												.getNeighbor(refB)) {
									if (monIA.verbose)
										System.out.println("Arrived NUD");
									break;
								}
								if (m.representation[referTile.getLine()][referTile
										.getCol()] >= 0) {
									if (referTile.getNeighbor(d).getBlocks()
											.size() > 0) {
										if (referTile.getNeighbor(d)
												.getBlocks().get(0)
												.isDestructible()) {
											m.markForBombPlacementCandidate(
													referTile.getNeighbor(d)
															.getBlocks().get(0),
													referTile.getLine(),
													referTile.getCol(), 60.0);
											m.bestDirectionOptimisation();
											obstacleFound = true;
											if (monIA.verbose)
												System.out.println("Obs found "
														+ referTile);
											break;
										} else {
											followCandidate = null;
											obstacleFound = true;
											break;
										}
									}
									referTile = referTile.getNeighbor(d);
									// System.out.println(referTile);
									followCandidate.addTile(referTile);
								} else {
									followCandidate = null;
									obstacleFound = true;
									break;
								}
							}
						}
					}
					tries++;
					if (followCandidate != null)
						break;
					if (monIA.verbose)
						System.out.println("New try " + tries);
				}
			} catch (NullPointerException e) // a la fin du jeu.
			{
				if (monIA.verbose)
				{	//e.printStackTrace();				
				}
			}
			if (monIA.verbose)
				System.out.println("cpath done");
			if (followCandidate != null
					&& !PathSafetyDeterminators.isThisPathDangerous(
							followCandidate, m)) {
				willPlayNaturally = false;
			}
		}
		return willPlayNaturally;
	}
}

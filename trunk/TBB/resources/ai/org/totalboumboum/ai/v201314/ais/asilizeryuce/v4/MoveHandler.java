package org.totalboumboum.ai.v201314.ais.asilizeryuce.v4;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.path.cost.ApproximateCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.TileCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.TileHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.SearchMode;
import org.totalboumboum.ai.v201314.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.agent.AiCombination;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.search.Astar;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent.
 * 
 * @author Emre Asıl
 * @author Tülin İzer
 * @author Miray Yüce
 */
@SuppressWarnings("deprecation")
public class MoveHandler extends AiMoveHandler<Agent> {

	/** c'est une case qui est sure */
	AiTile safeDestination = null;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected MoveHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

		// dans cette classe, on aura généralement besoin d'un objet de type
		// Astar.
		// à titre d'exemple, on construit ici un objet Astar très simple (pas
		// forcément très efficace)
		// pour des raisons de rapidité, il est recommandé de créer l'objet
		// Astar une seule fois,
		// et non pas à chaque itération. Cela permet aussi d'éviter certains
		// problèmes de mémoire.
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		height = zone.getHeight();
		width = zone.getWidth();

		ApproximateCostCalculator approximateCostCalculator = new ApproximateCostCalculator(
				ai, ownHero);
		ApproximateSuccessorCalculator approximateSuccessorCalculator = new ApproximateSuccessorCalculator(
				ai);

		TimeCostCalculator timeCostCalculator = new TimeCostCalculator(ai,
				ownHero);
		TimeHeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(
				ai, ownHero);
		TimePartialSuccessorCalculator timeSuccessorCalculator = new TimePartialSuccessorCalculator(
				ai, SearchMode.MODE_ONEBRANCH);

		CostCalculator basicCostCalculator = new TileCostCalculator(ai);
		HeuristicCalculator basicHeuristicCalculator = new TileHeuristicCalculator(
				ai);
		SuccessorCalculator basicSuccessorCalculator = new BasicSuccessorCalculator(
				ai);

		astar = new Astar(ai, ownHero, timeCostCalculator, heuristicCalculator,
				timeSuccessorCalculator);
		astarApproximate = new Astar(ai, ownHero, approximateCostCalculator,
				heuristicCalculator, approximateSuccessorCalculator);

		astarBasic = new Astar(ai, ownHero, basicCostCalculator,
				basicHeuristicCalculator, basicSuccessorCalculator);

		astar.setVerbose(false);
		astarApproximate.setVerbose(false);
		// this.astar.getSuccessorCalculator().setConsiderOpponents(true);
		this.astar.getCostCalculator().setMalusCost(1000);
		this.astarApproximate.getCostCalculator().setMalusCost(2000);

		this.astarTemporelleTileCosts = new double[zone.getHeight()][zone
				.getWidth()];
		// this.astarApproxTileCosts = new double[zone.getHeight()][zone
		// .getWidth()];

		// On initialise avec des zéros pour chaque case.
		for (int r = 0; r < astarTemporelleTileCosts.length; r++) {
			ai.checkInterruption();
			for (int c = 0; c < astarTemporelleTileCosts[0].length; c++) {
				ai.checkInterruption();
				astarTemporelleTileCosts[r][c] = 0;
				// astarApproxTileCosts[r][c] = 0;
			}
		}

		/* Add cost to corridors */
		for (int r = 0; r < astarTemporelleTileCosts.length; r++) {
			ai.checkInterruption();
			for (int c = 0; c < astarTemporelleTileCosts[0].length; c++) {
				ai.checkInterruption();

				if (ai.tileHandler.tileIsCorridor(zone.getTile(r, c)))
					astarTemporelleTileCosts[r][c] = AiTile.getSize() * 3;
			}

		}

		// this.astar.getCostCalculator().setTileCosts(tileCosts);

		// this.astar.getCostCalculator().setOpponentCost(5000);

	}

	// ///////////////////////////////////////////////////////////////
	// DATA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** A titre d'exemple, je stocke la zone de jeu, car on en a souvent besoin */
	private AiZone zone = null;
	/**
	 * A titre d'exemple, je stocke le sprite controlé par cet agent, car on en
	 * a aussi souvent besoin
	 */
	private AiHero ownHero = null;
	/**
	 * Il est nécessaire de stocker l'objet Astar, si on ne veut pas devoir le
	 * re-créer à chaque itération
	 */
	public Astar astar = null;
	/**
	 * Il est nécessaire de stocker l'objet Astar approximate, si on ne veut pas
	 * devoir le re-créer à chaque itération
	 */
	private Astar astarApproximate = null;
	/**
	 * Si l'astar temporelle renvoyer un chemin nulle, on essaye de trouver une
	 * chemin avec astar basic
	 */
	private Astar astarBasic = null;

	/** C'est un obstacle dans notre premier chemin indirect */
	AiTile blockedTile = null;

	/** le premier chemin indirect qu'on a trouve */
	boolean firstIndirectPath = false;
	/** Controle si la case qu'on se trouve est sure? */
	boolean controlSecurity = false;
	/** controle la case qui nous bloque contient une bombe */
	boolean controlBomb = false;
	/** controle la case qui nous bloque contient de flamme */
	boolean controlFire = false;
	/** Controle s'il y a un item dans la case bloqué (blockedNextTile) */
	boolean controlNextTile = false;
	/**
	 * si notre chemin est indirect, on pose cette question pour savoir est-ce
	 * q'on doit mets dans prochaine case une bombe ou pas?
	 */
	public boolean nextTileHasBlock = false;

	/** destCombine controle est-ce la combinaison de la case ou on veut 
	 * aller est egale a la combinaison de la case ou on se trouve
	 */
	AiCombination destCombin = null;

	/**C'est la destination sure pour notre IA */
	public AiTile safeDestionation = null;

	/** on determine de cout pour quelque cases. c'est la matrice de ces cases*/
	public double astarTemporelleTileCosts[][] = null;

	/** si astar nous retourne null, on utilise newDest de trouver une nouvelle destination sure*/
	AiTile newDest = null;

	/** c'est la precedent destination sure*/
	AiTile prevSafe = null;
	
	/** si la portee de notre bombe est plus petite que la zone sure et si astar nous avait retourne 
	 * null, alors on determine la case precedent de la bombe d'un ennemi qui nous menace et on pose une
	 * bombe la. en faisant ça, on essaye de creer une zone sure pour nous.  */
	public AiTile targetTile = null;
	
	/**
	 * si une case objectif est trouvee targetDetected nous donne true, sinon false
	*/
	public boolean targetDetected = false;

	/** heroIsDead controle quand il n'y a pas de case sure, destination sure et si 
	 * on va mourir dans quelque temps.*/
	public boolean heroIsDead = false;

	/** la largeur de la zone*/
	int width;
	/** la hauteur de la zone */
	int height;


	/**
	 * Location initial
	 * */
	private Boolean initalLocation = true;

	// ///////////////////////////////////////////////////////////////
	// DESTINATION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected AiTile processCurrentDestination() {
		ai.checkInterruption();
		AiTile result = null;

		// ici, à titre d'exemple, on se contente de prendre la case dont la
		// préférence est maximale
		// c'est une approche simpliste, ce n'est pas forcément la meilleure
		// (sûrement pas, d'ailleurs)
		// c'est seulement pour montrer un exemple en termes de programmation
		// (et non pas de conception d'agent)
		AiPreferenceHandler<Agent> preferenceHandler = ai
				.getPreferenceHandler();
		Map<Integer, List<AiTile>> preferences = preferenceHandler
				.getPreferencesByValue();
		int minPref = Collections.min(preferences.keySet()); // ATTENTION : ici
																// il faudrait
																// tester qu'il
																// y a au moins
																// une valeur
																// dans la map
																// (sinon :
																// NullPointerException
																// !
		AiTile previousTile = getCurrentDestination();
		List<AiTile> tiles = preferences.get(minPref); // on récupère la liste
														// de cases qui ont la
														// meilleure préférence

		if (initalLocation) {
			initalLocation = false;
			result = tiles.get(0); // on prend la première de la liste
									// (arbitrairement)
		} else {
			// if next tiles preference is not better than previous tiles don't
			// change destination
			if (tiles.contains(previousTile)) {
				result = previousTile;
			} else
				result = tiles.get(0);
		}

		destCombin = ai.preferenceHandler.getCombinationForTile(result);

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// PATH /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected AiPath processCurrentPath() {
		ai.checkInterruption();
		AiPath result = null;
		AiLocation startLocation = new AiLocation(ownHero);
		boolean tileHasMalus = false;
		boolean enemyAccessible = false;
		nextTileHasBlock = false;
		heroIsDead = false;

		int reacheableSize = ai.tileHandler.reacheableTiles.size();
		int bombRange = ownHero.getBombRange();

		/*
		 * cette case correspond à celle sélectionnée dans la méthode
		 * processCurrentDestination
		 */
		AiTile endTile = getCurrentDestination();

		for (AiTile tile : ai.enemyHandler.enemyTiles()) {
			ai.checkInterruption();
			if (ai.tileHandler.accessibleTiles.contains(tile))
				enemyAccessible = true;
		}

		/* If enemy is accesible use time based astar */
		if (enemyAccessible) {
			try {
				result = astar.startProcess(startLocation, endTile);

				if (result == null) {
					targetDetected = false;

					if (reacheableSize > bombRange) {
						for (AiTile tiles : ai.tileHandler.reacheableTiles) {
							ai.checkInterruption();
							for (AiTile tile : tiles.getNeighbors()) {
								ai.checkInterruption();
								if (!tile.getBombs().isEmpty()) {
									// target = tiles;
									targetDetected = true;
									targetTile = tiles;
									break;
								}
							}

							if (targetDetected)
								break;
						}

						if (targetTile != null
								&& ai.tileHandler.dangerousTiles().contains(
										ownHero.getTile())) {
							try {
								result = astarBasic.startProcess(startLocation,
										targetTile);

							} catch (LimitReachedException e) {
								//
							}
						}

					} 
					
//					else {
//
//						for (AiTile tile : ai.tileHandler.reacheableTiles) {
//							ai.checkInterruption();
//							if (!ai.tileHandler.dangerousTiles().contains(tile)) {
//								safeDestination = tile;
//							}
//						}
//
//						if (safeDestination != null
//								&& ai.tileHandler.dangerousTiles().contains(
//										ownHero.getTile())) {
//							try {
//								result = astarBasic.startProcess(startLocation,
//										safeDestination);
//							} catch (LimitReachedException e) {
//								//
//							}
//						}
//
//						if (safeDestination == null)
//							heroIsDead = true;
//
//					}
				}

			} catch (LimitReachedException e) { // e.printStackTrace(); // il ne
												// faut PAS afficher la trace
												// (cf.

				result = new AiPath();
			}
		}

		/*
		 * If non of the enemies are accessible use astar approximatif to find
		 * indirect path
		 */
		else {
			if (blockedTile != null) {
				controlSecurity = ownHero.getTile().equals(safeDestination);
				controlBomb = blockedTile.getBombs().isEmpty();
				controlFire = blockedTile.getFires().isEmpty();
			}

//			if (blockedNextTile != null) {
//				if (blockedNextTile.getBlocks().isEmpty()
//						&& blockedNextTile.getItems().isEmpty())
//					controlNextTile = true;
//			}

			if (firstIndirectPath == false || controlSecurity && controlBomb
					&& controlFire /*&& controlNextTile */ ) {
				try {
					result = astarApproximate.startProcess(startLocation,
							endTile);

				} catch (LimitReachedException e) {

					result = new AiPath();
				}
				firstIndirectPath = true;

				if (result != null) {
					blockedTile = null;
					Iterator<AiLocation> it = result.getLocations().iterator();
					AiTile previousTile = null;

					/* find the blocked tile */
					while (it.hasNext()) {
						ai.checkInterruption();
						AiLocation location = it.next();
						AiTile tile = location.getTile();
						List<AiBlock> blocks = tile.getBlocks();

						/* if tile has malus blow it up */
						for (AiItem item : tile.getItems()) {
							ai.checkInterruption();
							if (ai.itemHandler.isMalus(item)) {
								tileHasMalus = true;
						//		blockedNextTile = tile;
							}
						}

						if (!blocks.isEmpty() || tileHasMalus) {
							blockedTile = previousTile;
//							blockedNextTile = tile;
							break;
						}
						previousTile = tile;
					}

				}
				newDest = blockedTile;

			}

			/* set a flag for bomb handler */
			if (blockedTile != null) {
				if (blockedTile.equals(ownHero.getTile())
						&& blockedTile.getBombs().isEmpty()) {
					nextTileHasBlock = true;

				}
			}

			if (ownHero.getTile().equals(blockedTile)
					&& !blockedTile.getBombs().isEmpty()) {
				for (AiTile tile : ai.tileHandler.reacheableTiles) {
					ai.checkInterruption();

					if (!ai.tileHandler.dangerousTiles().contains(tile)) {
						safeDestination = tile;
						break;
					}
				}
				newDest = safeDestination;
			}

			if (newDest != null) {
				try {
					result = astar.startProcess(startLocation, newDest);
				} catch (LimitReachedException e) {
					//
				}
				if (result == null) {
					targetDetected = false;

					
					for (AiTile tile : ai.tileHandler.reacheableTiles) {
						ai.checkInterruption();
						if (!ai.tileHandler.dangerousTiles().contains(tile)) {
							safeDestination = tile;
						}
					}
					
					if (reacheableSize > bombRange) {
						for (AiTile tiles : ai.tileHandler.reacheableTiles) {
							ai.checkInterruption();
							for (AiTile tile : tiles.getNeighbors()) {
								ai.checkInterruption();
								if (!tile.getBombs().isEmpty()) {
									// target = tiles;
									targetDetected = true;
									targetTile = tiles;
									break;
								}
							}

							if (targetDetected)
								break;
						}

						if (targetTile != null
								&& ai.tileHandler.dangerousTiles().contains(
										ownHero.getTile()) && safeDestination == null) {
							try {
								result = astarBasic.startProcess(startLocation,
										targetTile);

							} catch (LimitReachedException e) {
								//
							}
						}

					} 
					
//					else {
//
//						if (safeDestination != null
//								&& ai.tileHandler.dangerousTiles().contains(
//										ownHero.getTile())) {
//							try {
//								result = astarBasic.startProcess(startLocation,
//										safeDestination);
//							} catch (LimitReachedException e) {
//								;
//							}
//						}
//
//						if (safeDestination == null)
//							heroIsDead = true;
//
//					}
				}
			}

		}

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// DIRECTION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Direction processCurrentDirection() {
		ai.checkInterruption();
		Direction result = Direction.NONE;

		// AiPath currentPath = getCurrentPath();

		ai.currentPath = getCurrentPath();

		/* Path is null or its only our tile */// destinasyonla bulundugum
												// karenin preferansı aynıysa
												// dur

		AiCombination ownHeroTileCombin = ai.preferenceHandler
				.getCombinationForTile(ownHero.getTile());

		// bulundugun case ın preferansı destinasyonun preferansına esitse
		// orda kal
		// if (!ai.tileHandler.dangerousTiles().contains(ownHero.getTile()))
		if (destCombin != null)
			if (destCombin.equals(ownHeroTileCombin))
				return Direction.NONE;

		if (ai.currentPath == null || ai.currentPath.getLength() < 2)
			result = Direction.NONE;

		else {
			long wait = ai.currentPath.getFirstPause();
			if (wait > 0) {
				result = Direction.NONE;
			} else {

				AiLocation nextLocation = ai.currentPath.getLocation(1);
				this.ai.nextTile = nextLocation.getTile();
				AiTile currentTile = ownHero.getTile();
				this.ai.lastDirection = zone.getDirection(currentTile,
						this.ai.nextTile);

				result = this.ai.lastDirection;
			}
		}
		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() {
		ai.checkInterruption();

		// ici on se contente de faire le traitement par défaut
		super.updateOutput();

	}
}
package org.totalboumboum.ai.v201011.ais.isbirkoyas.v5;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

public class DeplacementAttaque {
	IsbirKoyas ai = new IsbirKoyas();
	private DeplacementCommune deplacementCommune = null;
	boolean poserBombe2;
	boolean attaque = false;
	boolean detruire = false;
	boolean blockage = false;

	public DeplacementAttaque(IsbirKoyas ai) throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		this.ai = ai;

	}

	/**
	 * Cette méthode impl�mente l'algorithme d'attaque. Elle prend 3 arguments
	 * une matrice de type double, la zone du jeu et une action.
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @param matriceAttaque
	 *            la matrice attaque
	 * @return resultat
	 * @throws StopRequestException
	 */
	public AiAction algorithmAttaque(double[][] matriceAttaque,
			AiZone gameZone, AiAction resultat) throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		DeplacementCommune deplacementCommune = new DeplacementCommune(ai);
		Securite securite = new Securite(ai);
		AiTile startPoint = ai.ourHero.getTile();
		// Les positions finales possibles de notre hero calcule par la methode
		// calculateEndPoints
		List<AiTile> endPoints = calculeLesPointsFinaux(matriceAttaque,
				gameZone);
		List<AiTile> endPoints2 = new ArrayList<AiTile>();
		endPoints2 = endPoints;
		List<AiTile> heroTiles = new ArrayList<AiTile>();
		Collection<AiHero> heros = gameZone.getRemainingHeroes();
		Iterator<AiHero> iteratorHeros = heros.iterator();
		while (iteratorHeros.hasNext()) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			AiHero hero = iteratorHeros.next();
			heroTiles.add(hero.getTile());
		}

		// on va calculer une nouvelle action
		if (ai.nextMove == null && ai.senfuire == false) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			try {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				ai.nextMove = deplacementCommune.cheminLePlusCourt(ai.ourHero,
						startPoint, heroTiles);
				if (ai.nextMove.getLength() == 0)
					ai.nextMove = null;
				if (ai.nextMove != null) {
					ai.checkInterruption(); // APPEL OBLIGATOIRE
					endPoints = calculeLesPointsFinaux(matriceAttaque, gameZone);
					endPoints2 = endPoints;
					if (ai.print)
						System.out.println("Attaque: NORMAL EndPonits: "
								+ endPoints);

					try {
						ai.checkInterruption(); // APPEL OBLIGATOIRE
						ai.nextMove = deplacementCommune.cheminLePlusCourt(
								ai.ourHero, startPoint, endPoints);
					} catch (Exception e) {
						if (ai.print)
							System.out.println(e);
					}
					if (ai.print)
						System.out.println("Attaque: NORMAL NextMove:"
								+ ai.nextMove);
					if (ai.nextMove.getLength() == 0) {
						ai.checkInterruption(); // APPEL OBLIGATOIRE
						ai.nextMove = null;
					}
					ai.attaqueBombe = true;
					detruire = false;
					attaque = true;
				}
			} catch (Exception e) {
				if (ai.print)
					System.out.println("On est bloque" + e);
			}

			// si on ne peut pas aller a cette case, ça veut dire on est bloque
			// Donc il faut detruire les murs
			// Detruire
			if (ai.nextMove == null && ai.senfuire == false && !blockage) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				if (ai.print)
					System.out.println("Attaque: nextMove==NULL => DETRUIRE");
				endPoints2 = endPoints;
				endPoints = deplacementCommune.detruire(gameZone, resultat,
						endPoints2);
				// if(endPoints.contains(ai.ourHero.getTile()) &&
				// endPoints.size()>1)
				// endPoints.remove(ai.ourHero.getTile());
				if (!endPoints.isEmpty()) {
					ai.checkInterruption(); // APPEL OBLIGATOIRE
					ai.nextMove = deplacementCommune.cheminLePlusCourt(
							ai.ourHero, startPoint, endPoints);
					if (ai.print)
						System.out.println("Attaque: DETRUIRE NextMove"
								+ ai.nextMove);
				}
				ai.attaqueBombe = true;

				detruire = true;
				attaque = false;
			}
		}
		// Apres nos calculs on a une action
		if (ai.nextMove != null) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			if (ai.print)
				System.out.println("Attaque: nextMove!=NULL");
			if (ai.nextMove.getLength() == 0) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				if (ai.print)
					System.out.println("Attaque: NextMove.lenght()==0"
							+ ai.nextMove + "senfuire:" + ai.senfuire);
				ai.nextMove = null;
			} else {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				if (ai.print)
					System.out.println("Attaque: NextMove.length()!=0"
							+ ai.nextMove);
				// Si le joueur est arrive au case suivant
				if ((ai.ourHero.getLine() == ai.nextMove.getTile(0).getLine())
						&& (ai.ourHero.getCol() == ai.nextMove.getTile(0)
								.getCol())) {
					ai.checkInterruption(); // APPEL OBLIGATOIRE
					// On enleve cette cases da la liste des cases suivantes
					// nextMove
					ai.nextMove.getTiles().remove(0);
					// Si la liste est vide, alors l'objectif est obtenu et
					// il n'y a pas plus de cases a suivre
					if (ai.nextMove.getTiles().isEmpty()) {
						ai.checkInterruption(); // APPEL OBLIGATOIRE
						ai.nextMove = null;
						// on arrete de senfuire
						if (ai.senfuire == true)
							ai.senfuire = false;
						if (ai.senfuire2 == true)
							ai.senfuire2 = false;
						// si on peut poser bombe, on fait les calculs pour ce
						// posage
						if (ai.poserBombe == true && ai.senfuire == false
								&& ai.attaqueBombe == true
								&& ai.senfuire2 == false) {
							ai.checkInterruption(); // APPEL OBLIGATOIRE
							if (!securite.plein(gameZone, ai.ourHero.getTile())) {

								if (ai.print)
									System.out
											.println("Attaque: IA pense a poser bombe!");

								poserBombe2 = true;

								List<AiTile> casSurs = securite
										.posageControle(gameZone);

								if (ai.print)
									System.out.println("Attaque: casSurs: "
											+ casSurs);
								// s'il n'y a pas de case sure on ne la pose pas

								if (casSurs.isEmpty())

									poserBombe2 = false;
								else {

									ai.checkInterruption(); // APPEL OBLIGATOIRE
									AiPath chemin = deplacementCommune
											.cheminLePlusCourt(ai.ourHero,
													ai.ourHero.getTile(),
													endPoints);
									List<AiTile> tileList = new ArrayList<AiTile>();

									if (casSurs.size() > 1 && attaque) {
										for (int i = 0; i < casSurs.size(); i++) {
											ai.checkInterruption(); // APPEL
																	// OBLIGATOIRE
											tileList.add(casSurs.get(i));
											AiTile tile = deplacementCommune
													.cheminLePlusCourt(
															ai.ourHero,
															ai.ourHero
																	.getTile(),
															tileList)
													.getFirstTile();
											Direction dir = gameZone
													.getDirection(ai.ourHero,
															tile);
											AiTile tile2 = chemin
													.getFirstTile();
											Direction dir2 = gameZone
													.getDirection(ai.ourHero,
															tile2);
											if (dir == dir2) {

												tileList.remove(casSurs.get(i));
											}
										}
										if (!tileList.isEmpty()) {
											ai.checkInterruption(); // APPEL
																	// OBLIGATOIRE
											casSurs = tileList;
										}
									}

									ai.nextMove2 = deplacementCommune
											.cheminLePlusCourt(ai.ourHero,
													ai.ourHero.getTile(),
													casSurs);
									if (ai.nextMove2.isEmpty()) {
										ai.checkInterruption(); // APPEL
																// OBLIGATOIRE
										poserBombe2 = false;
									} else {
										ai.checkInterruption(); // APPEL
																// OBLIGATOIRE
										poserBombe2 = true;
									}
									if (ai.print)
										System.out.println("NextMove2:"
												+ ai.nextMove2);

									// les calculs des durees
									long temps = ai.ourHero.getBombPrototype()
											.getNormalDuration();
									long temps2 = ai.nextMove2
											.getDuration(ai.ourHero);
									if (temps2 > temps) {
										ai.checkInterruption(); // APPEL
																// OBLIGATOIRE
										if (ai.print)
											System.out
													.println("Il n'y a pas du temps à senfuire.");
										poserBombe2 = false;
									}

								}
								if (poserBombe2) {
									ai.checkInterruption(); // APPEL OBLIGATOIRE
									resultat = new AiAction(
											AiActionName.DROP_BOMB);
									if (ai.print)
										System.out
												.println("Attaque: IA a pose la bombe!");
									if (attaque) {
										ai.checkInterruption(); // APPEL
																// OBLIGATOIRE
										AiPath chemin = deplacementCommune
												.cheminLePlusCourt(ai.ourHero,
														ai.ourHero.getTile(),
														endPoints);
										if (chemin.isEmpty()) {
											if (ai.print)
											System.out.println("entre1");
											blockage = true;

										} else {
											ai.checkInterruption(); // APPEL
																	// OBLIGATOIRE
											if (ai.print)
											System.out.println("entre2");
											blockage = false;
										}
									}
									ai.nextMove = null;
									// alors notre IA a pose sa bombe donc il
									// peut s'enfuir
									ai.senfuire = true;
									// senfuire
									senfuireApresPosage();

								} else {
									// si on ne pose pas de bombe, on va
									// calculer une autre action
									ai.checkInterruption(); // APPEL OBLIGATOIRE
									if (ai.print)
										System.out
												.println("Attaque: On ne pose pas de bombe. On change NextMove=null");
									ai.nextMove = null;

								}
							}
						}
					} else {

						// on n'arrive pas a la fin du chemin
						ai.checkInterruption(); // APPEL OBLIGATOIRE
						// si on ne s'enfuit pas:
						if (!ai.senfuire && !ai.senfuire2) {
							ai.checkInterruption(); // APPEL OBLIGATOIRE

							// on calcule si notre case cible est chang�e
							if (!endPoints.contains(ai.nextMove.getLastTile())
									&& !endPoints.isEmpty()) {
								ai.checkInterruption(); // APPEL OBLIGATOIRE
								AiPath path = deplacementCommune
										.cheminLePlusCourt(ai.ourHero,
												startPoint, endPoints);
								if (!path.isEmpty()
										&& securite.nextMoveSecurite(
												ai.ourHero, gameZone, path))
									ai.nextMove = path;
							}

							// si le chemin est secure
							if (securite.nextMoveSecurite(ai.ourHero, gameZone,
									ai.nextMove)) {
								if (ai.print)
									System.out
											.println("Attaque: 1) nextMove est secure");
								if (!ai.nextMove.getFirstTile().getFires()
										.isEmpty()) {
									ai.checkInterruption(); // APPEL OBLIGATOIRE
									resultat = new AiAction(AiActionName.NONE);
								} else {
									ai.checkInterruption(); // APPEL OBLIGATOIRE
									resultat = deplacementCommune
											.newAction(ai.nextMove);
								}
								if (ai.print)
									System.out.println("ESSAI1");
							}
							// si le chemin nest pas secure on va senfuire s'il
							// y a une bombe
							else {
								// senfuire des bombes des ennemis
								ai.checkInterruption(); // APPEL OBLIGATOIRE
								if (!ai.nextMove.getFirstTile().getBombs()
										.isEmpty()) {
									ai.checkInterruption(); // APPEL OBLIGATOIRE
									AiBomb bombe = ai.nextMove.getFirstTile()
											.getBombs().get(0);
									if (ai.print)
										System.out
												.println("Attaque: 1) Il y a une bombe au NextMove");
									List<AiTile> casSurs2 = securite
											.bombeControle(gameZone, bombe);
									if (ai.print)
										System.out
												.println("Attaque: 1) casSurs2:"
														+ casSurs2);
									if (!casSurs2.isEmpty()) {
										ai.checkInterruption(); // APPEL
																// OBLIGATOIRE
										ai.nextMove3 = deplacementCommune
												.cheminLePlusCourt(ai.ourHero,
														ai.ourHero.getTile(),
														casSurs2);
										ai.senfuire2 = true;
										ai.nextMove = ai.nextMove3;
										if (ai.print)
											System.out
													.println("Attaque: 1) nextMove:"
															+ ai.nextMove);
									}
								} else {
									ai.checkInterruption(); // APPEL OBLIGATOIRE
									ai.senfuire2 = false;
								}
								if (!ai.nextMove.getFirstTile().getFires()
										.isEmpty()) {
									ai.checkInterruption(); // APPEL OBLIGATOIRE
									resultat = new AiAction(AiActionName.NONE);
								}
								if (ai.print)
									System.out.println("ESSAI2");
							}
						}
						/*
						 * ce n'est pas important que la case suivante contient
						 * une bombe ou un blast d'une bombe. Car on s'enfuit
						 * d'une bombe et on doit passer des cases dangers. Mais
						 * il est sur qu'on ne peut pas passer dans des feus.
						 * Donc on fait du controle des feux. S'il y a du feu,
						 * donc on attend avec une action qui s'appelle NONE.
						 * (on ne fait rien, on attend)
						 */
						// si on s'enfuit:
						else {
							// controle du feu avec IsCrossableBy
							if (ai.nextMove.getFirstTile().isCrossableBy(
									ai.ourHero)) {
								ai.checkInterruption(); // APPEL OBLIGATOIRE
								if (ai.print)
									System.out.println("ESSAI3");
								resultat = deplacementCommune
										.newAction(ai.nextMove);
							}
							// senfuit des bombes des ennemis
							if (!ai.nextMove.getFirstTile().getBombs()
									.isEmpty()) {
								ai.checkInterruption(); // APPEL OBLIGATOIRE
								AiBomb bombe = ai.nextMove.getFirstTile()
										.getBombs().get(0);
								if (ai.print)
									System.out
											.println("Attaque: 1) Il y a une bombe au NextMove");
								List<AiTile> casSurs2 = securite.bombeControle(
										gameZone, bombe);
								if (ai.print)
									System.out.println("Attaque: 1) casSurs2:"
											+ casSurs2);
								if (!casSurs2.isEmpty()) {
									ai.checkInterruption(); // APPEL OBLIGATOIRE
									ai.nextMove3 = deplacementCommune
											.cheminLePlusCourt(ai.ourHero,
													ai.ourHero.getTile(),
													casSurs2);
									ai.senfuire2 = true;
									ai.nextMove = ai.nextMove3;
									if (ai.print)
										System.out
												.println("Attaque: 1) nextMove:"
														+ ai.nextMove);
								}
							} else {
								ai.checkInterruption(); // APPEL OBLIGATOIRE
								ai.senfuire2 = false;
							}
							if (!ai.nextMove.getFirstTile().getFires()
									.isEmpty()) {
								ai.checkInterruption(); // APPEL OBLIGATOIRE
								resultat = new AiAction(AiActionName.NONE);
							}
						}
					}
				}

				// si on n'arrive pas à la case suivant
				else {
					ai.checkInterruption(); // APPEL OBLIGATOIRE
					if (ai.print) {
						System.out
								.println("Attaque: 2) On n'arrive pas a la fin du chemin");
						System.out.println("Attaque: 2) senfuire2:"
								+ ai.senfuire2);
					}
					// si on ne s'enfuit pas:
					if (!ai.senfuire && !ai.senfuire2) {
						ai.checkInterruption(); // APPEL OBLIGATOIRE
						// si le chemin est secure
						if (securite.nextMoveSecurite(ai.ourHero, gameZone,
								ai.nextMove)) {
							ai.checkInterruption(); // APPEL OBLIGATOIRE
							if (ai.print)
								System.out
										.println("Attaque: 1) nextMove est secure");

							if (ai.print)
								System.out.println("ESSAI4");

							// ///////////////////////////////////////////////////////

							if (!ai.nextMove.getFirstTile().getFires()
									.isEmpty()) {
								ai.checkInterruption(); // APPEL OBLIGATOIRE
								resultat = new AiAction(AiActionName.NONE);
							} else {
								ai.checkInterruption(); // APPEL OBLIGATOIRE
								resultat = deplacementCommune
										.newAction(ai.nextMove);
							}
						}
						// si le chemin nest pas secure
						else {
							ai.checkInterruption(); // APPEL OBLIGATOIRE
							if (!ai.nextMove.getFirstTile().getBombs()
									.isEmpty()) {
								AiBomb bombe = ai.nextMove.getFirstTile()
										.getBombs().get(0);
								if (ai.print)
									System.out
											.println("Attaque: 1) Il y a une bombe au NextMove");
								List<AiTile> casSurs2 = securite.bombeControle(
										gameZone, bombe);
								if (ai.print)
									System.out.println("Attaque: 1) casSurs2:"
											+ casSurs2);
								if (!casSurs2.isEmpty()) {
									ai.checkInterruption(); // APPEL OBLIGATOIRE
									ai.nextMove3 = deplacementCommune
											.cheminLePlusCourt(ai.ourHero,
													ai.ourHero.getTile(),
													casSurs2);
									ai.senfuire2 = true;
									ai.nextMove = ai.nextMove3;
									if (ai.print)
										System.out
												.println("Attaque: 1) nextMove:"
														+ ai.nextMove);
								}
							} else
								ai.senfuire2 = false;
							if (!ai.nextMove.getFirstTile().getFires()
									.isEmpty()) {
								ai.checkInterruption(); // APPEL OBLIGATOIRE
								resultat = new AiAction(AiActionName.NONE);
							}
							if (ai.print)
								System.out.println("ESAAI5");
						}
					}
					// si on s'enfuit:
					else {
						ai.checkInterruption(); // APPEL OBLIGATOIRE
						// controle du feu avec IsCrossableBy
						if (ai.nextMove.getFirstTile()
								.isCrossableBy(ai.ourHero)) {
							ai.checkInterruption(); // APPEL OBLIGATOIRE
							if (ai.print)
								System.out.println("ESSAI6");
							ai.checkInterruption(); // APPEL OBLIGATOIRE
							resultat = deplacementCommune
									.newAction(ai.nextMove);
						}
						ai.checkInterruption();
						if (!ai.nextMove.getFirstTile().getBombs().isEmpty()) {
							ai.checkInterruption(); // APPEL OBLIGATOIRE
							AiBomb bombe = ai.nextMove.getFirstTile()
									.getBombs().get(0);
							if (ai.print)
								System.out
										.println("Attaque: 1) Il y a une bombe au NextMove");
							List<AiTile> casSurs2 = securite.bombeControle(
									gameZone, bombe);
							if (ai.print)
								System.out.println("Attaque: 1) casSurs2:"
										+ casSurs2);
							if (!casSurs2.isEmpty()) {
								ai.checkInterruption(); // APPEL OBLIGATOIRE
								ai.nextMove3 = deplacementCommune
										.cheminLePlusCourt(ai.ourHero,
												ai.ourHero.getTile(), casSurs2);
								ai.senfuire2 = true;
								ai.nextMove = ai.nextMove3;
								if (ai.print)
									System.out.println("Attaque: 1) nextMove:"
											+ ai.nextMove);
							}
						} else
							ai.senfuire2 = false;
						if (!ai.nextMove.getFirstTile().getFires().isEmpty()) {
							ai.checkInterruption(); // APPEL OBLIGATOIRE
							resultat = new AiAction(AiActionName.NONE);
						}

					}
				}
			}
		}
		if (ai.nextMove != null)
			if (ai.nextMove.getFirstTile() == ai.ourHero.getTile()) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				ai.nextMove.removeTile(0);
			}
		return resultat;
	}

	/**
	 * Cette méthode forme une liste des cases cibles que notre IA peut aller
	 * dans le mode attaque. Elle prend deux arguments une matrice de type
	 * double et la zone du jeu. Notre IA peut se d�placer en traversant ces
	 * cases.
	 * 
	 * @param matrice
	 *            La Matrice attaque
	 * @param gameZone
	 *            la zone du jeu
	 * @return la liste des points finaux.
	 * @throws StopRequestException
	 */
	public List<AiTile> calculeLesPointsFinaux(double[][] matriceAttaque,
			AiZone gameZone) throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE

		List<AiTile> endPoints = new ArrayList<AiTile>();
		int i = gameZone.getHeight();
		int j = gameZone.getWidth();
		for (int ii = 0; ii < i; ii++)
			for (int jj = 0; jj < j; jj++)
				if (matriceAttaque[ii][jj] > 1
						&& gameZone.getTile(ii, jj).getBlocks().isEmpty()) {
					ai.checkInterruption(); // APPEL OBLIGATOIRE
					endPoints.add(gameZone.getTile(ii, jj));
				}

		List<AiTile> endPoints2 = new ArrayList<AiTile>();

		destinationAttaque(endPoints, endPoints2, matriceAttaque);

		if (ai.print)
			System.out.println("Attaque: EndPoints sans DANGER:" + endPoints2);

		return endPoints2;
	}

	/**
	 * Cette méthode compare les cases cibles entre eux et retourne la case qui
	 * a la valeur plus �lev�e. S�il y a plus d�une valeur �lev�e égales alors
	 * IA regarde au distance de ceux cases. Elle prend trois arguments une
	 * matrice de type double et deux listes des cases.
	 * 
	 * @param matrice
	 *            La Matrice Attaque
	 * @param endPoints
	 * @param endPoints2
	 * @return la liste des points finaux.
	 * @throws StopRequestException
	 */
	public void destinationAttaque(List<AiTile> endPoints,
			List<AiTile> endPoints2, double[][] matrice)
			throws StopRequestException {

		AiTile temp;
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		// ordre
		if (endPoints.size() > 1) {
			for (int j = 0; j < endPoints.size(); j++) {
				for (int i = 0; i < endPoints.size() - 1; i++) {
					ai.checkInterruption(); // APPEL OBLIGATOIRE
					AiTile element = endPoints.get(i);
					AiTile element2 = endPoints.get(i + 1);
					int x = element.getLine();
					int y = element.getCol();
					int x2 = element2.getLine();
					int y2 = element2.getCol();
					double val = matrice[x][y];
					double val2 = matrice[x2][y2];
					if (val < val2) {
						ai.checkInterruption(); // APPEL OBLIGATOIRE
						temp = element;
						endPoints.set(i, element2);
						endPoints.set(i + 1, temp);
					}
				}
			}
		}
		if (ai.print)
			System.out.println("Destination1:" + endPoints);
		if (!endPoints.isEmpty()) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			double max = matrice[endPoints.get(0).getLine()][endPoints.get(0)
					.getCol()];
			for (int i = 0; i < endPoints.size(); i++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				AiTile element = endPoints.get(i);
				int x = element.getLine();
				int y = element.getCol();
				double val = matrice[x][y];
				if (val >= max)
					endPoints2.add(element);
			}
		}

		if (ai.print)
			System.out.println("Destination2:" + endPoints2);
	}

	/**
	 * Cette méthode permet a s'enfuire apres le posage de bombe
	 * 
	 * @throws StopRequestException
	 */
	public void senfuireApresPosage() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		if (ai.nextMove == null && ai.senfuire == true) {
			if (ai.print)
				System.out.println("Attaque: nextMove==NULL => SENFUIRE");
			try {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				ai.nextMove = ai.nextMove2;
				if (ai.print)
					System.out.println("Attaque: SENFUIRE NextMove:"
							+ ai.nextMove);
				ai.attaqueBombe = false;
			} catch (Exception e) {
				if (ai.print)
					System.out
							.println("Attaque: SENFUIRE: On ne peut pas senfuir");
			}
		}
	}

	// LE METHODE D'ACCES
	/**
	 * METHODE D'ACCES a la classe DeplacementCommune
	 * 
	 * @throws StopRequestException
	 * @return deplacement commune
	 */
	public DeplacementCommune DeplacementCommune() throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE
		return deplacementCommune;
	}
}

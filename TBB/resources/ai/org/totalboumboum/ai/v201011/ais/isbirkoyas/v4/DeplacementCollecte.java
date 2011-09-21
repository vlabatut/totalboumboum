package org.totalboumboum.ai.v201011.ais.isbirkoyas.v4;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

public class DeplacementCollecte {
	IsbirKoyas ai = new IsbirKoyas();
	public boolean print = false;
	private Securite securite = null;
	private DeplacementCommune deplacementCommune = null;
	private boolean poserBombe2;

	public DeplacementCollecte(IsbirKoyas ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;

	}

	/**
	 * Methode implementant l'algorithme de collecte.
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @param matriceCollect
	 *            la matrice collecte
	 * @return resultat
	 * @throws StopRequestException
	 */
	public AiAction algorithmCollect(int[][] matriceCollecte, AiZone gameZone,
			AiAction resultat) throws StopRequestException {
		ai.checkInterruption();

		DeplacementCommune deplacementCommune = new DeplacementCommune(ai);
		Securite securite = new Securite(ai);
		// La position actuelle de notre hero
		AiTile startPoint = ai.ourHero.getTile();
		ai.checkInterruption();
		// Les positions finales possibles de notre hero calcule par la methode
		// calculateEndPoints
		List<AiTile> endPoints = calculeLesPointsFinaux(matriceCollecte,
				gameZone);
		// Si c'est le premier appel par le moteur ou l'agent a complete son
		// objectif
		// On commence a trouver si le hero est arrive au voisinage
		// d'une case mur.

		if (ai.nextMove == null && ai.senfuire == false) {
			try {
				endPoints = calculeLesPointsFinaux(matriceCollecte, gameZone);
				if (print)
					System.out.println("Collect Normal:" + endPoints);

				try {
					ai.nextMove = deplacementCommune.cheminLePlusCourt(
							ai.ourHero, startPoint, endPoints);
				} catch (Exception e) {
					System.out.println(e);
				}
				if (ai.nextMove.getLength() == 0)
					ai.nextMove = null;
				if (print)
					System.out.println("NextMove1:" + ai.nextMove);

				// il ne pose pas de bombe pour aller au cas de bonus.
				ai.collectBombe = false;

			} catch (Exception e) {
				if (print)
					System.out.println("Collect:On est bloqué" + e);
			}
			// Detruire
			if (ai.nextMove == null && ai.senfuire == false) {
				if (print)
					System.out.println("Collect: nextMove==NULL => DETRUIRE");
				endPoints = deplacementCommune.detruire(gameZone, resultat);
				ai.nextMove = deplacementCommune.cheminLePlusCourt(ai.ourHero,
						startPoint, endPoints);
				if (print)
					System.out.println("Collect: DETRUIRE" + ai.nextMove);

				// il pose une bombe pour detruire le mur destructible.
				ai.collectBombe = true;
			}
		}

		if (ai.nextMove != null) {

			if (print)
				System.out.println("Collect: nextMove!=NULL");
			if (ai.nextMove.getLength() == 0) {
				if (print)
					System.out.println("Collect: NextMove.lenght()==0"
							+ ai.nextMove);
				ai.nextMove = null;
			} else {
				if (print)
					System.out.println("Collect: NextMove.length()!=0"
							+ ai.nextMove);
				// Si le joueur est arrive au case suivant
				if ((ai.ourHero.getLine() == ai.nextMove.getTile(0).getLine())
						&& (ai.ourHero.getCol() == ai.nextMove.getTile(0)
								.getCol())) {
					// On enleve cette cases da la liste des cases suivantes
					// nextMove
					ai.nextMove.getTiles().remove(0);
					// System.out.println("essai1");
					// Si la liste est vide, alors l'objectif est obtenu et
					// il n'y a pas plus de cases a suivre
					if (ai.nextMove.getTiles().isEmpty()) {
						ai.nextMove = null;

						if (ai.senfuire == true) {
							ai.senfuire = false;
						}
						if (ai.senfuire2 == true) {
							ai.senfuire2 = false;
						}

						if (ai.poserBombe == true && ai.senfuire == false
								&& ai.collectBombe == true
								&& ai.senfuire2 == false) {
							if (!securite.plein(gameZone, ai.ourHero.getTile())) {
								if (print)
									System.out
											.println("Collect: IA pense à poser bombe!");

								poserBombe2 = true;

								List<AiTile> casSurs = securite
										.PosageControle(gameZone);
								if (print)
									System.out.println("casSurs: " + casSurs);
								if (casSurs.isEmpty())
									poserBombe2 = false;
								else {
									ai.nextMove2 = deplacementCommune
											.cheminLePlusCourt(ai.ourHero,
													ai.ourHero.getTile(),
													casSurs);

									if (ai.nextMove2.isEmpty()) {
										poserBombe2 = false;
									} else
										poserBombe2 = true;

									if (print)
										System.out.println("Collect SENFUIRE:"
												+ ai.nextMove2);

									long temps = ai.ourHero.getBombPrototype()
											.getNormalDuration();
									long temps2 = ai.nextMove2
											.getDuration(ai.ourHero);
									if (temps2 > temps) {
										if (print)
											System.out
													.println("Il n'y a pas du temps à senfuire.");
										poserBombe2 = false;
									}
								}

								if (poserBombe2) {
									resultat = new AiAction(
											AiActionName.DROP_BOMB);
									if (print)
										System.out
												.println("Collect: IA a pose la bombe!");
									ai.nextMove = null;
									ai.senfuire = true;
									if (ai.nextMove == null
											&& ai.senfuire == true) {
										// alors notre IA a pose sa bombe donc
										// il doit
										// s'enfuir
										if (print)
											System.out
													.println("Collect: nextMove==NULL => SENFUIRE");
										try {
											ai.nextMove = ai.nextMove2;
											if (print)
												System.out
														.println("NextMove3: SENFUIRE"
																+ ai.nextMove);

											ai.collectBombe = false;
										} catch (Exception e) {
											if (print)
												System.out
														.println("Collect:On ne peut pas senfuir");
										}
									}
								} else
									ai.nextMove = null;
							}
						}
					} else {
						/*
						 * ce n'est pas important que la case suivante contient
						 * une bombe ou un blast d'une bombe. Car on s'enfuit
						 * d'une bombe et on doit passer des cases dangers. Mais
						 * il est sur qu'on ne peut pas passer dans des feus.
						 * Donc on fait du controle des feux. S'il y a du feu,
						 * donc on attend avec une action qui s'appelle NONE.
						 * (on ne fait rien, on attend)
						 */
						if (print) {
							System.out
									.println(": 1) On n'arrive pas à la fin du chemin");
							System.out.println("Collect: 1) senfuire2:"
									+ ai.senfuire2);
						}
						// si on ne s'enfuit pas:

						if (!ai.senfuire && !ai.senfuire2) {
							if (securite.nextMoveSecurite(ai.ourHero, gameZone,
									ai.nextMove)) {
								if (print)
									System.out
											.println("Collect: 1) nextMoveSecurite");
								if (!ai.nextMove.getFirstTile().getBombs()
										.isEmpty()) {
									AiBomb bombe = ai.nextMove.getFirstTile()
											.getBombs().get(0);
									if (print)
										System.out
												.println("Collect: 1) Il y a une bombe au NextMove");
									List<AiTile> casSurs2 = securite
											.PosageControle2(gameZone, bombe);
									System.out.println("Collect: 1) casSurs2:"
											+ casSurs2);
									if (!casSurs2.isEmpty()) {
										ai.nextMove3 = deplacementCommune
												.cheminLePlusCourt(ai.ourHero,
														ai.ourHero.getTile(),
														casSurs2);
										ai.senfuire2 = true;
										ai.nextMove = ai.nextMove3;
										System.out
												.println("Collect: 1) nextMove:"
														+ ai.nextMove);
									}
								} else
									ai.senfuire2 = false;
								if (!ai.nextMove.getFirstTile().getFires()
										.isEmpty())
									resultat = new AiAction(AiActionName.NONE);
								else
									resultat = deplacementCommune
											.newAction(ai.nextMove);
								if (print)
									System.out.println("ESSAI1");
							} else {
								// ai.nextMove=null;
								if (print)
									System.out.println("ESAAI2");
								resultat = new AiAction(AiActionName.NONE);
							}
						}

						// si on s'enfuit:
						else {
							// controle du feu avec IsCrossableBy
							if (securite.controle(ai.nextMove.getTile(0),
									gameZone)) {
								if (print)
									System.out.println("ESSAI3");
								resultat = deplacementCommune
										.newAction(ai.nextMove);
							} else {
								// ai.nextMove=null;
								if (print)
									System.out.println("ESSAI4");
								resultat = new AiAction(AiActionName.NONE);
							}
						}
					}
				} else {
					if (print) {
						System.out
								.println("Collect: 2) On n'arrive pas à la fin du chemin");
						System.out.println("Collect: 2) senfuire2:"
								+ ai.senfuire2);
					}
					// si on ne s'enfuit pas:
					if (!ai.senfuire && !ai.senfuire2) {
						if (securite.nextMoveSecurite(ai.ourHero, gameZone,
								ai.nextMove)) {
							if (print)
								System.out
										.println("Collect: 2) nextMoveSecurite");
							if (!ai.nextMove.getFirstTile().getBombs()
									.isEmpty()) {
								AiBomb bombe = ai.nextMove.getFirstTile()
										.getBombs().get(0);
								if (print)
									System.out
											.println("Collect: 2) Il y a une bombe au NextMove");
								List<AiTile> casSurs2 = securite
										.PosageControle2(gameZone, bombe);
								System.out.println("Collect: 2) casSurs2: "
										+ casSurs2);
								if (!casSurs2.isEmpty()) {
									ai.nextMove3 = deplacementCommune
											.cheminLePlusCourt(ai.ourHero,
													ai.ourHero.getTile(),
													casSurs2);

									ai.nextMove = ai.nextMove3;
									ai.senfuire2 = true;

									System.out.println("Collect: 2) nextMove:"
											+ ai.nextMove);
								}

							} else
								ai.senfuire2 = false;

							if (!ai.nextMove.getFirstTile().getFires()
									.isEmpty())
								resultat = new AiAction(AiActionName.NONE);
							else
								resultat = deplacementCommune
										.newAction(ai.nextMove);

							if (print)
								System.out.println("ESSAI5");

						}

						else {

							if (print)
								System.out.println("ESSAI6 "
										+ ai.nextMove.getTile(0));
							resultat = new AiAction(AiActionName.NONE);
						}
					}
					// si on ne s'enfuit pas:
					else {

						if (securite.controle(ai.nextMove.getTile(0), gameZone)) {
							resultat = deplacementCommune
									.newAction(ai.nextMove);
							if (print)
								System.out.println("ESSAI7");
						} else {
							if (print)
								System.out.println("ESSAI8");
							resultat = new AiAction(AiActionName.NONE);
						}

					}
				}
			}
		}
		return resultat;
	}

	/**
	 * Methode calculant la liste des cases ou le hero peut aller pour la
	 * matrice collecte . On prend aussi en compte les cases qui sont dans la
	 * portee des bombes. Notre hero peut se deplacer en traversant ces cases.
	 * 
	 * @param matrice
	 *            La Matrice collecte
	 * @param gameZone
	 *            la zone du jeu
	 * @return la liste des points finaux.
	 * @throws StopRequestException
	 */
	public List<AiTile> calculeLesPointsFinaux(int[][] matrice, AiZone gameZone)
			throws StopRequestException {

		ai.checkInterruption();
		// La perception instantanement de l'environnement
		gameZone = ai.getPercepts();
		// la longueur de la matrice
		int width = gameZone.getWidth();
		// la largeur de la matrice
		int height = gameZone.getHeight();
		// la liste ou les points finaux sont tenus
		List<AiTile> endPoints = new ArrayList<AiTile>();
		for (int i = 0; i < height; i++) {
			ai.checkInterruption();
			for (int j = 0; j < width; j++) {
				ai.checkInterruption();
				if (matrice[i][j] > 0) {
					endPoints.add(gameZone.getTile(i, j));
				}
			}
		}
		if (print)
			System.out.println("Collect: EndPoints:" + endPoints);

		List<AiTile> endPoints2 = new ArrayList<AiTile>();

		try {
			destinationCollect(endPoints, endPoints2, matrice);
			endPoints = endPoints2;
		} catch (Exception e) {
			// 
			e.printStackTrace();
		}
		if (print)
			System.out.println("Collect: Destination EndPoints:" + endPoints2);
		return endPoints;
	}

	/**
	 * Cette méthode compare les cases cibles entre eux et retourne la case qui
	 * a la valeur plus �levée. S�il y a plus d�une valeur �levée égales alors
	 * IA regarde au distance de ceux cases
	 * 
	 * @param matrice
	 *            La Matrice collecte
	 * @param endPoints
	 * @param endPoints2
	 * @return la liste des points finaux.
	 * @throws StopRequestException
	 */
	public void destinationCollect(List<AiTile> endPoints,
			List<AiTile> endPoints2, int[][] matrice)
			throws StopRequestException {

		AiTile temp;
		for (int j = 0; j < endPoints.size(); j++) {
			for (int i = 0; i < endPoints.size() - 1; i++) {
				AiTile element = endPoints.get(i);
				AiTile element2 = endPoints.get(i + 1);
				int x = element.getLine();
				int y = element.getCol();
				int x2 = element2.getLine();
				int y2 = element2.getCol();
				int val = matrice[x][y];
				int val2 = matrice[x2][y2];
				if (val < val2) {
					temp = element;
					endPoints.set(i, element2);
					endPoints.set(i + 1, temp);
				}
			}
		}
		if (!endPoints.isEmpty()) {
			int max = matrice[endPoints.get(0).getLine()][endPoints.get(0)
					.getCol()];
			for (int i = 0; i < endPoints.size(); i++) {
				AiTile element = endPoints.get(i);
				int x = element.getLine();
				int y = element.getCol();
				int val = matrice[x][y];
				if (val >= max)
					endPoints2.add(element);
			}
		}
	}
	// LES METHODES D'ACCES 
	public Securite Securite() throws StopRequestException {
		ai.checkInterruption();
		return securite;

	}

	public DeplacementCommune DeplacementCommune() throws StopRequestException {
		ai.checkInterruption();
		return deplacementCommune;
	}
}

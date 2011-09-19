package org.totalboumboum.ai.v201011.ais.isbirkoyas.v5;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;

public class DeplacementCollecte {
	IsbirKoyas ai = new IsbirKoyas();
	private Securite securite = null;
	private DeplacementCommune deplacementCommune = null;
	private boolean poserBombe2;

	public DeplacementCollecte(IsbirKoyas ai) throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		this.ai = ai;
	}

	/**
	 * Cette méthode impl�mente l'algorithme de collecte. Elle prend 3 arguments
	 * une matrice de type Int, la zone du jeu et une action.
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
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		DeplacementCommune deplacementCommune = new DeplacementCommune(ai);
		Securite securite = new Securite(ai);
		// La position actuelle de notre hero
		AiTile startPoint = ai.ourHero.getTile();
		// Les positions finales possibles de notre hero calcule par la methode
		// calculateEndPoints
		List<AiTile> endPoints = calculeLesPointsFinaux(matriceCollecte,
				gameZone);
		List<AiTile> endPoints2 = new ArrayList<AiTile>();
		// Si c'est le premier appel par le moteur ou l'agent a complete son
		// objectif
		// On commence a trouver si le hero est arrive au voisinage
		// d'une case mur.

		if (ai.nextMove == null && ai.senfuire == false) {
			ai.checkInterruption();
			try {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				endPoints = calculeLesPointsFinaux(matriceCollecte, gameZone);
				endPoints2 = endPoints;
				if (ai.print)
					System.out.println("Collect Normal:" + endPoints);

				try {
					ai.nextMove = deplacementCommune.cheminLePlusCourt(
							ai.ourHero, startPoint, endPoints);
				} catch (Exception e) {
					if (ai.print)
						System.out.println(e);
				}
				if (ai.nextMove.getLength() == 0)
					ai.nextMove = null;
				if (ai.print)
					System.out.println("NextMove1:" + ai.nextMove);

				// il ne pose pas de bombe pour aller au cas de bonus.
				ai.collectBombe = false;

			} catch (Exception e) {
				if (ai.print)
					System.out.println("Collect:On est bloqu�" + e);
			}
			// Detruire
			if (ai.nextMove == null && ai.senfuire == false) {
				if (ai.print)
					System.out.println("Collect: nextMove==NULL => DETRUIRE");
				endPoints = deplacementCommune.detruire(gameZone, resultat,
						endPoints2);
				if (ai.print)
					System.out.println("Detuire End Points:" + endPoints);
				if (!endPoints.isEmpty()) {
					ai.nextMove = deplacementCommune.cheminLePlusCourt(
							ai.ourHero, startPoint, endPoints);
					if (ai.print)
						System.out.println("Collect: DETRUIRE nextMove"
								+ ai.nextMove);
				}
				// il pose une bombe pour detruire le mur destructible.
				ai.collectBombe = true;
			}
		}

		if (ai.nextMove != null) {

			if (ai.print)
				System.out.println("Collect: nextMove!=NULL");
			if (ai.nextMove.getLength() == 0) {
				if (ai.print)
					System.out.println("Collect: NextMove.lenght()==0"
							+ ai.nextMove);
				ai.nextMove = null;
			} else {
				if (ai.print)
					System.out.println("Collect: NextMove.length()!=0"
							+ ai.nextMove);
				// Si le joueur est arrive au case suivant
				if ((ai.ourHero.getLine() == ai.nextMove.getTile(0).getLine())
						&& (ai.ourHero.getCol() == ai.nextMove.getTile(0)
								.getCol())) {
					// On enleve cette cases da la liste des cases suivantes
					ai.nextMove.getTiles().remove(0);
					// Si la liste est vide, alors l'objectif est obtenu et
					// il n'y a pas plus de cases a suivre
					if (ai.nextMove.getTiles().isEmpty()) {
						ai.nextMove = null;
						// si on s'enfuit, on va l'arreter, parce qu'on a deja
						// suivi le chemin
						if (ai.senfuire == true) {
							ai.senfuire = false;
						}
						if (ai.senfuire2 == true) {
							ai.senfuire2 = false;
						}

						if (ai.poserBombe == true && ai.senfuire == false
								&& ai.collectBombe == true
								&& ai.senfuire2 == false) {
							// il controle si il peut senfuire
							if (!securite.plein(gameZone, ai.ourHero.getTile())) {

								if (ai.print)
									System.out
											.println("Collect: IA pense à poser bombe!");
								poserBombe2 = true;
								List<AiTile> casSurs = securite
										.posageControle(gameZone);
								if (ai.print)
									System.out.println("casSurs: " + casSurs);
								if (casSurs.isEmpty())
									poserBombe2 = false;
								else {
									ai.nextMove2 = deplacementCommune
											.cheminLePlusCourt(ai.ourHero,
													ai.ourHero.getTile(),
													casSurs);
									if (ai.nextMove2.isEmpty())
										poserBombe2 = false;
									else
										poserBombe2 = true;
									if (ai.print)
										System.out.println("Collect SENFUIRE:"
												+ ai.nextMove2);
									long temps = ai.ourHero.getBombPrototype()
											.getNormalDuration();
									long temps2 = ai.nextMove2
											.getDuration(ai.ourHero);
									if (temps2 > temps) {
										if (ai.print)
											System.out
													.println("Il n'y a pas du temps à senfuire.");
										poserBombe2 = false;
									}
								}
								// si il peut, en fait il pose une bombe
								if (poserBombe2) {
									resultat = new AiAction(
											AiActionName.DROP_BOMB);
									if (ai.print)
										System.out
												.println("Collect: IA a pose la bombe!");
									// il va s'enfuire, il va faire un nouveau
									// mouvement, donc nextMove=null
									ai.nextMove = null;
									ai.senfuire = true;
									senfuireApresPosage();
								}
								// si il ne pose pas de bombe, il va calculer un
								// nouveau cible, donv nextMove=null
								else {
									if (ai.print)
										System.out
												.println("Collect: On ne pose pas de bombe. On change NextMove=null");
									ai.nextMove = null;
								}
							}
						}
					} else {

						if (ai.print) {
							System.out
									.println(": 1) On n'arrive pas à la fin du chemin");
							System.out.println("Collect: 1) senfuire2:"
									+ ai.senfuire2 + "ai.senfuire:"
									+ ai.senfuire);
						}
						// si on ne s'enfuit pas:

						if (!ai.senfuire && !ai.senfuire2) {

							// on calcule si notre case cible est chang�e
							if (!endPoints.contains(ai.nextMove.getLastTile())
									&& !endPoints.isEmpty()) {
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
											.println("Collect: 1) nextMove est secure");
								if (!ai.nextMove.getFirstTile().getFires()
										.isEmpty())
									resultat = new AiAction(AiActionName.NONE);
								else
									resultat = deplacementCommune
											.newAction(ai.nextMove);
								if (ai.print)
									System.out.println("ESSAI1");
							}
							// si le chemin nest pas secure
							else {

								// ai.nextMove=null;
								// senfuire des bombes des ennemis
								ai.checkInterruption();
								if (!ai.nextMove.getFirstTile().getBombs()
										.isEmpty()) {
									AiBomb bombe = ai.nextMove.getFirstTile()
											.getBombs().get(0);
									if (ai.print)
										System.out
												.println("Collect: 1) Il y a une bombe au NextMove");
									List<AiTile> casSurs2 = securite
											.bombeControle(gameZone, bombe);
									if (ai.print)
										System.out
												.println("Collect: 1) casSurs2:"
														+ casSurs2);
									if (!casSurs2.isEmpty()) {
										ai.nextMove3 = deplacementCommune
												.cheminLePlusCourt(ai.ourHero,
														ai.ourHero.getTile(),
														casSurs2);
										ai.senfuire2 = true;
										ai.nextMove = ai.nextMove3;
										if (ai.print)
											System.out
													.println("Collect: 1) nextMove:"
															+ ai.nextMove);
									}
								} else
									ai.senfuire2 = false;

								if (!ai.nextMove.getFirstTile().getFires()
										.isEmpty())
									resultat = new AiAction(AiActionName.NONE);
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
								if (ai.print)
									System.out.println("ESSAI3");
								resultat = deplacementCommune
										.newAction(ai.nextMove);
								return resultat;
							}
							// senfuit des bombes des ennemis
							ai.checkInterruption();
							if (!ai.nextMove.getFirstTile().getBombs()
									.isEmpty()) {
								AiBomb bombe = ai.nextMove.getFirstTile()
										.getBombs().get(0);
								if (ai.print)
									System.out
											.println("Collect: 1) Il y a une bombe au NextMove");
								List<AiTile> casSurs2 = securite.bombeControle(
										gameZone, bombe);
								if (ai.print)
									System.out.println("Collect: 1) casSurs2:"
											+ casSurs2);
								if (!casSurs2.isEmpty()) {
									ai.nextMove3 = deplacementCommune
											.cheminLePlusCourt(ai.ourHero,
													ai.ourHero.getTile(),
													casSurs2);
									ai.senfuire2 = true;
									ai.nextMove = ai.nextMove3;
									if (ai.print)
										System.out
												.println("Collect: 1) nextMove:"
														+ ai.nextMove);
								}
							} else
								ai.senfuire2 = false;

							if (!ai.nextMove.getFirstTile().getFires()
									.isEmpty())
								resultat = new AiAction(AiActionName.NONE);
						}
					}
				}
				// si on n'arrive pas à la case suivant
				else {

					if (ai.print) {
						System.out
								.println("Collect: 2) On n'arrive pas à la fin du chemin");
						System.out.println("Collect: 2) senfuire2:"
								+ ai.senfuire2);
					}
					// si on ne s'enfuit pas:
					if (!ai.senfuire && !ai.senfuire2) {
						// si le chemin est secure
						if (securite.nextMoveSecurite(ai.ourHero, gameZone,
								ai.nextMove)) {
							if (ai.print)
								System.out
										.println("Collect: 1) nextMove est secure");
							if (!ai.nextMove.getFirstTile().getFires()
									.isEmpty())
								resultat = new AiAction(AiActionName.NONE);
							else
								resultat = deplacementCommune
										.newAction(ai.nextMove);
							if (ai.print)
								System.out.println("ESSAI4");
						}
						// si le chemin nest pas secure
						else {
							ai.checkInterruption();
							if (!ai.nextMove.getFirstTile().getBombs()
									.isEmpty()) {
								AiBomb bombe = ai.nextMove.getFirstTile()
										.getBombs().get(0);
								if (ai.print)
									System.out
											.println("Collect: 1) Il y a une bombe au NextMove");
								List<AiTile> casSurs2 = securite.bombeControle(
										gameZone, bombe);
								if (ai.print)
									System.out.println("Collect: 1) casSurs2:"
											+ casSurs2);
								if (!casSurs2.isEmpty()) {
									ai.nextMove3 = deplacementCommune
											.cheminLePlusCourt(ai.ourHero,
													ai.ourHero.getTile(),
													casSurs2);
									ai.senfuire2 = true;
									ai.nextMove = ai.nextMove3;
									if (ai.print)
										System.out
												.println("Collect: 1) nextMove:"
														+ ai.nextMove);
								}
							} else
								ai.senfuire2 = false;
							if (!ai.nextMove.getFirstTile().getFires()
									.isEmpty())
								resultat = new AiAction(AiActionName.NONE);
							if (ai.print)
								System.out.println("ESAAI5");
						}

					}
					// si on s'enfuit:
					else {
						// controle du feu avec IsCrossableBy
						if (ai.nextMove.getFirstTile()
								.isCrossableBy(ai.ourHero)) {
							ai.checkInterruption(); // APPEL OBLIGATOIRE
							if (ai.print)
								System.out.println("ESSAI6");
							resultat = deplacementCommune
									.newAction(ai.nextMove);
							return resultat;
						}
						ai.checkInterruption();
						if (!ai.nextMove.getFirstTile().getBombs().isEmpty()) {
							AiBomb bombe = ai.nextMove.getFirstTile()
									.getBombs().get(0);
							if (ai.print)
								System.out
										.println("Collect: 1) Il y a une bombe au NextMove");

							List<AiTile> casSurs2 = securite.bombeControle(
									gameZone, bombe);
							if (ai.print)
								System.out.println("Collect: 1) casSurs2:"
										+ casSurs2);
							if (!casSurs2.isEmpty()) {
								ai.nextMove3 = deplacementCommune
										.cheminLePlusCourt(ai.ourHero,
												ai.ourHero.getTile(), casSurs2);
								ai.senfuire2 = true;
								ai.nextMove = ai.nextMove3;
								if (ai.print)
									System.out.println("Collect: 1) nextMove:"
											+ ai.nextMove);
							}
						} else
							ai.senfuire2 = false;
						if (!ai.nextMove.getFirstTile().getFires().isEmpty())
							resultat = new AiAction(AiActionName.NONE);
					}
				}
			}
		}
		if (ai.nextMove != null)
			if (ai.nextMove.getFirstTile() == ai.ourHero.getTile())
				ai.nextMove.removeTile(0);
		return resultat;
	}

	/**
	 * Cette méthode forme une liste des cases cibles que notre IA peut aller
	 * dans le mode collecte. Elle prend deux arguments, une matrice de type Int
	 * et la zone du jeu. On prend aussi en compte les cases qui sont dans la
	 * port�e des bombes.
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
		if (ai.print)
			System.out.println("Collect: EndPoints:" + endPoints);

		List<AiTile> endPoints2 = new ArrayList<AiTile>();

		try {
			destinationCollect(endPoints, endPoints2, matrice);
			endPoints = endPoints2;
		} catch (Exception e) {
			// 
			e.printStackTrace();
		}
		if (ai.print)
			System.out.println("Collect: Destination EndPoints:" + endPoints2);
		return endPoints;
	}

	/**
	 * Cette méthode compare les cases cibles entre eux et retourne la case qui
	 * a la valeur plus �lev�e. S�il y a plus d�une valeur �lev�e �gales alors
	 * IA regarde au distance de ceux cases .Elle prend trois arguments une
	 * matrice de type Int et deux listes des cases.
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
		ai.checkInterruption(); // APPEL OBLIGATOIRE
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
	/**
	 * Cette méthode permet a s'enfuire apres le posage de bombe
	 * 
	 * @throws StopRequestException
	 */
	public void senfuireApresPosage() throws StopRequestException {
		ai.checkInterruption();
		if (ai.nextMove == null && ai.senfuire == true) {
			// alors notre IA a pose sa bombe donc
			// il doit
			// s'enfuir
			if (ai.print)
				System.out.println("Collect: nextMove==NULL => SENFUIRE");
			try {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				ai.nextMove = ai.nextMove2;
				if (ai.print)
					System.out.println("NextMove3: SENFUIRE" + ai.nextMove);

				ai.collectBombe = false;
			} catch (Exception e) {
				if (ai.print)
					System.out.println("Collect:On ne peut pas senfuir");
			}
		}
	}

	// LES METHODES D'ACCES
	/**
	 * METHODE D'ACCES a la classe Securite
	 * 
	 * @throws StopRequestException
	 * @return securite
	 */
	public Securite Securite() throws StopRequestException {
		ai.checkInterruption();
		return securite;

	}

	/**
	 * METHODE D'ACCES a la classe DeplacementCommune
	 * 
	 * @throws StopRequestException
	 * @return deplacementcommune
	 */
	public DeplacementCommune DeplacementCommune() throws StopRequestException {
		ai.checkInterruption();
		return deplacementCommune;
	}
}

package org.totalboumboum.ai.v201011.ais.isbirkoyas.v6;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
/**constructeur
 * 
 * @author Göksu İsbir
 * @author Ela Koyaş
 */
@SuppressWarnings("deprecation")
public class DeplacementCollecte {
	/** */
	IsbirKoyas ai = new IsbirKoyas();
	/** */
	private Securite securite = null;
	/** */
	private DeplacementCommune deplacementCommune = null;
	/** */
	private TraitementCommune traitementCommune=null;
	/** */
	private boolean poserBombe2;

	/**
	 * 
	 * @param ai
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public DeplacementCollecte(IsbirKoyas ai) throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		this.ai = ai;
	}

	/**
	 * Cette méthode implémente l'algorithme de collecte. Elle prend 3 arguments
	 * une matrice de type Int, la zone du jeu et une action.
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @param matriceCollecte
	 *            la matrice collecte
	 * @param resultat 
	 * 		description manquante !
	 * @return
	 * 		?
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public AiAction algorithmCollect(int[][] matriceCollecte, AiZone gameZone,
			AiAction resultat) throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		DeplacementCommune deplacementCommune = new DeplacementCommune(ai);
		TraitementCommune traitementCommune = new TraitementCommune(ai);
		Securite securite = new Securite(ai);
		// La position actuelle de notre hero
		AiTile startPoint = ai.ourHero.getTile();
		// Les positions finales possibles de notre hero calcule par la methode
		// calculateEndPoints
		List<AiTile> endPoints = traitementCommune.calculeLesPointsFinaux(matriceCollecte,
				gameZone);
		List<AiTile> endPoints2 = new ArrayList<AiTile>();
		// Si c'est le premier appel par le moteur ou l'agent a complete son
		// objectif
		// On commence a trouver si le hero est arrive au voisinage
		// d'une case mur.

		if (ai.nextMove == null && ai.senfuire == false) {
		
			
				endPoints = traitementCommune.calculeLesPointsFinaux(matriceCollecte, gameZone);
				endPoints2 = endPoints;
				if (ai.print)
					System.out.println("Collect Normal:" + endPoints);

					ai.nextMove = deplacementCommune.cheminLePlusCourt(
							ai.ourHero, startPoint, endPoints);
				if (ai.nextMove.getLength() == 0)
					ai.nextMove = null;
				if (ai.print)
					System.out.println("NextMove1:" + ai.nextMove);

				// il ne pose pas de bombe pour aller au cas de bonus.
				ai.collectBombe = false;
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
									
									
									List <AiTile> voisins= new ArrayList<AiTile>();
									if(!ai.nextMove2.isEmpty())
									{
										for (int k = 0; k < ai.nextMove2.getLength(); k++) 
										{		ai.checkInterruption(); // APPEL OBLIGATOIRE																
											voisins=ai.nextMove2.getTile(k).getNeighbors();
											voisins=ai.nextMove2.getFirstTile().getNeighbors();
											for (int j = 0; j < voisins.size(); j++) {
												ai.checkInterruption(); // APPEL OBLIGATOIRE
												if(!voisins.get(j).getBombs().isEmpty())
												{
													long temps3= voisins.get(j).getBombs().get(0).getNormalDuration()-voisins.get(j).getBombs().get(0).getTime();
													if(temps3<temps)
														poserBombe2 = false;
												}
												
											}
										}
									}
										
									
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
							// senfuire des bombes des ennemis
								

								List <AiTile> blasts= new ArrayList<AiTile>();
								List<AiBomb> bombes = gameZone.getBombs();	
								List<AiBomb> bombesDanger= new ArrayList<AiBomb>();
								long min=1000000;

								// on prend les blasts des bombes qui contient notre case suivante
								// on trouve le bombe qui va exploser plus avante.
								if(!bombes.isEmpty())
								{
									for (int i = 0; i < bombes.size(); i++) {
										ai.checkInterruption(); // APPEL OBLIGATOIRE
										AiBomb bombe=bombes.get(i);
										if(bombe.getBlast().contains(ai.nextMove.getFirstTile()))
											{
												long temps=bombe.getNormalDuration()-bombe.getTime();
												if(temps<min)
												{
													bombesDanger.add(bombe);
													blasts.addAll(bombe.getBlast());
													min=temps;
												}
											}
									}															
							
									if(blasts.contains(ai.nextMove.getFirstTile())&& !blasts.contains(ai.ourHero.getTile()))
									{	
											resultat = new AiAction(AiActionName.NONE);									
									}
									
									else if(blasts.contains(ai.nextMove.getFirstTile())&& blasts.contains(ai.ourHero.getTile()) )
									{
										AiBomb bombe= bombesDanger.get(0);
										if (ai.print)
											System.out
													.println("Attaque: 1) Il y a une bombe");
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
										if(ai.nextMove.getLength()!=0)
											resultat=deplacementCommune.newAction(ai.nextMove);
									}
									else
									{
										ai.senfuire2 = false;
										resultat=deplacementCommune.newAction(ai.nextMove);
									}
							
								}
															
								else {
								
									ai.senfuire2 = false;
									resultat= deplacementCommune.newAction(ai.nextMove);
								}
								if(!ai.nextMove.isEmpty())
								if (!ai.nextMove.getFirstTile().getFires()
										.isEmpty() ) {
									
									resultat = new AiAction(AiActionName.NONE);
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
							if(!ai.nextMove.isEmpty())
							if (!ai.nextMove.getFirstTile().getFires()
									.isEmpty())
								resultat = new AiAction(AiActionName.NONE);
						}
					}
				}
				// si on n'arrive pas à la case suivant
				else {

					// si on ne s'enfuit pas:
					if (!ai.senfuire && !ai.senfuire2) {
							// senfuire des bombes des ennemis
							

							List <AiTile> blasts= new ArrayList<AiTile>();
							List<AiBomb> bombes = gameZone.getBombs();	
							List<AiBomb> bombesDanger= new ArrayList<AiBomb>();
							long min=1000000;

							// on prend les blasts des bombes qui contient notre case suivante
							// on trouve le bombe qui va exploser plus avante.
							if(!bombes.isEmpty())
							{
								for (int i = 0; i < bombes.size(); i++) {
									ai.checkInterruption(); // APPEL OBLIGATOIRE
									AiBomb bombe=bombes.get(i);
									if(bombe.getBlast().contains(ai.nextMove.getFirstTile()))
										{
											long temps=bombe.getNormalDuration()-bombe.getTime();
											if(temps<min)
											{
												bombesDanger.add(bombe);
												blasts.addAll(bombe.getBlast());
												min=temps;
											}
										}
								}															
						
								if(blasts.contains(ai.nextMove.getFirstTile())&& !blasts.contains(ai.ourHero.getTile()))
								{	
										resultat = new AiAction(AiActionName.NONE);									
								}
								
								else if(blasts.contains(ai.nextMove.getFirstTile())&& blasts.contains(ai.ourHero.getTile()) )
								{
									AiBomb bombe= bombesDanger.get(0);
									if (ai.print)
										System.out
												.println("Attaque: 1) Il y a une bombe");
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
									if(ai.nextMove.getLength()!=0)
										resultat=deplacementCommune.newAction(ai.nextMove);
								}
								 else
									{
										ai.senfuire2 = false;
										resultat=deplacementCommune.newAction(ai.nextMove);
									}						
							}														
							else {
								
								ai.senfuire2 = false;
								resultat= deplacementCommune.newAction(ai.nextMove);
							}
							
							if(!ai.nextMove.isEmpty())
							if (!ai.nextMove.getFirstTile().getFires()
									.isEmpty() ) {
								
								resultat = new AiAction(AiActionName.NONE);
							}
					}
					// si on s'enfuit:
					else {
						// controle du feu avec IsCrossableBy
						if (ai.nextMove.getFirstTile()
								.isCrossableBy(ai.ourHero)) {
							
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
						if(!ai.nextMove.isEmpty())
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
	 * Cette méthode permet a s'enfuire apres le posage de bombe
	 * 
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public void senfuireApresPosage() throws StopRequestException {
		ai.checkInterruption();
		if (ai.nextMove == null && ai.senfuire == true) {
			// alors notre IA a pose sa bombe donc
			// il doit
			// s'enfuir
			if (ai.print)
				System.out.println("Collect: nextMove==NULL => SENFUIRE");	
				
				ai.nextMove = ai.nextMove2;
				if (ai.print)
					System.out.println("NextMove3: SENFUIRE" + ai.nextMove);
				ai.collectBombe = false;
		}
	}

	// LES METHODES D'ACCES
	/**
	 * METHODE D'ACCES a la classe Securite
	 * 
	 * @throws StopRequestException
	 * 		description manquante !
	 * @return securite
	 */
	public Securite Securite() throws StopRequestException {
		ai.checkInterruption();
		return securite;

	}
	/**
	 * METHODE D'ACCES a la classe TraitementCommune
	 * 
	 * @throws StopRequestException
	 * 		description manquante !
	 * @return deplacement commune
	 */
	public TraitementCommune traitementCommune() throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE
		return traitementCommune;
	}
	/**
	 * METHODE D'ACCES a la classe DeplacementCommune
	 * 
	 * @throws StopRequestException
	 * 		description manquante !
	 * @return deplacementcommune
	 */
	public DeplacementCommune DeplacementCommune() throws StopRequestException {
		ai.checkInterruption();
		return deplacementCommune;
	}
}

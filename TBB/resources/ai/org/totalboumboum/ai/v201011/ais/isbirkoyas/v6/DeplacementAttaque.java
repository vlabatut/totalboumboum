package org.totalboumboum.ai.v201011.ais.isbirkoyas.v6;

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
	private TraitementCommune traitementCommune=null;
	boolean poserBombe2;
	boolean attaque = false;
	boolean detruire = false;
	boolean blockage = false;

	public DeplacementAttaque(IsbirKoyas ai) throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		this.ai = ai;
	}
	/**
	 * Cette méthode implémente l'algorithme d'attaque. Elle prend 3 arguments
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
			AiZone gameZone, AiAction resultat) throws StopRequestException  {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		TraitementCommune traitementCommune =new TraitementCommune(ai);
		DeplacementCommune deplacementCommune = new DeplacementCommune(ai);
		Securite securite = new Securite(ai);
		AiTile startPoint = ai.ourHero.getTile();
		// Les positions finales possibles de notre hero calcule par la methode
		// calculateEndPoints
		List<AiTile> endPoints = traitementCommune.calculeLesPointsFinaux(matriceAttaque,
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
				ai.nextMove = deplacementCommune.cheminLePlusCourt(ai.ourHero,
						startPoint, heroTiles);
				if (ai.nextMove.getLength() == 0 && ai.nextMove!=null)
					ai.nextMove = null;
				if (ai.nextMove != null) {
					
					endPoints = traitementCommune.calculeLesPointsFinaux(matriceAttaque, gameZone);
					endPoints2 = endPoints;
					if (ai.print)
						System.out.println("Attaque: NORMAL EndPonits: "
								+ endPoints);
						ai.nextMove = deplacementCommune.cheminLePlusCourt(
								ai.ourHero, startPoint, endPoints);				
					if (ai.print)
						System.out.println("Attaque: NORMAL NextMove:"
								+ ai.nextMove);
					if (ai.nextMove.getLength() == 0) {
						
						ai.nextMove = null;
					}
					ai.attaqueBombe = true;
					detruire = false;
					attaque = true;
				}
			// si on ne peut pas aller a cette case, ça veut dire on est bloque
			// Donc il faut detruire les murs
			// Detruire
			if (ai.nextMove == null && ai.senfuire == false && !blockage) {
				
				if (ai.print)
					System.out.println("Attaque: nextMove==NULL => DETRUIRE");
				endPoints2 = endPoints;
				endPoints = deplacementCommune.detruire(gameZone, resultat,
						endPoints2);
				if (!endPoints.isEmpty()) {
					
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
			
			if (ai.print)
				System.out.println("Attaque: nextMove!=NULL");
			if (ai.nextMove.getLength() == 0) {
				
				if (ai.print)
					System.out.println("Attaque: NextMove.lenght()==0"
							+ ai.nextMove + "senfuire:" + ai.senfuire);
				ai.nextMove = null;
			} else {
				
				if (ai.print)
					System.out.println("Attaque: NextMove.length()!=0"
							+ ai.nextMove);
				// Si le joueur est arrive au case suivant
				if ((ai.ourHero.getLine() == ai.nextMove.getTile(0).getLine())
						&& (ai.ourHero.getCol() == ai.nextMove.getTile(0)
								.getCol())) {
					
					// On enleve cette cases da la liste des cases suivantes
					// nextMove
					ai.nextMove.getTiles().remove(0);
					// Si la liste est vide, alors l'objectif est obtenu et
					// il n'y a pas plus de cases a suivre
					if (ai.nextMove.getTiles().isEmpty()) {
					
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
								List <AiTile> voisins= new ArrayList<AiTile>();
								if(!ai.nextMove2.isEmpty())
								{
									for (int k = 0; k < ai.nextMove2.getLength(); k++) 
									{		ai.checkInterruption(); // APPEL OBLIGATOIRE																		
										voisins=ai.nextMove2.getTile(k).getNeighbors();
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
									ai.checkInterruption(); // APPEL
															// OBLIGATOIRE
									if (ai.print)
										System.out
												.println("Il n'y a pas du temps ï¿½ senfuire.");
									poserBombe2 = false;
								}
							}
								if (poserBombe2) {
							
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
								
									if (ai.print)
										System.out
												.println("Attaque: On ne pose pas de bombe. On change NextMove=null");
									ai.nextMove = null;

								}
							}
						}
					} else {

						// on n'arrive pas a la fin du chemin
						
						// si on ne s'enfuit pas:
						if (!ai.senfuire && !ai.senfuire2) {
						

							// on calcule si notre case cible est changée
							if (!endPoints.contains(ai.nextMove.getLastTile())
									&& !endPoints.isEmpty()) {
							
								AiPath path = deplacementCommune
										.cheminLePlusCourt(ai.ourHero,
												startPoint, endPoints);
								if (!path.isEmpty())
										//&& securite.nextMoveSecurite(
											//	ai.ourHero, gameZone, path))
									ai.nextMove = path;
							}

							// si le chemin nest pas secure on va senfuire s'il
							// y a une bombe
							//else {
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
										.isEmpty()) {
								
									resultat = new AiAction(AiActionName.NONE);
								}
								if (ai.print)
									System.out.println("ESSAI2");
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
							}
							// senfuit des bombes des ennemis
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
								
								ai.senfuire2 = false;
							}
							if(!ai.nextMove.isEmpty())
							if (!ai.nextMove.getFirstTile().getFires()
									.isEmpty() ) {
								
								resultat = new AiAction(AiActionName.NONE);
							}
						}
					}
				}

				// si on n'arrive pas à la case suivant
				else {
				
					if (ai.print) {
						System.out
								.println("Attaque: 2) On n'arrive pas a la fin du chemin");
						System.out.println("Attaque: 2) senfuire2:"
								+ ai.senfuire2);
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
					// si on s'enfuit:
					else {
						
						// controle du feu avec IsCrossableBy
						if (ai.nextMove.getFirstTile()
								.isCrossableBy(ai.ourHero)) {
						
							resultat = deplacementCommune
									.newAction(ai.nextMove);
						}
						ai.checkInterruption();
						if (!ai.nextMove.getFirstTile().getBombs().isEmpty()) {
						
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
						if(!ai.nextMove.isEmpty())
						if (!ai.nextMove.getFirstTile().getFires().isEmpty() ) {
						
							resultat = new AiAction(AiActionName.NONE);
						}

					}
				}
			}
		}
		
		if (ai.nextMove != null)
			if (ai.nextMove.getFirstTile() == ai.ourHero.getTile()) {
				List <AiHero> hereos=gameZone.getRemainingHeroes();
				List <AiTile> hereosTiles= new ArrayList<AiTile>();
				for (int i = 0; i < hereos.size(); i++) {
					ai.checkInterruption(); // APPEL OBLIGATOIRE
					hereosTiles.add(hereos.get(i).getTile());
				}
				if(!hereosTiles.contains(ai.ourHero.getTile()))
				{
					
					ai.nextMove.removeTile(0);
				}
			}
			
		return resultat;
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

				
				ai.nextMove = ai.nextMove2;
				if (ai.print)
					System.out.println("Attaque: SENFUIRE NextMove:"
							+ ai.nextMove);
				ai.attaqueBombe = false;
				if (ai.print)
					System.out
							.println("Attaque: SENFUIRE: On ne peut pas senfuir");
			
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
	/**
	 * METHODE D'ACCES a la classe TraitementCommune
	 * 
	 * @throws StopRequestException
	 * @return Traitementcommune
	 */
	public TraitementCommune traitementCommune() throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE
		return traitementCommune;
	}
}

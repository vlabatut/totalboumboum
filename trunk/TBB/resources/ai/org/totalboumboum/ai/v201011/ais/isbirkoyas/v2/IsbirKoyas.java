package org.totalboumboum.ai.v201011.ais.isbirkoyas.v2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**   
 * @author Ela Koyas
 * @author Goksu Isbir
 *
 */
public class IsbirKoyas extends ArtificialIntelligence {
	private AiPath nextMove=null;
	private AiHero ourHero;
	private final int BONUS=1000; 
	private final int ATTAQUE=1;
	private int bonus_init;
	private int destructible_init=200;
	private boolean mode_collect=false;
	private boolean mode_attaque=false;
	boolean poserbombe;
	boolean poserbombe2;
	boolean danger, plein;
	int i=0;
	
	/** méthode appelée par le moteur
	 *  du jeu pour obtenir une action de notre IA 
	 *  */
	public AiAction processAction() throws StopRequestException {
		// avant tout: test d'interruption
		checkInterruption();
		//La perception instantanement de l'environnement
		AiZone gameZone = getPercepts();
		//la longueur de la zone
		int width = gameZone.getWidth();
		//la largeur de la zone
		int height = gameZone.getHeight();
		AiAction resultat=new AiAction(AiActionName.NONE);
		//Notre hero dans cette zone
		this.ourHero = gameZone.getOwnHero();
		//la matrice de collecte
		int[][] matricecollect = new int[height][width];
		//la matrice d'attaque
		double[][] matriceattaque=new double[height][width];
		danger=false;
		poserbombe2=false;
		poserbombe=false;

		//Détermination du mode
		mode(gameZone);
		
		// si le mode: collecte
		if(mode_collect)	
		{
			checkInterruption();
			bonus_init=BONUS;
		
			System.out.println("le mode collecte");
			
			//La desicion du posage de bombe
			DecidePoserBombe_Collect(gameZone);
			//Initialise la matrice collecte
			this.initialiseMatrice(matricecollect, gameZone);
			//Calcul de la matrice collecte
			this.collect_matrice(matricecollect,gameZone);	
			//Affiche la matrice collecte
			this.affiche(matricecollect, gameZone);
			
			resultat = AlgorithmCollect(matricecollect,gameZone,resultat);
		}
		//si le mode : attaque
		if(mode_attaque)
		{			
			checkInterruption();
			System.out.println("le mode attaque");
			
			//La desicion du posage de bombe
			DecidePoserBombe_Attaque(gameZone);
			//Initialise la matrice attaque
			this.initialiseMatrice(matriceattaque,gameZone);
			//Calcul de la matrice attaque
			this.attaque_matrice(matriceattaque, gameZone);		
		
			
			resultat=AlgorithmAttaque(matriceattaque,gameZone,resultat);
			
			//Affiche la matrice collecte
			this.affiche(matriceattaque, gameZone);
		}
		return resultat;
	}

	/**
	 * Methode implementant l'algorithme de collecte.
	 * 
	 * @param gameZone la zone du jeu
	 * @throws StopRequestException
	 */
	private AiAction AlgorithmCollect(int[][] matricecollect, AiZone gameZone, AiAction resultat)throws StopRequestException 
	{
		checkInterruption();		
		//La position actuelle de notre hero
		AiTile startPoint = ourHero.getTile();			 
		checkInterruption();
		//Les positions finales possibles de notre hero calcule par la methode calculateEndPoints
		List<AiTile> endPoints = this.calcule_les_pointsFinaux(matricecollect, gameZone);		
		//Si c'est le premier appel par le moteur ou l'agent a complete son objectif
		// On commence a trouver si le hero est arrive au voisinage
		// d'une case mur.		
		
			if(this.nextMove==null)
			{   
				System.out.println("Collect: nextMove=NULL");
				//Le nouveau chemin le plus court est calcule utilisant la methode 
				//calculateShortestPath
				this.nextMove = this.chemin_le_plus_court(ourHero,startPoint,endPoints);
				System.out.println("Collect: on sort de shortestpath");
				if (nextMove.getLength() == 0)
				{
					nextMove = null;
					DESTUIRE(gameZone, resultat);
					System.out.println("Collect: la longueur du nextMove est 0 ");
				}
			}

			//Si non 			 
			else
			{
				System.out.println("Collect: nextMove!=NULL");
				if (nextMove.getLength() == 0)
				{
					nextMove = null;
					DESTUIRE(gameZone,resultat);
				}
				else
				{
					//Si le joueur est arrive au case suivant
					if((this.ourHero.getLine()==this.nextMove.getTile(0).getLine()) &&
							(this.ourHero.getCol()==this.nextMove.getTile(0).getCol()))
					{
						//On enleve cette cases da la liste des cases suivantes nextMove								
						this.nextMove.getTiles().remove(0);										
						//Si la liste est vide, alors l'objectif est obtenu et
						//il n'y a pas plus de cases a suivre
						if(this.nextMove.getTiles().isEmpty())
						{
							this.nextMove=null;
							if(poserbombe==true )//&& gameZone.getItems().isEmpty())
							{
								if(!Plein(gameZone,ourHero.getTile()))
								{								
							    	System.out.println("Collect: Poser bombe!");								
									resultat = new AiAction(AiActionName.DROP_BOMB);									
									System.out.println("Collect: IA a pose la bombe!");
						
									// si le terrain est plein alors notre IA a pose sa bombe donc il doit s'enfuir
									if(Plein(gameZone,ourHero.getTile()))
									{
										SENFUIRE(gameZone, resultat);
										/*
										if(nextMove.getTile(0).getFires().isEmpty() && nextMove.getTile(0).getBombs().isEmpty()&&nextMove.getTile(0).getFires().isEmpty())
										{
										resultat=this.newAction(nextMove);
										}
										*/
									}
									
								}					
							}
								
							else
							{
									if( !Plein(gameZone,ourHero.getTile())&& nextMove.getTile(0).getBombs().isEmpty()&& nextMove.getTile(0).getFires().isEmpty())
									{
									resultat=this.newAction(nextMove);
									System.out.println("Collect: IA n'a pas pu poser la bombe il passe a nextmove");
									}
							}
						}
					}
					else
					{
						//le controle de securite
						if(nextMove.getTile(0).getBombs().isEmpty()&& nextMove.getTile(0).getFires().isEmpty()&& nextMove.getTile(0).isCrossableBy(ourHero))
						{
							resultat=newAction(nextMove);
							//	poserbombe2=true;
							System.out.println("Collect: nextMove:"+nextMove);
						}
					}	
					
				}
			}
		
			System.out.println("Collect: nextMove:"+nextMove);	
			return resultat;
	}
	/**
	 * Methode implementant l'algorithme d'attaque.
	 * 
	 * @param gameZone la zone du jeu
	 * @throws StopRequestException
	 */
	private AiAction AlgorithmAttaque(double[][] matriceattaque, AiZone gameZone, AiAction resultat)throws StopRequestException 
	{
		checkInterruption();
		AiTile startPoint = ourHero.getTile();			 
		checkInterruption();
		//Les positions finales possibles de notre hero calcule par la methode calculateEndPoints
		List<AiTile>  endPoints2 = this.calcule_les_pointsFinaux(matriceattaque, gameZone);		
		if(this.nextMove==null)
		{
			System.out.println("Attaque: nextMove=NULL");
			//Le nouveau chemin le plus court est calcule utilisant la methode 
			//calculateShortestPath
			this.nextMove = this.chemin_le_plus_court(ourHero,startPoint,endPoints2);
			System.out.println("Attaque: on sort de shortestpath");
			
			if (nextMove.getLength() == 0)
				{
					nextMove = null;
					System.out.println("Attaque: la longueur du nextMove est 0 et DETRUIT");	
					DESTUIRE(gameZone,resultat);
				}
		}
		//Si non 			 
		else
		{
			System.out.println("Attaque: nextMove!=NULL");
			if (nextMove.getLength() == 0)
			{
				nextMove = null;
				DESTUIRE(gameZone,resultat);
			}
			else{
				//Si le joueur est arrive au case suivant
				if((this.ourHero.getLine()==this.nextMove.getTile(0).getLine()) &&
						(this.ourHero.getCol()==this.nextMove.getTile(0).getCol()))
				{
					//On enleve cette cases da la liste des cases suivantes nextMove								
					this.nextMove.getTiles().remove(0);						
					
					//Si la liste est vide, alors l'objectif est obtenu et
					//il n'y a pas plus de cases a suivre
					if(this.nextMove.getTiles().isEmpty())
					{
						this.nextMove=null;
						if(poserbombe==true) //&& gameZone.getItems().isEmpty())
							{
								if(!Plein(gameZone,ourHero.getTile()))
								{								
							    	System.out.println("Attaque: Poser bombe!");								
									resultat = new AiAction(AiActionName.DROP_BOMB);									
									System.out.println("Attaque: IA a pose la bombe!");
								
									// si le terrain est plein alors notre IA a pose sa bombe donc il doit s'enfuir
									if(Plein(gameZone,ourHero.getTile()))
									{
										/*
										if(nextMove.getTile(0).getFires().isEmpty() && nextMove.getTile(0).getBombs().isEmpty()&&nextMove.getTile(0).getFires().isEmpty())
										{
										resultat=this.newAction(nextMove);
										}
										*/
										SENFUIRE(gameZone, resultat);
									}
							
								}					
							}							
						else
						{
							if( !Plein(gameZone,ourHero.getTile())&& nextMove.getTile(0).getBombs().isEmpty()&&nextMove.getTile(0).getFires().isEmpty())
							{
								resultat=this.newAction(nextMove);
								System.out.println("Attaque: IA n'a pas pu poser la bombe il passe a nextmove");
							}
						}
					}
				}
				else
				{
					//le controle de securite
					if(nextMove.getTile(0).getBombs().isEmpty()&&nextMove.getTile(0).getFires().isEmpty()&& nextMove.getTile(0).isCrossableBy(ourHero) )
					{
						resultat=newAction(nextMove);
						//	poserbombe2=true;
						System.out.println("Attaque: nextMove:"+nextMove);
					}
				}	
				
			}
		}
		return resultat;
		
	}
	/**
	 * Methode  qui fait la decision du mode collecte ou attaque? 
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	private void mode(AiZone gameZone) throws StopRequestException 
	{
		checkInterruption();
		if(ourHero.getBombNumberMax()<3)
		{
			if(!gameZone.getItems().isEmpty()){
				System.out.println("IA possede moins de 3 bombe et il y a des bonus sur le terrain donc mode collecte");
				mode_collect=true;
				mode_attaque=false;
			}
			else{
				System.out.println("IA possede moins de 3 bombe et il y n'a pas des bonus sur le terrain donc mode attaque");
				mode_attaque=true;
				mode_collect=false;
			}
		}
		else
		{
			System.out.println("IA possede plus de de 2 bombe donc mode attaque.");
			mode_attaque=true;
			mode_collect=false;
		}
	}
	/**
	 * Methode  qui initialise la matrice collecte avec les '0'. 
	 * 
	 * @param matrice
	 *            La Matrice collecte
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	private void initialiseMatrice(int[][] matrice, AiZone gameZone)
	throws StopRequestException 
	{
		checkInterruption();
		for (int i = 0; i < gameZone.getHeight(); i++) 
		{
			checkInterruption();
			for (int j = 0; j < gameZone.getWidth(); j++) 
			{
				checkInterruption();
				matrice[i][j] = 0;
			}
		}
	}
	/**
	 * Methode  qui initialise la matrice attaque avec les '0'. 
	 * 
	 * @param matrice
	 *            La Matrice collecte
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	private void initialiseMatrice(double[][] matrice, AiZone gameZone)
	throws StopRequestException 
	{
		checkInterruption();
		for (int i = 0; i < gameZone.getHeight(); i++) 
		{
			checkInterruption();
			for (int j = 0; j < gameZone.getWidth(); j++) 
			{
				checkInterruption();
				matrice[i][j] = 0;
			}
		}
	}
	 /**
	 * Methode qui remplie la matrice collecte avec les bonus en fonction du temps
	 * 
	 * @param matrice
	 *            La Matrice de collecte
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */  
	
	private void collect_matrice(int[][] matrice, AiZone gameZone)
	throws StopRequestException 
	{
		checkInterruption();
		gameZone = getPercepts();
		Collection<AiItem> items = gameZone.getItems();
		Iterator<AiItem> iteratorItems = items.iterator();
		Collection<AiBlock> blocks = gameZone.getBlocks();
		Iterator<AiBlock> iteratorBlocks = blocks.iterator();
		AiBlock block = iteratorBlocks.next();	
		Collection<AiTile> neighbours = block.getTile().getNeighbors();
		Iterator<AiTile> iteratorNeighbours = neighbours.iterator();
		while (iteratorItems.hasNext() && iteratorBlocks.hasNext()&&iteratorNeighbours.hasNext()) 
		{
			checkInterruption();
			AiItem item = iteratorItems.next();
			matrice[item.getLine()][item.getCol()]=bonus_init;
			plus_proche_bombe(gameZone,item,matrice);
			plus_proche_Hero(matrice, gameZone,item);	
			checkInterruption();
			
			if(block.isDestructible())
			{
					checkInterruption();
					AiTile neighbour = iteratorNeighbours.next();
					if(neighbour.getBlocks().isEmpty()&& neighbour.getBombs().isEmpty())
						matrice[neighbour.getLine()][neighbour.getCol()]=destructible_init;
				}
			}
		} 	
			
	 /**
	 * Methode qui remplie la matrice attaque avec les bonus en fonction du temps
	 * 
	 * @param matrice
	 *            La Matrice de attaque
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */ 
	private void attaque_matrice(double[][] matrice, AiZone gameZone)throws StopRequestException 
	{
		checkInterruption();
		gameZone = getPercepts();
		Collection<AiBomb> bombs = gameZone.getBombs();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		while (iteratorBombs.hasNext()) 
		{
			checkInterruption();
			AiBomb bomb = iteratorBombs.next();
			matrice[bomb.getLine()][bomb.getCol()]=ATTAQUE;					
		} 		
		Collection<AiBlock> blocks = gameZone.getBlocks();
		Iterator<AiBlock> iteratorBlocks = blocks.iterator();
		while (iteratorBlocks.hasNext()) 
		{
			checkInterruption();
			AiBlock block = iteratorBlocks.next();
			matrice[block.getLine()][block.getCol()]=ATTAQUE;					
		} 					
	}
	/**
	 * Methode calculant la liste des cases ou le hero peut aller pour la matrice collecte
	 * . On prend aussi en compte les cases qui sont
	 * dans la portee des bombes. Notre hero peut se deplacer en traversant ces cases.
	 * 
	 * @param matrice La Matrice collecte
	 * @param gameZone la zone du jeu
	 * @return la liste des points finaux.
	 * @throws StopRequestException
	 */
	private List<AiTile> calcule_les_pointsFinaux(int[][] matrice, AiZone gameZone)throws StopRequestException{


		checkInterruption();
		//la longueur de la matrice
		int width = gameZone.getWidth();
		//la largeur de la matrice
		int height = gameZone.getHeight();
		//la liste ou les points finaux sont tenus
		List<AiTile> endPoints = new ArrayList<AiTile>();
		for (int i = 0; i < height; i++) {
			checkInterruption();
			for (int j = 0; j < width; j++) {
				checkInterruption();
				if (matrice[i][j] > 0 ) {
					//if(i!=this.ourHero.getLine() && j!=this.ourHero.getCol())
						endPoints.add(gameZone.getTile(i, j));
				}
			}
		}
		System.out.println("Collect: EndPoints:"+endPoints);
		return endPoints;
	}
	
	/**
	 * Methode calculant la liste des cases ou le hero peut aller pour la matrice attaque. On prend aussi en compte les cases qui sont
	 * dans la portee des bombes. Notre hero peut se deplacer en traversant ces cases.
	 * 
	 * @param matrice La Matrice attaque
	 * @param gameZone la zone du jeu
	 * @return la liste des points finaux.
	 * @throws StopRequestException
	 */
	private List<AiTile> calcule_les_pointsFinaux(double[][] matriceattaque, AiZone gameZone)throws StopRequestException
	{
		checkInterruption();
		List<AiTile> endPoints = new ArrayList<AiTile>();
		Collection<AiHero> heros = gameZone.getHeroes();
		heros.remove(ourHero);
		Iterator<AiHero> iteratorHeros = heros.iterator();
		while (iteratorHeros.hasNext()) 
		{
			checkInterruption();
			AiHero hero = iteratorHeros.next();
			int x= hero.getLine();
			int y=hero.getCol();
			double m =  matriceattaque[x-1][y-1] + matriceattaque[x][y-1]
			          + matriceattaque[x+1][y+1] + matriceattaque[x+1][y]
			          + matriceattaque[x+1][y-1] + matriceattaque[x][y+1]
			          + matriceattaque[x-1][y+1] + matriceattaque[x-1][y];
			int distance = gameZone.getTileDistance(hero.getTile(), ourHero.getTile());
			double valeur;
			if(distance==0)			
				valeur= m + 1/hero.getWalkingSpeed();
			else
				valeur= m + 1/hero.getWalkingSpeed()+1/distance;
			matriceattaque[x][y]=	valeur;
			int x1=x,x2=x,x3=x,x4=x;
			int y1=y,y2=y,y3=y,y4=y;
			//System.out.println("Attaque: essaiattaque2");
			//controle le bas 
			if(matriceattaque[x1+1][y1+1]==1 && matriceattaque[x1+1][y1-1]==1 &&  matriceattaque[x1+1][y1]!=1)
			{
				//System.out.println("Attaque: essaiattaque3");
				matriceattaque[x1+2][y1]= valeur+2;
				x1=x1+1;
			}	
			//controle le haut
			if(matriceattaque[x2-1][y2+1]==1 && matriceattaque[x2-1][y2-1]==1 && matriceattaque[x2-1][y2]!=1)
			{
				//System.out.println("Attaque: essaiattaque4");
				matriceattaque[x2-2][y2]= valeur+2;
				x2=x2-1;
			}
			//controle le droite
			if(matriceattaque[x3+1][y3+1]==1 && matriceattaque[x3-1][y3+1]==1  && matriceattaque[x3][y3+1]!=1)
			{
				//System.out.println("Attaque: essaiattaque5");
				matriceattaque[x3][y3+2]= valeur+2;
				y3=y3+1;
			}
			//controle la gauche
			if(matriceattaque[x4+1][y4-1]==1 && matriceattaque[x4-1][y4-1]==1 && matriceattaque[x4][y4-1]!=1)
			{				
				//System.out.println("Attaque: essaiattaque6");
				matriceattaque[x4][y4-2]= valeur+2;
				y4=y4-1;
			}	
			
			//if(x1!=this.ourHero.getLine() && y1!=this.ourHero.getCol())
				if(x1>1)
					endPoints.add(gameZone.getTile(x1-2, y1));
			//System.out.println("Attaque: 1"+endPoints);
			//if(x2!=this.ourHero.getLine() && y2!=this.ourHero.getCol())
				if(y2<2)
					endPoints.add(gameZone.getTile(x2, y2-2));
			//System.out.println("Attaque: 2"+endPoints);
			//if(x2!=this.ourHero.getLine() && y3!=this.ourHero.getCol())
				if(y3<2)
					endPoints.add(gameZone.getTile(x3, y3-2));
			//System.out.println("Attaque: 3"+endPoints);
			//if(x2!=this.ourHero.getLine() && y4!=this.ourHero.getCol())
				if(x4>1)
					endPoints.add(gameZone.getTile(x4-2, y4));
			//System.out.println("Attaque: 4"+endPoints);
		
		}
		System.out.println("Attaque: EndPoints:"+endPoints);
		return endPoints;
	}
	/**
	 * Methode qui permet l'affichage de la matrice collecte en utilisant l'API fournie
	 * @param matrice
	 *            la matrice collecte
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	 private void affiche(int[][] matrice, AiZone gameZone)throws StopRequestException
	 {
		 checkInterruption();
		 for (int i = 0; i < gameZone.getHeight(); i++) 
			{
				for (int j = 0; j < gameZone.getWidth(); j++) 
				{
					System.out.print(matrice[i][j]+"\t");
				}
				System.out.println();
			}
			
			System.out.println("-----------------");
	 }
		/**
		 * Methode qui permet l'affichage de la matrice attaque en utilisant l'API fournie
		 * @param matrice
		 *            la matrice collecte
		 * @param gameZone
		 *            la zone du jeu
		 * @throws StopRequestException
		 */
		 private void affiche(double[][] matrice, AiZone gameZone)throws StopRequestException
		 {
			 checkInterruption();
			 for (int i = 0; i < gameZone.getHeight(); i++) 
				{
					for (int j = 0; j < gameZone.getWidth(); j++) 
					{
						//System.out.println("deneme7");

						System.out.print(matrice[i][j]+"\t");
					}
					System.out.println();
				}
				
				System.out.println("-----------------");
		 }
		/**
		 * Methode qui calcule la duree necessaire pour aller a la case du bonus 
		 * @param bonus
		 *            item bonus
		 * @param gameZone
		 *            la zone du jeu
		 * @throws StopRequestException
		 */ 
	 private double duree_aller_a_Bonus(AiZone gameZone, AiItem bonus) throws StopRequestException {
			checkInterruption();
			AiTile bonusPos = bonus.getTile();
			AiTile MonPos = ourHero.getTile();
			int distance = gameZone.getTileDistance(bonusPos, MonPos);
			double Vitesse= ourHero.getWalkingSpeed();
			double T= distance/Vitesse;
			return T;
		}
		/**
		 * Methode qui trouve le plus proche bombe au bonus 
		 * cette methode controle les 4 cases voisins
		 * @param bonus
		 *            item bonus
		 * @param gameZone
		 *            la zone du jeu
		 * @param matrice 
		 *            La Matrice de collecte
		 * @throws StopRequestException
		 */
		private void plus_proche_bombe(AiZone gameZone, AiItem bonus,int[][] matrice) throws StopRequestException 
		{
			checkInterruption();
								
			gameZone = getPercepts();
			Collection<AiBomb> bombes = gameZone.getBombs();
			Iterator<AiBomb> iteratorBombes = bombes.iterator();
			AiBomb tempbombe=null;
			int distance;
			double T,T2;
			while (iteratorBombes.hasNext()) 
			{
				checkInterruption();
				tempbombe = iteratorBombes.next();
				AiTile bonusPos = bonus.getTile(), bombePos = tempbombe.getTile();
				Direction dir=gameZone.getDirection(bonusPos.getPosX(), bonusPos.getPosY(), bombePos.getPosX(),bombePos.getPosY());
				System.out.println("direction"+dir);			
				if(dir==Direction.UP||dir==Direction.DOWN||dir==Direction.LEFT||dir==Direction.RIGHT)
				{
					distance = gameZone.getTileDistance(bonusPos, bombePos);
					System.out.println("distance"+distance);
					T=tempbombe.getExplosionDuration()+tempbombe.getFailureProbability()-tempbombe.getLatencyDuration();		
					T2=T-duree_aller_a_Bonus(gameZone, bonus);
					System.out.println("bombe:"+tempbombe);
					
					if(T2<0)
						bonus_init=-1;
					else
						// la distance d'effet va etre calculee, a completer
						if(distance<2)
							bonus_init=BONUS/2;
				}
				
			} 
			matrice[bonus.getLine()][bonus.getCol()] = bonus_init;

		}	
		
		/**
		 * Methode qui calcule le temps d'aller au bonus du plus proche adversaire 
		 * @param bonus
		 *            item bonus
		 * @param gameZone
		 *            la zone du jeu
		 * @throws StopRequestException
		 */
		private void plus_proche_Hero(int[][] matrice, AiZone gameZone, AiItem bonus) throws StopRequestException
		{
			
			checkInterruption();
			int compte=1;
			List<AiHero> heros = gameZone.getRemainingHeroes();
			heros.remove(ourHero);
			AiTile bonusPos = bonus.getTile();
			if(matrice[bonus.getLine()][bonus.getCol()]!=-1)
			{
				checkInterruption();
				for (int i = 0; i < heros.size(); i++) 
				{
					checkInterruption();
					AiHero hero = heros.get(i);
					int tempDis = gameZone.getTileDistance(bonusPos, hero.getTile());
					double tempVitesse= hero.getWalkingSpeed();
					double tempT= tempDis/tempVitesse-duree_aller_a_Bonus(gameZone,bonus);
					System.out.println("hero:"+hero);
					if (tempT < 0) 
						compte=compte+1;
				}
				bonus_init=bonus_init/compte;
				matrice[bonus.getLine()][bonus.getCol()]=bonus_init;
			}
		}

	/**
	 * 
	 * Methode calculant le chemin le plus court que l'hero peut suivre.
	 * 
	 * @param ownHero l'hero sollicite par notre AI
	 * @param startPoint la position de notre hero
	 * @param endPoints les cases cibles ou le hero peut aller
	 * @return le chemin le plus court a parcourir
	 * @throws StopRequestException
	 */
	private AiPath chemin_le_plus_court(AiHero ownHero, AiTile startPoint,List<AiTile> endPoints) throws StopRequestException {
		checkInterruption();
		//le chemin le plus court possible
		AiPath shortestPath=null;
		//L'objet pour implementer l'algo A*
		Astar astar;
		//Calcul du cout par la classe de l'API
		CostCalculator cost = new BasicCostCalculator();
		//Calcul de l'heuristic par la classe de l'API
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();		
		astar = new Astar(this, ownHero, cost, heuristic);
		System.out.println("shortestpath");
		try
		{
			System.out.println(" on calcule le Shortestpath ");
			shortestPath = astar.processShortestPath(startPoint, endPoints);
			System.out.println("on a calcule le Shortestpath ");
		}
		catch (LimitReachedException e)
		{	// 
			e.printStackTrace();
		}
		return shortestPath;
	}		
	
	/**
	 * Methode calculant la nouvelle action
	 * 
	 * @return la nouvelle action de notre hero
	 * @throws StopRequestException
	 */
	private AiAction newAction(AiPath nextMove) throws StopRequestException {
		checkInterruption();
		// les cases suivant pour le deplacement.
		List<AiTile> tiles = nextMove.getTiles();
		// deplacement sur l'abcisse
		int dx;
		// deplacement sur l'ordonne
		double dy;

		dx = (tiles.get(0).getLine()) - (this.ourHero.getLine());
		// calcul de deplacement sur l'ordonne par rapport a la position de
		// l'hero et la premiere
		// case du chemin le plus court.
		dy = (tiles.get(0).getCol()) - (this.ourHero.getCol());

		// Determine la direction ou le hero va se deplacer.
		if (dx < 0 && dy == 0) {
			return new AiAction(AiActionName.MOVE, Direction.UP);
		} else if (dx < 0 && dy < 0) {
			return new AiAction(AiActionName.MOVE, Direction.UPLEFT);
		} else if (dx == 0 && dy < 0) {
			return new AiAction(AiActionName.MOVE, Direction.LEFT);
		} else if (dx > 0 && dy == 0) {
			return new AiAction(AiActionName.MOVE, Direction.DOWN);
		} else if (dx > 0 && dy > 0) {
			return new AiAction(AiActionName.MOVE, Direction.DOWNRIGHT);
		} else if (dx == 0 && dy > 0) {
			return new AiAction(AiActionName.MOVE, Direction.RIGHT);
		} else if (dx > 0 && dy < 0) {
			return new AiAction(AiActionName.MOVE, Direction.DOWNLEFT);
		} else if (dx < 0 && dy > 0) {
			return new AiAction(AiActionName.MOVE, Direction.UPRIGHT);
		} else {
			return new AiAction(AiActionName.NONE);
		}
	}
	
	/**
	 * Methode qui fait la decision de poser la bombe en mode collecte
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 * @return  poserbombe
	 */
	private boolean DecidePoserBombe_Collect(AiZone gameZone)throws StopRequestException
	 {
		Collection<AiBlock> blocks = gameZone.getBlocks();
		Iterator<AiBlock> iteratorBlocks = blocks.iterator();
		while (iteratorBlocks.hasNext()) 
		{
			checkInterruption();
			AiBlock block = iteratorBlocks.next();
			if(block.isDestructible())
				//if(senfuir(gameZone))
			{
					poserbombe=true;
			}
		//	else
			//	poserbombe=false;
		} 	
		System.out.println("Collect: Poser bombe="+poserbombe);
		return poserbombe;
	 }
	/**
	 * Methode qui fait la decision de poser la bombe en mode attaque
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 * @return  poserbombe
	 */
	private boolean DecidePoserBombe_Attaque(AiZone gameZone)throws StopRequestException
	 {		
		AiTile tile=gameZone.getTile(ourHero.getLine(), ourHero.getCol());
		if(!Plein(gameZone,tile))
			poserbombe=true;
		else 
			poserbombe=false;
		System.out.println("Attaque: Poser bombe="+poserbombe);
		return poserbombe;
	 }
	
	/**
	 * Methode qui controle si une case est vide ou pas
	 * @param AiTile tile
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 * @return  poserbombe
	 */
	private boolean Plein(AiZone gameZone, AiTile tile) throws StopRequestException 
	{
		checkInterruption();
		//s'il y a une bombe ou un bonus dans le tile cible alors il y a danger
		if(!(tile.getBombs().isEmpty()) || !(tile.getFires().isEmpty()))
		{
			plein=true;			
		}
		//s'il on est au portee du'une bombe alors il y a danger
		Collection<AiBomb> bombs = gameZone.getBombs();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		while (iteratorBombs.hasNext()) 
		{
			checkInterruption();
			AiBomb bomb = iteratorBombs.next();
			List <AiTile> tiles = bomb.getBlast();
			if (tiles.contains(tile))
			{
				plein=true;				
			}
		}
		return danger;
	}
	/**
	 * Methode qui controle l'existance des paths
	 * @param 
	 *       gameZone
	 * @param 
	 *        matrice
	 * @param 
	 *        endPoints
	 *  @param
	 *         startPoint
	 * @return true 
	 *        s'il y a un path accesible
	 *        false 
	 *        s'il n' ya pas 
	 * @throws StopRequestException
	 * @throws LimitReachedException
	 */
	public boolean existe_Path(AiZone gameZone,List<AiTile> endPoints,AiTile startPoint) throws StopRequestException, LimitReachedException{
		
		AiPath path=this.chemin_le_plus_court(ourHero,startPoint,endPoints);
		System.out.println(path);
		if(path.isEmpty())
			return false;
		else
			return true;
	}
	
	public AiAction DESTUIRE(AiZone gameZone, AiAction resultat )throws StopRequestException
	{
		System.out.println("DESTRUIRE");
		Collection<AiBlock> blocks = gameZone.getBlocks();
		Iterator<AiBlock> iteratorBlocks = blocks.iterator();
		List<AiTile> endPoints=new ArrayList<AiTile>();
	
		while (iteratorBlocks.hasNext()) 
		{
			AiBlock block = iteratorBlocks.next();				
			if(block.isDestructible())
			{
				checkInterruption();
				try{
				endPoints.add(block.getTile().getNeighbor(Direction.UP));
				}				
				catch(Exception e){}
				try
				{
					endPoints.add(block.getTile().getNeighbor(Direction.DOWN));
				}
				catch(Exception e){}
				try
				{
					endPoints.add(block.getTile().getNeighbor(Direction.LEFT));
				}
				catch(Exception e){}
				try
				{
					endPoints.add(block.getTile().getNeighbor(Direction.RIGHT));
				}
				catch(Exception e){}				
			}
		}
		this.nextMove = this.chemin_le_plus_court(ourHero,ourHero.getTile(),endPoints);
		System.out.println("DETRUIT:" +nextMove);
		System.out.println("on sort de shortestpath pour DETRUIT");
		resultat=this.newAction(nextMove);
		return resultat;
	}
	
	public AiAction SENFUIRE(AiZone gameZone, AiAction resultat )throws StopRequestException
	{
		Collection<AiTile> neighbours = ourHero.getTile().getNeighbors();
		Iterator<AiTile> iteratorNeighbours = neighbours.iterator();
		List<AiTile> endPoints=new ArrayList<AiTile>();
		while(iteratorNeighbours.hasNext())
		{
			AiTile neighbour = iteratorNeighbours.next();
			if(neighbour.getBombs().isEmpty())
			{
				endPoints.add(neighbour);
				this.nextMove = this.chemin_le_plus_court(ourHero,ourHero.getTile(),endPoints);
			}
		}
		System.out.println("SENFUIRE"+endPoints);
		System.out.println("SENFUIRE");
		resultat=this.newAction(nextMove);
		return resultat;
	}
}


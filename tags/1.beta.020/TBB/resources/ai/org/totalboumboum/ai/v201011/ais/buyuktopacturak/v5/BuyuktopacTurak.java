package org.totalboumboum.ai.v201011.ais.buyuktopacturak.v5;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * >> ce texte est à remplacer par votre propre description de votre IA
 * >> remplacez aussi le nom de l'auteur.
 * 
 * classe principale de l'IA, qui définit son comportement.
 * n'hésitez pas à décomposer le traitement en plusieurs classes,
 * plus votre programme est modulaire et plus il sera facile à
 * débugger, modifier, relire, comprendre, etc.
 * 
 * @author Onur Büyüktopaç & Yigit Turak
 *
 */
public class BuyuktopacTurak extends ArtificialIntelligence
{	
	/** la zone de jeu */
	private AiZone zone = null;		
	private AiTile currentTile = null;
	private AiHero deepPurple = null;
	private Elements element;
	private CollectionMatrix colMatrix;
	private AttackMatrix attackMatrix;
	private PerfectStrangers ps;
	
	private double[][] currentMatrix;
	
	private int maxLine, maxCol; //Col et Ligne de la case maximale
	ArtificialIntelligence ai=this;
	
	/**
	 * Cette classe dépend essentiellement de la fonction «processAction» 
	 * qui est étendu de la classe «ArtificialIntelligence» de l’API. 
	 * Car dans cette méthode, on décide les actions de l’iA.
	 * si notre Ai existe dans le jeu,
	 * d'abordon chois la mode et decide la posage de bombe avec la methode controlBomb
	 * -> si on renvoie "true" on met d'une bombe.
	 * ->sinon:
	 * 		  currentMatrix egale a la matrice dont laquelle mode.
	 * 		  on decide la direction du movement
	 * 		  si la valeur de notre tile est positive et 
	 * 		  la valeur de notre voisine de la direction choisant est plus moins de -600
	 * 		  on attend
	 * 		  sinon on se deplace a la voisine 	   
	 * 	 
	 * @return result: AiAction deplacement ou posage de bombe
	 * @throws StopRequestException
	 * @throws NullPointerException
	 */
	public AiAction processAction() throws StopRequestException, NullPointerException
	{	// avant tout : test d'interruption
		checkInterruption();
		// premier appel : on initialise
		AiAction result = new AiAction(AiActionName.NONE);
		initBT();
		// si le personnage controlé a été éliminé, inutile de continuer
		if(!this.deepPurple.hasEnded())
		{	// on met à jour la position de l'ia dans la zone
			boolean actionBomb, mode=selectMode();
			actionBomb=controlBomb(mode);
			if(actionBomb){
				result=new AiAction(AiActionName.DROP_BOMB);
			}
			else{
				if(mode){
					getColMatrix();
					this.currentMatrix=getSelectMatrix(true);
				}
				else{
					getAttMatrix();
					this.currentMatrix=getSelectMatrix(false);
				}
				Direction moveDir = aStarDirection(this.currentTile, getMaxTile());
				try{
					if(this.currentMatrix[this.currentTile.getLine()][this.currentTile.getCol()]>0 && this.currentMatrix[currentTile.getNeighbor(moveDir).getLine()][currentTile.getNeighbor(moveDir).getCol()]<-600){
						result = new AiAction(AiActionName.NONE);
					}
					else{
						result = new AiAction(AiActionName.MOVE,moveDir);
					}
				}
				catch(Exception e){
				}	
			}
		}
		return result;
	}
	
	
	/////////////////////////////////////////////////////////////////
	// INITIALISATION			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	/**
	 * D’abord on obtient des percepts, ensuite on trouve notre héro, 
	 * et puis on crée l’objet PerfectStrangers et Elements.
	 * En fin, on met à jour de notre case.  
	 */
	private void initBT() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		this.zone = getPercepts(); 
		this.deepPurple = this.zone.getOwnHero();
		ps=new PerfectStrangers(this,zone);
		element= new Elements(this,zone);		
		updateLocation();
	}
	
	/////////////////////////////////////////////////////////////////
	// MODE								 		  ///////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * On trouve les nombres de notre bombe et de notre bombe actuelle, 
	 * après on choisi le mode.
	 * 
	 * @return mode: boolean. notre mode
	 * @throws StopRequestException
	 */
	private boolean selectMode() throws StopRequestException{
		checkInterruption();
		boolean mode=false;
		//calculer d'apres les bombs actuelles.
		//System.out.println("sayý: "+this.deepPurple.getBombNumberMax()+" range: "+this.deepPurple.getBombRange());
		
			if(this.deepPurple.getBombNumberMax()>2){
				if(getCurrentBomb()>2){
					mode=false;
				}
				else{
					mode=true;
				}
			}
			else if(this.deepPurple.getBombNumberMax()==2){
				if(getCurrentBomb()==2){
					mode=false;
				}
				else{
					mode=true;
				}
			}
			else{
				mode=false;
			}
		
		return mode;
	}
	
	/////////////////////////////////////////////////////////////////
	// EXECUTION FOR DROP THE BOMB		 		  ///////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * dans cette methode, on decide la posage de bombe.
	 * si on renvoie "true" on pose d'une bombe dans la methode processAction
	 * 
	 * 
	 * @param mode
	 * @return
	 * @throws StopRequestException
	 */
	private boolean controlBomb(boolean mode) throws StopRequestException{
		checkInterruption();
		boolean dropBomb = false;
		int calcul;
		
		//les premiers 3 if conditions sont communs pour la mode collecte et attaque.
		//si currentTile ne contient pas de bombe
		if(this.currentTile.getBombs().size()==0){
			//si notre Ai pose d'une bombe, apres elle trouve une case vide qui est la valeur positive.
			if(ps.isRunnable(currentTile)){
				
					
					List<AiHero> heroesList = new ArrayList<AiHero>(); 
					List<AiHero> removeHeroesList = new ArrayList<AiHero>(); 
					heroesList = zone.getRemainingHeroes();
					heroesList.remove(deepPurple);
					//s'il n'y a pas d'une chemine vers les ennemies par l'algorithme A*
					//on les supprime dans la liste heroesList
					for(AiHero h:heroesList){
						checkInterruption();
						if(aStarDistance(currentTile,h.getTile()) ==-1)
							removeHeroesList.add(h);
					}
					heroesList.removeAll(removeHeroesList);
					
					//si on met une bombe currentTile, l'un hero ne fuir pas
					// on renvoie "true"
					for(AiHero hero:heroesList){
						if(!ps.isRunnableEnemy(hero, this.currentTile)){
							dropBomb=true;
						}
					}
						
					if(!dropBomb){
						//si notre portee ne contient pas d'une bombe
						if(!element.rangeBombe(currentTile)){
						//si la mode est "true" c'est dire que la mode est collecte.
						if(mode){
							if(element.destroyBonus(currentTile, deepPurple.getBombRange())){
								dropBomb=true;
							}
							else{
								calcul=calculateCalcul();
								if(element.getRangeBombItemCounter(currentTile, deepPurple.getBombRange())==0){
									if(element.getRangeBombBlockCounter(this.currentTile, this.deepPurple.getBombRange()) >= calcul){
										dropBomb=true;
									}
								}
							}
						}
						//si la mode est "false" c'est dire que la mode est attaque.
						else{
							//on trouve la portee de notre iA et puis si elle est plus haut de 4 on est egale a 4.
							int bombRange = this.deepPurple.getBombRange();
							if(bombRange>3)
								bombRange = 3;
							
							//s'il y a une hero dans notre portee, on renvoie "true"
							if(element.getRangeBombHeroCounter(this.currentTile, bombRange)>0){
								dropBomb=true;
							}
							//sinon
							else{
								//si on ne va pas aux heros, c'est a dire qu'il n'y a pas d'une chemine vers les adversaires.
								// on detruit des murs.
								if(heroesList.size()==0){
									boolean drop = true;
									//on controle les murs dans la notre portee 
									//si c'est plus eleve 1 on renvoie "true"
									if(this.element.getRangeBombBlockCounter(this.currentTile, this.deepPurple.getBombRange()) > 1){
										dropBomb=true;
									}
									//si c'est egale a 1. on controle notre voisines.
									else if(this.element.getRangeBombBlockCounter(this.currentTile, this.deepPurple.getBombRange()) == 1){
										List<AiTile> searchNeighbours = new ArrayList<AiTile>();//Komsulardaki yurunelilir kareler
										List<AiTile> neigList = new ArrayList<AiTile>();
										neigList = deepPurple.getTile().getNeighbors();
										for(AiTile n : neigList){
											checkInterruption();
											if(n.isCrossableBy(deepPurple))
												searchNeighbours.add(n);
										}
										for(AiTile n : searchNeighbours){
											checkInterruption();
											if(this.element.getRangeBombBlockCounter(n, this.deepPurple.getBombRange()) >1 && ps.isRunnable(n)==true)
												drop = false;
										}
										if(drop){
											dropBomb=true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return dropBomb;
	}
	/**
	 * 
	 * @return calcul
	 * @throws StopRequestException
	 */
	private int calculateCalcul() throws StopRequestException{
		checkInterruption();
		int hiddenBonus;
		int blocks;
		//Impossible de detruir 10 blocks est meme temps
		int calcul=10;		
		
		List<AiBlock> destWalls = this.zone.getDestructibleBlocks();
		blocks = destWalls.size();
		hiddenBonus = this.zone.getHiddenItemsCount();
		if ((hiddenBonus*getCurrentBomb())!=0){
			calcul = (int)(blocks*2/((hiddenBonus*getCurrentBomb())));
			if(calcul < 1){
				calcul = 1;
			}
		}
		return calcul;
	}	
	
	/////////////////////////////////////////////////////////////////
	// A*				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**on trouve un chemine a la case tile1 de la case tile2 par l'algorithme A*
	 * on calcule cost, heuristic et succes par la classe BasicXXXXXCalculator()
	 * 
	 * @param tile1
	 * @param tile2
	 * @return
	 * @throws StopRequestException
	 * @throws NullPointerException
	 */
	private AiPath aStarPath(AiTile tile1, AiTile tile2) throws StopRequestException, NullPointerException{	
		checkInterruption();
		CostCalculator  cost = new BasicCostCalculator();
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		SuccessorCalculator succes = new BasicSuccessorCalculator();
		Astar astar = new Astar(this.ai, this.deepPurple, cost, heuristic,succes);
		AiPath path = null;
		//on trouve la chemine plus proche
		try 
		{
			path = astar.processShortestPath(tile1, tile2);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}
	/**
	 * On utilise l’algorithme A*, on trouve le chemine vers les ennemies.
	 * Si on trouve une chemine, on renvoie la direction de notre case à la case adversaire case par case. 
	 */
	private Direction aStarDirection(AiTile tile1, AiTile tile2) throws StopRequestException, NullPointerException{	
		checkInterruption();
		Direction moveDir=Direction.NONE;
		AiPath path = new AiPath();
		path = aStarPath(tile1,tile2);
		if(path.getTiles().size()!=0){
			CostCalculator  cost = new BasicCostCalculator();
			HeuristicCalculator heuristic = new BasicHeuristicCalculator();
			SuccessorCalculator succes = new BasicSuccessorCalculator();
			Astar astar = new Astar(this.ai, this.deepPurple, cost, heuristic,succes);
			AiPath tempPath1 = path;
			AiPath tempPath2 = path;
			List<AiTile> casesList = new ArrayList<AiTile>();
			casesList = path.getTiles();
			Iterator<AiTile> cases = casesList.iterator();
			AiTile tile;
			while(cases.hasNext())
			{
				checkInterruption();
				tile = cases.next();
				if(tile != tile1)
				{			
					try {
						tempPath1 = astar.processShortestPath(tile1, tile);
					} catch (Exception e) {
						e.printStackTrace();
					} 
					if(tempPath1.getTiles().size()!=0 && tempPath1.compareTo(tempPath2)<0)
						tempPath2 = tempPath1;
				}
			}
			moveDir = this.zone.getDirection(tile1, tempPath2.getLastTile());
		}
		return moveDir;
	}
	
	/**
	 * On renvoie la longueur de la chemine qui est le plus court chemin 
	 * pour aller de la case départ à la case arrêt en utilisant l’algorithme A*.
	 * si tile1 est egale a tile2, distance est zero.
	 * sinon on trouve un chemine a la case tile1 de la case tile2.
	 * 	s'il y a une chemine, la distance est egale a la taille path.
	 * 	sinon la distance est negative -1.
	 * @param tile1
	 * @param tile2
	 * @return
	 * @throws StopRequestException
	 * @throws NullPointerException
	 */
	public int aStarDistance(AiTile tile1, AiTile tile2) throws StopRequestException, NullPointerException{	
		checkInterruption();
		int distance;
		AiPath path = new AiPath();
		//on trouve la chemine plus proche
		if(tile1!=tile2){
			try 
			{
				path = aStarPath(tile1,tile2);
			} 
			catch (Exception e) {

			}
			if(path.getTiles().size()!=0){
				distance=path.getLength()-1;
			}
			else{
				distance=-1;
			}
		}
		else
			distance=0;

		return distance;		
	}
	/////////////////////////////////////////////////////////////////
	//MATRIX				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Cree collectionMatrix
	 * @throws StopRequestException
	 */
	private void getColMatrix() throws StopRequestException{
		checkInterruption();
		this.colMatrix = new CollectionMatrix(this,zone);
		this.colMatrix.createMatrix();
	}
	/**
	 * Cree attackMatrix
	 * @throws StopRequestException
	 */
	private void getAttMatrix() throws StopRequestException{
		checkInterruption();
		this.attackMatrix = new AttackMatrix(this,zone);
		this.attackMatrix.createMatrix();
	}
	/**
	 * renvoi currentMatrix
	 * @return
	 * @throws StopRequestException
	 */
	public double[][] getMatrix() throws StopRequestException{
		checkInterruption();
		return this.currentMatrix;
	}

	/**
	 * On renvoie de la matrice de la mode.
	 * @param mode
	 * @return
	 * @throws StopRequestException
	 */
	private double[][] getSelectMatrix(boolean mode) throws StopRequestException{
		checkInterruption();
		if (mode){
			return this.colMatrix.getMatrix();
		}
		else{
			return this.attackMatrix.getMatrix();
		}
	}
	/////////////////////////////////////////////////////////////////
	//TILE						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	/**
	 * on trouve la case maximum valeur en utilisant la matrice actuelle.
	 * @return
	 * @throws StopRequestException
	 */
	private AiTile getMaxTile() throws StopRequestException{
		checkInterruption();
		double[][] matrix=this.currentMatrix;
		double max=-1;
		int line, col;
		for (line = 0; line < this.zone.getHeight(); line++) {
			checkInterruption();
			for (col = 0; col < this.zone.getWidth(); col++) {
				checkInterruption();
				if(matrix[line][col]>max){
					max=matrix[line][col];
					this.maxLine=line;
					this.maxCol=col;
				}
			}
	   	}
		
		return this.zone.getTile(maxLine, maxCol);
	}
	
	
	/////////////////////////////////////////////////////////////////
	// CURRENT LOCATION			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Miser a jour de la location
	 * @throws StopRequestException
	 */
	private void updateLocation() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		this.currentTile = this.deepPurple.getTile();
	}
	/**
	 * on trouve la nombre de actuelle bombe dont notre iA 
	 * @return :int nombre actuelle bombe
	 * @throws StopRequestException
	 */
	public int getCurrentBomb() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		return deepPurple.getBombNumberMax()-deepPurple.getBombNumberCurrent();
	}
}



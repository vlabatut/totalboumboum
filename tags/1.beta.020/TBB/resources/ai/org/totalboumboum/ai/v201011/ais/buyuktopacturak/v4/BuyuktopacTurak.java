package org.totalboumboum.ai.v201011.ais.buyuktopacturak.v4;

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
	/** le personnage dirigé par cette IA */
	private AiHero deepPurple = null;
	private AiAction result = new AiAction(AiActionName.NONE);
	private Direction moveDir;
	private Elements element;
	private CollectionMatrix colMatrix;
	private AttackMatrix attackMatrix;
	private PerfectStrangers ps;
	private double[][] currentMatrix;
	private boolean dropBomb=false;
	private int maxLine, maxCol; //Col et Ligne de la case maximale
	/** la case occupée actuellement par le personnage */
	private AiTile currentTile = null;
	/** la position en pixels occupée actuellement par le personnage */
	private double currentX;
	/** la position en pixels occupée actuellement par le personnage */
	private double currentY;
	ArtificialIntelligence ai=this;
	private Astar astar;
	private AiPath path;
	
	/**
	 * Cette classe dépend essentiellement de la fonction «processAction» 
	 * qui est étendu de la classe «ArtificialIntelligence» de l’API. 
	 * Car dans cette méthode, on décide les actions de l’iA.
	 */
	public AiAction processAction() throws StopRequestException, NullPointerException
	{	// avant tout : test d'interruption
		checkInterruption();
		// premier appel : on initialise
		
		initBT();
		// si le personnage controlé a été éliminé, inutile de continuer
		if(!this.deepPurple.hasEnded())
		{	// on met à jour la position de l'ia dans la zone
			updateLocation();
			
			if(selectMode())
			{
				executeCollect();
				
				if(!this.dropBomb)
				{
					getColMatrix();
					this.currentMatrix=getSelectMatrix(true); //currentMatrix = collectMatrix 
					AstarDirection(this.zone.getOwnHero().getTile(),getMaxTile());
					try{
					if(this.currentMatrix[currentTile.getNeighbor(moveDir).getLine()][currentTile.getNeighbor(moveDir).getCol()]>-100)
						this.result = new AiAction(AiActionName.MOVE,this.moveDir);
					else
						this.result = new AiAction(AiActionName.NONE,moveDir);
					}
					catch(Exception e)
					{
						
					}
				}
			}
			else
			{
				executeAttack();
				if(!this.dropBomb)
				{
					getAttMatrix();
					currentMatrix=getSelectMatrix(false); //currentMatrix = attackMatrix
					AstarDirection(this.zone.getOwnHero().getTile(),getMaxTile());
					try{
						if(this.currentMatrix[currentTile.getNeighbor(moveDir).getLine()][currentTile.getNeighbor(moveDir).getCol()]>-100)
							this.result = new AiAction(AiActionName.MOVE,this.moveDir);
						else
							this.result = new AiAction(AiActionName.NONE,moveDir);
					}
					catch(Exception e)
					{
							
					}
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
	 * et puis on crée l’objet Elements et met à jour de notre case.  
	 */
	private void initBT() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		this.zone = getPercepts(); 
		this.deepPurple = this.zone.getOwnHero();
		element= new Elements(this);
		ps=new PerfectStrangers(this);
		updateLocation();
	}
	
	/////////////////////////////////////////////////////
	// MODE			/////////////////////////////////////
	/////////////////////////////////////////////////////
	
	/**
	 * On trouve les nombres de notre bombe et de notre bombe actuelle, 
	 * après on choisi le mode.  
	 */
	private boolean selectMode() throws StopRequestException{
		checkInterruption();
		boolean mode;
		//calculer d'apres les bombs actuelles. 
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
	// EXECUTION COLLECTE FOR DROP THE BOMB		  ///////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * On évalue des critères (le nombre des murs destructibles et de bonus) 
	 * ensuite on décide le posage de bombe pour le mode collecte. 
	 */
	private void executeCollect() throws StopRequestException{
		checkInterruption();
		int hiddenBonus;
		int blocks;
		int calcul=10;//Impossible de detruir 10 blocks a meme temps		
		//Jeter les murs destructibles a une list
		List<AiBlock> wallsList = this.zone.getBlocks();
		List<AiBlock> destWalls = new ArrayList<AiBlock>();
		Iterator<AiBlock> itWalls = wallsList.iterator();
		AiBlock wall;
		while(itWalls.hasNext()){
			checkInterruption();
			wall = itWalls.next();
			if(wall.isDestructible()){
				destWalls.add(wall);
			}
		}
		
		blocks = destWalls.size();
		hiddenBonus = this.zone.getHiddenItemsCount();
		if ((hiddenBonus*getCurrentBomb())!=0){
			calcul = blocks/((hiddenBonus*getCurrentBomb()));
			if(calcul < 1){
				calcul = 1;
			}
		}
		if(ps.isRunnable(this.currentTile)==true){
			if(element.destroyBonus(this.currentTile,this.deepPurple.getBombRange())&&this.currentTile.getBombs().size()==0){
				this.result = new AiAction(AiActionName.DROP_BOMB);
				this.dropBomb=true;
			}
			else if(element.getRangeBombBlockCounter(this.currentTile, this.deepPurple.getBombRange()) >= calcul){
				if(element.getRangeBombItemCounter(this.currentTile, this.deepPurple.getBombRange())==0 && this.currentTile.getBombs().size()==0){
					this.result = new AiAction(AiActionName.DROP_BOMB);
					this.dropBomb=true;
				}
				else
					this.dropBomb=false;
			}
			else
				this.dropBomb=false;
		}
		else
			this.dropBomb=false;
	}
	
	/////////////////////////////////////////////////////////////////
	// EXECUTION ATTACK FOR DROP THE BOMB		  ///////////////////
	/////////////////////////////////////////////////////////////////
	
	/**
	 * On évalue des critères (tuer à l’adversaire) 
	 * ensuite on décide le posage de bombe pour le mode attaque.
	 */
	private void executeAttack() throws StopRequestException{
		checkInterruption();
		if(element.getRangeBombHeroCounter(this.currentTile, this.deepPurple.getBombRange())>0 && this.currentTile.getBombs().size()==0 && ps.isRunnable(this.currentTile)==true){
			this.result = new AiAction(AiActionName.DROP_BOMB);
			this.dropBomb=true;
		}
		else{
			this.dropBomb=false;
			List<AiHero> heroList = new ArrayList<AiHero>();
			heroList=this.zone.getHeroes();
			List<AiTile> tileList = new ArrayList<AiTile>();
			for(AiHero h:heroList){
				checkInterruption();
				if(h!=this.deepPurple)
					tileList.add(h.getTile());
			}
			CostCalculator  cost = new BasicCostCalculator();
			HeuristicCalculator heuristic = new BasicHeuristicCalculator();
			Astar astarHero = new Astar(this.ai, deepPurple, cost, heuristic);
			AiPath pathHero = null;
			try 
			{
				if(tileList.size()!=0)
					pathHero = astarHero.processShortestPath(this.currentTile, tileList);
			} 
			catch (Exception e) {
				e.printStackTrace();
			} 
						
			if(pathHero.getTiles().size()==0 && this.currentTile.getBombs().size()==0 && this.element.getRangeBombBlockCounter(this.currentTile, this.deepPurple.getBombRange()) >0 && ps.isRunnable(this.currentTile)==true){
				this.result = new AiAction(AiActionName.DROP_BOMB);
				this.dropBomb=true;
			}
		}
	}
	
	
	/////////////////////////////////////////////////////////////////
	// A*				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	/**
	 * On utilise l’algorithme A*, on recherche le chemine vers les ennemies.
	 * Si on trouve une chemine, on renvoie la direction de notre case à la case adversaire. 
	 */
	private void AstarDirection(AiTile tile1, AiTile tile2) throws StopRequestException, NullPointerException{	
		checkInterruption();
		AStarCost  cost = new AStarCost(this.currentMatrix);
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		SuccessorCalculator succes = new BasicSuccessorCalculator();
		this.astar = new Astar(this.ai, this.deepPurple, cost, heuristic,succes);
		this.path = null;
		//on trouve la chemine plus proche
		try 
		{
			this.path = this.astar.processShortestPath(tile1, tile2);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		if(this.path.getTiles().size()!=0){
			AiPath tempPath1 = null;
			AiPath tempPath2 = this.path;
			List<AiTile> casesList = new ArrayList<AiTile>();
			casesList = this.path.getTiles();
			Iterator<AiTile> cases = casesList.iterator();
			AiTile tile;
			while(cases.hasNext())
			{
				checkInterruption();
				tile = cases.next();
				if(tile != tile1)
				{			
					try {
						tempPath1 = this.astar.processShortestPath(tile1, tile);
					} catch (Exception e) {
						e.printStackTrace();
					} 
					if(tempPath1.getTiles().size()!=0 && tempPath1.compareTo(tempPath2)==-1)
						tempPath2 = tempPath1;
				}
			}
			this.moveDir = this.zone.getDirection(tile1, tempPath2.getLastTile());
		}
		else{
			this.moveDir=getFindDirection(tile1,tile2);
		}
	}
	
	/**
	 * On renvoie la longueur de la chemine qui est le plus court chemin 
	 * pour aller de la case départ à la case arrêt en utilisant l’algorithme A*.
	 * @param tile1
	 * @param tile2
	 * @return
	 * @throws StopRequestException
	 * @throws NullPointerException
	 */
	public int AstarDistance(AiTile tile1, AiTile tile2) throws StopRequestException, NullPointerException{	
		checkInterruption();
		int distance;
		CostCalculator  cost = new BasicCostCalculator();
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		Astar astar = new Astar(this.ai, this.deepPurple, cost, heuristic);
		AiPath path = null;
		//on trouve la chemine plus proche
		if(tile1!=tile2){
			try 
			{
				path = astar.processShortestPath(tile1, tile2);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			if(path.getTiles().size()!=0){
				distance=path.getLength();
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
	 */
	private void getColMatrix() throws StopRequestException{
		checkInterruption();
		this.colMatrix = new CollectionMatrix(this);
		this.colMatrix.createMatrix();
	}
	/**
	 * Cree attackMatrix
	 * @throws StopRequestException
	 */
	private void getAttMatrix() throws StopRequestException{
		checkInterruption();
		this.attackMatrix = new AttackMatrix(this);
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
	 * renvoie la case courante
	 */
	public AiTile getCurrentTile() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		return this.currentTile;
	}
	
	/**
	 * on trouve la case maximum point en utilisant la matrice actuelle.
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
	 * renvoie l'abscisse courante (en pixels)
	 */
	public double getCurrentX() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		return this.currentX;
	}
	/**
	 * renvoie l'ordonnée courante (en pixels)
	 */
	public double getCurrentY() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		return this.currentY;
	}
	/**
	 * Miser a jour de la location
	 * @throws StopRequestException
	 */
	private void updateLocation() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		this.currentTile = this.deepPurple.getTile();
		this.currentX = this.deepPurple.getPosX();
		this.currentY = this.deepPurple.getPosY();
	}
	
	/////////////////////////////////////////////////////////////////
	// OWN HERO					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie le personnage contrôlé par cette IA
	 */
	public AiHero getdeepPurple() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		return this.deepPurple;
	}
	/////////////////////////////////////////////////////////////////
	// PERCEPTS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la zone de jeu
	 */
	public AiZone getZone() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		return this.zone;
	}
	/**
	 * On calcule la bombe actuelle et on la renvoie.
	 * @return
	 * @throws StopRequestException
	 */
	public int getCurrentBomb() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		return deepPurple.getBombNumberMax()-deepPurple.getBombNumberCurrent();
	}
	
	/**
	 * Si on ne trouve pas de chemine en utilisant l’algorithme A*, 
	 * alors on trouve une direction pour se diriger de la case départ à la case arrêt.
	 * @param tile1
	 * @param tile2
	 * @return
	 * @throws StopRequestException
	 */
	private Direction getFindDirection(AiTile tile1,AiTile tile2) throws StopRequestException {
		checkInterruption();
		Direction result;
		// deplacement sur l'abcisse
		int dx;
		// deplacement sur l'ordonne
		double dy;
		dx = (tile2.getLine()) - (tile1.getLine());
		dy = (tile2.getCol()) - (tile1.getCol());
		
			if (dx < 0 && dy == 0) {
				result = Direction.UP;
			} else if (dx < 0 && dy < 0) {
				result = Direction.UPLEFT;
			} else if (dx == 0 && dy < 0) {
				result = Direction.LEFT;
			} else if (dx > 0 && dy == 0) {
				result = Direction.DOWN;
			} else if (dx > 0 && dy > 0) {
				result = Direction.DOWNRIGHT;
			} else if (dx == 0 && dy > 0) {
				result = Direction.RIGHT;
			} else if (dx > 0 && dy < 0) {
				result = Direction.DOWNLEFT;
			} else if (dx < 0 && dy > 0) {
				result = Direction.UPRIGHT;
			} else {
				result = Direction.NONE;
			}


		return result;
	}	
}

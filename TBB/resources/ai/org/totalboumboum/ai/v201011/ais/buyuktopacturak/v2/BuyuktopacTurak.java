package org.totalboumboum.ai.v201011.ais.buyuktopacturak.v2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
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
 * @author Onur B�y�ktopa� & Yigit Turak
 *
 */
public class BuyuktopacTurak extends ArtificialIntelligence
{	
	/** la zone de jeu */
	private AiZone zone = null;		
	/** le personnage dirig� par cette IA */
	private AiHero ownHero = null;
	private AiAction result = new AiAction(AiActionName.NONE);
	private AiTile tileMax;
	private Direction moveDir;
	private CollectionMatrix colMatrix;
	private AttackMatrix attackMatrix;
	private double[][] currentMatrix;
	private boolean dropBomb=false;
	private int maxLine, maxCol; //Max de�erin sat�r ve s�tunu
	private int currentBomb;
	private int distance;
	/** la case occup�e actuellement par le personnage */
	private AiTile currentTile = null;
	/** la position en pixels occup�e actuellement par le personnage */
	private double currentX;
	/** la position en pixels occup�e actuellement par le personnage */
	private double currentY;
	ArtificialIntelligence ai=this;
	private Astar astar;
	private AiPath path;
	//////////////////////
	//////TAMAMLA////////
	////////////////////
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException, NullPointerException
	{	// avant tout : test d'interruption
		checkInterruption();
		// premier appel : on initialise
		myInit();
		// si le personnage control� a �t� �limin�, inutile de continuer
		if(!ownHero.hasEnded())
		{	// on met à jour la position de l'ia dans la zone
			updateLocation();
			
			if(selectMode())
			{
				executeCollect();
				
				if(!dropBomb)
				{
					getColMatrix();
					currentMatrix=getSelectMatrix(true); //currentMatrix.i collectMatrix e e�itliyor.
					this.tileMax=getMaxTile(); //currentMatrix te yer alan en y�ksek de�erli kareyi buluyor.
					getDirectionAndDistance();
					result = new AiAction(AiActionName.MOVE,this.moveDir);
				}
			}
			else
			{
				executeAttack();
				if(!dropBomb)
				{
					getAttMatrix();
					currentMatrix=getSelectMatrix(false); //currentMatrix.i attackMatrix e e�itliyor.
					this.tileMax=getMaxTile(); //currentMatrix te yer alan en y�ksek de�erli kareyi buluyor.
					getDirectionAndDistance();
					result = new AiAction(AiActionName.MOVE,this.moveDir);
				}
			}
		}
		return result;
	}
	
	
	/////////////////////////////////////////////////////////////////
	// INITIALISATION			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void myInit() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		zone = getPercepts(); 
		ownHero = zone.getOwnHero();
		updateLocation();
	}
	/////////////////////////////////////////////////////////////////
	// MODE			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean selectMode() throws StopRequestException{
		checkInterruption();
		boolean mode;
		this.currentBomb=getCurrentBomb();
		//elimde kalan bomba say�s�na g�re kontrol yap�yor.
		if(ownHero.getBombNumberMax()>2){
			if(this.currentBomb>2){
				mode=false;
			}
			else{
				mode=true;
			}
		}
		else{
			if(this.currentBomb>1){
				mode=false;
			}
			else{
				mode=true;
			}
		}
		return mode;
	}
	/////////////////////////////////////////////////////////////////
	// EXECUTION COLLECTE FOR DROP THE BOMB		  ///////////////////
	/////////////////////////////////////////////////////////////////
	private void executeCollect() throws StopRequestException{
		checkInterruption();
		int hiddenBonus;
		int blocks;
		int calcul=10;//Ayn� anda 10 kare patlatmak imkans�z
		Bomb myBomb = new Bomb(this);
		Bonus myBonus = new Bonus(this);		
		//K�r�labilir duvarlar� listeye at�yor.
		List<AiBlock> wallsList = zone.getBlocks();
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
		hiddenBonus = zone.getHiddenItemsCount();
		if ((hiddenBonus*this.currentBomb)!=0){
			calcul = blocks/((hiddenBonus*this.currentBomb));
			if(calcul < 1){
				calcul = 1;
			}
		}
		if(myBonus.destroyBonus()&&currentTile.getBombs().size()==0){
			result = new AiAction(AiActionName.DROP_BOMB);
			this.dropBomb=true;
		}
		else if(myBomb.getRangeBombBlock(currentTile, ownHero.getBombRange()) >= calcul){
			if(!myBonus.findBonus() && currentTile.getBombs().size()==0){
				result = new AiAction(AiActionName.DROP_BOMB);
				this.dropBomb=true;
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
	private void executeAttack() throws StopRequestException{
		checkInterruption();
		Bomb myBomb = new Bomb(this);
		if(isThereHero()&&currentTile.getBombs().size()==0){
			result = new AiAction(AiActionName.DROP_BOMB);
			this.dropBomb=true;
		}
		else{
			this.dropBomb=false;
			List<AiHero> heroList = new ArrayList<AiHero>();
			heroList=zone.getHeroes();
			List<AiTile> tileList = new ArrayList<AiTile>();
			for(AiHero h:heroList){
				checkInterruption();
				if(h!=ownHero)
					tileList.add(h.getTile());
			}
			CostCalculator  cost = new BasicCostCalculator();
			HeuristicCalculator heuristic = new BasicHeuristicCalculator();
			Astar astarHero = new Astar(this.ai, ownHero, cost, heuristic);
			AiPath pathHero = null;
			//on trouve la chemine plus proche
			try 
			{
				pathHero = astarHero.processShortestPath(this.currentTile, tileList);
			} 
			catch (StopRequestException e) {
				e.printStackTrace();
			} 
			catch (LimitReachedException ex) {
				ex.printStackTrace();
			}
			if(pathHero.isEmpty()&&currentTile.getBombs().size()==0 && myBomb.getRangeBombBlock(currentTile, ownHero.getBombRange()) >= 1){
				result = new AiAction(AiActionName.DROP_BOMB);
				this.dropBomb=true;
			}
		}
	}
	private boolean isThereHero() throws StopRequestException{
		checkInterruption();
		boolean hasHero = false;
		Direction[] dirTable = {Direction.DOWN, Direction.RIGHT, Direction.UP, Direction.LEFT};
		List<AiHero> heroList = new ArrayList<AiHero>();
		heroList=zone.getHeroes();
		for(Direction dir:dirTable){
			List<AiTile> heroRange = new ArrayList<AiTile>();
			heroRange=getONeighbour(currentTile, dir, ownHero.getBombRange());
			Iterator<AiTile> itRange = heroRange.iterator();
			AiTile range;
			while(itRange.hasNext()&& hasHero == false){
				checkInterruption();
				range = itRange.next();
				Iterator<AiHero> itHero = heroList.iterator();
				AiHero hero;
				while(itHero.hasNext()&& hasHero == false){
					checkInterruption();
					hero = itHero.next();
					if(range == hero.getTile()){
						hasHero = true;
					}
				}
			}
		}
		return hasHero;
	}
	/////////////////////////////////////////////////////////////////
	// A*				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@SuppressWarnings("deprecation")
	private void getDirectionAndDistance() throws StopRequestException, NullPointerException{	
		checkInterruption();
		AStarCost  cost = new AStarCost(currentMatrix);
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		this.astar = new Astar(this.ai, ownHero, cost, heuristic);
		this.path = null;
		//on trouve la chemine plus proche
		try 
		{
			this.path = this.astar.processShortestPath(this.currentTile, this.tileMax);
		} 
		catch (StopRequestException e) {
			e.printStackTrace();
		} 
		catch (LimitReachedException ex) {
			ex.printStackTrace();
		}
		AiPath tempPath1 = this.path;
		AiPath tempPath2;
		tempPath2 = this.path;
		List<AiTile> casesList = new ArrayList<AiTile>();
		casesList = path.getTiles();
		this.distance=path.getTileDistance();
		Iterator<AiTile> cases = casesList.iterator();
		AiTile tile;
		while(cases.hasNext())
		{
			checkInterruption();
			tile = cases.next();
			if(tile != this.currentTile)
			{			
				try {
					tempPath1 = this.astar.processShortestPath(this.currentTile, tile);
				} catch (StopRequestException e) {
					e.printStackTrace();
				} catch (LimitReachedException e) {
					e.printStackTrace();
				}
				if(tempPath1.isShorterThan(tempPath2) && !tempPath1.isEmpty())
					tempPath2 = tempPath1;
			}
		}
		this.moveDir = zone.getDirection(this.currentTile, tempPath2.getLastTile());
	}
	
	//matrislerin distance hesaplar� i�in yazd�m
	//AMA YUKARDAKIYLE DUZENLENIRSE DAHA GUZEL OLUR
	/////////////////////////////////////////////////////////////////
	// DISTANCE WITH A*	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int getDistance(AiTile start, AiTile target) throws StopRequestException{	
		checkInterruption();
		CostCalculator  costD = new BasicCostCalculator();
		HeuristicCalculator heuristicD = new BasicHeuristicCalculator();
		Astar astarD = new Astar(this.ai, ownHero, costD, heuristicD);
		AiPath pathD = null;
		//on trouve la chemine plus proche
		try 
		{
			pathD = astarD.processShortestPath(start, target);
		} 
		catch (StopRequestException e) {
			e.printStackTrace();
		} 
		catch (LimitReachedException ex) {
			ex.printStackTrace();
		}
		return pathD.getTileDistance();
	}
	
	public int getAstarDistance() throws StopRequestException{
		checkInterruption();
		return this.distance;
		
	}
	
	public List<AiTile> getONeighbour(AiTile tile, Direction dir, int range) throws StopRequestException{
		checkInterruption();
		int i = 0;
		boolean crossable = true;
		List<AiTile> result = new ArrayList<AiTile>();
		AiTile tempTile;
		while(i < range && crossable){
			tempTile = tile.getNeighbor(dir);
			if(tempTile.isCrossableBy(ownHero)){
				result.add(tempTile);
				i++;
				tile = tempTile;
			}
			else
				crossable = false;
		}
		return result;
	}
	/////////////////////////////////////////////////////////////////
	//MATRIX				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	//matrisleri olu�turuyor.
	private void getColMatrix() throws StopRequestException{
		checkInterruption();
		colMatrix = new CollectionMatrix(this);
		colMatrix.createMatrix();
	}
	private void getAttMatrix() throws StopRequestException{
		checkInterruption();
		attackMatrix = new AttackMatrix(this);
		attackMatrix.createMatrix();
	}
	public double[][] getMatrix() throws StopRequestException{
		checkInterruption();
		return currentMatrix;
	}
	//se�ilen moda g�re matrisi al�yor.
	private double[][] getSelectMatrix(boolean mode) throws StopRequestException{
		checkInterruption();
		if (mode){
			return colMatrix.getMatrix();
		}
		else{
			return attackMatrix.getMatrix();
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
		return currentTile;
	}
	//en y�ksek de�erli kareyi buluyor.
	private AiTile getMaxTile() throws StopRequestException{
		checkInterruption();
		double[][] matrix=currentMatrix;
		double max=0;
		int line, col;
		for (line = 0; line < zone.getHeight(); line++) {
			checkInterruption();
			for (col = 0; col < zone.getWidth(); col++) {
				checkInterruption();
				if(matrix[line][col]>max){
					max=matrix[line][col];
					maxLine=line;
					maxCol=col;
				}
			}
	   	}
		
		return zone.getTile(maxLine, maxCol);
	}
	/////////////////////////////////////////////////////////////////
	//Bomb				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int getCurrentBomb() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		return ownHero.getBombNumberMax()-ownHero.getBombNumberCurrent();
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
		return currentX;
	}
	/**
	 * renvoie l'ordonnée courante (en pixels)
	 */
	public double getCurrentY() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		return currentY;
	}
	private void updateLocation() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		currentTile = ownHero.getTile();
		currentX = ownHero.getPosX();
		currentY = ownHero.getPosY();
	}
	
	/////////////////////////////////////////////////////////////////
	// OWN HERO					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie le personnage contr�l� par cette IA
	 */
	public AiHero getOwnHero() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		return ownHero;
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
		return zone;
	}
	
	
}

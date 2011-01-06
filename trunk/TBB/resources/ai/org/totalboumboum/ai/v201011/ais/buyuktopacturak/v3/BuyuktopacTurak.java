package org.totalboumboum.ai.v201011.ais.buyuktopacturak.v3;

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
	private AiHero ownHero = null;
	private AiAction result = new AiAction(AiActionName.NONE);
	private Direction moveDir;
	private Elements element;
	private CollectionMatrix colMatrix;
	private AttackMatrix attackMatrix;
	private double[][] currentMatrix;
	private boolean dropBomb=false;
	private int maxLine, maxCol; //Max deðerin satýr ve sütunu
	/** la case occupée actuellement par le personnage */
	private AiTile currentTile = null;
	/** la position en pixels occupée actuellement par le personnage */
	private double currentX;
	/** la position en pixels occupée actuellement par le personnage */
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
		
		initZibe();
		// si le personnage controlé a été éliminé, inutile de continuer
		if(!this.ownHero.hasEnded())
		{	// on met à jour la position de l'ia dans la zone
			updateLocation();
			
			if(selectMode())
			{
				//System.out.println("Collecte");
				executeCollect();
				
				if(!this.dropBomb)
				{
					getColMatrix();
					this.currentMatrix=getSelectMatrix(true); //currentMatrix.i collectMatrix e eþitliyor.
					//System.out.println("TileMax = " + getMaxTile());
					AstarDirection(this.zone.getOwnHero().getTile(),getMaxTile());
					this.result = new AiAction(AiActionName.MOVE,this.moveDir);
					//this.result = new AiAction(AiActionName.NONE,moveDir);
				}
			}
			else
			{
				//System.out.println("Attack");
				executeAttack();
				if(!this.dropBomb)
				{
					getAttMatrix();
					currentMatrix=getSelectMatrix(false); //currentMatrix.i attackMatrix e eþitliyor.
					//System.out.println("TileMax = " + getMaxTile());
					AstarDirection(this.zone.getOwnHero().getTile(),getMaxTile());
					this.result = new AiAction(AiActionName.MOVE,this.moveDir);
					//this.result = new AiAction(AiActionName.NONE,moveDir);
				}
			}
		}
		return result;
	}
	
	
	/////////////////////////////////////////////////////////////////
	// INITIALISATION			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void initZibe() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		this.zone = getPercepts(); 
		this.ownHero = this.zone.getOwnHero();
		element= new Elements(this);
		updateLocation();
	}
	/////////////////////////////////////////////////////////////////
	// MODE			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean selectMode() throws StopRequestException{
		checkInterruption();
		boolean mode;
		//elimde kalan bomba sayýsýna göre kontrol yapýyor.
		if(this.ownHero.getBombNumberMax()>2){
			if(getCurrentBomb()>2){
				mode=false;
			}
			else{
				mode=true;
			}
		}
		else if(this.ownHero.getBombNumberMax()==2){
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
	private void executeCollect() throws StopRequestException{
		checkInterruption();
		int hiddenBonus;
		int blocks;
		int calcul=10;//Ayný anda 10 kare patlatmak imkansýz		
		//Kýrýlabilir duvarlarý listeye atýyor.
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
		if(element.destroyBonus(this.currentTile,this.ownHero.getBombRange())&&this.currentTile.getBombs().size()==0){
			this.result = new AiAction(AiActionName.DROP_BOMB);
			this.dropBomb=true;
		}
		else if(element.getRangeBombBlockCounter(this.currentTile, this.ownHero.getBombRange()) >= calcul){
			if(element.getRangeBombItemCounter(this.currentTile, this.ownHero.getBombRange())==0 && this.currentTile.getBombs().size()==0){
				this.result = new AiAction(AiActionName.DROP_BOMB);
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
		if(element.getRangeBombHeroCounter(currentTile, this.ownHero.getBombRange())>0&&this.currentTile.getBombs().size()==0){
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
				if(h!=this.ownHero)
					tileList.add(h.getTile());
			}
			CostCalculator  cost = new BasicCostCalculator();
			HeuristicCalculator heuristic = new BasicHeuristicCalculator();
			Astar astarHero = new Astar(this.ai, ownHero, cost, heuristic);
			AiPath pathHero = null;
			//on trouve la chemine plus proche
			//System.out.println("Cost = "+cost.toString());
			try 
			{
				if(tileList.size()!=0)
					pathHero = astarHero.processShortestPath(this.currentTile, tileList);
			} 
			catch (Exception e) {
				e.printStackTrace();
			} 
						
			if(pathHero.getTiles().size()==0 && this.currentTile.getBombs().size()==0 && this.element.getRangeBombBlockCounter(this.currentTile, this.ownHero.getBombRange()) >0){
				this.result = new AiAction(AiActionName.DROP_BOMB);
				this.dropBomb=true;
			}
		}
	}
	
	
	public List<AiTile> getONeighbour(AiTile tile, Direction dir, int range) throws StopRequestException{
		checkInterruption();
		int i = 0;
		boolean crossable = true;
		List<AiTile> result = new ArrayList<AiTile>();
		AiTile tempTile;
		while(i < range && crossable){
			tempTile = tile.getNeighbor(dir);
			if(tempTile.isCrossableBy(this.ownHero)){
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
	// A*				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void AstarDirection(AiTile tile1, AiTile tile2) throws StopRequestException, NullPointerException{	
		checkInterruption();
		AStarCost  cost = new AStarCost(this.currentMatrix);
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		this.astar = new Astar(this.ai, this.ownHero, cost, heuristic);
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
	
	public int AstarDistance(AiTile tile1, AiTile tile2) throws StopRequestException, NullPointerException{	
		checkInterruption();
		int distance;
		CostCalculator  cost = new BasicCostCalculator();
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		Astar astar = new Astar(this.ai, this.ownHero, cost, heuristic);
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
	//matrisleri oluþturuyor.
	private void getColMatrix() throws StopRequestException{
		checkInterruption();
		this.colMatrix = new CollectionMatrix(this);
		this.colMatrix.createMatrix();
	}
	private void getAttMatrix() throws StopRequestException{
		checkInterruption();
		this.attackMatrix = new AttackMatrix(this);
		this.attackMatrix.createMatrix();
	}
	public double[][] getMatrix() throws StopRequestException{
		checkInterruption();
		return this.currentMatrix;
	}
	//seçilen moda göre matrisi alýyor.
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
	//en yüksek deðerli kareyi buluyor.
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
	private void updateLocation() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		this.currentTile = this.ownHero.getTile();
		this.currentX = this.ownHero.getPosX();
		this.currentY = this.ownHero.getPosY();
	}
	
	/////////////////////////////////////////////////////////////////
	// OWN HERO					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie le personnage contrôlé par cette IA
	 */
	public AiHero getOwnHero() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		return this.ownHero;
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
	public int getCurrentBomb() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		return ownHero.getBombNumberMax()-ownHero.getBombNumberCurrent();
	}
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
	
	
	/*public void printCurMatrix() throws StopRequestException{
		int temp;
		double t;
		System.out.println("****************************************************************************************************");
		for (int line = 0; line < zone.getHeight(); line++) 
		{
			checkInterruption();
			for (int col = 0; col < zone.getWidth(); col++) 
			{
				checkInterruption();
				temp=(int)(currentMatrix[line][col]*10);
				t=temp/10.0;
				System.out.print(t+"\t");
				output.setTileText(line,col,""+ t);
				if(col == zone.getWidth()-1)
					System.out.print("\n");
			}
	   	}
		System.out.println("********************************************************************************************************************");
	}*/
	
}

package org.totalboumboum.ai.v201314.ais.enginserhantipici.v4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.TileCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimHero;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.search.Dijkstra;

/**
 *	Class principal of our agent
 *
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
@SuppressWarnings("deprecation")
public class Agent extends ArtificialIntelligence
{
	/**
	 * instance of the class of Agent
	 */
	public Agent()
	{	checkInterruption();
	}
	
	
	@SuppressWarnings("static-access")
	@Override
	protected void initOthers()
	{	checkInterruption();
		
		preferenceHandler.speedAttack = false;
		preferenceHandler.ownHeroInTunnel = false;
			
		List<AiTile> myTiles = getZone().getTiles();
		myTiles = new ArrayList<AiTile>(myTiles);
		int i = 0;
		for(AiTile tile : myTiles){
			checkInterruption();
			this.tileId.put(i,tile);
			i++;
		}
		tlh = new TileHandler(this);

		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts()
	{	checkInterruption();
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	@Override
	protected void updatePercepts()
	{	checkInterruption();
	
		// active/désactive la sortie texte
		verbose = false;
		modeHandler.verbose = false;
		preferenceHandler.verbose = false;
		bombHandler.verbose = false;
		moveHandler.verbose = false;

		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	/////////////////////////////////////////////////////////////////
	// HANDLERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Handler responsible to calculation the of the agent */
	protected ModeHandler modeHandler;
	/** Handler responsible to preference values */
	protected PreferenceHandler preferenceHandler;
	/** Handler responsible to decision to put a bomb or not */
	protected BombHandler bombHandler;
	/**Handler responsible of the direction of the agent  */
	protected MoveHandler moveHandler;
	
	/**Handler for basic tile methods*/
	private TileHandler tlh;
	
	/** 
	 * to check if dijkstra algorithm reaches its limit
	 * We use dijkstra for finding accessible tiles with getAcc() and
	 * findAccessibleTilesBySimHero(AiSimHero simHero, AiSimZone simZone)
	 * methods in this class
	 */
	public boolean dijkstraReachedItsLimits = false;
	
	/**
	 * if there is a hero inside of the trap (triangle attack)
	 * holds the tile of this hero
	 */
	private AiTile tileOfTheHeroInsideTheTrap = null;
	

	/**
	 * This method returns true if our agent can escape from own bomb if it puts a bomb on current tile else returns false.
	 * @return boolean
	 */
	public boolean canEscapeFromOwnBomb(){
		checkInterruption();
		boolean result = false;
		List<AiTile> securityTiles = getAcc();
		securityTiles = new ArrayList<AiTile>(securityTiles);
		List<AiTile> tempSecurityTiles = new ArrayList<AiTile>();
		tempSecurityTiles.addAll(securityTiles);
		AiTile ownTile = getZone().getOwnHero().getTile();
		
		for(AiTile tile : securityTiles){
			checkInterruption();
			if(tile.getPosX() == ownTile.getPosX()){
				if(getZone().getTileDistance(ownTile, tile) <= getZone().getOwnHero().getBombRange()){
					tempSecurityTiles.remove(tile);
				}
			}else{
				if(tile.getPosY() == ownTile.getPosY()){
					if(getZone().getTileDistance(ownTile, tile) <= getZone().getOwnHero().getBombRange()){
						tempSecurityTiles.remove(tile);
					}
				}
			}
			if(tlh.DangerDegree(tile) == 1){
				tempSecurityTiles.remove(tile);
			}
		}
		if(!tempSecurityTiles.isEmpty()){
			result = true;
		}
		
		return result;
	}
	
	/**
	 * This method returns list of tiles that can be accessed form our agent.
	 * @return List of tiles that our agent can access
	 */
	public List<AiTile> getAcc(){
		checkInterruption();
		List<AiTile> accTiles = getZone().getTiles();
		accTiles = new ArrayList<AiTile>(accTiles);
		accTiles.clear();
		
		
		CostCalculator costCalculator = new TileCostCalculator(this);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(this);

		Dijkstra dijk = new Dijkstra(this,getZone().getOwnHero(), costCalculator, successorCalculator);
		Map<AiTile, AiSearchNode> myMap = new HashMap<AiTile, AiSearchNode>();
		
		try {
			myMap = dijk.startProcess(new AiLocation(getZone().getOwnHero().getTile()));
		} catch (LimitReachedException e) {
			dijkstraReachedItsLimits = true;
		//	System.out.println("Dijkstra reach limit, we are inside the Catch block --->" + dijkstraReachedItsLimits );
		//	e.printStackTrace();
			
		}
		if(dijkstraReachedItsLimits){
			try{
				dijkstraReachedItsLimits = false;
		//		System.out.println("Dijkstra reach limit, we are inside the SECOND Catch block --->" + dijkstraReachedItsLimits );
				dijk.setMaxHeight(50);
				myMap = dijk.continueProcess();
				
			}catch (LimitReachedException e) {
				dijkstraReachedItsLimits = true;
		//		e.printStackTrace();
			}	
		}
		accTiles.addAll(myMap.keySet());
		return accTiles;
	}
	
	/**
	 * This methods is similar to getAcc() but it works with simulation classes
	 * it finds accessible tiles for a simHero on a simZone
	 * 
	 * @param simHero the hero simulation whom we want to find accessible tiles for
	 * @param simZone the simulation zone where simHero stays
	 * @return List of AiTile for all the accessible tiles
	 */
	public List<AiTile> findAccessibleTilesBySimHero(AiSimHero simHero, AiSimZone simZone){
		checkInterruption();
		
		List<AiTile> accTiles = getZone().getTiles();
		accTiles = new ArrayList<AiTile>(accTiles);
		accTiles.clear();
		
		CostCalculator costCalculator = new TileCostCalculator(this);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(this);
		AiLocation simLocation = new AiLocation(simZone.getTile(simHero.getTile()));
		

//		DijkstraSimulation dijk = new DijkstraSimulation(this,simHero, simZone, costCalculator, successorCalculator);
		Dijkstra dijkSimLoc = new Dijkstra(this, simHero, costCalculator, successorCalculator);
		Map<AiTile, AiSearchNode> myMap = new HashMap<AiTile, AiSearchNode>();
		boolean limitReached = false;
		
		try {
//			myMap = dijk.startProcess(new AiLocation(simHero.getTile()));
			myMap = dijkSimLoc.startProcess(simLocation);
		} catch (LimitReachedException e) {
			limitReached = true;
			//e.printStackTrace();
		}
		if(limitReached){
			try {
				limitReached = false;
//				dijk.setMaxHeight(48);
//				dijk.continueProcess();
				dijkSimLoc.setMaxHeight(48);
				dijkSimLoc.continueProcess();
				
			} catch (LimitReachedException e) {
				limitReached = true;
				//e.printStackTrace();
			}
		}
		if(limitReached){
			try {
				limitReached = false;
//				dijk.setMaxHeight(56);
//				dijk.continueProcess();
				
				dijkSimLoc.setMaxHeight(56);
				dijkSimLoc.continueProcess();
				
			} catch (LimitReachedException e) {
				limitReached = true;
				//e.printStackTrace();
			}
		}
		
		accTiles.addAll(myMap.keySet());
		return accTiles;
	}
	
	/**
	 * it holds an integer value for each tile in the current game zone
	 */
	private Map<Integer,AiTile> tileId = new HashMap<Integer,AiTile>();
	
	/**
	 * it returns an identifier for a tile
	 * @param tile AiTile 
	 * @return integer : id of the tile
	 */
	public int tileIdentification(AiTile tile){
		checkInterruption();
		int result = -1;
		
			for(int i=0; i < tileId.keySet().size();i++){
				checkInterruption();
			if(tileId.get(i) == tile){
				
				result = i;	
				break;
			}	
			}	
		return result;
	}

		
	/**
	 * getter for getTileOfTheHeroInsideTheTrap()
	 * @return the tile of the hero inside the trap
	 */
	public AiTile getTileOfTheHeroInsideTheTrap() {
		checkInterruption();
		return tileOfTheHeroInsideTheTrap;
	}
	/**
	 * setter for getTileOfTheHeroInsideTheTrap()
	 * it should be use in pattern handler
	 * @param tileOfTheHeroInsideTheTrap hero
	 */
	public void setTileOfTheHeroInsideTheTrap(AiTile tileOfTheHeroInsideTheTrap) {
		checkInterruption();
		this.tileOfTheHeroInsideTheTrap = tileOfTheHeroInsideTheTrap;
	}
	
	
	
	/**
	 * if there is a pattern for applying triangle attack strategy
	 */
	public boolean PatternExist = false;
	
	
	/**
	 * list for holding the selected tile for triangle attack strategy
	 * myTiles(0) n myTiles(1) hold the corners and
	 * myTiles(2) holds the middle tile
	 * if there is specific pattern, myTiles(0) also shows the tile where there is enemy is staying near of it(inside of the trap)  
	 */
	public List<AiTile> myTiles = null;
	
	/**
	 * getter for myTiles
	 * @return myTiles
	 */
	public List<AiTile> getMyTiles() {
		checkInterruption();
		return myTiles;
	}

	/**
	 * setter for myTiles
	 * @param myTiles list of AiTiles
	 */
	public void setMyTiles(List<AiTile> myTiles) {
		checkInterruption();
		this.myTiles = myTiles;
	}

	/**
	 * 
	 * @return true if the pattern exist
	 */
	public boolean isPatternExist() {
		checkInterruption();
		return PatternExist;
	}

	/**
	 * setter for PatternExist
	 * @param patternExist true if pattern exist
	 */
	public void setPatternExist(boolean patternExist) {
		checkInterruption();
		PatternExist = patternExist;
	}

	@Override
	protected void initHandlers()
	{	checkInterruption();
		
		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler(this);
		preferenceHandler = new PreferenceHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);
	
		// b = preferenceHandler.isThereATileNearByEnemy;
		
		
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}


	/**
	 * List of tiles that is in the current tunnel
	 */
	public List<AiTile> tilesInTunnel;
	
	/**
	 * Getter for tilesInTunnel
	 * @return the list of tiles in tunnel
	 */
	public List<AiTile> getTilesInTunnel() {
		checkInterruption();
		return tilesInTunnel;
	}

	/**
	 *  Setter for tilesInTunnel
	 * @param tilesInTunnel List of tiles that is in the current tunnel
	 */
	public void setTilesInTunnel(List<AiTile> tilesInTunnel) {
		checkInterruption();
		this.tilesInTunnel =  new ArrayList<AiTile>(tilesInTunnel);
	}
	
	@Override
	protected AiModeHandler<Agent> getModeHandler()
	{	checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiPreferenceHandler<Agent> getPreferenceHandler()
	{	checkInterruption();
		return preferenceHandler;
	}

	@Override
	protected AiBombHandler<Agent> getBombHandler()
	{	checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<Agent> getMoveHandler()
	{	checkInterruption();
		return moveHandler;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput()
	{	checkInterruption();
	
		// vous pouvez changer la taille du texte affiché, si nécessaire
		// attention: il s'agit d'un coefficient multiplicateur
		AiOutput output = getOutput();
		output.setTextSize(2);


		// ici, par défaut on affiche :
			// les chemins et destinations courants
			moveHandler.updateOutput();
			// les preferences courantes
			preferenceHandler.updateOutput();
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
}

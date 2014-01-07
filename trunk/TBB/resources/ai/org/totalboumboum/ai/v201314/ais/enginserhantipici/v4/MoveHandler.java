package org.totalboumboum.ai.v201314.ais.enginserhantipici.v4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimHero;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.MatrixCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.TileCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.TileHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.search.Astar;
import org.totalboumboum.ai.v201314.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * This class manages the displacement of the agent 
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
@SuppressWarnings("deprecation")
public class MoveHandler extends AiMoveHandler<Agent>
{	
	
	/**
	 *construct a handler for agent to pass a parameter
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	
	protected MoveHandler(Agent ai)
    {	super(ai);
	ai.checkInterruption();
	setDestructibleWallSearch(false);
	
	zone = ai.getZone();
	ownHero = zone.getOwnHero();
	costMatrix = new double[zone.getHeight()][zone.getWidth()];

	CostCalculator costCalculator = new TileCostCalculator(ai);
	HeuristicCalculator heuristicCalculator = new TileHeuristicCalculator(ai);
	SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
	SuccessorCalculator approxScsCalculator = new ApproximateSuccessorCalculator(ai);

	astar = new Astar(ai, ownHero, costCalculator, heuristicCalculator, successorCalculator);
	approxAstar = new Astar(ai, ownHero, costCalculator, heuristicCalculator, approxScsCalculator);
    }

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** game zoe */
	private AiZone zone = null; 
	/** our hero */
	private AiHero ownHero = null; 
	/** instead of creating in every iteration we create an object a* */
	private Astar astar = null;
	
	/**
 	* TileHandler
 	*/
	private TileHandler tlh;
	/**
	 * for calculating cost for A*
	 */
	private double[][] costMatrix = null; 
	
	/**
	 * A* for security category
	 */
	private Astar astarSecurity = null;

	/**
	 * Getter for speedAttackFinished
	 * @return boolean variable speedAttackFinished
	 */
	public boolean isSpeedAttackFinished() {
		ai.checkInterruption();
		return speedAttackFinished;
	}
	/**
	 * Setter for boolean variable speedAttackFinished
	 * @param speedAttackFinished boolean variable
	 */
	public void setSpeedAttackFinished(boolean speedAttackFinished) {
		ai.checkInterruption();
		this.speedAttackFinished = speedAttackFinished;
	}
	
	/**
	 * Getter for boolean variable targetAtTheDeadEnd
	 * @return boolean
	 */
	public boolean isTargetAtTheDeadEnd() {
		ai.checkInterruption();
		return targetAtTheDeadEnd;
	}
	/**
	 * Setter for boolean variable targetAtTheDeadEnd
	 * @param targetAtTheDeadEnd boolean value
	 */
	public void setTargetAtTheDeadEnd(boolean targetAtTheDeadEnd) {
		ai.checkInterruption();
		this.targetAtTheDeadEnd = targetAtTheDeadEnd;
	}

	/**
	 * A star path algorithm for searching category
	 */
	private Astar approxAstar = null;
	
	
	/**
	 * if there is a possibility of being stucked inside of a tunnel
	 */
	public boolean stuckPossibleInTunnel = false;
	
	/**
	 * to check if a bomb placed for a dead end category
	 */
	public static volatile boolean bombPosedForTunnel = false;
	
	/**
	 * if the tile is closed to own hero
	 */
	@SuppressWarnings("unused")
	private boolean tileClosed = false;
	
	/**
	 * to check is destructible wall search is current category
	 */
	public boolean destructibleWallSearch;
	
	/**
	 * to check if speed attack finished
	 */
	public boolean speedAttackFinished;
	
/**
 * if the target in a closed tunnel
 */
	public boolean targetAtTheDeadEnd;
	
	/**
	 * As if a bomb is put in a iteration, motor doesn't pass to moveHandler to see if the agent consider moving or not
	 * flagBombLastIt holds the result value of bombHandler from last iteration
	 * 
	 */
	private boolean flagBombLastIt = false;

	/**
	 * getter for flagBombLastIt
	 * @return itself
	 */
	public boolean isFlagBombLastIt() {
		ai.checkInterruption();
		return flagBombLastIt;
	}
	/**
	 * setter for flagBombLastIt
	 * @param flagBombLastIt set true if a bomb put at last iteration
	 */
	public void setFlagBombLastIt(boolean flagBombLastIt) {
		ai.checkInterruption();
		this.flagBombLastIt = flagBombLastIt;
	}
	
	/**
	 * Getter for destructibleWallSearch
	 * @return boolean
	 */
	public boolean isDestructibleWallSearch() {
		ai.checkInterruption();
		return destructibleWallSearch;
	}
	/**
	 * Setter for destructibleWallSearch
	 * @param destructibleWallSearch boolean value
	 */
	public void setDestructibleWallSearch(boolean destructibleWallSearch) {
		ai.checkInterruption();
		this.destructibleWallSearch = destructibleWallSearch;
	}
	
	
	/////////////////////////////////////////////////////////////////
	// DESTINATION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	@Override
	protected AiTile processCurrentDestination()
	{	ai.checkInterruption();
		AiTile result = null;
		
		AiPreferenceHandler<Agent> preferenceHandler = ai.getPreferenceHandler();
		Map<Integer, List<AiTile>> preferences = preferenceHandler.getPreferencesByValue();

		int minPref = Collections.min(preferences.keySet());	 
		List<AiTile> tiles = preferences.get(minPref);			
		
		//ai.tileidentification!!!!
		AiTile destinatedTile = tiles.get(0);
		for(AiTile tile : tiles){
			ai.checkInterruption();
			if(ai.tileIdentification(tile) < ai.tileIdentification(destinatedTile))
				destinatedTile = tile;
		}
		
		result = destinatedTile;
		
		if(ai.getZone().getTileDistance(result, ai.getZone().getOwnHero().getTile()) < 2){
			this.tileClosed = true;
		}

		return result;
	}

	/////////////////////////////////////////////////////////////////
	// PATH						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiPath processCurrentPath()
	{	ai.checkInterruption();
		AiPath result = null;
		AiLocation startLocation = new AiLocation(ownHero);

		AiTile endTile = getCurrentDestination();
	//	if(!tileClosed){

		if(ai.preferenceHandler.selectedTileType == 6){//SEARCH CATEGORY
			AiLocation fakeLocation = new AiLocation(zone.getOwnHero());
			try	
			{	
				result = approxAstar.startProcess(fakeLocation,endTile);
			}
			catch (LimitReachedException e)
			{	//e.printStackTrace();
				result = new AiPath();
			}
		}else if(ai.preferenceHandler.selectedTileType == 8){//SPEED ATTACK	
			result = new AiPath();
			result.addLocation(startLocation);
			AiLocation endLocation = new AiLocation(endTile);
			result.addLocation(endLocation);
		}else if(ai.preferenceHandler.selectedTileType == 2){//TRIANGLE ATTACK
//			System.out.println("path tiles --->" + ai.preferenceHandler.tilesForTriangleAttackPath );
			if(ai.preferenceHandler.tilesForTriangleAttackPath.contains(ownHero.getTile())){	
				if(ai.preferenceHandler.tilesForTriangleAttackPath.contains(endTile)){
	
					int start = ai.preferenceHandler.tilesForTriangleAttackPath.indexOf(ownHero.getTile());
					int end = ai.preferenceHandler.tilesForTriangleAttackPath.indexOf(endTile);
					result = new AiPath();
					
					if(start<end){
						for(int i= start; i<=end ; i++){
							ai.checkInterruption();
							result.addLocation(new AiLocation( ai.preferenceHandler.tilesForTriangleAttackPath.get(i)));
						}
					}else if(start>end){
						for(int i= start; i>=end ; i--){
							ai.checkInterruption();
							result.addLocation(new AiLocation( ai.preferenceHandler.tilesForTriangleAttackPath.get(i)));
						}
					}else{//start==end
					result = new AiPath();
					result.addLocation(startLocation);
					}
//					System.out.println("path --->" + result);	
				}else{
//					System.out.println("ERROR ---> Dstinated destination tile couldn't find in tilesForTriangleAttackPath for TRIANGLE_ATTACK");
					result = new AiPath();
					result.addLocation(startLocation);
				}
			}else{
				try
				{
					ai.checkInterruption();
					result = astar.startProcess(startLocation,endTile);
				}
				catch (LimitReachedException e)
				{	//e.printStackTrace();		
					result = new AiPath();
				}	
			}
			
		}else if(ai.preferenceHandler.selectedTileType == 1){//SECURITY
	
			for(int i = 0; i < zone.getHeight() ; i++){
				ai.checkInterruption();
				for(int j = 0; j <zone.getWidth() ;j++){
					ai.checkInterruption();
					costMatrix[i][j] = 0;
					}
				}
					for(Long delay : zone.getBombsByDelays().keySet()){
						ai.checkInterruption();
						for(AiBomb bomb : zone.getBombsByDelays().get(delay)){
							ai.checkInterruption();
							 for(AiTile t : bomb.getBlast()){
								 ai.checkInterruption();
								 if(!zone.getTile(t.getRow(), t.getCol()).getFires().isEmpty())
									 costMatrix[t.getRow()][t.getCol()] = 40000; //infinity
								 else
									 costMatrix[t.getRow()][t.getCol()] = bomb.getNormalDuration() - (double)delay;
							 }
							}
						}
					SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
					HeuristicCalculator timeHeuristicCalculator = new TileHeuristicCalculator(ai);
					CostCalculator matrixCostCalculator = new MatrixCostCalculator(ai, costMatrix);	
					astarSecurity = new Astar(ai,ownHero, matrixCostCalculator,timeHeuristicCalculator,successorCalculator);

			try{
				ai.checkInterruption();
				
				result = astarSecurity.startProcess(startLocation, endTile); 
				
			}catch (LimitReachedException e){
				result = new AiPath();
			}
		/*TO PRINT THE COST MATRIX	
			for(int i = 0; i < zone.getHeight(); i++){
				ai.checkInterruption();
				for(int j = 0; j < zone.getWidth();j++){
					ai.checkInterruption();
					System.out.print(" -- " + costMatrix[i][j]);
					}
				System.out.println("");
				}
		 */	
		}else{
		
		try
		{
			ai.checkInterruption();
			result = astar.startProcess(startLocation,endTile);
		}
		catch (LimitReachedException e)
		{	//e.printStackTrace();		
			result = new AiPath();
		}
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	@SuppressWarnings("static-access")
	@Override
	protected Direction processCurrentDirection()
	{	ai.checkInterruption();
	Direction result = Direction.NONE;
	tlh = new TileHandler(ai);

	setTargetAtTheDeadEnd(false);
	
	AiPath path = getCurrentPath();
	
	if(path==null || path.getLength()<2 )	
			result = Direction.NONE;

	else
		{	
		AiLocation nextLocation = path.getLocation(1);		// un chemin est une séquence de AiLocation (position en pixel), chaque AiLocation contient la AiTile correspondant à la position.	
		AiTile nextTile = nextLocation.getTile();

//		System.out.println("We are in MOVE handler and selected tile type = " + ai.preferenceHandler.selectedTileType);
		if( (ai.preferenceHandler.selectedTileType !=1 && tlh.DangerDegree(nextTile) == 1  || !nextTile.getFires().isEmpty()) && ai.preferenceHandler.selectedTileType != 2){
			
			result =Direction.NONE;
		
		}else if(!nextTile.getBlocks().isEmpty() && ai.preferenceHandler.selectedTileType == 6 && !flagBombLastIt){//approxAstar
					
				result = Direction.NONE;
					setDestructibleWallSearch(true);
		}
		
		else if(ai.preferenceHandler.selectedTileType == 7){//DEAD END ABSOLUTE
//			System.out.println("ownhero index in tilesintunnel = " + ai.getTilesInTunnel().indexOf(ownHero.getTile()));
			if(ai.getTilesInTunnel().contains(ownHero.getTile()) && ai.getTilesInTunnel().indexOf(ownHero.getTile()) != 0){
				AiTile target = ai.getTilesInTunnel().get(ai.getTilesInTunnel().indexOf(ownHero.getTile()) - 1);
				for(AiHero enemy : zone.getRemainingOpponents()){
					ai.checkInterruption();
					if(!tlh.isATileCompetible(ownHero, enemy, target)){
						stuckPossibleInTunnel = true;
						break;
					}

				}
			}
				AiTile currentTile = ownHero.getTile();
			if(stuckPossibleInTunnel || bombPosedForTunnel){
				result = zone.getDirection(currentTile, ai.getTilesInTunnel().get(0));
				bombPosedForTunnel = false;
			}else{
				result = zone.getDirection(currentTile, nextTile);
			}

		}else if(ai.preferenceHandler.selectedTileType == 2 && nextTile.getFires().isEmpty()){//ATTACK TRIANGLE
			if(tlh.isInDangerTest(nextTile, 1666)){
				result = Direction.NONE;
			}else{
			AiTile currentTile = ownHero.getTile();
			result = zone.getDirection(currentTile, nextTile);
			}
		}
		else if(ai.preferenceHandler.selectedTileType == 1 && !ownHero.getTile().getBombs().isEmpty()){//Security

			AiSimZone simZone = new AiSimZone(zone);
			AiSimZone tempSimZone = new AiSimZone(simZone);
			ArrayList<AiTile> blastTiles = new ArrayList<AiTile>();

			for(AiItem item : tempSimZone.getItems()){
				ai.checkInterruption();
				simZone.removeSprite(simZone.getSpriteById(item));
			}
			for(AiTile tile : simZone.getOwnHero().getTile().getNeighbors()){
				ai.checkInterruption();
				if(tile.isCrossableBy(ownHero, false, false, false, true, true, true)) {

					AiSimHero simHero = null;
					simHero = simZone.createHero(tile, ownHero.getColor(), ownHero.getBombNumberCurrent(), ownHero.getBombRange(), false);		
					int bombRange = ownHero.getBombRange();
						if(!ownHero.getTile().getBombs().isEmpty())
								bombRange = ownHero.getTile().getBombs().get(0).getRange();
					simZone.createBomb(ownHero.getTile(), bombRange, ownHero.getBombDuration());

					List<AiTile> listOfAcc = zone.getTiles();
					listOfAcc = new ArrayList<AiTile>(listOfAcc);
					listOfAcc.clear();
						listOfAcc = ai.findAccessibleTilesBySimHero(simHero, simZone);
					List<AiTile> tempListOfAcc = new ArrayList<AiTile>(listOfAcc);		
					
					for(AiBomb bomb : simZone.getBombs()){
						ai.checkInterruption();
						blastTiles.addAll(bomb.getBlast());
					}
					
					for(AiTile simTile : listOfAcc){
						ai.checkInterruption();
						if(blastTiles.contains(simTile))
							tempListOfAcc.remove(simTile);	
					}

					if(!tempListOfAcc.isEmpty()){
					//first safe direction
						result = zone.getDirection(ownHero.getTile(),tile);
						break;
						}
				}


			}

		}
			
		else{
			
			AiTile currentTile = ownHero.getTile();
			result = zone.getDirection(currentTile, nextTile);
		}
		
		if(ai.bombHandler.dangerousDestinations != null){
			if(ai.bombHandler.dangerousDestinations.contains(nextTile) && ai.bombHandler.aBombPutAtLastIteration){
				result = Direction.NONE;
				ai.bombHandler.aBombPutAtLastIteration = false;
			}
		}
		if(tlh.isInDangerTest(nextTile, 333) || !nextTile.getFires().isEmpty())
			result = Direction.NONE;
		
		}
	
	
ai.bombHandler.aBombPutAtLastIteration = false;	//STATIC VARIABLE - it need to be "false" for each next iteration before bombHandler
return result;
}



	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput()
	{	ai.checkInterruption();
		
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();

	}
}

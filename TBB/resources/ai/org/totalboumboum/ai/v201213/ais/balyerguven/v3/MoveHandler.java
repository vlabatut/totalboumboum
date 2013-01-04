package org.totalboumboum.ai.v201213.ais.balyerguven.v3;




import java.util.List;
import java.util.Map;



import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.ai.v201213.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201213.adapter.path.cost.ApproximateCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.search.Astar;
import org.totalboumboum.ai.v201213.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201213.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;


/**
 * our move handler class.
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */
public class MoveHandler extends AiMoveHandler<BalyerGuven>
{	
	/**
	 * represents zone
	 */
	protected AiZone zone = null;
	/** represents our hero */
	protected AiHero myHero = null;
	/** represents current tile*/
	protected AiTile currentTile = null;
	/** represents next tile */
	protected AiTile nextTile = null;
	/** represents secure tile */
	protected AiTile secureTile = null;
	/** represents astar 1*/
	protected Astar astar1 = null;
	/** represents astar 2*/
	protected Astar astar2 = null;
	/** represents dijkstra*/
	protected Dijkstra dijkstra = null;	
	/** represents secure tile control*/
	protected AiTile secureTileControl = null;

	
	/**
	 * Constructs a handler for the agent passed as a parameter.
	 * 
	 * @param ai
	 *            The agent that the class will handle.
	 * 
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected MoveHandler(BalyerGuven ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		
		verbose = false;
		
		zone = ai.getZone();
		myHero = zone.getOwnHero();
		
		{	CostCalculator costCalculator = new TimeCostCalculator(ai,myHero);
		costCalculator.setOpponentCost(1000);
		HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai,myHero);
		SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(ai,TimePartialSuccessorCalculator.MODE_NOTREE);
		astar1 = new Astar(ai,myHero, costCalculator, heuristicCalculator, successorCalculator);
		}

		{	CostCalculator costCalculator = new ApproximateCostCalculator(ai,myHero);
		costCalculator.setOpponentCost(1000);
		HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai,myHero);
		SuccessorCalculator successorCalculator = new ApproximateSuccessorCalculator(ai);
		astar2 = new Astar(ai,myHero, costCalculator, heuristicCalculator, successorCalculator);
		}

		{	CostCalculator costCalculator = new TimeCostCalculator(ai,myHero);
		costCalculator.setOpponentCost(1000);
		SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(ai,TimePartialSuccessorCalculator.MODE_NOBRANCH);
		dijkstra = new Dijkstra(ai,myHero, costCalculator,successorCalculator);
		}


	}


	@Override
	protected AiTile updateCurrentDestination() throws StopRequestException
	{	

		this.ai.checkInterruption();
		zone = ai.getZone();
		myHero = zone.getOwnHero();
		
		AiTile destination = null;
	
		Map<AiTile, Float> hashmap = ai.utilityHandler.getUtilitiesByTile();
		hashmap =ai.utilityHandler.getUtilitiesByTile();

		float value = -1;
		

		for (AiTile currentTile : hashmap.keySet()) {
			
		
			
			this.ai.checkInterruption();
			
			if (ai.utilityHandler.getUtilitiesByTile().get(currentTile) > value)
			{
				
				value = ai.utilityHandler.getUtilitiesByTile().get(currentTile);
				destination = currentTile;

			}
		}
		

		
		if(destination==null){
			destination=ai.getNearestEnemy().getTile();
		}
		
		return destination;
		
	}

	/////////////////////////////////////////////////////////////////
	// PATH						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiPath updateCurrentPath() throws StopRequestException
	{	
		ai.checkInterruption();
		zone=ai.getZone();
		AiHero myHero=zone.getOwnHero();

		currentTile = myHero.getTile();
		currentPath = null;
		AiLocation ownLocation = new AiLocation(myHero);
		
		
		

		if (secureTile == null) {
			try {
				currentPath = dijkstra.processEscapePath(ownLocation);
	
			} catch (LimitReachedException e) {
				e.printStackTrace();
			}

			if (currentPath != null || !currentPath.isEmpty()) {

				AiLocation lastLocation = currentPath.getLastLocation();
				secureTile = lastLocation.getTile();
				secureTileControl = secureTile;

			}
		}

		if (secureTile != null) {

			if (currentTile.equals(secureTile))
				secureTile = null;
			else {
				try {
					
					currentPath = astar2.startProcess(ownLocation, currentDestination);
				
				} catch (LimitReachedException e) {
					e.printStackTrace();
				}
			}
		}

		if (secureTile == null) {
			try {
				
				currentPath = astar2.startProcess(ownLocation,currentDestination);
			} catch (LimitReachedException e) {
				e.printStackTrace();
			}
			if (currentPath == null || currentPath.isEmpty()) {
				try {
					
					currentPath = astar1.startProcess(ownLocation,currentDestination);
				} catch (LimitReachedException e) {
					e.printStackTrace();
				}
			}

		}

		return currentPath;
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Direction updateCurrentDirection() throws StopRequestException
	{			
	ai.checkInterruption();

	List<AiBomb> bombs=zone.getBombs();
	


	if (currentPath != null) {
		long wait = currentPath.getFirstPause();

		if (currentPath.getLength() >= 2 && wait <= 0)
		{
			AiLocation sourceLocation = currentPath.getFirstLocation();
			AiLocation targetLocation = currentPath.getLocation(1);
			currentDirection = zone.getDirection(sourceLocation.getTile(),targetLocation.getTile());
			nextTile = targetLocation.getTile();
		}
	}
	
	
	int controlBombNextTile = 0;
	int controlBombCurrentTile = 0;

	for (int a = 0; a < bombs.size(); a++) {
		ai.checkInterruption();

		if (bombs.get(a).getBlast().contains(nextTile)) {
			controlBombNextTile++;
		}

	}

	for (int a = 0; a < bombs.size(); a++) {
		ai.checkInterruption();

		if (bombs.get(a).getBlast().contains(currentTile)) {
			controlBombCurrentTile++;
		}

	}

	if (controlBombNextTile >= 1 && controlBombCurrentTile == 0) {
		currentDirection= Direction.NONE;
	}

	if (!nextTile.getFires().isEmpty() || nextTile.equals(currentTile)){
		currentDirection= Direction.NONE;
	}
	
	
	return currentDirection;

}
	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
		
	}
}




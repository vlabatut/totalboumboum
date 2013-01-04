package org.totalboumboum.ai.v201213.ais.balyerguven.v2;


import java.util.Map;


import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
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
 * @author BalyerGuven
 *
 */
public class MoveHandler extends AiMoveHandler<BalyerGuven>
{	
	/**
	 * 
	 */
	protected AiZone zone = null;
	/** */
	protected AiHero myHero = null;
	/** */
	protected AiTile currentTile = null;
	/** */
	protected AiTile nextTile = null;
	/** */
	protected AiTile secureTile = null;
	/** */
	protected Astar astar1 = null;
	/** */
	protected Astar astar2 = null;
	/** */
	protected Dijkstra dijkstra = null;	
	/** */
	protected AiTile secureTileControl = null;

	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
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

	
	
	/**
	 * @return AiTile
	 * @throws StopRequestException
	 */

	@Override
	protected AiTile updateCurrentDestination() throws StopRequestException
	{	
		this.ai.checkInterruption();
		AiTile destination = null;
		Map<AiTile, Float> hashmap;
		hashmap = ai.utilityHandler.getUtilitiesByTile();

		float value = -1;

		for (AiTile currentTile : hashmap.keySet()) {
			this.ai.checkInterruption();
			if (ai.utilityHandler.getUtilitiesByTile().get(currentTile) > value
					) {
				value = ai.utilityHandler.getUtilitiesByTile().get(
						currentTile);
				destination = currentTile;

			}
		}
		
		
		
		
		return destination;
		
	}

	/////////////////////////////////////////////////////////////////
	// PATH						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiPath updateCurrentPath() throws StopRequestException
	{	ai.checkInterruption();
		
		zone = ai.getZone();
		myHero = zone.getOwnHero();
		currentTile = myHero.getTile();
		currentPath = null;
		AiLocation ownLocation = new AiLocation(myHero);
		
	
		
		
		if(secureTile==null)
		{
			try
			{	
				
				currentPath = dijkstra.processEscapePath(ownLocation);
				
			}
			catch (LimitReachedException e)
			{	e.printStackTrace();
			}
			
			
			if(currentPath!=null || !currentPath.isEmpty()){
			
				AiLocation lastLocation = currentPath.getLastLocation();
				secureTile=lastLocation.getTile();
				secureTileControl=secureTile;
				
			}
		}
		
		if(secureTile!=null)
		{
			
			if(currentTile.equals(secureTile))
				secureTile=null;
			else
			{
				try {
						currentPath = astar1.startProcess(ownLocation, secureTile);
					} catch (LimitReachedException e) {
				e.printStackTrace();
					}
			}
		}
	
		if(secureTile==null)
		{
			try
			{	
				currentPath = astar1.startProcess(ownLocation,currentDestination);
			}
			catch (LimitReachedException e)
			{	
				e.printStackTrace();
			}
			if(currentPath==null || currentPath.isEmpty())
			{
				try
				{	
					currentPath = astar2.startProcess(ownLocation,currentDestination);
				}
				catch (LimitReachedException e)
				{	
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
	AiLocation location;
	
	
	try
	{	
		location = currentPath.getLocation(1);
	}
	catch (IndexOutOfBoundsException e)
	{	
		location = currentPath.getLocation(0);
	}
	
	
	zone = ai.getZone();
	myHero = zone.getOwnHero();
	double ownX = myHero.getPosX();
	double ownY = myHero.getPosY();
	double nextX = location.getPosX();
	double nextY = location.getPosY();
	
	
	if(nextX == ownX && nextY > ownY){
		return Direction.DOWN;
	}
	else if(nextX == ownX && nextY < ownY){
		return Direction.UP;
	}
	else if(nextX > ownX && nextY > ownY){
		return Direction.DOWNRIGHT;
	}
	else if(nextX > ownX && nextY < ownY){
		return Direction.UPRIGHT;
	}
	else if(nextX > ownX && nextY == ownY){
		return Direction.RIGHT;
	}
	else if(nextX < ownX && nextY > ownY){
		return Direction.DOWNLEFT;
	}
	else if(nextX < ownX && nextY < ownY){
		return Direction.UPLEFT;
	}
	else if(nextX < ownX && nextY == ownY){
		return Direction.LEFT;
	}
	
	return Direction.NONE;

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




package org.totalboumboum.ai.v201213.ais.balyerguven.v1;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

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
	 * @return Bool
	 * @throws StopRequestException
	 */
	@Override
	protected AiTile updateCurrentDestination() throws StopRequestException
	{	
		ai.checkInterruption();
		
		zone = this.ai.getZone();
		myHero = zone.getOwnHero();
		
		Map<Float,List<AiTile>> utilitiesByValue = ai.utilityHandler.getUtilitiesByValue();
		TreeSet<Float> values = new TreeSet<Float>(utilitiesByValue.keySet());
		Iterator<Float> it = values.descendingIterator();
	
		while(it.hasNext())
		{
			ai.checkInterruption();	
	
			float myUtility = it.next();
	
			List<AiTile> tiles = utilitiesByValue.get(myUtility);
	
			Collections.sort(tiles);	
	
			if(!tiles.isEmpty())
			{	
				currentDestination = tiles.get(0);
			}
	
		}
		
		return currentDestination;
		
	}

	/////////////////////////////////////////////////////////////////
	// PATH						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiPath updateCurrentPath() throws StopRequestException
	{	ai.checkInterruption();
		
		currentTile = myHero.getTile();
		currentPath = null;
		AiLocation ownLocation = new AiLocation(myHero);
		
		AiLocation ownLoca = new AiLocation(myHero); 
	
		
		if(secureTile==null)
		{
			try
			{	currentPath = dijkstra.processEscapePath(ownLocation);
			
			
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
						currentPath = astar1.startProcess(ownLoca, secureTile);
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
	AiLocation location = currentPath.getLocation(1);
	
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




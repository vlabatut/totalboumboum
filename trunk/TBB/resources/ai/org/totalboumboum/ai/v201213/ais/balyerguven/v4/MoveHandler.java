package org.totalboumboum.ai.v201213.ais.balyerguven.v4;

import java.util.Iterator;
import java.util.List;

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
import org.totalboumboum.ai.v201213.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;


/**
 * our move handler class.
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */
@SuppressWarnings("deprecation")
public class MoveHandler extends AiMoveHandler<BalyerGuven>
{	
	/**
	 * represents zone
	 */
	protected AiZone zone = null;
	/** represents our hero */
	protected AiHero myHero = null;
	/** represents A* precise */
	protected Astar astar1 = null;
	/** represents A* approximation */
	protected Astar astar2 = null;
	/**
	 * control for bombing
	 */
	public boolean control = false;
	/**
	 * represents the tile of next destination.
	 */
	public AiTile nextDest = null;
	/**
	 * represents the tile of secure destination.
	 */
	public AiTile secureDest = null;

	
	
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
		myHero = ai.getHero();
		
		{
			CostCalculator costCalculator = new TimeCostCalculator(ai, myHero);
			costCalculator.setOpponentCost(1000);
			costCalculator.setMalusCost(1000);
			HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai, myHero);
			SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(ai, TimePartialSuccessorCalculator.MODE_NOTREE);
			astar1 = new Astar(ai, myHero, costCalculator, heuristicCalculator, successorCalculator);
		}
		
		{
			CostCalculator costCalculator = new ApproximateCostCalculator(ai, myHero);
			HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai, myHero);
			SuccessorCalculator successorCalculator = new ApproximateSuccessorCalculator(ai);
			astar2 = new Astar(ai, myHero, costCalculator, heuristicCalculator, successorCalculator);
		}
	}

	
	
	@Override
	protected AiTile updateCurrentDestination() throws StopRequestException
	{
		ai.checkInterruption();
		AiTile currentDestination = null;
		if ( ai.getCurrentDangerousTiles().contains(myHero.getTile()) )
		{
			currentDestination = ai.getClosestSecureTile();
		}
		else
			{
				currentDestination = ai.getBiggestTile();
			}
		nextDest = currentDestination;
		return currentDestination;
	}

	/////////////////////////////////////////////////////////////////
	// PATH						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiPath updateCurrentPath() throws StopRequestException
	{	
		ai.checkInterruption();
		AiPath currentPath = null;
		AiHero ownHero = ai.getZone().getOwnHero();
		AiLocation firstLocation = new AiLocation( ownHero  );
		control = false;
		if ( ai.getReachableTiles(ai.getZone().getOwnHero().getTile() ).contains(currentDestination) )
		{
			try {
				currentPath = astar1.startProcess( firstLocation, currentDestination );
			}
			catch ( LimitReachedException e ) {
				// e.printStackTrace();
			}
		}
		else
		{
			try {
				currentPath = astar2.startProcess( firstLocation, currentDestination );
				if ( currentPath != null )
				{
					Iterator<AiLocation> iterator = currentPath.getLocations().iterator();
					AiTile blockTile = null;
					AiTile previousTile = null;
					while(iterator.hasNext() && blockTile == null)
					{
						ai.checkInterruption();
						AiTile tile = iterator.next().getTile();
						if ( !tile.getBlocks().isEmpty() )
							blockTile = previousTile;
						previousTile = tile;
						if ( blockTile != null && blockTile.getBombs().isEmpty() )
						{
							secureDest = blockTile;
							if ( secureDest.equals( ownHero.getTile() ) )
								control = true;
							else
							{
								return astar1.startProcess( firstLocation, secureDest );
							}
						}
					}
					return currentPath;
				}
			}
			catch ( LimitReachedException e ) {
				// e.printStackTrace();
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
		if (currentPath != null && currentPath.getLength() >= 2 )
		{
			AiTile currentTile = ai.getHero().getTile();
			AiTile nextTile = getNextTileOnPath(currentTile);
			List<AiTile> dangerousTiles = ai.getCurrentDangerousTiles();
			if ( dangerousTiles.contains(currentTile) || !dangerousTiles.contains(nextTile) )
				return zone.getDirection(currentTile, nextTile);
		}
		return Direction.NONE;
	}

	/**
	 *
	 * method for the next tile on our path
	 * @param tile
	 * 
	 * @return AiTile
	 * 
	 * @throws StopRequestException
	 *            If the engine demands the termination of the agent.
	 */
	public AiTile getNextTileOnPath( AiTile tile ) throws StopRequestException
	{
		ai.checkInterruption();
		Iterator<AiLocation> iterator = currentPath.getLocations().iterator();
		boolean isOurTile = false;
		
		while(iterator.hasNext() && !isOurTile)
		{
			ai.checkInterruption();
			isOurTile = ( tile == iterator.next().getTile() );
		}
		if(iterator.hasNext()){
			return iterator.next().getTile();
		}
		else{
			return null;
		}
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




package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v2;

import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.ai.v201213.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201213.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.search.Astar;
import org.totalboumboum.ai.v201213.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
public class MoveHandler extends AiMoveHandler<CiplakErakyol>
{	

	/**
	 * @param ai
	 * 
	 * @throws StopRequestException
	 */
	protected MoveHandler(CiplakErakyol ai) throws StopRequestException
    {	
		super(ai);
		ai.checkInterruption();
		verbose = true;

	}

	/////////////////////////////////////////////////////////////////
	// DESTINATION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiTile updateCurrentDestination() throws StopRequestException
	{
		ai.checkInterruption();
		AiTile target = ai.getTileWithBiggestUtility();
		return target;
	}

	/////////////////////////////////////////////////////////////////
	// PATH						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * The supplementary cost to an enemy.
	 */
	private final int	ASTAR_COST		= 1000;

	@Override
	protected AiPath updateCurrentPath() throws StopRequestException
	{
		ai.checkInterruption();
		AiHero ownHero = ai.getZone().getOwnHero();
		AiPath path;
		CostCalculator costCalculator = new TimeCostCalculator(ai, ownHero);
		costCalculator.setOpponentCost(ASTAR_COST);
		HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator( ai, ownHero);
		SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(ai, TimePartialSuccessorCalculator.MODE_NOTREE);
		Astar aStar = new Astar(ai, ownHero, costCalculator,	heuristicCalculator, successorCalculator);

		AiLocation loc = new AiLocation( ownHero.getTile());

		// Shortest path calculation
		try {
			path = aStar.startProcess(loc, currentDestination);
		} catch (LimitReachedException e) {
			path = new AiPath();
			path.addLocation(new AiLocation(ownHero.getTile()));
		}
		return path;
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Direction updateCurrentDirection() throws StopRequestException
	{	
		ai.checkInterruption();
		if (currentPath != null)
		{
			// If we dont have a path or if we must wait
			long wait = currentPath.getFirstPause();
			if ( currentPath.getLength() > 2 && wait <= 0)
			{
				AiLocation target = currentPath.getLocation(1);
				AiLocation location = new AiLocation(ai.getZone().getOwnHero().getTile());
				return ai.getZone().getDirection(location, target);
			}
		}
		return Direction.NONE;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	
		ai.checkInterruption();
		super.updateOutput();
	}
}

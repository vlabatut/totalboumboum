package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v4;

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
@SuppressWarnings("deprecation")
public class MoveHandler extends AiMoveHandler<CiplakErakyol> {
	/**
	 * astar
	 */
	Astar aStar;

	/**
	 * @param ai
	 * 		information manquante !?	
	 * @throws StopRequestException
	 * 		information manquante !?	
	 */
	protected MoveHandler(CiplakErakyol ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();
		AiHero ownHero = ai.getZone().getOwnHero();
		CostCalculator costCalculator = new TimeCostCalculator(ai, ownHero);
		HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(
				ai, ownHero);
		SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(
				ai, TimePartialSuccessorCalculator.MODE_NOTREE);
		Astar aStar = new Astar(ai, ownHero, costCalculator,
				heuristicCalculator, successorCalculator);
		this.aStar = aStar;
		verbose = false;
		aStar.setVerbose(false);
	}

	// ///////////////////////////////////////////////////////////////
	// DESTINATION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	@Override
	protected AiTile updateCurrentDestination() throws StopRequestException {
		ai.checkInterruption();
		Boolean control = false;
		AiHero ownHero = ai.getZone().getOwnHero();
		AiTile target = ownHero.getTile();
		AiTile tile = ai.getTileWithBiggestUtility();
		if(tile!=null)
		{	control = tile.isCrossableBy(ownHero);
			Direction direction = Direction.NONE;
			if (control == true)
				target = tile;
			else
				target = tile.getNeighbor(direction);
		}
		return target;
	}

	// ///////////////////////////////////////////////////////////////
	// PATH 					/////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	@Override
	protected AiPath updateCurrentPath() throws StopRequestException {
		ai.checkInterruption();
		AiHero ownHero = ai.getZone().getOwnHero();
		AiPath path = null;

		AiLocation loc = new AiLocation(ownHero);

		// Shortest path calculation

		try {
			path = aStar.startProcess(loc, currentDestination);
		} catch (LimitReachedException e) {
			path = new AiPath();
			path.addLocation(new AiLocation(ownHero.getTile()));
		}

		return path;
	}

	// ///////////////////////////////////////////////////////////////
	// DIRECTION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Direction updateCurrentDirection() throws StopRequestException {
		ai.checkInterruption();
		if (currentPath != null) {
			// If we dont have a path or if we must wait
			long wait = currentPath.getFirstPause();
			if (currentPath.getLength() > 2 && wait <= 0) {
				AiLocation target = currentPath.getLocation(1);
				AiLocation location = new AiLocation(ai.getZone().getOwnHero()
						.getTile());
				return ai.getZone().getDirection(location, target);
			}
		}
		return Direction.NONE;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException {
		ai.checkInterruption();
		super.updateOutput();
	}
}
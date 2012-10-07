package org.totalboumboum.ai.v201112.ais.balcetin.v3;

import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;

import org.totalboumboum.ai.v201112.adapter.data.AiTile;

import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiPath;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201112.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.search.Astar;
import org.totalboumboum.ai.v201112.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.TimePartialSuccessorCalculator;

import org.totalboumboum.engine.content.feature.Direction;

/**
 * Move Handler class to decide where AI moves.
 * 
 * @author Adnan Bal
 * @author Özcan Çetin
 */
@SuppressWarnings("deprecation")
public class MoveHandler extends AiMoveHandler<BalCetin> {
	/** */
	protected Astar astar = null;
	
	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	protected MoveHandler(BalCetin ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();
		//no text output on the console
		verbose = false;

	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	@Override
	protected Direction considerMoving() throws StopRequestException {
		ai.checkInterruption();
		TileProcess tp = new TileProcess(this.ai);

		AiTile destTile = null;

		try {
			AiLocation myLoc = new AiLocation(this.ai.getZone().getOwnHero()
					.getTile());
			AiPath path = null;

			CostCalculator costCalculator = new TimeCostCalculator(ai, ai
					.getZone().getOwnHero());
			costCalculator.setOpponentCost(1000);
			HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(
					ai, ai.getZone().getOwnHero());
			SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(
					ai, TimePartialSuccessorCalculator.MODE_NOTREE);
			astar = new Astar(ai, ai.getZone().getOwnHero(), costCalculator,
					heuristicCalculator, successorCalculator);

			try {
				path = astar.processShortestPath(myLoc, tp.getBestTile());

			} catch (LimitReachedException e) {
				return Direction.NONE;
			}
			if (path.getLength() > 1) {
				destTile = path.getLocation(1).getTile();
			} else
				destTile = path.getLocation(0).getTile();
		} catch (NullPointerException e) {

		}

		// if there is no destination.Stay still.
		if (destTile == null) {
			return Direction.NONE;
		}

		return ai.getZone().getDirection(ai.getZone().getOwnHero().getTile(),
				destTile);
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

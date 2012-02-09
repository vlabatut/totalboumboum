package org.totalboumboum.ai.v201112.ais.demireloz.v2;

import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
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
 * @author Enis Demirel
 * @author Berke Öz
 */
@SuppressWarnings("deprecation")
public class MoveHandler extends AiMoveHandler<DemirelOz> {

	protected MoveHandler(DemirelOz ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();
		verbose = false;
	}

	@Override
	protected Direction considerMoving() throws StopRequestException {
		ai.checkInterruption();
		AiHero ourhero = this.ai.getZone().getOwnHero();
		AiTile ourtile = ourhero.getTile();
		Direction direction = Direction.NONE;
		int enemycost = 0;

		// If we are in danger we will prefer to avoid a path containing an
		// enemy while escaping
		if (!this.ai.getZone().getRemainingOpponents().isEmpty()) {
			if (this.ai.getDanger2(ourtile)) {
				enemycost = 5000;
			}
		} else {
			enemycost = 0;
		}

		Direction result = null;
		AiLocation location = new AiLocation(ourtile);
		CostCalculator costcalculator = new TimeCostCalculator(this.ai, ourhero);
		costcalculator.setOpponentCost(enemycost);
		HeuristicCalculator heuristiccalculator = new TimeHeuristicCalculator(
				this.ai, ourhero);
		SuccessorCalculator successorcalculator = new TimePartialSuccessorCalculator(
				this.ai, TimePartialSuccessorCalculator.MODE_NOTREE);
		// Astar
		Astar astar = new Astar(this.ai, ourhero, costcalculator,
				heuristiccalculator, successorcalculator);

		if (this.ai.getBiggestTile() != null) {
			try {

				AiPath aipath = astar.processShortestPath(location,
						this.ai.getBiggestTile());

				if (aipath != null) {// If we dont have a path we wait
					long wait = aipath.getFirstPause();
					if (aipath.getLength() < 2 || wait > 0) {

						direction = Direction.NONE;
						return direction;
					}

					else if (!this.ai.controlDanger(aipath.getLocation(1).getTile())) {
						AiLocation target = aipath.getLocation(1);
						return direction = this.ai.getZone().getDirection(
								location, target);
					}
				} else {
					// System.out.println("No path available");
				}
			} catch (LimitReachedException e) {
			}
		} else {
			// System.out.println("No biggestile");
		}
		return result;
	}

	@Override
	public void updateOutput() throws StopRequestException {
		ai.checkInterruption();

		super.updateOutput();

	}
}

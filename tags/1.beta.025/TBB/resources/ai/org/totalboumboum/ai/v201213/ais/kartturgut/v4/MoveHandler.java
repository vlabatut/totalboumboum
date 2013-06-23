package org.totalboumboum.ai.v201213.ais.kartturgut.v4;

import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
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
 * Classe gérant le déplacement de l'agent.
 * Cf. la documentation de {@link AiMoveHandler} pour plus de détails.
 * 
 
 * @author Yunus Kart
 * @author Siyabend Turgut
 */
@SuppressWarnings("deprecation")
public class MoveHandler extends AiMoveHandler<KartTurgut>
{	
	/** */
	protected Astar astar = null;
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected MoveHandler(KartTurgut ai) throws StopRequestException
    {	
		super(ai);
		ai.checkInterruption();
		verbose = false;
	}

	
	@Override
	protected Direction updateCurrentDirection() throws StopRequestException
	{	ai.checkInterruption();
		
		TileProcess tp = new TileProcess(this.ai);

		AiTile notreTile = null;

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
				path = astar.startProcess(myLoc, tp.getBestTile());

			} catch (LimitReachedException e) {
				return Direction.NONE;
			}
			if (path.getLength() > 1) {
				notreTile = path.getLocation(1).getTile();
			} else
				notreTile = path.getLocation(0).getTile();
		} catch (NullPointerException e) {

		}

		
		if (notreTile == null) {
			return Direction.NONE;
		}

		return ai.getZone().getDirection(ai.getZone().getOwnHero().getTile(),
				notreTile);
	}


	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		super.updateOutput();
	}


	@Override
	protected AiTile updateCurrentDestination() throws StopRequestException {
		ai.checkInterruption();
		return null;
	}


	@Override
	protected AiPath updateCurrentPath() throws StopRequestException {
		ai.checkInterruption();
		return null;
	}
}

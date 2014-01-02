package org.totalboumboum.ai.v201314.ais.gunessaglamturgut.v2;

import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.TileCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.TileHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.search.Astar;
import org.totalboumboum.ai.v201314.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is responsible of the decision of moving to another tile.
 *
 * @author Neşe Güneş
 * @author Esra Sağlam
 * @author Siyabend Turgut
 */
public class MoveHandler extends AiMoveHandler<Agent> {
	/**
	 * Maximum radius of safety check.
	 */
	private final int SAFETY_CHECK_RADIUS = 5;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 *
	 * @param ai l'agent que cette classe doit gérer.
	 */
	protected MoveHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		CostCalculator costCalculator = new TileCostCalculator(ai);
		HeuristicCalculator heuristicCalculator = new TileHeuristicCalculator(ai);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
		astar = new Astar(ai, ownHero, costCalculator, heuristicCalculator, successorCalculator);
	}

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Zone.
	 */
	private AiZone zone = null;
	/**
	 * Hero fo the group.
	 */
	private AiHero ownHero = null;
	/**
	 * A-star object.
	 */
	private Astar astar = null;

	/////////////////////////////////////////////////////////////////
	// DESTINATION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiTile processCurrentDestination() {
		ai.checkInterruption();

		if (ai.getTileUtil().getCurrentDangerousTiles().contains(ownHero.getTile())) {
			// I am in danger
			AiTile destination = ai.getTileUtil().getClosestSafeTile(ownHero.getTile(), SAFETY_CHECK_RADIUS);
			if (destination == null) {
				destination = ownHero.getTile();
			}
			return destination;
		} else {
			Set<AiTile> accessibleTiles = ai.getTileUtil().getAccessibleTiles();
			AiPreferenceHandler<Agent> preferenceHandler = ai.getPreferenceHandler();
			Map<Integer, List<AiTile>> preferences = preferenceHandler.getPreferencesByValue();
			AiTile dest = ownHero.getTile();
			int minDistance = Integer.MAX_VALUE;
			int minPreference = Integer.MAX_VALUE;
			for (Integer i : preferences.keySet()) {
				ai.checkInterruption();
				List<AiTile> aiTiles = preferences.get(i);
				for (AiTile aiTile : aiTiles) {
					ai.checkInterruption();
					if (accessibleTiles.contains(aiTile)) {
						int tileDistance = ai.getZone().getTileDistance(ownHero.getTile(), aiTile);
						if (tileDistance < minDistance && i <= minPreference) {
							minDistance = tileDistance;
							minPreference = i;
							dest = aiTile;
						}
					}
				}
			}
			return dest;
		}
	}

	/////////////////////////////////////////////////////////////////
	// PATH						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiPath processCurrentPath() {
		ai.checkInterruption();
		AiPath result;
		AiLocation startLocation = new AiLocation(ownHero);

		AiTile endTile = getCurrentDestination();
		try {
			result = astar.startProcess(startLocation, endTile);
		} catch (LimitReachedException e) {
			result = new AiPath();
		}

		return result;
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Direction processCurrentDirection() {
		ai.checkInterruption();
		Direction result;
		AiPath path = getCurrentPath();
		if (path == null || path.getLength() < 2)
			result = Direction.NONE;
		else {
			AiLocation nextLocation = path.getLocation(1);
			AiTile nextTile = nextLocation.getTile();
			AiTile currentTile = ownHero.getTile();
			result = zone.getDirection(currentTile, nextTile);
		}

		if ( !this.ai.getTileUtil().getCurrentDangerousTiles().contains( ownHero.getTile() ) && ai.getTileUtil().getCurrentDangerousTiles().contains( ownHero.getTile().getNeighbor( result ) ) ) result = Direction.NONE;
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() {
		ai.checkInterruption();
		super.updateOutput();
	}
}

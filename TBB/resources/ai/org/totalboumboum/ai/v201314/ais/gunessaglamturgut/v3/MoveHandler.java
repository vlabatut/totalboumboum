package org.totalboumboum.ai.v201314.ais.gunessaglamturgut.v3;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

/**
 * Move handler of the agent.
 * 
 * @author Esra Sağlam
 * @author Siyabend Turgut
 */
public class MoveHandler extends AiMoveHandler<Agent> {
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected MoveHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		CostCalculator costCalculator = new TileCostCalculator(ai);
		HeuristicCalculator heuristicCalculator = new TileHeuristicCalculator(
				ai);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(
				ai);
		astar = new Astar(ai, ownHero, costCalculator, heuristicCalculator,
				successorCalculator);
	}

	/**
	 * Holds zone.
	 */
	private AiZone zone = null;

	/**
	 * Holds own hero.
	 */
	private AiHero ownHero = null;

	/**
	 * Holds a-star object.
	 */
	private Astar astar = null;

	/**
	 * If we reduce the last N tiles of the hero and it is equal to this number,
	 * then hero stayed on the same place for last N checks.
	 */
	private final int REDUCED_TILE_NUMBER = 1;

	/**
	 * Hero checks for the closest safe tile with this number as radius.
	 */
	private final int SAFETY_CHECK_RADIUS = 10;

	/**
	 * Represents single object count.
	 */
	private final int SINGLE = 1;

	/**
	 * Represents the first index.
	 */
	private final int FIRST = 0;

	/**
	 * Represents the second index.
	 */
	private final int SECOND = 1;

	/**
	 * If path legth is lesses than this value, hero will keep its location.
	 */
	private final int PATH_LIMIT = 2;

	// ///////////////////////////////////////////////////////////////
	// DESTINATION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected AiTile processCurrentDestination() {
		ai.checkInterruption();
		AiTile result;

		AiPreferenceHandler<Agent> preferenceHandler = ai
				.getPreferenceHandler();
		Map<Integer, List<AiTile>> preferences = preferenceHandler
				.getPreferencesByValue();
		int minPref = Collections.min(preferences.keySet());
		List<AiTile> tiles = preferences.get(minPref);

		if (minPref == ai.getPreviousMinPref()) {
			// decisive
			if (tiles.contains(ai.getPreviousDecision()))
				result = ai.getPreviousDecision();
			else
				result = ai.getTileUtil().getClosestTileToEnemy(tiles);
		} else {
			ai.setPreviousMinPref(minPref);
			result = ai.getTileUtil().getClosestTileToEnemy(tiles);
		}

		// if i am staying on the same tile for too long, and i am not
		// surrounded by danger, i need to change my decision now!
		boolean stayedForTooLong = new HashSet<AiTile>(ai.getLastTiles())
				.size() == REDUCED_TILE_NUMBER
				&& result.equals(ownHero.getTile());
		if (stayedForTooLong)
			result = fixStaticness(result, tiles);

		AiTile ownTile = ai.getZone().getOwnHero().getTile();
		if (ai.getTileUtil().isTileDangerous(ownTile))
			result = ai.getTileUtil().getClosestSafeTile(ownTile,
					SAFETY_CHECK_RADIUS);

		// hold the decision on agent
		ai.setPreviousDecision(result);
		return result;
	}

	/**
	 * Method to fix the situation if the agent stayed on the same place for too
	 * long.
	 * 
	 * @param currentResult
	 *            Current decision to process.
	 * @param tiles
	 *            Tiles to consider.
	 * @return A new result which is not the current tile of the hero.
	 */
	private AiTile fixStaticness(AiTile currentResult, List<AiTile> tiles) {
		ai.checkInterruption();
		Set<AiTile> dangerousTiles = ai.getTileUtil()
				.getCurrentDangerousTiles();
		Set<AiTile> accessibleTiles = ai.getTileUtil().getAccessibleTiles();
		AiTile ownTile = ai.getZone().getOwnHero().getTile();
		AiTile upTile = ownTile.getNeighbor(Direction.UP);
		AiTile downTile = ownTile.getNeighbor(Direction.DOWN);
		AiTile leftTile = ownTile.getNeighbor(Direction.LEFT);
		AiTile rightTile = ownTile.getNeighbor(Direction.RIGHT);
		boolean isUpSafe = accessibleTiles.contains(upTile)
				&& !dangerousTiles.contains(upTile);
		boolean isDownSafe = accessibleTiles.contains(downTile)
				&& !dangerousTiles.contains(downTile);
		boolean isLeftSafe = accessibleTiles.contains(leftTile)
				&& !dangerousTiles.contains(leftTile);
		boolean isRightSafe = accessibleTiles.contains(rightTile)
				&& !dangerousTiles.contains(rightTile);

		boolean isSafeToMove = (isUpSafe || isDownSafe || isLeftSafe || isRightSafe);

		if (isSafeToMove) {
			if (tiles.size() > SINGLE) {
				tiles.remove(ownTile);
				currentResult = tiles.get(FIRST);
			}
		}
		return currentResult;
	}

	// ///////////////////////////////////////////////////////////////
	// PATH /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
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

	// ///////////////////////////////////////////////////////////////
	// DIRECTION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Direction processCurrentDirection() {
		ai.checkInterruption();
		Direction result;

		AiPath path = getCurrentPath();
		if (path == null || path.getLength() < PATH_LIMIT)
			result = Direction.NONE;
		else {
			AiLocation nextLocation = path.getLocation(SECOND);
			AiTile nextTile = nextLocation.getTile();
			AiTile currentTile = ownHero.getTile();
			result = zone.getDirection(currentTile, nextTile);
		}

		AiTile ownTile = ai.getZone().getOwnHero().getTile();
		ai.addToLastTiles(ownTile);

		// if dangerous, and i am safe i won't "jump in the fire"!
		if (ai.getTileUtil().isTileDangerous(ownTile.getNeighbor(result))
				&& !ai.getTileUtil().isTileDangerous(ownTile))
			result = Direction.NONE;
		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() {
		ai.checkInterruption();
		
		super.updateOutput();
	}
}

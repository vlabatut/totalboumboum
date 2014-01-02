package org.totalboumboum.ai.v201314.ais.saglamturgut.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saglamturgut.v4.Agent;

import java.util.ArrayList;
import java.util.Set;

/**
 * Closest tile to enemy criterion. Checks if a tile is the closest tile to 
 * the enemy in all its accessible tiles.
 * This method uses non-cyclic manhattan distance to calculate the distance.
 * 
 * @author Esra Sağlam
 * @author Siyabend Turgut
 */
public class ClosestAccessibleTileToEnemy extends AiCriterionBoolean<Agent> {
	/** Name of the criterion */
	public static final String NAME = "CLOSEST_TILE";

	/** Constructs the criterion.
	 * @param ai Ai to process.
	 */
	public ClosestAccessibleTileToEnemy(Agent ai) {
		super(ai, NAME);
		ai.checkInterruption();
	}

	/**
	 * Checks if the given tile is the closest tile to an enemy.
	 * @param tile Tile to check.
	 * @return true if the tile is the closest tile to the enemy in all its accessible tiles, false otherwise.
	 */
	@Override
	protected Boolean processValue(AiTile tile) {
		ai.checkInterruption();

		Set<AiTile> accessibleTiles = ai.getTileUtil().getAccessibleTiles(tile);

		// is it closest in the set?
		return tile.equals(ai.getTileUtil().getClosestTileToEnemy(new ArrayList<AiTile>(accessibleTiles)));
	}
}

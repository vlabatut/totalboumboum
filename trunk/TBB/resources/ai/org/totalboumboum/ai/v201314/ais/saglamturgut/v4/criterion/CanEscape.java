package org.totalboumboum.ai.v201314.ais.saglamturgut.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saglamturgut.v4.Agent;

import java.util.Set;

/**
 * Hero can escape tile criterion. Checks if a bomb is put to the 
 * given tile, the hero can escape or not.
 * 
 * @author Esra Sağlam
 * @author Siyabend Turgut
 */
public class CanEscape extends AiCriterionBoolean<Agent> {
	/** Name of the criterion */
	public static final String NAME = "CAN_ESCAPE";

	/** Constructs the criterion.
	 * @param ai Ai to process.
	 */
	public CanEscape(Agent ai) {
		super(ai, NAME);
		ai.checkInterruption();
	}

	/**
	 * Checks if a bomb is put to the given tile, can agent escape from the danger.
	 * @param tile Tile to check.
	 * @return true hero can escape from that situation, false otherwise.
	 */
	@Override
	protected Boolean processValue(AiTile tile) {
		ai.checkInterruption();

		Set<AiTile> accessibleTiles = ai.getTileUtil().getAccessibleTiles(tile);
		// remove dangerous ones
		accessibleTiles.removeAll(ai.getTileUtil().getDangerousTiles());

		// remove potentially dangerous ones
		accessibleTiles.removeAll(ai.getTileUtil().getThreatenedTilesOnBombPut(tile));

		return !(accessibleTiles.isEmpty());
	}
}

package org.totalboumboum.ai.v201314.ais.saglamturgut.v3.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saglamturgut.v3.Agent;

import java.util.Set;

/**
 * Hero can escape tile criterion.
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

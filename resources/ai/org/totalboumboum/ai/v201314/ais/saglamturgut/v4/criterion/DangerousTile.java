package org.totalboumboum.ai.v201314.ais.saglamturgut.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saglamturgut.v4.Agent;

/**
 * Dangerous tile criterion. Checks if a tile is dangerous or not. 
 * The tile is in danger if it is threatened by a bomb
 * or it contains a flame.
 * 
 * @author Esra Sağlam
 * @author Siyabend Turgut
 */
@SuppressWarnings("deprecation")
public class DangerousTile extends AiCriterionBoolean<Agent> {
	/** Name of the criterion */
	public static final String NAME = "DANGEROUS_TILE";

	/** Constructs the criterion.
	 * @param ai Ai to process.
	 */
	public DangerousTile(Agent ai) {
		super(ai, NAME);
		ai.checkInterruption();
	}

	/**
	 * Checks if the given tile is dangerous or not.
	 * @param tile Tile to check.
	 * @return true if the tile is in danger, false otherwise.
	 */
	@Override
	protected Boolean processValue(AiTile tile) {
		ai.checkInterruption();
		return ai.getTileUtil().isTileDangerous(tile);
	}
}
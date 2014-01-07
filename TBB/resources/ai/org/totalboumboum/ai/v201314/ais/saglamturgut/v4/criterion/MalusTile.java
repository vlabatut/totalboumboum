package org.totalboumboum.ai.v201314.ais.saglamturgut.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saglamturgut.v4.Agent;

/**
 * Malus tile criterion.  Checks if a tile has a malus (negative bonus) on it.
 * 
 * @author Esra Sağlam
 * @author Siyabend Turgut
 */
@SuppressWarnings("deprecation")
public class MalusTile extends AiCriterionBoolean<Agent> {
	/** Name of the criterion */
	public static final String NAME = "MALUS_TILE";

	/** Constructs the criterion.
	 * @param ai Ai to process.
	 */
	public MalusTile(Agent ai) {
		super(ai, NAME);
		ai.checkInterruption();
	}

	/**
	 * Checks if the given tile has a malus (negative bonus) on it.
	 * @param tile Tile to check.
	 * @return true if the tile contains a malus (negative bonus), false otherwise.
	 */
	@Override
	protected Boolean processValue(AiTile tile) {
		ai.checkInterruption();
		for (AiItem aiItem : tile.getItems()) {
			ai.checkInterruption();
			if (aiItem.getType().isAntiKind()) return true;
		}
		return false;
	}
}
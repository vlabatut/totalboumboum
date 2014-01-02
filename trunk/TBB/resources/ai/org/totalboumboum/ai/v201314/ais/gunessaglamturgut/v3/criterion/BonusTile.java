package org.totalboumboum.ai.v201314.ais.gunessaglamturgut.v3.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.gunessaglamturgut.v3.Agent;

/**
 * Bonus tile criterion.
 */
public class BonusTile extends AiCriterionBoolean<Agent> {
	/** Name of the criterion */
	public static final String NAME = "BONUS_TILE";


	/** Constructs the criterion.
	 * @param ai Ai to process.
	 */
	public BonusTile(Agent ai) {
		super(ai, NAME);
		ai.checkInterruption();
	}

	@Override
	protected Boolean processValue(AiTile tile) {
		ai.checkInterruption();
		for (AiItem aiItem : tile.getItems()) {
			ai.checkInterruption();
			if (aiItem.getType().isBonus()) return true;
		}
		return false;
	}
}

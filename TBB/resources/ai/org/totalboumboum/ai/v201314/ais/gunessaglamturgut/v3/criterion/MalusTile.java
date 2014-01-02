package org.totalboumboum.ai.v201314.ais.gunessaglamturgut.v3.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.gunessaglamturgut.v3.Agent;

/**
 * Malus tile criterion
 */
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
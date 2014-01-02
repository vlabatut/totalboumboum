package org.totalboumboum.ai.v201314.ais.saglamturgut.v3.criterion;


import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saglamturgut.v3.Agent;

/**
 * Dangerous tile criterion.
 */
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

	@Override
	protected Boolean processValue(AiTile tile) {
		ai.checkInterruption();
		return ai.getTileUtil().isTileDangerous(tile);
	}
}
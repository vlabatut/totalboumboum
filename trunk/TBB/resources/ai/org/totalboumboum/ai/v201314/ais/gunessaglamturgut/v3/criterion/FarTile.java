package org.totalboumboum.ai.v201314.ais.gunessaglamturgut.v3.criterion;


import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.gunessaglamturgut.v3.Agent;

/**
 * Far tile criterion.
 */
public class FarTile extends AiCriterionBoolean<Agent> {
	/** Name of the criterion */
	public static final String NAME = "FAR_TILE";

	/**
	 * What we call "far".
	 */
	private final int FARNESS_LIMIT = 5;

	/** Constructs the criterion.
	 * @param ai Ai to process.
	 */
	public FarTile(Agent ai) {
		super(ai, NAME);
		ai.checkInterruption();
	}

	@Override
	protected Boolean processValue(AiTile tile) {
		ai.checkInterruption();
		return ai.getZone().getTileDistance(ai.getZone().getOwnHero().getTile(), tile) > FARNESS_LIMIT;
	}
}
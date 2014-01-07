package org.totalboumboum.ai.v201314.ais.saglamturgut.v4.criterion;


import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saglamturgut.v4.Agent;

/**
 * Far tile criterion. Checks if a tile is far from the agent.
 * 
 * @author Esra Sağlam
 * @author Siyabend Turgut
 */
@SuppressWarnings("deprecation")
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

	/**
	 * Checks if the given tile is far from the agent.
	 * @param tile Tile to check.
	 * @return true if the tile is far from the agent, false otherwise.
	 */
	@Override
	protected Boolean processValue(AiTile tile) {
		ai.checkInterruption();
		return ai.getZone().getTileDistance(ai.getZone().getOwnHero().getTile(), tile) > FARNESS_LIMIT;
	}
}
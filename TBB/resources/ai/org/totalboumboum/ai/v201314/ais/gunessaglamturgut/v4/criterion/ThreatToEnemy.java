package org.totalboumboum.ai.v201314.ais.gunessaglamturgut.v4.criterion;


import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.gunessaglamturgut.v4.Agent;

import java.util.Set;

/**
 * Threat to enemy criterion. Checks if a bomb on the given tile can threat an enemy or not.
 */
public class ThreatToEnemy extends AiCriterionBoolean<Agent> {

	/** Name of the criterion */
	public static final String NAME = "THREAT_TO_ENEMY";

	/** Constructs the criterion.
	 * @param ai Ai to process.
	 */
	public ThreatToEnemy(Agent ai) {
		super(ai, NAME);
		ai.checkInterruption();
	}

	/**
	 * Checks if a bomb on the given tile can threaten an enemy.
	 * @param tile Tile to check.
	 * @return true if a bomb on the given tile can threat an enemy, false otherwise.
	 */
	@Override
	protected Boolean processValue(AiTile tile) {
		ai.checkInterruption();
		AiHero ownHero = ai.getZone().getOwnHero();
		Set<AiTile> threatenedTiles = ai.getTileUtil().getThreatenedTilesOnBombPut(tile);
		for (AiHero aiHero : ai.getZone().getHeroes()) {
			ai.checkInterruption();
			if (aiHero != ownHero && threatenedTiles.contains(aiHero.getTile())) return true;
		}
		return false;
	}
}

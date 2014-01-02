package org.totalboumboum.ai.v201314.ais.saglamturgut.v4.criterion;


import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saglamturgut.v4.Agent;

import java.util.HashSet;
import java.util.Set;

/**
 * Locks enemy criterion. Checks if a bomb is put to the tile, can it lock the enemy in danger.
 * 
 * @author Esra Sağlam
 * @author Siyabend Turgut
 */
public class LocksEnemy extends AiCriterionBoolean<Agent> {
	/** Name of the criterion */
	public static final String NAME = "LOCKS_ENEMY";

	/** Constructs the criterion.
	 * @param ai Ai to process.
	 */
	public LocksEnemy(Agent ai) {
		super(ai, NAME);
		ai.checkInterruption();
	}

	/**
	 * Checks if a bomb of the agent on the given tile can stuck the enemy in his place.
	 * @param tile Tile to check.
	 * @return true if a bomb on the given tile can lock the enemy, false otherwise.
	 */
	@Override
	protected Boolean processValue(AiTile tile) {
		ai.checkInterruption();
		AiHero ownHero = ai.getZone().getOwnHero();
		Set<AiTile> threatenedTiles = ai.getTileUtil().getThreatenedTilesOnBombPut(tile);
		Set<AiHero> threatenedEnemies = new HashSet<AiHero>();
		for (AiHero aiHero : ai.getZone().getHeroes()) {
			ai.checkInterruption();
			if (aiHero != ownHero && threatenedTiles.contains(aiHero.getTile())) threatenedEnemies.add(aiHero);
		}

		Set<AiTile> allDangerousTiles = ai.getTileUtil().getCurrentDangerousTiles();
		allDangerousTiles.addAll(threatenedTiles);

		HashSet<AiTile> assumedExtraBlocks = new HashSet<AiTile>();
		assumedExtraBlocks.add(tile);
		// get accessibles of each enemy
		for (AiHero threatenedEnemy : threatenedEnemies) {
			ai.checkInterruption();
			Set<AiTile> accessibleTilesOfEnemy = ai.getTileUtil().getAccessibleTiles(threatenedEnemy.getTile(), assumedExtraBlocks);
			if (allDangerousTiles.containsAll(accessibleTilesOfEnemy)) return true;
		}

		return false;
	}
}

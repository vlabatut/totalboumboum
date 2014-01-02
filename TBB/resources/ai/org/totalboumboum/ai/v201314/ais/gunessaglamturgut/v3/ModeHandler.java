package org.totalboumboum.ai.v201314.ais.gunessaglamturgut.v3;

import java.util.List;
import java.util.Set;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

/**
 * Mode Handler of the agent.
 * 
 * @author Esra Sağlam
 * @author Siyabend Turgut
 */
public class ModeHandler extends AiModeHandler<Agent> {

	/** Symbolizes our hero. */
	private final AiHero ownHero;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected ModeHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();
		ownHero = ai.getZone().getOwnHero();
	}

	/**
	 * Minimum number of bombs needed for attack mode.
	 */
	private final int BOMB_LIMIT = 3;

	/**
	 * Minimum bomb range needed for attack mode.
	 */
	private final int RANGE_LIMIT = 3;

	/**
	 * Maximum penalty to other enemies to go into attack mode.
	 */
	private final int POWER_PENALTY_LIMIT = -1;

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() {
		ai.checkInterruption();

		int ownPower = ownHero.getBombRange() + ownHero.getBombNumberLimit();
		if (ownHero.getBombRange() >= RANGE_LIMIT
				&& ownHero.getBombNumberLimit() >= BOMB_LIMIT) {
			// check other enemies now. If our agent is nearly as strong as any
			// of other heroes, switch to attack mode
			for (AiHero aiHero : ai.getZone().getHeroes()) {
				ai.checkInterruption();
				if (aiHero != ownHero) {
					// this is an enemy
					int enemyPower = aiHero.getBombRange()
							+ aiHero.getBombNumberLimit();
					if (ownPower - enemyPower >= POWER_PENALTY_LIMIT)
						return true;
				}
			}
		}
		return false;
	}

	@Override
	protected boolean isCollectPossible() {
		ai.checkInterruption();

		List<AiBlock> blocks = ai.getZone().getBlocks();
		boolean destructibleBlockExists = false;
		for (AiBlock block : blocks) {
			ai.checkInterruption();
			if (block.isDestructible()) {
				destructibleBlockExists = true;
				break;
			}
		}

		if (destructibleBlockExists) {
			Set<AiTile> accessibleTiles = ai.getTileUtil().getAccessibleTiles();
			for (AiTile accessibleTile : accessibleTiles) {
				ai.checkInterruption();
				for (AiTile neighbor : accessibleTile.getNeighbors()) {
					ai.checkInterruption();
					if (!neighbor.getBlocks().isEmpty()
							&& neighbor.getBlocks().get(0).isDestructible())
						return true;
				}
			}
		}

		return false;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	/**
	 * Met à jour la sortie graphique.
	 */
	protected void updateOutput() {
		ai.checkInterruption();

	}
}

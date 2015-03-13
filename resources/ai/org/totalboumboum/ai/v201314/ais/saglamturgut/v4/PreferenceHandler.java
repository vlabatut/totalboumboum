package org.totalboumboum.ai.v201314.ais.saglamturgut.v4;

import java.util.Set;

import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

/**
 * Preference handler of the agent. REsponsible for selecting tiles to check and identifying the categories of tiles.
 * 
 * @author Esra Sağlam
 * @author Siyabend Turgut
 */
@SuppressWarnings("deprecation")
public class PreferenceHandler extends AiPreferenceHandler<Agent> {

	/** Symbolizes our hero. */
	private final AiHero ownHero;

	/**
	 * Constructs the preference handler for the given agent.
	 *
	 * @param ai
	 *            The agent to construct the handler for.
	 */
	protected PreferenceHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

		ownHero = ai.getZone().getOwnHero();
	}

	/**
	 * Selects the tiles to process for the agent at the moment.
	 * @return The tiles to process.
	 */
	@Override
	protected Set<AiTile> selectTiles() {
		ai.checkInterruption();
		return ai.getTileUtil().getAccessibleTiles();
	}

	/**
	 * Collect mode category name.
	 */
	private static String GOOD_FOR_COLLECT = "GOOD_FOR_COLLECT";

	/**
	 * Attack mode category name when an enemy is accessible from the processed
	 * tile.
	 */
	private static String ATTACK_ENEMY_ACCESSIBLE = "ATTACK_ENEMY_ACCESSIBLE";

	/**
	 * Attack mode category name when an enemy is not accessible from the
	 * processed tile.
	 */
	private static String ATTACK_ENEMY_INACCESSIBLE = "ATTACK_ENEMY_INACCESSIBLE";

	/**
	 * Identifies the category of the given tile.
	 * @param tile
	 * 		The tile to identify the category.
	 * @return The category the tile belongs to.
	 */
	@Override
	protected AiCategory identifyCategory(AiTile tile) {
		ai.checkInterruption();
		AiModeHandler<Agent> modeHandler = ai.getModeHandler();
		AiMode mode = modeHandler.getMode();
		if (mode == AiMode.COLLECTING)
			return getCategory(GOOD_FOR_COLLECT);
		else {
			Set<AiTile> accessibles = ai.getTileUtil().getAccessibleTiles();

			for (AiHero aiHero : ai.getZone().getHeroes()) {
				ai.checkInterruption();
				if (aiHero != ownHero) {
					if (accessibles.contains(aiHero.getTile()))
						return getCategory(ATTACK_ENEMY_ACCESSIBLE);
				}
			}
			return getCategory(ATTACK_ENEMY_INACCESSIBLE);
		}
	}

	/**
	 * Updates the graphical output of the agent.
	 */
	@Override
	public void updateOutput() {
		ai.checkInterruption();
		super.updateOutput();
	}
}

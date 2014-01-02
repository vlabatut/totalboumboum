package org.totalboumboum.ai.v201314.ais.saglamturgut.v3;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

/**
 * Bomb handler of the agent.
 * 
 * @author Esra Sağlam
 * @author Siyabend Turgut
 */
public class BombHandler extends AiBombHandler<Agent> {

	/**
	 * Number of the first N combinations on attack mode to put a bomb when an
	 * enemy is accessible;
	 */
	private final int ATTACK_COMBINATION_LIMIT = 6;

	/**
	 * Bomb number limit when approaching non-accessible enemy on attack mode.
	 */
	private final int ATTACK_BOMB_NUMBER_LIMIT = 2;

	/**
	 * Symbolizes zero.
	 */
	private final int ZERO = 0;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected BombHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() {
		ai.checkInterruption();
		AiHero ownHero = ai.getZone().getOwnHero();

		// if there is already a bomb, dont put
		if (!ownHero.getTile().getBombs().isEmpty())
			return false;
		// if i dont have a bomb, dont put
		if (ownHero.getBombNumberMax() - ownHero.getBombNumberCurrent() == 0)
			return false;

		// did i come here for a bonus? if so, do not put a bomb for this turn.
		List<AiItem> items = ownHero.getTile().getItems();
		for (AiItem item : items) {
			ai.checkInterruption();
			if (item.getType().isBonus())
				return false;
		}

		// if i put a bomb here, can i escape?
		boolean canEscape;
		Set<AiTile> accessibleTiles = ai.getTileUtil().getAccessibleTiles();
		// remove dangerous ones
		accessibleTiles.removeAll(ai.getTileUtil().getDangerousTiles());

		// remove potentially dangerous ones
		accessibleTiles.removeAll(ai.getTileUtil()
				.getThreatenedTilesOnBombPut());

		canEscape = !(accessibleTiles.isEmpty());
		if (!canEscape)
			return false;

		AiPreferenceHandler<Agent> preferenceHandler = ai
				.getPreferenceHandler();
		Map<Integer, List<AiTile>> preferences = preferenceHandler
				.getPreferencesByValue();
		int minPref = Collections.min(preferences.keySet());
		List<AiTile> minPrefTiles = preferences.get(minPref);
		switch (ai.getModeHandler().getMode()) {
		case COLLECTING:
			// is this one of the best tiles?
			return minPrefTiles.contains(ownHero.getTile())
					&& ai.getTileUtil().isTileMeaningfulToBombPut();
		case ATTACKING:
			if (minPref <= ATTACK_COMBINATION_LIMIT) {
				// is this one of the first 8 preference tiles?
				Set<AiTile> appropriateTiles = new HashSet<AiTile>();
				for (Integer integer : preferences.keySet()) {
					ai.checkInterruption();
					if (integer <= ATTACK_COMBINATION_LIMIT)
						appropriateTiles.addAll(preferences.get(integer));
				}
				return appropriateTiles.contains(ownHero.getTile());
			} else {
				// just try to approach the enemy
				if (ai.getModeHandler().getMode() == AiMode.ATTACKING
						&& ai.getTileUtil().getAccessibleEnemies().isEmpty()) {
					return ai
							.getZone()
							.getOwnHero()
							.getTile()
							.equals(ai.getTileUtil().getClosestTileToEnemy(
									minPrefTiles))
							&& ai.getZone().getOwnHero().getBombNumberCurrent() == ZERO
							&& ai.getTileUtil().isTileMeaningfulToBombPut();
				} else {
					return minPrefTiles.contains(ownHero.getTile())
							&& ownHero.getBombNumberCurrent() <= ATTACK_BOMB_NUMBER_LIMIT
							&& ai.getTileUtil().isTileMeaningfulToBombPut();
				}
			}
		default:
			return false;
		}
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

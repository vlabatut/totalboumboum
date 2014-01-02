package org.totalboumboum.ai.v201314.ais.gunessaglamturgut.v2;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

import java.util.Map;
import java.util.Set;

/**
 * This class is responsible of the decision of putting a bomb.
 *
 * @author Neşe Güneş
 * @author Esra Sağlam
 * @author Siyabend Turgut
 */
public class BombHandler extends AiBombHandler<Agent> {
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 *
	 * @param ai l'agent que cette classe doit gérer.
	 */
	protected BombHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() {
		ai.checkInterruption();

		AiTile ownTile = ai.getZone().getOwnHero().getTile();
		if (ai.getZone().getOwnHero().getBombNumberCurrent() == 0 || ai.getTileUtil().getDangerousTiles().contains(ownTile)) return false;

		AiTile closestSafeTile = ai.getTileUtil().getClosestSafeTile(ownTile, 6);
		if (closestSafeTile == null) return false;

		Map<AiTile, Integer> preferencesByTile = ai.getPreferenceHandler().getPreferencesByTile();
		if (preferencesByTile.containsKey(ownTile)) {
			int value = preferencesByTile.get(ownTile);
			if (value > 8) {
				return true;
			} else {
				Set<AiTile> tiles = ai.getTileUtil().getEnemyTilesInBombRange(ai.getZone().getOwnHero().getBombRange());
				if (!tiles.isEmpty()) return true;
			}
		}

		return false;
	}


	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/**
	 * Met à jour la sortie graphique.
	 */
	protected void updateOutput() {
		ai.checkInterruption();
	}
}

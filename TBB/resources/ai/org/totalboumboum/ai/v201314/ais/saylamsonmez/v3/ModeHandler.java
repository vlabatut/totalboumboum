package org.totalboumboum.ai.v201314.ais.saylamsonmez.v3;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;

/**
 * Classe gérant les déplacements de l'agent. Cf. la documentation de
 * {@link AiModeHandler} pour plus de détails.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class ModeHandler extends AiModeHandler<Agent> {
	/** our hero */
	AiHero ourHero;
	/** zone */
	AiZone zone;
	/** Le numéro de la bombe */
	private final int BOMB_NUMBER = 1;
	/** la portée de la bombe */
	private final int BOMB_RANGE = 1;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected ModeHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

		//verbose = true;
		zone = ai.getZone();
		ourHero = zone.getOwnHero();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() {
		ai.checkInterruption();
		boolean result = true;
		if (ourHero.getBombNumberMax() >= BOMB_NUMBER
				|| ourHero.getBombRange() >= BOMB_RANGE) {
			result = true;
		}
		return result;
	}

	@Override
	protected boolean isCollectPossible() {
		ai.checkInterruption();
		boolean result = false;
		List<AiTile> tiles = new ArrayList<AiTile>();
		zone = this.ai.getZone();
		int zoneItemListVisible = ai.getZone().getItems().size();
		int zoneItemListHidden = ai.getZone().getHiddenItemsCount();

		tiles = zone.getTiles();

		if (zoneItemListVisible > 0) {

			for (AiItem item : zone.getItems()) {
				ai.checkInterruption();
				AiTile itemTile = item.getTile();

				if (tiles.contains(itemTile)) {
					result = true;
				}
			}
		}
		if (zoneItemListHidden > 0) {
			result = true;
		}
		return result;
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

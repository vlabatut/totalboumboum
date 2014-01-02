package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v2;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;

/**
 * Classe gérant les déplacements de l'agent.
 * 
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
public class ModeHandler extends AiModeHandler<Agent> {
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */

	protected ModeHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

	}

	

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Détermine si l'agent possède assez d'item.
     * 
     * @return true si l'agent possède assez d'item
     *          false sinon
	 */
	@Override
	protected boolean hasEnoughItems() {
		ai.checkInterruption();
		boolean result = false;
		if (ai.hasEnoughEnnemyAccesible()
				&& (ai.getZone().getOwnHero().getBombNumberMax() >= 1 || ai
						.getZone().getOwnHero().getBombRange() >= 1)) {
			result = true;
		}
		return result;
	}

	@Override
	/**
	 * Détermine si l'agent a la possibilité de ramasser des items dans la zone 
	 * courante.
     *
     * @return true si l'agent a accès à des items
     *          false sinon
	 */
	protected boolean isCollectPossible() {
		ai.checkInterruption();
		boolean result = false;

		for (AiItem aiItem : ai.getZone().getItems()) {
			ai.checkInterruption();
			if (aiItem.getType() == AiItemType.EXTRA_BOMB
					|| aiItem.getType() == AiItemType.EXTRA_FLAME
					|| aiItem.getType() == AiItemType.EXTRA_SPEED
					|| aiItem.getType() == AiItemType.RANDOM_EXTRA
					|| aiItem.getType() == AiItemType.GOLDEN_BOMB
					|| aiItem.getType() == AiItemType.GOLDEN_FLAME
					|| aiItem.getType() == AiItemType.GOLDEN_SPEED) {
				result = true;
			}

		}
		if (ai.getZone().getHiddenItemsCount() > 0) {
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

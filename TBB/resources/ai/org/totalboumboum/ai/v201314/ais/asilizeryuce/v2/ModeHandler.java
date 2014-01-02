package org.totalboumboum.ai.v201314.ais.asilizeryuce.v2;

import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;

/**
 * Classe gérant la selectionne des modes de l'agent.
 * 
 * @author Emre Asıl
 * @author Tülin İzer
 * @author Miray Yüce
 */
public class ModeHandler extends AiModeHandler<Agent> {

	/** cette variable definis combien de bombes qu'on a besoin*/
	private int BOMB_NEED=1;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai l'agent que cette classe doit gérer.
	 */
	protected ModeHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		verbose = false;

	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() {
		ai.checkInterruption();
		boolean result = true;

		AiHero hero = ai.getZone().getOwnHero();

		if (hero.getBombNumberMax() < BOMB_NEED)
			result = false;

		return result;
	}

	@Override
	protected boolean isCollectPossible() {
		ai.checkInterruption();
		boolean result = false;

		if (zoneHasItem(AiItemType.EXTRA_BOMB)
				|| zoneHasItem(AiItemType.GOLDEN_BOMB))
			result = true;

		return result;
	}

	/**
	 * Le recherhe s'il existe un item existe et visible dans la zone
	 * 
	 * @param itemtype le type d'item
	 * @return vrai/faux
	 */
	public boolean zoneHasItem(AiItemType itemtype) {
		ai.checkInterruption();
		for (AiItem item : ai.getZone().getItems()) {
			ai.checkInterruption();
			if (item.getType().equals(itemtype))
				return true;
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

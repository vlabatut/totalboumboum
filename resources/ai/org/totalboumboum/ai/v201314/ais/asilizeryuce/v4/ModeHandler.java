package org.totalboumboum.ai.v201314.ais.asilizeryuce.v4;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;

/**
 * Classe gérant les perceptions de mode de l'agent.
 * 
 * @author Emre Asıl
 * @author Tülin İzer
 * @author Miray Yüce
 */
@SuppressWarnings("deprecation")
public class ModeHandler extends AiModeHandler<Agent> {

	/** cette variable definis combien de bombes qu'on a besoin */
	private int BOMB_NEED = 2;

	/**
	 * Construit un gestionnaire pour l'agent passÃ© en paramÃ¨tre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gÃ©rer.
	 */
	protected ModeHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

		// on rÃ¨gle la sortie texte pour ce gestionnaire
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
		
		if (hero.getBombNumberMax() < BOMB_NEED) {
			ai.itemType = 1;
			return false;
		} else if (hero.getBombRange() < 2) {
			ai.itemType = 2;
			return false;
		}
		else if (hero.getWalkingSpeed() < 220.00) {
			ai.itemType = 3;
			return false;
		}
		return result;
	}

	@Override
	protected boolean isCollectPossible() {
		ai.checkInterruption();
		boolean result = false;

		if (ai.itemType == 1) {
			if (zoneHasItemVisible(AiItemType.EXTRA_BOMB)
					|| zoneHasItemVisible(AiItemType.GOLDEN_BOMB))
				return true;
		} if (ai.itemType == 2) {
			if (zoneHasItemVisible(AiItemType.EXTRA_FLAME)
					|| zoneHasItemVisible(AiItemType.GOLDEN_FLAME))
				return true;
		} if (ai.itemType == 3) {
		if (zoneHasItemVisible(AiItemType.EXTRA_SPEED)
				|| zoneHasItemVisible(AiItemType.GOLDEN_SPEED))
			return true;
	}

		return result;
	}

	/**
	 * Le recherhe s'il existe un item existe et visible dans la zone
	 * 
	 * @param itemtype
	 *            le type d'item
	 * @return vrai/faux
	 */
	public boolean zoneHasItemVisible(AiItemType itemtype) {
		ai.checkInterruption();
		for (AiItem item : ai.getZone().getItems()) {
			ai.checkInterruption();
			if (item.getType().equals(itemtype)) {
				if (ai.tileHandler.reacheableTiles.contains(item.getTile()))
					return true;
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

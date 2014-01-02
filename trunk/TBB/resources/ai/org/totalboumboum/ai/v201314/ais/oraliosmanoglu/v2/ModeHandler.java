package org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v2;

import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;

import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;

/**
 * decider mode de notre agent attaque ou collecte selon les mothodes.
 * 
 * @author Selen Oralı
 * @author Arman Osmanoğlu
 * 
 */
public class ModeHandler extends AiModeHandler<Agent> {

	/** notre agent */
	AiHero ownHero = ai.getZone().getOwnHero();
	/**
	 * toutes les items dans la zone de jeu
	 * 
	 */
	List<AiItem> itemsList = ai.getZone().getItems();

	/**
	 * controler les items. si notre agent a items, la retourne est vrai.
	 * 
	 * @param ai
	 *            notre agent
	 */
	protected ModeHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	@Override
	protected boolean hasEnoughItems() {
		ai.checkInterruption();
		boolean result = false;
		if (ownHero.getBombNumberCurrent() > 0)
			result = true;
		int bombnumber = ownHero.getBombNumberMax();
		int bombrange = ownHero.getBombRange();
		int bombRangeMult = bombnumber * bombrange;

		if ((bombRangeMult >= 8 || bombnumber > 5 || bombrange > 5
				&& ownHero.getWalkingSpeed() > 200))
			result = true;

		return result;
	}

	@Override
	protected boolean isCollectPossible() {
		ai.checkInterruption();
		boolean result = true;

		if (itemsList.size() > 0)
			result = true;

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * cette methode met a jour la sortie graphique
	 */
	protected void updateOutput() {
		ai.checkInterruption();

	}
}

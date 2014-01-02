package org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v2;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;

/**
 * cette classe decide que "Ou notre agent va poser le bombe".
 * 
 * @author Selen Oralı
 * @author Arman Osmanoğlu
 * 
 * 
 */
public class BombHandler extends AiBombHandler<Agent> {
	/**
	 * si notre agent est dans la case destination, notre agent a bombe et il
	 * n'y a pas de bombe dans cette case, on va poser le bombe. si seulement
	 * une controle n'est pas false, on ne va pas poser le bombe. result est
	 * boolean. Si on decide poser le bombe, result va etre vrai. sinon, false.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
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
		AiModeHandler<Agent> modeHandler = ai.getModeHandler();
		AiMode mode = modeHandler.getMode();
		boolean result = false;
		AiHero myHero = ai.getZone().getOwnHero();
		AiPreferenceHandler<Agent> preferenceHandler = ai
				.getPreferenceHandler();
		Map<Integer, List<AiTile>> preferences = preferenceHandler
				.getPreferencesByValue();
		int minPref = Collections.min(preferences.keySet());
		List<AiTile> tiles = preferences.get(minPref);

		boolean bombControl = true;

		if (mode == AiMode.ATTACKING) {
			ai.checkInterruption();

			for (AiBomb bombss : ai.getZone().getBombs()) {
				ai.checkInterruption();
				if (tiles.contains(bombss.getTile())) {
					ai.checkInterruption();
					bombControl = false;
					break;
				}

			}

			if (myHero.getBombNumberMax() > 0 && bombControl) {
				if (tiles.contains(ai.getZone().getOwnHero().getTile()))
					result = true;
			}
		}

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
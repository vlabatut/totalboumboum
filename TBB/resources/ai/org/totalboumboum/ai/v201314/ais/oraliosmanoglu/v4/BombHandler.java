package org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v4;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. Cf. la
 * documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * @author Selen Oralı
 * @author Arman Osmanoğlu
 */
public class BombHandler extends AiBombHandler<Agent> {
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
	/**
	 * Méthode permettant de déterminer si l'agent doit poser une bombe ou pas. Cette décision dépend, entre autres, des valeurs de préférence courantes, et éventuellement d'autres informations. 
		La méthode renvoie true si l'agent doit poser une bombe, et false sinon.
	 * @return boolean value
	 */
	protected boolean considerBombing() {
		ai.checkInterruption();

		boolean result = false;
		AiHero myHero = ai.getZone().getOwnHero();
		AiPreferenceHandler<Agent> preferenceHandler = ai
				.getPreferenceHandler();
		Map<Integer, List<AiTile>> preferences = preferenceHandler
				.getPreferencesByValue();

		int minPref = Collections.min(preferences.keySet());
		List<AiTile> tiles = preferences.get(minPref);
		AiTile ownTile = ai.getZone().getOwnHero().getTile();

		boolean bombControl = true;// tile de bomba var mı diye kontrol et
		for (AiBomb bombss : ai.getZone().getBombs()) {
			ai.checkInterruption();
			if (tiles.contains(bombss.getTile()) || ownTile == bombss.getTile()) {
				bombControl = false;// orda bomba var bomba koyma kontrol false
				break;
			}
		}
		boolean sonuc = ai.safeTiles.size() > 0;

		if (ai.isEnnemyAccesible) {
			if (myHero.getBombNumberMax() > 0 && bombControl && sonuc == true
					&& tiles.contains(ai.getZone().getOwnHero().getTile()))
				result = true;
		} else {
			if (ai.moveHandler.nextTile != null) {
				if (myHero.getBombNumberMax() > 0 && bombControl
						&& sonuc == true
						&& !ai.moveHandler.nextTile.isCrossableBy(myHero))
					result = true;
				else if (myHero.getBombNumberMax() > 0 && bombControl
						&& sonuc == true
						&& tiles.contains(ai.getZone().getOwnHero().getTile()))
					result = true;
			}
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
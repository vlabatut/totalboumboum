package org.totalboumboum.ai.v201314.ais.saylamsonmez.v2;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

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

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected ModeHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();
		/*
		 * // on règle la sortie texte pour ce gestionnaire verbose = false;
		 */

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
		zone = this.ai.getZone();
		AiHero ourHero = zone.getOwnHero();

		if (ai.getZone().getOwnHero().getBombNumberMax() >= 1) {
			print("on a assez bombe pour realiser la strategie mais maintenant il faut controler si sa porté est suffit ou pas");
			if (!this.ai.getZone().getRemainingOpponents().isEmpty()) {
				for (AiHero hero : this.ai.getZone().getRemainingOpponents()) {
					ai.checkInterruption();
					if (ai.getBlockSize(hero) > ourHero.getBombRange()) {
						print("la portée de notre bombe n'est pas suffit pour faire la strategie d'attaque!");
						result = false;
					} else {
						print("la portée de la bombe est suffit!");
						result = true;
					}
				}
			}
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
		tiles = this.ai.zone.getTiles();

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

package org.totalboumboum.ai.v201314.ais.saylamsonmez.v2;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. Cf. la
 * documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class BombHandler extends AiBombHandler<Agent> {
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
	protected BombHandler(Agent ai) {
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
	protected boolean considerBombing() {
		ai.checkInterruption();
		boolean result = false;

		if ((ourHero.getTile().getBombs().size() == 0 && (ourHero
				.getBombNumberCurrent() < ourHero.getBombNumberMax()))) {
			if (ai.isBlockingEnemy(ourHero.getTile()))
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

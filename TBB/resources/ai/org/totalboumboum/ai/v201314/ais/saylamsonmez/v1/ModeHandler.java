package org.totalboumboum.ai.v201314.ais.saylamsonmez.v1;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;

/**
 * Classe gérant les déplacements de l'agent. Cf. la documentation de
 * {@link AiModeHandler} pour plus de détails.
 *  
 * @author Berrenur Saylam
 * @author Kübra Sönmez
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

		// on règle la sortie texte pour ce gestionnaire
		verbose = true;
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() {
		ai.checkInterruption();
		boolean result = true;
		return result;
	}

	@Override
	protected boolean isCollectPossible() {
		ai.checkInterruption();
		boolean result = false;
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

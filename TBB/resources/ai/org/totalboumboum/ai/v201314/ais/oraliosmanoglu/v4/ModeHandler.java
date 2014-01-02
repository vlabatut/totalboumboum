package org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v4;

import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;

/**
 * Classe gérant le mode de l'agent. Elle implémente la
 *         méthode update, utilisée pour mettre le mode à jour, et qui ne peut
 *         pas être modifiée ni surchargée. Cette méthode implémente
 *         l'algorithme de sélection du mode défini en cours, qui est imposé.
 *         Elle fait appel aux méthodes hasEnoughItems et isCollectPossible(),
 *         qui, elles, doivent être surchargées. Enfin, cette classe stocke le
 *         mode courant grâce au champ mode.
 *         
 * @author Selen Oralı
 * @author Arman Osmanoğlu 
 */
public class ModeHandler extends AiModeHandler<Agent> {
	/**
	 * decider le mode: collect ou attack
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
	@Override
	/**
	 * Détermine si l'agent possède assez d'items, ou bien s'il doit essayer d'en ramasser d'autres. Cette 
	distinction est relative à l'environnement, à l'agent lui-même et à la stratégie qu'il utilise. 
	Cette méthode est utilisée lors de la mise à jour du mode par update.
	 * @return boolean value
	 */
	protected boolean hasEnoughItems() {
		ai.checkInterruption();
		boolean result = true;
		AiHero ownHero = ai.getZone().getOwnHero();
		if (ownHero.getBombNumberCurrent() == 0)
			result = false;

		return result;
	}

	@Override
	/**
	 * Détermine si l'agent a la possibilité de ramasser des items dans la zone courante : présence d'items 
	cachés ou découverts, assez de temps restant, etc. 
	Cette méthode est utilisée lors de la mise à jour du mode par update.
	 * @return boolean value
	 */
	protected boolean isCollectPossible() {
		ai.checkInterruption();
		boolean result = false;

		List<AiItem> itemsList = ai.getZone().getItems();
		for (AiItem items : itemsList) {
			ai.checkInterruption();
			if (ai.acces.contains(items.getTile()))
				result = true;
			break;

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

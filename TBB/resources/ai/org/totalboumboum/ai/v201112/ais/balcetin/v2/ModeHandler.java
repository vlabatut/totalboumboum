package org.totalboumboum.ai.v201112.ais.balcetin.v2;

import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;

import org.totalboumboum.ai.v201112.adapter.data.AiZone;

/**
 * Mode handler class to choose the right mode for the AI.
 * 
 * @author Adnan Bal
 * @author Özcan Çetin
 */
@SuppressWarnings("deprecation")
public class ModeHandler extends AiModeHandler<BalCetin> {

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected ModeHandler(BalCetin ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
		zone = ai.getZone();
		ownHero = zone.getOwnHero();

	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	/** Zone de jeu */
	private AiZone zone;
	/** Personnage contrôlé par l'agent */
	private AiHero ownHero;

	protected boolean hasEnoughItems() throws StopRequestException {
		ai.checkInterruption();
		boolean result = false;

		if (ownHero.getBombNumberCurrent() >= 2 && ownHero.getBombRange() >= 3)
			result = true;
		else
			result = false;

		// if remaining time is lower than 3/4 of the time limit, than directly
		// pass to attack mode, to have more points.

		System.out.println("\nHas enoughitems result = " + result);
		return result;
	}

	@Override
	protected boolean isCollectPossible() throws StopRequestException {
		ai.checkInterruption();
		boolean result = false;

		int hiddenitems = zone.getHiddenItemsCount();
		// if there is at least 1 hidden item and the remaining time is higher
		// than a half.
		if (hiddenitems > 0
				&& (zone.getTotalTime() < (0.5) * zone.getLimitTime()))
			result = true;
		else
			result = false;

		System.out.println("\n is collect possible result = " + result);
		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	protected void updateOutput() throws StopRequestException {
		ai.checkInterruption();
		
	}
}

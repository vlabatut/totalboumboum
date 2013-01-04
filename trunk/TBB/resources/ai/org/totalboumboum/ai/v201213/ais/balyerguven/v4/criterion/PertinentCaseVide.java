package org.totalboumboum.ai.v201213.ais.balyerguven.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.balyerguven.v4.BalyerGuven;

/**
 * our PertinentCaseVide class
 * 
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */
public class PertinentCaseVide extends AiUtilityCriterionBoolean<BalyerGuven> {
	/** le pertinent de case vide */
	public static final String NAME = "PertinentCaseVide";

	/**
	 * Crée un nouveau critère boolean.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public PertinentCaseVide(BalyerGuven ai) throws StopRequestException { // init
																			// nom
		super(ai, NAME);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		boolean result = false;

		AiZone zone = ai.getZone();
		AiHero myHero = zone.getOwnHero();
		if (tile.getBlocks().isEmpty()
				&& tile.getBombs().isEmpty()
				&& tile.getFires().isEmpty()
				&& (tile.getHeroes().isEmpty() || tile.getHeroes().contains(
						myHero))) {
			result = true;
		}
		return result;
	}
}
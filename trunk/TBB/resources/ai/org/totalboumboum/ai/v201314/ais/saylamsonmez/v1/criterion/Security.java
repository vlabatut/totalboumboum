/**
 * 
 */
package org.totalboumboum.ai.v201314.ais.saylamsonmez.v1.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v1.Agent;

/**
 * Ce critère utilise pour toutes les cases dans la zone. Il évolue le status de
 * securité de la case.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class Security extends AiCriterionBoolean<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "Security";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Security(Agent ai) throws StopRequestException {
		super(ai, NAME);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		
		if (this.ai.getCurrentDangerousTiles().contains(tile)) {
			return false;
		}

		return true;
	}
}

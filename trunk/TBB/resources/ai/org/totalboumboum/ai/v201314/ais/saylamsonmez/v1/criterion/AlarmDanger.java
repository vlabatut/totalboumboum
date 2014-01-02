/**
 * 
 */
package org.totalboumboum.ai.v201314.ais.saylamsonmez.v1.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v1.Agent;

/**
 * Ce critère marche comme le critère securité. La difference, il n'y a pas de
 * bombe réel mais on compte si c'est le cas alors on n'utilise pas cette route
 * pour aller notre case qui est but.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class AlarmDanger extends AiCriterionBoolean<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "AlarmDanger";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public AlarmDanger(Agent ai) throws StopRequestException {
		super(ai, NAME);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		return ai.isDirectionEnemy(tile);

	}
}

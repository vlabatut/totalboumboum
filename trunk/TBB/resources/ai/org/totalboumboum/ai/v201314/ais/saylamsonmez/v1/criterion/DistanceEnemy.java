/**
 * 
 */
package org.totalboumboum.ai.v201314.ais.saylamsonmez.v1.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v1.Agent;

/**
 * Ce critère définie entre [0;3]. Il donne ces valeurs aux cases de la manière
 * de plus proche à plus loin. Si la distance(Manhattan) est plus grande que 3
 * alors on compte ça comme il est 3. Càd, 3 et plus grand que 3 ça vaut dire
 * qu'il est loin à cette case.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class DistanceEnemy extends AiCriterionInteger<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "DistanceEnemy";
	/** Domaine de définition */
	public static final int DISTANCE_LIMIT = 3;

	/**
	 * Crée un nouveau critère.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public DistanceEnemy(Agent ai) throws StopRequestException {
		super(ai, NAME, 0, 3);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	@Override
	public Integer processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		int result = 0;

		if (this.ai.getDistanceBetweenEnemy(tile) >= 3)
			result = 3;
		else {
			if (this.ai.getDistanceBetweenEnemy(tile) == 2)
				result = 2;
			else if (this.ai.getDistanceBetweenEnemy(tile) == 1)
				result = 1;
			else if (this.ai.getDistanceBetweenEnemy(tile) == 0)
				result = 0;
		}

		return result;

	}
}

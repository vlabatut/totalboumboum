package org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v4.Agent;

/**
 * cette critere nous envoyer la distance(integer(0,1 ou 2)) entre le
 * case et l'adversaire.
 * 
 * @author Selen Oralı
 * @author Arman Osmanoğlu
 */
@SuppressWarnings("deprecation")
public class Distance extends AiCriterionInteger<Agent> {

	/**
	 * variable de nom cette critere
	 */
	public static final String NAME = "Distance";

	/**
	 * cette critere nous envoyer la distance(integer(0,1 ou 2)) entre le case
	 * et l'adversaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	public Distance(Agent ai) {
		super(ai, NAME, 0, 2);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	/**
	 * cette critere nous envoyer la distance entre le case et l'adversaire.
	 * @param tile notre cases dans selection des cases
	 * @return integer 0,1 ou 2
	 */
	public Integer processValue(AiTile tile) {
		ai.checkInterruption();
		int result = 0;
		for (AiHero adversaire : ai.getZone().getRemainingOpponents()) {
			ai.checkInterruption();
			int distance = ai.getZone().getTileDistance(adversaire.getTile(),
					tile);
			if (distance >= 0 && distance < 3)
				result = 0;
			else if (distance >= 3 && distance < 6)
				result = 1;
			else
				result = 2;
		}

		return result;
	}
}
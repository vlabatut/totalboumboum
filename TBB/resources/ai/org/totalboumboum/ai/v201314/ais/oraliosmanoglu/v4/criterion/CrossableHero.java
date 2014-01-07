package org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v4.Agent;

/**
 * cette critere nous envoyer true, si on peut traverser directement à
 * les adversaire (qui est plus proche) . sinon, false.
 * 
 * @author Selen Oralı
 * @author Arman Osmanoğlu
 */
@SuppressWarnings("deprecation")
public class CrossableHero extends AiCriterionBoolean<Agent> {
	/**
	 * variable de nom cette critere
	 */
	public static final String NAME = "CrossableHero";

	/**
	 * Crée un nouveau critère binaire on envoye true, si on peut traverser
	 * directement à les adversaire (qui est plus proche) . sinon, false.
	 * 
	 * @param ai
	 *            agent l'agent concerné.
	 */
	public CrossableHero(Agent ai) {
		super(ai, NAME);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	/**
	 * controler on peut traverser directement à
	 * les adversaire (qui est plus proche) 
	 * @param tile notre cases dans selection des cases
	 * @return boolean true ou false
	 */
	public Boolean processValue(AiTile tile) {
		ai.checkInterruption();
		return ai.isEnnemyAccesible;
	}
}

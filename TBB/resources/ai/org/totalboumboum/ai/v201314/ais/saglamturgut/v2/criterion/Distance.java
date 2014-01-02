package org.totalboumboum.ai.v201314.ais.saglamturgut.v2.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saglamturgut.v2.Agent;

/**
 * Distance criterion.
 * 
 * @author Neşe Güneş
 * @author Esra Sağlam
 * @author Siyabend Turgut
 */
public class Distance extends AiCriterionInteger<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "DISTANCE";

	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	public Distance(Agent ai) {
		super(ai, NAME, 0, 1);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue(AiTile tile) {
		ai.checkInterruption();
		return ai.getZone().getTileDistance(ai.getZone().getOwnHero().getTile(), tile) < 5 ? 0 : 1;
	}
}

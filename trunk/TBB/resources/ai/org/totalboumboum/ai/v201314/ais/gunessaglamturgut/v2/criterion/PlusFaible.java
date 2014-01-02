package org.totalboumboum.ai.v201314.ais.gunessaglamturgut.v2.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.gunessaglamturgut.v2.Agent;

/**
 * Weakest enemy criterion.
 * 
 * @author Neşe Güneş
 * @author Esra Sağlam
 * @author Siyabend Turgut
 */
public class PlusFaible extends AiCriterionInteger<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "PLUS_FAIBLE";

	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	public PlusFaible(Agent ai) {
		super(ai, NAME, 0, 1);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue(AiTile tile) {
		ai.checkInterruption();
		ai.checkInterruption();
		for (AiHero hero : tile.getHeroes()) {
			ai.checkInterruption();
			if (ai.getZone().getOwnHero().getBombNumberMax() >= hero
					.getBombNumberMax())
				return 1;
		}
		return 0;
	}
}
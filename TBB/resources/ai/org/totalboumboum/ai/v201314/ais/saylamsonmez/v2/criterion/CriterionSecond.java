package org.totalboumboum.ai.v201314.ais.saylamsonmez.v2.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v2.Agent;

/**
 * Cette classe est un simple exemple de critère entier.
 * 
 * Comme pour le moment on n'a pas des critères pour le mode collecte, on a
 * utilisé vos critères exemples comme notre critères pour le mode collecte.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class CriterionSecond extends AiCriterionInteger<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "SECOND_CRITERION";

	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	public CriterionSecond(Agent ai) {
		super(ai, NAME, 1, 3);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue(AiTile tile) {
		ai.checkInterruption();
		int result = 2;

		return result;
	}
}

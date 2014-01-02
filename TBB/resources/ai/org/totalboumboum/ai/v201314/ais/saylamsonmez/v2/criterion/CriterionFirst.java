package org.totalboumboum.ai.v201314.ais.saylamsonmez.v2.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v2.Agent;

/**
 * Cette classe est un simple exemple de critère binaire.
 * 
 * Comme pour le moment on n'a pas des critères pour le mode collecte, on a
 * utilisé vos critères exemples comme notre critères pour le mode collecte.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class CriterionFirst extends AiCriterionBoolean<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "FIRST_CRITERION";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	public CriterionFirst(Agent ai) {
		super(ai, NAME);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Boolean processValue(AiTile tile) {
		ai.checkInterruption();
		boolean result = true;
		return result;
	}
}

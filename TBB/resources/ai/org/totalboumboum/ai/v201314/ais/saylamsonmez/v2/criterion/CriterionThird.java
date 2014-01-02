package org.totalboumboum.ai.v201314.ais.saylamsonmez.v2.criterion;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionString;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v2.Agent;

/**
 * Cette classe est un simple exemple de critère chaîne de caractères.
 * 
 * Comme pour le moment on n'a pas des critères pour le mode collecte, on a
 * utilisé vos critères exemples comme notre critères pour le mode collecte.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class CriterionThird extends AiCriterionString<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "THIRD_CRITERION";
	/** Domaine de définition */
	public static final Set<String> DOMAIN = new TreeSet<String>(Arrays.asList(
			"VALUE1", "VALUE2", "VALUE3", "VALUE4", "VALUE5"));

	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	public CriterionThird(Agent ai) {
		super(ai, NAME, DOMAIN);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected String processValue(AiTile tile) {
		ai.checkInterruption();
		String result = DOMAIN.iterator().next(); // on renvoie une valeur
													// arbitraire, pour
													// l'exemple

		return result;
	}
}

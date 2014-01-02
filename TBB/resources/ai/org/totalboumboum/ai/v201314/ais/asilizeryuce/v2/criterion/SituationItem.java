package org.totalboumboum.ai.v201314.ais.asilizeryuce.v2.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.asilizeryuce.v2.Agent;

/**
 * Cette critere est pour savoir que si une item est malus ou pas
 * 
 * @author Emre Asıl
 * @author Tülin İzer
 * @author Miray Yüce
 */
public class SituationItem extends AiCriterionInteger<Agent> {

	/** Nom de ce critère */
	public static final String NAME = "SITUATION_ITEM";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	public SituationItem(Agent ai) {
		super(ai, NAME, 0, 2);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) {
		ai.checkInterruption();

		if (tile.getItems().isEmpty())
			return 1;
		else {
			ai.checkInterruption();
			for (AiItem item : tile.getItems()) {
				ai.checkInterruption();

				if (ai.isMalus(item))
					return 2;
			}
		}
		return 0;
	}
}

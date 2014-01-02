/**
 * 
 */
package org.totalboumboum.ai.v201314.ais.asilizeryuce.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.asilizeryuce.v4.Agent;

/**
 * Cette critere nous donne 0 s'il y a d'item comme on veut, extra bombe, extra
 * flamme, flamme d'or et bombe d'or, et 1 s'il n'y a pas d'item comme on veut
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
				if (ai.itemType == 1) {
					if (item.getType().equals(AiItemType.EXTRA_BOMB)
							|| item.getType().equals(AiItemType.GOLDEN_BOMB))
						return 0;
				} else if (ai.itemType == 2) {
					if (item.getType().equals(AiItemType.EXTRA_FLAME)
							|| item.getType().equals(AiItemType.GOLDEN_FLAME))
						return 0;
				} else if (ai.itemType == 3) {
					if (item.getType().equals(AiItemType.EXTRA_SPEED)
							|| item.getType().equals(AiItemType.GOLDEN_SPEED))
						return 0;
				}
			}
			return 2;
		}
	}
}

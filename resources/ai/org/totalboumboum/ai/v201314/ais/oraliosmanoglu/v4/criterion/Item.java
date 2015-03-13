package org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v4.criterion;

import java.util.List;

import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v4.Agent;

/**
 * cette citere nous envoyer true, si nous traversons directement l'item
 * qui est nous intéressé.
 * 
 * @author Selen Oralı
 * @author Arman Osmanoğlu
 */
@SuppressWarnings("deprecation")
public class Item extends AiCriterionBoolean<Agent> {
	/**
	 * variable de nom cette critere
	 */
	public static final String NAME = "Item";

	/**
	 * on controle les items. On envoye true, si nous traversons directement
	 * l'item qui est nous intéressé
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	public Item(Agent ai) {
		super(ai, NAME);
		ai.checkInterruption();
	}

	/**
	 * Controler si l'item est bonus, on nous envoye true.
	 * 
	 * @param item
	 *            items dans la zone
	 * 
	 * @return boolean value
	 */
	public boolean isBonus(AiItem item) {
		ai.checkInterruption();
		AiItemType type = item.getType();
		if (type.equals(AiItemType.EXTRA_BOMB)
				|| type.equals(AiItemType.EXTRA_FLAME)
				|| type.equals(AiItemType.EXTRA_SPEED)
				|| type.equals(AiItemType.GOLDEN_BOMB)
				|| type.equals(AiItemType.GOLDEN_FLAME)
				|| type.equals(AiItemType.GOLDEN_SPEED)
				|| type.equals(AiItemType.RANDOM_EXTRA))
			return true;
		return false;
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	/**
	 * 
	 * controler si nous traversons directement l'item (qui est nous intéressé) avec la methode "isBonus"
	 * @param tile notre cases dans selection des cases
	 * @return boolean true ou false
	 */
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		boolean result = false;

		List<AiItem> items = ai.getZone().getItems();
		for (AiItem aiItem : items) {
			ai.checkInterruption();
			if (isBonus(aiItem) && ai.acces.contains(aiItem.getTile()))
				result = true;
			break;

		}
		return result;
	}
}

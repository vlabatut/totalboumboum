/**
 * 
 */
package org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v2.criterion;

import java.util.List;

import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v2.Agent;

/**
 * cette criter controle si les items sont accesible.
 * 
 * s'il y a seulement une item, on retourne true. s'il n'y a pas des items
 * accessibles, on retourne false.
 * 
 * @author Selen Oralı
 * @author Arman Osmanoğlu
 * 
 */
public class ItemAccess extends AiCriterionBoolean<Agent> {
	/**
	 * description de nom de critere.
	  */
	public static final String NAME = "ItemAccess";

	/**
	 * @param ai
	 *            agent
	 * 
	 */

	public ItemAccess(Agent ai) {
		super(ai, NAME);
		ai.checkInterruption();
	}

	/**
	 * les items dane le zone jeu on va controlle le liste d'item comme ça parce
	 * que liste d'item renvoie les items par leurs type
	 * 
	 * @param item
	 *            item
	 * 
	 * 
	 * @return boolean result
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
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		boolean result = false;
		List<AiItem> items = ai.getZone().getItems();

		for (AiItem aiItem : items) {
			ai.checkInterruption();

			if (isBonus(aiItem) == true)
				result = true;
			break;
		}

		return result;
	}
}

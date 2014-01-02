package org.totalboumboum.ai.v201314.ais.asilizeryuce.v4;

import org.totalboumboum.ai.v201314.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;

/**
 * Classe gérant les calculs des items de la zone.
 * 
 * 
 * @author Emre Asıl
 * @author Tülin İzer
 * @author Miray Yüce
 */
public class ItemHandler extends AiAbstractHandler<Agent> {
	
	/**
	 * Construit un gestionnaire pour les items.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	protected ItemHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();
	}

	/**
	 * Le recherhe s'il existe un item dans la zone
	 * 
	 * @param itemtype
	 *            le type d'item
	 * @return vrai/faux
	 */
	public boolean zoneHasItem(AiItemType itemtype) {
		ai.checkInterruption();
		for (AiItem item : ai.getZone().getItems()) {
			ai.checkInterruption();
			if (item.getType().equals(itemtype)) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * cette methode est pour savoir si un item est un malus ou pas
	 * 
	 * @param item
	 *            item qu'on veut savoir s'il est un item malus
	 * @return true s'il ya un malus item, sinon false
	 */
	public boolean isMalus(AiItem item) {
		ai.checkInterruption();
		AiItemType type = item.getType();
		if (type.equals(AiItemType.ANTI_BOMB)
				|| type.equals(AiItemType.ANTI_FLAME)
				|| type.equals(AiItemType.ANTI_SPEED)
				|| type.equals(AiItemType.NO_BOMB)
				|| type.equals(AiItemType.NO_FLAME)
				|| type.equals(AiItemType.NO_SPEED)
				|| type.equals(AiItemType.RANDOM_NONE))
			return true;
		return false;
	}

}

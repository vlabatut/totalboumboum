/**
 * 
 */
package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v4;

import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;

/**
 * Classe gérant la situation des items sur la zone de jeu.
 * 
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
public class ItemHandler extends AiAbstractHandler<Agent> {

	/**
	 * @param ai
	 *            agent concerné
	 */
	public ItemHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

	}

	/**
	 * Calcule l'existence de bonus sur toute la zone de jeu..
	 * 
	 * @return true si il y a au moins un bonus false sinon
	 */
	public boolean usefulItemsExistence() {
		ai.checkInterruption();

		for (AiItem aiItem : ai.getZone().getItems()) {
			ai.checkInterruption();

			if ( aiItem.getType() == AiItemType.EXTRA_BOMB || aiItem.getType() == AiItemType.EXTRA_FLAME
					|| aiItem.getType() == AiItemType.EXTRA_SPEED || aiItem.getType() == AiItemType.RANDOM_EXTRA
					|| aiItem.getType() == AiItemType.GOLDEN_BOMB || aiItem.getType() == AiItemType.GOLDEN_FLAME
					|| aiItem.getType() == AiItemType.GOLDEN_SPEED && this.usefulItemsAccesibility(aiItem) ) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Calcule la liste des bonus se trouvant sur la zone de jeu.
	 * 
	 * @return usefulItemsList liste des bonus
	 */
	public ArrayList<AiItem> usefulItemsList() {
		ai.checkInterruption();

		ArrayList<AiItem> usefulItemsList = new ArrayList<AiItem>();

		for (AiItem aiItem : ai.getZone().getItems()) {
			ai.checkInterruption();

			if ( (aiItem.getType() == AiItemType.EXTRA_BOMB || aiItem.getType() == AiItemType.EXTRA_FLAME
					|| aiItem.getType() == AiItemType.EXTRA_SPEED || aiItem.getType() == AiItemType.RANDOM_EXTRA
					|| aiItem.getType() == AiItemType.GOLDEN_BOMB || aiItem.getType() == AiItemType.GOLDEN_FLAME || aiItem.getType() == AiItemType.GOLDEN_SPEED) ) {
				usefulItemsList.add(aiItem);
			}
		}
		return usefulItemsList;
	}

	/**
	 * Calcule l'accessibilité d'un bonus se trouvant sur la zone de jeu.
	 * 
	 * @param item
	 *            bonus concerné
	 * 
	 * @return true si le bonus est accessible false sinon
	 */
	public boolean usefulItemsAccesibility(AiItem item) {
		ai.checkInterruption();

//		if ( this.usefulItemsExistence() ) {

			if ( ai.selectTiles.contains(item.getTile()) )
				return true;
//		}
		return false;
	}

	/**
	 * Calcule si l'agent doit ramasser des bonus parmis les bonus accessible.
	 * 
	 * @return true si il doit ramasser des bonus false sinon
	 */
	public boolean ItemNeed() {
		ai.checkInterruption();

		AiHero ownHero = ai.getZone().getOwnHero();

		for (AiItem usefulItem : this.usefulItemsList()) {
			ai.checkInterruption();

			if ( !ai.getHH().aroundEnnemy(ownHero.getTile()) && this.usefulItemsAccesibility(usefulItem) ) {
				if ( usefulItem.getType() == AiItemType.EXTRA_BOMB && ownHero.getBombNumberMax() < 3 )
					return true;
				if ( usefulItem.getType() == AiItemType.EXTRA_FLAME && ownHero.getBombRange() < 3 )
					return true;
				if ( usefulItem.getType() == AiItemType.EXTRA_SPEED || usefulItem.getType() == AiItemType.GOLDEN_SPEED )
					return true;
			}
		}
		return false;
	}

	/**
	 * Calcule le bonus nécessaire à l'agent.
	 * 
	 * @return usefulItem le bonus dont l'agent a besoin null s'il na besoin d'aucun bonus
	 */
	public AiItem whatINeed() {
		ai.checkInterruption();

		AiHero ownHero = ai.getZone().getOwnHero();

		for (AiItem usefulItem : this.usefulItemsList()) {
			ai.checkInterruption();

			if ( !ai.getHH().aroundEnnemy(ownHero.getTile()) && this.usefulItemsAccesibility(usefulItem) ) {
				if ( usefulItem.getType() == AiItemType.EXTRA_BOMB && ownHero.getBombNumberMax() < 3 )
					return usefulItem;
				if ( usefulItem.getType() == AiItemType.EXTRA_FLAME && ownHero.getBombRange() < 3 )
					return usefulItem;
				if ( usefulItem.getType() == AiItemType.EXTRA_SPEED || usefulItem.getType() == AiItemType.GOLDEN_SPEED )
					return usefulItem;
			}
		}
		return null;

	}

	/**
	 * Calcule la liste des malus existant sur la zone de jeu.
	 * 
	 * @return dangerousItemsList Liste des malus
	 */
	public ArrayList<AiItem> dangerousItemsList() {
		ai.checkInterruption();

		ArrayList<AiItem> dangerousItemList = new ArrayList<AiItem>();

		for (AiItem aiItem : ai.getZone().getItems()) {
			ai.checkInterruption();

			if ( aiItem.getType() == AiItemType.NO_BOMB || aiItem.getType() == AiItemType.NO_FLAME
					|| aiItem.getType() == AiItemType.NO_SPEED || aiItem.getType() == AiItemType.ANTI_BOMB
					|| aiItem.getType() == AiItemType.ANTI_FLAME || aiItem.getType() == AiItemType.ANTI_SPEED
					|| aiItem.getType() == AiItemType.RANDOM_NONE || aiItem.getType() == AiItemType.OTHER
					|| aiItem.getType() == AiItemType.PUNCH ) {
				dangerousItemList.add(aiItem);
			}
		}
		return dangerousItemList;
	}

	/**
	 * Vérifie l'existence de malus sur la case du prochain déplacement de l'agent.
	 * 
	 * @return true si la prochaine case contient un malus false sinon
	 */
	public boolean dangerousItemOnMyNextTile() {
		ai.checkInterruption();

		for (AiItem items : dangerousItemsList()) {
			ai.checkInterruption();

			if ( ai.moveHandler.nextTile != null ) {
				if ( ai.selectTiles.contains(items.getTile()) && ai.moveHandler.nextTile == items.getTile() )
					return true;
			}
		}

		return false;
	}
}

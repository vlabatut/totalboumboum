package org.totalboumboum.ai.v201213.ais.erdemtayyar.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.ais.erdemtayyar.v4.ErdemTayyar;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;

/**
 * C'est un critere binaire. Cette classe est pour la critere pertinence, pour
 * montrer si on a besoin d'un item ou pas.
 * 
 * @author Banu Erdem
 * @author Zübeyir Tayyar
 */
@SuppressWarnings({ "rawtypes", "deprecation" })
public class Pertinence extends AiUtilityCriterionBoolean {
	/**
	 * We affect the name of out criteria
	 */
	public static final String NAME = "Pertinence";
	/**
	 * Limit for the bomb item
	 */
	public static final int BombLimit = 3;

	/**
	 * Limit for the flame item
	 */
	public static final int FlameLimit = 2;
	/**
	 * limit for speed item
	 */
	public static final double SPEED_LIMIT = 150;

	// CONSTRUCTOR
	/**
	 * On initialise la valeur dont la domaine de définition est TRUE et FALSE.
	 * Si on a besoin d'un item sa valeur est TRUE sinon FALSE.
	 * 
	 * @param ai
	 * 		information manquante !?	
	 * @throws StopRequestException
	 * 		information manquante !?	
	 */
	@SuppressWarnings("unchecked")
	public Pertinence(ErdemTayyar ai) throws StopRequestException {
		super(ai, NAME);
		ai.checkInterruption();

	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		boolean result = true;
		int bombNumber = ((ErdemTayyar) this.ai).getHero().getBombNumberMax();
		int fireRange = ((ErdemTayyar) this.ai).getHero().getBombRange();
		double Speed = ((ErdemTayyar) this.ai).getHero().getWalkingSpeed();
		for (AiItem item : tile.getItems()) {
			ai.checkInterruption();
			if (item.getType() == AiItemType.EXTRA_BOMB
					|| item.getType() == AiItemType.GOLDEN_BOMB) {
				if (bombNumber <= BombLimit) {
					result = true;
				} else
					result = false;
			}

			if (item.getType() == AiItemType.EXTRA_FLAME
					|| item.getType() == AiItemType.GOLDEN_FLAME) {
				if (fireRange <= FlameLimit) {
					result = true;
				} else
					result = false;
			}
			if (item.getType() == AiItemType.EXTRA_SPEED
					|| item.getType() == AiItemType.GOLDEN_SPEED) {
				if (Speed <= SPEED_LIMIT) {
					result = true;
				} else
					result = false;
			}

		}
		return result;
	}
}

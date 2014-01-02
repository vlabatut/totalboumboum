package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v1.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v1.Agent;

/**
 * Cette classe permet de calculer la distance de Manhattan entre la case où se
 * trouve notre agent et la case séléctionnée.
 *
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
public class Distance extends AiCriterionInteger<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "DISTANCE";

	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	public Distance(Agent ai) {
		super(ai, NAME, 0, 3);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
    /**
	 * Calcul la valeur du critère.
	 *
	 * @param tile
	 *            la case séléctionnée.
     * @return result
     *             la valeur de la proximitée de la case séléctionnée.
	 */
	@Override
	public Integer processValue(AiTile tile) {
		ai.checkInterruption();
		int result = 0;
		AiTile myTile = ai.getZone().getOwnHero().getTile();
		int myDistance = ai.getZone().getTileDistance(myTile, tile);
		if (myDistance >= 9) {
			result = 3;
		} else if (myDistance >= 6 && myDistance < 9) {
			result = 2;
		} else if (myDistance >= 3 && myDistance < 6) {
			result = 1;
		} else if (myDistance >= 0 && myDistance < 3) {
			result = 0;
		}
		return result;
	}
}

package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v4.Agent;

/**
 * Cette classe permet de calculer la distance de Manhattan entre la case où se trouve notre agent
 * et la case séléctionnée.
 * 
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
@SuppressWarnings("deprecation")
public class Distance extends AiCriterionBoolean<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "DISTANCE";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 */
	public Distance(Agent ai) {
		super(ai, NAME);
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
	 * @return result la valeur de la proximitée de la case séléctionnée.
	 * 
	 */
	@Override
	public Boolean processValue(AiTile tile) {
		ai.checkInterruption();

		boolean result = true;

		AiTile myTile = ai.getZone().getOwnHero().getTile();

		int myDistance = ai.getCG().nonCyclicTileDistance(myTile, tile);

		if ( myDistance > 5 ) {
			result = false;
		} else {
			result = true;
		}
		return result;
	}
}

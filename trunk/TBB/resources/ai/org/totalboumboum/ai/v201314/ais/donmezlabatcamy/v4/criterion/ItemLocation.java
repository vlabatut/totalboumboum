package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v4.Agent;

/**
 * Cette classe permet de dire si une case contient un bonus.
 * 
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
public class ItemLocation extends AiCriterionBoolean<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "ITEMLOCATION";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 */
	public ItemLocation(Agent ai) {
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
	 * @return result true si la case contient un bonus 
	 * 				  false sinon
	 * 
	 */
	@Override
	public Boolean processValue(AiTile tile) {
		ai.checkInterruption();

		boolean result = false;

		if ( ai.getIH().whatINeed() != null ) {
			if ( tile == ai.getIH().whatINeed().getTile() ) {
				result = true;
			}
		}
		return result;
	}
}

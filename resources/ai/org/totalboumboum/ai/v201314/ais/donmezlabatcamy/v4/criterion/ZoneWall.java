package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v4.Agent;

/**
 * Cette classe permet de calculer le nombre de murs existant au voisinage de la case de l'adversaire.
 * 
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
@SuppressWarnings("deprecation")
public class ZoneWall extends AiCriterionInteger<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "ZONEWALL";

	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	public ZoneWall(Agent ai) {
		super(ai, NAME, 0, 2);
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
	 * @return result le nombre de murs sur les cases voisine de l'adversaire.
	 */
	@Override
	public Integer processValue(AiTile tile) {
		ai.checkInterruption();

		return ai.getCG().zoneWall(tile);
	}
}

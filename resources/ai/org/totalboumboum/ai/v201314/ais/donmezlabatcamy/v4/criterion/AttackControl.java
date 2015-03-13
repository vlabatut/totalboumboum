package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v4.Agent;

/**
 * Cette classe permet de dire si l'agent est bloqué entre un mur et un adversaire.
 * 
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
@SuppressWarnings("deprecation")
public class AttackControl extends AiCriterionBoolean<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "ATTACKCONTROL";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 */
	public AttackControl(Agent ai) {
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
	 * @return result valeur du critère
	 * 
	 */
	@Override
	public Boolean processValue(AiTile tile) {
		ai.checkInterruption();
		if ( ai.getZone().getRemainingOpponents().size() == 1 )
			return true;
		else
			return ai.getTH().dontStayBetweenWallAndEnnemyControl(tile);
	}
}

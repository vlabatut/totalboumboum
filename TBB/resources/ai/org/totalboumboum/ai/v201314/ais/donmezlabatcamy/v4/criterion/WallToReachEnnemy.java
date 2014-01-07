package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v4.Agent;

/**
 * Cette classe permet de dire si l'agent se trouve sur une case au voisinage d'un mur destructible
 * qui se trouve aussi sur une case voisine d'un ennemi.
 * 
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
@SuppressWarnings("deprecation")
public class WallToReachEnnemy extends AiCriterionBoolean<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "WALLTOREACHENNEMY";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 */
	public WallToReachEnnemy(Agent ai) {
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

		if ( ai.getWallToReachEnnemy != null ) {
			if ( ai.getWallToReachEnnemy == tile )
				return true;
		}

		return false;
	}
}

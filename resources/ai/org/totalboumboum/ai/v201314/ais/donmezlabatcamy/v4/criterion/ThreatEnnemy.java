package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v4.Agent;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe permet de décider si une case donnée est une des cases voisines d'un des ennemis
 * sur la zone de jeu.
 * 
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
@SuppressWarnings("deprecation")
public class ThreatEnnemy extends AiCriterionBoolean<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "THREATENNEMY";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 */
	public ThreatEnnemy(Agent ai) {
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
	 * @return result 
	 */
	@Override
	public Boolean processValue(AiTile tile) {
		ai.checkInterruption();

		boolean result = false;

		for (AiHero ennemy : ai.getZone().getRemainingOpponents()) {
			ai.checkInterruption();
			for (Direction direction : Direction.getPrimaryValues()) {
				ai.checkInterruption();
				if ( ennemy.getTile().getNeighbor(direction) == tile ) {
					result = true;
				}
			}
		}
		return result;
	}
}

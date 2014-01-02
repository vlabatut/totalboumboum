package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v3.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v3.Agent;

/**
 * Cette classe calcule la possibilitée d'atteindre une case cible avant l'adversaire.
 * 
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
public class Concurrence extends AiCriterionBoolean<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "CONCURRENCE";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 */
	public Concurrence(Agent ai) {
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
	 * @return result la valeur du critère
	 * 
	 */
	@Override
	public Boolean processValue(AiTile tile) {
		ai.checkInterruption();

		AiTile mytile = ai.getZone().getOwnHero().getTile();

		int mydistance = ai.getCG().nonCyclicTileDistance(mytile, tile);

		for (AiHero currentEnemy : ai.getZone().getRemainingOpponents()) {
			ai.checkInterruption();
			if ( ((double) ai.getCG().nonCyclicTileDistance(currentEnemy.getTile(), tile) / currentEnemy.getWalkingSpeed()) > ((double) mydistance / ai
					.getZone().getOwnHero().getWalkingSpeed()) )
				return true;
		}
		return false;
	}
}

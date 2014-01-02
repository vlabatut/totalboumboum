package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v1.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v1.Agent;

/**
 * Cette classe permet de calculer le temps écoulé pour les agents d'atteindre la
 * case cible. 
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
     * @return result
     *              true: notre agent atteindra la case séléctionnée le premier.
     *              false: l'agent adversaire atteindra la case séléctionnée le premier.
     *
	 */
	@Override
	public Boolean processValue(AiTile tile) {
		ai.checkInterruption();

		boolean result = false;
		AiTile myTile = ai.getZone().getOwnHero().getTile();
		int myDistance = ai.getZone().getTileDistance(myTile, tile);
		double mySpeed = ai.getZone().getOwnHero().getWalkingSpeed()*(1.44);
		for (AiHero ennemy : ai.getZone().getRemainingOpponents()) {
			ai.checkInterruption();
			int ennemyDistance = ai.getZone().getTileDistance(ennemy.getTile(),
					tile);
			double ennemySpeed = ennemy.getWalkingSpeed();

			if ((double) ennemyDistance / ennemySpeed >= (double) myDistance
					/ mySpeed)
				result = true;
		}
		return result;
	}
}

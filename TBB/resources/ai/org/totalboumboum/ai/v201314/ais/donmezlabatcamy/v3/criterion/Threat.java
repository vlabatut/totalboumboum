package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v3.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v3.Agent;

/**
 * Cette classe permet de calculer la distance de Manhattan entre notre case actuelle et 
 * celle où se trouve l'agent adversaire.
 * 
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
public class Threat extends AiCriterionInteger<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "THREAT";

	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	public Threat(Agent ai) {
		super(ai, NAME, 0, 4);
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
	 * @return result la valeur de la proximitée de l'adversaire.
	 */
	@Override
	public Integer processValue(AiTile tile) {
		ai.checkInterruption();
		int result = 4;

		int distance = 0;

		for (AiHero ennemy : ai.getZone().getRemainingOpponents()) {
			ai.checkInterruption();
			for (AiTile ennemyBlast : ennemy.getBombPrototype().getBlast()) {
				ai.checkInterruption();
				distance = ai.getCG().nonCyclicTileDistance(ennemyBlast, ennemy.getTile());
				if ( tile == ennemyBlast ) {
					if ( distance == 1 || distance == 2 ) {
						result = 1;
					} else if ( distance == 0 ) {
						result = 0;
					} else if ( distance == 3 || distance == 4 ) {
						result = 2;
					} else {
						result = 3;
					}
				}
			}
		}
		return result;
	}
}

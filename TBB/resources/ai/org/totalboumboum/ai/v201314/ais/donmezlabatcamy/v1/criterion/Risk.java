package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v1.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v1.Agent;

/**
 * Cette classe permet de calculer la distance de Manhattan entre notre case
 * actuelle et celle qui contient une bombe.
 * 
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
public class Risk extends AiCriterionInteger<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "RISK";

	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	public Risk(Agent ai) {
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
     *             la valeur de la proximitée de la bombe.
	 */
	@Override
	public Integer processValue(AiTile tile) {
		ai.checkInterruption();
		int result = 3;
		int distance = 0;
		for (AiBomb bomb : ai.getZone().getBombs()) {
			ai.checkInterruption();
			for (AiTile bombBlastTile : bomb.getBlast()) {
				ai.checkInterruption();
				distance = ai.getZone().getTileDistance(bomb.getTile(), tile);

				if (tile == bombBlastTile) {
					if (distance > bomb.getRange()
							&& (distance < bomb.getRange() + 3)) {
						result = 3;
					} else if (distance >= bomb.getRange() + 3
							&& (distance < bomb.getRange() + 6)) {
						result = 2;
					}
					if (distance <= bomb.getRange() - 3) {
						result = 0;
					} else if (distance < bomb.getRange() + 3) {
						result = 1;
					}
				}
			}
		}
		return result;
	}

}

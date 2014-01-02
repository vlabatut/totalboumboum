package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v2.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v2.Agent;

/**
 * Cette classe permet de calculer le temps écoulé pour les agents d'atteindre
 * la case cible.
 * 
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
public class BombRisk extends AiCriterionBoolean<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "BOMBRISK";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 */
	public BombRisk(Agent ai) {
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
	 * @return result false si la tile est en bombBlast sinon true
	 * 
	 */
	@Override
	public Boolean processValue(AiTile tile) {
		ai.checkInterruption();

		boolean result = true;
		for (AiBomb bomb : ai.getZone().getBombs()) {
			ai.checkInterruption();
			for (AiTile bombBlastTile : bomb.getBlast()) {
				ai.checkInterruption();
				if (tile == bombBlastTile) {
					result = false;
				}
			}
		}
		return result;
	}
}

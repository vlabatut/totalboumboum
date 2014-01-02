package org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v4.Agent;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;

/**
 * Cette critere nous envoyer false, si la tile est secure(i n'y a pas
 * de bombe ou souffle de bombe). sinon, true. Donc methode est boolean
 * 
 * @author Selen Oralı
 * @author Arman Osmanoğlu
 */
public class Security extends AiCriterionBoolean<Agent> {
	/**
	 * variable de nom cette critere
	 */
	public static final String NAME = "Security";

	/**
	 * controle si un case est un danger ou pas.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	public Security(Agent ai) {
		super(ai, NAME);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	/**
	 * cette critere nous envoyer false, si la tile est secure(i n'y a pas de bombe ou souffle de bombe).
	 * @param tile notre cases dans selection des cases
	 * @return boolean true ou false
	 */
	public Boolean processValue(AiTile tile) {
		ai.checkInterruption();
		// initialisation de result
		boolean result = false;

		AiZone zone = ai.getZone();
		// loop pour toutes les bombes dans cette zone.
		for (AiBomb bomb : zone.getBombs()) {
			ai.checkInterruption();
			// loop pour les cases correspondant au souffle de cette bombe
			for (AiTile blastTile : bomb.getBlast()) {
				ai.checkInterruption();

				if (tile == blastTile) {
					result = true;// pas securé
				}
			}
		}

		return result;
	}
}

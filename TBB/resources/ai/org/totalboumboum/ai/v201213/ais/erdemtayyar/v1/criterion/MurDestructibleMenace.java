package org.totalboumboum.ai.v201213.ais.erdemtayyar.v1.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.erdemtayyar.v1.ErdemTayyar;

/**
 * Cete classe est pour montrer la distance d'ennemie pour la critere
 * concurrence
 * 
 * @author Banu Erdem
 * @author Zübeyir Tayyar
 */
public class MurDestructibleMenace extends
		AiUtilityCriterionBoolean<ErdemTayyar> {
	/** Nom de ce critère */
	public static final String NAME = "MurDestructibleMenace";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public MurDestructibleMenace(ErdemTayyar ai) throws StopRequestException {
		super(ai, NAME);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		if (this.ai.getTs().getCurrentDangerousTiles().contains(tile))
			return false;

		return true;
	}
}

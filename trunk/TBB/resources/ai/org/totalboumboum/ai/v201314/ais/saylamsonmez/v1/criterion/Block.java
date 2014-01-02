/**
 * 
 */
package org.totalboumboum.ai.v201314.ais.saylamsonmez.v1.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v1.Agent;

/**
 * Ce critère controle si ennemie se trouve dans un bloque ou pas.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */

public class Block extends AiCriterionBoolean<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "Block";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public Block(Agent ai) throws StopRequestException {
		super(ai, NAME);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		boolean result = true;
		result = ai.isBlockingEnemy(tile);
		return result;
	}
}

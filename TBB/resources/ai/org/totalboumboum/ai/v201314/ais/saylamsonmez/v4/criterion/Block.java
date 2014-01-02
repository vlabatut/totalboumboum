package org.totalboumboum.ai.v201314.ais.saylamsonmez.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v4.Agent;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v4.BlockingHandler;

/**
 * Ce critère controle si ennemie cible se trouve dans un bloque ou pas.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class Block extends AiCriterionBoolean<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "Block";
	/** pour acceder des methodes de BlockingHandler */
	BlockingHandler blockingHandler;

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Block(Agent ai) throws StopRequestException {
		super(ai, NAME);
		ai.checkInterruption();
		blockingHandler = ai.blockingHandler;
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		boolean result = true;
		blockingHandler = ai.blockingHandler;
		result = blockingHandler.isBlockingEnemy(tile);
		return result;
	}
}

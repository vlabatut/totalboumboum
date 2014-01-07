package org.totalboumboum.ai.v201314.ais.saylamsonmez.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v4.Agent;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v4.TileCalculationHandler;

/**
 * Ce critere retourne nombre de murs qui vont etre detruit si on met une bombe
 * dans cette case.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
@SuppressWarnings("deprecation")
public class NumberMur extends AiCriterionInteger<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "NumberMur";
	/** Domaine de définition */
	public static final int DISTANCE_LIMIT = 3;
	/** pour acceder aux methodes de TileCalculationHandler */
	TileCalculationHandler tileCalculationHandler;

	/**
	 * Crée un nouveau critère.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public NumberMur(Agent ai) throws StopRequestException {
		super(ai, NAME, 0, 4);
		ai.checkInterruption();
		tileCalculationHandler = ai.tileCalculationHandler;
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	@Override
	public Integer processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		tileCalculationHandler = ai.tileCalculationHandler;
		return tileCalculationHandler.getNbMurDetruitofTile(tile);

	}
}

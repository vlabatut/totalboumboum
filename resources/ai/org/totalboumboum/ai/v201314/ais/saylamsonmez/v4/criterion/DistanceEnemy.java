package org.totalboumboum.ai.v201314.ais.saylamsonmez.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v4.Agent;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v4.EnemyHandler;

/**
 * Ce critere retourne vrai ssi la distance entre nous et l'ennemie ègale à 1.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
@SuppressWarnings("deprecation")
public class DistanceEnemy extends AiCriterionBoolean<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "DistanceEnemy";
	/** pour acceder aux methodes de EnemyHandler */
	EnemyHandler enemyHandler;

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public DistanceEnemy(Agent ai) throws StopRequestException {
		super(ai, NAME);
		ai.checkInterruption();
		enemyHandler = ai.enemyHandler;
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		boolean result = true;
		int distance = 0;
		enemyHandler = ai.enemyHandler;
		AiHero enemy = enemyHandler.selectEnemy();
		if (!ai.getZone().getRemainingOpponents().isEmpty())
			distance = ai.getZone().getTileDistance(tile, enemy.getTile());

		if (distance > 1 || distance == 0)
			result = false;
		else
			result = true;
		return result;
	}
}

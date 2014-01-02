/**
 * 
 */
package org.totalboumboum.ai.v201314.ais.saylamsonmez.v3.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v3.Agent;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v3.Enemy;

/**
 * Si l'ennemi est plus proche à une case par rapport à nous, il vaut vrai;
 * sinon faux.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class Concurrence extends AiCriterionBoolean<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "Concurrence";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Concurrence(Agent ai) throws StopRequestException {
		super(ai, NAME);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		AiTile myTile = ai.getZone().getOwnHero().getTile();
		int myDistance = ai.getZone().getTileDistance(myTile, tile);
		Enemy e = new Enemy(ai);
		AiHero currentEnemy = e.selectEnemy();
		if (!ai.getZone().getRemainingOpponents().isEmpty())
			if (ai.getZone().getTileDistance(currentEnemy.getTile(), tile) < myDistance)
				return true;

		return false;
	}

}

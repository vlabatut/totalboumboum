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
 *Ce critere retourne vrai ssi la distance entre nous et l'ennemie ègale à 1.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class DistanceEnemy extends AiCriterionBoolean<Agent>
{	
	/** Nom de ce critère */
	public static final String NAME = "DistanceEnemy";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public DistanceEnemy(Agent ai) throws StopRequestException
	{	
		super(ai,NAME);      
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		boolean result = true;
		int distance = 0;
		Enemy e=new Enemy(this.ai);
		AiHero enemy = e.selectEnemy();
		if (!ai.getZone().getRemainingOpponents().isEmpty())
			distance = ai.getZone().getTileDistance(tile, enemy.getTile());

		if (distance > 1 || distance == 0)
			result = false;
		else
			result = true;
		return result;
	}
}


package org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v1.criterion;

import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v1.Agent;

/**
 * Criteria for the distance between us and the enemy
 * @author Osman Emre Derin
 * @author Oktay Koçak
 * @author Emincan Zorluoglu
 */
public class DistanceEnemy extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "DistanceEnemy";
	/** Domaine de définition  {0 et 1} */
	/**Il parle de la distance entre nous et notre ennemie.
	 * Si la distance entre notre agent
 * et l’adversaire est plus petit ou égale a 3 , 
 * il prend 0 comme valeur.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public DistanceEnemy(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile)
	{	ai.checkInterruption();
		boolean result = false;
		
		ArrayList<AiHero> heros=new ArrayList<AiHero>();
		int min_distance=50;
		AiHero closest_hero=ai.getZone().getOwnHero();
 		for(AiHero hero:heros)
		{
 			ai.checkInterruption();
			if(ai.getZone().getTileDistance(ai.getZone().getOwnHero().getTile(),hero.getTile()) <min_distance  )
			{
				min_distance=ai.getZone().getTileDistance(ai.getZone().getOwnHero().getTile(),hero.getTile());
				closest_hero=hero;
			}
		}	
 		if(ai.getZone().getTileDistance(tile,closest_hero.getTile())<=3)
 			{
 			result=true;
 			}
		
	
		return result;
	}
}

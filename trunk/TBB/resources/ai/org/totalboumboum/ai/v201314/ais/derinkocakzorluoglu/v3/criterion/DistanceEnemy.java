package org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v3.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v3.Agent;

/**
 * DistanceEnemy
 * Criteria pour calculer la distance Manhattan entre nous et l'ennemie .
 * 
 * 
 * @author Emre Derin
 * @author Oktay Koçak
 * @author Emin Can Zorluoğlu
 */
public class DistanceEnemy extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "DistanceEnemy";
	
	/** Domaine de définition  {true,false} 
	 * Il parle de la distance de Manhattan entre nous et notre ennemie.
	 * Si la distance entre notre agent
	 * et l’adversaire est plus petit ou égale a 2 , 
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
	protected Boolean processValue(AiTile tile)
	{	ai.checkInterruption();
		boolean result = true;
		int D=100;
		for(AiHero hero:ai.zone.getRemainingOpponents()){
			ai.checkInterruption();
			if(ai.zone.getTileDistance(tile,hero.getTile())<D){
				D=ai.zone.getTileDistance(tile,hero.getTile());
			}
		}
		if(D<2){
			result=false;
		}
		return result;
	}
}

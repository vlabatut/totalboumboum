package org.totalboumboum.ai.v201314.ais.enginserhantipici.v2.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v2.Agent;

/**
 *Distance between our hero and the current tile
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class Distance extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "DISTANCE";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	
	public Distance(Agent ai)
	{	super(ai,NAME,1,4);//???? 1,4
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue(AiTile tile)
	{	ai.checkInterruption();
	
		int result;
		int dist;
		AiZone zone = ai.getZone();
		AiTile currentTile = zone.getOwnHero().getTile();
		dist = zone.getTileDistance(currentTile,tile);

		if(dist == 0){
			result = 1;
		}else if(dist > 0 && dist <= 2){
			result = 2;
		}else if(dist > 2 && dist <= 5){
			result = 3;
		}else{
			result = 4;
		}
		
		return result;
	}
}

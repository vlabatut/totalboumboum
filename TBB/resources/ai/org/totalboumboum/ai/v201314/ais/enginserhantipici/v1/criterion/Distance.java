package org.totalboumboum.ai.v201314.ais.enginserhantipici.v1.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v1.Agent;

/**
 * cette classe trouve le distance entre tile et notre agent.
 
	 * pour cette critere, on calcule le distance entre tile et tile de notre agent
	 * si on est sur meme tile, result est 1
	 * sur intervalle 0 et 2 result est 2
	 * sur 2 et 5 result est 3
	 * et bien si on a distancle plus grand 5 result est 4
	 * 
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
	{	super(ai,NAME,1,4);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	
	protected Integer processValue(AiTile tile)
	
	{	ai.checkInterruption();
		int result, d;
		AiZone zone = ai.getZone();
		AiTile currentTile = zone.getOwnHero().getTile();
		d = zone.getTileDistance(currentTile, tile);
		
		if(d == 0){
			result = 1;
		}else if(d > 0 && d <= 2){
			result = 2;
		}else if(d > 2 && d <= 5){
			result = 3;
		}else{
			result = 4;
		}
	
		
		return result;
	}
}

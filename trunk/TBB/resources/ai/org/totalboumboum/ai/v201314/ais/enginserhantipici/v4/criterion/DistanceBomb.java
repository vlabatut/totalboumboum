package org.totalboumboum.ai.v201314.ais.enginserhantipici.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v4.Agent;

/**
 *	First we find the closest bomb then we calculate the distance between 
 *	our hero and this bomb
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class DistanceBomb extends AiCriterionInteger<Agent>
{	/** Name of the criterion */
	public static final String NAME = "DISTANCE_BOMB";
	
	/**
	 * Creation of a new integer criterion
	 * 
	 * @param ai
	 * 		related agent 
	 */
	public DistanceBomb(Agent ai)
	{	super(ai,NAME,1,5);
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
		AiBomb currentBomb = zone.getBombs().get(0);
		
		for (AiBomb bomb : zone.getBombs()){ // find the closest bomb to own hero
			ai.checkInterruption();
			if(zone.getTileDistance(bomb.getTile(), zone.getOwnHero().getTile()) < 
					zone.getTileDistance(currentBomb.getTile(), zone.getOwnHero().getTile())){
				currentBomb = bomb;
			}	
		}
		
		if(zone.getTileDistance(currentBomb.getTile(), zone.getOwnHero().getTile()) < 5){
		dist = zone.getTileDistance(currentBomb.getTile(),tile);
		}else{
			dist = 1; //if distance between our hero and the bomb is more than five tile, we don't wanna go there
		}
		
		if(dist == 0 || dist == 1){
			result = 1;
		}else if(dist == 2){
			result = 2;
		}else if(dist == 3 || dist == 4){
			result = 3;
		}else if(dist > 4 && dist < 7){
			result = 4;	
		}else{	
			result = 5;
		}
		
		return result;
	}
}

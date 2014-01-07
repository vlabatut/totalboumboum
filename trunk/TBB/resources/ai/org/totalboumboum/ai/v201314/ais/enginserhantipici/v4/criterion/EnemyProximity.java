package org.totalboumboum.ai.v201314.ais.enginserhantipici.v4.criterion;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v4.Agent;

/**
 * Distance between the closest enemy and our hero
 * 
 * Values are 1 to 4;
 * 	if values is "1" we are at the same tile
 * 	if values is "2" distance is one tile 
 * 	if values is "3" distance is two tiles
 * 	if values is "4" distance is more then 3 tiles
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
@SuppressWarnings("deprecation")
public class EnemyProximity extends AiCriterionInteger<Agent>
{	/** Name of the criterion */
	public static final String NAME = "ENEMY_PROXIMITY";
	
	/**
	 * Creation of a new integer criterion
	 * 
	 * @param ai
	 * 		related agent 
	 */
	public EnemyProximity(Agent ai)
	{	super(ai,NAME,1,4);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue(AiTile tile)
	{	ai.checkInterruption();
	
		int result;
		int dist = 10;
		AiZone zone = ai.getZone();
		
		List<AiHero> myEnemies = ai.getZone().getRemainingOpponents();
		myEnemies = new ArrayList<AiHero>(myEnemies);

		for(AiHero hero : myEnemies){
			ai.checkInterruption();
		int newDist = zone.getTileDistance(hero.getTile(), tile);
			if(newDist < dist){//consider the closest enemy for distance calculation
				dist = newDist;
			}
		}
		
		
		if(dist == 0){
			result = 1;
		}else if(dist == 1){
			result = 2;
		}else if(dist == 2){
			result = 3;
		}else{
			result = 4;
		}
		
		return result;
	}
}

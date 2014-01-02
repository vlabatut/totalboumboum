package org.totalboumboum.ai.v201314.ais.enginserhantipici.v3.criterion;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v3.Agent;

/**
 * This criterion calculates distance of the tile to enemy
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 * 
 * 
 */
public class DistanceToEnemy extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "DISTANCETOENEMY";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public DistanceToEnemy(Agent ai)
	{	super(ai,NAME,1,6);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue(AiTile tile)
	{	ai.checkInterruption();
	int result = 6;
	AiZone zone = ai.getZone();
	List<AiHero> enemies = new ArrayList<AiHero>(zone.getRemainingOpponents());
	List<AiTile> enemyTiles = new ArrayList<AiTile>();
	for(AiHero enemy : enemies){
		ai.checkInterruption();
		enemyTiles.add(enemy.getTile());
	}
	
	int tempResult;
	for(AiTile enemyTile : enemyTiles){
		ai.checkInterruption();
		if(zone.getTileDistance(tile, enemyTile) < 2){
			tempResult = 1;
			if(tempResult < result){
				result = tempResult;
			}
		}else
		if(zone.getTileDistance(tile, enemyTile) < 4){
			tempResult = 2;
			if(tempResult < result){
				result = tempResult;
			}
		}else
		if(zone.getTileDistance(tile, enemyTile) < 6){
			tempResult = 3;
			if(tempResult < result){
				result = tempResult;
			}
		}else
		if(zone.getTileDistance(tile, enemyTile) < 8){
			tempResult = 4;
			if(tempResult < result){
				result = tempResult;
			}
		}else
		if(zone.getTileDistance(tile, enemyTile) < 10){
			tempResult = 5;
			if(tempResult < result){
				result = tempResult;
			}
		}
	}
	
	return result;
	}
}

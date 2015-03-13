package org.totalboumboum.ai.v201314.ais.enginserhantipici.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v4.Agent;

/**
 *Distance between our hero and the current tile plus (+) distance
 * between current tile and the tile of the enemy inside the pattern
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
@SuppressWarnings("deprecation")
public class DistanceTriangle extends AiCriterionInteger<Agent>
{	/** Name of the criterion*/
	public static final String NAME = "DISTANCE_TRIANGLE";
	
	/**
	 * Creation of a new integer criterion
	 * 
	 * @param ai
	 * 		related agent 
	 */
	
	public DistanceTriangle(Agent ai)
	{	super(ai,NAME,1,5);//???? 1,4
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue(AiTile tile)
	{	ai.checkInterruption();
	
		int result;
		double dist;
		double size = AiTile.getSize();
		AiZone zone = ai.getZone();
		AiTile currentTile = zone.getOwnHero().getTile();
		double delta1[] = zone.getPixelDeltas(tile.getPosX(), tile.getPosY(), ai.getTileOfTheHeroInsideTheTrap().getPosX(),ai.getTileOfTheHeroInsideTheTrap().getPosY());
		double delta2[] = zone.getPixelDeltas(tile.getPosX(), tile.getPosY(), currentTile.getPosX(), currentTile.getPosY());
		
		double distReelEnemyToTile = Math.sqrt((delta1[0]*delta1[0]) + (delta1[1]*delta1[1]));  
		double distReelOwnHeroToTile = Math.sqrt((delta2[0]*delta2[0]) + (delta2[1]*delta2[1]));  
		
		dist = distReelOwnHeroToTile + distReelEnemyToTile;
	/*	
		System.out.println("-------------------------------");
		System.out.println("CURRENT TILE -->>" + tile);
		System.out.println("enemy to tile = " + distReelEnemyToTile + " own hero to tile = " + distReelOwnHeroToTile);
		System.out.println("Addition =" + dist);	
		System.out.println("tile size =" + AiTile.getSize());
		System.out.println("-------------------------------");
	*/	
		
		
		if(dist == 1*size){
			result = 1;
		}else if(dist == 2*size){
			result = 2;
		}else if(dist == 3*size || dist == 4*size){
			result = 3;
		}else if(dist > 4*size && dist < 8*size){
			result = 4;
		}else{
			result = 5;
		}
		
		return result;
	}
}

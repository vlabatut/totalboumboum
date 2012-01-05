
package org.totalboumboum.ai.v201112.ais.demireloz.v3.criterion;

import java.util.HashMap;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.demireloz.v3.DemirelOz;

public class IsEnemyReachable extends AiUtilityCriterionInteger
{	
	public static final String NAME = "IsEnemyReachable";
	HashMap<AiTile,Integer> hashmap = new HashMap<AiTile, Integer>();



	public IsEnemyReachable(DemirelOz ai) throws StopRequestException
	{	
		super(NAME,1,6);
		ai.checkInterruption();
		this.ai = ai;

	}


	protected DemirelOz ai;


	@Override


	public Integer processValue(AiTile tile) throws StopRequestException
	{	//The values used to make a comparison (3,5,8,13,20), are referring to a distance
		//calculated with the number of tiles.
		ai.checkInterruption();	
		int result = 5; 
		hashmap=this.ai.getHashmap();
		//If controlStraight returns "true" for a given tile, then put it in this hashmap 
		//in order to use it in the next iteration. Otherwise if the enemy moves ,
		//controlStraight will return false for the next iteration, and that we don't want. 

		if(!hashmap.isEmpty()||!hashmap.equals(null))
		{

			if(hashmap.containsKey(tile))
			{

				result=hashmap.get(tile);
				hashmap.remove(tile);
				this.ai.setHashmap(hashmap);
				return result;
			}
		}

		//Tiles in the same line with the nearest enemy without indestructible walls between.
		if(!this.ai.getZone().getRemainingOpponents().isEmpty())
		{
			AiTile nearestenemytile = this.ai.getNearestEnemy().getTile();


			if(this.ai.controlStraight(tile,nearestenemytile)!=0)
			{
				if(this.ai.controlStraight(tile,nearestenemytile)<=3)
				{


					this.ai.callHashMap(tile,3);
					return result= 3;

				}	

				if(this.ai.controlStraight(tile,nearestenemytile)<=5)
				{


					this.ai.callHashMap(tile,2);
					return result= 2;

				}	
				if(this.ai.controlStraight(tile,nearestenemytile)<=15)
				{


					this.ai.callHashMap(tile,1);
					return result= 1;

				}	


			}
			//Tiles within a range to the nearest enemy determined by the distance.
			else
			{
				if(this.ai.getClosestTileDest(nearestenemytile)!=null)
				{
					if(this.ai.getClosestTileDest(nearestenemytile).equals(tile))
					{
						return result =6;
					}
				}

			}

		}



		return result;
	}
}










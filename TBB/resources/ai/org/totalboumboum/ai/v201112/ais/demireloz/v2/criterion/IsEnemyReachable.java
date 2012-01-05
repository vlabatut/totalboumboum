
package org.totalboumboum.ai.v201112.ais.demireloz.v2.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.demireloz.v2.DemirelOz;

public class IsEnemyReachable extends AiUtilityCriterionInteger
{	
	public static final String NAME = "IsEnemyReachable";

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
		int result = 4; 
	
        
		//Tiles in the same line with the nearest enemy without indestructible walls between.
		if(!this.ai.getZone().getRemainingOpponents().isEmpty())
		{
			AiTile nearestenemytile = this.ai.getNearestEnemy().getTile();
			if(this.ai.controlStraight(tile,nearestenemytile)!=0)
			{

				if(this.ai.controlStraight(tile,nearestenemytile)<=3)
				{

					return result= 3;

				}	
				if(this.ai.controlStraight(tile,nearestenemytile)<=5)
				{

					return result= 2;

				}	
				if(this.ai.controlStraight(tile,nearestenemytile)<=13)
				{

					return result= 1;

				}	


			}
			//Tiles within a range to the nearest enemy determined by the distance.
			else
			{
				if(this.ai.getDist(nearestenemytile,tile)<=3)
				{
					return result =6;
				}
				if(this.ai.getDist(nearestenemytile,tile)<=8)
				{
					return result =5;
				}
				if(this.ai.getDist(nearestenemytile,tile)<=20)
				{
					return result =4;
				}


			}

		}


		return result;
	}
}










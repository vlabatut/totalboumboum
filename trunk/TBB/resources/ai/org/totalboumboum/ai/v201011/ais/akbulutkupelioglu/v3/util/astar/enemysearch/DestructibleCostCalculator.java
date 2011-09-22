package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.util.astar.enemysearch;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.AkbulutKupelioglu;

/**
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 */
public class DestructibleCostCalculator extends CostCalculator
{

	AkbulutKupelioglu monIa;
	
	public DestructibleCostCalculator(AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		monIa = ia;
		// 
	}
	//calculate that s**t
	public final int COST = 2;
	@Override
	public double processCost(AiTile start, AiTile end)
			throws StopRequestException
	{
		monIa.checkInterruption();
		if(!end.getBlocks().isEmpty())
		{
			if(end.getBlocks().get(0).isDestructible())
				return COST;
		}else
			return 1;
			
		return 0;
	}

	
}

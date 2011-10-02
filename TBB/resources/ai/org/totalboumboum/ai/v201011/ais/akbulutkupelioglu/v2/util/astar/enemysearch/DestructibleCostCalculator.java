package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.util.astar.enemysearch;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;

/**
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 */
@SuppressWarnings("deprecation")
public class DestructibleCostCalculator extends CostCalculator
{

	//calculate that s**t
	public final int COST = 2;
	@Override
	public double processCost(AiTile start, AiTile end)
			throws StopRequestException
	{
		if(!end.getBlocks().isEmpty())
		{
			if(end.getBlocks().get(0).isDestructible())
				return COST;
		}else
			return 1;
			
		return 0;
	}

	
}

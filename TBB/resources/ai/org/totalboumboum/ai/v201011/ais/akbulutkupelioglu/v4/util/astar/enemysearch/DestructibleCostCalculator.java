package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v4.util.astar.enemysearch;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v4.AkbulutKupelioglu;

/**
 * A cost calculator used to find a path passing through the least obstacles.
 * @author yasa
 *
 */
public class DestructibleCostCalculator extends CostCalculator
{

	AkbulutKupelioglu monIa;
	//TODO: calculate that
	/**
	 * The cost of travel over a destructible block. 
	 */
	public final int COST = 2;
	
	/**
	 * Creates a new instance.
	 * @param ia The AkbulutKupelioglu using this.
	 * @throws StopRequestException
	 */
	public DestructibleCostCalculator(AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		monIa = ia;
		// TODO Auto-generated constructor stub
	}

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

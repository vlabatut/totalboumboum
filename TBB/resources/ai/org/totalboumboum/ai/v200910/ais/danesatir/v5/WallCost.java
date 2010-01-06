package org.totalboumboum.ai.v200910.ais.danesatir.v5;

import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.CostCalculator;


public class WallCost extends CostCalculator {

	ArtificialIntelligence ai;
	
	public WallCost(ArtificialIntelligence ai) throws StopRequestException
	{	this.ai = ai;
		ai.checkInterruption();
	
	}
	
	@Override
	public double processCost(AiTile start, AiTile end) throws StopRequestException {
		ai.checkInterruption();
		List<AiBlock> blocks = end.getBlocks();
		if(!blocks.isEmpty())
			if(blocks.get(0).isDestructible())
				return 0;
		return Limits.defaultCost;
	}

}

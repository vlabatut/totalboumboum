package org.totalboumboum.ai.v200910.ais.danesatir.v4;

import java.util.List;

import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.CostCalculator;

/**
 * 
 * @version 4
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
public class WallCost extends CostCalculator {
	@Override
	public double processCost(AiTile start, AiTile end) {
		List<AiBlock> blocks = end.getBlocks();
		if(!blocks.isEmpty())
			if(blocks.get(0).isDestructible())
				return 0;
		return Limits.defaultCost;
	}

}

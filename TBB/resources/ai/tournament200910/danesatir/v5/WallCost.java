package tournament200910.danesatir.v5;

import java.util.List;

import org.totalboumboum.ai.adapter200910.data.AiBlock;
import org.totalboumboum.ai.adapter200910.data.AiTile;
import org.totalboumboum.ai.adapter200910.path.astar.cost.CostCalculator;


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

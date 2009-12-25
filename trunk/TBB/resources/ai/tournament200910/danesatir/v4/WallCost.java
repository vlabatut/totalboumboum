package tournament200910.danesatir.v4;

import java.util.List;

import fr.free.totalboumboum.ai.adapter200910.data.AiBlock;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.path.astar.cost.CostCalculator;

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

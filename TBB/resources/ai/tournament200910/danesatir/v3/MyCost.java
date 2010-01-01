package tournament200910.danesatir.v3;

import org.totalboumboum.ai.adapter200910.data.AiTile;
import org.totalboumboum.ai.adapter200910.path.astar.cost.CostCalculator;

public class MyCost extends CostCalculator {

	@Override
	public double processCost(AiTile start, AiTile end) {
		double cost=Limits.defaultCost;
		if(end.getItems().size()>0)
			cost-=5;
		return cost;
	}

}

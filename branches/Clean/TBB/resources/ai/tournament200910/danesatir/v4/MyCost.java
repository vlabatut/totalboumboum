package tournament200910.danesatir.v4;

import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.path.astar.cost.CostCalculator;

public class MyCost extends CostCalculator {
	private TimeMatrice time;
	MyCost(TimeMatrice time) {
		this.time = time;
	}
	@Override
	public double processCost(AiTile start, AiTile end) {
		double cost=Limits.defaultCost;
		if(end.getItems().size()>0)
			cost-=Limits.defaultCost*Limits.bonusRate;
		if(this.time.getTime(end)!=0)
			cost+=Limits.defaultCost*Limits.fireRate;
		return cost;
	}

}

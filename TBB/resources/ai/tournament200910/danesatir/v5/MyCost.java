package tournament200910.danesatir.v5;

import org.totalboumboum.ai.adapter200910.data.AiTile;
import org.totalboumboum.ai.adapter200910.path.astar.cost.CostCalculator;

public class MyCost extends CostCalculator {
	private TimeMatrice time;
	MyCost(TimeMatrice time) {
		this.time = time;
	}
	/**
	 * Calculate cost with priority Bonus and depriority Fire.
	 */
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

package org.totalboumboum.ai.v200910.ais.danesatir.v4;

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

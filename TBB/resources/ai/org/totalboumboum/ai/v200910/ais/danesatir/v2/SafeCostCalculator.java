package org.totalboumboum.ai.v200910.ais.danesatir.v2;

import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.CostCalculator;

/**
 * 
 * @version 2
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
public class SafeCostCalculator extends CostCalculator {

	TimeMatrice matrice = null;
	DaneSatir ai = null;
	public SafeCostCalculator(DaneSatir ai, TimeMatrice matrice) {
		this.ai=ai;
		this.matrice=matrice;
	}

	@Override
	public double processCost(AiTile start, AiTile end) {
		AiTile target = Limits.checkTarget ? start : end;
		if (this.matrice.getTime(target) < Limits.dangerLimit)
			return Double.MAX_VALUE;
		double danger = (Limits.expandBombTime-this.matrice.getTime(target))*Limits.dangerFactor;
		double distance = 1 * Limits.distanceFactor;
		//return 1;
		return danger+distance;
	}
	
	/*public double processCost(AiPath path) {
		return 0;
	}*/

}

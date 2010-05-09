package tournament200910.danesatir.v2;

import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
//import fr.free.totalboumboum.ai.adapter200910.path.AiPath;
import fr.free.totalboumboum.ai.adapter200910.path.astar.cost.CostCalculator;

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

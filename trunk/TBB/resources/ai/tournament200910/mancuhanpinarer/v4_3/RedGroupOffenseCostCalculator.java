package tournament200910.mancuhanpinarer.v4_3;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.adapter200910.data.AiBomb;
import org.totalboumboum.ai.adapter200910.data.AiTile;
import org.totalboumboum.ai.adapter200910.data.AiZone;
import org.totalboumboum.ai.adapter200910.path.astar.cost.CostCalculator;


public class RedGroupOffenseCostCalculator extends CostCalculator {

	@Override
	public double processCost(AiTile start, AiTile end) {
		// TODO Auto-generated method stub
		double result=1;
		AiZone gameZone=end.getZone();
		List<AiBomb> bombList=gameZone.getBombs();
		Iterator<AiBomb> bombIterator=bombList.iterator();
		while(bombIterator.hasNext()){
			AiBomb bomb=bombIterator.next();
			List<AiTile> effectedTiles=bomb.getBlast();
			if(effectedTiles.contains(end))
				result=1000;
		}
		return result;
	}

}

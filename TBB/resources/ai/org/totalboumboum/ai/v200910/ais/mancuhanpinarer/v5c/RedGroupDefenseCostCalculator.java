package org.totalboumboum.ai.v200910.ais.mancuhanpinarer.v5c;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.CostCalculator;

/**
 * @author Koray Mançuhan
 * @author Özgün Pınarer
 */
@SuppressWarnings("deprecation")
public class RedGroupDefenseCostCalculator extends CostCalculator {

	/** */
	ArtificialIntelligence ai;
	
	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	public RedGroupDefenseCostCalculator(ArtificialIntelligence ai) throws StopRequestException
	{	ai.checkInterruption();
		this.ai = ai;
		
	}
	
	@Override
	public double processCost(AiTile start, AiTile end) throws StopRequestException {
		ai.checkInterruption();
		
		double result=1;
		AiZone gameZone=end.getZone();
		List<AiBomb> bombList=gameZone.getBombs();
		Iterator<AiBomb> bombIterator=bombList.iterator();
		while(bombIterator.hasNext()){
			ai.checkInterruption();
			AiBomb bomb=bombIterator.next();
			List<AiTile> effectedTiles=bomb.getBlast();
			if(effectedTiles.contains(end))
				result=2;
		}
		return result;
	}

}

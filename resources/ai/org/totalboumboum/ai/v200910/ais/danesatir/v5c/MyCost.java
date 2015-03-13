package org.totalboumboum.ai.v200910.ais.danesatir.v5c;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.CostCalculator;

/**
 * 
 * @version 5.c
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
@SuppressWarnings("deprecation")
public class MyCost extends CostCalculator {
	/** */
	private TimeMatrice time;
	/** */
	ArtificialIntelligence ai;
	
	/**
	 * 
	 * @param time
	 * 		Description manquante !
	 * @param ai
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	MyCost(TimeMatrice time, ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;
		this.time = time;
	}
	/**
	 * Calculate cost with priority Bonus and depriority Fire.
	 * 		Description manquante !
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	@Override
	public double processCost(AiTile start, AiTile end) throws StopRequestException {
		ai.checkInterruption();
		double cost=Limits.defaultCost;
		if(end.getItems().size()>0)
			cost-=Limits.defaultCost*Limits.bonusRate;
		if(this.time.getTime(end)!=0)
			cost+=Limits.defaultCost*Limits.fireRate;
		return cost;
	}

}

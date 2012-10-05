package org.totalboumboum.ai.v200809.ais.gunalpyurtsever.v2c;

import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
 * 
 * @author Ozan Günalp
 * @author Sinan Yürtsever
 *
 */
@SuppressWarnings("deprecation")
public class DangerPriorityCostTile {
	
	CostTile costTile;
	ArtificialIntelligence ai;
	int priority;
	/**
	 * 
	 * @param costTile
	 * @param priority
	 * @param ai
	 * @throws StopRequestException
	 */
	public DangerPriorityCostTile(CostTile costTile, int priority,ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;
		this.costTile = costTile;
		this.priority = priority;
	}
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public CostTile getCostTile() throws StopRequestException {
		ai.checkInterruption();
		return costTile;
	}
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public int getPriority() throws StopRequestException {
		ai.checkInterruption();
		return priority;
	}
	
	
	
	
}

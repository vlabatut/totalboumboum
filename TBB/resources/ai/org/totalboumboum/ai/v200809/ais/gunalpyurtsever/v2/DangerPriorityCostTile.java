package org.totalboumboum.ai.v200809.ais.gunalpyurtsever.v2;

/**
 * 
 * @author Ozan Günalp
 * @author Sinan Yürtsever
 *
 */
public class DangerPriorityCostTile {
	
	/** */
	CostTile costTile;
	/** */
	int priority;
	
	/**
	 * 
	 * @param costTile
	 * @param priority
	 */
	public DangerPriorityCostTile(CostTile costTile, int priority) {
		this.costTile = costTile;
		this.priority = priority;
	}
	/**
	 * 
	 * @return
	 * 		?
	 */
	public CostTile getCostTile() {
		return costTile;
	}
	/**
	 * 
	 * @return
	 * 		?
	 */
	public int getPriority() {
		return priority;
	}
	
	
	
	
}

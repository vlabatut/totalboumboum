package org.totalboumboum.ai.v200809.ais.gunalpyurtsever.v2;

public class DangerPriorityCostTile {
	
	CostTile costTile;
	int priority;
	public DangerPriorityCostTile(CostTile costTile, int priority) {
		this.costTile = costTile;
		this.priority = priority;
	}
	public CostTile getCostTile() {
		return costTile;
	}
	public int getPriority() {
		return priority;
	}
	
	
	
	
}
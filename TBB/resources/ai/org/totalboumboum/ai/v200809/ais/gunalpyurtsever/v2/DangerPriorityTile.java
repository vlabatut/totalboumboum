package org.totalboumboum.ai.v200809.ais.gunalpyurtsever.v2;

import org.totalboumboum.ai.v200809.adapter.AiTile;

/**
 * 
 * @author Ozan Günalp
 * @author Sinan Yürtsever
 *
 */
public class DangerPriorityTile{

	AiTile tile;
	int dangerpriority;
	public DangerPriorityTile(AiTile tile, int dangerpriority) {
		
		this.tile = tile;
		this.dangerpriority = dangerpriority;
	}
	public AiTile getTile() {
		return tile;
	}


	public int getDangerpriority() {
		return dangerpriority;
	}
	public void setDangerpriority(int dangerpriority) {
		this.dangerpriority = dangerpriority;
	}
	
}

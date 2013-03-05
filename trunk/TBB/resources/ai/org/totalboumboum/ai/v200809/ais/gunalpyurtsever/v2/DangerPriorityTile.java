package org.totalboumboum.ai.v200809.ais.gunalpyurtsever.v2;

import org.totalboumboum.ai.v200809.adapter.AiTile;

/**
 * 
 * @author Ozan Günalp
 * @author Sinan Yürtsever
 *
 */
@SuppressWarnings("deprecation")
public class DangerPriorityTile{

	/** */
	AiTile tile;
	/** */
	int dangerpriority;
	/**
	 * 
	 * @param tile
	 * 		Description manquante !
	 * @param dangerpriority
	 * 		Description manquante !
	 */
	public DangerPriorityTile(AiTile tile, int dangerpriority) {
		
		this.tile = tile;
		this.dangerpriority = dangerpriority;
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public AiTile getTile() {
		return tile;
	}

	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public int getDangerpriority() {
		return dangerpriority;
	}
	/**
	 * 
	 * @param dangerpriority
	 * 		Description manquante !
	 */
	public void setDangerpriority(int dangerpriority) {
		this.dangerpriority = dangerpriority;
	}
	
}

package org.totalboumboum.ai.v200910.ais.danesatir.v5;

import org.totalboumboum.ai.v200910.adapter.data.AiTile;

/**
 * AiTile implementation for use with BFS
 * 
 * @version 5
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
@SuppressWarnings("deprecation")
public class MonTile {
	/** */
	private AiTile tile;
	/** */
	private int iter;

	/**
	 * 
	 * @param tile
	 * 		Description manquante !
	 * @param iter
	 * 		Description manquante !
	 */
	MonTile(AiTile tile, int iter) {
		this.setTile(tile);
		this.setIter(iter);
	}
	
	/**
	 * 
	 * @param tile
	 * 		Description manquante !
	 */
	private void setTile(AiTile tile) {
		this.tile = tile;
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
	 * @param iter
	 * 		Description manquante !
	 */
	private void setIter(int iter) {
		this.iter = iter;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public int getIter() {
		return iter;
	}
	
}

package org.totalboumboum.ai.v200910.ais.danesatir.v3;

import org.totalboumboum.ai.v200910.adapter.data.AiTile;

/**
 * 
 * @version 3
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
public class MonTile {
	private AiTile tile;
	private int iter;
	MonTile(AiTile tile, int iter) {
		this.setTile(tile);
		this.setIter(iter);
	}
	private void setTile(AiTile tile) {
		this.tile = tile;
	}
	public AiTile getTile() {
		return tile;
	}
	private void setIter(int iter) {
		this.iter = iter;
	}
	public int getIter() {
		return iter;
	}
	
}

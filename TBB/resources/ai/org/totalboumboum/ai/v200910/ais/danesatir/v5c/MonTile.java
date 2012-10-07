package org.totalboumboum.ai.v200910.ais.danesatir.v5c;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;

/**
 * AiTile implementation for use with BFS
 * 
 * @version 5.c
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
	/** */
	ArtificialIntelligence ai;
	
	/**
	 * 
	 * @param tile
	 * @param iter
	 * @param ai
	 * @throws StopRequestException
	 */
	MonTile(AiTile tile, int iter, ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;
		this.setTile(tile);
		this.setIter(iter);
	}
	
	/**
	 * 
	 * @param tile
	 * @throws StopRequestException
	 */
	private void setTile(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		this.tile = tile;
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public AiTile getTile() throws StopRequestException {
		ai.checkInterruption();
		return tile;
	}
	
	/**
	 * 
	 * @param iter
	 * @throws StopRequestException
	 */
	private void setIter(int iter) throws StopRequestException {
		ai.checkInterruption();
		this.iter = iter;
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public int getIter() throws StopRequestException {
		ai.checkInterruption();
		return iter;
	}
	
}

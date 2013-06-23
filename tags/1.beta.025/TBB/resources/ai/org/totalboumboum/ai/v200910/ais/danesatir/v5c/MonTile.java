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
	 * 		Description manquante !
	 * @param iter
	 * 		Description manquante !
	 * @param ai
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
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
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void setTile(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		this.tile = tile;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiTile getTile() throws StopRequestException {
		ai.checkInterruption();
		return tile;
	}
	
	/**
	 * 
	 * @param iter
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void setIter(int iter) throws StopRequestException {
		ai.checkInterruption();
		this.iter = iter;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public int getIter() throws StopRequestException {
		ai.checkInterruption();
		return iter;
	}
	
}

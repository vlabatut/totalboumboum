package org.totalboumboum.ai.v200809.ais.gunalpyurtsever.v2c;

import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

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
	/** */
	ArtificialIntelligence ai;
	
	/**
	 * 
	 * @param tile
	 * @param dangerpriority
	 * @param ai
	 * @throws StopRequestException
	 */
	public DangerPriorityTile(AiTile tile, int dangerpriority,ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();		
		this.ai = ai;
		this.tile = tile;
		this.dangerpriority = dangerpriority;
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
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public int getDangerpriority() throws StopRequestException {
		ai.checkInterruption();		
		return dangerpriority;
	}
	/**
	 * 
	 * @param dangerpriority
	 * @throws StopRequestException
	 */
	public void setDangerpriority(int dangerpriority) throws StopRequestException {
		ai.checkInterruption();		
		this.dangerpriority = dangerpriority;
	}
	
}

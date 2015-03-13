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
	 * 		Description manquante !
	 * @param dangerpriority
	 * 		Description manquante !
	 * @param ai
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
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
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public int getDangerpriority() throws StopRequestException {
		ai.checkInterruption();		
		return dangerpriority;
	}
	/**
	 * 
	 * @param dangerpriority
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void setDangerpriority(int dangerpriority) throws StopRequestException {
		ai.checkInterruption();		
		this.dangerpriority = dangerpriority;
	}
	
}

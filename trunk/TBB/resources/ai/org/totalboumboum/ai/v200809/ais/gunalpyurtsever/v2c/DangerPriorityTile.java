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
public class DangerPriorityTile{

	AiTile tile;
	int dangerpriority;
	ArtificialIntelligence ai;
	public DangerPriorityTile(AiTile tile, int dangerpriority,ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();		
		this.ai = ai;
		this.tile = tile;
		this.dangerpriority = dangerpriority;
	}
	public AiTile getTile() throws StopRequestException {
		ai.checkInterruption();		
		return tile;
	}


	public int getDangerpriority() throws StopRequestException {
		ai.checkInterruption();		
		return dangerpriority;
	}
	public void setDangerpriority(int dangerpriority) throws StopRequestException {
		ai.checkInterruption();		
		this.dangerpriority = dangerpriority;
	}
	
}

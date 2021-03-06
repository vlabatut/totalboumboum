package org.totalboumboum.ai.v200809.ais.gunalpyurtsever.v2c;

import java.util.Comparator;

import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
 * 
 * @author Ozan Günalp
 * @author Sinan Yürtsever
 *
 */
@SuppressWarnings("deprecation")
public class DangerPriorityCostTileComparator implements Comparator<DangerPriorityCostTile> {

	/** */
	private int col;
	/** */
	private int line;
	
	/**
	 * 
	 * @param col
	 * 		Description manquante !
	 * @param line
	 * 		Description manquante !
	 * @param ai
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public DangerPriorityCostTileComparator(int col, int line, ArtificialIntelligence  ai) throws StopRequestException {
		ai.checkInterruption();
		this.col = col;
		this.line = line;
	}

	@Override
	public int compare(DangerPriorityCostTile tile1, DangerPriorityCostTile tile2){
		
		
		int i=0;
		try {
			i = tile1.getPriority() - tile2.getPriority();
			if(i==0){
				i = tile1.getCostTile().getCost() - tile2.getCostTile().getCost();
					if(i==0){
						i = tile1.getCostTile().getHeuristic() - tile2.getCostTile().getHeuristic();
						if(i==0)
							i = Math.abs(tile1.getCostTile().getAiTile().getCol() - (((int)(col/2))+1)) + Math.abs(tile1.getCostTile().getAiTile().getLine() - (((int)(line/2))+1)) - 
								Math.abs(tile2.getCostTile().getAiTile().getCol() - (((int)(col/2))+1)) + Math.abs(tile2.getCostTile().getAiTile().getLine() - (((int)(line/2))+1));
					}
				}
		} catch (StopRequestException e) {
			// 
			//e.printStackTrace();
			throw new RuntimeException();
		}
		
		return i;
	}
	
}

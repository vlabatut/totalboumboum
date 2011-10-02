package org.totalboumboum.ai.v200809.ais.gunalpyurtsever.v2;

import java.util.Comparator;

/**
 * 
 * @author Ozan Günalp
 * @author Sinan Yürtsever
 *
 */
@SuppressWarnings("deprecation")
public class DangerPriorityCostTileComparator implements Comparator<DangerPriorityCostTile> {

	private int col;
	private int line;
	
	

	public DangerPriorityCostTileComparator(int col, int line) {
		super();
		this.col = col;
		this.line = line;
	}



	public int compare(DangerPriorityCostTile tile1, DangerPriorityCostTile tile2){
		
		
		int i =  tile1.getPriority() - tile2.getPriority();
		if(i==0){
		i = tile1.getCostTile().getCost() - tile2.getCostTile().getCost();
			if(i==0){
				i = tile1.getCostTile().getHeuristic() - tile2.getCostTile().getHeuristic();
				if(i==0)
					i = Math.abs(tile1.getCostTile().getAiTile().getCol() - (((int)(col/2))+1)) + Math.abs(tile1.getCostTile().getAiTile().getLine() - (((int)(line/2))+1)) - 
						Math.abs(tile2.getCostTile().getAiTile().getCol() - (((int)(col/2))+1)) + Math.abs(tile2.getCostTile().getAiTile().getLine() - (((int)(line/2))+1));
			}
		}
		return i;
	}
	
}

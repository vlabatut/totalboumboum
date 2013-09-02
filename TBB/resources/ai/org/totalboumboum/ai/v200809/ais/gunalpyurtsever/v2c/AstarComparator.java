package org.totalboumboum.ai.v200809.ais.gunalpyurtsever.v2c;

import java.util.Comparator;

import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
 * 
 * @author Ozan Günalp
 * @author Sinan Yürtsever
 *
 */
@SuppressWarnings("deprecation")
public class AstarComparator implements Comparator<CostTile> {
	
	@Override
	public int compare(CostTile tile1, CostTile tile2){
		
		int i=0;
		try {
			i = tile1.getCost() + tile1.getHeuristic() - tile2.getCost() - tile2.getHeuristic();
			if(i == 0)
				i =tile1.toString().compareTo(tile2.toString());
		} catch (StopRequestException e) {
			// 
			//e.printStackTrace();
		}
		
		
		return i;
	}

}

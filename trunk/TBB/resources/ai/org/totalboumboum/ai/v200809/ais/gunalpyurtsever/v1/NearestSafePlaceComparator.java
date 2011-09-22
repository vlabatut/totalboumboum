package org.totalboumboum.ai.v200809.ais.gunalpyurtsever.v1;

import java.util.Comparator;

/**
 * 
 * @author Ozan Günalp
 * @author Sinan Yürtsever
 *
 */
public class NearestSafePlaceComparator implements Comparator<CostTile> {

	public int compare(CostTile tile1, CostTile tile2){
		
		int i =  tile1.getCost() - tile2.getCost();
		if(i==0){
		i = tile1.getHeuristic() - tile2.getHeuristic();
			if(i==0)
			i =tile1.toString().compareTo(tile2.toString());
		}
		return i;
	}
	
}

package tournament200809.gunalpyurtsever;

import java.util.Comparator;

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

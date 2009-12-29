package tournament200809.gunalpyurtsever.v1;

import java.util.Comparator;

public class AstarComparator implements Comparator<CostTile> {
	
	public int compare(CostTile tile1, CostTile tile2){
		
		int i = tile1.getCost() + tile1.getHeuristic() - tile2.getCost() - tile2.getHeuristic() ;
		
		if(i == 0)
			i =tile1.toString().compareTo(tile2.toString());
		
		return i;
	}

}

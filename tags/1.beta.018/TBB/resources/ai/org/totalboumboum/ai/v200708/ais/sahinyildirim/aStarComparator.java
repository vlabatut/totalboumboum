package org.totalboumboum.ai.v200708.ais.sahinyildirim;

import java.util.Comparator;

/**
 * 
 * @author Serkan Sahin
 * @author Mehmet Yildirim
 *
 */
public class aStarComparator implements Comparator<PointFind>
{
	public int compare(PointFind point1, PointFind point2) 
	{
		int result = -1;
		
		if(point1.getCost() >= point2.getCost())
			result = 1;
	else if(point1.getCost() < point2.getCost())
			result = -1;
		
		return result;
		
	
		
	
	}

	
	
	
}

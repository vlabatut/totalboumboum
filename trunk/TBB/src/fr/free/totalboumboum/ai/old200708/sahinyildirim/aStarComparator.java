package fr.free.totalboumboum.ai.old200708.sahinyildirim;

import java.util.Comparator;

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

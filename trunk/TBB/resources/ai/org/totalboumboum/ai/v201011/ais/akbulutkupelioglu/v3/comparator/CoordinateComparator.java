package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.comparator;

import java.util.Comparator;
import java.util.HashMap;

import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.util.Coordinate;

public class CoordinateComparator implements Comparator<Coordinate>{

	
	private HashMap<Coordinate, Integer> distanceMap = null;
	
	public CoordinateComparator()
	{
		// TODO Auto-generated constructor stub
	}

	public CoordinateComparator(HashMap<Coordinate, Integer> dm)
	{
		distanceMap = dm;
	}
	
	@Override
	public int compare(Coordinate o1, Coordinate o2)
	{
		if(distanceMap==null)
			return o1.value-o2.value;
		else
		{
			if(o1.value==o2.value)
				return distanceMap.get(o1)-distanceMap.get(o2);
			else
				return o1.value-o2.value;
		}
	}



}

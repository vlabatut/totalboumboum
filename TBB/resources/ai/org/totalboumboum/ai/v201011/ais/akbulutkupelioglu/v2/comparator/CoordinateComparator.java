package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.comparator;

import java.util.Comparator;

import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.util.Coordinate;

/**
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 */
public class CoordinateComparator implements Comparator<Coordinate>{

	@Override
	public int compare(Coordinate o1, Coordinate o2)
	{
		return o1.value-o2.value;
	}



}

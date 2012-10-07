package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6.comparator;

import java.util.Comparator;
import java.util.HashMap;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6.AkbulutKupelioglu;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6.util.Coordinate;

/**
 * A comparator used to compare coordinates. It works in two modes.
 * If a HashMap containing the distances to a certain tile is supplied,
 * the comparison is made according to those distances.
 * Else, the comparison is based solely on the values of coordinates.
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 *
 */
@SuppressWarnings("deprecation")
public class CoordinateComparator implements Comparator<Coordinate>{

	/** */
	private HashMap<Coordinate, Integer> distanceMap = null;
	
	/**
	 * The constructor initializing the distance map using a HashMap containing
	 * the distance values to corresponding coordinates.
	 * @param ia 
	 * @param dm
	 * @throws StopRequestException 
	 */
	public CoordinateComparator(AkbulutKupelioglu ia,HashMap<Coordinate, Integer> dm) throws StopRequestException
	{
		ia.checkInterruption();
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

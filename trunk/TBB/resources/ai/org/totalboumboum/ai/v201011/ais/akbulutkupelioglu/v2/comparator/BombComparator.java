package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.comparator;

import java.util.Comparator;

import org.totalboumboum.ai.v201011.adapter.data.AiBomb;

/**
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 */
@SuppressWarnings("deprecation")
public class BombComparator implements Comparator<AiBomb>
{

	@Override
	public int compare(AiBomb o1, AiBomb o2)
	{
		double timeRemaining1 = o1.getExplosionDuration() - o1.getTime();
		double timeRemaining2 = o2.getExplosionDuration() - o2.getTime();
		if(timeRemaining1<timeRemaining2)
			return -1;
		if(timeRemaining2<timeRemaining1)
			return 1;
		return 0;
	}

}

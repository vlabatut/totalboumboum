package org.totalboumboum.ai.v200910.ais.danesatir.v2;

import java.util.Comparator;

import org.totalboumboum.ai.v200910.adapter.data.AiBomb;

/**
 * 
 * @version 2
 * 
 * @author Levent Dane
 * @author Tolga Can Satir
 *
 */
public class BombComparator implements Comparator<AiBomb> {

	@Override
	public int compare(AiBomb arg0, AiBomb arg1) {
		double arg0time = GeneralFuncs.getTimeToExplode(arg0);
		double arg1time = GeneralFuncs.getTimeToExplode(arg1);
		if(arg0time < arg1time)
			return -1;
		else if (arg0time == arg1time)
			return 0;
		return 1;
	}

}

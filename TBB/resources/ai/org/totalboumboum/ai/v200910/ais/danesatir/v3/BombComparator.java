package org.totalboumboum.ai.v200910.ais.danesatir.v3;

import java.util.Comparator;

import org.totalboumboum.ai.v200910.adapter.data.AiBomb;

/**
 * 
 * @version 3
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
@SuppressWarnings("deprecation")
public class BombComparator implements Comparator<AiBomb> {

	@Override
	public int compare(AiBomb arg0, AiBomb arg1) {
		double arg0time = TimeMatrice.getTimeToExplode(arg0);
		double arg1time = TimeMatrice.getTimeToExplode(arg1);
		if(arg0time < arg1time)
			return -1;
		else if (arg0time == arg1time)
			return 0;
		return 1;
	}

}

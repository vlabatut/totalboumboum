package tournament200910.danesatir.v5;

import java.util.Comparator;

import org.totalboumboum.ai.adapter200910.data.AiBomb;


public class BombComparator implements Comparator<AiBomb> {

	/**
	 * Compare two bombs with ETA to explosion
	 * @return default comparator values
	 * @see AiBomb
	 */
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

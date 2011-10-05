package tournament200910.danesatir.v1;

import java.util.Comparator;

import fr.free.totalboumboum.ai.adapter200910.data.AiBomb;

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
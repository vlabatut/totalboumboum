package org.totalboumboum.ai.v200910.ais.danesatir.v5;

import java.util.Comparator;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;


public class BombComparator implements Comparator<AiBomb> {

	ArtificialIntelligence ai;
	
	public BombComparator(ArtificialIntelligence ai) throws StopRequestException
	{	ai.checkInterruption();
		this.ai = ai;		
	}
	
	/**
	 * Compare two bombs with ETA to explosion
	 * @return default comparator values
	 * @see AiBomb
	 */
	@Override
	public int compare(AiBomb arg0, AiBomb arg1) {
		double arg0time=0;
		try {
			arg0time = TimeMatrice.getTimeToExplode(arg0,ai);
		} catch (StopRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double arg1time=0;
		try {
			arg1time = TimeMatrice.getTimeToExplode(arg1,ai);
		} catch (StopRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(arg0time < arg1time)
			return -1;
		else if (arg0time == arg1time)
			return 0;
		return 1;
	}

}

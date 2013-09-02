package org.totalboumboum.ai.v200910.ais.danesatir.v5c;

import java.util.Comparator;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;

/**
 * 
 * @version 5.c
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
@SuppressWarnings("deprecation")
public class BombComparator implements Comparator<AiBomb> {

	/** */
	ArtificialIntelligence ai;
	
	/**
	 * 
	 * @param ai
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public BombComparator(ArtificialIntelligence ai) throws StopRequestException
	{	ai.checkInterruption();
		this.ai = ai;		
	}
	
	/**
	 * Compare two bombs with ETA to explosion
	 * @return default comparator values
	 * @see AiBomb
	 * 		Description manquante !
	 */
	@Override
	public int compare(AiBomb arg0, AiBomb arg1) {
		double arg0time=0;
		try {
			arg0time = TimeMatrice.getTimeToExplode(arg0,ai);
		} catch (StopRequestException e) {
			// 
			//e.printStackTrace();
		}
		double arg1time=0;
		try {
			arg1time = TimeMatrice.getTimeToExplode(arg1,ai);
		} catch (StopRequestException e) {
			// 
			//e.printStackTrace();
		}
		if(arg0time < arg1time)
			return -1;
		else if (arg0time == arg1time)
			return 0;
		return 1;
	}

}

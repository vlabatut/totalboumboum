package org.totalboumboum.ai.v200809.ais.tirtiltomruk.v2c.zone;

import java.util.Comparator;

import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
*
* @author Abdullah Tırtıl
* @author Mert Tomruk
*
*/
@SuppressWarnings("deprecation")
public class TimedBombComparator implements Comparator<TimedBomb>{

	@Override
	/*
	 * retourne une valeur negative si le premier bombe va exploser plus vite
	 * */
	public int compare(TimedBomb arg0, TimedBomb arg1) {
		try {
			if(arg0.getRemainingTime() == -1)
			{
				if(arg1.getRemainingTime() == -1)
					return (int) (arg0.getReleaseTime() - arg1.getReleaseTime());
				else
					return -1;
			}
			else
			{
				if(arg1.getRemainingTime() == -1)
					return 1;
				else
					return (int) (arg0.getRemainingTime() - arg1.getRemainingTime());
			}
		} catch (StopRequestException e) {
			// 
			e.printStackTrace();
		}
		return 0;
	}

}

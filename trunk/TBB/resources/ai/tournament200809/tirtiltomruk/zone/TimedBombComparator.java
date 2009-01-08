package tournament200809.tirtiltomruk.zone;

import java.util.Comparator;

public class TimedBombComparator implements Comparator<TimedBomb>{

	@Override
	/*
	 * retourne une valeur negative si le premier bombe va exploser plus vite
	 * */
	public int compare(TimedBomb arg0, TimedBomb arg1) {
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
	}

}

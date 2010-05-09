package fr.free.totalboumboum.engine.log.logstats;

public class Logstats
{
	/////////////////////////////////////////////////////////////////
	// UPDATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static long launchTime = System.currentTimeMillis();
	
	public static void update(boolean quicklaunch)
	{	long currentTime = System.currentTimeMillis();
		long elapsedTime = (currentTime - launchTime)/1000;
		if(quicklaunch)
		{	quickLaunchCount++;
			quickLaunchTime = quickLaunchTime + elapsedTime;
		}
		else // regular launch
		{	regularLaunchCount++;
			regularLaunchTime = regularLaunchTime + elapsedTime;
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// QUICK LAUNCH		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static long quickLaunchCount;
	private static long quickLaunchTime;
	
	public static long getQuickLaunchCount()
	{	return quickLaunchCount;
	}
	public static void setQuickLaunchCount(long quickLaunchCount)
	{	Logstats.quickLaunchCount = quickLaunchCount;
	}
	
	public static long getQuickLaunchTime()
	{	return quickLaunchTime;
	}
	public static void setQuickLaunchTime(long quickLaunchTime)
	{	Logstats.quickLaunchTime = quickLaunchTime;
	}
	
	/////////////////////////////////////////////////////////////////
	// REGULAR LAUNCH	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static long regularLaunchCount;
	private static long regularLaunchTime;

	public static long getRegularLaunchCount()
	{	return regularLaunchCount;
	}
	public static void setRegularLaunchCount(long regularLaunchCount)
	{	Logstats.regularLaunchCount = regularLaunchCount;
	}
	
	public static long getRegularLaunchTime()
	{	return regularLaunchTime;
	}
	public static void setRegularLaunchTime(long regularLaunchTime)
	{	Logstats.regularLaunchTime = regularLaunchTime;
	}
}

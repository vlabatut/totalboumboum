package fr.free.totalboumboum.data.configuration.engine;

public class EngineConfiguration
{
	/////////////////////////////////////////////////////////////////
	// TIMING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int fps;
	private long milliPeriod;
	private long nanoPeriod;
	//NOTE speedcoeff à descendre au niveau de loop, car il peut dépendre du level
	private double speedCoeff;

	public void setFps(int fps)
	{	this.fps = fps;
		milliPeriod = (long) 1000.0 / fps;
		nanoPeriod = milliPeriod * 1000000L;
	}
	public int getFps()
	{	return fps;
	}
	public long getMilliPeriod()
	{	return milliPeriod;
	}
	public long getNanoPeriod()
	{	return nanoPeriod;
	}

	public double getSpeedCoeff()
	{	return speedCoeff;
	}
	public void setSpeedCoeff(double speedCoeff)
	{	this.speedCoeff = speedCoeff;
	}
}

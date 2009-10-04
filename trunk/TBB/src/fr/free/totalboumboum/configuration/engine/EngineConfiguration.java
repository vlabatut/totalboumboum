package fr.free.totalboumboum.configuration.engine;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

public class EngineConfiguration
{

	public EngineConfiguration copy()
	{	EngineConfiguration result = new EngineConfiguration();
		result.setAutoFps(autoFps);
		result.setSpeedCoeff(speedCoeff);
		result.setFps(fps); 
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TIMING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean autoFps;
	/** frames per second */
	private int fps;
	/** engine update persiod in milliseconds */
	private long milliPeriod;
	/** engine update persiod in nanoseconds */
	private long nanoPeriod;
	/** speed coefficient */
	private double speedCoeff; //NOTE speedcoeff à descendre au niveau de loop, car il peut dépendre du level
	/** ai updates per second */
	private int aiUps = 10;
	/** ai update period */
	private long aiPeriod = (long)(1000.0/aiUps);

	public void setAutoFps(boolean autoFps)
	{	this.autoFps = autoFps;		
	}
	public boolean getAutoFps()
	{	return autoFps;		
	}
	
	public void setFps(int fps)
	{	this.fps = fps;
		milliPeriod = (long) (1000.0 / fps);
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
	
	public int getAiUps()
	{	return aiUps;
	}
	public long getAiPeriod()
	{	return aiPeriod;
	}

	public double getSpeedCoeff()
	{	return speedCoeff;
	}
	public void setSpeedCoeff(double speedCoeff)
	{	this.speedCoeff = speedCoeff;
	}
}

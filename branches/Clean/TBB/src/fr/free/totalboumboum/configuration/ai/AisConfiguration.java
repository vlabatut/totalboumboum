package fr.free.totalboumboum.configuration.ai;

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

public class AisConfiguration
{

	public AisConfiguration copy()
	{	AisConfiguration result = new AisConfiguration();

		result.setAiUps(aiUps);
		
		result.setAutoAdvance(autoAdvance);
		result.setAutoAdvanceDelay(autoAdvanceDelay);
		result.setHideAllAis(hideAllAis);
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TIMING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** ai updates per second */
	private int aiUps = 50;
	/** ai update period */
	private long aiPeriod = (long)(1000.0/aiUps);

	public int getAiUps()
	{	return aiUps;
	}
	
	public void setAiUps(int aiUps)
	{	this.aiUps = aiUps;
		aiPeriod = (long) (1000.0/aiUps);
	}
	
	public long getAiPeriod()
	{	return aiPeriod;
	}

	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** during a tournament/match, automatically advances to the next match/round */
	private boolean autoAdvance = false;
	/** delay (in ms) before the auto system advances to the next round */
	private long autoAdvanceDelay = 1000;
	/** during a tournament/match, only show rounds with at least a human player */
	private boolean hideAllAis = false;
	
	public long getAutoAdvanceDelay()
	{	return autoAdvanceDelay;
	}

	public void setAutoAdvanceDelay(long autoAdvanceDelay)
	{	this.autoAdvanceDelay = autoAdvanceDelay;
	}

	public boolean getAutoAdvance()
	{	return autoAdvance;
	}

	public void setAutoAdvance(boolean autoAdvance)
	{	this.autoAdvance = autoAdvance;
	}

	public boolean getHideAllAis()
	{	return hideAllAis;
	}

	public void setHideAllAis(boolean hideAllAis)
	{	this.hideAllAis = hideAllAis;
	}
}

package org.totalboumboum.configuration.game.quickmatch;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.configuration.profiles.ProfilesSelection;
import org.totalboumboum.tools.GameData;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class QuickMatchConfiguration
{
	public QuickMatchConfiguration copy()
	{	QuickMatchConfiguration result = new QuickMatchConfiguration();

		// options
		result.setUseLastPlayers(useLastPlayers);
		result.setUseLastLevels(useLastLevels);
		result.setUseLastSettings(useLastSettings);
	
		// settings
		result.setLevelsRandomOrder(levelsRandomOrder);
		result.setPlayersRandomLocation(playersRandomLocation);
		result.limitPoints = limitPoints;
		result.limitRounds = limitRounds;
		result.limitTime = limitTime;
		result.setPoints(points);
		result.setPointsShare(pointsShare);
		result.setPointsDraw(pointsDraw);
		result.setSuddenDeathDisabled(suddenDeathDisabled);
		
		// levels
		LevelsSelection levelsCopy = levelsSelection.copy();
		result.setLevelsSelection(levelsCopy);

		// players
		ProfilesSelection profilesCopy = profilesSelection.copy();
		result.setProfilesSelection(profilesCopy);

		return result;
	}
	
	public boolean hasChanged(QuickMatchConfiguration copy)
	{	boolean result = false;
		// use last players
		if(!result)
		{	boolean ulp = copy.getUseLastPlayers();
			result = !useLastPlayers==ulp;
		}
		// use last levels
		if(!result)
		{	boolean ull = copy.getUseLastLevels();
			result = !useLastLevels==ull;
		}
		// use last settings
		if(!result)
		{	boolean uls = copy.getUseLastSettings();
			result = !useLastSettings==uls;
		}
		//
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// OPTIONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean useLastPlayers = false;
	private boolean useLastLevels = false;
	private boolean useLastSettings = false;

	public boolean getUseLastPlayers()
	{	return useLastPlayers;
	}
	public void setUseLastPlayers(boolean useLastPlayers)
	{	this.useLastPlayers = useLastPlayers;
	}
	
	public boolean getUseLastLevels()
	{	return useLastLevels;
	}
	public void setUseLastLevels(boolean useLastLevels)
	{	this.useLastLevels = useLastLevels;
	}
	
	public boolean getUseLastSettings()
	{	return useLastSettings;
	}
	public void setUseLastSettings(boolean useLastSettings)
	{	this.useLastSettings = useLastSettings;
	}

	/////////////////////////////////////////////////////////////////
	// SETTINGS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean levelsRandomOrder = false;
	private boolean playersRandomLocation = false;
	private List<Integer> points = new ArrayList<Integer>(5);
	private boolean pointsShare = false;
	private QuickMatchDraw pointsDraw = QuickMatchDraw.BOTH;
	private int limitTime = 60000;
	private int limitPoints = 5;
	private int limitRounds = -1;
	private boolean suddenDeathDisabled = false;

	public int getLimitTime()
	{	return limitTime;
	}
	public void setLimitTime(int limitTime)
	{	this.limitTime = limitTime;
	}
	
	public int getLimitPoints()
	{	return limitPoints;
	}
	public void setLimitPoints(int limitPoints)
	{	this.limitPoints = limitPoints;
	}
	
	public int getLimitRounds()
	{	return limitRounds;
	}
	public void setLimitRounds(int limitRounds)
	{	this.limitRounds = limitRounds;
	}
	
	public boolean getLevelsRandomOrder()
	{	return levelsRandomOrder;
	}
	public void setLevelsRandomOrder(boolean levelsRandomOrder)
	{	this.levelsRandomOrder = levelsRandomOrder;
	}
	
	public boolean getPlayersRandomLocation()
	{	return playersRandomLocation;
	}
	public void setPlayersRandomLocation(boolean playersRandomLocation)
	{	this.playersRandomLocation = playersRandomLocation;
	}
	
	public List<Integer> getPoints()
	{	return points;
	}
	public void setPoints(List<Integer> points)
	{	this.points = points;
	}
	
	public boolean getPointsShare()
	{	return pointsShare;
	}
	public void setPointsShare(boolean pointsShare)
	{	this.pointsShare = pointsShare;
	}
	
	public QuickMatchDraw getPointsDraw()
	{	return pointsDraw;
	}
	public void setPointsDraw(QuickMatchDraw pointsDraw)
	{	this.pointsDraw = pointsDraw;
	}
	
	public boolean getSuddenDeathDisabled()
	{	return suddenDeathDisabled;
	}
	public void setSuddenDeathDisabled(boolean suddenDeath)
	{	this.suddenDeathDisabled = suddenDeath;
	}
	
	public void reinitSettings()
	{	levelsRandomOrder = false;
		playersRandomLocation = false;
		points = new ArrayList<Integer>();
		for(int i=0;i<GameData.CONTROL_COUNT;i++)
			points.set(i,0);
		pointsShare = false;
		pointsDraw = QuickMatchDraw.BOTH;
		limitTime = 60000;
		limitPoints = 5;
		limitRounds = -1;
		suddenDeathDisabled = false;
	}
	
	/////////////////////////////////////////////////////////////////
	// LEVELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private LevelsSelection levelsSelection = new LevelsSelection();

	public LevelsSelection getLevelsSelection()
	{	return levelsSelection;	
	}
	
	public void setLevelsSelection(LevelsSelection selectedLevels)
	{	this.levelsSelection = selectedLevels;	
	}

	public void reinitLevels()
	{	levelsSelection = new LevelsSelection();
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ProfilesSelection profilesSelection = new ProfilesSelection();

	public ProfilesSelection getProfilesSelection()
	{	return profilesSelection;	
	}
	
	public void setProfilesSelection(ProfilesSelection profilesSelection)
	{	this.profilesSelection = profilesSelection;	
	}

	public void reinitPlayers()
	{	profilesSelection = new ProfilesSelection();
	}
}

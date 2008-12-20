package fr.free.totalboumboum.configuration.game.quickmatch;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import fr.free.totalboumboum.configuration.profile.ProfilesSelection;

public class QuickMatchConfiguration
{
	public QuickMatchConfiguration copy()
	{	QuickMatchConfiguration result = new QuickMatchConfiguration();
		
		ProfilesSelection profilesCopy = profilesSelection.copy();
		result.setProfilesSelection(profilesCopy);
		result.setUseLastPlayers(useLastPlayers);
		LevelsSelection levelsCopy = levelsSelection.copy();
		result.setLevelsSelection(levelsCopy);
		result.setUseLastLevels(useLastLevels);
		result.setLevelsRandomOrder(levelsRandomOrder);
		result.setPlayersRandomLocation(playersRandomLocation);
		result.setPoints(points);
		result.setPointsShare(pointsShare);
		result.setPointsDraw(pointsDraw);

		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// QUICKMATCH		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean useLastPlayers = false;
	private boolean useLastLevels = false;
	private ProfilesSelection profilesSelection = new ProfilesSelection();
	private LevelsSelection levelsSelection = new LevelsSelection();
	private boolean levelsRandomOrder = false;
	private boolean playersRandomLocation = false;
	private ArrayList<Integer> points = new ArrayList<Integer>(5);
	private boolean pointsShare = false;
	private QuickMatchDraw pointsDraw = QuickMatchDraw.BOTH;
	private int limitTime = 60000;
	private int limitPoints = 5;
	private int limitRounds = -1;

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
	
	public ArrayList<Integer> getPoints()
	{	return points;
	}
	public void setPoints(ArrayList<Integer> points)
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
	
	public ProfilesSelection getProfilesSelection()
	{	return profilesSelection;	
	}	
	public void setProfilesSelection(ProfilesSelection profilesSelection)
	{	this.profilesSelection = profilesSelection;	
	}	

	public LevelsSelection getLevelsSelection()
	{	return levelsSelection;	
	}	
	public void setLevelsSelection(LevelsSelection selectedLevels)
	{	this.levelsSelection = selectedLevels;	
	}	
}

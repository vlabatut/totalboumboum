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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.ProfilesSelection;
import fr.free.totalboumboum.game.limit.ComparatorCode;
import fr.free.totalboumboum.game.limit.LimitConfrontation;
import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.limit.MatchLimit;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.match.MatchLoader;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.points.PointsTotal;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.round.RoundLoader;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.game.tournament.TournamentLoader;
import fr.free.totalboumboum.game.tournament.single.SingleTournament;
import fr.free.totalboumboum.tools.FileTools;

public class QuickMatchConfiguration
{
	public QuickMatchConfiguration copy()
	{	QuickMatchConfiguration result = new QuickMatchConfiguration();
		
		// tournament
		ProfilesSelection tournamentCopy = tournamentSelected.copy();
		result.setTournamentSelected(tournamentCopy);
		
		// quickmatch
		ProfilesSelection quickmatchProfilesCopy = quickMatchSelectedProfiles.copy();
		result.setQuickMatchSelectedProfiles(quickmatchProfilesCopy);
		result.setQuickMatchUseLastPlayers(quickMatchUseLastPlayers);
		result.setQuickMatchName(quickMatchName);
		LevelsSelection quickmatchLevelsCopy = quickMatchSelectedLevels.copy();
		result.setQuickMatchSelectedLevels(quickmatchLevelsCopy);
		result.setQuickMatchUseLastLevels(quickMatchUseLastLevels);
		result.setQuickMatchLevelsRandomOrder(quickMatchLevelsRandomOrder);
		result.setQuickMatchPlayersRandomLocation(quickMatchPlayersRandomLocation);
		result.setQuickMatchPoints(quickMatchPoints);
		result.setQuickMatchPointsShare(quickMatchPointsShare);
		result.setQuickMatchPointsDraw(quickMatchPointsDraw);
		
		// quickstart
		ProfilesSelection quickstartCopy = quickStartSelected.copy();
		result.setQuickStartSelected(quickstartCopy);
		result.setQuickStartName(quickStartName);

		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// QUICKMATCH		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String quickMatchName = null;
	private boolean quickMatchUseLastPlayers = false;
	private boolean quickMatchUseLastLevels = false;
	private ProfilesSelection quickMatchSelectedProfiles = new ProfilesSelection();
	private LevelsSelection quickMatchSelectedLevels = new LevelsSelection();
	private boolean quickMatchLevelsRandomOrder = false;
	private boolean quickMatchPlayersRandomLocation = false;
	private ArrayList<Integer> quickMatchPoints = new ArrayList<Integer>(5);
	private boolean quickMatchPointsShare = false;
	private QuickMatchDraw quickMatchPointsDraw = QuickMatchDraw.BOTH;
	private int quickMatchLimitTime = 60000;
	private int quickMatchLimitPoints = 5;
	private int quickMatchLimitRounds = -1;

	public int getQuickMatchLimitTime()
	{	return quickMatchLimitTime;
	}
	public void setQuickMatchLimitTime(int quickMatchLimitTime)
	{	this.quickMatchLimitTime = quickMatchLimitTime;
	}
	
	public int getQuickMatchLimitPoints()
	{	return quickMatchLimitPoints;
	}
	public void setQuickMatchLimitPoints(int quickMatchLimitPoints)
	{	this.quickMatchLimitPoints = quickMatchLimitPoints;
	}
	
	public int getQuickMatchLimitRounds()
	{	return quickMatchLimitRounds;
	}
	public void setQuickMatchLimitRounds(int quickMatchLimitRounds)
	{	this.quickMatchLimitRounds = quickMatchLimitRounds;
	}

	public boolean getQuickMatchUseLastPlayers()
	{	return quickMatchUseLastPlayers;
	}
	public void setQuickMatchUseLastPlayers(boolean quickMatchUseLastPlayers)
	{	this.quickMatchUseLastPlayers = quickMatchUseLastPlayers;
	}
	
	public boolean getQuickMatchUseLastLevels()
	{	return quickMatchUseLastLevels;
	}
	public void setQuickMatchUseLastLevels(boolean quickMatchUseLastLevels)
	{	this.quickMatchUseLastLevels = quickMatchUseLastLevels;
	}
	
	public boolean getQuickMatchLevelsRandomOrder()
	{	return quickMatchLevelsRandomOrder;
	}
	public void setQuickMatchLevelsRandomOrder(boolean quickMatchLevelsRandomOrder)
	{	this.quickMatchLevelsRandomOrder = quickMatchLevelsRandomOrder;
	}
	
	public boolean getQuickMatchPlayersRandomLocation()
	{	return quickMatchPlayersRandomLocation;
	}
	public void setQuickMatchPlayersRandomLocation(boolean quickMatchPlayersRandomLocation)
	{	this.quickMatchPlayersRandomLocation = quickMatchPlayersRandomLocation;
	}
	
	public ArrayList<Integer> getQuickMatchPoints()
	{	return quickMatchPoints;
	}
	public void setQuickMatchPoints(ArrayList<Integer> quickMatchPoints)
	{	this.quickMatchPoints = quickMatchPoints;
	}
	
	public boolean getQuickMatchPointsShare()
	{	return quickMatchPointsShare;
	}
	public void setQuickMatchPointsShare(boolean quickMatchPointsShare)
	{	this.quickMatchPointsShare = quickMatchPointsShare;
	}
	
	public QuickMatchDraw getQuickMatchPointsDraw()
	{	return quickMatchPointsDraw;
	}
	public void setQuickMatchPointsDraw(QuickMatchDraw quickMatchPointsDraw)
	{	this.quickMatchPointsDraw = quickMatchPointsDraw;
	}
	
	public String getQuickMatchName()
	{	return quickMatchName;	
	}
	public void setQuickMatchName(String quickMatchName)
	{	this.quickMatchName = quickMatchName;
	}
	
	public ProfilesSelection getQuickMatchSelectedProfiles()
	{	return quickMatchSelectedProfiles;	
	}	
	public void setQuickMatchSelectedProfiles(ProfilesSelection quickMatchSelectedProfiles)
	{	this.quickMatchSelectedProfiles = quickMatchSelectedProfiles;	
	}	

	public LevelsSelection getQuickMatchSelectedLevels()
	{	return quickMatchSelectedLevels;	
	}	
	public void setQuickMatchSelectedLevels(LevelsSelection quickMatchSelectedLevels)
	{	this.quickMatchSelectedLevels = quickMatchSelectedLevels;	
	}	

	public SingleTournament loadQuickmatch() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// single tournament
		SingleTournament result = new SingleTournament();
		// load match
		Match match = MatchLoader.loadMatchFromName(quickMatchName,result);
		result.setMatch(match);
		// 
		return result;
	}	
}

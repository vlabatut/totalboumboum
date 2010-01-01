package org.totalboumboum.configuration.game.tournament;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.profile.ProfilesSelection;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.game.tournament.TournamentLoader;
import org.totalboumboum.tools.files.FileTools;
import org.xml.sax.SAXException;


public class TournamentConfiguration
{
	public TournamentConfiguration copy()
	{	TournamentConfiguration result = new TournamentConfiguration();
		
		// options
		result.setUseLastPlayers(useLastPlayers);
		result.setUseLastTournament(useLastTournament);
		result.setAutoLoad(autoLoad);
		result.setAutoSave(autoSave);
		
		// tournament
		result.setTournamentName(new StringBuffer(tournamentName));
		
		// players
		ProfilesSelection tournamentCopy = profilesSelection.copy();
		result.setProfilesSelection(tournamentCopy);

		return result;
	}
	
	public boolean hasChanged(TournamentConfiguration copy)
	{	boolean result = false;
		// use last players
		if(!result)
		{	boolean ulp = copy.getUseLastPlayers();
			result = !useLastPlayers==ulp;
		}
		// use last tournament
		if(!result)
		{	boolean ult = copy.getUseLastTournament();
			result = !useLastTournament==ult;
		}
		// auto load
		if(!result)
		{	boolean al = copy.getAutoLoad();
			result = !autoLoad==al;
		}
		// auto save
		if(!result)
		{	boolean as = copy.getAutoSave();
			result = !autoSave==as;
		}
		//
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// OPTIONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean useLastPlayers = false;
	private boolean useLastTournament = false;
	private boolean autoSave = true;
	private boolean autoLoad = true;

	public boolean getUseLastPlayers()
	{	return useLastPlayers;
	}
	public void setUseLastPlayers(boolean useLastPlayers)
	{	this.useLastPlayers = useLastPlayers;
	}
	
	public boolean getUseLastTournament()
	{	return useLastTournament;
	}
	public void setUseLastTournament(boolean useLastTournament)
	{	this.useLastTournament = useLastTournament;
	}

	public boolean getAutoSave()
	{	return autoSave;
	}
	public void setAutoSave(boolean autoSave)
	{	this.autoSave = autoSave;
	}

	public boolean getAutoLoad()
	{	return autoLoad;
	}
	public void setAutoLoad(boolean autoLoad)
	{	this.autoLoad = autoLoad;
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private StringBuffer tournamentName = new StringBuffer();
	private AbstractTournament tournament;
	
	public AbstractTournament getTournament()
	{	return tournament;
	}
	
	public void setTournament(AbstractTournament tournament)
	{	this.tournament = tournament;
	}

	public StringBuffer getTournamentName()
	{	return tournamentName;
	}
	
	public void setTournamentName(StringBuffer tournamentName)
	{	this.tournamentName = tournamentName;
	}

	public void loadLastTournament() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	if(tournamentName!=null)
		{	// String folderPath = FileTools.getConfigurationPath()+File.separator+FileTools.FILE_TOURNAMENT;
			String folderPath = FileTools.getTournamentsPath()+File.separator+tournamentName;
			tournament = TournamentLoader.loadTournamentFromFolderPath(folderPath);
		}
	}
	
	public void reinitTournament() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	tournamentName.delete(0,tournamentName.length());
		String folderPath = FileTools.getConfigurationPath()+File.separator+FileTools.FOLDER_TOURNAMENT;
		tournament = TournamentLoader.loadTournamentFromFolderPath(folderPath);
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ProfilesSelection profilesSelection = new ProfilesSelection();

	public ProfilesSelection getProfilesSelection()
	{	return profilesSelection;	
	}	
	
	public void setProfilesSelection(ProfilesSelection profimesSelection)
	{	this.profilesSelection = profimesSelection;	
	}	

	public void reinitPlayers()
	{	profilesSelection = new ProfilesSelection();
	}
}

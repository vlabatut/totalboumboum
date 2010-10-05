package org.totalboumboum.stream.file.archive;

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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.game.tournament.TournamentType;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class GameArchive
{
	public static GameArchive getArchive(AbstractTournament tournament, String folder)
	{	GameArchive result = new GameArchive();
		
		// folder
		result.folder = folder;
		
		// tournament
		result.name = tournament.getName();
		result.type = TournamentType.getType(tournament);
		
		// played
		result.matches = 0;
		if(tournament.getStats()!=null)
			result.matches = tournament.getStats().getConfrontationCount();
		Match match = tournament.getCurrentMatch();
		if(match==null || match.getStats()==null)
			result.rounds = 0;
		else
			result.rounds = match.getStats().getConfrontationCount();
		
		// dates
		result.start = tournament.getStats().getStartDate();
		result.save = GregorianCalendar.getInstance().getTime();
		
		// players
		List<Profile> profiles = tournament.getProfiles();
		for(Profile profile: profiles)
		{	String name = profile.getName();
			result.addPlayer(name);
		}
		
		return result;
	}

	public static void saveGame(String folder, AbstractTournament tournament) throws ParserConfigurationException, SAXException, IOException
	{	String path = FilePaths.getSavesPath()+File.separator+folder;
		// folder
		File folderFile = new File(path);
		if(!folderFile.exists())
			folderFile.mkdir();
		// XML file
		GameArchive gameArchive = GameArchive.getArchive(tournament,folder);
		GameArchiveSaver.saveGameArchive(gameArchive);
		// data file
		String fileName = FileNames.FILE_ARCHIVE+FileNames.EXTENSION_DATA;
		File file = new File(path+File.separator+fileName);
		FileOutputStream out = new FileOutputStream(file);
		BufferedOutputStream outBuff = new BufferedOutputStream(out);
		ObjectOutputStream oOut = new ObjectOutputStream(outBuff);
		oOut.writeObject(tournament);
		oOut.close();
	}
	
	public static AbstractTournament loadGame(String folder) throws IOException, ClassNotFoundException, ParserConfigurationException, SAXException
	{	String path = FilePaths.getSavesPath()+File.separator+folder;
		String fileName = FileNames.FILE_ARCHIVE+FileNames.EXTENSION_DATA;
		File file = new File(path+File.separator+fileName);
		FileInputStream in = new FileInputStream(file);
		BufferedInputStream inBuff = new BufferedInputStream(in);
		ObjectInputStream oIn = new ObjectInputStream(inBuff);
		AbstractTournament result = (AbstractTournament)oIn.readObject();
		oIn.close();
		result.reloadPortraits();
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// FOLDER				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String folder;
	
	public void setFolder(String folder)
	{	this.folder = folder;
	}
	public String getFolder()
	{	return folder;
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String name;
	private TournamentType type;
	
	public void setName(String name)
	{	this.name = name;
	}
	public String getName()
	{	return name;
	}
	
	public void setType(TournamentType type)
	{	this.type = type;
	}
	public TournamentType getType()
	{	return type;
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYED				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int matches;
	private int rounds;
	
	public void setPlayedMatches(int matches)
	{	this.matches = matches;
	}
	public int getPlayedMatches()
	{	return matches;
	}
	
	public void setPlayedRounds(int rounds)
	{	this.rounds = rounds;
	}
	public int getPlayedRounds()
	{	return rounds;
	}

	/////////////////////////////////////////////////////////////////
	// DATES				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Date start;
	private Date save;
	
	public void setStartDate(Date start)
	{	this.start = start;
	}
	public Date getStartDate()
	{	return start;
	}
	
	public void setSaveDate(Date save)
	{	this.save = save;
	}
	public Date getSaveDate()
	{	return save;
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<String> players = new ArrayList<String>();
	
	public void addPlayer(String player)
	{	players.add(player);
	}
	public List<String> getPlayers()
	{	return players;
	}
}

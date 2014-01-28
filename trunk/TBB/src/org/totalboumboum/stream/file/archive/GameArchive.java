package org.totalboumboum.stream.file.archive;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
 * Represents  a game recorded in a file.
 * 
 * @author Vincent Labatut
 */
public class GameArchive
{
	/**
	 * Builds the game archive object for the specified tournament.
	 * 
	 * @param tournament
	 * 		Tournament saved. 
	 * @param folder
	 * 		Folder containing the files.
	 * @return
	 * 		Corresponding game archive object.
	 */
	public static GameArchive getArchive(AbstractTournament tournament, String folder)
	{	GameArchive result = new GameArchive();
		
		// folder
		result.folder = folder;
		
		// tournament
		result.name = tournament.getName();
		result.type = TournamentType.getType(tournament);
		
		// played
		result.matchesPlayed = 0;
		if(tournament.getStats()!=null)
			result.matchesPlayed = tournament.getStats().getConfrontationCount();
		Match match = tournament.getCurrentMatch();
		if(match==null || match.getStats()==null)
			result.roundsPlayed = 0;
		else
			result.roundsPlayed = match.getStats().getConfrontationCount();
		
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
	
	/**
	 * Records the specified tournament in the specified folder.
	 * 
	 * @param folder
	 * 		Folder to contain the save.
	 * @param tournament
	 * 		Tournament to save.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while accessing the files.
	 * @throws SAXException
	 * 		Problem while accessing the files.
	 * @throws IOException
	 * 		Problem while accessing the files.
	 */
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
	
	/**
	 * Loads the save directly located in the specified folder.
	 * 
	 * @param folder
	 * 		Folder containing the savE.
	 * @return
	 * 		Loaded tournament.
	 * 
	 * @throws IOException
	 * 		Problem while accessing the existing files.
	 * @throws ClassNotFoundException
	 * 		Problem while accessing the existing files.
	 * @throws ParserConfigurationException
	 * 		Problem while accessing the existing files.
	 * @throws SAXException
	 * 		Problem while accessing the existing files.
	 */
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
	/** Folder containing the save */
	private String folder;
	
	/**
	 * Changes the folder containing the save.
	 * 
	 * @param folder
	 * 		Folder containing the save.
	 */
	public void setFolder(String folder)
	{	this.folder = folder;
	}
	
	/**
	 * Returns the folder containing the save.
	 * 
	 * @return
	 * 		Folder containing the save.
	 */
	public String getFolder()
	{	return folder;
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Name of the tournament */
	private String name;
	/** Type of the tournament (cup, league, etc.) */
	private TournamentType type;
	
	/**
	 * Changes the name of the tournament.
	 * 
	 * @param name
	 * 		Name of the tournament.
	 */
	public void setName(String name)
	{	this.name = name;
	}
	
	/**
	 * Returns the name of the tournament.
	 * 
	 * @return
	 * 		Name of the tournament.
	 */
	public String getName()
	{	return name;
	}
	
	/**
	 * Changes the type of the tournament (cup, league, etc.).
	 * 
	 * @param type
	 * 		Type of the tournament.
	 */
	public void setType(TournamentType type)
	{	this.type = type;
	}
	
	/**
	 * Returns the type of the tournament (cup, league, etc.).
	 * 
	 * @return
	 * 		Type of the tournament.
	 */
	public TournamentType getType()
	{	return type;
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYED CONFRONTATIONS	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Number of matches played until now */
	private int matchesPlayed;
	/** Number of rounds played until now */
	private int roundsPlayed;
	
	/**
	 * Changes the number of matches played until now.
	 * 
	 * @param matchesPlayed
	 * 		Number of matches played until now.
	 */
	public void setPlayedMatches(int matchesPlayed)
	{	this.matchesPlayed = matchesPlayed;
	}
	
	/**
	 * Returns the number of matches played until now.
	 * 
	 * @return
	 * 		Number of matches played until now.
	 */
	public int getPlayedMatches()
	{	return matchesPlayed;
	}
	
	/**
	 * Changes the number of rounds played until now.
	 * 
	 * @param roundsPlayed
	 * 		Number of rounds played until now.
	 */
	public void setPlayedRounds(int roundsPlayed)
	{	this.roundsPlayed = roundsPlayed;
	}
	
	/**
	 * Returns the number of rounds played until now.
	 * 
	 * @return
	 * 		Number of rounds played until now.
	 */
	public int getPlayedRounds()
	{	return roundsPlayed;
	}

	/////////////////////////////////////////////////////////////////
	// TOTAL CONFRONTATIONS		/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Total number of matches */
	private int matchesTotal;
	/** Total number of rounds */
	private int roundsTotal;
	
	/**
	 * Changes the total number of matches.
	 * 
	 * @param matchesTotal
	 * 		Total number of matches.
	 */
	public void setTotalMatches(int matchesTotal)
	{	this.matchesTotal = matchesTotal;
	}
	
	/**
	 * Returns the total number of matches.
	 * 
	 * @return
	 * 		Total number of matches.
	 */
	public int getTotalMatches()
	{	return matchesTotal;
	}
	
	/**
	 * Changes the total number of rounds.
	 * 
	 * @param roundsTotal
	 * 		Total number of rounds.
	 */
	public void setTotalRounds(int roundsTotal)
	{	this.roundsTotal = roundsTotal;
	}
	
	/**
	 * Returns the Total number of rounds.
	 * 
	 * @return
	 * 		Total number of rounds.
	 */
	public int getTotalRounds()
	{	return roundsTotal;
	}

	/////////////////////////////////////////////////////////////////
	// DATES				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Date the tournament was started */
	private Date start;
	/** Date the tournament was saved */
	private Date save;
	
	/**
	 * Changes the date the tournament was started.
	 * 
	 * @param start
	 * 		Date the tournament was started.
	 */
	public void setStartDate(Date start)
	{	this.start = start;
	}
	
	/**
	 * Returns the date the tournament was started.
	 * 
	 * @return
	 * 		Date the tournament was started.
	 */
	public Date getStartDate()
	{	return start;
	}
	
	/**
	 * Changes the date the tournament was saved.
	 * 
	 * @param save
	 * 		Date the tournament was saved.
	 */
	public void setSaveDate(Date save)
	{	this.save = save;
	}
	
	/**
	 * Returns the date the tournament was saved.
	 * 
	 * @return
	 * 		Date the tournament was saved.
	 */
	public Date getSaveDate()
	{	return save;
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Players involved in the tournament */
	private final List<String> players = new ArrayList<String>();
	
	/**
	 * Adds a player to the list of those involved in
	 * this tournament.
	 * 
	 * @param player
	 * 		Player to add to the list.
	 */
	public void addPlayer(String player)
	{	players.add(player);
	}

	/**
	 * Returns the list of players involved in
	 * this tournament.
	 * 
	 * @return
	 * 		Players involved in this tournement.
	 */
	public List<String> getPlayers()
	{	return players;
	}
}

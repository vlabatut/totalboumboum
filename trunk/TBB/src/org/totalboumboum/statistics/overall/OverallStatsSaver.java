package org.totalboumboum.statistics.overall;

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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.profile.ProfileLoader;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.xml.sax.SAXException;

/**
 * This class is in charge for recording the overall statistics,
 * i.e. those not already handled by the Glicko-2 system
 * (especially scores).
 * 
 * @author Vincent Labatut
 */
public class OverallStatsSaver
{	/** Mute or display debug messages */
	private static final boolean verbose = false;

	/**
	 * Record the overall statistics as a serialized java object.
	 * 
	 * @param playersStats
	 * 		Overall statistics to be recorded.
	 * 
	 * @throws IOException
	 * 		Problem while recording the serialized file.
	 * @throws IllegalArgumentException
	 * 		Problem while recording the serialized file.
	 * @throws SecurityException
	 * 		Problem while recording the serialized file.
	 * @throws ParserConfigurationException
	 * 		Problem while recording the serialized file.
	 * @throws SAXException
	 * 		Problem while recording the serialized file.
	 * @throws IllegalAccessException
	 * 		Problem while recording the serialized file.
	 * @throws NoSuchFieldException
	 * 		Problem while recording the serialized file.
	 * @throws ClassNotFoundException
	 * 		Problem while recording the serialized file.
	 */
	public static void saveOverallStatistics(Map<String,PlayerStats> playersStats) throws IOException, IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// init files
		String path = FilePaths.getOverallStatisticsPath()+File.separator+FileNames.FILE_STATISTICS+FileNames.EXTENSION_DATA;
		String backup = FilePaths.getOverallStatisticsPath()+File.separator+FileNames.FILE_STATISTICS+FileNames.EXTENSION_BACKUP;
		File backupFile = new File(backup);
		File previousFile = new File(path);
		
		// move previous file
		backupFile.delete();
		previousFile.renameTo(backupFile);
		
		// write the stats
		File file = new File(path);
		FileOutputStream fileOut = new FileOutputStream(file);
		BufferedOutputStream outBuff = new BufferedOutputStream(fileOut);
		ObjectOutputStream out = new ObjectOutputStream(outBuff);
		out.writeObject(playersStats);
		out.close();
		
		// display written data (debug)
		if(verbose)
		{	System.out.println("\n######### OVERALL STATISTICS #########");
			for(PlayerStats playerStats: playersStats.values())
			{	String playerId = playerStats.getPlayerId();
				String text = playerStats.toString();
				Profile profile = ProfileLoader.loadProfile(playerId);
				System.out.println(profile.getName()+text);				
			}
		}
	}

	/**
	 * (Re)Initializes the overall stats of all players.
	 * 
	 * @return
	 * 		Overall statistics.
	 * 
	 * @throws IllegalArgumentException
	 * 		Problem while recording the serialized file.
	 * @throws SecurityException
	 * 		Problem while recording the serialized file.
	 * @throws IOException
	 * 		Problem while recording the serialized file.
	 * @throws ParserConfigurationException
	 * 		Problem while recording the serialized file.
	 * @throws SAXException
	 * 		Problem while recording the serialized file.
	 * @throws IllegalAccessException
	 * 		Problem while recording the serialized file.
	 * @throws NoSuchFieldException
	 * 		Problem while recording the serialized file.
	 * @throws ClassNotFoundException
	 * 		Problem while recording the serialized file.
	 */
	public static HashMap<String,PlayerStats> initOverallStatistics() throws IllegalArgumentException, SecurityException, IOException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// create stats map
		HashMap<String,PlayerStats> result = new HashMap<String, PlayerStats>();
		
		// get ids list
	    List<String> idsList = ProfileLoader.getIdsList();

		// init stats for all existing players
		for(String id: idsList)
		{	if(verbose)
				System.out.println(id);
			PlayerStats playerStats = new PlayerStats(id);
			result.put(id,playerStats);
		}
		
		// save the rankings
		saveOverallStatistics(result);
		return result;
	}
	
	/**
	 * Export overall stats as text (for stats maintenance through classes changes).
	 * 
	 * @param playersStats
	 * 		Stats to be exported.
	 * 
	 * @throws FileNotFoundException
	 * 		Problem while accessing the text file. 
	 */
	public static void exportOverallStatistics(HashMap<String,PlayerStats> playersStats) throws FileNotFoundException
	{	// open the file
		String path = FilePaths.getOverallStatisticsPath()+File.separator+FileNames.FILE_STATISTICS+FileNames.EXTENSION_TEXT;
		File file = new File(path);
		FileOutputStream fileOut = new FileOutputStream(file);
		BufferedOutputStream outBuff = new BufferedOutputStream(fileOut);
		OutputStreamWriter outSW = new OutputStreamWriter(outBuff);
		PrintWriter writer = new PrintWriter(outSW);
		
		// write data
		for(PlayerStats ps: playersStats.values())
			ps.exportToText(writer);
		
		writer.close();
	}
	
	/**
	 * Remove the history file corresponding
	 * to the specified player. This operation
	 * is performed only when the player profile is
	 * definitely removed from the game.
	 * 
	 * @param playerId
	 * 		Id of the removed player.
	 */
	public static void removePlayer(String playerId)
	{	String historyFolder = FilePaths.getDetailedStatisticsPath();
		File dataFile = new File(historyFolder+File.separator+playerId+FileNames.EXTENSION_TEXT);
		if(dataFile.exists())
			dataFile.delete();
	}
}

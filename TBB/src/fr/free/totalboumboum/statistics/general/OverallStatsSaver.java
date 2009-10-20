package fr.free.totalboumboum.statistics.general;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.configuration.profile.ProfileLoader;
import fr.free.totalboumboum.tools.FileTools;

public class OverallStatsSaver
{	private static final boolean verbose = false;

	public static void saveStatistics(HashMap<Integer,PlayerStats> playersStats) throws IOException, IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// init files
		String path = FileTools.getOverallStatisticsPath()+File.separator+FileTools.FILE_STATISTICS+FileTools.EXTENSION_DATA;
		String backup = FileTools.getOverallStatisticsPath()+File.separator+FileTools.FILE_STATISTICS+FileTools.EXTENSION_BACKUP;
		File backupFile = new File(backup);
		File previousFile = new File(path);
		
		// move previous file
		backupFile.delete();
		previousFile.renameTo(backupFile);
		
		// write the rankings
		File file = new File(path);
		FileOutputStream fileOut = new FileOutputStream(file);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(playersStats);
		
		// display written data (debug)
		if(verbose)
		{	System.out.println("\n######### OVERALL STATISTICS #########");
			for(PlayerStats playerStats: playersStats.values())
			{	int playerId = playerStats.getPlayerId();
				String text = playerStats.toString();
				Profile profile = ProfileLoader.loadProfile(playerId);
				System.out.println(profile.getName()+text);				
			}
		}
	}

	public static void init() throws IllegalArgumentException, SecurityException, IOException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// create stats map
		HashMap<Integer,PlayerStats> playersStats = new HashMap<Integer, PlayerStats>();
		
		// get ids list
	    List<Integer> idsList = ProfileLoader.getIdsList();

		// register all existing players
		for(Integer id: idsList)
		{	System.out.println(id);
			PlayerStats playerStats = new PlayerStats(id);
			playersStats.put(id,playerStats);
		}
		
		// save the rankings
		saveStatistics(playersStats);
	}
}

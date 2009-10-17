package fr.free.totalboumboum.statistics.glicko2;

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
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.SortedSet;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.configuration.profile.ProfileLoader;
import fr.free.totalboumboum.statistics.glicko2.jrs.PlayerRating;
import fr.free.totalboumboum.statistics.glicko2.jrs.RankingService;
import fr.free.totalboumboum.statistics.glicko2.jrs.ResultsBasedRankingService;
import fr.free.totalboumboum.tools.FileTools;

public class Glicko2Saver
{	private static final boolean verbose = true;

	public static void saveStatistics(RankingService rankingService) throws IOException, IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// init files
		String path = FileTools.getGlicko2Path()+File.separator+FileTools.FILE_STATISTICS+FileTools.EXTENSION_DATA;
		String backup = FileTools.getGlicko2Path()+File.separator+FileTools.FILE_STATISTICS+FileTools.EXTENSION_BACKUP;
		File backupFile = new File(backup);
		File previousFile = new File(path);
		
		// move previous file
		backupFile.delete();
		previousFile.renameTo(backupFile);
		
		// write the rankings
		File file = new File(path);
		FileOutputStream fileOut = new FileOutputStream(file);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(rankingService);
		
		// display rankings (debug)
		if(verbose)
		{	SortedSet<PlayerRating> playerRatings = rankingService.getSortedPlayerRatings();
			System.out.println("\n######### GLICKO-2 RANKINGS #########");
			for(PlayerRating pr: playerRatings)
			{	int playerId = (Integer)pr.getPlayerId();
				Profile profile = ProfileLoader.loadProfile(playerId);
				double rating = pr.getRating();
				double ratingDeviation = pr.getRatingDeviation();
				double volatility = pr.getRatingVolatility();
				System.out.println(profile.getName()+"\t\t"+playerId+"\t"+rating+"\t"+ratingDeviation+"\t"+volatility);				
			}
		}
	}

	public static void init() throws IllegalArgumentException, SecurityException, IOException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// change ranking properties
		
		// create ranking service
		ResultsBasedRankingService rankingService = new ResultsBasedRankingService();
		
		// register all existing players
		String folderStr = FileTools.getProfilesPath();
		File folder = new File(folderStr);
		FileFilter filter = new FileFilter()
		{	@Override
			public boolean accept(File pathname)
			{	String name = pathname.getName();
				int length = name.length();
				int extLength = FileTools.EXTENSION_XML.length();
				String ext = name.substring(length-extLength,length);
				return ext.equalsIgnoreCase(FileTools.EXTENSION_XML);
			}			
		};
		File[] files = folder.listFiles(filter);
		for(File file: files)
		{	int length = file.getName().length();
			int extLength = FileTools.EXTENSION_XML.length();
			String idStr = file.getName().substring(0,length-extLength);
			int id = Integer.parseInt(idStr);
			System.out.println(id);
			rankingService.registerPlayer(id);
		}
		
		// save the rankings
		saveStatistics(rankingService);
	}
}

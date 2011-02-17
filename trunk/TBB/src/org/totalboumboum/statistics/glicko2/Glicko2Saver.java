package org.totalboumboum.statistics.glicko2;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
import java.util.List;
import java.util.SortedSet;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.profile.ProfileLoader;
import org.totalboumboum.statistics.glicko2.jrs.PlayerRating;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.statistics.glicko2.jrs.ResultsBasedRankingService;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class Glicko2Saver
{	private static final boolean verbose = false;

	public static void saveGlicko2Statistics(RankingService rankingService) throws IOException, IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// init files
		String path = FilePaths.getGlicko2Path()+File.separator+FileNames.FILE_STATISTICS+FileNames.EXTENSION_DATA;
		String backup = FilePaths.getGlicko2Path()+File.separator+FileNames.FILE_STATISTICS+FileNames.EXTENSION_BACKUP;
		File backupFile = new File(backup);
		File previousFile = new File(path);
		
		// move previous file
		backupFile.delete();
		previousFile.renameTo(backupFile);
		
		// write the rankings
		File file = new File(path);
		FileOutputStream fileOut = new FileOutputStream(file);
		BufferedOutputStream outBuff = new BufferedOutputStream(fileOut);
		ObjectOutputStream out = new ObjectOutputStream(outBuff);
		out.writeObject(rankingService);
		out.close();
		
		// display rankings (debug)
		if(verbose)
		{	SortedSet<PlayerRating> playerRatings = rankingService.getSortedPlayerRatings();
			System.out.println("\n######### GLICKO-2 RANKINGS #########");
			int total = 0;
			int players = 0;
			for(PlayerRating pr: playerRatings)
			{	String playerId = (String)pr.getPlayerId();
				Profile profile = ProfileLoader.loadProfile(playerId);
				double rating = pr.getRating();
				double ratingDeviation = pr.getRatingDeviation();
				double volatility = pr.getRatingVolatility();
				int roundcount = pr.getRoundcount();
				System.out.println(profile.getName()+"\t\t"+playerId+"\t"+rating+"\t"+ratingDeviation+"\t"+volatility+"\t"+roundcount);				
				total = total + roundcount;
				players ++;
			}
			System.out.println("total;average rounds played since last update: "+total+";"+(total/(float)players));
		}
	}

	public static ResultsBasedRankingService initGlicko2Statistics() throws IllegalArgumentException, SecurityException, IOException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// change ranking properties
		
		// create ranking service
		ResultsBasedRankingService result = new ResultsBasedRankingService();
		
		// get ids list
	    List<String> idsList = ProfileLoader.getIdsList();

		// register all existing players
		for(String id: idsList)
		{	if(verbose)
				System.out.println(id);
			result.registerPlayer(id);
		}
		
		// save the rankings
		saveGlicko2Statistics(result);
		return result;
	}

	/**
	 * export glicko2 stats as text (for stats maintenance through classes changes)
	 */
	public static void exportGlicko2Statistics(RankingService rankingService) throws FileNotFoundException
	{	// open the file
		String path = FilePaths.getGlicko2Path()+File.separator+FileNames.FILE_STATISTICS+FileNames.EXTENSION_TEXT;
		File file = new File(path);
		FileOutputStream fileOut = new FileOutputStream(file);
		BufferedOutputStream outBuff = new BufferedOutputStream(fileOut);
		OutputStreamWriter outSW = new OutputStreamWriter(outBuff);
		PrintWriter writer = new PrintWriter(outSW);
		
		// write data
		rankingService.exportToText(writer);
		
		writer.close();
	}
}

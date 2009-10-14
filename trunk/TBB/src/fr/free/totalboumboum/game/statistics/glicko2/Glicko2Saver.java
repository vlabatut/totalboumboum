package fr.free.totalboumboum.game.statistics.glicko2;

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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import jrs.PlayerRating;
import jrs.RankingService;

import fr.free.totalboumboum.tools.FileTools;

public class Glicko2Saver
{
	public static void saveStatistics(RankingService rankingService, HashMap<Integer,Integer> roundCounts) throws IOException
	{	// init files
		String path = FileTools.getGlicko2Path()+File.separator+FileTools.FILE_STATISTICS+FileTools.EXTENSION_DATA;
		String backup = FileTools.getGlicko2Path()+File.separator+FileTools.FILE_STATISTICS+FileTools.EXTENSION_BACKUP;
		File backupFile = new File(backup);
		File previousFile = new File(path);
		
		// move previous file
		backupFile.delete();
		previousFile.renameTo(backupFile);
		
		// write the rankings for each registered player
        PrintWriter pw = new PrintWriter(new FileWriter(path));
        for(Object o:rankingService.getPlayers())
        {	int id = (Integer)o;
        	int roundCount = roundCounts.get(id);
	        PlayerRating playerRating = rankingService.getPlayerRating(o);
	        StringBuffer record = new StringBuffer();
	        record.append(id).append("|");
	        record.append(roundCount).append("|");
	        record.append(playerRating.getRating()).append("|");
	        record.append(playerRating.getRatingDeviation()).append("|");
	        record.append(playerRating.getRatingVolatility());
	        //System.out.println("Writing '" + record + "' to '" + dataStoreFile.getAbsolutePath() + "'");
	        pw.println(record);
        }
        pw.flush();
        pw.close();
	}
}

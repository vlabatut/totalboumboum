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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import jrs.PlayerRating;
import jrs.RankingService;

import fr.free.totalboumboum.tools.FileTools;

public class RanksLoader
{
	public static void loadStatistics(RankingService rankingService) throws NumberFormatException, IOException
	{	// init path
		String path = FileTools.getGlicko2Path()+File.pathSeparator+FileTools.FILE_STATISTICS+FileTools.EXTENSION_DATA;

		// open text file
		BufferedReader br = new BufferedReader(new FileReader(path));
		
		// read the file content and update the ranking service
		String line;
		while((line = br.readLine()) != null)
		{	int index = 0;
			String[] fields = line.split("\\|");
			int playerId = Integer.parseInt(fields[index++]);
			double rating = Double.parseDouble(fields[index++]);
			double ratingDeviation = Double.parseDouble(fields[index++]);
			double ratingVolatility = Double.parseDouble(fields[index++]);
			PlayerRating playerRating = new PlayerRating(playerId,rating,ratingDeviation,ratingVolatility);
			rankingService.registerPlayer(playerId, playerRating);
		}
		br.close();
        // TODO: if problem while reading the file, should restaure and use the backup 
	}
}

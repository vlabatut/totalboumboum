package org.totalboumboum.statistics.overall;

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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Scanner;

import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class OverallStatsLoader
{
	@SuppressWarnings("unchecked")
	public static HashMap<String,PlayerStats> loadOverallStatistics() throws IOException, ClassNotFoundException
	{	// init path
		String path = FilePaths.getOverallStatisticsPath()+File.separator+FileNames.FILE_STATISTICS+FileNames.EXTENSION_DATA;
		
		// read the rankings
		File file = new File(path);
		FileInputStream filein = new FileInputStream(file);
		BufferedInputStream inBuff = new BufferedInputStream(filein);
		ObjectInputStream in = new ObjectInputStream(inBuff);
		HashMap<String,PlayerStats> result = (HashMap<String,PlayerStats>) in.readObject();
		in.close();
		return result;		
		// TODO: if problem while reading the file, should restaure and use the backup 
	}

	public static HashMap<String,PlayerStats> importOverallStatistics() throws FileNotFoundException
	{	HashMap<String,PlayerStats> result = new HashMap<String, PlayerStats>();
	
		// open the file
		String path = FilePaths.getOverallStatisticsPath()+File.separator+FileNames.FILE_STATISTICS+FileNames.EXTENSION_TEXT;
		File file = new File(path);
		FileInputStream fileIn = new FileInputStream(file);
		BufferedInputStream inBuff = new BufferedInputStream(fileIn);
		InputStreamReader inSR = new InputStreamReader(inBuff);
		Scanner scanner = new Scanner(inSR);
		
		// read data
		while(scanner.hasNextLine())
		{	PlayerStats playerStats = new PlayerStats("");
			playerStats.importFromText(scanner);
			result.put(playerStats.getPlayerId(),playerStats);
		}
		
		scanner.close();
		return result;
	}
}

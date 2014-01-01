package org.totalboumboum.statistics.glicko2;

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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.Scanner;

import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.statistics.glicko2.jrs.ResultsBasedRankingService;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;

/**
 * This class is in charge for loading the Glicko-2 statistics
 * for all registered players.
 * 
 * @author Vincent Labatut
 */
public class Glicko2Loader
{
	/**
	 * Loads the Glicko-2 statistics from a serialized java file.
	 * 
	 * @return
	 * 		Glicko-2 statistics.
	 * 
	 * @throws IOException
	 * 		Problem while accessing the serialized file.
	 * @throws ClassNotFoundException
	 * 		Problem while accessing the serialized file.
	 */
	public static RankingService loadGlicko2Statistics() throws IOException, ClassNotFoundException
	{	// init path
		String path = FilePaths.getGlicko2Path()+File.separator+FileNames.FILE_STATISTICS+FileNames.EXTENSION_DATA;
		
		// read the rankings
		File file = new File(path);
		FileInputStream filein = new FileInputStream(file);
		BufferedInputStream inBuff = new BufferedInputStream(filein);
		ObjectInputStream in = new ObjectInputStream(inBuff);
		RankingService result = (RankingService) in.readObject();
		in.close();
		return result;		
		// TODO: if problem while reading the file, should restore and use the backup 
	}

	/**
	 * Retrieves the Glicko-2 statistics from a text file.
	 * 
	 * @return
	 * 		Glicko-2 statistics.
	 * 
	 * @throws FileNotFoundException
	 * 		Problem while accessing the text file.
	 */
	public static RankingService importGlicko2Statistics() throws FileNotFoundException
	{	RankingService result = new ResultsBasedRankingService();
	
		// open the file
		String path = FilePaths.getGlicko2Path()+File.separator+FileNames.FILE_STATISTICS+FileNames.EXTENSION_TEXT;
		File file = new File(path);
		FileInputStream fileIn = new FileInputStream(file);
		BufferedInputStream inBuff = new BufferedInputStream(fileIn);
		InputStreamReader inSR = new InputStreamReader(inBuff);
		Scanner scanner = new Scanner(inSR);
		
		// read data
		result.importFromText(scanner);
		
		scanner.close();
		return result;
	}
}

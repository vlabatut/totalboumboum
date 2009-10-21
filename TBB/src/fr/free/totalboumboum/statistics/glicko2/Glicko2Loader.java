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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import fr.free.totalboumboum.statistics.glicko2.jrs.RankingService;
import fr.free.totalboumboum.tools.FileTools;

public class Glicko2Loader
{
	public static RankingService loadStatistics() throws IOException, ClassNotFoundException
	{	// init path
		String path = FileTools.getGlicko2Path()+File.separator+FileTools.FILE_STATISTICS+FileTools.EXTENSION_DATA;
		
		// init properties
		System.setProperty("jrs.aveGamesPerPeriod", "5");
		
		// read the rankings
		File file = new File(path);
		FileInputStream fileOut = new FileInputStream(file);
		ObjectInputStream in = new ObjectInputStream(fileOut);
		RankingService result = (RankingService) in.readObject();
		return result;		
		// TODO: if problem while reading the file, should restaure and use the backup 
	}
}

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
import java.io.FileFilter;
import java.io.IOException;

import fr.free.totalboumboum.game.statistics.glicko2.jrs.ResultsBasedRankingService;
import fr.free.totalboumboum.tools.FileTools;

public class Glicko2Init {

	/**
	 * @param args
	 */
	public static void main(String[] args)
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
		try
		{	Glicko2Saver.saveStatistics(rankingService);
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
	}
}

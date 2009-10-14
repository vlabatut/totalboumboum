package fr.free.totalboumboum.game.statistics.glicko2;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;

import fr.free.totalboumboum.tools.FileTools;

import jrs.RankingService;

public class Glicko2Init {

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{	// change ranking properties
		
		// create ranking service
		RankingService rankingService = new RankingService();
		// create round counts
		HashMap<Integer,Integer> roundCounts = new HashMap<Integer, Integer>();
		
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
			roundCounts.put(id,0);
		}
		
		
		// save the rankings
		try
		{	Glicko2Saver.saveStatistics(rankingService,roundCounts);
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
	}
}

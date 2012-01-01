package org.totalboumboum.statistics;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.statistics.glicko2.Glicko2Loader;
import org.totalboumboum.statistics.glicko2.Glicko2Saver;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.statistics.overall.OverallStatsLoader;
import org.totalboumboum.statistics.overall.OverallStatsSaver;
import org.totalboumboum.statistics.overall.PlayerStats;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class StatTools
{	
	public static void main(String[] args) throws IllegalArgumentException, SecurityException, IOException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	//initAllStats();
		//reinitOverallStatsForPlayer("d6aa991b-f803-4074-8259-220e648a8cd5");
		//reinitOverallStatsForPlayer("553c0fb4-5418-4e33-b244-d5f399ae40f2");
		//reinitOverallStatsForPlayer("227b2e4d-bd7b-4153-962b-699bc909e5e1"); // simplet
		//exportAllStats();
		//importAllStats();
	}
	
	/**
	 * initializes both glicko-2 rankings and overall stats, for all profiles
	 */
	public static void initAllStats() throws IllegalArgumentException, SecurityException, IOException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	Glicko2Saver.initGlicko2Statistics();
		OverallStatsSaver.initOverallStatistics();		
	}
	
	/**
	 * reinit overall stats only for the specified profile.
	 * does not change the glicko-2 ranking 
	 * (which can be manually reinitialized from the game GUI anyway)
	 * @param id	the player to reinitialize
	 */
	public static void reinitOverallStatsForPlayer(String id) throws IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException
	{	// load
		HashMap<String,PlayerStats> playersStats = OverallStatsLoader.loadOverallStatistics();
		
		// change
		PlayerStats playerStat = playersStats.get(id);
		playerStat.reset();
		
		// save
		OverallStatsSaver.saveOverallStatistics(playersStats);
	}
	
	public static void exportAllStats() throws IOException, ClassNotFoundException
	{	// load
		HashMap<String,PlayerStats> playersStats = OverallStatsLoader.loadOverallStatistics();
		RankingService glicko2Stats = Glicko2Loader.loadGlicko2Statistics();
		
		// export
		OverallStatsSaver.exportOverallStatistics(playersStats);
		Glicko2Saver.exportGlicko2Statistics(glicko2Stats);
	}
	
	public static void importAllStats() throws IllegalArgumentException, SecurityException, IOException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// import
		HashMap<String,PlayerStats> playersStats = OverallStatsLoader.importOverallStatistics();
		RankingService glicko2Stats = Glicko2Loader.importGlicko2Statistics();
			
		// save
		OverallStatsSaver.saveOverallStatistics(playersStats);
		Glicko2Saver.saveGlicko2Statistics(glicko2Stats);		
	}

}

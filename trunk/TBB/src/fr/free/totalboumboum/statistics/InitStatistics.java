package fr.free.totalboumboum.statistics;

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

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.statistics.glicko2.Glicko2Saver;
import fr.free.totalboumboum.statistics.overall.OverallStatsLoader;
import fr.free.totalboumboum.statistics.overall.OverallStatsSaver;
import fr.free.totalboumboum.statistics.overall.PlayerStats;

public class InitStatistics
{	
	public static void main(String[] args) throws IllegalArgumentException, SecurityException, IOException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	initAllStats();
		//reinitOverallStatsForPlayer(0);
	}
	
	/**
	 * initializes both glicko-2 rankings and overall stats, for all profiles
	 */
	public static void initAllStats() throws IllegalArgumentException, SecurityException, IOException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	Glicko2Saver.init();
		OverallStatsSaver.init();		
	}
	
	/**
	 * reinit overall stats only for the specified profile.
	 * does not change the glicko-2 ranking 
	 * (which can be manually reinitialized from the game GUI anyway)
	 * @param id	the player to reinitialize
	 */
	public static void reinitOverallStatsForPlayer(int id) throws IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException
	{	// load
		HashMap<Integer,PlayerStats> playersStats = OverallStatsLoader.loadStatistics();

		// change
		PlayerStats playerStat = playersStats.get(id);
		playerStat.reset();
		
		// save
		OverallStatsSaver.saveStatistics(playersStats);
	}
}

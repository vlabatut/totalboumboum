package fr.free.totalboumboum.game.statistics;

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

import fr.free.totalboumboum.game.statistics.glicko2.Glicko2Loader;
import fr.free.totalboumboum.game.statistics.glicko2.Glicko2Saver;
import jrs.GameResults;
import jrs.RankingService;
import jrs.ResultsBasedRankingService;

public class GameStatistics
{
	/////////////////////////////////////////////////////////////////
	// FILE ACCESS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static void loadStatistics() throws NumberFormatException, IOException
	{	// glicko2 ranking service
		rankingService = new ResultsBasedRankingService();
		Glicko2Loader.loadStatistics(rankingService);
	}
	
	public static void saveStatistics() throws IOException
	{	Glicko2Saver.saveStatistics(rankingService);
	}

	/////////////////////////////////////////////////////////////////
	// RANKING SERVICE	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static RankingService rankingService;

	public static RankingService getRankingService()
	{	return rankingService;
	}
	
	public void update()
	{	
		
		GameResults gameResults = new GameResults();
		
		// update rankings
		rankingService.postResults(gameResults);
		
		// save new rankings
		try
		{	saveStatistics();
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
	}
}

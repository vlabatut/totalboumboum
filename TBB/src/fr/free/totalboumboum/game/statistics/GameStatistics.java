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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import fr.free.totalboumboum.game.statistics.glicko2.Glicko2Loader;
import fr.free.totalboumboum.game.statistics.glicko2.Glicko2Saver;
import fr.free.totalboumboum.game.statistics.raw.StatisticRound;
import jrs.GameResults;
import jrs.RankingService;

public class GameStatistics
{
	/////////////////////////////////////////////////////////////////
	// FILE ACCESS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static void loadStatistics() throws NumberFormatException, IOException
	{	// glicko2 ranking service
		Glicko2Loader.loadStatistics(rankingService,roundCounts);
	}
	
	public static void saveStatistics() throws IOException
	{	Glicko2Saver.saveStatistics(rankingService,roundCounts);
	}

	/////////////////////////////////////////////////////////////////
	// ROUND COUNT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static final int updatePeriod = 10;
	private static final HashMap<Integer,Integer> roundCounts = new HashMap<Integer, Integer>();
	
	
	/////////////////////////////////////////////////////////////////
	// RANKING SERVICE	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static final RankingService rankingService = new RankingService();

	public static RankingService getRankingService()
	{	return rankingService;
	}
	
	public static void update(StatisticRound stats)
	{	
		// create the game results objects
		GameResults gameResults = new GameResults();
		/*		if(isTeamGame())
		{	Iterator teamNames = teamScores.keySet().iterator();
			while (teamNames.hasNext())
			{	String teamName = (String)teamNames.next();
				Iterator playerIds = getTeam(teamName).getPlayerIds().iterator();
				while (playerIds.hasNext())
				{	String playerId = (String)playerIds.next();
					double playerScore = ((Integer)playerScores.get(playerId)).doubleValue();
					gameResults.addPlayerResults(teamName, playerId,playerScore);
				}
				double teamScore = ((Integer)teamScores.get(teamName)).doubleValue();
				gameResults.setTeamResults(teamName, teamScore);
			}
		}
		else*/
		{	float[] points = stats.getPoints();
			ArrayList<String> players = stats.getPlayers();
			for(int index=0;index<points.length;index++)
			{	int playerId = Integer.parseInt(players.get(index));
				double playerScore = points[index];
				// init game result
				gameResults.addPlayerResults(playerId,playerScore);
				// update round count
				int roundCount = roundCounts.get(playerId);
				roundCount++;
				roundCounts.put(playerId,roundCount);
			}
		}
		
		// update rankings
		rankingService.postResults(gameResults);
		
		// possibly make a service update
		int total = 0;
		for(Entry<Integer,Integer> entry: roundCounts.entrySet())
		{	int roundCount = entry.getValue();
			total = total + roundCount;
		}
		float average = total/(float)roundCounts.size();
		if(average>=updatePeriod)
		{	// update rankings
			rankingService.endPeriod();
			// reinit round counts
			for(Integer id: roundCounts.keySet())
				roundCounts.put(id,0);
		}
		
		// save new rankings
		try
		{	saveStatistics();
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
	}
}

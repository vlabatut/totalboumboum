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
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.statistics.glicko2.Glicko2Loader;
import fr.free.totalboumboum.statistics.glicko2.Glicko2Saver;
import fr.free.totalboumboum.statistics.glicko2.jrs.GameResults;
import fr.free.totalboumboum.statistics.glicko2.jrs.RankingService;
import fr.free.totalboumboum.statistics.raw.StatisticRound;

public class GameStatistics
{
	/////////////////////////////////////////////////////////////////
	// FILE ACCESS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static void loadStatistics() throws IOException, ClassNotFoundException
	{	// glicko2 ranking service
		rankingService = Glicko2Loader.loadStatistics();
	}
	
	public static void saveStatistics() throws IOException, IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	Glicko2Saver.saveStatistics(rankingService);
	}

	/////////////////////////////////////////////////////////////////
	// RANKING SERVICE	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static RankingService rankingService;

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
				gameResults.addPlayerResults(playerId,playerScore);
			}
		}
		
		// update rankings
		rankingService.postResults(gameResults);
		
		// save new rankings
		try
		{	saveStatistics();
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{	e.printStackTrace();
		}
		catch (SecurityException e)
		{	e.printStackTrace();
		}
		catch (ParserConfigurationException e)
		{	e.printStackTrace();
		}
		catch (SAXException e)
		{	e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{	e.printStackTrace();
		}
		catch (NoSuchFieldException e)
		{	e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{	e.printStackTrace();
		}
	}
}

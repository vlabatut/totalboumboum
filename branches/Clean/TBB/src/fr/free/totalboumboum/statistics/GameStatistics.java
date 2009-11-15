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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.statistics.detailed.Score;
import fr.free.totalboumboum.statistics.detailed.StatisticRound;
import fr.free.totalboumboum.statistics.general.OverallStatsLoader;
import fr.free.totalboumboum.statistics.general.OverallStatsSaver;
import fr.free.totalboumboum.statistics.general.PlayerStats;
import fr.free.totalboumboum.statistics.glicko2.Glicko2Loader;
import fr.free.totalboumboum.statistics.glicko2.Glicko2Saver;
import fr.free.totalboumboum.statistics.glicko2.jrs.GameResults;
import fr.free.totalboumboum.statistics.glicko2.jrs.PlayerRating;
import fr.free.totalboumboum.statistics.glicko2.jrs.RankingService;
import fr.free.totalboumboum.tools.CalculusTools;

public class GameStatistics
{
	/////////////////////////////////////////////////////////////////
	// FILE ACCESS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static void loadStatistics() throws IOException, ClassNotFoundException
	{	// overall statistics
		playersStats = OverallStatsLoader.loadStatistics();
		// glicko2 ranking service
		rankingService = Glicko2Loader.loadStatistics();
	}
	
	public static void saveStatistics() throws IOException, IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// overall statistics
		OverallStatsSaver.saveStatistics(playersStats);
		// glicko2 ranking service
		Glicko2Saver.saveStatistics(rankingService);		
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static void update(StatisticRound stats)
	{	// update
		updatePlayersStats(stats);
		updateRankingService(stats);
		
		// save
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
	
	public static void reset() throws IllegalArgumentException, SecurityException, IOException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	resetPlayersStats();
		resetRankingService();
	}

	/**
	 * used when a new profile is created
	 * @param playerId
	 */
	public static void addPlayer(int playerId) throws IllegalArgumentException, SecurityException, IOException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	if(!playersStats.containsKey(playerId))
		{	PlayerStats playerStats = new PlayerStats(playerId);
			playersStats.put(playerId,playerStats);
			rankingService.registerPlayer(playerId);
			saveStatistics();
		}		
	}

	/**
	 * used when a profile is definitely removed
	 * @param playerId
	 */
	public static void deletePlayer(int playerId) throws IllegalArgumentException, SecurityException, IOException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	if(playersStats.containsKey(playerId))
		{	playersStats.remove(playerId);
			rankingService.deregisterPlayer(playerId);
			saveStatistics();
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYERS STATISTICS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static HashMap<Integer,PlayerStats> playersStats = new HashMap<Integer, PlayerStats>();

	public static void resetPlayersStats() throws IllegalArgumentException, SecurityException, IOException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	HashMap<Integer,PlayerStats> playersStats = OverallStatsSaver.init();
		GameStatistics.playersStats = playersStats;
	}
	
	public static HashMap<Integer,PlayerStats> getPlayersStats()
	{	return playersStats;	
	}
	
	public static void updatePlayersStats(StatisticRound stats)
	{	float[] points = stats.getPoints();
		ArrayList<Integer> winners = CalculusTools.getWinners(points);
		
		for(int index=0;index<stats.getPlayersIds().size();index++)
		{	// init
			Integer id = stats.getPlayersIds().get(index);
			PlayerStats playerStats = playersStats.get(id);
			
			// scores
			for(Score score: Score.values())
			{	long scores[] = stats.getScores(score);
				long delta = scores[index];
				long value = playerStats.getScore(score);
				playerStats.setScore(score,value+delta);
			}
			
			// rounds
			long roundsPlayed = playerStats.getRoundsPlayed();
			playerStats.setRoundsPlayed(roundsPlayed+1);
			if(winners.size()==0 || winners.size()==points.length)
			{	long roundsDrawn = playerStats.getRoundsDrawn();
				playerStats.setRoundsDrawn(roundsDrawn+1);
			}
			else if(winners.contains(index))
			{	long roundsWon = playerStats.getRoundsWon();
				playerStats.setRoundsWon(roundsWon+1);
			}
			else
			{	long roundsLost = playerStats.getRoundsLost();
				playerStats.setRoundsLost(roundsLost+1);
			}
		}
	}
	
	public static void updatePreviousRankings()
	{	// put all values to -1 (to handle non-registered players)
		for(PlayerStats playerStats: playersStats.values())
			playerStats.setPreviousRank(-1);
		
		// update ranks according to the actual ranking service values
    	SortedSet<PlayerRating> sortedRatings = rankingService.getSortedPlayerRatings();
    	int rank = 0;
    	Iterator<PlayerRating> it = sortedRatings.iterator();
		while(it.hasNext())
		{	PlayerRating playerRating = it.next();
			rank++;
			int playerId = playerRating.getPlayerId();
			playersStats.get(playerId).setPreviousRank(rank);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// RANKING SERVICE	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static RankingService rankingService;

	public static void resetRankingService() throws IllegalArgumentException, SecurityException, IOException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	RankingService rankingService = Glicko2Saver.init();
		GameStatistics.rankingService = rankingService;
	}
	
	public static RankingService getRankingService()
	{	return rankingService;
	}
	
	public static void updateRankingService(StatisticRound stats)
	{	// create the game results objects
		GameResults gameResults = null;
		/*		if(isTeamGame())  to be adapted
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
		{	List<Integer> players = stats.getPlayersIds();
			// ignore rounds with only one player
			if(players.size()>1)
			{	gameResults = new GameResults();
				float[] points = stats.getPoints();
				Set<Integer> registeredPlayers = rankingService.getPlayers();
				for(int index=0;index<points.length;index++)
				{	int playerId = players.get(index);
					// only consider the registered players
					if(registeredPlayers.contains(playerId))
					{	double playerScore = points[index];
						gameResults.addPlayerResults(playerId,playerScore);
					}
				}
			}
		}
		
		// update rankings
		if(gameResults!=null)
			rankingService.postResults(gameResults);
	}
}

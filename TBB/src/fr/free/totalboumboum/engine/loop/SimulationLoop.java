package fr.free.totalboumboum.engine.loop;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.engine.player.Player;
import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.limit.RoundLimit;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.statistics.GameStatistics;
import fr.free.totalboumboum.statistics.detailed.Score;
import fr.free.totalboumboum.statistics.detailed.StatisticEvent;
import fr.free.totalboumboum.statistics.general.PlayerStats;
import fr.free.totalboumboum.statistics.glicko2.jrs.PlayerRating;
import fr.free.totalboumboum.statistics.glicko2.jrs.RankingService;

public class SimulationLoop extends Loop
{	private static final long serialVersionUID = 1L;

	public SimulationLoop(Round round)
	{	super(round);
	}	

	/////////////////////////////////////////////////////////////////
	// ENGINE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void temp()
	{	// init
		List<Profile> profiles = round.getProfiles();
		HashMap<Integer,PlayerStats> playersStats = GameStatistics.getPlayersStats();
		List<Profile> currentPlayers = new ArrayList<Profile>(profiles);
		HashMap<Integer,HashMap<Score,Long>> currentScores = new HashMap<Integer, HashMap<Score,Long>>();
		
		// process theoretical duration
		long theoreticalDuration = 0;
		for(Profile profile: profiles)
		{	int playerId = profile.getId();
			PlayerStats playerStats = playersStats.get(playerId);
			double totalTime = playerStats.getScore(Score.TIME);
			double roundsPlayed = playerStats.getRoundsPlayed();
			long averageTime = Math.round(totalTime/roundsPlayed);
			theoreticalDuration = theoreticalDuration + averageTime;
		}
		theoreticalDuration = Math.round(theoreticalDuration/(double)profiles.size());
		
		// start the simulation
		long currentTime = 0;
		long milliPeriod = Configuration.getEngineConfiguration().getMilliPeriod();
		while(!isOver())
		{	currentTime = currentTime + milliPeriod;
			for(Profile profile: currentPlayers)
			{	if(profile!=null)
				{	
					
				}
			}
			
			
			round.updateTime(currentTime);
		}
		round.loopOver();
	}
	
	
	
	public void run()
	{	List<Profile> profiles = round.getProfiles();
		HashMap<Integer,PlayerStats> playersStats = GameStatistics.getPlayersStats();
		
		// process the final results		
		List<List<Profile>> metalist = new ArrayList<List<Profile>>();
		List<Profile> temp = new ArrayList<Profile>(profiles);
		metalist.add(temp);
		List<Boolean> flags = new ArrayList<Boolean>();
		flags.add(false);
		processOrder(metalist,flags);
		
		// process the total statistics
		HashMap<Integer,HashMap<Score,Long>> playersScores = new HashMap<Integer,HashMap<Score,Long>>();
		double stdev = 1; //TODO would be better with the actual standard-deviation
		Score scores[] = Score.values();
		for(Profile profile: profiles)
		{	int playerId = profile.getId();
			HashMap<Score,Long> playerScores = new HashMap<Score,Long>();
			playersScores.put(playerId,playerScores);
			PlayerStats playerStats = playersStats.get(playerId);
			for(int i=0;i<scores.length;i++)
			{	Score score = scores[i];
				long total = playerStats.getScore(score);
				Random generator = new Random();
				double z = generator.nextGaussian();
				long value = Math.round(z*stdev + total);
				if(value<0)
					value = 0;
				playerScores.put(score,value);
			}			
		}
		
		// verify consistancy between scores
		long previousTime = Long.MAX_VALUE;
		long totalKills = 0;
		for(int i=0;i<metalist.size();i++)
		{	List<Profile> list = metalist.get(i);
			long groupTime = -1;
			for(int j=0;j<list.size();j++)
			{	Profile profile = list.get(i);
				int playerId = profile.getId();
				HashMap<Score,Long> playerScores = playersScores.get(playerId);
				// death
				long deaths = playerScores.get(Score.BOMBEDS);
				if(i==0)
					deaths = 0;
				else
					deaths = 1;
				playerScores.put(Score.BOMBEDS,deaths);
				// kills
				long kills = playerScores.get(Score.BOMBINGS);
				if(totalKills+kills>profiles.size())
				{	kills = profiles.size() - totalKills;
					playerScores.put(Score.BOMBINGS,kills);
				}
				//bombs
				long bombs = playerScores.get(Score.BOMBS);
				if(bombs<kills)
					playerScores.put(Score.BOMBS,kills);
				// time
				long time = playerScores.get(Score.TIME); //NOTE should be generalized for other points systems
				if(groupTime<0)
				{	if(time>previousTime)
					{	time = previousTime-1;
						playerScores.put(Score.TIME,time);
					}
					groupTime = time;
					previousTime = time;
				}
				else
					playerScores.put(Score.TIME,groupTime);
				// other scores
				playerScores.put(Score.CROWNS,0l);//NOTE temporary;
				playerScores.put(Score.PAINTINGS,0l);//NOTE temporary;
			}
		}
		
		// process the round events
		for(Profile profile: profiles)
		{	int playerId = profile.getId();
			HashMap<Score,Long> playerScores = playersScores.get(playerId);
			// Score.BOMBS
			long playerScore = playerScores.get(Score.BOMBS);
			// Score.ITEMS
			// Score.BOMBEDS
			// Score.BOMBINGS
			// Score.TIME
			// Score.PAINTINGS
			// Score.CROWNS
			
			for(Score score: Score.values())
			{	long playerScore = playerScores.get(score);
				
			}			
		}
		
		StatisticEvent event;
		
		round.addStatisticEvent(event)
	}

	public void playerOut(Player player)
	{	// TODO reuse this stuff VV
		int index = players.indexOf(player);
		round.playerOut(index);
		panel.playerOut(index);	
	}

	private void processOrder(List<List<Profile>> metalist, List<Boolean> flags)
	{	// find a list to process
		int index = flags.indexOf(false);
		List<Profile> list = metalist.get(index);
		flags.set(index,true);
		
		// process the list
		if(list.size()>1)
		{	RankingService rankingService = GameStatistics.getRankingService();
			Iterator<Profile> it = list.iterator();
			Profile p1 = it.next();
			int id1 = p1.getId();
			PlayerRating pr1 = rankingService.getPlayerRating(id1);
			List<Profile> before = new ArrayList<Profile>();
			List<Profile> after = new ArrayList<Profile>();
			while(it.hasNext())
			{	Profile p2 = it.next();
				it.remove();
				int id2 = p2.getId();
				PlayerRating pr2 = rankingService.getPlayerRating(id2);
				// draw ?
				double threshold = rankingService.calculateProbabilityOfDraw(pr1,pr2);
				double proba = Math.random();
				if(proba>threshold)
				{	// win ?
					threshold = rankingService.calculateProbabilityOfWin(pr1,pr2);
					proba = Math.random();
					if(proba<=threshold)
						after.add(p2);
					else
						before.add(p2);
				}
			}
			if(before.size()>0)
			{	metalist.add(index,before);
				flags.add(index,false);
				index++;
			}
			if(after.size()>0)
			{	index++;
				if(index<metalist.size())
				{	metalist.add(index,after);
					flags.add(index,false);
				}
				else
				{	metalist.add(after);
					flags.add(false);
				}
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	super.finish();
		}		
	}	
}

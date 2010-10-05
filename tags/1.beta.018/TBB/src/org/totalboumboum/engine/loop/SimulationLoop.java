package org.totalboumboum.engine.loop;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
import java.util.List;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.detailed.Score;
import org.totalboumboum.statistics.detailed.StatisticAction;
import org.totalboumboum.statistics.detailed.StatisticEvent;
import org.totalboumboum.statistics.glicko2.jrs.PlayerRating;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.statistics.overall.PlayerStats;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class SimulationLoop extends Loop
{	private static final long serialVersionUID = 1L;
	private boolean verbose = false;
	
	public SimulationLoop(Round round)
	{	super(round);
	}	

	/////////////////////////////////////////////////////////////////
	// ENGINE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void run()
	{	// init
		RankingService rankingService = GameStatistics.getRankingService();
		List<Profile> profiles = round.getProfiles();
		HashMap<String,PlayerStats> playersStats = GameStatistics.getPlayersStats();
		List<Profile> currentPlayers = new ArrayList<Profile>(profiles);
		long milliPeriod = Configuration.getEngineConfiguration().getMilliPeriod();
		HashMap<String,HashMap<Score,Long>> currentScores = new HashMap<String, HashMap<Score,Long>>();
		
		// process probabilities and estimate duration
		long theoreticalDuration = 0;
		HashMap<String,HashMap<Score,Double>> probabilities = new HashMap<String, HashMap<Score,Double>>();
		for(Profile profile: profiles)
		{	String playerId = profile.getId();
			PlayerStats playerStats = playersStats.get(playerId);
			// init scores
			HashMap<Score,Long> playerScores = new HashMap<Score, Long>();
			currentScores.put(playerId,playerScores);
			// estimate duration
			double totalTime = playerStats.getScore(Score.TIME);
			double roundsPlayed = playerStats.getRoundsPlayed();
			double averageTime = totalTime/roundsPlayed;
			if(averageTime==0)
				averageTime = 60000;
			theoreticalDuration = Math.max(theoreticalDuration,Math.round(averageTime));
			averageTime = averageTime/milliPeriod;
			// init probas
			HashMap<Score,Double> playerProbas = new HashMap<Score, Double>();
			probabilities.put(playerId,playerProbas);
			for(Score score: Score.values())
			{	double totalScore = playerStats.getScore(score);
				double averageScore = totalScore/roundsPlayed;
				double proba = averageScore/averageTime;
				playerProbas.put(score,proba);
				playerScores.put(score,0l);
			}
		}
		long updatePeriod = theoreticalDuration/100;
		// the meeting probabilities grows non-linearly with time
		double a = 1.0/(theoreticalDuration*theoreticalDuration);
		
		// start the simulation
		long currentTime = 0;
		long previousUpdate = 0;
		while(!isOver())
		{	currentTime = currentTime + milliPeriod;
			if(verbose)
				System.out.println("-------------- "+currentTime+" --------------");			
			if(currentTime>=previousUpdate+updatePeriod)
			{	previousUpdate = previousUpdate+updatePeriod;
				round.simulationStepOver();
				try
				{	Thread.sleep(10);
				}
				catch (InterruptedException e)
				{	e.printStackTrace();
				}				
			}
			double meetingProba = Math.min(1,currentTime*currentTime*a)/100;
			List<Profile> deadPlayers = new ArrayList<Profile>();
			for(Profile profile: currentPlayers)
			{	if(!deadPlayers.contains(profile))
				{	String playerId = profile.getId();
					PlayerRating playerRating = rankingService.getPlayerRating(playerId);
					HashMap<Score,Double> playerProbas = probabilities.get(playerId);
					double p,threshold;
					// items
					p = Math.random();
					threshold = playerProbas.get(Score.ITEMS);
					if(verbose)
						System.out.println("items:"+p+" vs "+threshold);			
					if(p<=threshold)
					{	StatisticEvent event = new StatisticEvent(playerId,StatisticAction.GATHER_ITEM,null,currentTime);
						round.addStatisticEvent(event);
					}
					// bombs
					p = Math.random();
					threshold = playerProbas.get(Score.BOMBS);
					if(verbose)
						System.out.println("bombs:"+p+" vs "+threshold);			
					if(p<=threshold)
					{	StatisticEvent event = new StatisticEvent(playerId,StatisticAction.DROP_BOMB,null,currentTime);
						round.addStatisticEvent(event);
					}
					// meeting
					p = Math.random();
					if(verbose)
						System.out.println("meeting:"+p+" vs "+meetingProba);			
					if(p<=meetingProba)
					{	List<Profile> temp = new ArrayList<Profile>(currentPlayers);
						temp.remove(profile);
						if(temp.size()>0)
						{	int index = (int)(Math.random()*temp.size());
							Profile profile2 = temp.get(index);
							String playerId2 = profile2.getId();
							PlayerRating playerRating2 = rankingService.getPlayerRating(playerId2);
							// draw
							p = Math.random();
							threshold = rankingService.calculateProbabilityOfDraw(playerRating,playerRating2,100);
							if(verbose)
								System.out.println("draw:"+p+" vs "+threshold);			
							if(p>threshold)
							{	// win
								p = Math.random();
								threshold = rankingService.calculateProbabilityOfWin(playerRating,playerRating2);
								if(verbose)
									System.out.println("win:"+p+" vs "+threshold);			
								if(p<=threshold)
								{	StatisticEvent event = new StatisticEvent(playerId,StatisticAction.BOMB_PLAYER,playerId2,currentTime);
									round.addStatisticEvent(event);
									deadPlayers.add(profile2);
									round.playerOut(profiles.indexOf(profile2));
									long items = currentScores.get(playerId2).get(Score.ITEMS);
									for(int i=0;i<items;i++)
									{	event = new StatisticEvent(playerId,StatisticAction.LOSE_ITEM,null,currentTime);
										round.addStatisticEvent(event);
									}
								}
							}
						}
					}
				}
			}
			// remove dead players
			currentPlayers.removeAll(deadPlayers);
			// update round time
			round.updateTime(currentTime);
		}
		round.loopOver();
	}
		
/*	
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
*/
/*	
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
*/	
}

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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.container.itemset.Itemset;
import org.totalboumboum.engine.container.level.hollow.HollowLevel;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.container.level.players.Players;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.feature.action.gather.SpecificGather;
import org.totalboumboum.engine.content.feature.event.ActionEvent;
import org.totalboumboum.engine.content.sprite.hero.Hero;
import org.totalboumboum.engine.content.sprite.hero.HollowHeroFactory;
import org.totalboumboum.engine.content.sprite.hero.HollowHeroFactoryLoader;
import org.totalboumboum.engine.content.sprite.item.Item;
import org.totalboumboum.engine.control.system.ReplaySytemControl;
import org.totalboumboum.engine.control.system.SystemControl;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteCreationEvent;
import org.totalboumboum.engine.player.Player;
import org.totalboumboum.engine.player.PlayerLocation;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.detailed.Score;
import org.totalboumboum.statistics.detailed.StatisticAction;
import org.totalboumboum.statistics.detailed.StatisticEvent;
import org.totalboumboum.statistics.glicko2.jrs.PlayerRating;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.statistics.overall.PlayerStats;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.xml.sax.SAXException;

public class ReplayLoop extends VisibleLoop
{	private static final long serialVersionUID = 1L;
	private boolean verbose = false;
	
	public ReplayLoop(Round round)
	{	super(round);
	}	

	public void init() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, InstantiationException, InvocationTargetException, NoSuchMethodException
	{	// control
		systemControl = new ReplaySytemControl(this);
long start = System.currentTimeMillis();

		// init
		List<Profile> profiles = round.getProfiles();
		HollowLevel hollowLevel = round.getHollowLevel();
		Instance instance = hollowLevel.getInstance();
		RoundVariables.instance = instance;
		RoundVariables.loop = this;

		// load level & instance
		hollowLevel.initLevel(this);
		level = hollowLevel.getLevel();
		RoundVariables.level = level;
		instance.loadFiresetMap();
		instance.loadExplosionset();
		loadStepOver();
		instance.loadBombsetMaps(round.getProfilesColors());
		loadStepOver();
		instance.loadItemset();
		loadStepOver();
		hollowLevel.loadTheme();
		loadStepOver();
		
		// load players : common stuff
		String baseFolder = FilePaths.getInstancesPath()+File.separator+RoundVariables.instance.getName()+File.separator+FileNames.FILE_HEROES;
		HollowHeroFactory base = HollowHeroFactoryLoader.loadBase(baseFolder);
//		loadStepOver();		
		// load players : individual stuff
		int remainingPlayers = profiles.size();
		Players plyrs = hollowLevel.getPlayers();
		PlayerLocation[] initialPositions = plyrs.getLocations().get(remainingPlayers);
		if(round.getRandomLocation())
		{	ArrayList<PlayerLocation> loc = new ArrayList<PlayerLocation>();
			for(int i=0;i<initialPositions.length;i++)
				loc.add(initialPositions[i]);
			Calendar cal = new GregorianCalendar();
			long seed = cal.getTimeInMillis();
			Random random = new Random(seed);
			Collections.shuffle(loc,random);
			for(int i=0;i<initialPositions.length;i++)
				initialPositions[i] = loc.get(i);
		}
		HashMap<String,Integer> items = plyrs.getInitialItems();
		Itemset itemset = instance.getItemset();
		Iterator<Profile> i = profiles.iterator();
		int j=0;
		while(i.hasNext())
		{	// location
			PlayerLocation pl = initialPositions[j];
			Tile tile = level.getTile(pl.getLine(),pl.getCol());
			
			// sprite
			Profile profile = i.next();
			Player player = new Player(profile,base,tile);
			hollowLevel.getInstance().initLinks();
			players.add(player);
			pauseAis.add(false);
			showAiPaths.add(false);
			showAiTileTexts.add(false);
			showAiTileColors.add(false);
			
			// record/transmit event
			SpriteCreationEvent spriteEvent = new SpriteCreationEvent(player.getSprite(),Integer.toString(j));
			RoundVariables.recordEvent(spriteEvent);
			
			// level
			Hero hero = (Hero)player.getSprite();
//			level.addHero(hero,pl.getLine(),pl.getCol());
			
			// initial items
			for(Entry<String,Integer> entry: items.entrySet())
			{	String name = entry.getKey();
				int number = entry.getValue();
				for(int k=0;k<number;k++)
				{	// create item
					Item item = itemset.makeItem(name,tile);
					// add item
					hero.addInitialItem(item);
					// hide item
					SpecificAction action = new SpecificGather(hero,item);
					ActionEvent evt = new ActionEvent(action);
					item.processEvent(evt);
				}
			}
			
			// ai
			player.initAi();
			
			// next player...
			loadStepOver();
			j++;
		}
long end = System.currentTimeMillis();
System.out.println("total load time: "+(end-start));
		
		// init logs
		initLogs();
		// init entries
		initEntries();
	}
	
	public void loadStepOver()
	{	round.loadStepOver();
	}

	/////////////////////////////////////////////////////////////////
	// SYNCH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Lock loadLock = new ReentrantLock();
	private Condition cond = loadLock.newCondition();

	/////////////////////////////////////////////////////////////////
	// ENGINE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void run()
	{	// init
		RankingService rankingService = GameStatistics.getRankingService();
		List<Profile> profiles = round.getProfiles();
		HashMap<Integer,PlayerStats> playersStats = GameStatistics.getPlayersStats();
		List<Profile> currentPlayers = new ArrayList<Profile>(profiles);
		long milliPeriod = Configuration.getEngineConfiguration().getMilliPeriod();
		HashMap<Integer,HashMap<Score,Long>> currentScores = new HashMap<Integer, HashMap<Score,Long>>();
		
		// process probabilities and estimate duration
		long theoreticalDuration = 0;
		HashMap<Integer,HashMap<Score,Double>> probabilities = new HashMap<Integer, HashMap<Score,Double>>();
		for(Profile profile: profiles)
		{	int playerId = profile.getId();
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
				{	int playerId = profile.getId();
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
							int playerId2 = profile2.getId();
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

	/////////////////////////////////////////////////////////////////
	// CELEBRATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void initCelebrationDuration()
	{	setOver(true);
	}

	public void reportVictory(int index)
	{	// useless here		
	}
	
	public void reportDefeat(int index)
	{	// useless here		
	}

	/////////////////////////////////////////////////////////////////
	// SIMULATED		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public boolean isSimulated()
	{	return false;		
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

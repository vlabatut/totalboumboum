package org.totalboumboum.engine.loop;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.ai.AisConfiguration;
import org.totalboumboum.engine.container.bombset.Bombset;
import org.totalboumboum.engine.container.bombset.BombsetMap;
import org.totalboumboum.engine.container.itemset.Itemset;
import org.totalboumboum.engine.container.level.hollow.HollowLevel;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.container.level.players.Players;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.ability.ActionAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.feature.action.appear.SpecificAppear;
import org.totalboumboum.engine.content.feature.action.drop.SpecificDrop;
import org.totalboumboum.engine.content.feature.action.gather.SpecificGather;
import org.totalboumboum.engine.content.feature.event.ActionEvent;
import org.totalboumboum.engine.content.feature.event.EngineEvent;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;
import org.totalboumboum.engine.content.sprite.hero.Hero;
import org.totalboumboum.engine.content.sprite.hero.HollowHeroFactory;
import org.totalboumboum.engine.content.sprite.hero.HollowHeroFactoryLoader;
import org.totalboumboum.engine.content.sprite.item.Item;
import org.totalboumboum.engine.loop.display.Display;
import org.totalboumboum.engine.loop.display.ais.DisplayAisColors;
import org.totalboumboum.engine.loop.display.ais.DisplayAisPaths;
import org.totalboumboum.engine.loop.display.ais.DisplayAisPause;
import org.totalboumboum.engine.loop.display.ais.DisplayAisRecordPercepts;
import org.totalboumboum.engine.loop.display.ais.DisplayAisTexts;
import org.totalboumboum.engine.loop.display.game.DisplayCancel;
import org.totalboumboum.engine.loop.display.game.DisplayFPS;
import org.totalboumboum.engine.loop.display.game.DisplayMessage;
import org.totalboumboum.engine.loop.display.misc.DisplayScreenCapture;
import org.totalboumboum.engine.loop.display.player.DisplayPlayersNames;
import org.totalboumboum.engine.loop.display.sprites.DisplaySprites;
import org.totalboumboum.engine.loop.display.sprites.DisplaySpritesPositions;
import org.totalboumboum.engine.loop.display.tiles.DisplayGrid;
import org.totalboumboum.engine.loop.display.tiles.DisplayTilesPositions;
import org.totalboumboum.engine.loop.display.time.DisplaySpeed;
import org.totalboumboum.engine.loop.display.time.DisplaySpeedChange;
import org.totalboumboum.engine.loop.display.time.DisplayTime;
import org.totalboumboum.engine.loop.display.usage.DisplayEffectiveUsage;
import org.totalboumboum.engine.loop.display.usage.DisplayRealtimeUsage;
import org.totalboumboum.engine.loop.event.replay.StopReplayEvent;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteCreationEvent;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteInsertionEvent;
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.engine.player.AiPlayer;
import org.totalboumboum.engine.player.PlayerLocation;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.xml.sax.SAXException;

/**
 * This class is used to represent all forms
 * of loops executed locally, i.e. on the host machine.
 * This includes {@link RegularLoop} and {@link ServerLoop}.
 * 
 * @author Vincent Labatut
 */
public abstract class LocalLoop extends VisibleLoop implements InteractiveLoop
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Builds a new local loop.
	 * 
	 * @param round
	 * 		Round to be displayed by this loop.
	 */
	public LocalLoop(Round round)
	{	super(round);
	}	
	
	/////////////////////////////////////////////////////////////////
	// INITIALIZING		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void load() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, InstantiationException, InvocationTargetException, NoSuchMethodException, URISyntaxException
	{	// init
		List<Profile> profiles = round.getProfiles();
		HollowLevel hollowLevel = round.getHollowLevel();
		Instance instance = hollowLevel.getInstance();
		RoundVariables.instance = instance;
		RoundVariables.loop = this;

		// load level & instance
		hollowLevel.initLevel(this);
		RoundVariables.writeZoomCoef(RoundVariables.zoomFactor);
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
		hollowLevel.instanciateZone();
		loadStepOver();
		
		// load players base
		String baseFolder = FilePaths.getInstancesPath()+File.separator+RoundVariables.instance.getName()+File.separator+FileNames.FILE_HEROES;
		HollowHeroFactory base = HollowHeroFactoryLoader.loadBase(baseFolder);
		
		// place players
		int remainingPlayers = profiles.size();
		Players plyrs = hollowLevel.getPlayers();
		PlayerLocation[] initialPositions = plyrs.getLocations().get(remainingPlayers);
		if(round.getRandomLocation())
		{	List<PlayerLocation> loc = new ArrayList<PlayerLocation>();
			for(int i=0;i<initialPositions.length;i++)
				loc.add(initialPositions[i]);
			Calendar cal = new GregorianCalendar();
			long seed = cal.getTimeInMillis();
			Random random = new Random(seed);
			Collections.shuffle(loc,random);
			for(int i=0;i<initialPositions.length;i++)
				initialPositions[i] = loc.get(i);
		}
		
		// create sprites and stuff
		HashMap<String,Integer> items = plyrs.getInitialItems();
		Itemset itemset = instance.getItemset();
		Iterator<Profile> i = profiles.iterator();
		int j=0;
		while(i.hasNext())
		{	// location
			PlayerLocation pl = initialPositions[j];
			Tile tile = level.getTile(pl.getRow(),pl.getCol());
			
			// sprite
			Profile profile = i.next();
			AbstractPlayer player = initPlayer(profile,base,tile);
			hollowLevel.getInstance().initLinks();
			players.add(player);
			pauseAis.add(false);
			recordAiPercepts.add(false);
			lastActionAis.add(0l);
			
			// record/transmit creation event
			SpriteCreationEvent creationEvent = new SpriteCreationEvent(player.getSprite(),Integer.toString(j));
			RoundVariables.writeEvent(creationEvent);
			// record/transmit insertion event
			SpriteInsertionEvent insertionEvent = new SpriteInsertionEvent(player.getSprite());
			RoundVariables.writeEvent(insertionEvent);
			
			// level
			Hero hero = (Hero)player.getSprite();
//			level.addHero(hero,pl.getRow(),pl.getCol());
			
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
			
			// next player...
			loadStepOver();
			j++;
		}

		// separation event
		StopReplayEvent event = new StopReplayEvent();
		RoundVariables.writeEvent(event);
	}
	
	@Override
	protected void startLoopInit()
	{	super.startLoopInit();
		
		initAis();
//System.out.println("Agents initialized"); // TODO >> bloquer jusqu'à ce que chaque agent ait renvoyé son résultat ?
		initCycleHistory();
	}
	
	/////////////////////////////////////////////////////////////////
	// CANCELATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateCancel()
	{	if(isCanceled())
		{	round.cancelGame();
			setCanceled(false);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// LOGS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Objects containing all agent-related options */
	private AisConfiguration aisConfiguration = Configuration.getAisConfiguration();
	
	@Override
	protected void initLogs()
	{	super.initLogs();
		if(aisConfiguration.getLogExceptions())
		{	try
			{	aisConfiguration.initExceptionsLogStream();
				OutputStream out = aisConfiguration.getExceptionsLogOutput();
				PrintWriter printWriter = new PrintWriter(out,true);
				// start date/time
				Calendar cal = new GregorianCalendar();
				Date startDate = cal.getTime();
				DateFormat dateFormat = DateFormat.getDateTimeInstance();
				printWriter.println("Start: "+dateFormat.format(startDate));
				// players
				printWriter.println("Players: ");
				for(AbstractPlayer player: players)
				{	String id = player.getId();
					String name = player.getName();
					String color = player.getColor().toString();
					String type = "Human player";
					if(player instanceof AiPlayer)
						type = "AI player";
					printWriter.println("\t("+id+") "+name+" ["+color+"] - "+type);
				}
			}
			catch (FileNotFoundException e)
			{	e.printStackTrace();
			}			
		}
	}
	
	@Override
	protected void updateLogs()
	{	super.updateLogs();
		if(aisConfiguration.getLogExceptions())
		{	OutputStream out = aisConfiguration.getExceptionsLogOutput();
			PrintWriter printWriter = new PrintWriter(out,true);
			printWriter.println("--"+totalGameTime+"ms --------------------------");
		}
	}
	
	@Override
	protected void closeLogs()
	{	super.closeLogs();
		if(aisConfiguration.getLogExceptions())
		{	try
			{	aisConfiguration.closeExceptionsLogStream();
			}
			catch(IOException e)
			{	e.printStackTrace();
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// AIS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates which agents are currently paused */
	protected final List<Boolean> pauseAis = new ArrayList<Boolean>();
	/** Indicates the time of the last action of each agent */
	protected final List<Long> lastActionAis = new ArrayList<Long>();
	/** Indicates which agent percepts should be recorded at the next update */
	protected final List<Boolean> recordAiPercepts = new ArrayList<Boolean>();
	
	@Override
	public void switchAiPause(int index)
	{	debugLock.lock();
		if(index<pauseAis.size())
		{	boolean pause = pauseAis.get(index);
			if(pause)
				pause = false;
			else if(players.get(index) instanceof AiPlayer)
				pause = true;
			pauseAis.set(index,pause);
		}
		debugLock.unlock();
	}
	
	@Override
	public boolean getAiPause(int index)
	{	boolean result;
		debugLock.lock();
		result = pauseAis.get(index);
		debugLock.unlock();
		return result;
	}
	
	/**
	 * Updates all agent players depending
	 * on the last actions they returned.
	 * Also implements in-game process such
	 * as bombing idle agents.
	 * 
	 * @throws IOException 
	 * 		Problem while capturing some agent percepts.
	 */
	protected void updateAis() throws IOException
	{	
//		if(gameStarted) // only after the round has started
		{	aiTime = aiTime + milliPeriod;
			boolean active[] = new boolean[players.size()];
			Arrays.fill(active,false);
			AisConfiguration aiConfig = Configuration.getAisConfiguration();
			
			// simple update
			if(aiTime >= aiConfig.getAiPeriod())
			{	aiTime = 0;
				for(int i=0;i<players.size();i++) //TODO would be good to actually have a list of ai players, processing it again each time is useless
				{	AbstractPlayer player = players.get(i);
					if(!player.isOut() && player instanceof AiPlayer)
					{	boolean aiPause = getAiPause(i);
						active[i] = ((AiPlayer)player).updateAi(aiPause);
//System.out.println(player.getName()+":"+active[i]);
					}
				}
			}
			
			// bomb idle ais
			for(int i=0;i<players.size();i++)
			{	AbstractPlayer player = players.get(i);
				if(!player.isOut() && player instanceof AiPlayer)
				{	boolean aiPause = getAiPause(i);
					if(!aiPause)
					{	long lastAction = lastActionAis.get(i);
//System.out.println(i+": "+lastAction);
						if(active[i])
							lastAction = 0;
						else
							lastAction = lastAction + milliPeriod;
						long bombUselessAis = aiConfig.getBombUselessAis();
						// we want slightly different times to avoid draws
						double imprecision = (Math.random()-0.5)/5;
						bombUselessAis = (long)(bombUselessAis*(1+imprecision));
						if(bombUselessAis>0 && lastAction>=bombUselessAis)
						{	Tile tile = player.getSprite().getTile();
							boolean success = dropLevelBomb(tile);
							if(success)
								lastAction = 0;
						}
						lastActionAis.set(i,lastAction);
					}
				}
			}
		}
	}
	
	/**
	 * Tries to drop a bomb in the level's name.
	 * If possible, the bomb is dropped in the 
	 * specified tile and the method returns {@code true}.
	 * Otherwise, the method just returns {@code false}
	 * and no bomb is dropped at all.
	 * <br/>
	 * The bomb dropped by this method has a 0 range.
	 *  
	 * @param tile
	 * 		Targeted tile.
	 * @return
	 * 		{@code true} iff a bomb could be dropped.
	 */
	protected boolean dropLevelBomb(Tile tile)
	{	// we occasionally increase the range
		int range = 0;
		double extraRangeProba = Math.random();
		if(extraRangeProba>0.95)
			range = 1;

		// drop it
		boolean result = dropLevelBomb(tile,range);
		return result;
	}
	
	/**
	 * Tries to drop a bomb in the level's name.
	 * If possible, the bomb is dropped in the 
	 * specified tile and the method returns {@code true}.
	 * Otherwise, the method just returns {@code false}
	 * and no bomb is dropped at all.
	 *  
	 * @param tile
	 * 		Targeted tile.
	 * @param range
	 * 		Bomb range.
	 * @return
	 * 		{@code true} iff a bomb could be dropped.
	 */
	protected boolean dropLevelBomb(Tile tile, int range)
	{	boolean result = false;

		// duration
		int duration = 2500;
		
		// drop bomb
		HollowLevel hollowLevel = round.getHollowLevel();
		BombsetMap bombsetMap = hollowLevel.getInstance().getBombsetMap();
		Bombset bombset = bombsetMap.getBombset(null);
		Bomb bomb = bombset.makeBomb(null,tile,range,duration);
		SpecificAction appearAction = new SpecificAppear(bomb);
		ActionAbility actionAbility = bomb.modulateAction(appearAction);
		if(actionAbility.isActive())
		{	level.insertSpriteTile(bomb);
			SpriteInsertionEvent event = new SpriteInsertionEvent(bomb);
			RoundVariables.writeEvent(event);
			//
			SpecificDrop dropAction = new SpecificDrop(tile,bomb);
			ActionEvent evt = new ActionEvent(dropAction);
			bomb.processEvent(evt);
			result = true;
		}
		return result;
	}
	
	/**
	 * Initializes all agent players.
	 */
	protected void initAis()
	{	for(int i=0;i<players.size();i++)
		{	AbstractPlayer player = players.get(i);
			if(player instanceof AiPlayer)
				((AiPlayer)player).initAi();
		}
	}
	
	/**
	 * Sets up the recording of the agent percepts,
	 * for the player whose number is specified
	 * as a parameter..
	 * 
	 * @param index
	 * 		Player (must be an agent) whose percepts are to be recorded.
	 * @param rec 
	 * 		{@code true} for recording.
	 */
	public void switchRecordAiPercepts(int index, boolean rec)
	{	debugLock.lock();
		if(index<recordAiPercepts.size() && players.get(index) instanceof AiPlayer)
			recordAiPercepts.set(index,rec);
		debugLock.unlock();
	}
	
	/**
	 * Indicates if the agent whose index is specified
	 * as a parameter should have its percepts recorded.
	 * 
	 * @param index
	 * 		Index of the concerned agent.
	 * @return
	 * 		{@code true} iff the agent percepts must be recorded.
	 */
	public boolean getRecordAiPercepts(int index)
	{	boolean result;
		debugLock.lock();
		result = recordAiPercepts.get(index);
		debugLock.unlock();
		return result;
	}

	/**
	 * Record the percepts of the agents
	 * when requested.
	 * 
	 * @throws IOException
	 * 		Problem while writing the percept files.
	 */
	protected void recordAis() throws IOException
	{	for(int i=0;i<players.size();i++)
		{	AbstractPlayer player = players.get(i);
			if(!player.isOut() && player instanceof AiPlayer)
			{	boolean recordPercepts = getRecordAiPercepts(i);
				if(recordPercepts)
				{	((AiPlayer)player).recordAi();
					switchRecordAiPercepts(i, false);
				}
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// CYCLES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List used to monitor the position of players and detect cycles */
	private List<LinkedList<Tile>> cycleHistory = null;
	/** Maximal size of the tile lists used to monitor cycles */
	private final static int CYCLE_SIZE = 15;
	
	/**
	 * Initializes the structure used to monitor player cycles.
	 */
	private void initCycleHistory()
	{	cycleHistory = new ArrayList<LinkedList<Tile>>();
		for(int i=0;i<players.size();i++)
		{	LinkedList<Tile> list = new LinkedList<Tile>();
			cycleHistory.add(list);
		}
	}
	
	/**
	 * Updates the structure monitoring player cycles,
	 * and possible sends a bomb on the players involved
	 * in cycles.
	 */
	protected void updateCycleHistory()
	{	AisConfiguration aiConfig = Configuration.getAisConfiguration();
		if(aiConfig.getBombCyclingAis())
		{	for(int i=0;i<players.size();i++)
			{	// retrieve data
				AbstractPlayer player = players.get(i);
				Hero hero = player.getSprite();
				Tile tile = hero.getTile();
				LinkedList<Tile> tiles = cycleHistory.get(i);
				
				// update cycle list
				if(tiles.isEmpty())
					tiles.add(tile);
				else
				{	Tile t = tiles.getLast();
					// check if the last tile is different
					if(!t.equals(tile))
					{	if(tiles.size()==1)
							tiles.add(tile);
					
						else
						{	// ignore aligned tiles (to simplify the search for cycles)
							Tile prev0 = tiles.pollLast();
							Tile prev1 = tiles.getLast();
							if(!level.areOrderAlignedTiles(prev1,prev0,tile) || prev1.equals(tile))
								tiles.addLast(prev0);
							tiles.addLast(tile);
							
							// possibly remove the first tile to keep the queue size constant
							if(tiles.size()>CYCLE_SIZE)
								tiles.removeFirst();
		
//System.out.println(player.getColor()+": "+tiles.toString());			
							// check for cycles
							if(tiles.size()==CYCLE_SIZE)
							{	for(int c=3;c<=5;c++)
								{	// find a cycle
									boolean hasCycle = lookForCycle(tiles, c);
									// possibly bomb the concerned player
									if(hasCycle)
									{	boolean result = dropLevelBomb(tile);
										if(result)
										{	tiles.clear();
//System.out.println(player.getColor()+": cycle of size c="+c+" detected");			
										}
									}
								}
							}
						}
					}
				}
			}
		}
//System.out.println("---------------------------------");			
	}
	
	/**
	 * Checks if the specified list contains a cycle
	 * of the specified length. What we call cycle here
	 * is the repetition of a sequence of tiles. The search
	 * starts from the end of the list (most recent tiles).
	 *  
	 * @param tiles
	 * 		List of tiles.
	 * @param length
	 * 		Length of the targeted cycle.
	 * @return
	 * 		{@code true} iff a cycle was found.
	 */
	private boolean lookForCycle(LinkedList<Tile> tiles, int length)
	{	boolean result = false;
		
		// we try identifying a cycle starting from the end of the sequence
		int i = tiles.size()-1;
//		while(!result && i>=2*length-1)
		{	// we look for the maximum of repetitions
			result = true;
			int j=i-length;
			while(result && j>=length-1)
			{	int k = 0;
				boolean similar = true;
				while(similar && k<length)
				{	Tile tRef = tiles.get(i-k);
					Tile tComp = tiles.get(j-k);
					similar = tRef.equals(tComp);
					k++;
				}
				if(!similar)
					result = false;
				else
					j--;
			}
			
//			i--;
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** AIs current time, used to inforce AIs period defined in the game options */
	private long aiTime = 0;

	@Override
	protected void initTimes()
	{	super.initTimes();
		aiTime = 0;
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY MANAGER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initDisplayManager()
	{	Display display;
	
		display = new DisplayMessage(this);
		displayManager.addDisplay(display);

		// grid positions
		display = new DisplayGrid(this);
		displayManager.addDisplay(display);

		// tiles positions
		display = new DisplayTilesPositions(this);
		displayManager.addDisplay(display);

		// sprites positions
		display = new DisplaySpritesPositions(this);
		displayManager.addDisplay(display);

		// sprites
		display = new DisplaySprites(this);
		displayManager.addDisplay(display);
	
		// AIs paths
		display = new DisplayAisPaths(this);
		displayManager.addDisplay(display);
		// AIs colors
		display = new DisplayAisColors(this);
		displayManager.addDisplay(display);
		// AIs texts
		display = new DisplayAisTexts(this);
		displayManager.addDisplay(display);
		// AIs pauses
		display = new DisplayAisPause(this);
		displayManager.addDisplay(display);
		// AIs record percepts
		display = new DisplayAisRecordPercepts(this);
		displayManager.addDisplay(display);
		// AIs effective usage
		display = new DisplayEffectiveUsage(this);
		displayManager.addDisplay(display);
		// AIs real-time usage
		display = new DisplayRealtimeUsage(this);
		displayManager.addDisplay(display);
		
		// players names
		display = new DisplayPlayersNames(this);
		displayManager.addDisplay(display);
		
		// speed
		display = new DisplaySpeed();
		displayManager.addDisplay(display);
		// change speed
		display = new DisplaySpeedChange();
		displayManager.addDisplay(display);
		
		// time
		display = new DisplayTime(this);
		displayManager.addDisplay(display);
		
		// FPS
		display = new DisplayFPS(this);
		displayManager.addDisplay(display);
		
		// cancel
		display = new DisplayCancel();
		displayManager.addDisplay(display);
		
		// screen capture
		display = new DisplayScreenCapture();
		displayManager.addDisplay(display);
	}
	
	/////////////////////////////////////////////////////////////////
	// SUDDEN DEATH		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Performs all updates related
	 * to sudden death.
	 */
	public void updateSuddenDeath()
	{	HollowLevel hollowLevel = round.getHollowLevel();
		hollowLevel.applySuddenDeath(totalGameTime);
	}
	
	/////////////////////////////////////////////////////////////////
	// CELEBRATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Variable used to track celebration duration */
	double celebrationDuration = -1;

	@Override
	public void initCelebration()
	{	if(players.size()>0)
		{	AbstractPlayer player = players.get(0);
			Sprite sprite = player.getSprite();
			StateAbility ability = sprite.modulateStateAbility(StateAbilityName.HERO_CELEBRATION_DURATION);
			celebrationDuration = ability.getStrength();
		}
		else
			celebrationDuration = 1;
		if(celebrationDuration>=0)
			gameOver = true;
	}
	
	/**
	 * Updates the celebration animation.
	 */
	protected void updateCelebration()
	{	if(celebrationDuration>0)
		{	celebrationDuration = celebrationDuration - (milliPeriod*Configuration.getEngineConfiguration().getSpeedCoeff());
			if(celebrationDuration<=0)
				setOver(true);
		}
	}
		
	@Override
	public void reportVictory(int index)
	{	AbstractPlayer player = players.get(index);
		Sprite sprite = player.getSprite();
		EngineEvent event = new EngineEvent(EngineEvent.CELEBRATION_VICTORY);
		sprite.processEvent(event);
	}
	
	@Override
	public void reportDefeat(int index)
	{	AbstractPlayer player = players.get(index);
		Sprite sprite = player.getSprite();
		EngineEvent event = new EngineEvent(EngineEvent.CELEBRATION_DEFEAT);
		sprite.processEvent(event);
	}
}

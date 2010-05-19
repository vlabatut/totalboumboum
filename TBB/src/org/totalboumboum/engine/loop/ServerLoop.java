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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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

import org.totalboumboum.ai.AbstractAiManager;
import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.ai.AisConfiguration;
import org.totalboumboum.configuration.engine.EngineConfiguration;
import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.container.itemset.Itemset;
import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.level.hollow.HollowLevel;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.container.level.players.Players;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.feature.action.gather.SpecificGather;
import org.totalboumboum.engine.content.feature.event.ActionEvent;
import org.totalboumboum.engine.content.feature.event.EngineEvent;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.hero.Hero;
import org.totalboumboum.engine.content.sprite.hero.HollowHeroFactory;
import org.totalboumboum.engine.content.sprite.hero.HollowHeroFactoryLoader;
import org.totalboumboum.engine.content.sprite.item.Item;
import org.totalboumboum.engine.control.SystemControl;
import org.totalboumboum.engine.loop.event.sprite.SpriteCreationEvent;
import org.totalboumboum.engine.player.Player;
import org.totalboumboum.engine.player.PlayerLocation;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.calculus.CalculusTools;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.time.TimeUnit;
import org.xml.sax.SAXException;

public class ServerLoop extends Loop
{	private static final long serialVersionUID = 1L;
	
	public ServerLoop(Round round)
	{	super(round);
	}	
	
	public void init() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, InstantiationException, InvocationTargetException, NoSuchMethodException
	{	// control
		systemControl = new SystemControl(this);
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
	// CONTROL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private SystemControl systemControl;

	/////////////////////////////////////////////////////////////////
	// LOGS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private EngineConfiguration engineConfiguration = Configuration.getEngineConfiguration();
	private AisConfiguration aisConfiguration = Configuration.getAisConfiguration();
	
	private void initLogs()
	{	if(engineConfiguration.getLogControls())
		{	try
			{	engineConfiguration.initControlsLogStream();
				OutputStreamWriter out = new OutputStreamWriter(engineConfiguration.getControlsLogOutput(),"UTF8");
				PrintWriter printWriter = new PrintWriter(out,true);
				// start date/time
				Calendar cal = new GregorianCalendar();
				Date startDate = cal.getTime();
				DateFormat dateFormat = DateFormat.getDateTimeInstance();
				printWriter.println("Start: "+dateFormat.format(startDate));
				// players
				printWriter.println("Players: ");
				for(Player player: players)
				{	int id = player.getId();
					String name = player.getName();
					String color = player.getColor().toString();
					String type = "Human player";
					if(player.hasAi())
						type = "AI player";
					printWriter.println("\t("+id+") "+name+" ["+color+"] - "+type);
				}
			}
			catch (FileNotFoundException e)
			{	e.printStackTrace();
			}
			catch (UnsupportedEncodingException e)
			{	e.printStackTrace();
			}			
		}
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
				for(Player player: players)
				{	int id = player.getId();
					String name = player.getName();
					String color = player.getColor().toString();
					String type = "Human player";
					if(player.hasAi())
						type = "AI player";
					printWriter.println("\t("+id+") "+name+" ["+color+"] - "+type);
				}
			}
			catch (FileNotFoundException e)
			{	e.printStackTrace();
			}			
		}
	}
	
	private void updateLogs()
	{	if(engineConfiguration.getLogControls())
		{	try
			{	OutputStreamWriter out = new OutputStreamWriter(engineConfiguration.getControlsLogOutput(),"UTF8");
				PrintWriter printWriter = new PrintWriter(out,true);
				printWriter.println("--"+totalGameTime+"ms --------------------------");
			}
			catch (UnsupportedEncodingException e)
			{	e.printStackTrace();
			}
		}
		if(aisConfiguration.getLogExceptions())
		{	OutputStream out = aisConfiguration.getExceptionsLogOutput();
			PrintWriter printWriter = new PrintWriter(out,true);
			printWriter.println("--"+totalGameTime+"ms --------------------------");
		}
	}
	
	private void closeLogs()
	{	if(engineConfiguration.getLogControls())
		{	try
			{	engineConfiguration.closeControlsLogStream();
			}
			catch(IOException e)
			{	e.printStackTrace();
			}
		}
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
	// DEBUG			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean showGrid = false;
	private int showTilesPositions = 0;
	private int showSpritesPositions = 0;
	private boolean showSpeed = false;
	private int showTime = 0;
	private boolean showFPS = false;
	private boolean showNames = false;
	private boolean pauseEngine = false;
	private boolean stepEngine = false;
	private final ArrayList<Boolean> pauseAis = new ArrayList<Boolean>();
	private final ArrayList<Boolean> showAiPaths = new ArrayList<Boolean>();
	private final ArrayList<Boolean> showAiTileTexts = new ArrayList<Boolean>();
	private final ArrayList<Boolean> showAiTileColors = new ArrayList<Boolean>();
	private Lock debugLock = new ReentrantLock();

	public void setShowGrid(boolean showGrid)
	{	debugLock.lock();
		this.showGrid = showGrid;
		debugLock.unlock();
	}
	public boolean getShowGrid()
	{	boolean result;
		debugLock.lock();
		result = showGrid;
		debugLock.unlock();
		return result;
	}

	public void switchShowFPS()
	{	debugLock.lock();
		showFPS = !showFPS;
		debugLock.unlock();
	}
	public boolean getShowFPS()
	{	boolean result;
		debugLock.lock();
		result = showFPS;
		debugLock.unlock();
		return result;
	}

	public void switchShowSpeed()
	{	debugLock.lock();
		showSpeed = !showSpeed;		
		debugLock.unlock();
	}
	public boolean getShowSpeed()
	{	boolean result;
		debugLock.lock();
		result = showSpeed;
		debugLock.unlock();
		return result;
	}

	public void switchShowTime()
	{	debugLock.lock();
		showTime = (showTime+1)%4;
		debugLock.unlock();
	}
	public int getShowTime()
	{	int result;
		debugLock.lock();
		result = showTime;
		debugLock.unlock();
		return result;
	}

	public void switchShowNames()
	{	debugLock.lock();
		showNames = !showNames;		
		debugLock.unlock();
	}
	public boolean getShowNames()
	{	boolean result;
		debugLock.lock();
		result = showNames;
		debugLock.unlock();
		return result;
	}

	public void switchShowTilesPositions()
	{	debugLock.lock();
		showTilesPositions = (showTilesPositions+1)%3;
		debugLock.unlock();
	}
	public int getShowTilesPositions()
	{	int result;
		debugLock.lock();
		result = showTilesPositions;
		debugLock.unlock();
		return result;
	}

	public void switchShowSpritesPositions()
	{	debugLock.lock();
		showSpritesPositions = (showSpritesPositions+1)%3;
		debugLock.unlock();
	}
	public int getShowSpritesPositions()
	{	int result;
		debugLock.lock();
		result = showSpritesPositions;
		debugLock.unlock();
		return result;
	}

	public void switchEnginePause()
	{	debugLock.lock();
		pauseEngine = !pauseEngine;		
		debugLock.unlock();
	}
	public boolean getEnginePause()
	{	boolean result;
		debugLock.lock();
		result = pauseEngine;
		debugLock.unlock();
		return result;
	}

	public void switchEngineStep(boolean value)
	{	debugLock.lock();
		stepEngine = value;		
		debugLock.unlock();
	}
	public boolean getEngineStep()
	{	boolean result;
		debugLock.lock();
		result = stepEngine;
		debugLock.unlock();
		return result;
	}

	public void switchAiPause(int index)
	{	debugLock.lock();
		if(index<pauseAis.size())
		{	boolean pause = pauseAis.get(index);
			if(pause)
				pause = false;
			else if(players.get(index).getArtificialIntelligence()!=null)
				pause = true;
			pauseAis.set(index,pause);
		}
		debugLock.unlock();
	}
		
	public boolean getAiPause(int index)
	{	boolean result;
		debugLock.lock();
		result = pauseAis.get(index);
		debugLock.unlock();
		return result;
	}

	public void switchShowAiPaths(int index)
	{	debugLock.lock();
		if(index<showAiPaths.size())
		{	boolean show = showAiPaths.get(index);
			if(players.get(index).getArtificialIntelligence()!=null)
				show = !show;
			showAiPaths.set(index,show);
		}
		debugLock.unlock();
	}
	public void switchShowAiTileTexts(int index)
	{	debugLock.lock();
		if(index<showAiTileTexts.size())
		{	boolean show = showAiTileTexts.get(index);
			if(players.get(index).getArtificialIntelligence()!=null)
				show = !show;
			showAiTileTexts.set(index,show);
		}
		debugLock.unlock();
	}
	public void switchShowAiTileColors(int index)
	{	debugLock.lock();
		if(index<showAiTileColors.size())
		{	boolean show = showAiTileColors.get(index);
			if(players.get(index).getArtificialIntelligence()!=null)
				show = !show;
			showAiTileColors.set(index,show);
		}
		debugLock.unlock();
	}
		
	public boolean getShowAiPaths(int index)
	{	boolean result;
		debugLock.lock();
		result = showAiPaths.get(index);
		debugLock.unlock();
		return result;
	}
	public boolean getShowAiTileTexts(int index)
	{	boolean result;
		debugLock.lock();
		result = showAiTileTexts.get(index);
		debugLock.unlock();
		return result;
	}
	public boolean getShowAiTileColors(int index)
	{	boolean result;
		debugLock.lock();
		result = showAiTileColors.get(index);
		debugLock.unlock();
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// GRAPHICS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	transient private LoopRenderPanel panel;

	public void setPanel(LoopRenderPanel panel)
	{	loadLock.lock();
		this.panel = panel;
		// system listener
		panel.addKeyListener(systemControl);
		// players listeners
		Iterator<Player> i = players.iterator();
		while(i.hasNext())
		{	Player player = i.next();
			panel.addKeyListener(player.getSpriteControl());
		}
		// waking the process thread up
		cond.signal();
		loadLock.unlock();	
	}
	public LoopRenderPanel getPanel()
	{	return panel;
	}

	/////////////////////////////////////////////////////////////////
	// ENGINE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** total game time elapsed since the players took control */
	private long totalGameTime = 0;
	/** total real time elapsed since the level started appearing */
	private long totalEngineTime = 0;
	/** AIs current time, used to inforce AIs period defined in the game options */
	private long aiTime = 0;
	/** 
	 * Number of frames with a delay of 0 ms before the animation thread yields
	 * to other running threads. 
	 */
	private static final int NO_DELAYS_PER_YIELD = 16;
	/** 
	 * no. of frames that can be skipped in any one animation loop
	 * i.e the games state is updated but not rendered
	 */ 
	@SuppressWarnings("unused")
	private static int MAX_FRAME_SKIPS = 5;
	/** game period expressed un milliseconds */
	private long milliPeriod;
	private Lock loopLock = new ReentrantLock();
	/** indicates if the game has been canceled */
	private boolean isCanceled = false;

	public void setCanceled(boolean isCanceled)
	{	loopLock.lock();
		this.isCanceled = isCanceled;
		loopLock.unlock();
	}
	public boolean isCanceled()
	{	boolean result;
		loopLock.lock();
		result = isCanceled;
		loopLock.unlock();
		return result;
	}

	public long getTotalGameTime()
	{	return totalGameTime;	
	}
	
	public long getTotalEngineTime()
	{	return totalEngineTime;	
	}
	
	public void run()
	{	loadLock.lock();
		try
		{	// load the round
			init();
			// wait for the GUI to be ready
			if(panel==null)
				cond.await();
			// start the game
			process();
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
		catch (IOException e)
		{	e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{	e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{	e.printStackTrace();
		}
		catch (NoSuchFieldException e)
		{	e.printStackTrace();
		}
		catch (InterruptedException e)
		{	e.printStackTrace();
		}
		catch (InstantiationException e)
		{	e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{	e.printStackTrace();
		}
		catch (NoSuchMethodException e)
		{	e.printStackTrace();
		}
		loadLock.unlock();
	}
	
	public void process()
	{	long beforeTime,afterTime,timeDiff,sleepTime,lastTime;
		long overSleepTime = 0L;
		int noDelays = 0;
		long excess = 0L;

		for(int k=0;k<NUM_FPS;k++)
		{	fpsStore[k] = 0;
	        upsStore[k] = 0;
		}		
		
		gameStartTime = System.currentTimeMillis();
		prevStatsTime = gameStartTime;
		beforeTime = gameStartTime;
		afterTime = gameStartTime;
		totalGameTime = 0;
		totalEngineTime = 0;
		aiTime = 0;

		while(!isOver())
		{	
			milliPeriod = Configuration.getEngineConfiguration().getMilliPeriod();
			
			// cycle
			update();
			panel.paintScreen();
			// time process
			lastTime = afterTime;
			afterTime = System.currentTimeMillis();
			timeDiff = afterTime - beforeTime;
			sleepTime = milliPeriod - timeDiff - overSleepTime;

			// some time left in this cycle
			if(sleepTime>0)
			{	try
				{	Thread.sleep(sleepTime);
				}
				catch (InterruptedException ex)
				{	//ex.printStackTrace();
				}
				overSleepTime = System.currentTimeMillis() - afterTime - sleepTime;
			}
			// the frame took longer than the period (sleepTime<=0)
			else
			{	// store excess time value
				excess = excess - sleepTime;
				overSleepTime = 0L;
				// give another thread a chance to run
				noDelays++;
				if(noDelays>=NO_DELAYS_PER_YIELD)
				{	Thread.yield(); 
					noDelays = 0;
				}
			}

			beforeTime = System.currentTimeMillis();

			/* 
			 * If frame animation is taking too long, update the game state
			 * without rendering it, to get the updates/sec nearer to the required FPS.
			 */
			int skips = 0;
			RoundVariables.setFilterEvents(false); // in this situation, we do not want to record certain events
			while (excess>milliPeriod
//					&& skips<MAX_FRAME_SKIPS
					)
			{	excess = excess - milliPeriod;
				// update state but don't render
				update(); 
				skips++;
			}
			RoundVariables.setFilterEvents(true);
			
			framesSkipped = framesSkipped + skips;
			storeStats( );
			
			long delta = afterTime-lastTime;
			totalEngineTime = totalEngineTime + delta;
			if(!getEnginePause() && hasStarted && celebrationDuration<0)
			{	totalGameTime = totalGameTime + (long)(delta*Configuration.getEngineConfiguration().getSpeedCoeff());
				round.updateTime(totalGameTime);
			}
			
			if(isCanceled())
			{	round.cancelGame();
				setCanceled(false);
			}
		}
		
		// logs
		closeLogs();
		
		round.loopOver();
		panel.loopOver();
	}

	private void update()
	{	if(!getEnginePause() || getEngineStep())
		{	if(getEngineStep())
			{	totalGameTime = totalGameTime + (long)(milliPeriod*Configuration.getEngineConfiguration().getSpeedCoeff());
				switchEngineStep(false);
			}
			
			// logs
			updateLogs();
			
			// celebrations
			if(celebrationDuration>0)
			{	celebrationDuration = celebrationDuration - (milliPeriod*Configuration.getEngineConfiguration().getSpeedCoeff());
				if(celebrationDuration<=0)
					setOver(true);
			}
			
			// entry
			manageEntries();
			
			// update level
			level.update();
			
			// update AIs
			if(hasStarted) // only after the round has started
			{	aiTime = aiTime + milliPeriod;
				if(aiTime >= Configuration.getAisConfiguration().getAiPeriod())
				{	aiTime = 0;
					for(int i=0;i<players.size();i++)
					{	Player player = players.get(i);
						boolean aiPause = getAiPause(i);
						if(!player.isOut())
							player.update(aiPause);
					}
				}
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// ENGINE STATS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    /** number of FPS values stored to get an average */
	private static int NUM_FPS = 10;   
	private static long MAX_STATS_INTERVAL = 1000L;
	private long prevStatsTime;   
	private long gameStartTime;
	private long frameCount = 0;
	private long framesSkipped = 0L;
	private long statsCount = 0;
	private double averageFPS = 0.0;
	private double averageUPS = 0.0;
	private double fpsStore[] = new double[NUM_FPS];
	private double upsStore[] = new double[NUM_FPS];

	public double getAverageFPS()
	{	return averageFPS;	
	}
	
	public double getAverageUPS()
	{	return averageUPS;	
	}

	private void storeStats( )
    {	frameCount++;
    	long timeNow = System.currentTimeMillis();
//System.out.println("stat time: "+(timeNow-prevStatsTime));    	
  		long elapsedTime = timeNow - prevStatsTime;

      	if (elapsedTime>=MAX_STATS_INTERVAL)
      	{	// calculate the latest FPS and UPS
      		double actualFPS = 0;     
      		double actualUPS = 0;
      		actualFPS = ((frameCount / (double)elapsedTime) * 1000L);
      		actualUPS = (((frameCount+framesSkipped) / (double)elapsedTime) * 1000L);

      		// store the latest FPS and UPS
      		fpsStore[(int)statsCount%NUM_FPS] = actualFPS;
      		upsStore[(int)statsCount%NUM_FPS] = actualUPS;
      		statsCount ++;

      		// total the stored FPSs and UPSs
      		double totalFPS = 0.0;     
      		double totalUPS = 0.0;
      		for(int i=0;i<NUM_FPS;i++)
      		{	totalFPS = totalFPS + fpsStore[i];
      			totalUPS = totalUPS + upsStore[i];
      		}

      		if(statsCount<NUM_FPS)
      		{	// obtain the average FPS and UPS 
      			averageFPS = totalFPS/statsCount;
      			averageUPS = totalUPS/statsCount;
      		}
      		else
      		{	averageFPS = totalFPS/NUM_FPS;
      			averageUPS = totalUPS/NUM_FPS;
      		}

      		frameCount = 0;
      		framesSkipped = 0;
      		prevStatsTime = timeNow;
      		
      		// adapt the FPS according to the PC power
			int fps = Configuration.getEngineConfiguration().getFps();
			boolean adjust = Configuration.getEngineConfiguration().getAutoFps();
			if(averageFPS>0 && adjust)
			{	if(averageFPS<30 && fps>30)
				Configuration.getEngineConfiguration().setFps(fps-10);
				else if(averageFPS>(fps-1) && fps<50)
					Configuration.getEngineConfiguration().setFps(fps+10);
			}
      	}
    }
	
	/////////////////////////////////////////////////////////////////
	// LOOP END		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void playerOut(Player player)
	{	int index = players.indexOf(player);
		round.playerOut(index);
		panel.playerOut(index);	
	}

	/////////////////////////////////////////////////////////////////
	// LEVEL 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Level level;
	
	public Level getLevel()
	{	return level;	
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<Player> players = new ArrayList<Player>();
	
	public ArrayList<Player> getPlayers()
	{	return players;
	}

	/////////////////////////////////////////////////////////////////
	// ENTRY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Role[] entryRoles;
	private Double[] entryDelays;
	private int entryIndex = 0;
	private boolean hasStarted = false;
	
	private void initEntries()
	{	entryIndex = 0;
		entryRoles = new Role[]{Role.FLOOR,Role.BLOCK,Role.ITEM,Role.BOMB,Role.HERO};
		entryDelays = new Double[]{0d,0d,0d,0d,0d,0d,GameData.READY_TIME,GameData.SET_TIME,GameData.GO_TIME};
		String[] entryTexts = new String[]{null,null,null,null,null,panel.getMessageTextReady(),panel.getMessageTextSet(),panel.getMessageTextGo()};
		// set the roles
		for(int i=0;i<entryRoles.length;i++)
		{	double duration = level.getEntryDuration(entryRoles[i]);
//System.out.println(entryRoles[i]+":"+duration);
			if(i<entryRoles.length-1)
				duration = duration/2; //so that sprites appear almost at the same time
			entryDelays[i+1] = duration/*+Configuration.getEngineConfiguration().getMilliPeriod()*/;// a little more time, just too be sure it goes OK
			entryTexts[i] = entryRoles[i].toString(); //actually not used, but hey...
		}
		// unset the messages (for quicklaunch)
		for(int i=entryRoles.length;i<entryTexts.length;i++)
		{	if(entryTexts[i]==null)
				entryDelays[i+1] = 0d;			
		}
//for(int i=0;i<entryDelays.length;i++)
//	System.out.println("entryDelays["+i+"]="+entryDelays[i]);
	
		// init the message displayers
		RoundVariables.initMessageDisplayers(entryTexts);
	}

	private void manageEntries()
	{	// general case
		if(entryIndex<entryDelays.length)
		{	boolean done = false;
			while(!done)
			{	if(entryDelays[entryIndex]>0)
				{	entryDelays[entryIndex] = entryDelays[entryIndex] - (milliPeriod*Configuration.getEngineConfiguration().getSpeedCoeff());
					done = true;
				}
				else
				{	// show next sprites
					if(entryIndex<entryRoles.length)
					{	EngineEvent event = new EngineEvent(EngineEvent.ROUND_ENTER);
//System.out.println(totalTime+": "+entryRoles[entryIndex]);		
						event.setDirection(Direction.NONE);
						level.spreadEvent(event,entryRoles[entryIndex]);
					}
					// show ready-set-go
					else if(entryIndex<entryDelays.length-1)
					{	level.updateMessageDisplayer(entryIndex);
//System.out.println(totalTime+": message");				
					}
					// start the game
					else //if(entryIndex==entryDelays.length-1) 
					{	level.updateMessageDisplayer(-1);
//System.out.println(totalTime+": start");				
						EngineEvent event = new EngineEvent(EngineEvent.ROUND_START);
						level.spreadEvent(event);
						hasStarted = true;
						done = true;
					}
					entryIndex++;
				}
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// CELEBRATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	double celebrationDuration = -1;

	public void initCelebrationDuration()
	{	if(players.size()>0)
		{	Player player = players.get(0);
			Sprite sprite = player.getSprite();
			StateAbility ability = sprite.modulateStateAbility(StateAbilityName.HERO_CELEBRATION_DURATION);
			celebrationDuration = ability.getStrength();
		}
		else
			celebrationDuration = 1;
	}
	
	public void reportVictory(int index)
	{	Player player = players.get(index);
		Sprite sprite = player.getSprite();
		EngineEvent event = new EngineEvent(EngineEvent.CELEBRATION_VICTORY);
		sprite.processEvent(event);
	}
	
	public void reportDefeat(int index)
	{	Player player = players.get(index);
		Sprite sprite = player.getSprite();
		EngineEvent event = new EngineEvent(EngineEvent.CELEBRATION_DEFEAT);
		sprite.processEvent(event);
	}

	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void draw(Graphics g)
	{	// level
		level.draw(g);
		// players names
		if(getShowNames())
			drawPlayersNames(g);
		// ais data
		drawAisInfo(g);
		// speed
		if(getShowSpeed())
			drawSpeed(g);
		// time
		drawTime(g);
		// FPS
		if(getShowFPS())
			drawFPS(g);
		// pause
		if(getEnginePause())
			drawEnginePause(g);

		drawAisPause(g);
	}
	
	private void drawSpeed(Graphics g)
	{	g.setColor(Color.CYAN);
		Font font = new Font("Dialog", Font.PLAIN, 18);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		String text = "Speed: "+Configuration.getEngineConfiguration().getSpeedCoeff();
		Rectangle2D box = metrics.getStringBounds(text, g);
		int x = 10;
		int y = (int)Math.round(10+box.getHeight()/2);
		g.drawString(text, x, y);
	}
	
	private void drawTime(Graphics g)
	{	int st = getShowTime();
		// loop time
		if(st==1)
		{	g.setColor(Color.CYAN);
			Font font = new Font("Dialog", Font.PLAIN, 18);
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics(font);
			long time = getTotalGameTime();
			String text = "Game time: "+TimeTools.formatTime(time,TimeUnit.HOUR,TimeUnit.MILLISECOND,false);
			Rectangle2D box = metrics.getStringBounds(text, g);
			int x = 10;
			int y = (int)Math.round(30+box.getHeight()/2);
			g.drawString(text, x, y);
		}
		// engine time
		else if(st==2)
		{	g.setColor(Color.CYAN);
			Font font = new Font("Dialog", Font.PLAIN, 18);
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics(font);
			long time = getTotalEngineTime();
			String text = "Engine time: "+TimeTools.formatTime(time,TimeUnit.HOUR,TimeUnit.MILLISECOND,false);
			Rectangle2D box = metrics.getStringBounds(text, g);
			int x = 10;
			int y = (int)Math.round(30+box.getHeight()/2);
			g.drawString(text, x, y);
		}
		// real time
		else if(st==3)
		{	g.setColor(Color.CYAN);
			Font font = new Font("Dialog", Font.PLAIN, 18);
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics(font);
			long time = System.currentTimeMillis()-gameStartTime;
			String text = "Real time: "+TimeTools.formatTime(time,TimeUnit.HOUR,TimeUnit.MILLISECOND,false);
			Rectangle2D box = metrics.getStringBounds(text, g);
			int x = 10;
			int y = (int)Math.round(30+box.getHeight()/2);
			g.drawString(text, x, y);
		}
	}

	private void drawFPS(Graphics g)
	{	g.setColor(Color.CYAN);
		Font font = new Font("Dialog", Font.PLAIN, 18);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		double fps = getAverageFPS();
		String fpsStr = nf.format(fps); 
		double ups = getAverageUPS();
		String upsStr = nf.format(ups);
		String thFps = Integer.toString(Configuration.getEngineConfiguration().getFps());
		String text = "FPS/UPS/Th: "+fpsStr+"/"+upsStr+"/"+thFps;
		Rectangle2D box = metrics.getStringBounds(text, g);
		int x = 10;
		int y = (int)Math.round(50+box.getHeight()/2);
		g.drawString(text, x, y);
	}

	private void drawEnginePause(Graphics g)
	{	g.setColor(Color.MAGENTA);
		Font font = new Font("Dialog", Font.PLAIN, 18);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		String text = "Engine paused";
		Rectangle2D box = metrics.getStringBounds(text, g);
		int x = 10;
		int y = (int)Math.round(70+box.getHeight()/2);
		g.drawString(text, x, y);
	}

	private void drawAisPause(Graphics g)
	{	g.setColor(Color.MAGENTA);
		Font font = new Font("Dialog", Font.BOLD, 12);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		String text = "AI Paused";
		Rectangle2D box = metrics.getStringBounds(text, g);
		for(int i=0;i<players.size();i++)
		{	Player player = players.get(i);
			if(pauseAis.get(i) && !player.isOut())
			{	Sprite s = player.getSprite();
				int x = (int)Math.round(s.getCurrentPosX()-box.getWidth()/2);
				int y = (int)Math.round(s.getCurrentPosY()+box.getHeight()/2);
				g.drawString(text, x, y);
			}
		}
	}
	
	private static final int AI_INFO_ALPHA_LEVEL = 100;
	private void drawAisInfo(Graphics g)
	{	Graphics2D g2 = (Graphics2D)g;
		double tileSize = RoundVariables.scaledTileDimension;
		for(int i=0;i<players.size();i++)
		{	Player player = players.get(i);
			if(player.hasAi())
			{	AbstractAiManager<?> aiMgr = player.getArtificialIntelligence();
				// tile colors
				if(getShowAiTileColors(i))
				{	Color[][] colors = aiMgr.getTileColors();
					for(int line=0;line<level.getGlobalHeight();line++)
					{	for(int col=0;col<level.getGlobalWidth();col++)
						{	Color color = colors[line][col];
							if(color!=null)
							{	Color paintColor = new Color(color.getRed(),color.getGreen(),color.getBlue(),AI_INFO_ALPHA_LEVEL);
								g2.setPaint(paintColor);
								Tile tile = level.getTile(line,col);
								double x = tile.getPosX()-tileSize/2;
								double y = tile.getPosY()-tileSize/2;
								g2.fill(new Rectangle2D.Double(x,y,tileSize,tileSize));
							}
						}
					}
				}
				// paths
				if(getShowAiPaths(i))
				{	List<List<Tile>> paths = aiMgr.getPaths();
					List<Color> colors = aiMgr.getPathColors();
					Stroke prevStroke = g2.getStroke();
					int thickness = (int)(tileSize/3);
					Stroke stroke = new BasicStroke(thickness,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
					g2.setStroke(stroke);
					for(int j=0;j<paths.size();j++)
					{	List<Tile> path = paths.get(j);
						Color color = colors.get(j);
						if(color!=null && !path.isEmpty())
						{	Color paintColor = new Color(color.getRed(),color.getGreen(),color.getBlue(),AI_INFO_ALPHA_LEVEL);
							g2.setPaint(paintColor);
							Tile tile2 = path.get(0);
							double x1,x2 = tile2.getPosX();
							double y1,y2 = tile2.getPosY();
							Path2D shape = new Path2D.Double();
							shape.moveTo(x2,y2);
							int k = 1;
							while(k<path.size())
							{	// tiles
								x1 = x2;
								y1 = y2;
								tile2 = path.get(k);							
								x2 = tile2.getPosX();
								y2 = tile2.getPosY();
								// directions (to manage the case where the path cross the level off-scree)
								Direction direction12 = level.getDirection(x1,y1,x2,y2);
								int[] intDir12 = direction12.getIntFromDirection();
								Direction direction21 = direction12.getOpposite();
								int[] intDir21 = direction21.getIntFromDirection();
								// alternative locations
								double x1b = x2 + intDir21[0]*tileSize;
								double y1b = y2 + intDir21[1]*tileSize;
								double x2b = x1 + intDir12[0]*tileSize;
								double y2b = y1 + intDir12[1]*tileSize;
								// compare actual and theoretical positions
								if(!CalculusTools.isRelativelyEqualTo(x1,x1b) || !CalculusTools.isRelativelyEqualTo(y1,y1b))
								{	shape.lineTo(x2b,y2b);
									g2.draw(shape);
									shape = new Path2D.Double();
									shape.moveTo(x1b,y1b);
									shape.lineTo(x2,y2);
								}
								else
									shape.lineTo(x2,y2);
								k++;
							}
							g2.draw(shape);
						}
					}
					g2.setStroke(prevStroke);
				}
				// tile texts
				if(getShowAiTileTexts(i))
				{	g.setColor(Color.MAGENTA);
					Font font = new Font("Dialog", Font.PLAIN, 15);
					g.setFont(font);
					FontMetrics metrics = g.getFontMetrics(font);
					String[][] texts = aiMgr.getTileTexts();
					for(int line=0;line<level.getGlobalHeight();line++)
					{	for(int col=0;col<level.getGlobalWidth();col++)
						{	String text = texts[line][col];
							if(text!=null)
							{	Tile tile = level.getTile(line,col);
								Rectangle2D box = metrics.getStringBounds(text, g);
								int x = (int)Math.round(tile.getPosX()-box.getWidth()/2);
								int y = (int)Math.round(tile.getPosY()+box.getHeight()/2);
								g.drawString(text, x, y);
							}
						}
					}
				}
			}
		}
		
		g.setColor(Color.MAGENTA);
		Font font = new Font("Dialog", Font.BOLD, 12);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		String text = "AI Paused";
		Rectangle2D box = metrics.getStringBounds(text, g);
		for(int i=0;i<players.size();i++)
		{	if(pauseAis.get(i))
			{	Sprite s = players.get(i).getSprite();
				int x = (int)Math.round(s.getCurrentPosX()-box.getWidth()/2);
				int y = (int)Math.round(s.getCurrentPosY()+box.getHeight()/2);
				g.drawString(text, x, y);
			}
		}
	}

	private void drawPlayersNames(Graphics g)
	{	//Graphics2D g2 = (Graphics2D) g;
		Font font = new Font("Dialog",Font.BOLD,12);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		for(int i=0;i<players.size();i++)
		{	Player player = players.get(i);
			if(!player.isOut())
			{	String text = "["+(i+1)+"] "+player.getName();
				Sprite s = player.getSprite();
				Rectangle2D box = metrics.getStringBounds(text,g);
				double boxWidth = box.getWidth();
				double boxHeight = box.getHeight();
				int x = (int)Math.round(s.getCurrentPosX()-boxWidth/2);
				int y = (int)Math.round(s.getCurrentPosY()+boxHeight/2-metrics.getDescent());
				Color rectangleColor = new Color(255,255,255,100);
				g.setColor(rectangleColor);
				int arcDim = (int)Math.round(boxWidth/10);
				double xMargin = boxWidth/15;
				double yMargin = boxHeight/5;
				int rectangleWidth = (int)Math.round(boxWidth+2*xMargin);
				int rectangleHeight = (int)Math.round(boxHeight+2*yMargin);
				int rx = (int)Math.round(s.getCurrentPosX()-rectangleWidth/2);
				int ry = (int)Math.round(s.getCurrentPosY()-rectangleHeight/2);
				g.fillRoundRect(rx,ry,rectangleWidth,rectangleHeight,arcDim,arcDim);
				g.setColor(Color.BLACK);
				g.drawString(text,x+1,y+1);
				Color color = player.getColor().getColor();
				g.setColor(color);
				g.drawString(text,x,y);
			}
		}		
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
			// system listener
			panel.removeKeyListener(systemControl);
			
			// players
			Iterator<Player> i = players.iterator();
			while(i.hasNext())
			{	Player player = i.next();
				panel.removeKeyListener(player.getSpriteControl());
				player.finish();
				i.remove();
			}
			
			// panel
//			panel.finish();
			panel = null;
			
			// level
//			level.finish();
			level = null;
			
			// control
			systemControl.finish();
			systemControl = null;
		}		
	}	
}

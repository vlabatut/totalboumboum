package org.totalboumboum.engine.loop;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.engine.EngineConfiguration;
import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.feature.event.EngineEvent;
import org.totalboumboum.engine.content.sprite.hero.HollowHeroFactory;
import org.totalboumboum.engine.control.system.SystemControl;
import org.totalboumboum.engine.loop.display.DisplayManager;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.engine.player.AiPlayer;
import org.totalboumboum.engine.player.ControlledPlayer;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.images.PredefinedColor;
import org.xml.sax.SAXException;

/**
 * Loop object associated to all game mode requiring
 * graphical display, i.e. all of them except the
 * simulated mode.
 * 
 * @author Vincent Labatut
 */
public abstract class VisibleLoop extends Loop
{	/** Class id */
	private static final long serialVersionUID = 1L;
	/** Whether details should be displayed in the console, or not */
	protected final static boolean VERBOSE = false;
	
	/**
	 * Builds a new loop.
	 * 
	 * @param round
	 * 		Round displayed in this loop.
	 */
	public VisibleLoop(Round round)
	{	super(round);
	}	
	
	/////////////////////////////////////////////////////////////////
	// LEVEL 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Level currently played */
	protected Level level;
	
	/**
	 * Returns the level currently played.
	 * 
	 * @return
	 * 		Current level. 
	 */
	public Level getLevel()
	{	return level;	
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Players involved in the current game */
	protected List<AbstractPlayer> players = new ArrayList<AbstractPlayer>();
	
	/**
	 * Returns the current players.
	 * 
	 * @return
	 * 		List of players involved in the current game.
	 */
	public List<AbstractPlayer> getPlayers()
	{	return players;
	}

	/**
	 * Indicates the specified player is out
	 * of the game.
	 * 
	 * @param player
	 * 		Concerned player.
	 */
	public void playerOut(AbstractPlayer player)
	{	int index = players.indexOf(player);
		round.playerOut(index);
		panel.playerOut(index);	
	}
	
	/**
	 * Initialize a player involved in this game.
	 * 
	 * @param profile
	 * 		Profile of the concerned player.
	 * @param base
	 * 		Sprite base.
	 * @param tile
	 * 		Starting tile.
	 * @return
	 * 		Corresponding initialized player.
	 * 
	 * @throws IllegalArgumentException
	 * 		Problem while loading an agent.
	 * @throws SecurityException
	 * 		Problem while loading a file.
	 * @throws ParserConfigurationException
	 * 		Problem while loading an XML file.
	 * @throws SAXException
	 * 		Problem while loading an XML file.
	 * @throws IOException
	 * 		Problem while loading a file.
	 * @throws ClassNotFoundException
	 * 		Problem while loading an agent.
	 * @throws InstantiationException
	 * 		Problem while loading an agent.
	 * @throws IllegalAccessException
	 * 		Problem while loading an agent.
	 * @throws InvocationTargetException
	 * 		Problem while loading an agent.
	 * @throws NoSuchMethodException
	 * 		Problem while loading an agent.
	 * @throws URISyntaxException
	 * 		Problem while loading an XML file.
	 */
	public abstract AbstractPlayer initPlayer(Profile profile, HollowHeroFactory base, Tile tile) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, URISyntaxException;

	/////////////////////////////////////////////////////////////////
	// CANCELATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates if the game has been canceled */
	protected boolean isCanceled = false;
	/** Lock associated to game cancelation */
	protected Lock cancelLock = new ReentrantLock();

	/**
	 * Changes the canceled state of the current game
	 * (canceled or not).
	 * 
	 * @param isCanceled
	 * 		If {@code true}, the game is canceled.
	 */
	protected void setCanceled(boolean isCanceled)
	{	cancelLock.lock();
		this.isCanceled = isCanceled;
		cancelLock.unlock();
	}
	
	/**
	 * Checks whether the current game
	 * is canceled, or not.
	 * 
	 * @return
	 * 		{@code true} iff the game was canceled.
	 */
	public boolean isCanceled()
	{	boolean result;
		cancelLock.lock();
		result = isCanceled;
		cancelLock.unlock();
		return result;
	}
	
	/**
	 * Update the cancel status of the current game.
	 */
	protected abstract void updateCancel();

	/////////////////////////////////////////////////////////////////
	// INITIALIZING		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Lock associated to the load step */
	protected Lock loadLock = new ReentrantLock();
	/** Condition associated to the load lock */
	protected Condition cond = loadLock.newCondition();
	
	/**
	 * Loads all the data required to start playing.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while loading XML data.
	 * @throws SAXException
	 * 		Problem while loading XML data.
	 * @throws IOException
	 * 		Problem while loading some file.
	 * @throws ClassNotFoundException
	 * 		Problem while loading agents.
	 * @throws IllegalArgumentException
	 * 		Problem while loading agents.
	 * @throws SecurityException
	 * 		Problem while loading some file.
	 * @throws IllegalAccessException
	 * 		Loading problem.
	 * @throws NoSuchFieldException
	 * 		Problem while loading agents.
	 * @throws InstantiationException
	 * 		Problem while loading agents.
	 * @throws InvocationTargetException
	 * 		Problem while loading agents.
	 * @throws NoSuchMethodException
	 * 		Problem while loading agents.
	 * @throws URISyntaxException
	 * 		Problem while loading XML data.
	 */
	protected abstract void load() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, InstantiationException, InvocationTargetException, NoSuchMethodException, URISyntaxException;
	
	/**
	 * Indicate the loading step is finished.
	 */
	public void loadStepOver()
	{	round.loadStepOver();
	}
	
	/**
	 * Stuff not needing the panel to be initialized.
	 */
	protected void startLoopInit()
	{	initLogs();
	}

	/**
	 * Stuff to be initialized once the panel is set
	 */
	protected void finishLoopInit()
	{	initEntries();
		initDisplayManager();
	}
	
	/////////////////////////////////////////////////////////////////
	// GAME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates if the game has started yet */
	protected boolean gameStarted = false;
	/** Indicates if the game is considered over */
	protected boolean gameOver = false;

	/////////////////////////////////////////////////////////////////
	// ENGINE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Number of frames with a delay of 0 ms before the animation thread yields
	 * to other running threads. 
	 */
	protected static final int NO_DELAYS_PER_YIELD = 16;
	/** 
	 * no. of frames that can be skipped in any one animation loop
	 * i.e the games state is updated but not rendered
	 */ 
	protected static int MAX_FRAME_SKIPS = 5;
	/** game period expressed un milliseconds */
	protected long milliPeriod;

	@Override
	public void run()
	{	loadLock.lock();
		try
		{	// load the round
			long loadStart = System.currentTimeMillis();
			load();
			long loadEnd = System.currentTimeMillis();
			if(VERBOSE)
				System.out.println("total load time: "+(loadEnd-loadStart));
			
			// init the loop
			startLoopInit();
			
			// wait for the GUI to be ready
			if(panel==null)
				cond.await();
			
			// finish init (stuff requiring the panel)
			finishLoopInit();
			
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
		catch (URISyntaxException e)
		{	e.printStackTrace();
		}
		loadLock.unlock();
	}
	
	/**
	 * Performs one loop iteration.
	 * 
	 * @throws IOException 
	 * 		Problem while capturing some agent percepts.
	 */
	public void process() throws IOException
	{	long beforeTime,afterTime,timeDiff,sleepTime,lastTime;
		long overSleepTime = 0L;
		int noDelays = 0;
		long excess = 0L;

		// stats
		initStats();		

		// time
		initTimes();
		beforeTime = gameStartTime;
		afterTime = gameStartTime;

		while(!isOver())
		{	milliPeriod = Configuration.getEngineConfiguration().getMilliPeriod();
			
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

			// If frame animation is taking too long, update the game state
			// without rendering it, to get the updates/sec nearer to the required FPS.
			int skips = 0;
			RoundVariables.setFilterEvents(true); // in this situation, we do not want to record certain events
			while (excess>milliPeriod
//					&& skips<MAX_FRAME_SKIPS
					)
			{	excess = excess - milliPeriod;
				// update state but don't render
				update(); 
				skips++;
			}
			RoundVariables.setFilterEvents(false);			
			framesSkipped = framesSkipped + skips;
			
			long delta = afterTime - lastTime;
			totalEngineTime = totalEngineTime + delta;
			if(!getEnginePause() && gameStarted && !gameOver)
			{	//prevTotalGameTime = totalGameTime;
				totalGameTime = totalGameTime + (long)(delta*Configuration.getEngineConfiguration().getSpeedCoeff());
				round.updateTime(totalGameTime);
			}
		}
		
		// logs
		closeLogs();
		
		round.loopOver();
		panel.loopOver();
	}
	
	/**
	 * Updates the game state.
	 * 
	 * @throws IOException 
	 * 		Problem while capturing some agent percepts.
	 */
	protected abstract void update() throws IOException;

	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Total game time elapsed since the players took control */
	protected long totalGameTime = 0;
	/** Total real time elapsed since the level started appearing */
	protected long totalEngineTime = 0;
	
	/**
	 * Initializes the time-related variales
	 */
	protected void initTimes()
	{	gameStartTime = System.currentTimeMillis();
		prevStatsTime = gameStartTime;
		totalGameTime = 0;
		totalEngineTime = 0;
	}
	
	/** 
	 * Returns the total game time elapsed 
	 * since the players took control. It ignores
	 * the time spent in pause.
	 * 
	 * @return
	 * 		Time spent since the player took control, excluding pauses.
	 */
	public long getTotalGameTime()
	{	return totalGameTime;	
	}
	
//	public long getPrevTotalGameTime()
//	{	return prevTotalGameTime;	
//	}
	
	/** 
	 * Returns the total time elapsed 
	 * since the level started appearing.
	 * It includes the time spent in pause.
	 * 
	 * @return
	 * 		Total time spent since the round started.
	 */
	public long getTotalEngineTime()
	{	return totalEngineTime;
	}

	/** 
	 * Returns the total real time elapsed 
	 * since the round started, expressed in ms.
	 * 
	 * @return
	 * 		Total real time spent since the round started.
	 */
	public long getTotalRealTime()
	{	long result = System.currentTimeMillis() - gameStartTime;
		return result;	
	}

	/////////////////////////////////////////////////////////////////
	// ENGINE STATS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    /** Number of values stored to get an average */
	protected static int NUM_VALUES = 10;
	/** Time interval between to stat updates */
	protected static long MAX_STATS_INTERVAL = 1000L;
	/** Time of the previous stat update */
	protected long prevStatsTime;   
	/** Time when the game started */
	protected long gameStartTime;
	/** Number of frames counted since last update */
	protected long frameCount = 0;
	/** Number of frames skipped since last update */
	protected long framesSkipped = 0L;
	/** Stat count */
	protected long statsCount = 0;
	/** Average frame per second */
	protected double averageFPS = 0.0;
	/** Average AI update per second */
	protected double averageUPS = 0.0;
	/** Store FPS values */
	protected double fpsStore[];
	/** Store UPS values */
	protected double upsStore[];
	/** Store CPU usage values */
	protected long cpuStore[][];
	/** Previous CPU usage values */
	protected long cpuPrev[];
	/** Averaged CPU usage */
	protected double averageCpu[];
	/** Percentages of CPU usage */
	protected double averageCpuProportions[];
	/** Thread used to measure CPU usage */
	protected ThreadMXBean tmxb = null;
	/** Ids of the engine and agents threads */
	protected HashMap<Long,PredefinedColor> threadIds = null;
	/** Colors associated to the threads */
	protected List<PredefinedColor> colors = null;
	
	/**
	 * Returns the current average
	 * frame per second rate.
	 * 
	 * @return
	 * 		Current FPS.
	 */
	public double getAverageFPS()
	{	return averageFPS;	
	}
	
	/**
	 * Returns the current average
	 * update per second rate.
	 * 
	 * @return
	 * 		Current UPS.
	 */
	public double getAverageUPS()
	{	return averageUPS;	
	}
	
	/**
	 * Returns the current average
	 * CPU usage.
	 * 
	 * @return
	 * 		Current CPU usage.
	 */
	public double[] getAverageCpu()
	{	return averageCpu;
	}

	/**
	 * Returns the current average
	 * CPU usage in percents.
	 * 
	 * @return
	 * 		Current CPU usage in percents.
	 */
	public double[] getAverageCpuProportions()
	{	return averageCpuProportions;
	}

	/**
	 * Initializes stat-related objects.
	 */
	protected void initStats()
	{	initFps();
		initCpu();
	}
	
	/**
	 * Initializes FPS-related objects,
	 * used when calculating stats.
	 */
	private void initFps()
	{	// frames per second
		fpsStore = new double[NUM_VALUES];
		Arrays.fill(fpsStore,0);
		
		// updates per second
		upsStore = new double[NUM_VALUES];
		Arrays.fill(upsStore,0);
	}
	
	/**
	 * Initializes CPU usage-related objects,
	 * used when calculating stats.
	 */
	private void initCpu()
	{	// init stats
		int pc = players.size() + 2;
		cpuPrev = new long[pc];
		Arrays.fill(cpuPrev,0);
		averageCpu = new double[pc];
		Arrays.fill(averageCpu,0);
		averageCpuProportions = new double[pc];
		Arrays.fill(averageCpuProportions,1/pc);

		cpuStore = new long[NUM_VALUES][pc];
		for(int k=0;k<NUM_VALUES;k++)
		{	for(int i=0;i<pc;i++)
			{	cpuStore[k][i] = 0;
			}
		}

		// init color list
		colors = new ArrayList<PredefinedColor>();
		for(AbstractPlayer player: players)
			colors.add(player.getColor());
		
		// init thread map
		tmxb = ManagementFactory.getThreadMXBean();
		threadIds = new HashMap<Long,PredefinedColor>();
		long ids[] = tmxb.getAllThreadIds();
		ThreadInfo[] infos = tmxb.getThreadInfo(ids);
		for(ThreadInfo info: infos)
		{	String name = info.getThreadName();
			long id = info.getThreadId();
			String[] temp = name.split(":");
			if(temp.length>1)
			{	PredefinedColor color = PredefinedColor.valueOf(temp[0]);
				threadIds.put(id,color);
			}
			else if(name.startsWith("TBB"))
			{	threadIds.put(id,null);
			}
		}
	}

	/**
	 * Update statistics.
	 */
	protected void updateStats( )
    {	frameCount++;
    	long timeNow = System.currentTimeMillis();
    	//System.out.println("stat time: "+(timeNow-prevStatsTime));    	
  		long elapsedTime = timeNow - prevStatsTime;

      	if(elapsedTime>=MAX_STATS_INTERVAL)
      	{	// calculate the latest FPS and UPS
      		updateFps(elapsedTime);
      		updateCpu(elapsedTime);

      		statsCount++;
      		prevStatsTime = timeNow;
      	}
    }

	/**
	 * Update FPS-related statistics.
	 * 
	 * @param elapsedTime
	 * 		Time elapsed since last update.
	 */
	protected void updateFps(long elapsedTime)
    {	// calculate the latest FPS and UPS
      	double currentFPS = 0;     
      	double currentUPS = 0;
      	currentFPS = ((frameCount / (double)elapsedTime) * 1000L);
      	currentUPS = (((frameCount+framesSkipped) / (double)elapsedTime) * 1000L);

  		// store the latest FPS and UPS
  		fpsStore[(int)statsCount%NUM_VALUES] = currentFPS;
  		upsStore[(int)statsCount%NUM_VALUES] = currentUPS;

  		// total the stored FPSs and UPSs
  		double totalFPS = 0.0;     
  		double totalUPS = 0.0;
  		for(int i=0;i<NUM_VALUES;i++)
  		{	totalFPS = totalFPS + fpsStore[i];
  			totalUPS = totalUPS + upsStore[i];
  		}

  		int norm = (int)Math.min(statsCount+1,NUM_VALUES);
  		{	// obtain the average FPS and UPS 
  			averageFPS = totalFPS/norm;
  			averageUPS = totalUPS/norm;
  		}
      		
  		// adapt the FPS according to the PC power
		int fps = Configuration.getEngineConfiguration().getFps();
		boolean adjust = Configuration.getEngineConfiguration().getAutoFps();
		if(averageFPS>0 && adjust)
		{	if(averageFPS<30 && fps>30)
			Configuration.getEngineConfiguration().setFps(fps-10);
			else if(averageFPS>(fps-1) && fps<50)
				Configuration.getEngineConfiguration().setFps(fps+10);
		}
		
  		frameCount = 0;
  		framesSkipped = 0;
    }

	/**
	 * Update CPU usage-related statistics.
	 * 
	 * @param elapsedTime
	 * 		Time elapsed since last update.
	 */
	protected void updateCpu(long elapsedTime)
    {	// retrieve current cpu times
		//System.out.println("------------------------------------------");
		long[] tids = tmxb.getAllThreadIds();
        ThreadInfo[] tinfos = tmxb.getThreadInfo(tids);
        HashMap<PredefinedColor,Long> values = new HashMap<PredefinedColor,Long>();
        long engineValue = 0;
        long swingValue = 0;
        for(int i=0;i<tids.length;i++)
        {	long id = tids[i];
        	ThreadInfo info = tinfos[i];
        	long cpuTime = tmxb.getThreadCpuTime(id)/1000000;
        	//System.out.println(info.getThreadName()+": "+cpuTime);        	
            // focus only on running threads
            if(cpuTime!=-1 && info!=null)
            {	// separate ai-related, engine-related and swing-related threads
            	PredefinedColor color = threadIds.get(id);
            	if(color==null)
            	{	// engine related (color is null)
            		if(threadIds.containsKey(id))
            			engineValue = cpuTime;
            		// swing related (no color at all)
            		else
            			swingValue = swingValue + cpuTime;
            	}
            	// ai-related (non-null color)
            	else
            	{	values.put(color,cpuTime);
            		//System.out.print(cpuTime+" ");		
            	}
            }
        }
        //System.out.println("xxxxxxxxxxxxxxx");
        
        // process difference with previous values
        long currentCpu[] = new long[colors.size()+2];
        currentCpu[0] = swingValue - cpuPrev[0];
        cpuPrev[0] = swingValue;
        //System.out.println("swing: "+currentCpu[0]);
        currentCpu[1] = engineValue - cpuPrev[1];
        cpuPrev[1] = engineValue;
        //System.out.println("engine: "+currentCpu[1]);
		//double timeTotal = currentCpu[0] + currentCpu[1];
		for(int i=0;i<colors.size();i++)
		{	PredefinedColor color = colors.get(i);
			Long cpuTime = values.get(color);
			if(cpuTime==null)
			{	currentCpu[i+2] = 0l;
				cpuPrev[i+2] = 0l;
			}
			else
			{	currentCpu[i+2] = cpuTime - cpuPrev[i+2];
	      		cpuPrev[i+2] = cpuTime;
			}
	        //System.out.println(color+": "+currentCpu[i+2]);
		}
        //System.out.println("---------------");
     	
		// normalize to get proportions and store the latest CPU usage (proportions)
		for(int i=0;i<currentCpu.length;i++)
		{	//double temp = currentCpu[i] / timeTotal;
			cpuStore[(int)statsCount%NUM_VALUES][i] = currentCpu[i];
		}
      	
  		// total the stored usage values
  		double totalUsage[] = new double[colors.size()+2];
  		Arrays.fill(totalUsage,0);
  		for(int i=0;i<NUM_VALUES;i++)
  		{	for(int j=0;j<totalUsage.length;j++)
 			{	totalUsage[j] = totalUsage[j] + cpuStore[i][j];
 			}
  		}
  		
  		int norm = (int)Math.min(statsCount+1,NUM_VALUES);
  		double totalTime = 0;
  		for(int j=0;j<averageCpu.length;j++)
  		{	averageCpu[j] = totalUsage[j]/norm;
			totalTime = totalTime + averageCpu[j];
  		}
  		for(int j=0;j<averageCpu.length;j++)
  			averageCpuProportions[j] = averageCpu[j]/totalTime;
    }

	/////////////////////////////////////////////////////////////////
	// CONTROL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Object in charge of controls (keys) related to the system */
	protected SystemControl systemControl;

	/**
	 * Receives and treats a system-related event.
	 * 
	 * @param event
	 * 		Event to be processed.
	 */
	public void processEvent(SystemControlEvent event)
	{	
//		String name = event.getName();
//		if(name.equals(ControlEvent.REQUIRE_CANCEL_ROUND))
//			setCanceled(true);
//		else
			displayManager.provessEvent(event);		
	}

	/////////////////////////////////////////////////////////////////
	// GRAPHICS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Panel used to display this loop */
	protected transient LoopRenderPanel panel;

	/**
	 * Changes the panel used to display this loop.
	 * 
	 * @param panel
	 * 		New panel used to display this loop.
	 */
	public void setPanel(LoopRenderPanel panel)
	{	loadLock.lock();
		this.panel = panel;
		
		// system listener
		panel.addKeyListener(systemControl);
		
		// players listeners
		Iterator<AbstractPlayer> i = players.iterator();
		while(i.hasNext())
		{	AbstractPlayer player = i.next();
			if(player instanceof ControlledPlayer)
				panel.addKeyListener(((ControlledPlayer)player).getSpriteControl());
		}
		
		// waking the process thread up
		cond.signal();
		loadLock.unlock();	
	}
	
	/**
	 * Returns the panel used to display this loop.
	 * 
	 * @return
	 * 		Panel used to display this loop.
	 */
	public LoopRenderPanel getPanel()
	{	return panel;
	}
	
	/**
	 * Draws graphics in the specified object,
	 * and returns {@code true} if a capture
	 * was planned.
	 * 
	 * @param g
	 * 		Graphics object in which the loop must draw.
	 * @return
	 * 		{@code true} iff a capture must be peformed.
	 */
	public boolean draw(Graphics g)
	{	// level: always display
		level.draw(g);
		
		// check for possible capture
		boolean result = mustScreenCapture();
		if(result)
			setScreenCapture(false);
		// display manager (possibly not feedback messages)
		displayManager.draw(g, result);
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// PRINT SCREEN		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates if a capture must be performed */
	protected boolean screenCapture = false;
	/** Lock associated to screen capture */
	protected Lock screenCaptureLock = new ReentrantLock();

	/**
	 * Asks the loop to perform
	 * a screen capture (or disable it).
	 * 
	 * @param screenCapture
	 * 		{@code true} to make a screen capture.
	 */
	protected void setScreenCapture(boolean screenCapture)
	{	screenCaptureLock.lock();
		this.screenCapture = screenCapture;
		screenCaptureLock.unlock();
	}
	
	/**
	 * Checks if a screen capture should be performed.
	 * 
	 * @return
	 * 		{@code true} iff a screen capture is currently planned.
	 */
	public boolean mustScreenCapture()
	{	boolean result;
		screenCaptureLock.lock();
		result = screenCapture;
		screenCaptureLock.unlock();
		return result;
	}
	
//	public void performScreenCapture()
//	{	boolean sc = mustScreenCapture();
//		if(sc)
//		{	setScreenCapture(false);
//			panel.captureScreen();
//		}
//	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY MANAGER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Object used to display additional information over the actuall game graphics */
	DisplayManager displayManager = new DisplayManager();
	
	/**
	 * Initializes the object used to display additional information 
	 * over the actuall game graphics.
	 */
	protected abstract void initDisplayManager();
	
	/////////////////////////////////////////////////////////////////
	// LOGS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Configuration object for the engine options */
	protected EngineConfiguration engineConfiguration = Configuration.getEngineConfiguration();
	
	/**
	 * Initializes all logs.
	 */
	protected void initLogs()
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
			catch (UnsupportedEncodingException e)
			{	e.printStackTrace();
			}			
		}
	}
	
	/**
	 * Updatge all logs.
	 */
	protected void updateLogs()
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
	}
	
	/**
	 * Close all log files.
	 */
	protected void closeLogs()
	{	if(engineConfiguration.getLogControls())
		{	try
			{	engineConfiguration.closeControlsLogStream();
			}
			catch(IOException e)
			{	e.printStackTrace();
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// ENGINE PAUSE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicate if the engine is currently paused */
	protected boolean pauseEngine = false;
	/** Indicate if the user required an engine step to be performed */
	protected boolean stepEngine = false;
	/** Lock associated to engine pause */
	protected Lock debugLock = new ReentrantLock();

	/**
	 * Pause/unpause the engine.
	 */
	public void switchEnginePause()
	{	debugLock.lock();
		pauseEngine = !pauseEngine;		
		debugLock.unlock();
	}
	
	/**
	 * Indicates if the engine is currently
	 * paused.
	 * 
	 * @return
	 * 		{@code true} if the engine is paused.
	 */
	public boolean getEnginePause()
	{	boolean result;
		debugLock.lock();
		result = pauseEngine;
		debugLock.unlock();
		return result;
	}

	/**
	 * Requires to peform an
	 * engine step.
	 * 
	 * @param value
	 * 		{@code true} to peform an engine step.
	 */
	public void switchEngineStep(boolean value)
	{	debugLock.lock();
		stepEngine = value;		
		debugLock.unlock();
	}
	
	/**
	 * Indicates if an engine step was required.
	 * 
	 * @return
	 * 		{@code true} if an engine step was required.
	 */
	public boolean getEngineStep()
	{	boolean result;
		debugLock.lock();
		result = stepEngine;
		debugLock.unlock();
		return result;
	}
	
	/**
	 * Performs the engine step.
	 */
	protected void updateEngineStep()
	{	if(getEngineStep())
		{	if(gameStarted && !gameOver)
			{	//prevTotalGameTime = totalGameTime;
				totalGameTime = totalGameTime + (long)(milliPeriod*Configuration.getEngineConfiguration().getSpeedCoeff());
			}
			switchEngineStep(false);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// ENGINE SPEED		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Increases engine speed.
	 */
	protected void speedUp()
	{	debugLock.lock();
		double coef = Configuration.getEngineConfiguration().getSpeedCoeff()*2;
		Configuration.getEngineConfiguration().setSpeedCoeff(coef);
		debugLock.unlock();
	}
	
	/**
	 * Decreaes engine speed.
	 */
	protected void slowDown()
	{	debugLock.lock();
		double coef = Configuration.getEngineConfiguration().getSpeedCoeff()/2;
		Configuration.getEngineConfiguration().setSpeedCoeff(coef);
		debugLock.unlock();
	}
	
	/////////////////////////////////////////////////////////////////
	// ENTRY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Order of the roles for the before-game sprite entry */
	protected Role[] entryRoles;
	/** Moment each type of sprite appears at the begining of a round */ 
	protected Double[] entryDelays;
	/** Index of the current step in these entries */
	protected int entryIndex = 0;
	/** Text displayed before game starts */
	protected String[] entryTexts;
	
	/**
	 * Handles how sprites enter the zone.
	 */
	protected void initEntries()
	{	entryIndex = 0;
		entryRoles = new Role[]{Role.FLOOR,Role.BLOCK,Role.ITEM,Role.BOMB,Role.HERO};
		entryDelays = new Double[]{0d,0d,0d,0d,0d,0d,GameData.READY_TIME,GameData.SET_TIME,GameData.GO_TIME};
		entryTexts = new String[]{null,null,null,null,null,panel.getMessageTextReady(),panel.getMessageTextSet(),panel.getMessageTextGo()};
		// set the roles
		for(int i=0;i<entryRoles.length;i++)
		{	double duration = level.getEntryDuration(entryRoles[i]);
//System.out.println(entryRoles[i]+":"+duration);
			if(i<entryRoles.length-1)
				duration = duration/2; //so that sprites appear almost at the same time
			entryDelays[i+1] = duration/*+Configuration.getEngineConfiguration().getMilliPeriod()*/;// a little more time, just too be sure it goes OK
			//entryTexts[i] = entryRoles[i].toString(); //actually not used, but hey...
		}
		// unset the messages (for quicklaunch)
		for(int i=entryRoles.length;i<entryTexts.length;i++)
		{	if(entryTexts[i]==null)
				entryDelays[i+1] = 0d;			
		}
//for(int i=0;i<entryDelays.length;i++)
//	System.out.println("entryDelays["+i+"]="+entryDelays[i]);
	}

	/**
	 * Display the appropriate sprites/messages
	 * before the actual beginning of the round.
	 */
	protected void updateEntries()
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
					if(entryIndex<entryDelays.length-1)
					{	SystemControlEvent event = new SystemControlEvent(SystemControlEvent.REQUIRE_NEXT_MESSAGE);
						processEvent(event);
//System.out.println(totalTime+": message");				
					}
					// start the game
					if(entryIndex==entryDelays.length-1) 
					{	SystemControlEvent controlEvent = new SystemControlEvent(SystemControlEvent.REQUIRE_NEXT_MESSAGE);
						processEvent(controlEvent);
//System.out.println(totalTime+": start");				
						EngineEvent engineEvent = new EngineEvent(EngineEvent.ROUND_START);
						level.spreadEvent(engineEvent);
						gameStarted = true;
						done = true;
					}
					entryIndex++;
				}
			}
		}
	}

	/**
	 * Returns the texts displayed when
	 * starting the round.
	 * 
	 * @return
	 * 		Texts displayed at the very begining of a round.
	 */
	public String[] getEntryTexts()
	{	return entryTexts;
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates if this object was destroyed */
	protected boolean finished = false;
	
	@Override
	public void finish()
	{	if(!finished)
		{	super.finish();
			// system listener
			panel.removeKeyListener(systemControl);
			
			// players
			Iterator<AbstractPlayer> i = players.iterator();
			while(i.hasNext())
			{	AbstractPlayer player = i.next();
				if(player instanceof ControlledPlayer)
					panel.removeKeyListener(((ControlledPlayer)player).getSpriteControl());
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

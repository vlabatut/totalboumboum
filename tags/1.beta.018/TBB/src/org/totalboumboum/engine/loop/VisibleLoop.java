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

import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class VisibleLoop extends Loop
{	private static final long serialVersionUID = 1L;
	protected final static boolean VERBOSE = false;
	
	public VisibleLoop(Round round)
	{	super(round);
	}	
	
	/////////////////////////////////////////////////////////////////
	// LEVEL 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Level level;
	
	public Level getLevel()
	{	return level;	
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected List<AbstractPlayer> players = new ArrayList<AbstractPlayer>();
	
	public List<AbstractPlayer> getPlayers()
	{	return players;
	}

	public void playerOut(AbstractPlayer player)
	{	int index = players.indexOf(player);
		round.playerOut(index);
		panel.playerOut(index);	
	}
	
	public abstract AbstractPlayer initPlayer(Profile profile, HollowHeroFactory base, Tile tile) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;

	/////////////////////////////////////////////////////////////////
	// CANCELATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indicates if the game has been canceled */
	protected boolean isCanceled = false;
	protected Lock cancelLock = new ReentrantLock();

	protected void setCanceled(boolean isCanceled)
	{	cancelLock.lock();
		this.isCanceled = isCanceled;
		cancelLock.unlock();
	}
	
	public boolean isCanceled()
	{	boolean result;
		cancelLock.lock();
		result = isCanceled;
		cancelLock.unlock();
		return result;
	}
	
	protected abstract void updateCancel();

	/////////////////////////////////////////////////////////////////
	// INITIALIZING		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Lock loadLock = new ReentrantLock();
	protected Condition cond = loadLock.newCondition();

	protected abstract void load() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, InstantiationException, InvocationTargetException, NoSuchMethodException;

	public void loadStepOver()
	{	round.loadStepOver();
	}
	
	/**
	 * stuff not needing the panel to be initialized
	 */
	protected void startLoopInit()
	{	initLogs();
	}

	/**
	 * stuff to be initialized once the panel is set
	 */
	protected void finishLoopInit()
	{	initEntries();
		initDisplayManager();
	}
	
	/////////////////////////////////////////////////////////////////
	// GAME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean gameStarted = false;
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
		loadLock.unlock();
	}
	
	public void process()
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

			/* 
			 * If frame animation is taking too long, update the game state
			 * without rendering it, to get the updates/sec nearer to the required FPS.
			 */
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
			
			long delta = afterTime-lastTime;
			totalEngineTime = totalEngineTime + delta;
			if(!getEnginePause() && gameStarted && !gameOver)
			{	totalGameTime = totalGameTime + (long)(delta*Configuration.getEngineConfiguration().getSpeedCoeff());
				round.updateTime(totalGameTime);
			}
		}
		
		// logs
		closeLogs();
		
		round.loopOver();
		panel.loopOver();
	}

	protected abstract void update();

	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** total game time elapsed since the players took control */
	protected long totalGameTime = 0;
	/** total real time elapsed since the level started appearing */
	protected long totalEngineTime = 0;
	
	protected void initTimes()
	{	gameStartTime = System.currentTimeMillis();
		prevStatsTime = gameStartTime;
		totalGameTime = 0;
		totalEngineTime = 0;
	}
	
	public long getTotalGameTime()
	{	return totalGameTime;	
	}
	
	public long getTotalEngineTime()
	{	return totalEngineTime;	
	}

	public long getTotalRealTime()
	{	long result = System.currentTimeMillis() - gameStartTime;
		return result;	
	}

	/////////////////////////////////////////////////////////////////
	// ENGINE STATS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    /** number of FPS values stored to get an average */
	protected static int NUM_FPS = 10;   
	protected static long MAX_STATS_INTERVAL = 1000L;
	protected long prevStatsTime;   
	protected long gameStartTime;
	protected long frameCount = 0;
	protected long framesSkipped = 0L;
	protected long statsCount = 0;
	protected double averageFPS = 0.0;
	protected double averageUPS = 0.0;
	protected double fpsStore[] = new double[NUM_FPS];
	protected double upsStore[] = new double[NUM_FPS];

	public double getAverageFPS()
	{	return averageFPS;	
	}
	
	public double getAverageUPS()
	{	return averageUPS;	
	}

	protected void initStats()
	{	for(int k=0;k<NUM_FPS;k++)
		{	fpsStore[k] = 0;
	        upsStore[k] = 0;
		}		
	}
	
	protected void updateStats( )
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
	// CONTROL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected SystemControl systemControl;

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
	protected transient LoopRenderPanel panel;

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
	
	public LoopRenderPanel getPanel()
	{	return panel;
	}
	
	public void draw(Graphics g)
	{	// level
		level.draw(g);

		// display manager
		displayManager.draw(g);
	}

	/////////////////////////////////////////////////////////////////
	// DISPLAY MANAGER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	DisplayManager displayManager = new DisplayManager();
	
	protected abstract void initDisplayManager();
	
	/////////////////////////////////////////////////////////////////
	// LOGS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected EngineConfiguration engineConfiguration = Configuration.getEngineConfiguration();
	
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
	protected boolean pauseEngine = false;
	protected boolean stepEngine = false;
	protected Lock debugLock = new ReentrantLock();

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
	
	protected void updateEngineStep()
	{	if(getEngineStep())
		{	totalGameTime = totalGameTime + (long)(milliPeriod*Configuration.getEngineConfiguration().getSpeedCoeff());
			switchEngineStep(false);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// ENGINE SPEED		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void speedUp()
	{	debugLock.lock();
		double coef = Configuration.getEngineConfiguration().getSpeedCoeff()*2;
		Configuration.getEngineConfiguration().setSpeedCoeff(coef);
		debugLock.unlock();
	}
	
	protected void slowDown()
	{	debugLock.lock();
		double coef = Configuration.getEngineConfiguration().getSpeedCoeff()/2;
		Configuration.getEngineConfiguration().setSpeedCoeff(coef);
		debugLock.unlock();
	}
	
	/////////////////////////////////////////////////////////////////
	// ENTRY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Role[] entryRoles;
	protected Double[] entryDelays;
	protected int entryIndex = 0;
	protected String[] entryTexts;
	
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

	public String[] getEntryTexts()
	{	return entryTexts;
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean finished = false;
	
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

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
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.feature.event.EngineEvent;
import org.totalboumboum.engine.control.system.SystemControl;
import org.totalboumboum.engine.player.Player;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.GameData;
import org.xml.sax.SAXException;

public abstract class VisibleLoop extends Loop
{	private static final long serialVersionUID = 1L;
	
	public VisibleLoop(Round round)
	{	super(round);
	}	
	
	public abstract void init() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, InstantiationException, InvocationTargetException, NoSuchMethodException;

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
	protected List<Player> players = new ArrayList<Player>();
	
	public List<Player> getPlayers()
	{	return players;
	}

	/////////////////////////////////////////////////////////////////
	// SYNCH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Lock loadLock = new ReentrantLock();
	protected Condition cond = loadLock.newCondition();

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
	protected Lock loopLock = new ReentrantLock();
	/** indicates if the game has been canceled */
	protected boolean isCanceled = false;

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

	protected void update()
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
	}

	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** total game time elapsed since the players took control */
	protected long totalGameTime = 0;
	/** total real time elapsed since the level started appearing */
	protected long totalEngineTime = 0;
	
	public long getTotalGameTime()
	{	return totalGameTime;	
	}
	
	public long getTotalEngineTime()
	{	return totalEngineTime;	
	}

	public long getTotalRealTime()
	{	long result = System.currentTimeMillis()-gameStartTime;
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

	protected void storeStats( )
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

	/////////////////////////////////////////////////////////////////
	// GRAPHICS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	transient protected LoopRenderPanel panel;

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
	// LOOP END		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void playerOut(Player player)
	{	int index = players.indexOf(player);
		round.playerOut(index);
		panel.playerOut(index);	
	}

	/////////////////////////////////////////////////////////////////
	// LOAD 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void loadStepOver()
	{	round.loadStepOver();
	}

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
	// DEBUG			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean showGrid = false;
	protected int showTilesPositions = 0;
	protected int showSpritesPositions = 0;
	protected boolean pauseEngine = false;
	protected boolean stepEngine = false;
	protected Lock debugLock = new ReentrantLock();

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

	/////////////////////////////////////////////////////////////////
	// ENTRY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Role[] entryRoles;
	protected Double[] entryDelays;
	protected int entryIndex = 0;
	protected boolean hasStarted = false;
	
	protected void initEntries()
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

	protected void manageEntries()
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
	// SIMULATED		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public boolean isSimulated()
	{	return false;		
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

// TODO gérer différents types de Player: local, IA, remote
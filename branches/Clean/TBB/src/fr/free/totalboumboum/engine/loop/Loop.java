package fr.free.totalboumboum.engine.loop;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.GameConstants;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.engine.container.itemset.Itemset;
import fr.free.totalboumboum.engine.container.level.HollowLevel;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.container.level.Players;
import fr.free.totalboumboum.engine.content.feature.ability.AbilityLoader;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.event.EngineEvent;
import fr.free.totalboumboum.engine.content.feature.permission.PermissionPack;
import fr.free.totalboumboum.engine.content.feature.permission.PermissionPackLoader;
import fr.free.totalboumboum.engine.content.feature.trajectory.TrajectoryPack;
import fr.free.totalboumboum.engine.content.feature.trajectory.TrajectoryPackLoader;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.content.sprite.hero.Hero;
import fr.free.totalboumboum.engine.content.sprite.item.Item;
import fr.free.totalboumboum.engine.control.SystemControl;
import fr.free.totalboumboum.engine.player.Player;
import fr.free.totalboumboum.engine.player.PlayerLocation;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.statistics.StatisticEvent;
import fr.free.totalboumboum.tools.FileTools;

public class Loop implements Runnable
{	private Round round;
	
	public Loop(Round round)
	{	this.round = round;
	}	
	
	private Lock loadLock = new ReentrantLock();
	private Condition cond = loadLock.newCondition();
	
	public void init() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, InstantiationException, InvocationTargetException, NoSuchMethodException
	{	// control
		systemControl = new SystemControl(this);

		// load level
		HollowLevel hollowLevel = round.getHollowLevel();
		hollowLevel.initLevel(this);
		hollowLevel.loadBombset();
		loadStepOver();
		hollowLevel.loadItemset();
		loadStepOver();
		hollowLevel.loadTheme();
		loadStepOver();
		level = hollowLevel.getLevel();

		// load players : common stuff
		String baseFolder = level.getInstancePath()+File.separator+FileTools.FOLDER_HEROES;
		String folder = baseFolder + File.separator+FileTools.FOLDER_ABILITIES;
		ArrayList<AbstractAbility> abilities = AbilityLoader.loadAbilityPack(folder,level);
		folder = baseFolder + File.separator+FileTools.FOLDER_TRAJECTORIES;
		TrajectoryPack trajectoryPack = TrajectoryPackLoader.loadTrajectoryPack(folder,level);
		folder = baseFolder + File.separator+FileTools.FOLDER_PERMISSIONS;
		PermissionPack permissionPack = PermissionPackLoader.loadPermissionPack(folder,level);
//		loadStepOver();		
		// load players : individual stuff
		ArrayList<Profile> profiles = round.getProfiles();
		int remainingPlayers = profiles.size();
		Players plyrs = hollowLevel.getPlayers();
		PlayerLocation[] initialPositions = plyrs.getLocations().get(remainingPlayers);
		ArrayList<String> items = plyrs.getInitialItems();
		Itemset itemset = level.getItemset();
		Iterator<Profile> i = profiles.iterator();
		int j=0;
		while(i.hasNext())
		{	// init
			Profile profile = i.next();
			Player player = new Player(profile,level,abilities,permissionPack,trajectoryPack);
			players.add(player);
			Hero hero = (Hero)player.getSprite();
			// location
			PlayerLocation pl = initialPositions[j];
			level.addHero(hero,pl.getLine(),pl.getCol());
			// initial items
			Iterator<String> it = items.iterator();
			while(it.hasNext())
			{	String name = it.next();
				Item item = itemset.makeItem(name);
				hero.addInitialItem(item);
			}
			// ai
			player.initAi();
			// next player...
			loadStepOver();
			j++;
		}
		
		initEntryDelay();
	}
	
	public void loadStepOver()
	{	round.loadStepOver();
	}
	
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;	
			// system listener
			panel.removeKeyListener(systemControl);
			// players
			Iterator<Player> i = players.iterator();
			while(i.hasNext())
			{	Player temp = i.next();
				panel.removeKeyListener(temp.getSpriteControl());
				temp.finish();
				i.remove();
			}
			// panel
//			panel.finish();
			panel = null;
			// level
			level.finish();
			level = null;
			// round
			round = null;
			// controm
			systemControl.finish();
			systemControl = null;
		}		
	}
	
	/////////////////////////////////////////////////////////////////
	// ROUND			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	/////////////////////////////////////////////////////////////////
	// CONTROL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private SystemControl systemControl;

	/////////////////////////////////////////////////////////////////
	// DEBUG			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean showGrid = false;
	private int showTilesPositions = 0;
	private int showSpritesPositions = 0;
	private boolean showSpeed = false;
	private boolean showTime = false;
	private boolean showFPS = false;
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

	public void setShowFPS(boolean showFPS)
	{	debugLock.lock();
		this.showFPS = showFPS;
		debugLock.unlock();
	}
	public boolean getShowFPS()
	{	boolean result;
		debugLock.lock();
		result = showFPS;
		debugLock.unlock();
		return result;
	}

	public void setShowSpeed(boolean showSpeed)
	{	debugLock.lock();
		this.showSpeed = showSpeed;		
		debugLock.unlock();
	}
	public boolean getShowSpeed()
	{	boolean result;
		debugLock.lock();
		result = showSpeed;
		debugLock.unlock();
		return result;
	}

	public void setShowTime(boolean showTime)
	{	debugLock.lock();
		this.showTime = showTime;		
		debugLock.unlock();
	}
	public boolean getShowTime()
	{	boolean result;
		debugLock.lock();
		result = showTime;
		debugLock.unlock();
		return result;
	}

	public void setShowTilesPositions(int showTilesPositions)
	{	debugLock.lock();
		this.showTilesPositions = showTilesPositions;		
		debugLock.unlock();
	}
	public int getShowTilesPositions()
	{	int result;
		debugLock.lock();
		result = showTilesPositions;
		debugLock.unlock();
		return result;
	}

	public void setShowSpritesPositions(int showSpritesPositions)
	{	debugLock.lock();
		this.showSpritesPositions = showSpritesPositions;		
		debugLock.unlock();
	}
	public int getShowSpritesPositions()
	{	int result;
		debugLock.lock();
		result = showSpritesPositions;
		debugLock.unlock();
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// GRAPHICS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private LoopRenderPanel panel;

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
		loadLock.unlock();	//	
	}
	public LoopRenderPanel getPanel()
	{	return panel;
	}

	/////////////////////////////////////////////////////////////////
	// ENGINE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private long totalTime = 0;
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
	private long milliPeriod;
//	private long nanoPeriod;
	private boolean isPaused = false;
	private Lock loopLock = new ReentrantLock();
	private boolean isCanceled = false;
/*
	private boolean isLooping = false;
	
	public void setLooping(boolean isLooping)
	{	loopLock.lock();
		this.isLooping = isLooping;
		loopLock.unlock();
	}
	public boolean isLooping()
	{	boolean result;
		loopLock.lock();
		result = isLooping;
		loopLock.unlock();
		return result;
	}
*/
	public void setPause(boolean isPaused)
	{	loopLock.lock();
		this.isPaused = isPaused;
		loopLock.unlock();
	}
	public boolean isPaused()
	{	boolean result;
		loopLock.lock();
		result = isPaused;
		loopLock.unlock();
		return result;
	}

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

	public long getTotalTime()
	{	return totalTime;	
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
		loadLock.unlock(); //
	}
	
long totalUpdateTime=0;
long totalDrawTime=0;
long totalMakeupTime=0;
long totalTtime=0;
float nbrUpdates=0;
	
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
		totalTime = 0;

//		setLooping(true);
		while(/*isLooping() && */!isOver())
		{	
			milliPeriod = Configuration.getEngineConfiguration().getMilliPeriod();
			
			// cycle
long a = System.currentTimeMillis();
			update();
long b = System.currentTimeMillis();
			panel.paintScreen();
long c = System.currentTimeMillis();
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

			/* If frame animation is taking too long, update the game state
			   without rendering it, to get the updates/sec nearer to the required FPS. */
			int skips = 0;
			while (excess>milliPeriod 
//					&& skips<MAX_FRAME_SKIPS
					)
			{	excess = excess - milliPeriod;
				// update state but don't render
				update(); 
				skips++;
			}
//System.out.println(skips);
			
			framesSkipped = framesSkipped + skips;
			storeStats( );
			
long d = System.currentTimeMillis();
			
long updateTime = b-a;
long drawTime = c-b;
long makeupTime = d-c;
long ttime = d-a;
totalUpdateTime = totalUpdateTime+updateTime; 
totalDrawTime = totalDrawTime+drawTime; 
totalMakeupTime = totalMakeupTime+makeupTime; 
totalTtime = totalTtime+ttime; 
nbrUpdates++;
/*
System.out.println("update: "+updateTime+"("+(totalUpdateTime/nbrUpdates)+")");
System.out.println("draw: "+drawTime+"("+(totalDrawTime/nbrUpdates)+")");
System.out.println("makeup: "+makeupTime+"("+(totalMakeupTime/nbrUpdates)+")");
System.out.println("---------------");
System.out.println("total: "+ttime+"("+(totalTtime/nbrUpdates)+")");
//System.out.println("\t"+a);
//System.out.println("\t"+b);
//System.out.println("\t"+c);
//System.out.println("\t"+d);
System.out.println();
*/

			
			
			if(!isPaused)
			{	totalTime = totalTime + (afterTime-lastTime);
				round.updateTime(totalTime);				
			}
			
			if(isCanceled())
			{	round.cancelGame();
				setCanceled(false);
			}
		}
		panel.loopOver();
		round.loopOver();
	}

	private void update()
	{	if(!isPaused)
		{	long milliPeriod = Configuration.getEngineConfiguration().getMilliPeriod();
			// celebration ?
			if(celebrationDelay>0)
			{	celebrationDelay = celebrationDelay - (milliPeriod*Configuration.getEngineConfiguration().getSpeedCoeff());
				if(celebrationDelay<=0)
					setOver(true);
			}
			
			// entry ?
			if(entryDelay>=0)
				entryDelay = entryDelay - (milliPeriod*Configuration.getEngineConfiguration().getSpeedCoeff());
					
			// normal update (level and AI)
			getLevel().update();
			Iterator<Player> i = players.iterator();
			while(i.hasNext() && entryDelay<0)
			{	Player temp = i.next();
				if(!temp.isOut())
					temp.update();
			}
		}
	}

	public void drawLevel(Graphics g)
	{	level.draw(g);
	}
	
	public double getAverageFPS()
	{	return averageFPS;	
	}
	public double getAverageUPS()
	{	return averageUPS;	
	}
	
	// used for gathering statistics
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
	private boolean isOver = false;
	
	public void setOver(boolean isOver)
	{	this.isOver = isOver;
		
	}
	public boolean isOver()
	{	return isOver;
	}
	
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
	
	public String getInstancePath()
	{	return level.getInstancePath();
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYERS 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<Player> players = new ArrayList<Player>();
	
	public ArrayList<Player> getPlayers()
	{	return players;
	}

	/////////////////////////////////////////////////////////////////
	// SCALE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private double zoomFactor;	
	private double scaledTileDimension;

	public double getZoomFactor()
	{	return zoomFactor;
	}
	public void setZoomFactor(double zoomFactor)
	{	this.zoomFactor = zoomFactor;
		updateScaledTileDimension();
	}

	public double getScaledTileDimension()
	{	return scaledTileDimension;
	}
	public void updateScaledTileDimension()
	{	scaledTileDimension = GameConstants.STANDARD_TILE_DIMENSION*zoomFactor;
	}
	

	public Round getRound()
	{	return round;	
	}
	
	public void addStatisticEvent(StatisticEvent event)
	{	round.addStatisticEvent(event);
	}
	
	double celebrationDelay = -1;
	double entryDelay = -1;

	public void initEntryDelay()
	{	if(players.size()>0)
		{	Player player = players.get(0);
			Sprite sprite = player.getSprite();
			StateAbility ability = sprite.computeAbility(StateAbility.HERO_ENTRY_DURATION);
			entryDelay = ability.getStrength();
		}
	}
	public void initCelebrationDelay()
	{	if(players.size()>0)
		{	Player player = players.get(0);
			Sprite sprite = player.getSprite();
			StateAbility ability = sprite.computeAbility(StateAbility.HERO_CELEBRATION_DURATION);
			celebrationDelay = ability.getStrength();
		}
		else
			celebrationDelay = 1;
	}
	
	public void reportVictory(int index)
	{	Player player = players.get(index);
		Sprite sprite = player.getSprite();
		EngineEvent event = new EngineEvent(EngineEvent.VICTORY);
		sprite.processEvent(event);
	}
	public void reportDefeat(int index)
	{	Player player = players.get(index);
		Sprite sprite = player.getSprite();
		EngineEvent event = new EngineEvent(EngineEvent.DEFEAT);
		sprite.processEvent(event);
	}
}

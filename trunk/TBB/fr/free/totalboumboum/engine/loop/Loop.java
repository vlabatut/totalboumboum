package fr.free.totalboumboum.engine.loop;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import sun.awt.image.OffScreenImage;

import fr.free.totalboumboum.ai.InterfaceAI;
import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.data.configuration.GameConstants;
import fr.free.totalboumboum.data.profile.Profile;
import fr.free.totalboumboum.data.statistics.StatisticEvent;
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
import fr.free.totalboumboum.game.round.PlayMode;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.tools.FileTools;

public class Loop implements Runnable
{	private Round round;
	
	public Loop(Round round)
	{	this.round = round;
		configuration = round.getConfiguration();
	}	
	
	private Lock loadLock = new ReentrantLock();
	private Condition cond = loadLock.newCondition();
	
	public void init() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
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
				hero.addItem(item);
			}
			// next player...
			loadStepOver();
			j++;
		}
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
			graphics = null;
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
    private Configuration configuration;
	public Configuration getConfiguration()
	{	return configuration;	
	}
	
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
	private boolean showBorders = false;
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

	public void setShowBorders(boolean showBorders)
	{	debugLock.lock();
		this.showBorders = showBorders;
		debugLock.unlock();
	}
	public boolean getShowBorders()
	{	boolean result;
		debugLock.lock();
		result = showBorders;
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
	private Graphics graphics;
	private LoopRenderPanel panel;

	public void setPanel(LoopRenderPanel panel)
	{	loadLock.lock();
		this.panel = panel;
		// graphics
		Image image = panel.getBackgroundImage();
		graphics = image.getGraphics();
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
		loadLock.lock();		
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
	private static int MAX_FRAME_SKIPS = 5;
	/** used to stop the animation thread */
    /** number of FPS values stored to get an average */
	private static int NUM_FPS = 10;   
	// used for gathering statistics
//	private static long MAX_STATS_INTERVAL = 1000000000L;
	private static long MAX_STATS_INTERVAL = 1000L;
	private long statsInterval = 0L;    // in ns
	private long prevStatsTime;   
	private long totalElapsedTime = 0L;
	private long gameStartTime;
	private int timeSpentInGame = 0;    // in seconds
	private long frameCount = 0;
	private long statsCount = 0;
	private double averageFPS = 0.0;
	private double averageUPS = 0.0;
	private long framesSkipped = 0L;
	private long totalFramesSkipped = 0L;
	private double fpsStore[] = new double[NUM_FPS];
	private double upsStore[] = new double[NUM_FPS];
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
	}
	
long totalUpdateTime=0,totalLevelDrawTime=0,totalPanelPaintTime=0,totalTtime=0;
float nbrUpdates=0;
	
	public void process()
	{	long beforeTime,afterTime,timeDiff,sleepTime;
		long overSleepTime = 0L;
		int noDelays = 0;
		long excess = 0L;

		for(int k=0;k<NUM_FPS;k++)
		{	fpsStore[k] = 0;
	        upsStore[k] = 0;
		}		
		
//		nanoPeriod = getConfiguration().getNanoPeriod();
		milliPeriod = getConfiguration().getMilliPeriod();
		
//		gameStartTime = System.nanoTime();
		gameStartTime = System.currentTimeMillis();
		prevStatsTime = gameStartTime;
		beforeTime = gameStartTime;
		totalTime = 0;

//		setLooping(true);
		while(/*isLooping() && */!isOver())
		{	// cycle
long a = System.currentTimeMillis();
			update();
long b = System.currentTimeMillis();
			getLevel().draw(graphics);
long c = System.currentTimeMillis();
			panel.paintScreen();
long d = System.currentTimeMillis();
long updateTime = b-a;
long levelDrawTime = c-b;
long panelPaintTime = d-c;
long ttime = d-a;
totalUpdateTime = totalUpdateTime+updateTime; 
totalLevelDrawTime = totalLevelDrawTime+levelDrawTime; 
totalPanelPaintTime = totalPanelPaintTime+panelPaintTime;
totalTtime = totalTtime+ttime; 
nbrUpdates++;

System.out.println(updateTime+"("+(totalUpdateTime/nbrUpdates)+")");
System.out.println(levelDrawTime+"("+(totalLevelDrawTime/nbrUpdates)+")");
System.out.println(panelPaintTime+"("+(totalPanelPaintTime/nbrUpdates)+")");
System.out.println("---------------");
System.out.println(ttime+"("+(totalTtime/nbrUpdates)+")");
System.out.println("\t"+a);
System.out.println("\t"+b);
System.out.println("\t"+c);
System.out.println("\t"+d);
System.out.println();

			// time process
//			afterTime = System.nanoTime();
			afterTime = System.currentTimeMillis();
			timeDiff = afterTime - beforeTime;
//			sleepTime = nanoPeriod - timeDiff - overSleepTime;
			sleepTime = milliPeriod - timeDiff - overSleepTime;

			// some time left in this cycle
			if(sleepTime>0)
			{	try
				{	
//					Thread.sleep(sleepTime/1000000L); // nano -> ms
					Thread.sleep(sleepTime);
				}
				catch (InterruptedException ex)
				{	//ex.printStackTrace();
				}
//				overSleepTime = System.nanoTime() - afterTime - sleepTime;
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

//			beforeTime = System.nanoTime();
			beforeTime = System.currentTimeMillis();

			/* If frame animation is taking too long, update the game state
			   without rendering it, to get the updates/sec nearer to the required FPS. */
			int skips = 0;
//			while (excess>nanoPeriod && skips<MAX_FRAME_SKIPS)
			while (excess>milliPeriod && skips<MAX_FRAME_SKIPS)
			{	
//				excess = excess - nanoPeriod;
				excess = excess - milliPeriod;
				update(); // update state but don't render
				skips++;
			}
//System.out.println(skips);
			framesSkipped = framesSkipped + skips;
			storeStats( );
			
			if(!isPaused)
			{	
//				totalTime = totalTime + (timeDiff/1000000L);
				totalTime = totalTime + timeDiff;
				round.updateTime(totalTime);				
			}
			
			if(isCanceled())
			{	round.closeGame();
				setCanceled(false);
			}
		}
		panel.loopOver();
		round.loopOver();
	}

	private void update()
	{	if(!isPaused)
		{	// celebration ?
			if(celebrationDelay>0)
			{	celebrationDelay = celebrationDelay - (getConfiguration().getMilliPeriod()*getConfiguration().getSpeedCoeff());
				if(celebrationDelay<=0)
					setOver(true);
			}		
			// normal update (level and AI)
			getLevel().update();
			Iterator<Player> i = players.iterator();
			while(i.hasNext())
			{	Player temp = i.next();
				if(!temp.isOut())
					temp.update();
			}
		}
	}
	
	private void storeStats( )
    {	frameCount++;
//	  	statsInterval = statsInterval + nanoPeriod;
	  	statsInterval = statsInterval + milliPeriod;

      	if (statsInterval>=MAX_STATS_INTERVAL)
      	{	
//      		long timeNow = System.nanoTime();
      		long timeNow = System.currentTimeMillis();
//  			timeSpentInGame = (int)((timeNow - gameStartTime)/1000000000L);  // ns-->secs
  			timeSpentInGame = (int)((timeNow - gameStartTime)/1000L);  // ns-->secs

      		long realElapsedTime = timeNow - prevStatsTime;
      		// time since last stats collection
      		totalElapsedTime = totalElapsedTime + realElapsedTime;

      		double timingError = ((double)(realElapsedTime - statsInterval) / statsInterval) * 100.0;

      		totalFramesSkipped += framesSkipped;

      		double actualFPS = 0;     // calculate the latest FPS and UPS
      		double actualUPS = 0;
      		if(totalElapsedTime>0)
      		{	
//      			actualFPS = (((double)frameCount / totalElapsedTime) * 1000000000L);
      			actualFPS = (((double)frameCount / totalElapsedTime) * 1000L);
//      			actualUPS = (((double)(frameCount + totalFramesSkipped) / totalElapsedTime) * 1000000000L);
      			actualUPS = (((double)(frameCount + totalFramesSkipped) / totalElapsedTime) * 1000L);
      		}

      		// store the latest FPS and UPS
      		fpsStore[(int)statsCount%NUM_FPS] = actualFPS;
      		upsStore[(int)statsCount%NUM_FPS] = actualUPS;
      		statsCount = statsCount+1;

      		double totalFPS = 0.0;     // total the stored FPSs and UPSs
      		double totalUPS = 0.0;
      		for(int i=0;i<NUM_FPS;i++)
      		{	totalFPS += fpsStore[i];
      			totalUPS += upsStore[i];
      		}

      		if (statsCount < NUM_FPS)
      		{	// obtain the average FPS and UPS 
      			averageFPS = totalFPS/statsCount;
      			averageUPS = totalUPS/statsCount;
      		}
      		else
      		{	averageFPS = totalFPS/NUM_FPS;
      		averageUPS = totalUPS/NUM_FPS;
      		}
/*
			System.out.println(
//			timedf.format((double) statsInterval/1000000000L) + " " +
			timedf.format((double) statsInterval/1000L) + " " +
			timedf.format((double) realElapsedTime/1000000000L)+"s "+
//			timedf.format((double) realElapsedTime/1000L)+"s "+
			df.format(timingError) + "% " +
			frameCount + "c " +
			framesSkipped + "/" + totalFramesSkipped + " skip; " +
			df.format(actualFPS) + " " + df.format(averageFPS)+" afps; " +
			df.format(actualUPS) + " " + df.format(averageUPS)+" aups" );
*/
      		framesSkipped = 0;
      		prevStatsTime = timeNow;
      		statsInterval = 0L;   // reset
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
	// OPTIONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public PlayMode getPlayMode()
	{	return round.getPlayMode();
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

	public void initCelebrationDelay()
	{	Player player = players.get(0);
		Sprite sprite = player.getSprite();
		StateAbility ability = sprite.computeAbility(StateAbility.HERO_CELEBRATION_DURATION);
		celebrationDelay = ability.getStrength();
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

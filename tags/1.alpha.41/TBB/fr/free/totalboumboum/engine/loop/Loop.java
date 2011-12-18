package fr.free.totalboumboum.engine.loop;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
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
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.container.level.LevelLoader;
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
	{	// init
		image = round.getPanel().getImage();
		graphics = image.getGraphics();
		systemControl = new SystemControl(this);

		// load level
		level = LevelLoader.loadLevel(round.getLevelDescription().getPath(),this);

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
		PlayerLocation[] initialPositions = level.getPlayersLocations().get(remainingPlayers);
		ArrayList<String> items = level.getPlayersItems();
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
			image = null;
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
	private Image image = null;

	public void setPanel(LoopRenderPanel panel)
	{	loadLock.lock();
		// panel
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
		loadLock.lock();		
	}
	public LoopRenderPanel getPanel()
	{	return panel;
	}

	public Image getImage()
	{	return image;		
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
	
	public void process()
	{	long beforeTime, afterTime, timeDiff, sleepTime;
		long overSleepTime = 0L;
		int noDelays = 0;
		long excess = 0L;

		beforeTime = System.nanoTime();
		totalTime = 0;

//		setLooping(true);
		// The frames of the animation are drawn inside the while loop
		while(/*isLooping() && */!isOver())
		{	update();
			getLevel().draw(graphics);
			panel.paintScreen();

			afterTime = System.nanoTime();
			timeDiff = afterTime - beforeTime;
			if(!isPaused)
			{	totalTime = totalTime + (timeDiff/1000000);
				round.updateTime(totalTime);				
			}
			sleepTime = (getConfiguration().getNanoPeriod() - timeDiff) - overSleepTime;

			if (sleepTime > 0)
			{	// some time left in this cycle
				try
				{	Thread.sleep(sleepTime / 1000000L); // nano -> ms
				}
				catch (InterruptedException ex)
				{	//ex.printStackTrace();
				}
				overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
			}
			else
			{	// sleepTime <= 0; the frame took longer than the period
				excess -= sleepTime; // store excess time value
				overSleepTime = 0L;

				if(++noDelays >= NO_DELAYS_PER_YIELD)
				{	Thread.yield(); // give another thread a chance to run
					noDelays = 0;
				}
			}

			beforeTime = System.nanoTime();

			/* If frame animation is taking too long, update the game state
			   without rendering it, to get the updates/sec nearer to
			   the required FPS. */
			int skips = 0;
			while ((excess > getConfiguration().getNanoPeriod()) && (skips < MAX_FRAME_SKIPS))
			{	excess -= getConfiguration().getNanoPeriod();
				update(); // update state but don't render
				skips++;
			}	
/*			
playerOut(players.get(0));
playerOut(players.get(1));
playerOut(players.get(2));
loopOver = true;
*/		
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
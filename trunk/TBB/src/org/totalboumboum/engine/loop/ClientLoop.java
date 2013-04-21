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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.ai.AisConfiguration;
import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.engine.container.level.hollow.HollowLevel;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.hero.Hero;
import org.totalboumboum.engine.content.sprite.hero.HollowHeroFactory;
import org.totalboumboum.engine.content.sprite.hero.HollowHeroFactoryLoader;
import org.totalboumboum.engine.control.system.LocalSytemControl;
import org.totalboumboum.engine.loop.display.Display;
import org.totalboumboum.engine.loop.display.ais.DisplayAisColors;
import org.totalboumboum.engine.loop.display.ais.DisplayAisPaths;
import org.totalboumboum.engine.loop.display.ais.DisplayAisPause;
import org.totalboumboum.engine.loop.display.ais.DisplayAisTexts;
import org.totalboumboum.engine.loop.display.game.DisplayCancel;
import org.totalboumboum.engine.loop.display.game.DisplayFPS;
import org.totalboumboum.engine.loop.display.game.DisplayMessage;
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
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;
import org.totalboumboum.engine.loop.event.replay.ReplayEvent;
import org.totalboumboum.engine.loop.event.replay.StopReplayEvent;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteChangeAnimeEvent;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteChangePositionEvent;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteCreationEvent;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteEvent;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteInsertionEvent;
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.engine.player.AiPlayer;
import org.totalboumboum.engine.player.HumanPlayer;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.stream.network.client.ClientGeneralConnection;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ClientLoop extends VisibleLoop implements InteractiveLoop, ReplayedLoop
{	private static final long serialVersionUID = 1L;
	
	public ClientLoop(Round round)
	{	super(round);
	}	

	/////////////////////////////////////////////////////////////////
	// INITIALIZING		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void load() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, InstantiationException, InvocationTargetException, NoSuchMethodException
	{	// control
		systemControl = new LocalSytemControl(this);
		
		// init
		List<Profile> profiles = round.getProfiles();
		HollowLevel hollowLevel = round.getHollowLevel();
		Instance instance = hollowLevel.getInstance();
		RoundVariables.instance = instance;
		RoundVariables.loop = this;

		// load level & instance
		hollowLevel.initLevel(this);
		clientConnection = Configuration.getConnectionsConfiguration().getClientConnection();
		zoomCoefficient = RoundVariables.zoomFactor / clientConnection.getZoomCoef();
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
		hollowLevel.synchronizeZone(this);
		loadStepOver();
		
		// load players base
		String baseFolder = FilePaths.getInstancesPath()+File.separator+RoundVariables.instance.getName()+File.separator+FileNames.FILE_HEROES;
		HollowHeroFactory base = HollowHeroFactoryLoader.loadBase(baseFolder);
		
		// create players sprites
		Iterator<Profile> i = profiles.iterator();
		while(i.hasNext())
		{	// get event
			SpriteCreationEvent event;
			do
			{	SpriteEvent tempEvent;
				do
					tempEvent = (SpriteEvent)retrieveEvent();
				while(!(tempEvent instanceof SpriteCreationEvent));
				event = (SpriteCreationEvent)tempEvent;
			}
			while(event.getRole()!=Role.HERO);
			
			// extract info from event
			int col = event.getCol();
			int row = event.getRow();
			int id = event.getSpriteId();
			
			// location
			Tile tile = level.getTile(row,col);
			
			// sprite
			Profile profile = i.next();
			AbstractPlayer player = initPlayer(profile,base,tile);
			hollowLevel.getInstance().initLinks();
			players.add(player);
			pauseAis.add(false);
			
			// level
			Hero hero = (Hero)player.getSprite();
			level.changeSpriteId(hero,id);
System.out.println(hero+" "+hero.getId());
			
			// next player...
			loadStepOver();
		}
		
		// send controlSettings
		List<ControlSettings> controlSettings = new ArrayList<ControlSettings>();
		HashMap<Integer,ControlSettings> map = Configuration.getControlsConfiguration().getControlSettings();
		for(Profile p: profiles)
		{	if(!p.isRemote())
			{	int index = p.getControlSettingsIndex();
				if(index==0)
					controlSettings.add(null);
				else
					controlSettings.add(map.get(index));
			}
		}
		clientConnection.sendControlSettings(controlSettings);
	}
	
	@Override
	protected void startLoopInit()
	{	super.startLoopInit();
		initEvent();
		initAis();
	}

	@Override
	protected void finishLoopInit()
	{	super.finishLoopInit();
		//ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
		clientConnection.loadingComplete();
	}
	
	/////////////////////////////////////////////////////////////////
	// CANCELATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateCancel()
	{	if(isCanceled())
		{	setOver(true);
			setCanceled(false);
		}
	}	

	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public AbstractPlayer initPlayer(Profile profile, HollowHeroFactory base, Tile tile) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{	AbstractPlayer result;
//		if(profile.isRemote())
//			profile.setControlSettingsIndex(0);
		if(profile.hasAi())
			result = new AiPlayer(profile,base,tile);
		else
			result = new HumanPlayer(profile,base,tile);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// AIS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<Boolean> pauseAis = new ArrayList<Boolean>();

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
	
	private void updateAis()
	{	if(gameStarted) // only after the round has started
		{	aiTime = aiTime + milliPeriod;
			if(aiTime >= Configuration.getAisConfiguration().getAiPeriod())
			{	aiTime = 0;
				for(int i=0;i<players.size();i++)
				{	AbstractPlayer player = players.get(i);
					boolean aiPause = getAiPause(i);
					if(!player.isOut() && player instanceof AiPlayer)
						((AiPlayer)player).updateAi(aiPause);
				}
			}
		}
	}
	
	protected void initAis()
	{	for(int i=0;i<players.size();i++)
		{	AbstractPlayer player = players.get(i);
			if(player instanceof AiPlayer)
				((AiPlayer)player).initAi();
		}
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
	// ENGINE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void update()
	{	if(!getEnginePause() || getEngineStep())
		{	updateCancel();
			updateLogs();
			updateEntries();
			level.update();
			updateEvents();
			updateAis();
			updateStats();
//System.err.println("XXXXXXXXXXXXXXXXXXXXXXX update");
		}
	}

	/////////////////////////////////////////////////////////////////
	// ZOOM COEFFICIENT		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private double zoomCoefficient = 1;
	
	public void setZoomCoef(double zoomCoef)
	{	this.zoomCoefficient = zoomCoef;
	}
	
	/////////////////////////////////////////////////////////////////
	// REPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ClientGeneralConnection clientConnection;
	
	/**
	 * always returns an event.
	 * if the list is empty, the thread is blocked until an event arrives
	 */
	@Override
	public ReplayEvent retrieveEvent()
	{	ReplayEvent result = clientConnection.retrieveEvent();
		return result;
	}
	
	private void initEvent()
	{	// get all the remaining useless SpriteEvents
		ReplayEvent tempEvent;
		do
			tempEvent = retrieveEvent();
		while(!(tempEvent instanceof StopReplayEvent));

		// get the first meaningful ReplayEvent
		//currentEvent = RoundVariables.netClientIn.readEvent();
	}
	
	private void updateEvents()
	{	if(!isOver())
		{	if(VERBOSE)
				System.out.println("/////////////////////////////////////////");		
			List<ReplayEvent> events = clientConnection.retrieveEventList(getTotalEngineTime());
	
			// process events
			for(ReplayEvent event: events)
			{	if(VERBOSE)
					System.out.print("["+event.getTime()+"<"+getTotalEngineTime()+"]");		
				
				// sprite insertion
				if(event instanceof SpriteInsertionEvent)
				{	SpriteInsertionEvent siEvent = (SpriteInsertionEvent) event;
					int id = siEvent.getSpriteId();
					Sprite sprite = level.getSprite(id);
					sprite.getTile().addSprite(sprite); // add to the tile (complete the below process)
				}
				
				// sprite creation
				else if(event instanceof SpriteCreationEvent)
				{	SpriteCreationEvent scEvent = (SpriteCreationEvent) event;
					HollowLevel hollowLevel = round.getHollowLevel();
					Sprite sprite = hollowLevel.createSpriteFromEvent(scEvent);
					level.addSprite(sprite); // just add to the level lists, not in the tile
				}
				
				// sprite anime change
				else if(event instanceof SpriteChangeAnimeEvent)
				{	SpriteChangeAnimeEvent scaEvent = (SpriteChangeAnimeEvent) event;
					int id = scaEvent.getSpriteId();
					Sprite sprite = level.getSprite(id);
					if(sprite!=null) //certainly a creation-related anime change, when initializing the gesture
						sprite.processChangeAnimeEvent(scaEvent);
				}
				
				// sprite position change
				else if(event instanceof SpriteChangePositionEvent)
				{	SpriteChangePositionEvent scpEvent = (SpriteChangePositionEvent) event;
					int id = scpEvent.getSpriteId();
					Sprite sprite = level.getSprite(id);
					sprite.processChangePositionEvent(scpEvent,zoomCoefficient);
				}
				
				// finished
				else if(event instanceof StopReplayEvent)
				{	setOver(true);
				}
			}
		}
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
	}

	/////////////////////////////////////////////////////////////////
	// LOGS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
	// SYSTEM CONTROLS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void processEvent(SystemControlEvent event)
	{	super.processEvent(event);
		
		String name = event.getName();
		if(name.equals(SystemControlEvent.REQUIRE_CANCEL_ROUND))
		{	setCanceled(true);
		}
		else if(name.equals(SystemControlEvent.SWITCH_AIS_PAUSE))
		{	int index = event.getIndex();
			switchAiPause(index);
		}
	}
}

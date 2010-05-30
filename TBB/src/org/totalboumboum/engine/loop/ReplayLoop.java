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
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.container.level.hollow.HollowLevel;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.hero.Hero;
import org.totalboumboum.engine.content.sprite.hero.HollowHeroFactory;
import org.totalboumboum.engine.content.sprite.hero.HollowHeroFactoryLoader;
import org.totalboumboum.engine.control.system.ReplaySytemControl;
import org.totalboumboum.engine.loop.display.Display;
import org.totalboumboum.engine.loop.display.DisplayEnginePause;
import org.totalboumboum.engine.loop.display.DisplayFPS;
import org.totalboumboum.engine.loop.display.DisplayGrid;
import org.totalboumboum.engine.loop.display.DisplayMessage;
import org.totalboumboum.engine.loop.display.DisplayPlayersNames;
import org.totalboumboum.engine.loop.display.DisplaySpeed;
import org.totalboumboum.engine.loop.display.DisplaySpritesPositions;
import org.totalboumboum.engine.loop.display.DisplayTilesPositions;
import org.totalboumboum.engine.loop.display.DisplayTime;
import org.totalboumboum.engine.loop.event.control.ControlEvent;
import org.totalboumboum.engine.loop.event.replay.ReplayEvent;
import org.totalboumboum.engine.loop.event.replay.StopReplayEvent;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteChangeAnimeEvent;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteChangePositionEvent;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteCreationEvent;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteEvent;
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.engine.player.ReplayedPlayer;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.xml.sax.SAXException;

public class ReplayLoop extends VisibleLoop
{	private static final long serialVersionUID = 1L;
	private boolean verbose = false;
	
	public ReplayLoop(Round round)
	{	super(round);
	}	

	/////////////////////////////////////////////////////////////////
	// INITIALIZING		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void load() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, InstantiationException, InvocationTargetException, NoSuchMethodException
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
		hollowLevel.synchronizeZone();
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
					tempEvent = (SpriteEvent)RoundVariables.loadEvent();
				while(!(tempEvent instanceof SpriteCreationEvent));
				event = (SpriteCreationEvent)tempEvent;
			}
			while(event.getRole()!=Role.HERO);
			
			// extract info from event
			int col = event.getCol();
			int line = event.getLine();
			int id = event.getSpriteId();
			
			// location
			Tile tile = level.getTile(line,col);
			
			// sprite
			Profile profile = i.next();
			AbstractPlayer player = initPlayer(profile,base,tile);
			hollowLevel.getInstance().initLinks();
			players.add(player);
			
			// level
			Hero hero = (Hero)player.getSprite();
			level.changeSpriteId(hero,id);
			
			// next player...
			loadStepOver();
		}
		long end = System.currentTimeMillis();
		if(verbose)
			System.out.println("total load time: "+(end-start));
		
	}
	
	@Override
	protected void startLoopInit()
	{	super.startLoopInit();
		initEvent();
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
		result = new ReplayedPlayer(profile,base,tile);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// ENGINE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void update()
	{	if(!getEnginePause() || getEngineStep())
		{	updateCancel();
			updateEntries();
			level.update();
			updateEvents();
			updateStats();
		}
	}

	/////////////////////////////////////////////////////////////////
	// REPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ReplayEvent currentEvent = null;
	
	private void initEvent()
	{	// get all the remaining useless SpriteEvents
		ReplayEvent tempEvent;
		do
			tempEvent = (ReplayEvent)RoundVariables.loadEvent();
		while(!(tempEvent instanceof StopReplayEvent));

		// get the first meaningful ReplayEvent
		currentEvent = (ReplayEvent)RoundVariables.loadEvent();
	}
	
	private void updateEvents()
	{	if(!isOver())
		{	// read events
System.out.println("/////////////////////////////////////////");		
			List<ReplayEvent> events = new ArrayList<ReplayEvent>();
			while(currentEvent.getTime()<getTotalEngineTime() && !(currentEvent instanceof StopReplayEvent))
			{	events.add(currentEvent);
System.out.print("["+currentEvent.getTime()+"<"+getTotalEngineTime()+"]");		
				currentEvent = RoundVariables.loadEvent();
			}
	
			// process events
			for(ReplayEvent event: events)
			{	// sprite creation
				if(event instanceof SpriteCreationEvent)
				{	SpriteCreationEvent scEvent = (SpriteCreationEvent) event;
					HollowLevel hollowLevel = round.getHollowLevel();
					Sprite sprite = hollowLevel.createSpriteFromEvent(scEvent);
					level.insertSpriteTile(sprite);
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
					sprite.processChangePositionEvent(scpEvent);
				}
				
				// final event
				else
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
	
		// players names
		display = new DisplayPlayersNames(this);
		displayManager.addDisplay(display);
		
		// speed
		display = new DisplaySpeed();
		displayManager.addDisplay(display);
		
		// time
		display = new DisplayTime(this);
		displayManager.addDisplay(display);
		
		// FPS
		display = new DisplayFPS(this);
		displayManager.addDisplay(display);
		
		// engine pause
		display = new DisplayEnginePause(this);
		displayManager.addDisplay(display);
	}

	/////////////////////////////////////////////////////////////////
	// LOGS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void initLogs()
	{	//useless here
	}

	protected void updateLogs()
	{	//useless here
	}

	protected void closeLogs()
	{	//useless here
	}

	/////////////////////////////////////////////////////////////////
	// ENGINE SPEED		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean fastforward = false;
	private boolean backward = false;
	
	private void switchFastforward()
	{	fastforward = !fastforward;
		if(fastforward)
			speedUp();
		else
			slowDown();
	}
	
	private void switchBackward()
	{	backward = !backward;
		if(backward)
			slowDown();
		else
			speedUp();
	}
	
	/////////////////////////////////////////////////////////////////
	// SYSTEM CONTROLS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void processEvent(ControlEvent event)
	{	String name = event.getName();
		if(name.equals(ControlEvent.REQUIRE_CANCEL_ROUND))
			setCanceled(true);
		else if(name.equals(ControlEvent.REQUIRE_SLOW_DOWN))
			slowDown();
		else if(name.equals(ControlEvent.REQUIRE_SPEED_UP))
			speedUp();
		else if(name.equals(ControlEvent.SWITCH_BACKWARD))
			switchBackward();
		else if(name.equals(ControlEvent.SWITCH_ENGINE_PAUSE))
			switchEnginePause();
		else if(name.equals(ControlEvent.SWITCH_FAST_FORWARD))
			switchFastforward();
		else
			super.processEvent(event);
	}
}

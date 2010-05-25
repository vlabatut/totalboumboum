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
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.container.level.hollow.HollowLevel;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.container.tile.Tile;
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
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteCreationEvent;
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.engine.player.ReplayedPlayer;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.statistics.detailed.StatisticRound;
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
		SpriteCreationEvent event = hollowLevel.synchronizeZone();
		loadStepOver();
		
		// load players base
		String baseFolder = FilePaths.getInstancesPath()+File.separator+RoundVariables.instance.getName()+File.separator+FileNames.FILE_HEROES;
		HollowHeroFactory base = HollowHeroFactoryLoader.loadBase(baseFolder);
		
		// create players sprites
		Iterator<Profile> i = profiles.iterator();
		while(i.hasNext())
		{	// extract info from event
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
			hero.setId(id);
			
			// next player...
			loadStepOver();
		}
		long end = System.currentTimeMillis();
		if(verbose)
			System.out.println("total load time: "+(end-start));
	}
	
	/////////////////////////////////////////////////////////////////
	// CANCELATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateCancel()
	{	setOver(true);
		setCanceled(false);
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
	
	private void updateEvents()
	{	Object o = RoundVariables.loadEvent();
		if(o instanceof ReplayEvent)
		{
			// TODO
		}
		else
		{	StatisticRound stats = (StatisticRound)o;
			round.setStats(stats);
			setOver(true);
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

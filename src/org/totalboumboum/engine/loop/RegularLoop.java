package org.totalboumboum.engine.loop;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.engine.container.itemset.Itemset;
import org.totalboumboum.engine.container.level.hollow.HollowLevel;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.container.level.players.Players;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.feature.action.gather.SpecificGather;
import org.totalboumboum.engine.content.feature.event.ActionEvent;
import org.totalboumboum.engine.content.sprite.hero.Hero;
import org.totalboumboum.engine.content.sprite.hero.HollowHeroFactory;
import org.totalboumboum.engine.content.sprite.hero.HollowHeroFactoryLoader;
import org.totalboumboum.engine.content.sprite.item.Item;
import org.totalboumboum.engine.control.system.LocalSytemControl;
import org.totalboumboum.engine.loop.display.Display;
import org.totalboumboum.engine.loop.display.game.DisplayEnginePause;
import org.totalboumboum.engine.loop.display.game.DisplayEngineStep;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;
import org.totalboumboum.engine.loop.event.replay.StopReplayEvent;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteCreationEvent;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteInsertionEvent;
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.engine.player.AiPlayer;
import org.totalboumboum.engine.player.HumanPlayer;
import org.totalboumboum.engine.player.PlayerLocation;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.xml.sax.SAXException;

/**
 * Loop used to display local, interactive, non-network games.
 *  
 * @author Vincent Labatut
 */
public class RegularLoop extends LocalLoop
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Builds a new regular loop
	 * for the specified round.
	 * 
	 * @param round
	 * 		Round handled by this loop.
	 */
	public RegularLoop(Round round)
	{	super(round);
	}	
	
	/////////////////////////////////////////////////////////////////
	// INITIALIZING		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void load() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, InstantiationException, InvocationTargetException, NoSuchMethodException, URISyntaxException
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
		RoundVariables.writeZoomCoef(RoundVariables.zoomFactor);
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
		hollowLevel.instanciateZone();
		loadStepOver();
		
		// load players base
		String baseFolder = FilePaths.getInstancesPath()+File.separator+RoundVariables.instance.getName()+File.separator+FileNames.FILE_HEROES;
		HollowHeroFactory base = HollowHeroFactoryLoader.loadBase(baseFolder);
		
		// place players
		int remainingPlayers = profiles.size();
		Players plyrs = hollowLevel.getPlayers();
		PlayerLocation[] initialPositions = plyrs.getLocations().get(remainingPlayers);
		if(round.getRandomLocation())
		{	List<PlayerLocation> loc = new ArrayList<PlayerLocation>();
			for(int i=0;i<initialPositions.length;i++)
				loc.add(initialPositions[i]);
			Calendar cal = new GregorianCalendar();
			long seed = cal.getTimeInMillis();
			Random random = new Random(seed);
			Collections.shuffle(loc,random);
			for(int i=0;i<initialPositions.length;i++)
				initialPositions[i] = loc.get(i);
		}
		
		// create sprites and stuff
		Map<String,Integer> items = plyrs.getInitialItems();
		Itemset itemset = instance.getItemset();
		Iterator<Profile> i = profiles.iterator();
		int j=0;
		while(i.hasNext())
		{	// location
			PlayerLocation pl = initialPositions[j];
			Tile tile = level.getTile(pl.getRow(),pl.getCol());
			
			// sprite
			Profile profile = i.next();
			AbstractPlayer player = initPlayer(profile,base,tile);
			hollowLevel.getInstance().initLinks();
			players.add(player);
			pauseAis.add(false);
			recordAiPercepts.add(false);
			lastActionAis.add(0l);
			
			// record/transmit creation event
			SpriteCreationEvent creationEvent = new SpriteCreationEvent(player.getSprite(),Integer.toString(j));
			RoundVariables.writeEvent(creationEvent);
			// record/transmit insertion event
			SpriteInsertionEvent insertionEvent = new SpriteInsertionEvent(player.getSprite());
			RoundVariables.writeEvent(insertionEvent);
			
			// level
			Hero hero = (Hero)player.getSprite();
//			level.addHero(hero,pl.getRow(),pl.getCol());
			
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
			
			// next player...
			loadStepOver();
			j++;
		}
		
// TODO debug
//try {
//	System.out.println("AAAAAAAAAAAAAAA");
//	initAis();
//	Thread.sleep(1000);
//	System.out.println("BBBBBBBBBBBBBBB");
//	for(int k=0;k<10;k++)
//	{	for(int z=0;z<players.size();z++)
//		{	AbstractPlayer player = players.get(z);
//			if(player instanceof AiPlayer)
//			{	((AiPlayer)player).updateAi(false);
//			}
//		}
//		System.out.println("CCCCCCCCCCCCCCC");
//		Thread.sleep(500);
//	}
//	System.out.println("DDDDDDDDDDDDDDD");
//} catch (InterruptedException e) {
//	e.printStackTrace();
//}
		
		// separation event
		StopReplayEvent event = new StopReplayEvent();
		RoundVariables.writeEvent(event);
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYERS 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public AbstractPlayer initPlayer(Profile profile, HollowHeroFactory base, Tile tile) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, URISyntaxException
	{	AbstractPlayer result;
		if(profile.hasAi())
			result = new AiPlayer(profile,base,tile);
		else
			result = new HumanPlayer(profile,base,tile);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// ENGINE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void update() throws IOException
	{	if(!getEnginePause() || getEngineStep())
		{	updateCancel();
			updateEngineStep();
			updateLogs();
			updateCelebration();
			updateEntrances();
			updateSuddenDeath();
			level.update();		
			updateAis();
			updateCycleHistory();
			updateStats();
		}
		
		recordAis();
	}

	/////////////////////////////////////////////////////////////////
	// DISPLAY MANAGER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initDisplayManager()
	{	Display display;
	
		super.initDisplayManager();
	
		// engine pause
		display = new DisplayEnginePause(this);
		displayManager.addDisplay(display);
		// engine step
		display = new DisplayEngineStep(this);
		displayManager.addDisplay(display);
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
		else if(name.equals(SystemControlEvent.REQUIRE_PRINT_SCREEN))
		{	setScreenCapture(true);
		}
		else if(name.equals(SystemControlEvent.REQUIRE_ENGINE_STEP))
		{	switchEngineStep(true);
		}
		else if(name.equals(SystemControlEvent.REQUIRE_SLOW_DOWN))
		{	slowDown();
		}
		else if(name.equals(SystemControlEvent.REQUIRE_SPEED_UP))
		{	speedUp();
		}
		else if(name.equals(SystemControlEvent.REQUIRE_RECORD_AI_PERCEPTS))
		{	int index = event.getIndex();
			switchRecordAiPercepts(index,true);
		}
		else if(name.equals(SystemControlEvent.SWITCH_AIS_PAUSE))
		{	int index = event.getIndex();
			switchAiPause(index);
		}
		else if(name.equals(SystemControlEvent.SWITCH_ENGINE_PAUSE))
		{	int index = event.getIndex();
			if(index==SystemControlEvent.REGULAR)
				switchEnginePause();
		}
		else if(name.equals(SystemControlEvent.CUSTOM_SYSTEM_CONTROL))
		{	int index = event.getIndex();
			customSystemControl(index);
		}
	}
	
	/**
	 * Activates some custom functionality, generally
	 * used for degugging purposes.
	 * 
	 * @param index
	 * 		Numeric parameter related to the custom control.
	 */
	private void customSystemControl(int index)
	{	// here we drop a level bomb on each player
		for(AbstractPlayer player: players)
		{	if(!player.isOut())
			{	Hero sprite = player.getSprite();
				Tile tile = sprite.getTile();
				dropLevelBomb(tile, index);
			}
		}
	}
}

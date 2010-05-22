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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.ai.AisConfiguration;
import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.container.itemset.Itemset;
import org.totalboumboum.engine.container.level.hollow.HollowLevel;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.container.level.players.Players;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.feature.action.gather.SpecificGather;
import org.totalboumboum.engine.content.feature.event.ActionEvent;
import org.totalboumboum.engine.content.feature.event.EngineEvent;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.hero.Hero;
import org.totalboumboum.engine.content.sprite.hero.HollowHeroFactory;
import org.totalboumboum.engine.content.sprite.hero.HollowHeroFactoryLoader;
import org.totalboumboum.engine.content.sprite.item.Item;
import org.totalboumboum.engine.control.system.ServerSytemControl;
import org.totalboumboum.engine.loop.display.Display;
import org.totalboumboum.engine.loop.display.DisplayAisColors;
import org.totalboumboum.engine.loop.display.DisplayAisPaths;
import org.totalboumboum.engine.loop.display.DisplayAisPause;
import org.totalboumboum.engine.loop.display.DisplayAisTexts;
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
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteCreationEvent;
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.engine.player.AiPlayer;
import org.totalboumboum.engine.player.PlayerLocation;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.xml.sax.SAXException;

public class ServerLoop extends VisibleLoop
{	private static final long serialVersionUID = 1L;
	
	public ServerLoop(Round round)
	{	super(round);
	}	
	
	public void init() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, InstantiationException, InvocationTargetException, NoSuchMethodException
	{	// control
		systemControl = new ServerSytemControl(this);
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
		loadStepOver();
		
		// load players : common stuff
		String baseFolder = FilePaths.getInstancesPath()+File.separator+RoundVariables.instance.getName()+File.separator+FileNames.FILE_HEROES;
		HollowHeroFactory base = HollowHeroFactoryLoader.loadBase(baseFolder);
//		loadStepOver();		
		// load players : individual stuff
		int remainingPlayers = profiles.size();
		Players plyrs = hollowLevel.getPlayers();
		PlayerLocation[] initialPositions = plyrs.getLocations().get(remainingPlayers);
		if(round.getRandomLocation())
		{	ArrayList<PlayerLocation> loc = new ArrayList<PlayerLocation>();
			for(int i=0;i<initialPositions.length;i++)
				loc.add(initialPositions[i]);
			Calendar cal = new GregorianCalendar();
			long seed = cal.getTimeInMillis();
			Random random = new Random(seed);
			Collections.shuffle(loc,random);
			for(int i=0;i<initialPositions.length;i++)
				initialPositions[i] = loc.get(i);
		}
		HashMap<String,Integer> items = plyrs.getInitialItems();
		Itemset itemset = instance.getItemset();
		Iterator<Profile> i = profiles.iterator();
		int j=0;
		while(i.hasNext())
		{	// location
			PlayerLocation pl = initialPositions[j];
			Tile tile = level.getTile(pl.getLine(),pl.getCol());
			
			// sprite
			Profile profile = i.next();
			AbstractPlayer player = new AbstractPlayer(profile,base,tile);
			hollowLevel.getInstance().initLinks();
			players.add(player);
			pauseAis.add(false);
			
			// record/transmit event
			SpriteCreationEvent spriteEvent = new SpriteCreationEvent(player.getSprite(),Integer.toString(j));
			RoundVariables.recordEvent(spriteEvent);
			
			// level
			Hero hero = (Hero)player.getSprite();
//			level.addHero(hero,pl.getLine(),pl.getCol());
			
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
long end = System.currentTimeMillis();
System.out.println("total load time: "+(end-start));
		
		// init logs
		initLogs();
		// init entries
		initEntries();
		// init displays
		initDisplayManager();
	}
	
	/////////////////////////////////////////////////////////////////
	// LOGS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AisConfiguration aisConfiguration = Configuration.getAisConfiguration();
	
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
				{	int id = player.getId();
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
	
	protected void updateLogs()
	{	super.updateLogs();
		if(aisConfiguration.getLogExceptions())
		{	OutputStream out = aisConfiguration.getExceptionsLogOutput();
			PrintWriter printWriter = new PrintWriter(out,true);
			printWriter.println("--"+totalGameTime+"ms --------------------------");
		}
	}
	
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
	// DEBUG AIS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<Boolean> pauseAis = new ArrayList<Boolean>();

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
		
	public boolean getAiPause(int index)
	{	boolean result;
		debugLock.lock();
		result = pauseAis.get(index);
		debugLock.unlock();
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// ENGINE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** AIs current time, used to inforce AIs period defined in the game options */
	private long aiTime = 0;

	public void process()
	{	aiTime = 0;
		super.process();
	}

	protected void update()
	{	if(!getEnginePause() || getEngineStep())
		{	super.update();
			
			// update AIs
			if(hasStarted) // only after the round has started
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
	}

	/////////////////////////////////////////////////////////////////
	// DISPLAY MANAGER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void initDisplayManager()
	{	Display display;

		// pre-round messages (ready-steady-go)
		display = new DisplayMessage();
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
	// CELEBRATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	double celebrationDuration = -1;

	public void initCelebrationDuration()
	{	if(players.size()>0)
		{	AbstractPlayer player = players.get(0);
			Sprite sprite = player.getSprite();
			StateAbility ability = sprite.modulateStateAbility(StateAbilityName.HERO_CELEBRATION_DURATION);
			celebrationDuration = ability.getStrength();
		}
		else
			celebrationDuration = 1;
	}
	
	public void reportVictory(int index)
	{	AbstractPlayer player = players.get(index);
		Sprite sprite = player.getSprite();
		EngineEvent event = new EngineEvent(EngineEvent.CELEBRATION_VICTORY);
		sprite.processEvent(event);
	}
	
	public void reportDefeat(int index)
	{	AbstractPlayer player = players.get(index);
		Sprite sprite = player.getSprite();
		EngineEvent event = new EngineEvent(EngineEvent.CELEBRATION_DEFEAT);
		sprite.processEvent(event);
	}

	/////////////////////////////////////////////////////////////////
	// SYSTEM CONTROLS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void processEvent(ControlEvent event)
	{	String name = event.getName();
		if(name.equals(ControlEvent.SWITCH_AIS_PAUSE))
			switchEngineStep(true);
		else
			super.processEvent(event);
	}
}

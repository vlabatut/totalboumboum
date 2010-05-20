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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.NumberFormat;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.ai.AbstractAiManager;
import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.ai.AisConfiguration;
import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.container.itemset.Itemset;
import org.totalboumboum.engine.container.level.hollow.HollowLevel;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.container.level.players.Players;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.Role;
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
import org.totalboumboum.engine.loop.event.sprite.SpriteCreationEvent;
import org.totalboumboum.engine.player.Player;
import org.totalboumboum.engine.player.PlayerLocation;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.calculus.CalculusTools;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.time.TimeUnit;
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
			Player player = new Player(profile,base,tile);
			hollowLevel.getInstance().initLinks();
			players.add(player);
			pauseAis.add(false);
			showAiPaths.add(false);
			showAiTileTexts.add(false);
			showAiTileColors.add(false);
			
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
			
			// ai
			player.initAi();
			
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
	private final ArrayList<Boolean> showAiPaths = new ArrayList<Boolean>();
	private final ArrayList<Boolean> showAiTileTexts = new ArrayList<Boolean>();
	private final ArrayList<Boolean> showAiTileColors = new ArrayList<Boolean>();

	public void switchAiPause(int index)
	{	debugLock.lock();
		if(index<pauseAis.size())
		{	boolean pause = pauseAis.get(index);
			if(pause)
				pause = false;
			else if(players.get(index).getArtificialIntelligence()!=null)
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

	public void switchShowAiPaths(int index)
	{	debugLock.lock();
		if(index<showAiPaths.size())
		{	boolean show = showAiPaths.get(index);
			if(players.get(index).getArtificialIntelligence()!=null)
				show = !show;
			showAiPaths.set(index,show);
		}
		debugLock.unlock();
	}
	public void switchShowAiTileTexts(int index)
	{	debugLock.lock();
		if(index<showAiTileTexts.size())
		{	boolean show = showAiTileTexts.get(index);
			if(players.get(index).getArtificialIntelligence()!=null)
				show = !show;
			showAiTileTexts.set(index,show);
		}
		debugLock.unlock();
	}
	public void switchShowAiTileColors(int index)
	{	debugLock.lock();
		if(index<showAiTileColors.size())
		{	boolean show = showAiTileColors.get(index);
			if(players.get(index).getArtificialIntelligence()!=null)
				show = !show;
			showAiTileColors.set(index,show);
		}
		debugLock.unlock();
	}
		
	public boolean getShowAiPaths(int index)
	{	boolean result;
		debugLock.lock();
		result = showAiPaths.get(index);
		debugLock.unlock();
		return result;
	}
	public boolean getShowAiTileTexts(int index)
	{	boolean result;
		debugLock.lock();
		result = showAiTileTexts.get(index);
		debugLock.unlock();
		return result;
	}
	public boolean getShowAiTileColors(int index)
	{	boolean result;
		debugLock.lock();
		result = showAiTileColors.get(index);
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
					{	Player player = players.get(i);
						boolean aiPause = getAiPause(i);
						if(!player.isOut())
							player.update(aiPause);
					}
				}
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// CELEBRATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	double celebrationDuration = -1;

	public void initCelebrationDuration()
	{	if(players.size()>0)
		{	Player player = players.get(0);
			Sprite sprite = player.getSprite();
			StateAbility ability = sprite.modulateStateAbility(StateAbilityName.HERO_CELEBRATION_DURATION);
			celebrationDuration = ability.getStrength();
		}
		else
			celebrationDuration = 1;
	}
	
	public void reportVictory(int index)
	{	Player player = players.get(index);
		Sprite sprite = player.getSprite();
		EngineEvent event = new EngineEvent(EngineEvent.CELEBRATION_VICTORY);
		sprite.processEvent(event);
	}
	
	public void reportDefeat(int index)
	{	Player player = players.get(index);
		Sprite sprite = player.getSprite();
		EngineEvent event = new EngineEvent(EngineEvent.CELEBRATION_DEFEAT);
		sprite.processEvent(event);
	}

	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void draw(Graphics g)
	{	// level
		level.draw(g);

		// ais data
		drawAisInfo(g);
		// FPS
		if(getShowFPS())
			drawFPS(g);
		// pause
		if(getEnginePause())
			drawEnginePause(g);

		drawAisPause(g);
	}

	/**
	 * NOTE
	 * level
	 * players names
	 * ais data
	 * speed
	 * time
	 * FPS
	 * pause
	 */
	
	/*
	 * TODO finir le système de displays :
	 * 	- comment gérer le lock des pauses ? >> niveau de loop (ou display) ?
	 *  - display à mettre dans level ? après tout, y a déjà grille et cie.
	 *    changer tout ça en display, qui gèreraient leurs évts clavier d'une façon ou d'une autre ?
	 */
	
	private void drawEnginePause(Graphics g)
	{	g.setColor(Color.MAGENTA);
		Font font = new Font("Dialog", Font.PLAIN, 18);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		String text = "Engine paused";
		Rectangle2D box = metrics.getStringBounds(text, g);
		int x = 10;
		int y = (int)Math.round(70+box.getHeight()/2);
		g.drawString(text, x, y);
	}

	private void drawAisPause(Graphics g)
	{	g.setColor(Color.MAGENTA);
		Font font = new Font("Dialog", Font.BOLD, 12);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		String text = "AI Paused";
		Rectangle2D box = metrics.getStringBounds(text, g);
		for(int i=0;i<players.size();i++)
		{	Player player = players.get(i);
			if(pauseAis.get(i) && !player.isOut())
			{	Sprite s = player.getSprite();
				int x = (int)Math.round(s.getCurrentPosX()-box.getWidth()/2);
				int y = (int)Math.round(s.getCurrentPosY()+box.getHeight()/2);
				g.drawString(text, x, y);
			}
		}
	}
	
	private static final int AI_INFO_ALPHA_LEVEL = 100;
	private void drawAisInfo(Graphics g)
	{	Graphics2D g2 = (Graphics2D)g;
		double tileSize = RoundVariables.scaledTileDimension;
		for(int i=0;i<players.size();i++)
		{	Player player = players.get(i);
			if(player.hasAi())
			{	AbstractAiManager<?> aiMgr = player.getArtificialIntelligence();
				// tile colors
				if(getShowAiTileColors(i))
				{	Color[][] colors = aiMgr.getTileColors();
					for(int line=0;line<level.getGlobalHeight();line++)
					{	for(int col=0;col<level.getGlobalWidth();col++)
						{	Color color = colors[line][col];
							if(color!=null)
							{	Color paintColor = new Color(color.getRed(),color.getGreen(),color.getBlue(),AI_INFO_ALPHA_LEVEL);
								g2.setPaint(paintColor);
								Tile tile = level.getTile(line,col);
								double x = tile.getPosX()-tileSize/2;
								double y = tile.getPosY()-tileSize/2;
								g2.fill(new Rectangle2D.Double(x,y,tileSize,tileSize));
							}
						}
					}
				}
				// paths
				if(getShowAiPaths(i))
				{	List<List<Tile>> paths = aiMgr.getPaths();
					List<Color> colors = aiMgr.getPathColors();
					Stroke prevStroke = g2.getStroke();
					int thickness = (int)(tileSize/3);
					Stroke stroke = new BasicStroke(thickness,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
					g2.setStroke(stroke);
					for(int j=0;j<paths.size();j++)
					{	List<Tile> path = paths.get(j);
						Color color = colors.get(j);
						if(color!=null && !path.isEmpty())
						{	Color paintColor = new Color(color.getRed(),color.getGreen(),color.getBlue(),AI_INFO_ALPHA_LEVEL);
							g2.setPaint(paintColor);
							Tile tile2 = path.get(0);
							double x1,x2 = tile2.getPosX();
							double y1,y2 = tile2.getPosY();
							Path2D shape = new Path2D.Double();
							shape.moveTo(x2,y2);
							int k = 1;
							while(k<path.size())
							{	// tiles
								x1 = x2;
								y1 = y2;
								tile2 = path.get(k);							
								x2 = tile2.getPosX();
								y2 = tile2.getPosY();
								// directions (to manage the case where the path cross the level off-scree)
								Direction direction12 = level.getDirection(x1,y1,x2,y2);
								int[] intDir12 = direction12.getIntFromDirection();
								Direction direction21 = direction12.getOpposite();
								int[] intDir21 = direction21.getIntFromDirection();
								// alternative locations
								double x1b = x2 + intDir21[0]*tileSize;
								double y1b = y2 + intDir21[1]*tileSize;
								double x2b = x1 + intDir12[0]*tileSize;
								double y2b = y1 + intDir12[1]*tileSize;
								// compare actual and theoretical positions
								if(!CalculusTools.isRelativelyEqualTo(x1,x1b) || !CalculusTools.isRelativelyEqualTo(y1,y1b))
								{	shape.lineTo(x2b,y2b);
									g2.draw(shape);
									shape = new Path2D.Double();
									shape.moveTo(x1b,y1b);
									shape.lineTo(x2,y2);
								}
								else
									shape.lineTo(x2,y2);
								k++;
							}
							g2.draw(shape);
						}
					}
					g2.setStroke(prevStroke);
				}
				// tile texts
				if(getShowAiTileTexts(i))
				{	g.setColor(Color.MAGENTA);
					Font font = new Font("Dialog", Font.PLAIN, 15);
					g.setFont(font);
					FontMetrics metrics = g.getFontMetrics(font);
					String[][] texts = aiMgr.getTileTexts();
					for(int line=0;line<level.getGlobalHeight();line++)
					{	for(int col=0;col<level.getGlobalWidth();col++)
						{	String text = texts[line][col];
							if(text!=null)
							{	Tile tile = level.getTile(line,col);
								Rectangle2D box = metrics.getStringBounds(text, g);
								int x = (int)Math.round(tile.getPosX()-box.getWidth()/2);
								int y = (int)Math.round(tile.getPosY()+box.getHeight()/2);
								g.drawString(text, x, y);
							}
						}
					}
				}
			}
		}
		
		g.setColor(Color.MAGENTA);
		Font font = new Font("Dialog", Font.BOLD, 12);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		String text = "AI Paused";
		Rectangle2D box = metrics.getStringBounds(text, g);
		for(int i=0;i<players.size();i++)
		{	if(pauseAis.get(i))
			{	Sprite s = players.get(i).getSprite();
				int x = (int)Math.round(s.getCurrentPosX()-box.getWidth()/2);
				int y = (int)Math.round(s.getCurrentPosY()+box.getHeight()/2);
				g.drawString(text, x, y);
			}
		}
	}

}

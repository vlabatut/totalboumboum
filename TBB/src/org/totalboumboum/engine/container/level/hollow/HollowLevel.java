package org.totalboumboum.engine.container.level.hollow;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.container.bombset.Bombset;
import org.totalboumboum.engine.container.fireset.Fireset;
import org.totalboumboum.engine.container.fireset.FiresetMap;
import org.totalboumboum.engine.container.itemset.Itemset;
import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.level.info.LevelInfo;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.container.level.players.Players;
import org.totalboumboum.engine.container.level.zone.Zone;
import org.totalboumboum.engine.container.level.zone.ZoneTile;
import org.totalboumboum.engine.container.theme.Theme;
import org.totalboumboum.engine.container.theme.ThemeLoader;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.feature.event.EngineEvent;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.block.Block;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;
import org.totalboumboum.engine.content.sprite.floor.Floor;
import org.totalboumboum.engine.content.sprite.item.Item;
import org.totalboumboum.engine.loop.ReplayedLoop;
import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.loop.event.replay.ReplayEvent;
import org.totalboumboum.engine.loop.event.replay.StopReplayEvent;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteChangeAnimeEvent;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteCreationEvent;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.images.PredefinedColor;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HollowLevel implements Serializable
{	private static final long serialVersionUID = 1L;

	public HollowLevel()
	{		
	}

	/////////////////////////////////////////////////////////////////
	// LEVEL INFO 		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private LevelInfo levelInfo;
	
	public LevelInfo getLevelInfo()
	{	return levelInfo;		
	}
	
	public void setLevelInfo(LevelInfo levelInfo)
	{	this.levelInfo = levelInfo;	
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Players players;

	public Players getPlayers()
    {	return players;    	
    }
	
	public void setPlayers(Players players)
	{	this.players = players;		
	}

	/////////////////////////////////////////////////////////////////
	// INSTANCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private transient Instance instance;

	public Instance getInstance()
    {	return instance;
    }
    
	public void setInstance(Instance instance)
	{	this.instance = instance;
	}

	/////////////////////////////////////////////////////////////////
	// ZONE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Zone zone;
	private HashMap<String,Integer> itemCounts = new HashMap<String, Integer>();
	
	public void setItemCounts(HashMap<String,Integer> itemCounts)
	{	this.itemCounts = itemCounts;
	}
	
	public void makeZone(long timeLimit)
	{	if(zone!=null)
			zone.makeMatrix(timeLimit);		
	}
	
	public HashMap<String,Integer> getItemCount()
	{	HashMap<String,Integer> result;
		if(zone!=null)
			result = zone.getItemCount();
		else
			result = itemCounts;
		return result;
	}
	
	public Zone getZone()
    {	return zone;
    }
	
	public void setZone(Zone zone)
	{	this.zone = zone;		
	}
	
	/**
	 * Used to create the sprites whose names
	 * have been alread loaded in the zone object.
	 */
	public void instanciateZone()
	{	// init zone
		Tile[][] matrix = level.getMatrix();
		double globalLeftX = level.getPixelLeftX();
		double globalUpY = level.getPixelTopY();
		int globalHeight = levelInfo.getGlobalHeight();
		int globalWidth = levelInfo.getGlobalWidth();
		ZoneTile[][] m = zone.getMatrix();
		
		// init tiles
		for(int row=0;row<globalHeight;row++)
		{	for(int col=0;col<globalWidth;col++)
			{	double x = globalLeftX + RoundVariables.scaledTileDimension/2 + col*RoundVariables.scaledTileDimension;
				double y = globalUpY + RoundVariables.scaledTileDimension/2 + row*RoundVariables.scaledTileDimension;
				matrix[row][col] = new Tile(level,row,col,x,y);

				initSprites(m[row][col],matrix[row][col],true);
			}
		}
		level.initTileList();
		
		// separation event
		StopReplayEvent event = new StopReplayEvent();
		RoundVariables.writeEvent(event);
	}
	
	/**
	 * Initializes the sprites for the specified tile, using
	 * the specified ZoneTile (basically a list of the names
	 * of the sprites to be created). The force parameter
	 * allows to force the creation of basic floor when the 
	 * floor type is not explicitly stated in the ZoneTile object.
	 * 
	 * @param zoneTile
	 * 		Object containing the names of the sprites to be created.
	 * @param tile
	 * 		Tile to contain the created sprites.
	 * @param force
	 * 		Force the creation of floor when not specified.
	 * @return
	 * 		The list of created sprites.
	 */
	private List<Sprite> initSprites(ZoneTile zoneTile, Tile tile, boolean force)
	{	List<Sprite> result = new ArrayList<Sprite>();
	
		// floors
		String floorName = zoneTile.getFloor();
		if(floorName==null && force)
		{	Floor floor = theme.makeFloor(tile);
			level.insertSpriteTile(floor);
			result.add(floor);
		}
		else if(floorName!=null)
		{	Floor floor = theme.makeFloor(floorName,tile);
			level.insertSpriteTile(floor);
			result.add(floor);
		}
		
		// blocks
		String blockName = zoneTile.getBlock();
		if(blockName!=null)
		{	Block block = theme.makeBlock(blockName,tile);
			level.insertSpriteTile(block);
			result.add(block);
		}
		
		// items
		String itemName = zoneTile.getItem();
		if(itemName!=null)
		{	Itemset itemset = instance.getItemset();
			Item item = itemset.makeItem(itemName,tile);
			level.insertSpriteTile(item);				
			result.add(item);
		}
		
		// bombs
		String bombName = zoneTile.getBomb();
		if(bombName!=null)
		{	Bombset bombset = instance.getBombsetMap().getBombset(null);
			String temp[] = bombName.split(Theme.PROPERTY_SEPARATOR);
			int range = Integer.parseInt(temp[temp.length-2]);
			int duration = Integer.parseInt(temp[temp.length-1]);
			String name = "";
			for(int i=0;i<temp.length-2;i++)
				name = name + temp[i];
			Bomb bomb = bombset.makeBomb(name,tile,range,duration);
			if(bomb==null)
				System.err.println("makeBomb error: sprite "+name+" not found.");
			
			level.insertSpriteTile(bomb);				
			result.add(bomb);
		}
		
		return result;
	}
	
	/**
	 * Checks if some sprites should fall from the sky.
	 * If it's the case, creates them.
	 * 
	 * Summary of the expected behaviors of the sprites during
	 * sudden death fall:
	 * 			x floor
	 * 				x sur case vide : apparait
	 * 				x sur item : prendre l'item pr vérifier
	 * 				x sur softwall, détruire le bloc pr vérifier
	 * 				x sur hardwall, juste pour tester
	 * 				x sur shrinkwall, juste pour tester
	 * 				x sur bombe : apparait dessous
	 * 				x sur joueur : apparait dessous
	 * 			x item
	 * 				x sur case vide
	 * 				x sur item : attend que l'item soit pris
	 * 				x sur softwall : apparait à sa destruction
	 * 				x sur hardwall : juste pour tester
	 * 				x sur bombe : attend que la bombe explose
	 * 				x sur joueur : attend qu'il bouge
	 * 			x bomb
	 * 				x sur case vide
	 * 				x sur item : rebond
	 * 				x sur softwall : rebond
	 * 				x sur hardwall : rebond
	 * 				x sur shrinkwall : rebond
	 * 				x sur bombe : rebond
	 * 				x sur joueur : atterit normalement (le joueur devrait normalement être étourdi)
	 * 			x softwall
	 * 				x sur case vide : exploser pour voir le sol
	 * 				x sur bombe : rebondit
	 * 				x sur item : recouvre seulement l'item sans le crusher (détruire bloc pr tester, ou throughwalls)
	 * 				x sur softwall : rebond
	 * 				x sur hardwall : rebond
	 * 				x sur shrinkwall : rebond
	 * 				x sur joueur : crush, sauf si throughwall
	 * 			x hardwall
	 * 				x sur case vide
	 * 				x sur bombe : crush (vérifier en debuggant)
	 * 				x sur item : crush (vérifier en debuggant)
	 * 				x sur softwall : crush (vérif graphique)
	 * 				x sur hardwall : rebond
	 * 				x sur shrinkwall : rebond
	 * 				x sur joueur : crush, toujours (y compris through wall)
	 * 			x shrinkwall
	 * 				x sur case vide
	 * 				x sur bombe : crush (vérifier en debuggant)
	 * 				x sur item : crush (vérifier en debuggant)
	 * 				x sur softwall : crush (vérif graphique)
	 * 				x sur hardwall : crush (vérif graphique)
	 * 				x sur shrinkwall : rebond
	 * 				x sur joueur : crush, toujours (y compris through wall)
	 * 			x softwall + item 
	 * 				x sur case vide
	 * 			x softwall + bomb  
	 * 				x sur case vide
	 * 
	 * @param currentGameTime
	 * 		The current game time, in ms.
	 */
	public void applySuddenDeath(long currentGameTime)
	{	// get the info of the sprites to be created
		List<ZoneTile> tiles = zone.getEventsInit(currentGameTime);

		// pre-init the necessary events
		EngineEvent fallEvent = new EngineEvent(EngineEvent.START_FALL);
		fallEvent.setDirection(Direction.NONE);
		EngineEvent enterEvent = new EngineEvent(EngineEvent.ROUND_ENTER);
		enterEvent.setDirection(Direction.NONE);
		
		// create the sprites
		Tile[][] matrix = level.getMatrix();
		for(ZoneTile zoneTile: tiles)
		{	// create the sprites
			int col = zoneTile.getCol();
			int row = zoneTile.getRow();
			Tile tile = matrix[row][col];
			List<Sprite> sprites = initSprites(zoneTile, tile, false);
			
			// checks if there's a block amongst them
			Iterator<Sprite> it = sprites.iterator();
			Block block = null;
			while(it.hasNext() && block==null)
			{	Sprite sprite = it.next();
				if(sprite instanceof Block)
						block = (Block)sprite;
			}
			
// block, bomb, floor, item
System.out.println("["+tile.getRow()+","+tile.getCol()+"] sudden death event processed");			
// TODO TODO what should we send to the client so that its own sprites also fall?
			
			// put the possible block at the begining of the list
			if(block!=null)
			{	sprites.remove(block);
				sprites.add(0,block);
			}
			
			// init the new sprites actions
			for(Sprite sprite: sprites)
			{	if(block!=null && (sprite instanceof Item || sprite instanceof Bomb))
					sprite.processEvent(enterEvent);
				else
					sprite.processEvent(fallEvent);
			}
		}
	}
	
	/**
	 * similar to {@link #instanciateZone()}, but for
	 * replay/client loops, i.e. the sprites come from
	 * a stream, and not the zone obejct.
	 * 
	 * @param loop
	 */
	public void synchronizeZone(ReplayedLoop loop)
	{	// init zone
		Tile[][] matrix = level.getMatrix();
		double globalLeftX = level.getPixelLeftX();
		double globalUpY = level.getPixelTopY();
		int globalHeight = levelInfo.getGlobalHeight();
		int globalWidth = levelInfo.getGlobalWidth();
		
		// init tiles
		for(int row=0;row<globalHeight;row++)
		{	for(int col=0;col<globalWidth;col++)
			{	double x = globalLeftX + RoundVariables.scaledTileDimension/2 + col*RoundVariables.scaledTileDimension;
				double y = globalUpY + RoundVariables.scaledTileDimension/2 + row*RoundVariables.scaledTileDimension;
				matrix[row][col] = new Tile(level,row,col,x,y);
			}
		}
		
		// init sprites
		ReplayEvent tempEvent = loop.retrieveEvent();
		do
		{	// creation
			if(tempEvent instanceof SpriteCreationEvent)
			{	SpriteCreationEvent event = (SpriteCreationEvent) tempEvent;
				Sprite sprite = createSpriteFromEvent(event);
				level.insertSpriteTile(sprite);
			}
			// anime
			else if(tempEvent instanceof SpriteChangeAnimeEvent)
			{	SpriteChangeAnimeEvent event = (SpriteChangeAnimeEvent) tempEvent;
				int id = event.getSpriteId();
				Sprite sprite = level.getSprite(id);
				if(sprite!=null)
					sprite.processChangeAnimeEvent(event);
			}
			// next event
			tempEvent = loop.retrieveEvent();
		}
		while(!(tempEvent instanceof StopReplayEvent));
		
		level.initTileList();
	}

	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Sprite createSpriteFromEvent(SpriteCreationEvent event)
	{	Tile[][] matrix = level.getMatrix();
		
		int row = event.getRow();
		int col = event.getCol();
		//PredefinedColor color = event.getColor();
		Role role = event.getRole();
		String name = event.getName();
		int id = event.getSpriteId();
		//long time = sce.getTime();
		Sprite sprite = null;
		
		// floors
		if(role==Role.FLOOR)
		{	sprite = theme.makeFloor(name,matrix[row][col]);
			sprite.setId(id);
		}
		
		// blocks
		else if(role==Role.BLOCK)
		{	sprite = theme.makeBlock(name,matrix[row][col]);
			sprite.setId(id);
		}
		
		// items
		else if(role==Role.ITEM)
		{	Itemset itemset = instance.getItemset();
			sprite = itemset.makeItem(name,matrix[row][col]);
			sprite.setId(id);
		}
		
		// bombs
		else if(role==Role.BOMB)
		{	String names[] = name.split("/");
			PredefinedColor color = null;
			if(!names[1].equalsIgnoreCase(null))
				color = PredefinedColor.valueOf(names[1]);
			Bombset bombset = instance.getBombsetMap().getBombset(color);
			sprite = bombset.makeBomb(names[0],matrix[row][col],0,-1);
			sprite.setId(id);
		}
		
		// fires
		else if(role==Role.FIRE)
		{	String names[] = name.split("/");
			FiresetMap firesetMap = instance.getFiresetMap();
			Fireset fireset = firesetMap.getFireset(names[0]);
			sprite = fireset.makeFire(names[1],matrix[row][col]);
			sprite.setId(id);
		}
		
		// heroes
		else if(role==Role.HERO)
		{	//TODO
		}
		
		return sprite;
	}
	
	/////////////////////////////////////////////////////////////////
	// THEME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    private transient Theme theme;
	
	public void loadTheme() throws SAXException, IOException, ParserConfigurationException, ClassNotFoundException
    {	// theme
    	String individualFolder = FilePaths.getInstancesPath()+File.separator+levelInfo.getInstanceName();
    	individualFolder = individualFolder+File.separator+FileNames.FILE_THEMES+File.separator+levelInfo.getThemeName();
    	theme = ThemeLoader.loadTheme(individualFolder);
//		level.setTheme(theme);
    	
		instance.initLinks();
		theme.setInstance(instance);
	}
    
	/////////////////////////////////////////////////////////////////
	// LEVEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	transient private Level level;

	public Level getLevel()
    {	return level;    
    }
    
	public void initLevel(VisibleLoop loop)
	{	// init
    	level = new Level(loop);
		Dimension panelDim = Configuration.getVideoConfiguration().getPanelDimension();
    	double sizeX = panelDim.width;
    	double sizeY = panelDim.height;
		// matrix
    	int globalHeight = levelInfo.getGlobalHeight();
    	int globalWidth = levelInfo.getGlobalWidth();
		Tile[][] matrix = new Tile[globalHeight][globalWidth];
		level.setMatrix(matrix);
		
		// visible part
		int visibleHeight = levelInfo.getVisibleHeight();
		int visibleWidth = levelInfo.getVisibleWidth();
		int visibleUpRow = levelInfo.getVisiblePositionUpRow();
		int visibleLeftCol = levelInfo.getVisiblePositionLeftCol();
		if(levelInfo.getForceAll())
		{	visibleWidth = globalWidth;
			visibleHeight = globalHeight;
			visibleLeftCol = 0;
			visibleUpRow = 0;
		}
		
		// zoom factor
		double standardTileDimension = GameData.STANDARD_TILE_DIMENSION;
//		double trueRatioX = sizeX/visibleWidth/standardTileDimension;
//		double trueRatioY = sizeY/visibleHeight/standardTileDimension;
//		double trueZoom = Math.min(trueRatioX,trueRatioY);
		double ratioX = (int)(sizeX/visibleWidth)/standardTileDimension;
		double ratioY = (int)(sizeY/visibleHeight)/standardTileDimension;
		double zoomFactor = Math.min(ratioX,ratioY);
		RoundVariables.setZoomFactor(zoomFactor);
//configuration.setZoomFactor(1.0f);
		
		// position
		double posX = sizeX/2;
		double posY = sizeY/2;
		double visibleLeftX = posX - visibleWidth*RoundVariables.scaledTileDimension/2/* + tileDimension/2*/;
		double visibleUpY = posY - visibleHeight*RoundVariables.scaledTileDimension/2 /*+ tileDimension/2*/;
//		double globalLeftX = posX - globalWidth*tileDimension/2;
//		double globalUpY = posY - globalHeight*tileDimension/2;
		double globalLeftX = posX - (visibleLeftCol+visibleWidth/2.0)*RoundVariables.scaledTileDimension;
		double globalUpY = posY - (visibleUpRow+visibleHeight/2.0)*RoundVariables.scaledTileDimension;
    	level.setTilePositions(globalWidth,globalHeight,globalLeftX,globalUpY);
		
		// border
		double downBorderY;
		double horizontalBorderHeight;
		double verticalBorderY;
		double leftBorderX;
		double rightBorderX;
		double verticalBorderHeight;
		double verticalBorderWidth;
		double horizontalBorderX = 0;
		double upBorderY = 0;
		double horizontalBorderWidth = sizeX;
		if(levelInfo.getMaximize())
		{	downBorderY = globalUpY+globalHeight*RoundVariables.scaledTileDimension+1;
			if(globalUpY>0)
				horizontalBorderHeight = globalUpY;
			else
				horizontalBorderHeight = 0;
			verticalBorderY = horizontalBorderHeight+1;
			rightBorderX = globalLeftX+globalWidth*RoundVariables.scaledTileDimension+1;
			if(globalLeftX>0)
				verticalBorderWidth = globalLeftX;
			else
				verticalBorderWidth = 0;
			verticalBorderHeight = sizeY-2*horizontalBorderHeight+1;
		}
		else
		{	downBorderY = visibleUpY+visibleHeight*RoundVariables.scaledTileDimension+1;
			horizontalBorderHeight = visibleUpY;
			verticalBorderY = visibleUpY;
			rightBorderX = visibleLeftX+visibleWidth*RoundVariables.scaledTileDimension+1;
			verticalBorderWidth = visibleLeftX;
			verticalBorderHeight = sizeY-2*horizontalBorderHeight+1;
		}
		leftBorderX = 0;
		double values[] = {horizontalBorderX,upBorderY,downBorderY,horizontalBorderHeight,horizontalBorderWidth,verticalBorderY,leftBorderX,rightBorderX,verticalBorderHeight,verticalBorderWidth};
		level.setBorders(values);
	}

	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    public HollowLevel copy()
    {	HollowLevel result = new HollowLevel();
    	result.levelInfo = levelInfo;
    	result.zone = zone;
    	result.players = players;
if(instance==null)
	instance = new Instance(levelInfo.getInstanceName());
    	result.instance = new Instance(instance.getName());
    	result.itemCounts = itemCounts;
    	return result;
    }
    
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void finish()
    {	instance = null;
    	level = null;
    	levelInfo = null;
    	players = null;
    	zone = null;
    }
}

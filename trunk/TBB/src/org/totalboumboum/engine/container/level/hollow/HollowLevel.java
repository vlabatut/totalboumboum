package org.totalboumboum.engine.container.level.hollow;

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

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.container.bombset.Bombset;
import org.totalboumboum.engine.container.itemset.Itemset;
import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.level.info.LevelInfo;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.container.level.players.Players;
import org.totalboumboum.engine.container.level.zone.Zone;
import org.totalboumboum.engine.container.theme.Theme;
import org.totalboumboum.engine.container.theme.ThemeLoader;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.sprite.block.Block;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;
import org.totalboumboum.engine.content.sprite.floor.Floor;
import org.totalboumboum.engine.content.sprite.item.Item;
import org.totalboumboum.engine.loop.ServerLoop;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.xml.sax.SAXException;

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
	
	public void makeZone()
	{	if(zone!=null)
			zone.makeMatrix();		
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

	/////////////////////////////////////////////////////////////////
	// THEME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    public void loadTheme() throws SAXException, IOException, ParserConfigurationException, ClassNotFoundException
    {	// theme
    	String individualFolder = FilePaths.getInstancesPath()+File.separator+levelInfo.getInstanceName();
    	individualFolder = individualFolder+File.separator+FileNames.FILE_THEMES+File.separator+levelInfo.getThemeName();
    	Theme theme = ThemeLoader.loadTheme(individualFolder);
//		level.setTheme(theme);
    	
		instance.initLinks();
		theme.setInstance(instance);
		
    	// init zone
		Tile[][] matrix = level.getMatrix();
//		Itemset itemset = level.getItemset();
		Itemset itemset = instance.getItemset();
		Bombset bombset = instance.getBombsetMap().getBombset(null);
		double globalLeftX = level.getGlobalLeftX();
		double globalUpY = level.getGlobalUpY();
		int globalHeight = levelInfo.getGlobalHeight();
		int globalWidth = levelInfo.getGlobalWidth();
		ArrayList<String[][]> matrices = zone.getMatrices();
    	String[][] mFloors = matrices.get(0);
		String[][] mBlocks = matrices.get(1);
		String[][] mItems = matrices.get(2);
		String[][] mBombs = matrices.get(3);
		
		// init tiles
		for(int line=0;line<globalHeight;line++)
		{	for(int col=0;col<globalWidth;col++)
			{	double x = globalLeftX + RoundVariables.scaledTileDimension/2 + col*RoundVariables.scaledTileDimension;
				double y = globalUpY + RoundVariables.scaledTileDimension/2 + line*RoundVariables.scaledTileDimension;
				matrix[line][col] = new Tile(level,line,col,x,y);

				// floors
				Floor floor;
				if(mFloors[line][col]==null)
				{	floor = theme.makeFloor(matrix[line][col]);
					level.insertSpriteTile(floor);
				}
				else
				{	floor = theme.makeFloor(mFloors[line][col],matrix[line][col]);
					level.insertSpriteTile(floor);
				}
				
				// blocks
				if(mBlocks[line][col]!=null)
				{	Block block = theme.makeBlock(mBlocks[line][col],matrix[line][col]);
					level.insertSpriteTile(block);
				}
				
				// items
				if(mItems[line][col]!=null)
				{	Item item = itemset.makeItem(mItems[line][col],matrix[line][col]);
					level.insertSpriteTile(item);				
				}
				
				// bombs
				if(mBombs[line][col]!=null)
				{	String temp[] = mBombs[line][col].split(Theme.PROPERTY_SEPARATOR);
					int range = Integer.parseInt(temp[temp.length-1]);
					String name = "";
					for(int i=0;i<temp.length-1;i++)
						name = name+temp[i];
					Bomb bomb = bombset.makeBomb(name,matrix[line][col],range);
if(bomb==null)
	System.err.println("makeBomb error: sprite "+name+" not found.");
					level.insertSpriteTile(bomb);				
				}
			}
		}
		level.initTileList();
	}
    
	/////////////////////////////////////////////////////////////////
	// LEVEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	transient private Level level;

	public Level getLevel()
    {	return level;    
    }
    
	public void initLevel(ServerLoop loop)
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
		int visibleUpLine = levelInfo.getVisiblePositionUpLine();
		int visibleLeftCol = levelInfo.getVisiblePositionLeftCol();
		if(levelInfo.getForceAll())
		{	visibleWidth = globalWidth;
			visibleHeight = globalHeight;
			visibleLeftCol = 0;
			visibleUpLine = 0;
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
		double globalUpY = posY - (visibleUpLine+visibleHeight/2.0)*RoundVariables.scaledTileDimension;
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

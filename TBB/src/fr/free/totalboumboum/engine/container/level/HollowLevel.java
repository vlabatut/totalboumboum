package fr.free.totalboumboum.engine.container.level;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.engine.container.bombset.Bombset;
import fr.free.totalboumboum.engine.container.bombset.BombsetMap;
import fr.free.totalboumboum.engine.container.fireset.FiresetLoader;
import fr.free.totalboumboum.engine.container.fireset.FiresetMap;
import fr.free.totalboumboum.engine.container.itemset.Itemset;
import fr.free.totalboumboum.engine.container.itemset.ItemsetLoader;
import fr.free.totalboumboum.engine.container.theme.Theme;
import fr.free.totalboumboum.engine.container.theme.ThemeLoader;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.container.zone.Zone;
import fr.free.totalboumboum.engine.container.zone.ZoneLoader;
import fr.free.totalboumboum.engine.content.sprite.block.Block;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.engine.content.sprite.floor.Floor;
import fr.free.totalboumboum.engine.content.sprite.item.Item;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.game.GameData;
import fr.free.totalboumboum.game.round.RoundVariables;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class HollowLevel implements Serializable
{	private static final long serialVersionUID = 1L;

	private HollowLevel()
	{		
	}

	public HollowLevel(String folder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		packName = folder.substring(0,folder.indexOf(File.separator));
		folderName = folder.substring(folder.indexOf(File.separator)+1,folder.length());
		String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = FileTools.getLevelsPath()+File.separator+folder;
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_LEVEL+FileTools.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_LEVEL+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// loading
		loadLevelElement(individualFolder,root);
    }
    
	/////////////////////////////////////////////////////////////////
	// INIT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void initLevel(Loop loop)
	{	// init
    	level = new Level(loop);
    	RoundVariables.setInstanceName(instanceName);
		Dimension panelDim = Configuration.getVideoConfiguration().getPanelDimension();
    	double sizeX = panelDim.width;
    	double sizeY = panelDim.height;
		// matrix
		Tile[][] matrix = new Tile[globalHeight][globalWidth];
		level.setMatrix(matrix);
		
		// visible part
		if(displayForceAll)
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
		
//NOTE il y a une ligne horizontale dans les borders au dessus du niveau (forcer le zoomFactor à 1 pour la faire apparaitre)		
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
		if(displayMaximize)
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
	// LOAD				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void loadLevelElement(String folder, Element root) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		Element element;
		String content;
		
		// display
		element = root.getChild(XmlTools.ELT_DISPLAY);
		content = element.getAttribute(XmlTools.ATT_FORCE_ALL).getValue().trim();
		displayForceAll = Boolean.parseBoolean(content);
		content = element.getAttribute(XmlTools.ATT_MAXIMIZE).getValue().trim();
		displayMaximize = Boolean.parseBoolean(content);
		
		// global size
		element = root.getChild(XmlTools.ELT_GLOBAL_DIMENSION);
		content = element.getAttribute(XmlTools.ATT_HEIGHT).getValue().trim();
		globalHeight = Integer.parseInt(content);
		content = element.getAttribute(XmlTools.ATT_WIDTH).getValue().trim();
		globalWidth = Integer.parseInt(content);
		// visible size
		element = root.getChild(XmlTools.ELT_VISIBLE_DIMENSION);
		content = element.getAttribute(XmlTools.ATT_HEIGHT).getValue().trim();
		visibleHeight = Integer.parseInt(content);
		content = element.getAttribute(XmlTools.ATT_WIDTH).getValue().trim();
		visibleWidth = Integer.parseInt(content);
		// visible position
		element = root.getChild(XmlTools.ELT_VISIBLE_POSITION);
		content = element.getAttribute(XmlTools.ATT_UPLINE).getValue().trim();
		visibleUpLine = Integer.parseInt(content);
		content = element.getAttribute(XmlTools.ATT_LEFTCOL).getValue().trim();
		visibleLeftCol = Integer.parseInt(content);
		
		// instance
		element = root.getChild(XmlTools.ELT_INSTANCE);
		instanceName = element.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		instancePath = FileTools.getInstancesPath()+File.separator+instanceName;

		// players locations
		players = PlayersLoader.loadPlayers(folder);

		// bombset
		bombsetPath = instancePath + File.separator+FileTools.FOLDER_BOMBS;

		// fireset
		firePath = instancePath + File.separator+FileTools.FOLDER_FIRES;

		// itemset
		itemPath = instancePath + File.separator+FileTools.FOLDER_ITEMS;

		// theme
		element = root.getChild(XmlTools.ELT_THEME);
		themeName = element.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		String themeFolder = instancePath + File.separator + FileTools.FOLDER_THEMES;
		themePath = themeFolder + File.separator+themeName;

		// zone
		zone = ZoneLoader.loadZone(folder,globalHeight,globalWidth);
	}
    
	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Players players;

	public Players getPlayers()
    {	return players;    	
    }

	/////////////////////////////////////////////////////////////////
	// INSTANCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String instanceName;
	private String instancePath;

	public String getInstancePath()
    {	return instancePath;
    }
    
	public String getInstanceName()
    {	return instanceName;
    }
    
	/////////////////////////////////////////////////////////////////
	// ZONE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Zone zone;
	public Zone getZone()
    {	return zone;
    }

	/////////////////////////////////////////////////////////////////
	// THEME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String themeName;
	private String themePath;

	public String getThemeName()
    {	return themeName;
    }

    public void loadTheme() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// theme
    	Theme theme = ThemeLoader.loadTheme(themePath);
//		level.setTheme(theme);
		
    	// init zone
		Tile[][] matrix = level.getMatrix();
		Itemset itemset = level.getItemset();
		double globalLeftX = level.getGlobalLeftX();
		double globalUpY = level.getGlobalUpY();
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
				if(mFloors[line][col]==null)
				{	Floor floor = theme.makeFloor(matrix[line][col]);
					level.insertSpriteTile(floor);
				}
				else
				{	Floor floor = theme.makeFloor(mFloors[line][col],matrix[line][col]);
					level.insertSpriteTile(floor);
				}
				if(mBlocks[line][col]!=null)
				{	Block block = theme.makeBlock(mBlocks[line][col],matrix[line][col]);
					level.insertSpriteTile(block);
				
				}
				if(mItems[line][col]!=null)
				{	Item item = itemset.makeItem(mItems[line][col],matrix[line][col]);
					level.insertSpriteTile(item);				
				}
				if(mBombs[line][col]!=null)
				{	String temp[] = mBombs[line][col].split(":");
					int range = Integer.parseInt(temp[temp.length-1]);
					String name = "";
					for(int i=0;i<temp.length-1;i++)
						name = name+temp[i];
					Bomb bomb = bombset.makeBomb(name,matrix[line][col],range);
					level.insertSpriteTile(bomb);				
				}
			}
		}
		level.initTileList();
	}
    
	/////////////////////////////////////////////////////////////////
	// PACK			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String packName;

	public String getPackName()
    {	return packName;
    }
    
	/////////////////////////////////////////////////////////////////
	// FOLDER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String folderName;

	public String getFolderName()
    {	return folderName;
    }

	/////////////////////////////////////////////////////////////////
	// DIMENSIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    private boolean displayForceAll;
	private boolean displayMaximize;
	private int globalHeight;
	private int globalWidth;
	private int visibleHeight;
	private int visibleWidth;
	private int visibleUpLine;
	private int visibleLeftCol;

	public int getVisibleHeight()
    {	return visibleHeight;
    }
    
    public int getVisibleWidth()
    {	return visibleWidth;
    }
   
	/////////////////////////////////////////////////////////////////
	// BOMBSET			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String bombsetPath;
	private BombsetMap bombsetMap;
	private Bombset bombset;

	public void loadBombsets() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// bombsets map
		bombsetMap = new BombsetMap();
    	bombsetMap.loadBombset(bombsetPath);
		
    	// level bombset
    	bombset = bombsetMap.loadBombset(bombsetPath,null);
		level.setBombset(bombset);
    }
	
	public BombsetMap getBombsetMap()
	{	return bombsetMap;	
	}

	/////////////////////////////////////////////////////////////////
	// ITEMSET			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String firePath;

	public void loadFiresetMap() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	FiresetMap firesetMap = FiresetLoader.loadFiresetMap(firePath);
		level.setFiresetMap(firesetMap);
    }

	/////////////////////////////////////////////////////////////////
	// ITEMSET			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String itemPath;

	public void loadItemset() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	Itemset itemset = ItemsetLoader.loadItemset(itemPath);
		level.setItemset(itemset);
    }

	/////////////////////////////////////////////////////////////////
	// LEVEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	transient private Level level;

	public Level getLevel()
    {	return level;    
    }
    
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    public HollowLevel copy()
    {	HollowLevel result = new HollowLevel();
    	result.displayForceAll = displayForceAll;
    	result.displayMaximize = displayMaximize;
    	result.globalHeight = globalHeight;
    	result.globalWidth = globalWidth;
    	result.visibleHeight = visibleHeight;
    	result.visibleWidth = visibleWidth;
    	result.visibleUpLine = visibleUpLine;
    	result.visibleLeftCol = visibleLeftCol;
    	result.zone = zone;
    	result.instancePath = instancePath;
    	result.themePath = themePath;
    	result.itemPath = itemPath;
    	result.bombsetPath = bombsetPath;
    	result.firePath = firePath;
    	result.players = players;
      	result.instanceName = instanceName;
    	result.themeName = themeName;
    	result.packName = packName;
    	result.folderName = folderName;
    	//
    	return result;
    }
    
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    private boolean finished = false;
	
	public void finish()
    {	if(!finished)
	    {	// misc
	    	bombsetPath = null;
	    	instancePath = null;
	    	firePath = null;
	    	itemPath = null;
	    	level = null;
	    	players = null;
	    	themePath = null;
	    	zone = null;
	    }
    }
}

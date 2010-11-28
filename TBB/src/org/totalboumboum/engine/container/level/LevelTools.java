package org.totalboumboum.engine.container.level;

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
import java.text.NumberFormat;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.engine.container.level.hollow.HollowLevel;
import org.totalboumboum.engine.container.level.hollow.HollowLevelLoader;
import org.totalboumboum.engine.container.level.hollow.HollowLevelSaver;
import org.totalboumboum.engine.container.level.info.LevelInfo;
import org.totalboumboum.engine.container.level.players.Players;
import org.totalboumboum.engine.container.level.zone.Zone;
import org.totalboumboum.engine.container.level.zone.ZoneTile;
import org.totalboumboum.engine.container.theme.Theme;
import org.totalboumboum.engine.player.PlayerLocation;
import org.totalboumboum.gui.tools.GuiFileTools;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.files.FileTools;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
@SuppressWarnings("unused")
public class LevelTools
{	
	/**
	 * allows to programmatically initialize a zone,
	 * in order to help designing new levels
	 * 
	 */
	public static void main(String[] args) throws Exception
	{	
		// create a level from scratch
		HollowLevel level = initLevel(15,15,"temp","level","superbomberman1","tournament4");
		setBackground(level);
		addGrid(level);
		addBorder(level,2,1,1,1);
		addSoftwalls(level);
		insertCol(level,0,true,true,true,true,true);
		insertLine(level,0,true,true,true,true,true);
		insertCol(level,level.getLevelInfo().getGlobalWidth()/2,true,true,true,true,true);
		insertLine(level,level.getLevelInfo().getGlobalHeight()/2,true,true,true,true,true);
		insertCol(level,level.getLevelInfo().getGlobalWidth()-1,true,true,true,true,true);
		insertLine(level,level.getLevelInfo().getGlobalHeight()-1,true,true,true,true,true);
		saveLevel(level);
				
/*		// open an existing level and replace the background
		String pack = "level";
		String folder = "level";
		XmlTools.init();
		HollowLevel level = HollowLevelLoader.loadHollowLevel(pack,folder);
		removeBackground(level);
		setBackground(level);
		saveLevel(level);
*/		
/*		// open an existing level and add/remove columns/lines
		String pack = "tournament201011";
		String folder = "level";
		XmlTools.init();
		HollowLevel level = HollowLevelLoader.loadHollowLevel(pack,folder);
		removeLine(level,12,true,true,true,true,true);
		removeLine(level,12,true,true,true,true,true);
		removeLine(level,12,true,true,true,true,true);
		removeLine(level,0,true,true,true,true,true);
		removeLine(level,0,true,true,true,true,true);
		removeLine(level,0,true,true,true,true,true);
		insertCol(level,0,true,true,true,true,true);
		insertCol(level,23,true,true,true,true,true);
		saveLevel(level);
*/
	}
		
	/**
	 * save the specified level in order to get a set of files
	 * the game will be able to load and use.
	 *  
	 * @param hollowLevel
	 * 		the level to be saved
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	protected static void saveLevel(HollowLevel hollowLevel) throws IOException, IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException
	{	LevelInfo levelInfo = hollowLevel.getLevelInfo();
		
		// create level folder
		String pack = levelInfo.getPackName();
		String name = levelInfo.getFolder();
		String folder = FilePaths.getLevelsPath()+File.separator+pack+File.separator+name;
		File folderFile = new File(folder);
		folderFile.mkdirs();
		
		// possibly copy preview image
		String originalPreview = GuiFileTools.getImagesPath()+File.separator+"preview.jpg";
		String copy = folder+File.separator+levelInfo.getPreview();
		File fileCopy = new File(copy);
		if(!fileCopy.exists())
			FileTools.copyFile(originalPreview,copy);
		
		// save level
		HollowLevelSaver.saveHollowLevel(hollowLevel);
	}
	
	/**
	 * creates and save an empty level
	 * 
	 * @param height
	 * 		zone height (in tiles)
	 * @param width
	 * 		zone width (in tiles)
	 * @param pack
	 * 		name of the pack containing the level
	 * @param name
	 * 		name of the level
	 * @param instance
	 * 		name of the instance the level uses
	 * @param theme
	 * 		name of the theme used for the blocks
	 * @return
	 * 		an empty level with the specified properties
	 */
	protected static HollowLevel initLevel(int height, int width, String pack, String name, String instance, String theme)
	{	HollowLevel result = new HollowLevel();
		
		// init level info
		LevelInfo levelInfo = new LevelInfo();
		
		levelInfo.setPackName(pack);
		levelInfo.setFolder(name);
		levelInfo.setAuthor("[Author's name]");
		levelInfo.setForceAll(false);
		levelInfo.setGlobalHeight(height);
		levelInfo.setGlobalWidth(width);
		levelInfo.setMaximize(true);
		levelInfo.setSource("original");
		levelInfo.setTitle("[Level title]");
		levelInfo.setInstanceName(instance);
		levelInfo.setPreview("preview.jpg");
		levelInfo.setThemeName(theme);
		levelInfo.setVisibleHeight(height);
		levelInfo.setVisibleWidth(width);
		levelInfo.setVisiblePositionLeftCol(0);
		levelInfo.setVisiblePositionUpLine(0);
		result.setLevelInfo(levelInfo);
		
		// init zone
		Zone zone = new Zone(width,height);
		for(int line=0;line<height;line++)
		{	for(int col=0;col<width;col++)
			{	ZoneTile tile = new ZoneTile(line,col);
				tile.setFloor("regular");
				zone.addTile(tile);
			}
		}
		result.setZone(zone);
		
		// init players
		Players players = new Players();
		initPlayersLocations(players,0,0,width-1,height-1);
		for(int i=0;i<3;i++)
			players.addInitialItem("extrabomb");
		for(int i=0;i<3;i++)
			players.addInitialItem("extraflame");
		result.setPlayers(players);
		
		return result;
	}
	
	/**
	 * set the location of 4 players in the level
	 * 
	 * @param players
	 * 		object representing the players locations in the level object
	 * @param left
	 * 		column of the left players
	 * @param up
	 * 		line of the top players
	 * @param right
	 * 		column of the right players
	 * @param down
	 * 		column of the bottom players
	 */
	protected static void initPlayersLocations(Players players, int left, int up, int right, int down)
	{	for(int i=1;i<=4;i++)
		{	PlayerLocation[] loc = new PlayerLocation[i];
			for(int j=0;j<i;j++)
			{	int col = 0;
				int line = 0;
				switch(j)
				{	case 0: 
						col = left;
						line = up;
						break;
					case 1: 
						col = right;
						line = up;
						break;
					case 2: 
						col = left;
						line = down;
						break;
					case 3: 
						col = right;
						line = down;
						break;
				}
				loc[j] = new PlayerLocation();
				loc[j].setLine(line);
				loc[j].setCol(col);
				loc[j].setNumber(j);
			}
			players.addLocation(loc);
		}
	}

	/**
	 * put a border on a supposedly empty level
	 * 
	 * @param hollowLevel
	 * 		the level get borders
	 * @param xThickness
	 * 		thickness of the vertical borders (in tiles)
	 * @param yThickness
	 * 		thickness of the horizontal borders (in tiles)
	 * @param xMargin
	 * 		empty space between the left/right sides of the zone and their vertical border (in tiles)
	 * @param yMargin
	 * 		empty space between the top/bottom sides of the zone and their horizontal border (in tiles)
	 */
	protected static void addBorder(HollowLevel hollowLevel, int xThickness, int yThickness, int xMargin, int yMargin)
	{	// init
		LevelInfo levelInfo = hollowLevel.getLevelInfo();
		int width = levelInfo.getGlobalWidth();
		int height = levelInfo.getGlobalHeight();
		Zone zone = hollowLevel.getZone();
		
		// top border
		for(int line=yMargin;line<yMargin+yThickness;line++)
		{	for(int col=xMargin;col<width-xMargin;col++)
			{	ZoneTile tile = zone.getTile(line,col);
//				tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"border");
				tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"regular");
			}
		}
		// side borders
		for(int line=yMargin+yThickness;line<height-yMargin;line++)
		{	for(int col=xMargin;col<xMargin+xThickness;col++)
			{	ZoneTile tile = zone.getTile(line,col);
//				tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"border");
				tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"regular");
			}
			for(int col=width-xMargin-xThickness;col<width-xMargin;col++)
			{	ZoneTile tile = zone.getTile(line,col);
//				tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"border");
				tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"regular");
			}
		}
		// bottom border
		for(int line=height-yMargin-yThickness;line<height-yMargin;line++)
		{	for(int col=xMargin;col<width-xMargin;col++)
			{	ZoneTile tile = zone.getTile(line,col);
//				tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"border");
				tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"regular");
			}
		}
		
		// center visible area on the upper left border corner 
		levelInfo.setVisiblePositionLeftCol(xMargin+xThickness-1);
		levelInfo.setVisiblePositionUpLine(yMargin+yThickness-1);
		levelInfo.setVisibleWidth(width-2*xMargin-2*(xThickness-1));
		levelInfo.setVisibleHeight(height-2*yMargin-2*(yThickness-1));
		
		// move players locations inside the border
		int left = xMargin + xThickness;
		int up = yMargin + yThickness;
		int right = width - 1 - xMargin - xThickness;
		int down = height - 1 - yMargin - yThickness;
		Players players = hollowLevel.getPlayers();
		initPlayersLocations(players,left,up,right,down);
	}

	/**
	 * set a background on the floor, 
	 * without changing anything else in the level structure.
	 * the method automatically uses the floors located
	 * in the theme set for the specified level
	 * 
	 * @param hollowLevel
	 * 		the level to get a nice background
	 */
	protected static void setBackground(HollowLevel hollowLevel)
	{	// init
		LevelInfo levelInfo = hollowLevel.getLevelInfo();
		String instance = levelInfo.getInstanceName();
		String theme = levelInfo.getThemeName();
		String path = FilePaths.getInstancesPath()+File.separator+instance+File.separator+FileNames.FILE_THEMES+File.separator+theme+File.separator+"floors";
		File folder = new File(path);
		
		// process bg dimensions
		int bgWidth = -1;
		int bgHeight = -1;
		File[] files = folder.listFiles();
		for(File f: files)
		{	if(f.isDirectory())
			{	String[] temp = f.getName().split("_");
				if(temp.length==2)
				{	try
					{	int line = Integer.parseInt(temp[0]);
						if(line>bgHeight)
							bgHeight = line;
						int col = Integer.parseInt(temp[1]);
						if(col>bgWidth)
							bgWidth = col;
					}
					catch(NumberFormatException e)
					{	//
					}
				}
			}
		}
		bgWidth++;
		bgHeight++;
		
		// process bg upper-left corner
		int height = levelInfo.getVisibleHeight();
		int yCenter = levelInfo.getVisiblePositionUpLine()+height/2;
		int bgUp = yCenter - bgHeight/2;
		int width = levelInfo.getVisibleWidth();
		int xCenter = levelInfo.getVisiblePositionLeftCol()+width/2;
		int bgLeft = xCenter - bgWidth/2;
		
		// setting the appropriate floors
		Zone zone = hollowLevel.getZone();
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(0);
		nf.setMinimumIntegerDigits(2);
		for(int line=0;line<bgHeight;line++)
		{	for(int col=0;col<=bgWidth;col++)
			{	ZoneTile tile = zone.getTile(bgUp+line,bgLeft+col);
				String floorName = nf.format(line)+"_"+nf.format(col);
				File tempFile = new File(path+File.separator+floorName);
				if(tempFile.exists())
					tile.setFloor(floorName);
			}
		}
	}

	protected static void removeBackground(HollowLevel level)
	{	Zone zone = level.getZone();
		int height = zone.getGlobalHeight();
		int width = zone.getGlobalWidth();
		
		for(int line=0;line<height;line++)
		{	for(int col=0;col<width;col++)
			{	ZoneTile tile = zone.getTile(line,col);
				tile.setFloor("regular");
			}
		}
	}
	
	/**
	 * add the traditional grid structure to an empty level
	 * i.e. hardwall on 1 column/line out of 2.
	 * 
	 * @param hollowLevel
	 * 		the level to be completed
	 */
	protected static void addGrid(HollowLevel hollowLevel)
	{	// init
		LevelInfo levelInfo = hollowLevel.getLevelInfo();
		int height = levelInfo.getVisibleHeight();
		int yCenter = levelInfo.getVisiblePositionUpLine()+height/2;
		int width = levelInfo.getVisibleWidth();		
		int xCenter = levelInfo.getVisiblePositionLeftCol()+width/2;
		Zone zone = hollowLevel.getZone();
		
		// put hardwalls
		for(int line=0;line<height;line++)
		{	if(line%2!=yCenter%2)
			{	for(int col=0;col<width;col++)
				{	if(col%2!=xCenter%2)
					{	ZoneTile tile = zone.getTile(line,col);
						tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"regular");				
					}
				}
			}
		}		
	}
	
	/**
	 * add softwalls wherever it is necessary in the specified level,
	 * so that a classic zone is obtained.
	 * Note the initial locations of the players are considered,
	 * so that no block is put on a tile possibly occupied by a player
	 * when the round starts.
	 * 
	 * @param hollowLevel
	 * 		the level to be completed
	 */
	protected static void addSoftwalls(HollowLevel hollowLevel)
	{	// init
		LevelInfo levelInfo = hollowLevel.getLevelInfo();
		int height = levelInfo.getGlobalHeight();
		int width = levelInfo.getGlobalWidth();		
		Zone zone = hollowLevel.getZone();
		Players players = hollowLevel.getPlayers();
		
		// put hardwalls
		for(int line=0;line<height;line++)
		{	for(int col=0;col<width;col++)
			{	if(!players.isOccupied(line,col)
					&& !players.isOccupied(line-1,col)
					&& !players.isOccupied(line+1,col)
					&& !players.isOccupied(line,col-1)
					&& !players.isOccupied(line,col+1))
				{	ZoneTile tile = zone.getTile(line,col);
					if(tile.getBlock()==null)
						tile.setBlock(Theme.DEFAULT_GROUP+Theme.GROUP_SEPARATOR+"softwall");
				}
			}
		}		
	}
	
	/**
	 * insert a new line in order to make the zone taller.
	 * the user can choose to slide only certain types
	 * of sprites, or none of them.
	 * 
	 * @param hollowLevel
	 * 		the level to be modified
	 * @param line
	 * 		the location of the new line
	 * @param moveFloors
	 * 		whether the floor sprites should be moved to make room for the new line 
	 * @param moveBlocks
	 * 		whether the block sprites should be moved to make room for the new line 
	 * @param moveItems
	 * 		whether the item sprites should be moved to make room for the new line 
	 * @param moveBombs
	 * 		whether the bomb sprites should be moved to make room for the new line 
	 * @param moveVariables
	 * 		whether the variable sprites should be moved to make room for the new line 
	 */
	protected static void insertLine(HollowLevel hollowLevel, int line, boolean moveFloors, boolean moveBlocks, boolean moveItems, boolean moveBombs, boolean moveVariables)
	{	// update dimensions
		LevelInfo levelInfo = hollowLevel.getLevelInfo();
		int height = levelInfo.getGlobalHeight() + 1;
		levelInfo.setGlobalHeight(height);
		int width = levelInfo.getGlobalWidth();
		int vHeight = levelInfo.getVisibleHeight();
		int vUpLine = levelInfo.getVisiblePositionUpLine();
		if(line>=vUpLine && line<=vUpLine+vHeight)
			levelInfo.setVisibleHeight(vHeight+1);
		else if(line<vUpLine)
			levelInfo.setVisiblePositionUpLine(vUpLine+1);
		
		// update zone
		Zone zone = hollowLevel.getZone();
		zone.setGlobalHeight(height);
		// add new line
		for(int c=0;c<width;c++)
		{	ZoneTile tile = new ZoneTile(height-1,c);			
			zone.addTile(tile);
		}
		// move existing lines
		for(int l=height-1;l>line;l--)
		{	for(int c=0;c<width;c++)
			{	ZoneTile tile1 = zone.getTile(l,c);
				ZoneTile tile2 = zone.getTile(l-1,c);
				if(moveFloors)
					tile1.setFloor(tile2.getFloor());
				else
					tile1.setFloor(null);
				if(moveBlocks)
					tile1.setBlock(tile2.getBlock());
				else
					tile1.setBlock(null);
				if(moveBombs)
					tile1.setBomb(tile2.getBomb());
				else
					tile1.setBomb(null);
				if(moveItems)
					tile1.setItem(tile2.getItem());
				else
					tile1.setItem(null);
				if(moveVariables)
					tile1.setVariable(tile2.getVariable());
				else
					tile1.setVariable(null);
			}
		}
		// reinit line "line"
		for(int c=0;c<width;c++)
		{	ZoneTile tile = zone.getTile(line,c);
			tile.setFloor("regular");
			tile.setBlock(null);
			tile.setBomb(null);
			tile.setItem(null);
			tile.setVariable(null);
		}
		
		// update players
		Players players = hollowLevel.getPlayers();
		for(PlayerLocation[] pls: players.getLocations().values())
		{	for(PlayerLocation pl: pls)
			{	int temp = pl.getLine();
				if(line<=temp)
					pl.setLine(temp+1);
			}
		}
	}

	/**
	 * remove the specified line in order to make the zone narrower.
	 * the user can choose to slide only certain types
	 * of sprites, or none of them.
	 * 
	 * @param hollowLevel
	 * 		the level to be modified
	 * @param line
	 * 		the location of the line to be removed
	 * @param moveFloors
	 * 		whether the floor sprites should be moved to make room for the new line 
	 * @param moveBlocks
	 * 		whether the block sprites should be moved to make room for the new line 
	 * @param moveItems
	 * 		whether the item sprites should be moved to make room for the new line 
	 * @param moveBombs
	 * 		whether the bomb sprites should be moved to make room for the new line 
	 * @param moveVariables
	 * 		whether the variable sprites should be moved to make room for the new line 
	 */
	protected static void removeLine(HollowLevel hollowLevel, int line, boolean moveFloors, boolean moveBlocks, boolean moveItems, boolean moveBombs, boolean moveVariables)
	{	// update dimensions
		LevelInfo levelInfo = hollowLevel.getLevelInfo();
		int height = levelInfo.getGlobalHeight() - 1;
		levelInfo.setGlobalHeight(height);
		int width = levelInfo.getGlobalWidth();
		int vHeight = levelInfo.getVisibleHeight();
		int vUpLine = levelInfo.getVisiblePositionUpLine();
		if(line>=vUpLine && line<=vUpLine+vHeight)
			levelInfo.setVisibleHeight(vHeight-1);
		else if(line<vUpLine)
			levelInfo.setVisiblePositionUpLine(vUpLine-1);
		
		// update zone
		Zone zone = hollowLevel.getZone();
		zone.setGlobalHeight(height);
		// move existing lines
		for(int l=line;l<height;l++)
		{	for(int c=0;c<width;c++)
			{	ZoneTile tile1 = zone.getTile(l,c);
				ZoneTile tile2 = zone.getTile(l+1,c);
				if(moveFloors)
					tile1.setFloor(tile2.getFloor());
				else
					tile1.setFloor(null);
				if(moveBlocks)
					tile1.setBlock(tile2.getBlock());
				else
					tile1.setBlock(null);
				if(moveBombs)
					tile1.setBomb(tile2.getBomb());
				else
					tile1.setBomb(null);
				if(moveItems)
					tile1.setItem(tile2.getItem());
				else
					tile1.setItem(null);
				if(moveVariables)
					tile1.setVariable(tile2.getVariable());
				else
					tile1.setVariable(null);
			}
		}
		// remove line
		for(int c=0;c<width;c++)
		{	ZoneTile tile = zone.getTile(height,c);			
			zone.removeTile(tile);
		}
		
		// update players
		Players players = hollowLevel.getPlayers();
		for(PlayerLocation[] pls: players.getLocations().values())
		{	for(PlayerLocation pl: pls)
			{	int temp = pl.getLine();
				if(line<=temp)
					pl.setLine(temp-1);
			}
		}
	}

	/**
	 * insert a new column in order to make the zone wider.
	 * the user can choose to slide only certain types
	 * of sprites, or none of them.
	 * 
	 * @param hollowLevel
	 * 		the level to be modified
	 * @param col
	 * 		the location of the new column
	 * @param moveFloors
	 * 		whether the floor sprites should be moved to make room for the new column 
	 * @param moveBlocks
	 * 		whether the block sprites should be moved to make room for the new column 
	 * @param moveItems
	 * 		whether the item sprites should be moved to make room for the new column 
	 * @param moveBombs
	 * 		whether the bomb sprites should be moved to make room for the new column 
	 * @param moveVariables
	 * 		whether the variable sprites should be moved to make room for the new column 
	 */
	protected static void insertCol(HollowLevel hollowLevel, int col, boolean moveFloors, boolean moveBlocks, boolean moveItems, boolean moveBombs, boolean moveVariables)
	{	// update dimensions
		LevelInfo levelInfo = hollowLevel.getLevelInfo();
		int height = levelInfo.getGlobalHeight();
		int width = levelInfo.getGlobalWidth() + 1;
		levelInfo.setGlobalWidth(width);
		int vWidth = levelInfo.getVisibleWidth();
		int vLeftCol = levelInfo.getVisiblePositionLeftCol();
		if(col>=vLeftCol && col<=vLeftCol+vWidth)
			levelInfo.setVisibleWidth(vWidth+1);
		else if(col<vLeftCol)
			levelInfo.setVisiblePositionLeftCol(vLeftCol+1);
		
		// update zone
		Zone zone = hollowLevel.getZone();
		zone.setGlobalWidth(width);
		// add new col
		for(int l=0;l<height;l++)
		{	ZoneTile tile = new ZoneTile(l,width-1);			
			zone.addTile(tile);
		}
		// move existing columns
		for(int c=width-1;c>col;c--)
		{	for(int l=0;l<height;l++)
			{	ZoneTile tile1 = zone.getTile(l,c);
				ZoneTile tile2 = zone.getTile(l,c-1);
				if(moveFloors)
					tile1.setFloor(tile2.getFloor());
				else
					tile1.setFloor(null);
				if(moveBlocks)
					tile1.setBlock(tile2.getBlock());
				else
					tile1.setBlock(null);
				if(moveBombs)
					tile1.setBomb(tile2.getBomb());
				else
					tile1.setBomb(null);
				if(moveItems)
					tile1.setItem(tile2.getItem());
				else
					tile1.setItem(null);
				if(moveVariables)
					tile1.setVariable(tile2.getVariable());
				else
					tile1.setVariable(null);
			}
		}
		// reinit column "col"
		for(int l=0;l<height;l++)
		{	ZoneTile tile = zone.getTile(l,col);
			tile.setFloor("regular");
			tile.setBlock(null);
			tile.setBomb(null);
			tile.setItem(null);
			tile.setVariable(null);
		}
		
		// update players
		Players players = hollowLevel.getPlayers();
		for(PlayerLocation[] pls: players.getLocations().values())
		{	for(PlayerLocation pl: pls)
			{	int temp = pl.getCol();
				if(col<=temp)
					pl.setCol(temp+1);
			}
		}
	}
	
	/**
	 * remove the specified column in order to make the zone smaller.
	 * the user can choose to slide only certain types
	 * of sprites, or none of them.
	 * 
	 * @param hollowLevel
	 * 		the level to be modified
	 * @param col
	 * 		the location of the column to be removed
	 * @param moveFloors
	 * 		whether the floor sprites should be moved to make room for the new column 
	 * @param moveBlocks
	 * 		whether the block sprites should be moved to make room for the new column 
	 * @param moveItems
	 * 		whether the item sprites should be moved to make room for the new column 
	 * @param moveBombs
	 * 		whether the bomb sprites should be moved to make room for the new column 
	 * @param moveVariables
	 * 		whether the variable sprites should be moved to make room for the new column 
	 */
	protected static void removeCol(HollowLevel hollowLevel, int col, boolean moveFloors, boolean moveBlocks, boolean moveItems, boolean moveBombs, boolean moveVariables)
	{	// update dimensions
		LevelInfo levelInfo = hollowLevel.getLevelInfo();
		int height = levelInfo.getGlobalHeight();
		int width = levelInfo.getGlobalWidth() - 1;
		levelInfo.setGlobalWidth(width);
		int vWidth = levelInfo.getVisibleWidth();
		int vLeftCol = levelInfo.getVisiblePositionLeftCol();
		if(col>=vLeftCol && col<=vLeftCol+vWidth)
			levelInfo.setVisibleWidth(vWidth-1);
		else if(col<vLeftCol)
			levelInfo.setVisiblePositionLeftCol(vLeftCol-1);
		
		// update zone
		Zone zone = hollowLevel.getZone();
		zone.setGlobalWidth(width);
		// move existing columns
		for(int c=col;c<width;c++)
		{	for(int l=0;l<height;l++)
			{	ZoneTile tile1 = zone.getTile(l,c);
				ZoneTile tile2 = zone.getTile(l,c+1);
				if(moveFloors)
					tile1.setFloor(tile2.getFloor());
				else
					tile1.setFloor(null);
				if(moveBlocks)
					tile1.setBlock(tile2.getBlock());
				else
					tile1.setBlock(null);
				if(moveBombs)
					tile1.setBomb(tile2.getBomb());
				else
					tile1.setBomb(null);
				if(moveItems)
					tile1.setItem(tile2.getItem());
				else
					tile1.setItem(null);
				if(moveVariables)
					tile1.setVariable(tile2.getVariable());
				else
					tile1.setVariable(null);
			}
		}
		// remove col
		for(int l=0;l<height;l++)
		{	ZoneTile tile = zone.getTile(l,width);			
			zone.removeTile(tile);
		}
		
		// update players
		Players players = hollowLevel.getPlayers();
		for(PlayerLocation[] pls: players.getLocations().values())
		{	for(PlayerLocation pl: pls)
			{	int temp = pl.getCol();
				if(col<=temp)
					pl.setCol(temp-1);
			}
		}
	}
}

package org.totalboumboum.engine.container.level;

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
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.engine.container.level.hollow.HollowLevel;
import org.totalboumboum.engine.container.level.hollow.HollowLevelLoader;
import org.totalboumboum.engine.container.level.hollow.HollowLevelSaver;
import org.totalboumboum.engine.container.level.info.LevelInfo;
import org.totalboumboum.engine.container.level.players.Players;
import org.totalboumboum.engine.container.level.variabletile.ValueTile;
import org.totalboumboum.engine.container.level.variabletile.VariableTile;
import org.totalboumboum.engine.container.level.zone.Zone;
import org.totalboumboum.engine.container.level.zone.ZoneHollowTile;
import org.totalboumboum.engine.container.level.zone.ZoneTile;
import org.totalboumboum.engine.container.theme.Theme;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.player.PlayerLocation;
import org.totalboumboum.gui.tools.GuiFileTools;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.files.FileTools;
import org.totalboumboum.tools.levels.DistanceTileTools;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * Set of tools (without any GUI) allowing basic
 * editing of the zones. See the details in the
 * methods Javadoc.
 * 
 * @author Vincent Labatut
 */
@SuppressWarnings("unused")
public class LevelTools
{	
	/**
	 * Allows to programmatically initialize a zone,
	 * in order to help designing new levels.
	 * 
	 * @param args 
	 * 		Not used.
	 * @throws Exception 
	 * 		Whatever exception.
	 */
	public static void main(String[] args) throws Exception
	{	
		// create a level from scratch
//		HollowLevel level = initLevel(8,14,"tournament201314","averse","tournament","tournament7");
////		setBackground(level);
//		addGrid(level);
////		addBorder(level,2,1,1,1);
////		addSoftwalls(level);
//		insertCol(level,0,true,true,true,true,true);
////		insertRow(level,0,true,true,true,true,true);
////		insertCol(level,level.getLevelInfo().getGlobalWidth()/2,true,true,true,true,true);
////		insertRow(level,level.getLevelInfo().getGlobalHeight()/2,true,true,true,true,true);
////		insertCol(level,level.getLevelInfo().getGlobalWidth(),true,true,true,true,true);
//		insertRow(level,level.getLevelInfo().getGlobalHeight(),true,true,true,true,true);
//		setBackground(level);
//		saveLevel(level);
				
		// open an existing level and replace the background
//		String pack = "tournament201314qualif";
//		String folder = "qualif8";
//		XmlTools.init();
//		HollowLevel level = loadLevel(pack,folder);
//		removeBackground(level);
//		setBackground(level);
//		saveLevel(level);
		
		// open an existing level and add/remove columns/rows
		String pack = "tournament201314qualif";
		String folder = "qualif8";
		XmlTools.init();
		HollowLevel level = loadLevel(pack,folder);
//		removeRow(level,12,true,true,true,true,true);
//		removeRow(level,12,true,true,true,true,true);
//		removeRow(level,12,true,true,true,true,true);
//		removeRow(level,0,true,true,true,true,true);
//		removeRow(level,0,true,true,true,true,true);
//		removeRow(level,0,true,true,true,true,true);
		insertCol(level,9,true,true,true,true,true);
		insertCol(level,9,true,true,true,true,true);
		saveLevel(level);

		// open an existing level and add a sudden death
/*		String pack = "tournament201213";
		String folder = "compromis";
		XmlTools.init();
		HollowLevel level = loadLevel(pack,folder);
//		rescaleSuddenDeathTime(level,90000,120000);
//		removeSuddenDeath(level);
		int thickness = 4;
		boolean clockwise = true;
		long startTime = 60000;
		long endTime = 80000;
		boolean relative = false;
		long totalTime = 90000;
		boolean crushHardwalls = false;
//		addSpiralSuddenDeath(level, thickness, clockwise, 1, 5, startTime, endTime, relative, totalTime, crushHardwalls);
//		addLinearSuddenDeath(level, thickness, 2, true, 1, 13, startTime, endTime, relative, totalTime, crushHardwalls);
/*		addCustomSuddenDeath(level, Arrays.asList(
				Arrays.asList(new int[]{1,1},new int[]{1,13},new int[]{13,1},new int[]{13,13}),
				Arrays.asList(new int[]{1,2},new int[]{1,12},new int[]{2,1},new int[]{2,13},new int[]{12,1},new int[]{12,13},new int[]{13,2},new int[]{13,12}),
				Arrays.asList(new int[]{1,3},new int[]{1,11},new int[]{3,1},new int[]{3,13},new int[]{11,1},new int[]{11,13},new int[]{13,3},new int[]{13,11}),
				Arrays.asList(new int[]{1,4},new int[]{1,10},new int[]{4,1},new int[]{4,13},new int[]{10,1},new int[]{10,13},new int[]{13,4},new int[]{13,10}),
				Arrays.asList(new int[]{1,5},new int[]{1,9},new int[]{5,1},new int[]{5,13},new int[]{9,1},new int[]{9,13},new int[]{13,5},new int[]{13,9}),
				Arrays.asList(new int[]{1,6},new int[]{1,8},new int[]{6,1},new int[]{6,13},new int[]{8,1},new int[]{8,13},new int[]{13,6},new int[]{13,8}),
				Arrays.asList(new int[]{1,7},new int[]{7,1},new int[]{7,13},new int[]{13,7}),
				Arrays.asList(new int[]{2,7},new int[]{7,2},new int[]{7,12},new int[]{12,7}),
				Arrays.asList(new int[]{2,5},new int[]{2,9},new int[]{5,2},new int[]{5,12},new int[]{9,2},new int[]{9,12},new int[]{12,5},new int[]{12,9}),
				Arrays.asList(new int[]{2,3},new int[]{2,11},new int[]{3,2},new int[]{3,12},new int[]{11,2},new int[]{11,12},new int[]{12,3},new int[]{12,11}),
				Arrays.asList(new int[]{3,3},new int[]{3,11},new int[]{11,3},new int[]{11,11}),
				Arrays.asList(new int[]{3,4},new int[]{3,10},new int[]{4,3},new int[]{4,11},new int[]{10,3},new int[]{10,11},new int[]{11,4},new int[]{11,10}),
				Arrays.asList(new int[]{3,5},new int[]{3,9},new int[]{5,3},new int[]{5,11},new int[]{9,3},new int[]{9,11},new int[]{11,5},new int[]{11,9}),
				Arrays.asList(new int[]{3,6},new int[]{3,8},new int[]{6,3},new int[]{6,11},new int[]{8,3},new int[]{8,11},new int[]{11,6},new int[]{11,8}),
				Arrays.asList(new int[]{3,7},new int[]{7,3},new int[]{7,11},new int[]{11,7}),
				Arrays.asList(new int[]{4,7},new int[]{7,4},new int[]{7,10},new int[]{10,7}),
				Arrays.asList(new int[]{4,5},new int[]{4,9},new int[]{5,4},new int[]{5,10},new int[]{9,4},new int[]{9,10},new int[]{10,5},new int[]{10,9})
				), startTime, endTime, relative, totalTime, crushHardwalls);*/
//		addRandomFallingBombs(level, 20, new int[]{1,2,11,14}, 5000, 59500, 3000, relative, totalTime);
		
		
/*		HollowLevel level = initLevel(15,15,"tournament201213","vite","tournament","tournament6");
		level.getLevelInfo().setVisibleHeight(15);
		level.getLevelInfo().setVisibleWidth(15);
		level.getLevelInfo().setVisiblePositionLeftCol(0);
		level.getLevelInfo().setVisiblePositionUpRow(0);
		addGrid(level);
		removeCol(level, 0, true, true, true, true, true);
		removeCol(level, 0, true, true, true, true, true);
		removeRow(level, 0, true, true, true, true, true);
		removeRow(level, 0, true, true, true, true, true);
		addBorder(level,1,1,0,0);
		setBackground(level);
		saveLevel(level);
*/		
		
		System.out.println("Process done");
	}
	
    /////////////////////////////////////////////////////////////////
	// I/O					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Loads the level with specified package and name.
	 * 
	 * @param pack
	 * 		The pack containing the level to be loaded.
	 * @param name
	 * 		The name of the level.
	 * @return
	 * 		A {@link HollowLevel} object representing the level.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while accessing the level files.
	 * @throws SAXException
	 * 		Problem while accessing the level files.
	 * @throws IOException
	 * 		Problem while accessing the level files.
	 * @throws ClassNotFoundException
	 * 		Problem while accessing the level files.
	 */
	protected static HollowLevel loadLevel(String pack, String name) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	HollowLevel level = HollowLevelLoader.loadHollowLevel(pack,name);
		return level;
	}
	
	/**
	 * Saves the specified level in order to get a set of files
	 * the game will be able to load and use.
	 *  
	 * @param hollowLevel
	 * 		the level to be saved
	 * 
	 * @throws IOException
	 * 		Problem while accessing the level files.
	 * @throws IllegalArgumentException
	 * 		Problem while accessing the level files.
	 * @throws SecurityException
	 * 		Problem while accessing the level files.
	 * @throws ParserConfigurationException
	 * 		Problem while accessing the level files.
	 * @throws SAXException
	 * 		Problem while accessing the level files.
	 * @throws IllegalAccessException
	 * 		Problem while accessing the level files.
	 * @throws NoSuchFieldException
	 * 		Problem while accessing the level files.
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
	
    /////////////////////////////////////////////////////////////////
	// INIT					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Creates and save an empty level.
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
		levelInfo.setTitle(name);
		levelInfo.setInstanceName(instance);
		levelInfo.setPreview("preview.jpg");
		levelInfo.setThemeName(theme);
		levelInfo.setVisibleHeight(height);
		levelInfo.setVisibleWidth(width);
		levelInfo.setVisiblePositionLeftCol(0);
		levelInfo.setVisiblePositionUpRow(0);
		result.setLevelInfo(levelInfo);
		
		// init zone
		Zone zone = new Zone(width,height);
		for(int row=0;row<height;row++)
		{	for(int col=0;col<width;col++)
			{	ZoneHollowTile tile = new ZoneHollowTile(row,col);
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
	 * Sets the location of 4 players in the level.
	 * 
	 * @param players
	 * 		Object representing the players locations in the level object.
	 * @param left
	 * 		Column of the left players.
	 * @param up
	 * 		Row of the top players.
	 * @param right
	 * 		Column of the right players.
	 * @param down
	 * 		Column of the bottom players.
	 */
	protected static void initPlayersLocations(Players players, int left, int up, int right, int down)
	{	for(int i=1;i<=4;i++)
		{	PlayerLocation[] loc = new PlayerLocation[i];
			for(int j=0;j<i;j++)
			{	int col = 0;
				int row = 0;
				switch(j)
				{	case 0: 
						col = left;
						row = up;
						break;
					case 1: 
						col = right;
						row = up;
						break;
					case 2: 
						col = left;
						row = down;
						break;
					case 3: 
						col = right;
						row = down;
						break;
				}
				loc[j] = new PlayerLocation();
				loc[j].setRow(row);
				loc[j].setCol(col);
				loc[j].setNumber(j);
			}
			players.addLocation(loc);
		}
	}

    /////////////////////////////////////////////////////////////////
	// BACKGROUND			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Sets a background on the floor, 
	 * without changing anything else in the level structure.
	 * The method automatically uses the floors located
	 * in the theme set for the specified level
	 * 
	 * @param hollowLevel
	 * 		The level to get a nice background.
	 */
	protected static void setBackground(HollowLevel hollowLevel)
	{	// init
		LevelInfo levelInfo = hollowLevel.getLevelInfo();
		String instance = levelInfo.getInstanceName();
		String theme = levelInfo.getThemeName();
		String path = FilePaths.getInstancesPath()+File.separator+instance+File.separator+FileNames.FILE_THEMES+File.separator+theme+File.separator+"floors";
		File folder = new File(path);
		int lvHeight = levelInfo.getGlobalHeight();
		int lvWidth = levelInfo.getGlobalWidth();
		
		// process bg dimensions
		int bgWidth = -1;
		int bgHeight = -1;
		File[] files = folder.listFiles();
		for(File f: files)
		{	if(f.isDirectory())
			{	String[] temp = f.getName().split("_");
				if(temp.length==2)
				{	try
					{	int row = Integer.parseInt(temp[0]);
						if(row>bgHeight)
							bgHeight = row;
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
		int yCenter = levelInfo.getVisiblePositionUpRow()+height/2;
		int bgUp = yCenter - bgHeight/2;
		int width = levelInfo.getVisibleWidth();
		int xCenter = levelInfo.getVisiblePositionLeftCol()+width/2;
		int bgLeft = xCenter - bgWidth/2;
		
		// setting the appropriate floors
		Zone zone = hollowLevel.getZone();
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(0);
		nf.setMinimumIntegerDigits(2);
		for(int row=0;row<bgHeight;row++)
		{	for(int col=0;col<=bgWidth;col++)
			{	int l = bgUp+row;
				int c = bgLeft+col;
				if(l>=0 && l<lvHeight && c>=0 && c<lvWidth)
				{	ZoneHollowTile tile = zone.getTile(bgUp+row,bgLeft+col);
					String floorName = nf.format(row)+"_"+nf.format(col);
					File tempFile = new File(path+File.separator+floorName);
					if(tempFile.exists())
						tile.setFloor(floorName);
				}
			}
		}
	}

	/**
	 * Just removes all floor background from the 
	 * specified level object.
	 * 
	 * @param level
	 * 		The level whose background must be removed.
	 */
	protected static void removeBackground(HollowLevel level)
	{	Zone zone = level.getZone();
		int height = zone.getGlobalHeight();
		int width = zone.getGlobalWidth();
		
		for(int row=0;row<height;row++)
		{	for(int col=0;col<width;col++)
			{	ZoneHollowTile tile = zone.getTile(row,col);
				tile.setFloor("regular");
			}
		}
	}
	
    /////////////////////////////////////////////////////////////////
	// STRUCTURE			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Puts a border on a supposedly empty level.
	 * 
	 * @param hollowLevel
	 * 		The level get borders.
	 * @param xThickness
	 * 		Thickness of the vertical borders (in tiles).
	 * @param yThickness
	 * 		Thickness of the horizontal borders (in tiles).
	 * @param xMargin
	 * 		Empty space between the left/right sides of the zone and their vertical border (in tiles).
	 * @param yMargin
	 * 		Empty space between the top/bottom sides of the zone and their horizontal border (in tiles).
	 */
	protected static void addBorder(HollowLevel hollowLevel, int xThickness, int yThickness, int xMargin, int yMargin)
	{	// init
		LevelInfo levelInfo = hollowLevel.getLevelInfo();
		int width = levelInfo.getGlobalWidth();
		int height = levelInfo.getGlobalHeight();
		Zone zone = hollowLevel.getZone();
		
		// top border
		for(int row=yMargin;row<yMargin+yThickness;row++)
		{	for(int col=xMargin;col<width-xMargin;col++)
			{	ZoneHollowTile tile = zone.getTile(row,col);
//				tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"border");
				tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"regular");
			}
		}
		// side borders
		for(int row=yMargin+yThickness;row<height-yMargin;row++)
		{	for(int col=xMargin;col<xMargin+xThickness;col++)
			{	ZoneHollowTile tile = zone.getTile(row,col);
//				tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"border");
				tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"regular");
			}
			for(int col=width-xMargin-xThickness;col<width-xMargin;col++)
			{	ZoneHollowTile tile = zone.getTile(row,col);
//				tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"border");
				tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"regular");
			}
		}
		// bottom border
		for(int row=height-yMargin-yThickness;row<height-yMargin;row++)
		{	for(int col=xMargin;col<width-xMargin;col++)
			{	ZoneHollowTile tile = zone.getTile(row,col);
//				tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"border");
				tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"regular");
			}
		}
		
		// center visible area on the upper left border corner 
		levelInfo.setVisiblePositionLeftCol(xMargin+xThickness-1);
		levelInfo.setVisiblePositionUpRow(yMargin+yThickness-1);
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
	 * Adds the traditional grid structure to an empty level
	 * i.e. hardwall on 1 column/row out of 2.
	 * 
	 * @param hollowLevel
	 * 		The level to be completed.
	 */
	protected static void addGrid(HollowLevel hollowLevel)
	{	// init
		LevelInfo levelInfo = hollowLevel.getLevelInfo();
		int height = levelInfo.getVisibleHeight();
		int yCenter = levelInfo.getVisiblePositionUpRow()+height/2;
		int width = levelInfo.getVisibleWidth();		
		int xCenter = levelInfo.getVisiblePositionLeftCol()+width/2;
		Zone zone = hollowLevel.getZone();
		
		// put hardwalls
		for(int row=0;row<height;row++)
		{	if(row%2!=yCenter%2)
			{	for(int col=0;col<width;col++)
				{	if(col%2!=xCenter%2)
					{	ZoneHollowTile tile = zone.getTile(row,col);
						tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"regular");				
					}
				}
			}
		}		
	}
	
	/**
	 * Adds softwalls wherever it is necessary in the specified level,
	 * so that a classic zone is obtained.
	 * <br/>
	 * Note the initial locations of the players are considered,
	 * so that no block is put on a tile possibly occupied by a player
	 * when the round starts.
	 * 
	 * @param hollowLevel
	 * 		The level to be completed.
	 */
	protected static void addSoftwalls(HollowLevel hollowLevel)
	{	// init
		LevelInfo levelInfo = hollowLevel.getLevelInfo();
		int height = levelInfo.getGlobalHeight();
		int width = levelInfo.getGlobalWidth();		
		Zone zone = hollowLevel.getZone();
		Players players = hollowLevel.getPlayers();
		
		// put hardwalls
		for(int row=0;row<height;row++)
		{	for(int col=0;col<width;col++)
			{	if(!players.isOccupied(row,col)
					&& !players.isOccupied(row-1,col)
					&& !players.isOccupied(row+1,col)
					&& !players.isOccupied(row,col-1)
					&& !players.isOccupied(row,col+1))
				{	ZoneHollowTile tile = zone.getTile(row,col);
					if(tile.getBlock()==null)
						tile.setBlock(Theme.DEFAULT_GROUP+Theme.GROUP_SEPARATOR+"softwall");
				}
			}
		}		
	}
	
    /////////////////////////////////////////////////////////////////
	// SIZE					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Inserts a new row in order to make the zone taller.
	 * the user can choose to slide only certain types
	 * of sprites, or none of them.
	 * 
	 * @param hollowLevel
	 * 		The level to be modified.
	 * @param row
	 * 		The location of the new row.
	 * @param moveFloors
	 * 		Whether the floor sprites should be moved to make room for the new row.
	 * @param moveBlocks
	 * 		Whether the block sprites should be moved to make room for the new row.
	 * @param moveItems
	 * 		Whether the item sprites should be moved to make room for the new row.
	 * @param moveBombs
	 * 		Whether the bomb sprites should be moved to make room for the new row.
	 * @param moveVariables
	 * 		Whether the variable sprites should be moved to make room for the new row.
	 */
	protected static void insertRow(HollowLevel hollowLevel, int row, boolean moveFloors, boolean moveBlocks, boolean moveItems, boolean moveBombs, boolean moveVariables)
	{	// update dimensions
		LevelInfo levelInfo = hollowLevel.getLevelInfo();
		int height = levelInfo.getGlobalHeight() + 1;
		levelInfo.setGlobalHeight(height);
		int width = levelInfo.getGlobalWidth();
		int vHeight = levelInfo.getVisibleHeight();
		int vUpRow = levelInfo.getVisiblePositionUpRow();
		if(row>=vUpRow && row<=vUpRow+vHeight)
			levelInfo.setVisibleHeight(vHeight+1);
		else if(row<vUpRow)
			levelInfo.setVisiblePositionUpRow(vUpRow+1);
		
		// update zone
		Zone zone = hollowLevel.getZone();
		zone.setGlobalHeight(height);
		// add new row
		for(int c=0;c<width;c++)
		{	ZoneHollowTile tile = new ZoneHollowTile(height-1,c);			
			zone.addTile(tile);
		}
		// move existing rows
		for(int r=height-1;r>row;r--)
		{	for(int c=0;c<width;c++)
			{	ZoneHollowTile tile1 = zone.getTile(r,c);
				ZoneHollowTile tile2 = zone.getTile(r-1,c);
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
		// reinit row "row"
		for(int c=0;c<width;c++)
		{	ZoneHollowTile tile = zone.getTile(row,c);
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
			{	int temp = pl.getRow();
				if(row<=temp)
					pl.setRow(temp+1);
			}
		}
	}

	/**
	 * Removes the specified row in order to make the zone narrower.
	 * the user can choose to slide only certain types
	 * of sprites, or none of them.
	 * 
	 * @param hollowLevel
	 * 		The level to be modified.
	 * @param row
	 * 		The location of the row to be removed.
	 * @param moveFloors
	 * 		Whether the floor sprites should be moved to make room for the new row.
	 * @param moveBlocks
	 * 		Whether the block sprites should be moved to make room for the new row.
	 * @param moveItems
	 * 		Whether the item sprites should be moved to make room for the new row.
	 * @param moveBombs
	 * 		Whether the bomb sprites should be moved to make room for the new row.
	 * @param moveVariables
	 * 		Whether the variable sprites should be moved to make room for the new row.
	 */
	protected static void removeRow(HollowLevel hollowLevel, int row, boolean moveFloors, boolean moveBlocks, boolean moveItems, boolean moveBombs, boolean moveVariables)
	{	// update dimensions
		LevelInfo levelInfo = hollowLevel.getLevelInfo();
		int height = levelInfo.getGlobalHeight() - 1;
		levelInfo.setGlobalHeight(height);
		int width = levelInfo.getGlobalWidth();
		int vHeight = levelInfo.getVisibleHeight();
		int vUpRow = levelInfo.getVisiblePositionUpRow();
		if(row>=vUpRow && row<=vUpRow+vHeight)
			levelInfo.setVisibleHeight(vHeight-1);
		else if(row<vUpRow)
			levelInfo.setVisiblePositionUpRow(vUpRow-1);
		
		// update zone
		Zone zone = hollowLevel.getZone();
		zone.setGlobalHeight(height);
		// move existing rows
		for(int l=row;l<height;l++)
		{	for(int c=0;c<width;c++)
			{	ZoneHollowTile tile1 = zone.getTile(l,c);
				ZoneHollowTile tile2 = zone.getTile(l+1,c);
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
		// remove row
		for(int c=0;c<width;c++)
		{	ZoneHollowTile tile = zone.getTile(height,c);			
			zone.removeTile(tile);
		}
		
		// update players
		Players players = hollowLevel.getPlayers();
		for(PlayerLocation[] pls: players.getLocations().values())
		{	for(PlayerLocation pl: pls)
			{	int temp = pl.getRow();
				if(row<=temp)
					pl.setRow(temp-1);
			}
		}
	}

	/**
	 * Inserts a new column in order to make the zone wider.
	 * the user can choose to slide only certain types
	 * of sprites, or none of them.
	 * 
	 * @param hollowLevel
	 * 		The level to be modified.
	 * @param col
	 * 		The location of the new column.
	 * @param moveFloors
	 * 		Whether the floor sprites should be moved to make room for the new column.
	 * @param moveBlocks
	 * 		Whether the block sprites should be moved to make room for the new column.
	 * @param moveItems
	 * 		Whether the item sprites should be moved to make room for the new column.
	 * @param moveBombs
	 * 		Whether the bomb sprites should be moved to make room for the new column.
	 * @param moveVariables
	 * 		Whether the variable sprites should be moved to make room for the new column.
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
		for(int r=0;r<height;r++)
		{	ZoneHollowTile tile = new ZoneHollowTile(r,width-1);			
			zone.addTile(tile);
		}
		// move existing columns
		for(int c=width-1;c>col;c--)
		{	for(int r=0;r<height;r++)
			{	ZoneHollowTile tile1 = zone.getTile(r,c);
				ZoneHollowTile tile2 = zone.getTile(r,c-1);
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
		for(int r=0;r<height;r++)
		{	ZoneHollowTile tile = zone.getTile(r,col);
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
	 * Removes the specified column in order to make the zone smaller.
	 * the user can choose to slide only certain types
	 * of sprites, or none of them.
	 * 
	 * @param hollowLevel
	 * 		The level to be modified.
	 * @param col
	 * 		The location of the column to be removed.
	 * @param moveFloors
	 * 		Whether the floor sprites should be moved to make room for the new column.
	 * @param moveBlocks
	 * 		Whether the block sprites should be moved to make room for the new column.
	 * @param moveItems
	 * 		Whether the item sprites should be moved to make room for the new column.
	 * @param moveBombs
	 * 		Whether the bomb sprites should be moved to make room for the new column.
	 * @param moveVariables
	 * 		Whether the variable sprites should be moved to make room for the new column.
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
			{	ZoneHollowTile tile1 = zone.getTile(l,c);
				ZoneHollowTile tile2 = zone.getTile(l,c+1);
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
		{	ZoneHollowTile tile = zone.getTile(l,width);			
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

    /////////////////////////////////////////////////////////////////
	// SUDDEN DEATH			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Removes all sudden death events.
	 * 
	 * @param level 
	 * 		The level to be modified.
	 */
	protected static void removeSuddenDeath(HollowLevel level)
	{	Zone zone = level.getZone();
		Map<Long, List<ZoneHollowTile>> map = zone.getEvents();
		map.clear();
		zone.setEventsDuration(-1);
	}

	/**
	 * Adds the appropriate sudden death events in order to form a spiral
	 * of hardwalls. The user must specify the first tile to be crushed,
	 * the rest are deducted by symmetry. The direction the spiral unfolds
	 * is determined depending on the position of this starting point. The
	 * time parameters allow determining when the events should occur.
	 *  
	 * TODO Slide sudden death events when inserting/removing cols/rows
	 * 
	 * @param level
	 * 		The level to be modified.
	 * @param thickness
	 * 		Thickness of the spiral, expressed in number of tiles.
	 * @param clockwise
	 * 		Rotation of the spiral: {@code true} for clockwise, {@code false} for anti-clockwise.
	 * @param startRow
	 * 		Row of the the first tile to be crushed.
	 * @param startCol
	 * 		Column of the the first tile to be crushed.
	 * @param startTime
	 * 		Starting time of the sudden death.
	 * @param endTime
	 * 		Ending time of the sudden death.
	 * @param relative
	 * 		Whether those times should be interpreted relatively to the actual game duration.
	 * @param totalTime
	 * 		Total duration to be used if no time limit exist when actually playing this level.
	 * @param crushHardwalls
	 * 		Whether hardwalls should be ignored, or crushed (mainly for esthetic reasons).
	 */
	protected static void addSpiralSuddenDeath(HollowLevel level, int thickness, boolean clockwise, int startRow, int startCol, long startTime, long endTime, boolean relative, long totalTime, boolean crushHardwalls)
	{	// get level info
		LevelInfo info = level.getLevelInfo();
		int globalHeight = info.getGlobalHeight();
		int globalWidth = info.getGlobalWidth();
		Zone zone = level.getZone();
		
		// set general stuff
		zone.setEventsDuration(totalTime);
		zone.setEventsRelative(relative);
		
		// determine spiral direction
		int row2[] = {0,0,globalHeight-1,globalHeight-1};
		int col2[] = {0,globalWidth-1,0,globalWidth-1};
		Direction dirs[] = {Direction.UPLEFT,Direction.UPRIGHT,Direction.DOWNLEFT,Direction.DOWNRIGHT};
		int minDist = Integer.MAX_VALUE;
		Direction direction = null;
		for(int i=0;i<row2.length;i++)
		{	int dist = DistanceTileTools.getTileDistance(startRow, startCol, row2[i], col2[i], dirs[i], globalHeight, globalWidth);
			if(dist<minDist)
			{	minDist = dist;
				direction = dirs[i];
			}
		}
		direction = direction.getNext().getNext().getNext();
		Direction originalDirection = direction;

		// process the corresponding (first) limits
		HashMap<Direction,int[]> originalLimits = new HashMap<Direction, int[]>();
		int colLimit = globalWidth - startCol - 1;
		int rowLimit = globalHeight - startRow - 1;
		int temp[] = null;
		if(originalDirection.isHorizontal())
			temp = new int[]{startRow,colLimit,rowLimit,colLimit,rowLimit,startCol,startRow-(int)Math.signum(startRow-rowLimit),startCol};
		else if(originalDirection.isVertical())
			temp = new int[]{rowLimit,startCol,rowLimit,colLimit,startRow,colLimit,startRow,startCol-(int)Math.signum(startCol-colLimit)};
		direction = originalDirection;
		for(int i=0;i<temp.length;i=i+2)
		{	originalLimits.put(direction, new int[]{temp[i],temp[i+1]});
			if(clockwise)
				direction = direction.getNextPrimary();
			else
				direction = direction.getPreviousPrimary();
		}
		int sidesLimit = thickness * 4; 
		
		// count the number of events
		int row = startRow;
		int col = startCol;
		int eventCount = 0;
		int sides = 0;
		direction = originalDirection;
		HashMap<Direction,int[]> limits = new HashMap<Direction, int[]>();
		for(Entry<Direction,int[]> entry: originalLimits.entrySet())
		{	Direction key = entry.getKey();
			int[] t = entry.getValue();
			int[] value = Arrays.copyOf(t,t.length);
			limits.put(key,value);
		}
		do
		{	// check current tile
			ZoneHollowTile tile = zone.getTile(row,col);
			String blockName = tile.getBlock();
			if(crushHardwalls || blockName==null  || !blockName.contains("hardwall"))
				eventCount++;
				
			// process next tile coordinates
			int intDir[] = direction.getIntFromDirection();
			row = row + intDir[1];
			col = col + intDir[0];
			
			// check for a direction change
			int limit[] = limits.get(direction);
			if(row==limit[0] && col==limit[1])
			{	if(direction.isHorizontal())
					limit[1] = limit[1] - intDir[0];
				else if(direction.isVertical())
					limit[0] = limit[0] - intDir[1];
				if(clockwise)
					direction = direction.getNextPrimary();
				else
					direction = direction.getPreviousPrimary();
				intDir = direction.getIntFromDirection();
				if(direction.isHorizontal())
					limit[1] = limit[1] + intDir[0];
				else if(direction.isVertical())
					limit[0] = limit[0] + intDir[1];
				sides++;
			}
		}
		while(sides<sidesLimit);
		eventCount++;
		
		// determine the time steps
		long duration = (endTime - startTime) / (eventCount-1);
		long rem = (endTime - startTime) % (eventCount-1);
		
		row = startRow;
		col = startCol;
		eventCount = 0;
		sides = 0;
		direction = originalDirection;
		limits = new HashMap<Direction, int[]>();
		long time = startTime;
		for(Entry<Direction,int[]> entry: originalLimits.entrySet())
		{	Direction key = entry.getKey();
			int[] t = entry.getValue();
			int[] value = Arrays.copyOf(t,t.length);
			limits.put(key,value);
		}
		do
		{	// possibly insert new event
			ZoneHollowTile tile = zone.getTile(row,col);
			String blockName = tile.getBlock();
			if(crushHardwalls || blockName==null  || !blockName.contains("hardwall"))
			{	// create new event
				ZoneHollowTile eTile = new ZoneHollowTile(row, col);
				eTile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"shrink");
				zone.addEvent(time, eTile);
				// update counts and time
				eventCount++;
				time = time + duration;
				if(eventCount<rem)
					time++;
			}
				
			// process next tile coordinates
			int intDir[] = direction.getIntFromDirection();
			row = row + intDir[1];
			col = col + intDir[0];
			
			// check for a direction change
			int limit[] = limits.get(direction);
			if(row==limit[0] && col==limit[1])
			{	if(direction.isHorizontal())
					limit[1] = limit[1] - intDir[0];
				else if(direction.isVertical())
					limit[0] = limit[0] - intDir[1];
				if(clockwise)
					direction = direction.getNextPrimary();
				else
					direction = direction.getPreviousPrimary();
				intDir = direction.getIntFromDirection();
				if(direction.isHorizontal())
					limit[1] = limit[1] + intDir[0];
				else if(direction.isVertical())
					limit[0] = limit[0] + intDir[1];
				sides++;
			}
		}
		while(sides<sidesLimit);
		// create last event
		ZoneHollowTile eTile = new ZoneHollowTile(row, col);
		eTile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"shrink");
		zone.addEvent(time, eTile);
	}
	
	/**
	 * Adds the appropriate sudden death events in order to progressively 
	 * fill the level with strait lines. The user must specify the location
	 * and number of lines, and if those should be vertical or horizontal.
	 * The rest is deducted by symmetry. The time parameters allow determining 
	 * when the events should occur.
	 *  
	 * TODO Slide sudden death events when inserting/removing cols/rows
	 * 
	 * @param level
	 * 		The level to be modified.
	 * @param thickness
	 * 		Number of lines to be created. 
	 * @param offset
	 * 		Where the lines should start. For a vertical sudden death, then this represents
	 * 		the column of the left first line. For a horizontal one, it is its row. 
	 * @param vertical
	 * 		Orientation of the lines: {@code true} for vertical, {@code false} for horizontal.
	 * @param startPosition
	 * 		Position of the the first tile to be crushed. For a vertical sudden death, this is the
	 * 		row of the upper tiles composing the liens. For a horizontal one, it is their left column.
	 * @param endPosition
	 * 		Symmetrical to {@code startPosition}.
	 * @param startTime
	 * 		Starting time of the sudden death.
	 * @param endTime
	 * 		Ending time of the sudden death.
	 * @param relative
	 * 		Whether those times should be interpreted relatively to the actual game duration.
	 * @param totalTime
	 * 		Total duration to be used if no time limit exist when actually playing this level.
	 * @param crushHardwalls
	 * 		Whether hardwalls should be ignored, or crushed (mainly for esthetic reasons).
	 */
	protected static void addLinearSuddenDeath(HollowLevel level, int thickness, int offset, boolean vertical, int startPosition, int endPosition, long startTime, long endTime, boolean relative, long totalTime, boolean crushHardwalls)
	{	// get level info
		LevelInfo info = level.getLevelInfo();
		int globalHeight = info.getGlobalHeight();
		int globalWidth = info.getGlobalWidth();
		Zone zone = level.getZone();
		
		// set general stuff
		zone.setEventsDuration(totalTime);
		zone.setEventsRelative(relative);

		// set positions
		if(startPosition>endPosition)
		{	int temp = startPosition;
			startPosition = endPosition;
			endPosition = temp;
		}
		
		// count the number of events
		int eventCount = 0;
		int gap;
		if(vertical)
			gap = globalWidth - 1 - offset - offset;
		else
			gap = globalHeight - 1 - offset - offset;
		int pos1 = offset;
		for(int i=0;i<thickness*2;i++)
		{	// set coordinates
			int row = -1;
			int col = -1;
			if(vertical)
				col = pos1;
			else
				row = pos1;
			for(int pos2=startPosition;pos2<=endPosition;pos2++)
			{	// set coordinates
				if(vertical)
					row = pos2;
				else
					col = pos2;
				
				// check current tile
				ZoneHollowTile tile = zone.getTile(row,col);
				String blockName = tile.getBlock();
				if(crushHardwalls || blockName==null  || !blockName.contains("hardwall"))
					eventCount++;
			}
			
			// update position
			pos1 = pos1 + gap;
			gap = -(gap-1);
		}
		
		// determine the time steps
		long duration = (endTime - startTime) / (eventCount-1);
		long rem = (endTime - startTime) % (eventCount-1);

		long time = startTime;
		eventCount = 0;
		if(vertical)
			gap = globalWidth - 1 - offset - offset;
		else
			gap = globalHeight - 1 - offset - offset;
		pos1 = offset;
		for(int i=0;i<thickness*2;i++)
		{	// set coordinates
			int row = -1;
			int col = -1;
			if(vertical)
				col = pos1;
			else
				row = pos1;
			for(int pos2=startPosition;pos2<=endPosition;pos2++)
			{	// set coordinates
				if(vertical)
					row = pos2;
				else
					col = pos2;
				
				// possibly insert new event
				ZoneHollowTile tile = zone.getTile(row,col);
				String blockName = tile.getBlock();
				if(crushHardwalls || blockName==null  || !blockName.contains("hardwall"))
				{	// create new event
					ZoneHollowTile eTile = new ZoneHollowTile(row, col);
					eTile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"shrink");
					zone.addEvent(time, eTile);
					// update counts and time
					eventCount++;
					time = time + duration;
					if(eventCount<rem)
						time++;
				}
			}
			
			// update position
			pos1 = pos1 + gap;
			gap = -(gap-1);
		}
	}
	
	/**
	 * Define the sudden death events as specified in the list parameter.
	 * This list contains list of integer arrays. Each array contains the
	 * coordinates of a tile. Each sublist corresponds to an event : all
	 * tiles in the list will be crushed by a falling block at the same time.
	 *  
	 * TODO Slide sudden death events when inserting/removing cols/rows
	 * 
	 * @param level
	 * 		The level to be modified.
	 * @param coords
	 * 		The list of list of coordinates.
	 * @param startTime
	 * 		Starting time of the sudden death.
	 * @param endTime
	 * 		Ending time of the sudden death.
	 * @param relative
	 * 		Whether those times should be interpreted relatively to the actual game duration.
	 * @param totalTime
	 * 		Total duration to be used if no time limit exist when actually playing this level.
	 * @param crushHardwalls
	 * 		Whether hardwalls should be ignored, or crushed (mainly for esthetic reasons).
	 */
	protected static void addCustomSuddenDeath(HollowLevel level, List<List<int[]>> coords, long startTime, long endTime, boolean relative, long totalTime, boolean crushHardwalls)
	{	// get level info
		LevelInfo info = level.getLevelInfo();
		int globalHeight = info.getGlobalHeight();
		int globalWidth = info.getGlobalWidth();
		Zone zone = level.getZone();
		
		// set general stuff
		zone.setEventsDuration(totalTime);
		zone.setEventsRelative(relative);

		// count the number of events
		int eventCount = 0;
		for(List<int[]> tiles: coords)
		{	Iterator<int[]> it = tiles.iterator();
			boolean found = false;
			while(!found && it.hasNext())
			{	// set coordinates
				int[] pos = it.next();
				int row = pos[0];
				int col = pos[1];
	
				// check current tile
				ZoneHollowTile tile = zone.getTile(row,col);
				String blockName = tile.getBlock();
				if(crushHardwalls || blockName==null  || !blockName.contains("hardwall"))
				{	found = true;
					eventCount++;
				}
			}
		}
		
		// determine the time steps
		long duration = (endTime - startTime) / (eventCount-1);
		long rem = (endTime - startTime) % (eventCount-1);
		
		// set sudden death events
		eventCount = 0;
		long time = startTime;
		for(List<int[]> tiles: coords)
		{	boolean found = false;
			for(int[] pos: tiles)
			{	// set coordinates
				int row = pos[0];
				int col = pos[1];

				// possibly insert new event
				ZoneHollowTile tile = zone.getTile(row,col);
				String blockName = tile.getBlock();
				if(crushHardwalls || blockName==null  || !blockName.contains("hardwall"))
				{	// create new event
					ZoneHollowTile eTile = new ZoneHollowTile(row, col);
					eTile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"shrink");
					zone.addEvent(time, eTile);
					found = true;
				}
			}
			// update counts and time
			if(found)
			{	eventCount++;
				time = time + duration;
				if(eventCount<rem)
					time++;
			}
		}
	}
	
	/**
	 * Sets sudden death events taking the form of bombs falling
	 * from the sky randomly during the game.
	 * 
	 * @param level
	 * 		Level to be processed.
	 * @param totalNumber
	 * 		Total number of bombs to fall.
	 * @param boundaries
	 * 		Boundaries of the area concerned by the falling bombs (or {@code null} for no limit).
	 * @param startTime
	 * 		Time when the bombs start falling (or {@code -1} for no limit).
	 * @param endTime
	 * 		Time when the bombs stop falling (or {@code -1} for no limit).
	 * @param timeStep
	 * 		Time between the dropping of two bombs.
	 * @param relative
	 * 		Whether the specified times should be taken relatively.
	 * @param totalTime
	 * 		Supposed total duration of the game.
	 */
	protected static void addRandomFallingBombs(HollowLevel level, int totalNumber, int[] boundaries, long startTime, long endTime, long timeStep, boolean relative, long totalTime)
	{	Zone zone = level.getZone();
		int globalHeight = zone.getGlobalHeight();
		int globalWidth = zone.getGlobalWidth();
		
		// set general stuff
		zone.setEventsDuration(totalTime);
		zone.setEventsRelative(relative);
		
		// size of the area
		if(boundaries==null)
			boundaries = new int[]{0,0,globalHeight-1,globalWidth-1};
		int height = Math.abs(boundaries[0]-boundaries[2]);
		int width = Math.abs(boundaries[1]-boundaries[3]);
		int surface = height * width;
		
		// number of time steps
		if(startTime==-1)
			startTime = 1;
		if(endTime==-1)
			endTime = totalTime;
		long duration = endTime - startTime;
		int steps = (int)(duration / timeStep);
		
		// probability for a bomb to appear on a give tile at a given time step
		float proba = totalNumber / (float)(surface * steps);
		//proba = proba / 2;
		
		// create appropriate variable
		Map<String, VariableTile> variables = zone.getVariableTiles();
		String varName = "fallingBomb";
		VariableTile variable = new VariableTile(varName);
		variables.put(varName, variable);
		String name = "normal"+Theme.PROPERTY_SEPARATOR+1+Theme.PROPERTY_SEPARATOR+2000;
		ValueTile value1 = new ValueTile(null, null, null, name, proba);
		variable.addValue(value1);
		ValueTile value2 = new ValueTile(null, null, null, null, 1-proba);
		variable.addValue(value2);
		
		// insert as sudden death events
		for(long time=startTime;time<=endTime;time=time+timeStep)
		{	for(int row=boundaries[0];row<=boundaries[2];row++)
			{	for(int col=boundaries[1];col<=boundaries[3];col++)
				{	ZoneHollowTile tile = new ZoneHollowTile(row, col);
					tile.setVariable(varName);
					zone.addEvent(time, tile);
				}
			}
		}
	}
	
	/**
	 * Translates all existing sudden death events by a fixed
	 * duration, which can be negative (events happen sooner)
	 * or positive (they occur later). 
	 * 
	 * @param level
	 * 		Level to be modified.
	 * @param delta
	 * 		Amount of time used to translate all events (can be negative).
	 */
	private static void translateSuddenDeathTime(HollowLevel level, long delta)
	{	// get data
		Zone zone = level.getZone();
		Map<Long, List<ZoneHollowTile>> temp = zone.getEvents();
	 	Map<Long, List<ZoneHollowTile>> events = new HashMap<Long, List<ZoneHollowTile>>(temp);
	 	temp.clear();
	 	
	 	// translate events
	 	for(Entry<Long, List<ZoneHollowTile>> entry: events.entrySet())
	 	{	Long time = entry.getKey() + delta;
	 		List<ZoneHollowTile> list = entry.getValue();
	 		zone.addEvents(time,list);
	 	}
	}

	/**
	 * Translates all existing sudden death events in order to
	 * match new begin and ending times. 
	 * 
	 * @param level
	 * 		Level to be modified.
	 * @param newStart
	 * 		New starting time.
	 * @param newEnd
	 * 		New ending time.
	 */
	private static void rescaleSuddenDeathTime(HollowLevel level, long newStart, long newEnd)
	{	// get data
		Zone zone = level.getZone();
		Map<Long, List<ZoneHollowTile>> temp = zone.getEvents();
	 	Map<Long, List<ZoneHollowTile>> events = new HashMap<Long, List<ZoneHollowTile>>(temp);
	 	temp.clear();
	 	
	 	// process transformation
	 	TreeSet<Long> times = new TreeSet<Long>(events.keySet());
	 	long oldStart = times.first();
	 	long oldEnd = times.last();
	 	double coef = (newEnd-newStart)/(double)(oldEnd-oldStart);
	 	
	 	// translate events
	 	for(Entry<Long, List<ZoneHollowTile>> entry: events.entrySet())
	 	{	long oldTime = entry.getKey();
	 		long newTime = (long)(newStart + (oldTime-oldStart)*coef); 
	 		List<ZoneHollowTile> list = entry.getValue();
	 		zone.addEvents(newTime,list);
	 	}
	}
}

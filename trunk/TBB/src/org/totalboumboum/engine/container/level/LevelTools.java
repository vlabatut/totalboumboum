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

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.engine.container.level.hollow.HollowLevel;
import org.totalboumboum.engine.container.level.hollow.HollowLevelSaver;
import org.totalboumboum.engine.container.level.info.LevelInfo;
import org.totalboumboum.engine.container.level.info.LevelInfoSaver;
import org.totalboumboum.engine.container.level.players.Players;
import org.totalboumboum.engine.container.level.players.PlayersSaver;
import org.totalboumboum.engine.container.level.zone.Zone;
import org.totalboumboum.engine.container.level.zone.ZoneSaver;
import org.totalboumboum.engine.container.level.zone.ZoneTile;
import org.totalboumboum.engine.container.theme.Theme;
import org.totalboumboum.engine.player.PlayerLocation;
import org.totalboumboum.gui.tools.GuiFileTools;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.files.FileTools;
import org.xml.sax.SAXException;

public class LevelTools
{	
	/**
	 * allows to programmatically initialize a zone,
	 * in order to help designing new levels
	 */
	public static void main(String[] args) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	HollowLevel level = initLevel(15,15,"superbomberman1","normal");
		addBorder(level,2,1,1,1);
		saveLevel(level,"temp","level");
	}
		
	private static void saveLevel(HollowLevel hollowLevel, String pack, String name) throws IOException, IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException
	{	// create level folder
		String folder = FilePaths.getLevelsPath()+File.separator+pack+File.separator+name;
		File folderFile = new File(folder);
		folderFile.mkdirs();
		
		// possibly copy preview image
		LevelInfo levelInfo = hollowLevel.getLevelInfo();
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
	 */
	private static HollowLevel initLevel(int height, int width, String instance, String theme)
	{	HollowLevel result = new HollowLevel();
		
		// init level info
		LevelInfo levelInfo = new LevelInfo();		
		levelInfo.setAuthor("[Author's name]");
		levelInfo.setForceAll(false);
		levelInfo.setGlobalHeight(height);
		levelInfo.setGlobalWidth(width);
		levelInfo.setMaximize(true);
		levelInfo.setSource("original");
		levelInfo.setTitle("[Level title]");
		levelInfo.setInstance(instance);
		levelInfo.setPreview("preview.jpg");
		levelInfo.setTheme(theme);
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
	
	private static void initPlayersLocations(Players players, int left, int up, int right, int down)
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
	 */
	private static void addBorder(HollowLevel hollowLevel, int xThickness, int yThickness, int xMargin, int yMargin)
	{	// init
		LevelInfo levelInfo = hollowLevel.getLevelInfo();
		int width = levelInfo.getGlobalWidth();
		int height = levelInfo.getGlobalHeight();
		Zone zone = hollowLevel.getZone();
		
		// put border
		for(int line=yMargin;line<height-yMargin;line++)
		{	if(line==yMargin || line==height-1-yMargin)
			{	for(int col=xMargin;col<width-xMargin;col++)
				{	ZoneTile tile = zone.getTile(line,col);
					tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"border");
				}
			}
			else
			{	// left
				{	int col = xMargin;
					ZoneTile tile = zone.getTile(line,col);
					tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"border");
				}
				// right
				{	int col = width-1-xMargin;
					ZoneTile tile = zone.getTile(line,col);
					tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"border");
				}
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
	 * set a background on the floor, without changing anything else in the level structure
	 */
	private static void setBackground()
	{	
		
	}
	
	

	
	
	private static Zone initZone(int height, int width, int border)
	{	Zone result = new Zone(width,height);
		for(int line=0;line<height;line++)
		{	for(int col=0;col<width;col++)
			{	ZoneTile tile = new ZoneTile(line,col);
				// floor
				tile.setFloor("regular");
				// block
				if(line<border || line>=height-border || col<border || col>=width-border)
					// build the border
					tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"border");				
				else if(line==border || line==height-border-1 || col==border || col==width-border-1)
					// fill the borderline with softwalls
					tile.setBlock(Theme.DEFAULT_GROUP+Theme.GROUP_SEPARATOR+"softwall");
				else if((col-border)%2==1 && (line-border)%2==1)
					// put a block if it fits the regular pattern
					tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"regular");				
				else
					// else put a softwall
					tile.setBlock(Theme.DEFAULT_GROUP+Theme.GROUP_SEPARATOR+"softwall");
				// add to zone	
				result.addTile(tile);
			}
		}
		
		return result;
	}
}

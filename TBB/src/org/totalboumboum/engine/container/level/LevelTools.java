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
	{	createLevel(15,15,"temp","level",1);
	}
		
	private static void createLevel(int height, int width, String pack, String name, int border) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	// create folder
		String folder = FilePaths.getLevelsPath()+File.separator+pack+File.separator+name;
		File folderFile = new File(folder);
		folderFile.mkdirs();
		
		// init level info
		LevelInfo levelInfo = initLevelInfo(height, width, border);
		LevelInfoSaver.saveLevelInfo(folder, levelInfo);
		
		// init zone
		Zone zone = initZone(height,width,border);
		ZoneSaver.saveZone(folder,zone);

		// init players
		Players players = initPlayers(height,width,border);
		PlayersSaver.savePlayers(folder, players);
		
		// copy preview image
		String originalPreview = GuiFileTools.getImagesPath()+File.separator+"preview.jpg";
		String copy = folder+File.separator+levelInfo.getPreview();
		FileTools.copyFile(originalPreview,copy);
	}
	
	private static LevelInfo initLevelInfo(int height, int width, int border)
	{	LevelInfo result = new LevelInfo();
		
		// misc data
		result.setAuthor("[Author's name]");
		result.setForceAll(false);
		result.setGlobalHeight(height);
		result.setGlobalWidth(width);
		result.setMaximize(true);
		result.setSource("original");
		result.setTitle("[Level title]");
		result.setInstance("superbomberman1");
		result.setPreview("preview.jpg");
		result.setTheme("normal");
		
		// dimensions
		int visibleHeight = height;
		if(border>0)
			visibleHeight = height - 2*(border-1);
		result.setVisibleHeight(visibleHeight);
		int visibleWidth = width;
		if(border>0)
			visibleWidth = width - 2*(border-1);
		result.setVisibleWidth(visibleWidth);
		int visiblePositionLeftCol = 0;
		if(border>0)
			visiblePositionLeftCol = border - 1;
		result.setVisiblePositionLeftCol(visiblePositionLeftCol);
		int visiblePositionUpLine = 0;
		if(border>0)
			visiblePositionUpLine = border - 1;
		result.setVisiblePositionUpLine(visiblePositionUpLine);

		return result;
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

	private static Players initPlayers(int height, int width, int border)
	{	Players result = new Players();
		
		// locations
		for(int i=1;i<=4;i++)
		{	PlayerLocation[] loc = new PlayerLocation[i];
			for(int j=0;j<i;j++)
			{	int col = 0;
				int line = 0;
				switch(j)
				{	case 0: 
						col = border + 1;
						line = border + 1;
						break;
					case 1: 
						col = width - border - 1;
						line = border + 1;
						break;
					case 2: 
						col = border + 1;
						line = height - border - 1;
						break;
					case 3: 
						col = width - border - 1;
						line = height - border - 1;
						break;
				}
				loc[j] = new PlayerLocation();
				loc[j].setLine(line);
				loc[j].setCol(col);
				loc[j].setNumber(j);
			}
			result.addLocation(loc);
		}
		
		// items
		for(int i=0;i<3;i++)
			result.addInitialItem("extrabomb");
		for(int i=0;i<3;i++)
			result.addInitialItem("extraflame");
		
		return result;
	}
	
	private static void setBackground(int backgroundHeight, int backgroundWidth)
	{
		
	}
}

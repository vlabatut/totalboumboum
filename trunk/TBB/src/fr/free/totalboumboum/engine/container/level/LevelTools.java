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

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.level.players.Players;
import fr.free.totalboumboum.engine.container.level.players.PlayersSaver;
import fr.free.totalboumboum.engine.container.level.preview.LevelPreview;
import fr.free.totalboumboum.engine.container.level.preview.LevelPreviewSaver;
import fr.free.totalboumboum.engine.container.level.zone.Zone;
import fr.free.totalboumboum.engine.container.level.zone.ZoneSaver;
import fr.free.totalboumboum.engine.container.level.zone.ZoneTile;
import fr.free.totalboumboum.engine.container.theme.Theme;
import fr.free.totalboumboum.engine.player.PlayerLocation;
import fr.free.totalboumboum.tools.FileTools;

public class LevelTools
{	
	/**
	 * allows to programmatically initialize a zone,
	 * in order to help designing new levels
	 */
	public static void main(String[] args) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	createClassicLevel(15,15,"temp","level",1);
	}

	private static void createEmptyLevel()
	
	private static void createClassicLevel(int height, int width, String pack, String level, int border) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	// init
		String folder = FileTools.getLevelsPath()+File.separator+pack+File.separator+level;
		File folderFile = new File(folder);
		folderFile.mkdirs();
		
		// level file
		LevelPreview levelPreview = new LevelPreview();
		levelPreview.setAuthor("Vincent Labatut");
		levelPreview.setForceAll(false);
		levelPreview.setGlobalHeight(height);
		levelPreview.setGlobalWidth(width);
		levelPreview.setInstanceName("superbomberman1");
		levelPreview.setMaximize(true);
		levelPreview.setPreviewFile("preview.jpg");
		levelPreview.setSource("original");
		levelPreview.setThemeName("normal");
		levelPreview.setTitle("New level");
		int visibleHeight = height;
		if(border>0)
			visibleHeight = height - 2*(border-1);
		levelPreview.setVisibleHeight(visibleHeight);
		int visibleWidth = width;
		if(border>0)
			visibleWidth = width - 2*(border-1);
		levelPreview.setVisibleWidth(visibleWidth);
		int visiblePositionLeftCol = 0;
		if(border>0)
			visiblePositionLeftCol = border - 1;
		levelPreview.setVisiblePositionLeftCol(visiblePositionLeftCol);
		int visiblePositionUpLine = 0;
		if(border>0)
			visiblePositionUpLine = border - 1;
		levelPreview.setVisiblePositionUpLine(visiblePositionUpLine);
		LevelPreviewSaver.saveLevelPreview(folder,levelPreview);
		
		// fill zone
		Zone zone = new Zone(width,height);
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
				zone.addTile(tile);
			}
		}		
		// save zone
		ZoneSaver.saveZone(folder,zone);
		
		// players file
		Players players = new Players();
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
			players.addLocation(loc);
		}
		// items
		for(int i=0;i<3;i++)
			players.addInitialItem("extrabomb");
		for(int i=0;i<3;i++)
			players.addInitialItem("extraflame");
		// save players file
		PlayersSaver.savePlayers(folder,players);
		
		// create image
	}
}

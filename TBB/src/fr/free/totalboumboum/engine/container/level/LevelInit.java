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

import fr.free.totalboumboum.engine.container.theme.Theme;
import fr.free.totalboumboum.engine.container.zone.Zone;
import fr.free.totalboumboum.engine.container.zone.ZoneSaver;
import fr.free.totalboumboum.engine.container.zone.ZoneTile;
import fr.free.totalboumboum.tools.FileTools;

public class LevelInit
{	
	/**
	 * allows to programmatically initialize a zone,
	 * in order to help designing new levels
	 */
	public static void main(String[] args) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	createClassicLevel(15,21,"temp","level",1);
	}

	private static void createClassicLevel(int height, int width, String pack, String level, int border) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	// init
		String folder = FileTools.getLevelsPath()+File.separator+pack+File.separator+level;
		File folderFile = new File(folder);
		folderFile.mkdirs();
		
		// level file
		
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
				// add to zone	
				zone.addTile(tile);
			}
		}		
		// save zone
		ZoneSaver.saveZone(folder,zone);
		
		// players file
		
		// create image
	}
}

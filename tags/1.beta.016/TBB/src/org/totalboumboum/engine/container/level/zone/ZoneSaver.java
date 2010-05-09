package org.totalboumboum.engine.container.level.zone;

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
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.engine.container.level.variabletile.VariableTile;
import org.totalboumboum.engine.container.level.variabletile.VariableTilesSaver;
import org.totalboumboum.engine.container.theme.Theme;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

public class ZoneSaver
{	    
	public static void saveZone(String folder, Zone zone) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	// build document
		Element root = saveZoneElement(zone);	
		// save file
		String individualFolder = folder;
		File dataFile = new File(individualFolder+File.separator+FileNames.FILE_ZONE+FileNames.EXTENSION_XML);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_ZONE+FileNames.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveZoneElement(Zone zone)
	{	Element result = new Element(XmlTools.ZONE);
		
		// tiles random variable
		HashMap<String,VariableTile> variableTiles = zone.getVariableTiles();
		if(!variableTiles.isEmpty())
		{	Element variableTilesElement = VariableTilesSaver.saveVariableTilesElement(variableTiles);
			result.addContent(variableTilesElement);
		}
		
		// matrix
		Element matrixElement = saveMatrixElement(zone);
		result.addContent(matrixElement);
		return result;
	}
    
    private static Element saveMatrixElement(Zone zone)
    {	Element result = new Element(XmlTools.MATRIX);
    	
    	// create and init all elements
    	int prevLine = -1;
    	Element lineElement = null;
    	for(ZoneTile zoneTile: zone.getTiles())
    	{	// init lines and cols
    		int line = zoneTile.getLine();
    		int col = zoneTile.getCol();
    		if(line!=prevLine)
    		{	prevLine = line;
    			lineElement = new Element(XmlTools.LINE);
    			lineElement.setAttribute(XmlTools.POSITION, Integer.toString(line));
        		result.addContent(lineElement);
    		}
    		// process contant terms
    		String floor = zoneTile.getFloor();
        	String block = zoneTile.getBlock();
       		String item = zoneTile.getItem();
       		String bomb = zoneTile.getBomb();
       		Element tileElement = saveBasicTileElement(floor, block, item, bomb);
			tileElement.setAttribute(XmlTools.POSITION, Integer.toString(col));
    		// process variable term
    		String variable = zoneTile.getVariable();
    		if(variable!=null)
    		{	Element variableElement = new Element(XmlTools.REFERENCE);
    			variableElement.setAttribute(XmlTools.NAME,variable);
    			tileElement.addContent(variableElement);
    		}
    		
    		lineElement.addContent(tileElement);
    	}
 
    	return result;
    }
    
    public static Element saveBasicTileElement(String floor, String block, String item, String bomb)
    {	// init
		Element result = new Element(XmlTools.TILE);
		
		// floor
		if(floor!=null)
		{	Element floorElement = new Element(XmlTools.FLOOR);
		result.addContent(floorElement);
			String floorName = floor;
			floorElement.setAttribute(XmlTools.NAME,floorName);
		}
		
		// block
		if(block!=null)
		{	Element blockElement = new Element(XmlTools.BLOCK);
			result.addContent(blockElement);
			String temp[] = block.split(Theme.GROUP_SEPARATOR);
			String blockGroup = temp[0];
			blockElement.setAttribute(XmlTools.GROUP,blockGroup);
			String blockName = temp[1];
			blockElement.setAttribute(XmlTools.NAME,blockName);
		}
		
		// item
		if(item!=null)
		{	Element itemElement = new Element(XmlTools.ITEM);
			result.addContent(itemElement);
			String itemName = item;
			itemElement.setAttribute(XmlTools.NAME,itemName);
		}
		
		// bomb		
		if(bomb!=null)
		{	Element bombElement = new Element(XmlTools.BOMB);
			result.addContent(bombElement);
			String temp[] = bomb.split(Theme.PROPERTY_SEPARATOR);
			String bombName = temp[0];
			bombElement.setAttribute(XmlTools.NAME,bombName);
			String bombRange = temp[1];
			bombElement.setAttribute(XmlTools.RANGE,bombRange);
		}

		return result;
    }
}

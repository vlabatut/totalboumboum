package org.totalboumboum.engine.container.level.zone;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Comment;
import org.jdom.Element;
import org.totalboumboum.engine.container.level.variabletile.VariableTile;
import org.totalboumboum.engine.container.level.variabletile.VariableTilesSaver;
import org.totalboumboum.engine.container.theme.Theme;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 */
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
	{	Element result = new Element(XmlNames.ZONE);
		
		// GPL comment
		Comment gplComment = XmlTools.getGplComment();
		result.addContent(gplComment);

		// tiles random variable
		HashMap<String,VariableTile> variableTiles = zone.getVariableTiles();
		if(!variableTiles.isEmpty())
		{	Element variableTilesElement = VariableTilesSaver.saveVariableTilesElement(variableTiles);
			result.addContent(variableTilesElement);
		}
		
		// matrix
		Element matrixElement = saveMatrixElement(zone);
		result.addContent(matrixElement);
		
		// events
		Element eventsElement = saveEventsElement(zone);
		if(eventsElement!=null)
			result.addContent(eventsElement);
		
		return result;
	}
    
    private static Element saveEventsElement(Zone zone)
    {	Element result = null;

    	HashMap<Long, List<ZoneHollowTile>> map = zone.getEvents();
    	if(!map.isEmpty())
    	{	result = new Element(XmlNames.EVENTS);
			
    		// duration
    		long totalDuration = zone.getEventsDuration();
    		result.setAttribute(XmlNames.DURATION,Long.toString(totalDuration));
	    	
	    	// relative
    		boolean relative = zone.isEventsRelative();
    		result.setAttribute(XmlNames.RELATIVE,Boolean.toString(relative));
	    	
	    	// process each event
    		List<Long> times = new ArrayList<Long>(map.keySet());
    		Collections.sort(times);
    		for(Long time: times)
    		{	// create event element
    			List<ZoneHollowTile> tiles = map.get(time);
    			Element eventElement = new Element(XmlNames.EVENT);
    			eventElement.setAttribute(XmlNames.TIME,Long.toString(time));
    			result.addContent(eventElement);
				
    			// order tiles
    			HashMap<Integer,HashMap<Integer,List<ZoneHollowTile>>> temp = new HashMap<Integer, HashMap<Integer,List<ZoneHollowTile>>>();
    			for(ZoneHollowTile tile: tiles)
    			{	int row = tile.getRow();
    				HashMap<Integer,List<ZoneHollowTile>> temp2 = temp.get(row);
    				if(temp2 == null)
    				{	temp2 = new HashMap<Integer, List<ZoneHollowTile>>();
    					temp.put(row,temp2);
    				}
    				int col = tile.getCol();
    				List<ZoneHollowTile> temp3 = temp2.get(col);
    				if(temp3 == null)
    				{	temp3 = new ArrayList<ZoneHollowTile>();
    					temp2.put(col,temp3);
    				}
    				temp3.add(tile);
    			}
				
    			// process line
    			for(Entry<Integer,HashMap<Integer,List<ZoneHollowTile>>> entry: temp.entrySet())
    			{	// create row element
    				int row = entry.getKey();
    				Element rowElement = new Element(XmlNames.LINE);
    				rowElement.setAttribute(XmlNames.POSITION,Integer.toString(row));
    				eventElement.addContent(rowElement);
    				
		    		HashMap<Integer,List<ZoneHollowTile>> temp2 = entry.getValue();
        			for(Entry<Integer,List<ZoneHollowTile>> entry2: temp2.entrySet())
        			{	int col = entry2.getKey();
    					List<ZoneHollowTile> temp3 = entry2.getValue();
    					for(ZoneHollowTile tile: temp3)
    					{	// process contant terms
			        		String floor = tile.getFloor();
			            	String block = tile.getBlock();
			           		String item = tile.getItem();
			           		String bomb = tile.getBomb();
			           		Element tileElement = saveTileElement(floor,block,item,bomb);
			    			tileElement.setAttribute(XmlNames.POSITION,Integer.toString(col));
			        		
			    			// process variable term
			        		String variable = tile.getVariable();
			        		if(variable!=null)
			        		{	Element variableElement = new Element(XmlNames.REFERENCE);
			        			variableElement.setAttribute(XmlNames.NAME,variable);
			        			tileElement.addContent(variableElement);
			        		}
			        		
			        		// set row element
			        		rowElement.addContent(tileElement);	    	
			        	}
         			}
    			}
    		}
    	}
    	
    	return result;
    }

    private static Element saveMatrixElement(Zone zone)
    {	Element result = new Element(XmlNames.MATRIX);
    	
    	// create and init all elements
    	int width = zone.getGlobalWidth();
    	int height = zone.getGlobalHeight();
    	for(int row=0;row<height;row++)
    	{	Element rowElement = new Element(XmlNames.LINE);
			rowElement.setAttribute(XmlNames.POSITION,Integer.toString(row));
			result.addContent(rowElement);
    		for(int col=0;col<width;col++)
	    	{	ZoneHollowTile tile = zone.getTile(row,col);
    			// process contant terms
        		String floor = tile.getFloor();
            	String block = tile.getBlock();
           		String item = tile.getItem();
           		String bomb = tile.getBomb();
           		Element tileElement = saveTileElement(floor,block,item,bomb);
    			tileElement.setAttribute(XmlNames.POSITION,Integer.toString(col));
        		// process variable term
        		String variable = tile.getVariable();
        		if(variable!=null)
        		{	Element variableElement = new Element(XmlNames.REFERENCE);
        			variableElement.setAttribute(XmlNames.NAME,variable);
        			tileElement.addContent(variableElement);
        		}
        		// set row element
        		rowElement.addContent(tileElement);	    	}
    	}
 
    	return result;
    }
    
    private static Element saveTileElement(String floor, String block, String item, String bomb)
    {	Element result = new Element(XmlNames.TILE);
    	saveTileContent(result,floor,block,item,bomb);
		return result;
    }
    
    public static void saveTileContent(Element result, String floor, String block, String item, String bomb)
    {	// floor
		if(floor!=null)
		{	Element floorElement = new Element(XmlNames.FLOOR);
			result.addContent(floorElement);
			String floorName = floor;
			floorElement.setAttribute(XmlNames.NAME,floorName);
		}
		
		// block
		if(block!=null)
		{	Element blockElement = new Element(XmlNames.BLOCK);
			result.addContent(blockElement);
			String temp[] = block.split(Theme.GROUP_SEPARATOR);
			String blockGroup = temp[0];
			blockElement.setAttribute(XmlNames.GROUP,blockGroup);
			String blockName = temp[1];
			blockElement.setAttribute(XmlNames.NAME,blockName);
		}
		
		// item
		if(item!=null)
		{	Element itemElement = new Element(XmlNames.ITEM);
			result.addContent(itemElement);
			String itemName = item;
			itemElement.setAttribute(XmlNames.NAME,itemName);
		}
		
		// bomb		
		if(bomb!=null)
		{	Element bombElement = new Element(XmlNames.BOMB);
			result.addContent(bombElement);
			String temp[] = bomb.split(Theme.PROPERTY_SEPARATOR);
			String bombName = temp[0];
			bombElement.setAttribute(XmlNames.NAME,bombName);
			String bombRange = temp[1];
			bombElement.setAttribute(XmlNames.RANGE,bombRange);
		}
    }
}

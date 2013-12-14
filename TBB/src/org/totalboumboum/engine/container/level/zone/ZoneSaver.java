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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

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
 * Records a zone (as a file).
 * 
 * @author Vincent Labatut
 */
public class ZoneSaver
{	
	/**
	 * Records the specified zone as a file in the specified folder.
	 *  
	 * @param folder
	 * 		Folder to contain the file to create.
	 * @param zone
	 * 		Zone to record.
	 * 
	 * @throws IllegalArgumentException
	 * 		Problem while recording the zone.
	 * @throws SecurityException
	 * 		Problem while recording the zone.
	 * @throws ParserConfigurationException
	 * 		Problem while recording the zone.
	 * @throws SAXException
	 * 		Problem while recording the zone.
	 * @throws IOException
	 * 		Problem while recording the zone.
	 * @throws IllegalAccessException
	 * 		Problem while recording the zone.
	 * @throws NoSuchFieldException
	 * 		Problem while recording the zone.
	 */
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

	/**
	 * Initializes the root element of the xml file.
	 * 
	 * @param zone
	 * 		Zone to record.
	 * @return
	 * 		Root XML element.
	 */
	private static Element saveZoneElement(Zone zone)
	{	Element result = new Element(XmlNames.ZONE);
		
		// GPL comment
		Comment gplComment = XmlTools.getGplComment();
		result.addContent(gplComment);

		// tiles random variable
		Map<String,VariableTile> variableTiles = zone.getVariableTiles();
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
    
	/**
	 * Generates the elements related to sudden death events.
	 * 
	 * @param zone
	 * 		Zone to record.
	 * @return
	 * 		Created element.
	 */
    private static Element saveEventsElement(Zone zone)
    {	Element result = null;

    	Map<Long, List<ZoneHollowTile>> map = zone.getEvents();
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
    			Map<String,Map<String,List<ZoneHollowTile>>> temp = new HashMap<String, Map<String,List<ZoneHollowTile>>>();
    			for(ZoneHollowTile tile: tiles)
    			{	Set<Integer> rows = tile.getRows();
    				String rowsStr = XmlTools.generateMultipleIntegerString(rows);
    				Map<String,List<ZoneHollowTile>> temp2 = temp.get(rowsStr);
    				if(temp2 == null)
    				{	temp2 = new HashMap<String, List<ZoneHollowTile>>();
    					temp.put(rowsStr,temp2);
    				}
    				Set<Integer> cols = tile.getCols();
    				String colsStr = XmlTools.generateMultipleIntegerString(cols);
    				List<ZoneHollowTile> temp3 = temp2.get(colsStr);
    				if(temp3 == null)
    				{	temp3 = new ArrayList<ZoneHollowTile>();
    					temp2.put(colsStr,temp3);
    				}
    				temp3.add(tile);
    			}
				
    			// process line
    			for(Entry<String,Map<String,List<ZoneHollowTile>>> entry: temp.entrySet())
    			{	// create row element
    				String rowsStr = entry.getKey();
    				Element rowElement = new Element(XmlNames.LINE);
    				rowElement.setAttribute(XmlNames.POSITION,rowsStr);
    				eventElement.addContent(rowElement);
    				
		    		Map<String,List<ZoneHollowTile>> temp2 = entry.getValue();
        			for(Entry<String,List<ZoneHollowTile>> entry2: temp2.entrySet())
        			{	String colsStr = entry2.getKey();
    					List<ZoneHollowTile> temp3 = entry2.getValue();
    					for(ZoneHollowTile tile: temp3)
    					{	int nRows = tile.getRowNumber();
    						if(nRows!=tile.getRows().size())
    							rowElement.setAttribute(XmlNames.NBR,Integer.toString(nRows));
    						
    						// process contant terms
			        		String floor = tile.getFloor();
			            	String block = tile.getBlock();
			           		String item = tile.getItem();
			           		String bomb = tile.getBomb();
			           		Element tileElement = saveTileElement(floor,block,item,bomb);
			    			tileElement.setAttribute(XmlNames.POSITION,colsStr);
			    			
    						int nCols = tile.getColNumber();
    						if(nCols!=tile.getCols().size())
    							tileElement.setAttribute(XmlNames.NBR,Integer.toString(nCols));

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

    /**
     * Processes the matrix part of the zone file.
     * 
     * @param zone
     * 		Zone to record.
     * @return
     * 		Matrix element.
     */
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
    
    /**
     * Generates a tile element.
     * 
     * @param floor
     * 		Floor name for tthe tile.
     * @param block
     * 		Block name for tthe tile.
     * @param item
     * 		Item name for tthe tile.
     * @param bomb
     * 		Bomb name for tthe tile.
     * @return
     * 		The corresponding tile element.
     */
    private static Element saveTileElement(String floor, String block, String item, String bomb)
    {	Element result = new Element(XmlNames.TILE);
    	saveTileContent(result,floor,block,item,bomb);
		return result;
    }
    
    /**
     * Adds the content of a tile
     * to the specified element.
     * 
     * @param result
     * 		Element to be completed.
     * @param floor
     * 		Floor name for tthe tile.
     * @param block
     * 		Block name for tthe tile.
     * @param item
     * 		Item name for tthe tile.
     * @param bomb
      * 		Bomb name for tthe tile.
    */
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

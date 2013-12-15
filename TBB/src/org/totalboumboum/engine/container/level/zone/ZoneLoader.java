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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.totalboumboum.engine.container.level.variabletile.VariableTile;
import org.totalboumboum.engine.container.level.variabletile.VariableTilesLoader;
import org.totalboumboum.engine.container.theme.Theme;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * Loads a zone from a file.
 * None of the random elements are valued at loading.
 * This allows generating different levels when the
 * same zone is played several times.
 * 
 * @author Vincent Labatut
 */
public class ZoneLoader
{	   
	/**
	 * Loads a zone from a file.
	 * 
	 * @param folder
	 * 		Folder containing the zone.
	 * @param globalHeight
	 * 		Height of the zone, in tiles.
	 * @param globalWidth
	 * 		Width of the zone, in tiles.
	 * @return
	 * 		The loaded zone object.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem during the loading.
	 * @throws SAXException
	 * 		Problem during the loading.
	 * @throws IOException
	 * 		Problem during the loading.
	 */
    public static Zone loadZone(String folder, int globalHeight, int globalWidth) throws ParserConfigurationException, SAXException, IOException
    {	// init
    	Zone result = new Zone(globalWidth,globalHeight);
    	Element root;
		String schemaFolder = FilePaths.getSchemasPath();
		String individualFolder = folder;
		File schemaFile,dataFile;
		
		// opening
		dataFile = new File(individualFolder+File.separator+FileNames.FILE_ZONE+FileNames.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_ZONE+FileNames.EXTENSION_SCHEMA);
		root = XmlTools.getRootFromFile(dataFile,schemaFile);
		
		// tiles random variable
		Element variables = root.getChild(XmlNames.VARIABLE_TILES);
		Map<String,VariableTile> variableTiles = VariableTilesLoader.loadVariableTilesElement(variables);
		result.setVariableTiles(variableTiles);
		
		// matrix
		Element matrx = root.getChild(XmlNames.MATRIX);
		loadMatrixElement(matrx,result);
		
		// events
		Element events = root.getChild(XmlNames.EVENTS);
		loadEventsElement(events,result);
		
		return result;
    }
       
    /**
     * Processes the matrix part of the XML file.
     * 
     * @param root
     * 		XML element representing the matrix.
     * @param result
     * 		Zone object to be completed.
     */
    private static void loadMatrixElement(Element root, Zone result)
    {	loadLineElements(root, result);
    }
    
    /**
     * Processes a line element from the XML file.
     * 
     * @param root
     * 		XML element representing the line.
     * @param result
     * 		Zone object to be completed.
     */
    @SuppressWarnings("unchecked")
   private static void loadLineElements(Element root, Zone result)
    {	// matrix
    	Map<String,VariableTile> variableTiles = result.getVariableTiles();
    	List<Element> elements = root.getChildren(XmlNames.LINE);
    	Iterator<Element> i = elements.iterator();
    	while(i.hasNext())
    	{	Element row = i.next();
    		String posLstr = row.getAttributeValue(XmlNames.POSITION);
    		int posL = Integer.parseInt(posLstr);
    		List<Element> elementsL = row.getChildren(XmlNames.TILE);
        	Iterator<Element> iL = elementsL.iterator();
        	while(iL.hasNext())
        	{	String[] content = {null,null,null,null};
        		Element tile = iL.next();
        		int posT = Integer.parseInt(tile.getAttributeValue(XmlNames.POSITION));
        		ZoneHollowTile zt = new ZoneHollowTile(posL,posT);
        		
        		// constant parts
        		content = loadBasicTileElement(tile);
    			// floor
    			if(content[0]!=null)
    				zt.setFloor(content[0]);
    			// blocks
    			if(content[1]!=null)
    				zt.setBlock(content[1]);
    			// items
    			if(content[2]!=null)
    				zt.setItem(content[2]);
    			// bombs
    			if(content[3]!=null)
    				zt.setBomb(content[3]);        		
        		
    			// variable part
        		Element elt = tile.getChild(XmlNames.REFERENCE);
        		if(elt!=null)
        		{	String name = elt.getAttribute(XmlNames.NAME).getValue();
        			zt.setVariable(name);
        			VariableTile vt = variableTiles.get(name);
        			vt.incrementOccurrencesCount();
        		}
       			result.addTile(zt);
        	}
    	}
    }
    
    /**
     * Processes certain parts of a tile element.
     * 
     * @param root
     * 		XML element representing the tile.
     * @return
     * 		A string representing the tile properties.
     */
    @SuppressWarnings("unchecked")
    public static String[] loadBasicTileElement(Element root)
    {	String[] result = new String[4];
		
    	// floor
		List<Element> elementsT = root.getChildren(XmlNames.FLOOR);
		if(elementsT.size()>0)
		{	String name = elementsT.get(0).getAttribute(XmlNames.NAME).getValue();
			result[0] = name;
		}
		
		// block
		elementsT = root.getChildren(XmlNames.BLOCK);
		if(elementsT.size()>0)
		{	String name = elementsT.get(0).getAttribute(XmlNames.NAME).getValue();
			String group;
			Attribute attribute = elementsT.get(0).getAttribute(XmlNames.GROUP);
			if(attribute!=null)
				group = attribute.getValue();
			else
				group = Theme.DEFAULT_GROUP;
			result[1] = group+Theme.GROUP_SEPARATOR+name;
		}
		
		// item
		elementsT = root.getChildren(XmlNames.ITEM);
		if(elementsT.size()>0)
		{	String name = elementsT.get(0).getAttribute(XmlNames.NAME).getValue();
			result[2] = name;
		}

		// bomb
		elementsT = root.getChildren(XmlNames.BOMB);
		if(elementsT.size()>0)
		{	Element element = elementsT.get(0);
			String name = element.getAttribute(XmlNames.NAME).getValue();
			String range = element.getAttribute(XmlNames.RANGE).getValue();
			String duration = element.getAttribute(XmlNames.DURATION).getValue();
			result[3] = name+Theme.PROPERTY_SEPARATOR+range+Theme.PROPERTY_SEPARATOR+duration;
		}

		return result;
    }

    /**
     * Processes the element containing all sudden-death events.
     * 
     * @param root
     * 		XML element representing the event list.
     * @param result
     * 		Zone object to be completed.
     */
    @SuppressWarnings("unchecked")
    private static void loadEventsElement(Element root, Zone result)
    {	if(root!=null)
    	{	// duration
    		long totalDuration = 0;
			{	Attribute attribute = root.getAttribute(XmlNames.DURATION);
				totalDuration = Long.valueOf(attribute.getValue());
				result.setEventsDuration(totalDuration);
			}
	    	
	    	// relative
			{	Attribute attribute = root.getAttribute(XmlNames.RELATIVE);
				boolean relative = Boolean.valueOf(attribute.getValue());
				result.setEventsRelative(relative);
			}
	    	
	    	// process each event
	    	List<Element> elements = root.getChildren(XmlNames.EVENT);
	    	Iterator<Element> i = elements.iterator();
	    	while(i.hasNext())
	    	{	// get element
	    		Element event = i.next();
	    		// get time
				Attribute attribute = event.getAttribute(XmlNames.TIME);
				long time = Long.valueOf(attribute.getValue());
if(time==7000)
	System.out.print("");
				if(time>totalDuration)
					throw new IndexOutOfBoundsException("Zone events: one of the time step values is larger than the total duration.");
				// complete result
				loadEvtLineElements(event, result, time);
	    	}
    	}
    }
    
   /**
    * Processes a line element in the event list.
    * 
    * @param root
    * 		XML element representing the line.
    * @param result
    * 		Zone object to be completed.
	* @param time 
	* 		Time of the event.
    */
   @SuppressWarnings("unchecked")
	private static void loadEvtLineElements(Element root, Zone result, long time)
    {	// matrix
    	Map<String,VariableTile> variableTiles = result.getVariableTiles();
    	List<Element> elements = root.getChildren(XmlNames.LINE);
    	Iterator<Element> i = elements.iterator();
    	while(i.hasNext())
    	{	Element row = i.next();
    		String possiblePosLstr = row.getAttribute(XmlNames.POSITION).getValue().trim();
    		Set<Integer> possiblePosLs = XmlTools.parseMultipleInteger(possiblePosLstr);
    		int nbrL = possiblePosLs.size();
    		String nbrLstr = row.getAttributeValue(XmlNames.NBR);
    		if(nbrLstr!=null)
    			nbrL = Integer.parseInt(nbrLstr);
    		
    		List<Element> elementsL = row.getChildren(XmlNames.TILE);
        	Iterator<Element> iL = elementsL.iterator();
        	while(iL.hasNext())
        	{	String[] content = {null,null,null,null};
        		Element tile = iL.next();
        		content = loadBasicTileElement(tile);
        		
        		String possiblePosTstr = tile.getAttribute(XmlNames.POSITION).getValue().trim();
        		Set<Integer> possiblePosTs = XmlTools.parseMultipleInteger(possiblePosTstr);
        		int nbrT = possiblePosTs.size();
        		String nbrTstr = tile.getAttributeValue(XmlNames.NBR);
        		if(nbrTstr!=null)
        			nbrT = Integer.parseInt(nbrTstr);

        		
//        		List<List<Integer>> pos = new ArrayList<List<Integer>>();
//        		if(fixed)
//        		{	List<Integer> drawnPosT = drawPositions(possiblePosTs, nbrT);
//       				for(int l: drawnPosL)
//       				{	for(int t: drawnPosT)
//       					{	List<Integer> tmp = Arrays.asList(l,t);
//   							pos.add(tmp);
//       					}
//       				}
//        		}
//        		else
//        		{	int nbr = nbrT*nbrL;
//        			List<List<Integer>> temp = new ArrayList<List<Integer>>();
//        			for(int l: possiblePosLs)
//       				{	for(int t: possiblePosTs)
//       					{	List<Integer> tmp = Arrays.asList(l,t);
//       						temp.add(tmp);
//       					}
//       				}
//        			pos = drawPositions(temp, nbr);
//        		}
        		
//        		for(List<Integer> p: pos)
//        		{	int l = p.get(0);
//        			int t = p.get(1);
//        			ZoneHollowTile zt = new ZoneHollowTile(l,t);
        			ZoneHollowTile zt = new ZoneHollowTile(possiblePosLs,nbrL,possiblePosTs,nbrT);
	        		
	        		// constant parts
	    			// floor
	    			if(content[0]!=null)
	    				zt.setFloor(content[0]);
	    			// blocks
	    			if(content[1]!=null)
	    				zt.setBlock(content[1]);
	    			// items
	    			if(content[2]!=null)
	    				zt.setItem(content[2]);
	    			// bombs
	    			if(content[3]!=null)
	    				zt.setBomb(content[3]);        		
	        		
	    			// variable part
	        		Element elt = tile.getChild(XmlNames.REFERENCE);
	        		if(elt!=null)
	        		{	String name = elt.getAttribute(XmlNames.NAME).getValue();
	        			zt.setVariable(name);
	        			VariableTile vt = variableTiles.get(name);
	        			vt.incrementOccurrencesCount();
	        		}
	    			result.addEvent(time, zt);
//        		}
        	}
    	}
    }
}

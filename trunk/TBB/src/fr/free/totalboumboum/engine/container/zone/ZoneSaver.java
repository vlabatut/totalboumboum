package fr.free.totalboumboum.engine.container.zone;

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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.controls.ControlSettings;
import fr.free.totalboumboum.configuration.controls.ControlSettingsSaver;
import fr.free.totalboumboum.configuration.controls.ControlsConfiguration;
import fr.free.totalboumboum.engine.container.theme.Theme;
import fr.free.totalboumboum.engine.container.tile.VariableTile;
import fr.free.totalboumboum.engine.container.tile.VariableTilesLoader;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class ZoneSaver
{	    
	public static void saveZone(String folder, Zone zone) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	// build document
		Element root = saveZoneElement(zone);	
		// save file
		String individualFolder = folder;
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_ZONE+FileTools.EXTENSION_XML);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_CONTROLS+FileTools.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveZoneElement(Zone zone)
	{	Element result = new Element(XmlTools.ZONE);
		
		// tiles random variable
		Element variableTilesElement = saveVariableTilesElement(zone);
		result.addContent(variableTilesElement);
		
		// matrix
		Element matrixElement = saveMatrixElement(zone);
		result.addContent(matrixElement);
		return result;
	}
        
    private static Element saveMatrixElement(Zone zone)
    {	// init variables
    	ArrayList<String[][]> matrices = zone.getMatrices();
    	String[][] matrixFloors = matrices.get(0);
    	String[][] matrixBlocks = matrices.get(1);
    	String[][] matrixItems = matrices.get(2);
    	String[][] matrixBombs = matrices.get(3);
    	Element result = new Element(XmlTools.MATRIX);
    	
    	// create and init all elements
    	for(int line=0;line<zone.getGlobalHeight();line++)
    	{	Element lineElement = new Element(XmlTools.LINE);
    		lineElement.setAttribute(XmlTools.POSITION, Integer.toString(line));
    		result.addContent(lineElement);
        	for(int col=0;col<zone.getGlobalWidth();col++)
        	{	Element tileElement = new Element(XmlTools.TILE);
    			tileElement.setAttribute(XmlTools.POSITION, Integer.toString(col));
        		lineElement.addContent(tileElement);
        		String floor = matrixFloors[line][col];
        	}
    	}
    	


    	
    	HashMap<String,VariableTile> variableTiles = result.getVariableTiles();
    	List<Element> elements = root.getChildren(XmlTools.LINE);
    	Iterator<Element> i = elements.iterator();
    	while(i.hasNext())
    	{	Element line = i.next();
    		int posL = Integer.parseInt(line.getAttribute(XmlTools.POSITION).getValue().trim());
    		List<Element> elementsL = line.getChildren(XmlTools.TILE);
        	Iterator<Element> iL = elementsL.iterator();
        	while(iL.hasNext())
        	{	String[] content = {null,null,null,null};
        		Element tile = iL.next();
        		int posT = Integer.parseInt(tile.getAttribute(XmlTools.POSITION).getValue().trim());
        		ZoneTile zt = new ZoneTile(posL,posT);
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
        		Element elt = tile.getChild(XmlTools.REFERENCE);
        		if(elt!=null)
        		{	String name = elt.getAttribute(XmlTools.NAME).getValue();
        			zt.setVariable(name);
        			VariableTile vt = variableTiles.get(name);
        			vt.incrementOccurrencesCount();
        		}
        		result.addTile(zt);
        	}
    	}
    }
    
    @SuppressWarnings("unchecked")
    public static String[] saveBasicTileElement(Element root)
    {	String[] result = new String[4];
		
    	// floor
		List<Element> elementsT = root.getChildren(XmlTools.FLOOR);
		if(elementsT.size()>0)
		{	String name = elementsT.get(0).getAttribute(XmlTools.NAME).getValue();
			result[0] = name;
		}
		
		// block
		elementsT = root.getChildren(XmlTools.BLOCK);
		if(elementsT.size()>0)
		{	String name = elementsT.get(0).getAttribute(XmlTools.NAME).getValue();
			String group;
			Attribute attribute = elementsT.get(0).getAttribute(XmlTools.GROUP);
			if(attribute!=null)
				group = attribute.getValue();
			else
				group = Theme.DEFAULT_GROUP;
			result[1] = group+Theme.GROUP_SEPARATOR+name;
		}
		
		// item
		elementsT = root.getChildren(XmlTools.ITEM);
		if(elementsT.size()>0)
		{	String name = elementsT.get(0).getAttribute(XmlTools.NAME).getValue();
			result[2] = name;
		}

		// bomb
		elementsT = root.getChildren(XmlTools.BOMB);
		if(elementsT.size()>0)
		{	String name = elementsT.get(0).getAttribute(XmlTools.NAME).getValue();
			String range = elementsT.get(0).getAttribute(XmlTools.RANGE).getValue();
			result[3] = name+":"+range;
		}

		return result;
    }
}

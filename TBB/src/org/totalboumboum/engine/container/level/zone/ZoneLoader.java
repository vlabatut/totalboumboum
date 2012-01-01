package org.totalboumboum.engine.container.level.zone;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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
import java.util.Iterator;
import java.util.List;

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
 * 
 * @author Vincent Labatut
 *
 */
public class ZoneLoader
{	    
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
		HashMap<String,VariableTile> variableTiles = VariableTilesLoader.loadVariableTilesElement(variables);
		result.setVariableTiles(variableTiles);
		
		// matrix
		Element matrx = root.getChild(XmlNames.MATRIX);
		loadMatrixElement(matrx,globalHeight,globalWidth,result);
		return result;
    }
        
    @SuppressWarnings("unchecked")
    private static void loadMatrixElement(Element root, int globalHeight, int globalWidth, Zone result)
    {	// matrix
    	HashMap<String,VariableTile> variableTiles = result.getVariableTiles();
    	List<Element> elements = root.getChildren(XmlNames.LINE);
    	Iterator<Element> i = elements.iterator();
    	while(i.hasNext())
    	{	Element row = i.next();
    		int posL = Integer.parseInt(row.getAttribute(XmlNames.POSITION).getValue().trim());
    		List<Element> elementsL = row.getChildren(XmlNames.TILE);
        	Iterator<Element> iL = elementsL.iterator();
        	while(iL.hasNext())
        	{	String[] content = {null,null,null,null};
        		Element tile = iL.next();
        		int posT = Integer.parseInt(tile.getAttribute(XmlNames.POSITION).getValue().trim());
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
}

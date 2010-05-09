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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.theme.Theme;
import fr.free.totalboumboum.engine.container.tile.VariableTile;
import fr.free.totalboumboum.engine.container.tile.VariableTilesLoader;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class ZoneLoader
{	    
    public static Zone loadZone(String folder, int globalHeight, int globalWidth) throws ParserConfigurationException, SAXException, IOException
    {	// init
    	Zone result = new Zone(globalWidth,globalHeight);
    	Element root;
		String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = folder;
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_ZONE+FileTools.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_ZONE+FileTools.EXTENSION_SCHEMA);
		root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// tiles random variable
		Element variables = root.getChild(XmlTools.ELT_VARIABLE_TILES);
		HashMap<String,VariableTile> variableTiles = VariableTilesLoader.loadVariableTilesElement(variables);
		result.setVariableTiles(variableTiles);
		// matrix
		Element matrx = root.getChild(XmlTools.ELT_MATRIX);
		loadMatrixElement(matrx,globalHeight,globalWidth,result);
		return result;
    }
        
    @SuppressWarnings("unchecked")
    private static void loadMatrixElement(Element root, int globalHeight, int globalWidth, Zone result)
    {	// matrix
    	HashMap<String,VariableTile> variableTiles = result.getVariableTiles();
    	List<Element> elements = root.getChildren(XmlTools.ELT_LINE);
    	Iterator<Element> i = elements.iterator();
    	while(i.hasNext())
    	{	Element line = i.next();
    		int posL = Integer.parseInt(line.getAttribute(XmlTools.ATT_POSITION).getValue().trim());
    		List<Element> elementsL = line.getChildren(XmlTools.ELT_TILE);
        	Iterator<Element> iL = elementsL.iterator();
        	while(iL.hasNext())
        	{	String[] content = {null,null,null};
        		Element tile = iL.next();
        		int posT = Integer.parseInt(tile.getAttribute(XmlTools.ATT_POSITION).getValue().trim());
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
        		// variable part
        		Element elt = tile.getChild(XmlTools.ELT_REFERENCE);
        		if(elt!=null)
        		{	String name = elt.getAttribute(XmlTools.ATT_NAME).getValue();
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
    {	String[] result = new String[3];
		// floor
		List<Element> elementsT = root.getChildren(XmlTools.ELT_FLOOR);
		if(elementsT.size()>0)
		{	String name = elementsT.get(0).getAttribute(XmlTools.ATT_NAME).getValue();
			result[0] = name;
		}
		// block
		elementsT = root.getChildren(XmlTools.ELT_BLOCK);
		if(elementsT.size()>0)
		{	String name = elementsT.get(0).getAttribute(XmlTools.ATT_NAME).getValue();
			String group;
			Attribute attribute = elementsT.get(0).getAttribute(XmlTools.ATT_GROUP);
			if(attribute!=null)
				group = attribute.getValue();
			else
				group = Theme.DEFAULT_GROUP;
			result[1] = group+Theme.GROUP_SEPARATOR+name;
		}
		// item
		elementsT = root.getChildren(XmlTools.ELT_ITEM);
		if(elementsT.size()>0)
		{	String type = elementsT.get(0).getAttribute(XmlTools.ATT_TYPE).getValue();
			result[2] = type;
		}
		//
		return result;
    }
}

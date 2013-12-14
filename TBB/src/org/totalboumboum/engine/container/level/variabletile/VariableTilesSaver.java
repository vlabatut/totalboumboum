package org.totalboumboum.engine.container.level.variabletile;

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

import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.totalboumboum.engine.container.level.zone.ZoneSaver;
import org.totalboumboum.tools.xml.XmlNames;

/**
 * Create the appropriate XML element to
 * represent one or several tile variables.
 * 
 * @author Vincent Labatut
 */
public class VariableTilesSaver
{	
	/**
	 * Creates the general element containing all variable elements.
	 * 
	 * @param variableTiles
	 * 		Map of variable tile objects.
	 * @return
	 * 		Corresponding XML element.
	 */
    public static Element saveVariableTilesElement(Map<String,VariableTile> variableTiles)
    {	Element result = new Element(XmlNames.VARIABLE_TILES);
    	for(VariableTile variableTile: variableTiles.values())
    	{	Element variableTileElement = saveVariableTileElement(variableTile);
    		result.addContent(variableTileElement);
    	}
    	return result;
    }

    /**
     * Creates an element representing a single variable tile.
     * 
     * @param variableTile
     * 		Object to represent.
     * @return
     * 		Corresponding element.
     */
    private static Element saveVariableTileElement(VariableTile variableTile)
    {	Element result = new Element(XmlNames.VARIABLE_TILE);
    	
    	// name
    	result.setAttribute(XmlNames.NAME,variableTile.getName());

    	// values
    	List<ValueTile> valueTiles = variableTile.getValues();
    	for(ValueTile valueTile: valueTiles)
    	{	Element valueTileElement = saveValueTileElement(valueTile);
    		result.addContent(valueTileElement);
    	}
    	
    	return result;
    }
    
    /**
     * Creates an element corresponding to a tile value.
     *  
     * @param valueTile
     * 		Tile value.
     * @return
     * 		Corresponding element.
     */
    private static Element saveValueTileElement(ValueTile valueTile)
    {	// tile terms
    	String floor = valueTile.getFloor();
    	String block = valueTile.getBlock();
    	String item = valueTile.getItem();
    	String bomb = valueTile.getBomb();
    	Element result = new Element(XmlNames.VALUE);
    	ZoneSaver.saveTileContent(result,floor,block,item,bomb);
    	
    	// probability
		double proba = valueTile.getProba();
		result.setAttribute(XmlNames.PROBA,Double.toString(proba));
		
		return result;
    }
}

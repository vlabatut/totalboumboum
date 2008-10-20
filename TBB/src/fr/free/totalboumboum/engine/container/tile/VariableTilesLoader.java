package fr.free.totalboumboum.engine.container.tile;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import fr.free.totalboumboum.engine.container.zone.ZoneLoader;
import fr.free.totalboumboum.tools.XmlTools;

public class VariableTilesLoader
{	
    @SuppressWarnings("unchecked")
    public static HashMap<String,VariableTile> loadVariableTilesElement(Element root)
    {	HashMap<String,VariableTile> result = new HashMap<String, VariableTile>();
    	List<Element> elements = root.getChildren(XmlTools.ELT_VARIABLE_TILE);
    	Iterator<Element> i = elements.iterator();
    	while(i.hasNext())
    	{	Element temp = i.next();
    		VariableTile vi = loadVariableTileElement(temp);
    		result.put(vi.getName(), vi);
    	}
    	return result;
    }
    
    @SuppressWarnings("unchecked")
    private static VariableTile loadVariableTileElement(Element root)
    {	String name = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
    	VariableTile result = new VariableTile(name);
    	List<Element> elements = root.getChildren(XmlTools.ELT_VALUE);
    	Iterator<Element> i = elements.iterator();
    	float sum = 0;
    	while(i.hasNext())
    	{	Element temp = i.next();
    		String[] elts = ZoneLoader.loadBasicTileElement(temp);    	
        	float tProba = Float.valueOf(temp.getAttribute(XmlTools.ATT_PROBA).getValue().trim());
    		ValueTile vt = new ValueTile(elts[0],elts[1],elts[2],tProba);
    		/*
    		 * NOTE 
    		 * 	- v�rifier que les noms des blocks/items/itemvariables/floor r�f�renc�s sont bien d�finis
    		 * 
    		 */
        	sum = sum+tProba;
    		result.addValue(vt);
    	}
    	// normalize probas
    	for(int j=0;j<elements.size();j++)
    	{	float p = result.getProba(j);
    		p = p / sum;
    		result.setProba(j,p);
    	}
    	//
    	return result;
    }
}

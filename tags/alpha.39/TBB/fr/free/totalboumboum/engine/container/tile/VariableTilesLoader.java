package fr.free.totalboumboum.engine.container.tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;

import fr.free.totalboumboum.engine.container.level.LevelLoader;
import fr.free.totalboumboum.tools.XmlTools;


public class VariableTilesLoader
{

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
    
    private static VariableTile loadVariableTileElement(Element root)
    {	String name = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
    	VariableTile result = new VariableTile(name);
    	List<Element> elements = root.getChildren(XmlTools.ELT_VALUE);
    	Iterator<Element> i = elements.iterator();
    	float sum = 0;
    	while(i.hasNext())
    	{	Element temp = i.next();
    		String[] elts = LevelLoader.loadBasicTileElement(temp);    	
        	float tProba = Float.valueOf(temp.getAttribute(XmlTools.ATT_PROBA).getValue().trim());
    		ValueTile vt = new ValueTile(elts[0],elts[1],elts[2],tProba);
    		/*
    		 * NOTE 
    		 * 	- vérifier que les noms des blocks/items/itemvariables/floor référencés sont bien définis
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

package fr.free.totalboumboum.engine.content.feature.ability;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.action.GeneralAction;
import fr.free.totalboumboum.engine.content.feature.action.GeneralActionLoader;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;


public class AbilityLoader
{	
	
    private static AbstractAbility loadAbilityElement(Element root, Level level) throws ClassNotFoundException
    {	AbstractAbility result = null;
		// max
		float max = Float.MAX_VALUE;
		Attribute attribute = root.getAttribute(XmlTools.ATT_MAX_STRENGTH);
		if(attribute!=null)
			max = Float.parseFloat(attribute.getValue());
    	// strength
		String strengthStr = root.getAttribute(XmlTools.ATT_STRENGTH).getValue().trim();
		float strength;
		if(strengthStr.equals(AbstractAbility.MAXIMUM_VALUE))
			strength = Float.MAX_VALUE; //NOTE format de données à inclure dans le XSD
		else
			strength = Float.parseFloat(strengthStr);
    	// frame
		attribute = root.getAttribute(XmlTools.ATT_FRAME);
		boolean frame = Boolean.parseBoolean(attribute.getValue());
    	// uses
		attribute = root.getAttribute(XmlTools.ATT_USES);
		int uses = Integer.parseInt(attribute.getValue());
    	// time
		attribute = root.getAttribute(XmlTools.ATT_TIME);
		int time = Integer.parseInt(attribute.getValue());
		// state ?
		Element temp = root.getChild(XmlTools.ELT_NAME);
		if(temp!=null)
		{	String name = temp.getAttribute(XmlTools.ATT_VALUE).getValue();
			result = new StateAbility(name,level);
		}
		// or action ?
		else
		{	temp = root.getChild(XmlTools.ELT_ACTION);
			GeneralAction action = GeneralActionLoader.loadActionElement(temp);
			result = new ActionAbility(action,level);				
		}
		result.setMax(max);
		result.setStrength(strength);
		result.setFrame(frame);
		result.setUses(uses);
		result.setTime(time);
		return result;
    }
    
    public static ArrayList<AbstractAbility> loadAbilitiesElement(Element root, Level level) throws ClassNotFoundException
    {	ArrayList<AbstractAbility> result = new ArrayList<AbstractAbility>();
    	List<Element> abilitiesElts = root.getChildren(XmlTools.ELT_ABILITY);
		Iterator<Element> i = abilitiesElts.iterator();
		while(i.hasNext())
		{	Element elt = i.next();
			AbstractAbility ability = loadAbilityElement(elt,level);
			result.add(ability);
		}
		return result;
    }
    
	public static ArrayList<AbstractAbility> loadAbilityPack(String folderPath, Level level) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	ArrayList<AbstractAbility> result = new ArrayList<AbstractAbility>();
		File dataFile = new File(folderPath+File.separator+FileTools.FILE_ABILITIES+FileTools.EXTENSION_DATA);
		if(dataFile.exists())
		{	String schemaFolder = FileTools.getSchemasPath();
			File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_ABILITIES+FileTools.EXTENSION_SCHEMA);
			Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
			result = loadAbilitiesElement(root,level);
		}
		return result;
	}
}

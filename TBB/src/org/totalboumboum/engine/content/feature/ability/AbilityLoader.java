package org.totalboumboum.engine.content.feature.ability;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.totalboumboum.engine.content.feature.action.GeneralAction;
import org.totalboumboum.engine.content.feature.action.GeneralActionLoader;
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
public class AbilityLoader
{		
    private static AbstractAbility loadAbilityElement(Element root) throws ClassNotFoundException
    {	AbstractAbility result = null;
		// strength
		String strengthStr = root.getAttribute(XmlNames.STRENGTH).getValue().trim().toUpperCase(Locale.ENGLISH);
		float strength;
		if(strengthStr.equals(XmlNames.VAL_MAX))
			strength = Float.MAX_VALUE; //NOTE format de données à inclure dans le XSD (>> actually I removed it)
		else
			strength = Float.parseFloat(strengthStr);
    	// frame
		Attribute attribute = root.getAttribute(XmlNames.FRAME);
		boolean frame = Boolean.parseBoolean(attribute.getValue());
    	// uses
		attribute = root.getAttribute(XmlNames.USES);
		int uses = Integer.parseInt(attribute.getValue());
    	// time
		attribute = root.getAttribute(XmlNames.TIME);
		int time = Integer.parseInt(attribute.getValue());
		// state ?
		Element temp = root.getChild(XmlNames.NAME);
		if(temp!=null)
		{	String name = temp.getAttribute(XmlNames.VALUE).getValue().trim().toUpperCase(Locale.ENGLISH);
			result = new StateAbility(name);
		}
		// or action ?
		else
		{	temp = root.getChild(XmlNames.ACTION);
			GeneralAction action = GeneralActionLoader.loadActionElement(temp);
			result = new ActionAbility(action);				
		}
		result.setStrength(strength);
		result.setFrame(frame);
		result.setUses(uses);
		result.setTime(time);
		return result;
    }
    
    @SuppressWarnings("unchecked")
	public static List<AbstractAbility> loadAbilitiesElement(Element root) throws ClassNotFoundException
    {	List<AbstractAbility> result = new ArrayList<AbstractAbility>();
    	List<Element> abilitiesElts = root.getChildren(XmlNames.ABILITY);
		Iterator<Element> i = abilitiesElts.iterator();
		while(i.hasNext())
		{	Element elt = i.next();
			AbstractAbility ability = loadAbilityElement(elt);
			result.add(ability);
		}
		return result;
    }
    
	public static void loadAbilityPack(String folderPath, List<AbstractAbility> result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	File dataFile = new File(folderPath+File.separator+FileNames.FILE_ABILITIES+FileNames.EXTENSION_XML);
		if(dataFile.exists())
		{	String schemaFolder = FilePaths.getSchemasPath();
			File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_ABILITIES+FileNames.EXTENSION_SCHEMA);
			Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
			result.addAll(loadAbilitiesElement(root));
		}
	}
}

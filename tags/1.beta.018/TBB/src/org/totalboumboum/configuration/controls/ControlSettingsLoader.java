package org.totalboumboum.configuration.controls;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
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
public class ControlSettingsLoader
{	
	public static ControlSettings loadControlSettings(String fileName) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{	ControlSettings result;
		String controlFile = FilePaths.getControlsPath()+File.separator+fileName+FileNames.EXTENSION_XML;
		File dataFile = new File(controlFile);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_CONTROLS+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		result = loadControlsElement(root);
		return result;
	}
	
    @SuppressWarnings("unchecked")
	private static ControlSettings loadControlsElement(Element root) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{	ControlSettings result = new ControlSettings();
    	List<Element> eventsList = root.getChildren(XmlNames.EVENT);
		for(int i=0;i<eventsList.size();i++)
			loadEventElement(eventsList.get(i),result);
		return result;
	}
    
    private static void loadEventElement(Element root, ControlSettings result) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
    {	// name
    	String name = root.getAttribute(XmlNames.NAME).getValue();
    	// autofire
		String autofStr = root.getAttribute(XmlNames.AUTOFIRE).getValue();
		boolean autofire = Boolean.parseBoolean(autofStr);
		if(autofire)
			result.addAutofire(name);
		// on key
		Element onElt = root.getChild(XmlNames.ON);
		if(onElt!=null)
			loadKeyElement(onElt,name,true,result);
		// off key
		Element offElt = root.getChild(XmlNames.OFF);
		if(offElt!=null)
			loadKeyElement(offElt,name,false,result);
    }	
    
    /**
     * mode : true pour ON et false pour OFF
     */
    private static void loadKeyElement(Element root, String name, boolean mode, ControlSettings result) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
    {	// value
    	String cst = root.getAttribute(XmlNames.KEY).getValue();
    	int value = KeyEvent.class.getField(cst).getInt(KeyEvent.class);
    	if(mode)
    		result.addOnKey(value, name);
    	else
    		result.addOffKey(value, name);
    }	
}

package fr.free.totalboumboum.configuration.controls;

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

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.tools.ClassTools;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class ControlSettingsSaver
{	
	public static void saveControlSettings(String fileName, ControlSettings controlSettings) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{	// build document
		Element root = saveControlElement(controlSettings);	
		// save file
		String controlFile = FileTools.getControlsPath()+File.separator+fileName+FileTools.EXTENSION_XML;
		File dataFile = new File(controlFile);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_CONTROLS+FileTools.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveControlElement(ControlSettings controlSettings)
	{	Element result = new Element(XmlTools.CONTROLS);
		HashMap<String,Integer> onEvents = controlSettings.getOnEvents();
		Iterator<Entry<String,Integer>> onIt = onEvents.entrySet().iterator();
		while(onIt.hasNext())
		{	// data
			Entry<String,Integer> onEntry = onIt.next();
			String event = onEntry.getKey();
			int onKey = onEntry.getValue();
			int offKey = controlSettings.getOffKeyFromEvent(event);
			boolean autofire = controlSettings.isAutofire(event);
			// main element
			Element eventElement;
			{	String autofireText = Boolean.toString(autofire);
				eventElement = new Element(XmlTools.EVENT);
				eventElement.setAttribute(XmlTools.NAME,event);
				eventElement.setAttribute(XmlTools.AUTOFIRE,autofireText);
				result.addContent(eventElement);
			}
			// on element
			{	String onText = getKeyName(onKey);
				Element onElement = new Element(XmlTools.ON);
				onElement.setAttribute(XmlTools.KEY,onText);
				eventElement.addContent(onElement);
			}
			// off element
			{	String offText = getKeyName(offKey);
				Element offElement = new Element(XmlTools.OFF);
				offElement.setAttribute(XmlTools.KEY,offText);
				eventElement.addContent(offElement);
			}
		}
		return result;
	}
    
	private static String getKeyName(int key)
	{	String result = null;
		try
		{	Field field = ClassTools.getFieldFromValue(key,KeyEvent.class);
			result = field.getName();
		}
		catch (IllegalArgumentException e)
		{	e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{	e.printStackTrace();
		}
		return result;
	}
}

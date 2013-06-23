package org.totalboumboum.configuration.controls;

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

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Comment;
import org.jdom.Element;
import org.totalboumboum.tools.classes.ClassTools;
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
public class ControlSettingsSaver
{	
	public static void saveControlSettings(String fileName, ControlSettings controlSettings) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{	// build document
		Element root = saveControlElement(controlSettings);	
		
		// save file
		String controlFile = FilePaths.getControlsPath()+File.separator+fileName+FileNames.EXTENSION_XML;
		File dataFile = new File(controlFile);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_CONTROLS+FileNames.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveControlElement(ControlSettings controlSettings)
	{	Element result = new Element(XmlNames.CONTROLS);
	
		// GPL comment
		Comment gplComment = XmlTools.getGplComment();
		result.addContent(gplComment);

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
				eventElement = new Element(XmlNames.EVENT);
				eventElement.setAttribute(XmlNames.NAME,event);
				eventElement.setAttribute(XmlNames.AUTOFIRE,autofireText);
				result.addContent(eventElement);
			}
			// on element
			{	String onText = getKeyName(onKey);
				Element onElement = new Element(XmlNames.ON);
				onElement.setAttribute(XmlNames.KEY,onText);
				eventElement.addContent(onElement);
			}
			// off element
			{	String offText = getKeyName(offKey);
				Element offElement = new Element(XmlNames.OFF);
				offElement.setAttribute(XmlNames.KEY,offText);
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

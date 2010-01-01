package org.totalboumboum.gui.data.language;

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

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.totalboumboum.gui.tools.GuiFileTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiXmlTools;
import org.totalboumboum.tools.FileTools;
import org.totalboumboum.tools.XmlTools;
import org.xml.sax.SAXException;


public class LanguageLoader
{	
	public static Language loadLanguage(String name) throws ParserConfigurationException, SAXException, IOException
	{	Language result = new Language();
		String individualFolder = GuiFileTools.getLanguagesPath();
		File dataFile = new File(individualFolder+File.separator+name+FileTools.EXTENSION_XML);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_LANGUAGE+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadLanguageElement(root,result);
		return result;
	}

	@SuppressWarnings("unchecked")
	private static void loadLanguageElement(Element root, Language result)
	{	List<Element> elements = root.getChildren(GuiXmlTools.ELT_GROUP);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			loadGroupElement(temp,"",result);
		}
	}

	@SuppressWarnings("unchecked")
	private static void loadGroupElement(Element root, String name, Language result)
	{	String key = root.getAttribute(GuiXmlTools.ATT_NAME).getValue().trim();
		String newName = name+key;
		// text
		{	List<Element> elements = root.getChildren(GuiXmlTools.ELT_TEXT);
			Iterator<Element> i = elements.iterator();
			while(i.hasNext())
			{	Element temp = i.next();
				loadTextElement(temp,newName,result);
			}
		}
		// other groups
		{	List<Element> elements = root.getChildren(GuiXmlTools.ELT_GROUP);
			Iterator<Element> i = elements.iterator();
			while(i.hasNext())
			{	Element temp = i.next();
				loadGroupElement(temp,newName,result);
			}
		}
	}

	private static void loadTextElement(Element root, String name, Language result)
	{	String key = root.getAttribute(GuiXmlTools.ATT_NAME).getValue().trim();
		String newName = name+key;
		// value
		String value = root.getAttribute(GuiXmlTools.ATT_VALUE).getValue().trim();
		result.addText(newName,value);
		// tooltip
		Attribute att = root.getAttribute(GuiXmlTools.ATT_TOOLTIP);
		if(att!=null)
		{	String tooltip = att.getValue().trim();
			result.addText(newName+GuiKeys.TOOLTIP,tooltip);
		}
	}
}

package fr.free.totalboumboum.engine.log.logstats;

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

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class LogstatsLoader
{	
	public static void loadLogstats() throws SAXException, IOException
	{	String individualFolder = FileTools.getLogsPath();
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_LOGSTATS+FileTools.EXTENSION_XML);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_LOGSTATS+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadLogstatsElement(root);
	}

	private static void loadLogstatsElement(Element root)
	{	// regular launch
		Element regularLaunchElement = root.getChild(XmlTools.REGULAR_LAUNCH);
		loadRegularLaunchElement(regularLaunchElement);
		
		// quick launch
		Element quickLaunchElement = root.getChild(XmlTools.QUICK_LAUNCH);
		loadQuickLaunchElement(quickLaunchElement);
	}
	
	private static void loadRegularLaunchElement(Element root)
	{	// count
		String strCount = root.getAttribute(XmlTools.COUNT).getValue().trim();
		int count = Integer.parseInt(strCount);
		Logstats.setRegularLaunchCount(count);
		
		// time
		String strTime = root.getAttribute(XmlTools.TIME).getValue().trim();
		int time = Integer.parseInt(strTime);
		Logstats.setRegularLaunchTime(time);
	}
	
	private static void loadQuickLaunchElement(Element root)
	{	// count
		String strCount = root.getAttribute(XmlTools.COUNT).getValue().trim();
		int count = Integer.parseInt(strCount);
		Logstats.setQuickLaunchCount(count);
		
		// time
		String strTime = root.getAttribute(XmlTools.TIME).getValue().trim();
		int time = Integer.parseInt(strTime);
		Logstats.setQuickLaunchTime(time);
	}
}

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

import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class LogstatsSaver
{	
	public static void saveLogstats() throws IOException
	{	// build document
		Element root = saveLogstatsElement();	
		// save file
		String engineFile = FileTools.getLogsPath()+File.separator+FileTools.FILE_LOGSTATS+FileTools.EXTENSION_XML;
		File dataFile = new File(engineFile);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_LOGSTATS+FileTools.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveLogstatsElement()
	{	Element result = new Element(XmlTools.LOGSTATS); 
			
		// regular launch
		Element regularLaunchElement = saveRegularLaunchELement();
		result.addContent(regularLaunchElement);
	
		// quick launch
		Element quickLaunchElement = saveQuickLaunchELement();
		result.addContent(quickLaunchElement);
		
		return result;
	}
	
	private static Element saveRegularLaunchELement()
	{	Element result = new Element(XmlTools.REGULAR_LAUNCH); 
	
		// count
		String countStr = Long.toString(Logstats.getRegularLaunchCount());
		result.setAttribute(XmlTools.COUNT,countStr);

		// time
		String timeStr = Long.toString(Logstats.getRegularLaunchTime());
		result.setAttribute(XmlTools.TIME,timeStr);
		
		return result;
	}

	private static Element saveQuickLaunchELement()
	{	Element result = new Element(XmlTools.QUICK_LAUNCH); 
	
		// count
		String countStr = Long.toString(Logstats.getQuickLaunchCount());
		result.setAttribute(XmlTools.COUNT,countStr);

		// time
		String timeStr = Long.toString(Logstats.getQuickLaunchTime());
		result.setAttribute(XmlTools.TIME,timeStr);
		
		return result;
	}
}

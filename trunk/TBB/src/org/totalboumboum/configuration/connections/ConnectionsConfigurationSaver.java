package org.totalboumboum.configuration.connections;

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
public class ConnectionsConfigurationSaver
{	
	public static void saveConnectionsConfiguration(ConnectionsConfiguration connectionsConfiguration) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveConnectionsElement(connectionsConfiguration);	
		
		// save file
		String engineFile = FilePaths.getConfigurationPath()+File.separator+FileNames.FILE_CONNECTIONS+FileNames.EXTENSION_XML;
		File dataFile = new File(engineFile);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_CONNECTIONS+FileNames.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveConnectionsElement(ConnectionsConfiguration connectionsConfiguration)
	{	Element result = new Element(XmlNames.CONNECTIONS); 

		// central
		Element centralElement = saveCentralElement(connectionsConfiguration);
		result.addContent(centralElement);
		
		// hosting
		Element hostingElement = saveHostingElement(connectionsConfiguration);
		result.addContent(hostingElement);
		
		return result;
	}
	
	private static Element saveCentralElement(ConnectionsConfiguration connectionsConfiguration)
	{	Element result = new Element(XmlNames.CENTRAL);
		
		// ip
		String ip = connectionsConfiguration.getCentralIp().getHostName();
		result.setAttribute(XmlNames.IP,ip);
				
		return result;
	}

	private static Element saveHostingElement(ConnectionsConfiguration connectionsConfiguration)
	{	Element result = new Element(XmlNames.HOSTING);
		
		// port
		String port = Integer.toString(connectionsConfiguration.getPort());
		result.setAttribute(XmlNames.PORT,port);
			
		// id
		String id = connectionsConfiguration.getHostId();
		result.setAttribute(XmlNames.ID,id);
			
		// name
		String name = connectionsConfiguration.getHostName();
		result.setAttribute(XmlNames.NAME,name);
			
		// mac addresses
		List<String> macAddresses = connectionsConfiguration.getMacAddresses();
		String macStr = "";
		if(macAddresses.size()>0)
			macStr = macAddresses.get(0);
		for(int i=1;i<macAddresses.size();i++)
			macStr = macStr + " " + macAddresses.get(i);
		result.setAttribute(XmlNames.MAC,macStr);
			
		return result;
	}
}

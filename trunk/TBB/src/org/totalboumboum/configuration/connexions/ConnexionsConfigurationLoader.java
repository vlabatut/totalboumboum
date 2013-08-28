package org.totalboumboum.configuration.connexions;

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

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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
public class ConnexionsConfigurationLoader
{	
	public static ConnexionsConfiguration loadConnexionsConfiguration() throws SAXException, IOException
	{	ConnexionsConfiguration result = new ConnexionsConfiguration();
		String individualFolder = FilePaths.getConfigurationPath();
		File dataFile = new File(individualFolder+File.separator+FileNames.FILE_CONNECTIONS+FileNames.EXTENSION_XML);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_CONNECTIONS+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadConnexionsElement(root,result);
		return result;
	}

	private static void loadConnexionsElement(Element root, ConnexionsConfiguration result) throws UnknownHostException
	{	// central
		Element centralElement = root.getChild(XmlNames.CENTRAL);
		loadCentralElement(centralElement,result);
		
		// hosting
		Element hostingElement = root.getChild(XmlNames.HOSTING);
		loadHostingElement(hostingElement,result);
	}
	
	private static void loadCentralElement(Element root, ConnexionsConfiguration result) throws UnknownHostException
	{	// ip
		String centralIp = root.getAttribute(XmlNames.IP).getValue().trim();
		result.setCentralIp(centralIp);
	}

	private static void loadHostingElement(Element root, ConnexionsConfiguration result) throws UnknownHostException
	{	// port
		String portStr = root.getAttribute(XmlNames.PORT).getValue().trim();
		int port = Integer.parseInt(portStr);
		result.setPort(port);
		
		// id
		String id = root.getAttribute(XmlNames.ID).getValue().trim();
		result.setHostId(id);

		// name
		String name = root.getAttribute(XmlNames.NAME).getValue().trim();
		result.setHostName(name);
		
		// mac addresses
		List<String> macAddresses = new ArrayList<String>();
		String macStr = root.getAttribute(XmlNames.MAC).getValue().trim();
		String[] split = macStr.split(" ");
		for(String s: split)
		{	//int value = Integer.parseInt(s);
			macAddresses.add(s);
		}
		result.setMacAddresses(macAddresses);
	}
}

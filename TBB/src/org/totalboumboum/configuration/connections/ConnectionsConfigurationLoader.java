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
import java.net.InetAddress;
import java.net.UnknownHostException;

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
public class ConnectionsConfigurationLoader
{	
	public static ConnectionsConfiguration loadConnectionsConfiguration() throws SAXException, IOException
	{	ConnectionsConfiguration result = new ConnectionsConfiguration();
		String individualFolder = FilePaths.getConfigurationPath();
		File dataFile = new File(individualFolder+File.separator+FileNames.FILE_CONNECTIONS+FileNames.EXTENSION_XML);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_CONNECTIONS+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadConnectionsElement(root,result);
		return result;
	}

	private static void loadConnectionsElement(Element root, ConnectionsConfiguration result) throws UnknownHostException
	{	// central
		Element centralElement = root.getChild(XmlNames.CENTRAL);
		loadCentralElement(centralElement,result);
		
		// hosting
		Element hostingElement = root.getChild(XmlNames.HOSTING);
		loadHostingElement(hostingElement,result);
	}
	
	private static void loadCentralElement(Element root, ConnectionsConfiguration result) throws UnknownHostException
	{	// ip
		String centralIpStr = root.getAttribute(XmlNames.IP).getValue().trim();
		InetAddress centralIp = InetAddress.getByName(centralIpStr);
		result.setCentralIp(centralIp);
	}

	private static void loadHostingElement(Element root, ConnectionsConfiguration result) throws UnknownHostException
	{	// port
		String portStr = root.getAttribute(XmlNames.PORT).getValue().trim();
		int port = Integer.parseInt(portStr);
		result.setPort(port);
	}
}

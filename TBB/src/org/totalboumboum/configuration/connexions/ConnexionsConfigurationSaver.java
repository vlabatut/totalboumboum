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
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Comment;
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
public class ConnexionsConfigurationSaver
{	
	public static void saveConnexionsConfiguration(ConnexionsConfiguration connexionsConfiguration) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveConnexionsElement(connexionsConfiguration);	
		
		// save file
		String engineFile = FilePaths.getConfigurationPath()+File.separator+FileNames.FILE_CONNECTIONS+FileNames.EXTENSION_XML;
		File dataFile = new File(engineFile);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_CONNECTIONS+FileNames.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveConnexionsElement(ConnexionsConfiguration connexionsConfiguration)
	{	Element result = new Element(XmlNames.CONNECTIONS); 

		// GPL comment
		Comment gplComment = XmlTools.getGplComment();
		result.addContent(gplComment);
	
		// central
		Element centralElement = saveCentralElement(connexionsConfiguration);
		result.addContent(centralElement);
		
		// hosting
		Element hostingElement = saveHostingElement(connexionsConfiguration);
		result.addContent(hostingElement);
		
		return result;
	}
	
	private static Element saveCentralElement(ConnexionsConfiguration connexionsConfiguration)
	{	Element result = new Element(XmlNames.CENTRAL);
		
		// ip
		String ip = connexionsConfiguration.getCentralIp();
		result.setAttribute(XmlNames.IP,ip);
				
		return result;
	}

	private static Element saveHostingElement(ConnexionsConfiguration connexionsConfiguration)
	{	Element result = new Element(XmlNames.HOSTING);
		
		// port
		String port = Integer.toString(connexionsConfiguration.getPort());
		result.setAttribute(XmlNames.PORT,port);
			
		// id
		String id = connexionsConfiguration.getHostId();
		result.setAttribute(XmlNames.ID,id);
			
		// name
		String name = connexionsConfiguration.getHostName();
		result.setAttribute(XmlNames.NAME,name);
			
		// mac addresses
		List<String> macAddresses = connexionsConfiguration.getMacAddresses();
		String macStr = "";
		if(macAddresses.size()>0)
			macStr = macAddresses.get(0);
		for(int i=1;i<macAddresses.size();i++)
			macStr = macStr + " " + macAddresses.get(i);
		result.setAttribute(XmlNames.MAC,macStr);
			
		return result;
	}
}

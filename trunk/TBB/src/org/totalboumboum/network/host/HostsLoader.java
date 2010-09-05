package org.totalboumboum.network.host;

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
import java.util.HashMap;
import java.util.Iterator;
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
public class HostsLoader
{	
	public static HashMap<String,HostInfo> loadHosts() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	
		HashMap<String,HostInfo> result = new HashMap<String,HostInfo>();
		File dataFile = new File(FilePaths.getHostsStatisticsPath()+File.separator+FileNames.FILE_STATISTICS+FileNames.EXTENSION_XML);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_HOSTS+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadHostsElement(root,result);
		return result;
	}

	private static void loadHostsElement(Element root, HashMap<String,HostInfo> result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	@SuppressWarnings("unchecked")
		List<Element> elements = root.getChildren(XmlNames.HOST);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			HostInfo host = loadHostElement(temp);
			result.put(host.getId(),host);
		}
	}
	
	private static HostInfo loadHostElement(Element root) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	HostInfo result = new HostInfo();
	
		// id
    	String id = root.getAttribute(XmlNames.ID).getValue();
    	result.setName(id);
		
		// name
    	String name = root.getAttribute(XmlNames.NAME).getValue();
    	result.setName(name);
		
		// use
    	String useStr = root.getAttribute(XmlNames.USE).getValue();
    	int use = Integer.parseInt(useStr);
    	result.setUses(use);
		
		// last IP
    	String ipStr = root.getAttribute(XmlNames.LAST_IP).getValue();
    	InetAddress ip = InetAddress.getByName(ipStr);
    	result.setLastIp(ip);
    	
		// last port
    	String portStr = root.getAttribute(XmlNames.LAST_PORT).getValue();
    	Integer port = Integer.parseInt(portStr);
    	result.setLastPort(port);
    	
    	// preferred
    	String preferredStr = root.getAttribute(XmlNames.PREFERRED).getValue();
    	Boolean preferred = Boolean.parseBoolean(preferredStr);
    	result.setPreferred(preferred);
    	
    	// central
//    	String centralStr = root.getAttribute(XmlNames.CENTRAL).getValue();
//    	Boolean central = Boolean.parseBoolean(centralStr);
//    	result.setCentral(central);
    	
    	// direct
    	String directStr = root.getAttribute(XmlNames.DIRECT).getValue();
    	Boolean direct = Boolean.parseBoolean(directStr);
    	result.setDirect(direct);
    	
    	return result;
	}
}

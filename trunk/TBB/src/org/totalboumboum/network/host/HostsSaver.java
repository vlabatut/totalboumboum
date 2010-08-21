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
import java.util.HashMap;

import org.jdom.Element;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HostsSaver
{	
	public static void saveHosts(HashMap<String,HostInfo> hosts) throws IOException
	{	// build document
		Element root = saveHostsElement(hosts);
		
		// save file
		File dataFile = new File(FilePaths.getHostsStatisticsPath()+File.separator+FileNames.FILE_STATISTICS+FileNames.EXTENSION_XML);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_HOSTS+FileNames.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveHostsElement(HashMap<String,HostInfo> hosts)
	{	Element result = new Element(XmlNames.HOST);
		
		for(HostInfo host: hosts.values())
		{	Element hostElement = saveHostElement(host);
			result.addContent(hostElement);
		}
		
		return result;
	}
	
	private static Element saveHostElement(HostInfo host)
	{	Element result = new Element(XmlNames.HOST);
		
		// id
		String id = host.getId();
		result.setAttribute(XmlNames.ID,id);
		
		// name
		String name = host.getName();
		result.setAttribute(XmlNames.NAME,name);
		
		// use
		String use = Integer.toString(host.getUses());
		result.setAttribute(XmlNames.USE,use);
		
		// last IP
		String ip = host.getLastIp().getHostAddress();
		result.setAttribute(XmlNames.LAST_IP,ip);
	
		// preferred
		String preferred = Boolean.toString(host.isPreferred());
		result.setAttribute(XmlNames.PREFERRED,preferred);
		
		// central
		String central = Boolean.toString(host.isCentral());
		result.setAttribute(XmlNames.CENTRAL,central);
		
		// direct
		String direct = Boolean.toString(host.isDirect());
		result.setAttribute(XmlNames.DIRECT,direct);
		
		return result;
	}
}

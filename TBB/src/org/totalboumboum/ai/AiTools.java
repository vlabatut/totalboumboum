package org.totalboumboum.ai;

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

import org.totalboumboum.tools.classes.ClassTools;

/**
 * Sets of methods used to handle agents.
 * 
 * @author Vincent Labatut
 */
public class AiTools
{	
	/** Name of the package containing all agents for a given API version */
	public final static String AGENTS_PACKAGE = "ais";
	
	/**
	 * Returns the qualified name of the API package
	 * containing the specified agent class or package.
	 *  
	 * @param qualifiedName
	 * 		Path of a package or class of the agent of interest.
	 * @return
	 * 		Path of the API package containing (indirectly) the specified path.
	 */
	public static String getApiPackage(String qualifiedName)
	{	String result = "";
		
		String temp[] = qualifiedName.split("\\.");
		int i = 0;
		while(!temp[i].equals(AGENTS_PACKAGE))
		{	result = result + temp[i] + ClassTools.CLASS_SEPARATOR;
			i++;
		}
		result = result.substring(0,result.length()-1);
		
		return result;
	}
	
	/**
	 * Returns the qualified name of the pack package
	 * containing the specified agent class or package.
	 *  
	 * @param qualifiedName
	 * 		Path of a package or class of the agent of interest.
	 * @return
	 * 		Path of the pack package containing (indirectly) the specified path.
	 */
	public static String getPackPackage(String qualifiedName)
	{	String result = "";
	
		// get API package path
		String temp[] = qualifiedName.split("\\.");
		int i = 0;
		while(!temp[i].equals(AGENTS_PACKAGE))
		{	result = result + temp[i] + ClassTools.CLASS_SEPARATOR;
			i++;
		}
	
		// add 'ais' package to the path
		result = result + temp[i] + ClassTools.CLASS_SEPARATOR;
		
		return result;
	}
	
	/**
	 * Returns the qualified name of the agent package
	 * containing the specified agent class or package.
	 *  
	 * @param qualifiedName
	 * 		Path of a package or class of the agent of interest.
	 * @param version
	 * 		Whether or not the version package should be included in the path.
	 * @return
	 * 		Path of the agent package containing (possibly indirectly) the specified path.
	 */
	public static String getAgentPackage(String qualifiedName, boolean version)
	{	String result = "";
		
		// get API package path
		String temp[] = qualifiedName.split("\\.");
		int i = 0;
		while(!temp[i].equals(AGENTS_PACKAGE))
		{	result = result + temp[i] + ClassTools.CLASS_SEPARATOR;
			i++;
		}

		// add 'ais' package to the path
		result = result + temp[i] + ClassTools.CLASS_SEPARATOR;
		i++;
		
		// add the agent package to the path
		result = result + temp[i];
		i++;
		
		// possibly add the version package to the path
		if(version && i<temp.length && temp[i].startsWith("v"))
			result = result + ClassTools.CLASS_SEPARATOR + temp[i];
		
		return result;
	}
}

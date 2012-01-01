package org.totalboumboum.engine.container.theme;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

/**
 * 
 * @author Vincent Labatut
 *
 */
public class AbstractTheme
{	public static final String DEFAULT_GROUP = "default";
	public static final String GROUP_SEPARATOR = "-";
	public static final String PROPERTY_SEPARATOR = ":";

	/////////////////////////////////////////////////////////////////
	// VERSION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected String version;
	
	public String getVersion()
	{	return version;
	}
	
	public void setVersion(String version)
	{	this.version = version;
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected String name;
	
	public String getName()
	{	return name;
	}
	
	public void setName(String name)
	{	this.name = name;
	}
	
	/////////////////////////////////////////////////////////////////
	// AUTHOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected String author;
	
	public String getAuthor()
	{	return author;
	}
	
	public void setAuthor(String author)
	{	this.author = author;
	}
	
	/////////////////////////////////////////////////////////////////
	// SOURCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected String source;
	
	public String getSource()
	{	return source;
	}
	
	public void setSource(String source)
	{	this.source = source;
	}
}

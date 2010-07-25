package org.totalboumboum.game.network.host;

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

import java.net.InetAddress;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HostInfo
{	
	/////////////////////////////////////////////////////////////////
	// ID					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String id = null;

	public String getId()
	{	return id;
	}

	public void setId(String id)
	{	this.id = id;
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String name = null;

	public String getName()
	{	return name;
	}

	public void setName(String name)
	{	this.name = name;
	}
	
	/////////////////////////////////////////////////////////////////
	// USE					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int uses = 0;

	public int getUses()
	{	return uses;
	}

	public void setUses(int uses)
	{	this.uses = uses;
	}

	public void incrementUses()
	{	uses++;
	}
	
	/////////////////////////////////////////////////////////////////
	// ADDRESS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private InetAddress lastIp = null;

	public InetAddress getLastIp()
	{	return lastIp;
	}

	public void setLastIp(InetAddress lastIp)
	{	this.lastIp = lastIp;
	}
	
	/////////////////////////////////////////////////////////////////
	// STATE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HostType type = HostType.DIRECT;
	
	public HostType getType()
	{	return type;
	}

	public void setType(HostType type)
	{	this.type = type;
	}
}

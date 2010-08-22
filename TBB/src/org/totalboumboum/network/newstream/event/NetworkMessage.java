package org.totalboumboum.network.newstream.event;

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

import java.io.Serializable;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class NetworkMessage implements Serializable
{	private static final long serialVersionUID = 1L;

	public NetworkMessage(NetworkInfo info)
	{	this.info = info;
	}
	
	public NetworkMessage(Object data)
	{	this.data = data;
	}
	
	public NetworkMessage(NetworkInfo info, Object data)
	{	this.info = info;
		this.data = data;
	}

	/////////////////////////////////////////////////////////////////
	// INFO 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private NetworkInfo info;
	
	public NetworkInfo getInfo()
	{	return info;	
	}
	
	/////////////////////////////////////////////////////////////////
	// DATA 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Object data;
	
	public Object getData()
	{	return data;	
	}
}

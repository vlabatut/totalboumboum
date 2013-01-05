package org.totalboumboum.stream.network.message;

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

import java.io.Serializable;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class NetworkMessage implements Serializable
{	private static final long serialVersionUID = 1L;

	public NetworkMessage(MessageName info)
	{	this.info = info;
	}
	
	public NetworkMessage(MessageName info, Object data)
	{	this.info = info;
		this.data = data;
	}

	/////////////////////////////////////////////////////////////////
	// INFO 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private MessageName info = null;
	
	public MessageName getInfo()
	{	return info;	
	}
	
	/////////////////////////////////////////////////////////////////
	// DATA 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Object data = null;
	
	public Object getData()
	{	return data;	
	}
	
	/////////////////////////////////////////////////////////////////
	// STRING 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result = "(";
		if(info!=null)
			result = result + info.toString();
		result = result + ":";
		if(data!=null)
			result = result + data.toString();
		result = result + ")";
		return result;
	}
}

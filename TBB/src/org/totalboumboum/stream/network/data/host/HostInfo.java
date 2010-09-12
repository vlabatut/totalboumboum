package org.totalboumboum.stream.network.data.host;

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
public class HostInfo implements Serializable
{	private static final long serialVersionUID = 1L;

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
	private Integer uses = null;

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
	private String lastIp = null;

	public String getLastIp()
	{	return lastIp;
	}

	public void setLastIp(String lastIp)
	{	this.lastIp = lastIp;
	}
	
	/////////////////////////////////////////////////////////////////
	// PORT					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Integer lastPort = null;

	public Integer getLastPort()
	{	return lastPort;
	}

	public void setLastPort(Integer lastPort)
	{	this.lastPort = lastPort;
	}
	
	/////////////////////////////////////////////////////////////////
	// TYPE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean central = false;
	private boolean direct = false;
	
	public boolean isCentral()
	{	return central;
	}

	public void setCentral(boolean central)
	{	this.central = central;
	}
	
	public boolean isDirect()
	{	return direct;
	}

	public void setDirect(boolean direct)
	{	this.direct = direct;
	}
	
	/////////////////////////////////////////////////////////////////
	// STATE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HostState state = HostState.UNKOWN;
	
	public HostState getState()
	{	return state;
	}

	public void setState(HostState state)
	{	this.state = state;
	}

	/////////////////////////////////////////////////////////////////
	// PREFERRED			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Boolean preferred = null;
	
	public Boolean isPreferred()
	{	return preferred;
	}

	public void setPreferred(boolean preferred)
	{	this.preferred = preferred;
	}

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public HostInfo copy()
	{	HostInfo result = new HostInfo();
		
		result.central = central;
		result.direct = direct;
		result.id = id;
		result.lastIp = lastIp;
		result.lastPort = lastPort;
		result.name = name;
		result.preferred = preferred;
		result.state = state;
		result.uses = uses;
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// STRING				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result = "";
		if(name!=null)
			result = result + name;
		if(state!=null)
			result = result + ", " + state;
		if(uses!=null)
			result = result + ", " + uses;
		if(lastIp!=null)
			result = result + ", " + lastIp;
		if(lastPort!=null)
			result = result + ", " + lastPort;
		return result;
	}
}

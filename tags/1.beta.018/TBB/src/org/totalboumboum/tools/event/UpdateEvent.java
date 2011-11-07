package org.totalboumboum.tools.event;

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

/**
 * 
 * @author Vincent Labatut
 *
 */
public class UpdateEvent
{
	public UpdateEvent(Object source, EventName eventName)
	{	this(source,null,eventName);
	}
	
	public UpdateEvent(Object source, Object target, EventName eventName)
	{	this.source = source;
		this.target = target;
		this.eventName = eventName;
	}
	
	/////////////////////////////////////////////////////////////////
	// SOURCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Object source;
	
	public Object getSource()
	{	return source;
	}

	/////////////////////////////////////////////////////////////////
	// TARGET			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Object target;
	
	public Object getTarget()
	{	return target;
	}

	/////////////////////////////////////////////////////////////////
	// EVENT NAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private EventName eventName;
	
	public EventName getEventName()
	{	return eventName;
	}
}
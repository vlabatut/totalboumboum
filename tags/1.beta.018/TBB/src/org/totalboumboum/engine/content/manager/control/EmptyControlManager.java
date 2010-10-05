package org.totalboumboum.engine.content.manager.control;

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

import org.totalboumboum.engine.content.feature.event.ControlEvent;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.control.ControlCode;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class EmptyControlManager extends ControlManager
{	
	public EmptyControlManager(Sprite sprite)
	{	super(sprite);
	}	

	/////////////////////////////////////////////////////////////////
	// CODES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public synchronized void putControlCode(ControlCode controlCode)
	{	
		// useless here
	}

	/////////////////////////////////////////////////////////////////
	// EVENTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public synchronized void putControlEvent(ControlEvent controlEvent)
	{	
		// useless here
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void update()
	{	
		// useless here
	}

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public ControlManager copy(Sprite sprite)
	{	ControlManager result = new EmptyControlManager(sprite);
		return result;
	}
}

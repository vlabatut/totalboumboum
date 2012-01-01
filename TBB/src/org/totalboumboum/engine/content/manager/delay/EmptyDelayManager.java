package org.totalboumboum.engine.content.manager.delay;

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

import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class EmptyDelayManager extends DelayManager
{	
	public EmptyDelayManager(Sprite sprite)
	{	super(sprite);
	}

	/////////////////////////////////////////////////////////////////
	// DELAYS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void addDelay(String name, double duration)
	{	
		// useless here
	}
	
	@Override
	public void addIterDelay(String name, int iterations)
	{	
		// useless here
	}
	
	@Override
	public void removeDelay(String name)
	{		
		// useless here
	}
	
	@Override
	public double getDelay(String name)
	{	double result = -1;
		return result;
	}
	
	@Override
	public boolean hasDelay(String name)
	{	return false;		
	}

	/////////////////////////////////////////////////////////////////
	// EXECUTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void update()
	{	
		// useless here
	}

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public DelayManager copy(Sprite sprite)
	{	DelayManager result = new EmptyDelayManager(sprite);
		return result;
	}
}

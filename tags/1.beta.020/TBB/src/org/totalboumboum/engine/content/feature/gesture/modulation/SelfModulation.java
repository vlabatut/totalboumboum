package org.totalboumboum.engine.content.feature.gesture.modulation;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
public class SelfModulation extends AbstractStateModulation
{	private static final long serialVersionUID = 1L;

	public SelfModulation(String name)
	{	super(name);
	}
	
	/////////////////////////////////////////////////////////////////
	// COMPARISON				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public boolean equals(Object modulation)
	{	boolean result = false;
		if(modulation instanceof SelfModulation)
		{	SelfModulation m = (SelfModulation) modulation;
			result = getName().equals(m.getName());
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public SelfModulation copy()
	{	SelfModulation result = new SelfModulation(name);
		result.name = name;
		result.frame = frame;
		result.gestureName = gestureName;
		result.strength = strength;
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// CCACHE					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public SelfModulation cacheCopy(double zoomFactor)
	{	SelfModulation result = new SelfModulation(name);
	
		// name
		result.name = name;
		
		// misc
		result.frame = frame;
		result.gestureName = gestureName;
		result.strength = strength;
		
		return result;
	}
}

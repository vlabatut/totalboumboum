package fr.free.totalboumboum.engine.content.feature.gesture.modulation;

import fr.free.totalboumboum.engine.content.feature.ability.StateAbilityName;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

public class StateModulation extends AbstractModulation
{		
	public StateModulation(StateAbilityName name)
	{	super();
		this.name = name;
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private StateAbilityName name;

	public StateAbilityName getName()
	{	return name;
	}
	public void setName(StateAbilityName name)
	{	this.name = name;
	}	

	/////////////////////////////////////////////////////////////////
	// COMPARISON				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public boolean equals(Object modulation)
	{	boolean result = false;
		if(modulation instanceof StateModulation)
		{	StateModulation m = (StateModulation) modulation;
			result = name==m.getName();
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// STRING					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String toString()
	{	String result = name.toString();
		result = "<"+strength+","+frame+">";
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public StateModulation copy()
	{	StateModulation result = new StateModulation(name);
		result.name = name;
		result.finished = finished;
		result.frame = frame;
		result.gestureName = gestureName;
		result.strength = strength;
		return result;
	}
}

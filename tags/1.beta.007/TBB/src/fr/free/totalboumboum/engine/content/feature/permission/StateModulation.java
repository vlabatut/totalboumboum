package fr.free.totalboumboum.engine.content.feature.permission;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

public class StateModulation extends AbstractPermission
{	
	String name;
	
	public StateModulation(String name)
	{	super();
		this.name = name;
	}
	
	public String getName()
	{	return name;
	}
	public void setName(String name)
	{	this.name = name;
	}	

	public boolean equals(Object modulation)
	{	boolean result = false;
		if(modulation instanceof StateModulation)
		{	StateModulation m = (StateModulation) modulation;
			result = name.equalsIgnoreCase(m.getName());
		}
		return result;
	}
	
	public String toString()
	{	String result = name;
		result = "<"+strength+","+frame+">";
		return result;
	}
	
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
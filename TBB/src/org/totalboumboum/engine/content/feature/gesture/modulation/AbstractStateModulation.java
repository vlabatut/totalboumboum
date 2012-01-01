package org.totalboumboum.engine.content.feature.gesture.modulation;

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

import org.totalboumboum.engine.content.feature.ability.StateAbility;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class AbstractStateModulation extends AbstractModulation
{	private static final long serialVersionUID = 1L;

	public AbstractStateModulation(String name)
	{	super();
		this.name = name;
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected String name;

	public String getName()
	{	return name;
	}
	public void setName(String name)
	{	this.name = name;
	}
	
	/////////////////////////////////////////////////////////////////
	// MODULATE					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * modulates the specified ability. This ability is 
	 * supposed to concern the specified state ability.
	 * The returned ability is a new object, with fields similar to
	 * the specified parameter, excepted for the strength and frame,
	 * whitch have been modulated.
	 */
	public StateAbility modulate(StateAbility ability)
	{	StateAbility result = (StateAbility)ability.copy();
		//if(getName()==ability.getName()) already tested
		{	result.setStrength(getStrength());
			result.setFrame(getFrame());
			result.combine(ability);
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
}

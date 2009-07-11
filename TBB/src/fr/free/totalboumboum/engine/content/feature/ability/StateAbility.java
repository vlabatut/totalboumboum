package fr.free.totalboumboum.engine.content.feature.ability;

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

public class StateAbility extends AbstractAbility
{	
	public StateAbility(String name)
	{	super();
		this.name = name;
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** name of the ability */
	private String name;
	
	public String getName()
	{	return name;
	}
	public void setName(String name)
	{	this.name = name;
	}

	/////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public boolean equals(Object ability)
	{	boolean result = false;
		if(ability instanceof StateAbility)
		{	StateAbility ab = (StateAbility)ability;
			// name
			result = name.equals(ab.getName());
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public AbstractAbility copy()
	{	StateAbility result;
		result = new StateAbility(name);
		result.setFrame(frame);
		result.setMax(max);
		result.setStrength(strength);
		result.setTime(time);
		result.setUses(uses);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String toString()
	{	String result = name+"("+")";
		result = result+"["+strength+"]";
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void finish()
	{	if(!finished)
		{	super.finish();
		}
	}

}

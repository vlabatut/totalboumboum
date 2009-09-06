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

import fr.free.totalboumboum.engine.content.feature.action.ActionName;
import fr.free.totalboumboum.engine.content.feature.action.GeneralAction;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;

public class ActionAbility extends AbstractAbility
{	
	public ActionAbility(GeneralAction action)
	{	super();
		this.action = action;
	}
	
	public ActionAbility(SpecificAction action)
	{	this(action.getGeneralAction());
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private GeneralAction action;
	
	public GeneralAction getAction()
	{	return action;
	}

	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public ActionName getName()
	{	return action.getName();
	}
	
	/////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
/*
	public boolean equals(Object ability)
	{	boolean result = false;
		if(ability instanceof ActionAbility)
		{	ActionAbility ab = (ActionAbility)ability;
			// action
			result = action.equals(ab.getAction());
		}
		return result;
	}
*/	

	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public AbstractAbility copy()
	{	ActionAbility result;
		GeneralAction a = action; //NOTE à copier ? (non)
		result = new ActionAbility(a);
		result.setFrame(frame);
		result.setStrength(strength);
		result.setTime(time);
		result.setUses(uses);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String toString()
	{	String result = action.toString();
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

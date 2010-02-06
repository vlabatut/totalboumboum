package org.totalboumboum.engine.content.feature.ability;

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

import org.totalboumboum.engine.content.feature.action.ActionName;
import org.totalboumboum.engine.content.feature.action.GeneralAction;
import org.totalboumboum.engine.content.feature.action.SpecificAction;

public class ActionAbility extends AbstractAbility
{	private static final long serialVersionUID = 1L;
	
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
	public boolean equals(Object ability)
	{	boolean result = false;
		if(ability instanceof ActionAbility)
		{	ActionAbility ab = (ActionAbility)ability;
			// action
			//result = action.equals(ab.getAction());
			result = action.getName().equals(ab.getAction().getName());
		}
		return result;
	}	

	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public AbstractAbility copy()
	{	ActionAbility result;
		GeneralAction a = action; //NOTE � copier ? (non)
		result = new ActionAbility(a);
		result.setFrame(frame);
		result.setStrength(strength);
		result.setTime(time);
		result.setUses(uses);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// CACHE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public AbstractAbility cacheCopy(double zoomFactor)
	{	ActionAbility result;
	
		// action
		GeneralAction a = action.cacheCopy(zoomFactor);
		result = new ActionAbility(a);
		
		// misc
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

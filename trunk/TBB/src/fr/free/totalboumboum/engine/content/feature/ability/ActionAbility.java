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

import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.gesture.action.ActionName;
import fr.free.totalboumboum.engine.content.feature.gesture.action.GeneralAction;
import fr.free.totalboumboum.engine.content.feature.gesture.action.IncompatibleParameterException;
import fr.free.totalboumboum.engine.content.feature.gesture.action.SpecificAction;

public class ActionAbility extends AbstractAbility
{	
	private GeneralAction action;
	
	public ActionAbility(GeneralAction action, Level level)
	{	super(level);
		this.action = action;
	}
	
	public ActionAbility(SpecificAction specificAction, Level level)
	{	super(level);
//TODO mieux vaudrait p-ê utiliser une fonction de généralisation dans SpecificAction?	
		this.action = specificAction.getName().createGeneralAction();
		try
		{	action.addActor(specificAction.getActor().getRole());
			action.addDirection(specificAction.getDirection());
			action.addContact(specificAction.getContact());
			action.addOrientation(specificAction.getOrientation());
			action.addTilePosition(specificAction.getTilePosition());
			if(specificAction.getTarget()!=null)
				action.addTarget(specificAction.getTarget().getRole());
		}
		catch (IncompatibleParameterException e)
		{	e.printStackTrace();
		}
	}
	
	public GeneralAction getAction()
	{	return action;
	}

	public ActionName getName()
	{	return action.getName();
	}
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
	public AbstractAbility copy()
	{	ActionAbility result;
		GeneralAction a = action; //NOTE à copier ? (non)
		result = new ActionAbility(a,level);
		result.setStrength(strength);
		result.setUses(uses);
		result.setTime(time);
		return result;
	}

	
	public String toString()
	{	String result = action.toString();
		result = result+"["+strength+"]";
		return result;
	}

	public void finish()
	{	if(!finished)
		{	super.finish();
		}
	}
}

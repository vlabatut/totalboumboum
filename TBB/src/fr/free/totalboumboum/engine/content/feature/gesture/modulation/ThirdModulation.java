package fr.free.totalboumboum.engine.content.feature.gesture.modulation;

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

import java.util.Iterator;

import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.gesture.action.GeneralAction;

public class ThirdModulation extends AbstractActionModulation
{
	public ThirdModulation(GeneralAction action)
	{	super(action);
	}
	
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof ThirdModulation)
		{	ThirdModulation perm = (ThirdModulation) o;
			result = action.equals(perm.getAction());
		}
		return result;
	}

	public ThirdModulation copy()
	{	GeneralAction actionCopy = action;
		ThirdModulation result = new ThirdModulation(actionCopy);
		// actor restrictions
		{	Iterator<AbstractAbility> it = actorRestrictions.iterator();
			while(it.hasNext())
			{	AbstractAbility temp = it.next().copy();
				result.addActorRestriction(temp);
			}
		}
		// target restrictions
		{	Iterator<AbstractAbility> it = targetRestrictions.iterator();
			while(it.hasNext())
			{	AbstractAbility temp = it.next().copy();
				result.addTargetRestriction(temp);
			}
		}
		//
		result.finished = finished;
		result.frame = frame;
		result.gestureName = gestureName;
		result.strength = strength;
		return result;
	}
}

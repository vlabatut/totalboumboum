package org.totalboumboum.engine.content.feature.gesture.modulation;

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

import org.totalboumboum.engine.content.feature.action.GeneralAction;
import org.totalboumboum.engine.content.feature.action.SpecificAction;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ActorModulation extends AbstractActionModulation
{	private static final long serialVersionUID = 1L;

	public ActorModulation(GeneralAction action)
	{	super(action);
	}

	public ActorModulation(SpecificAction action)
	{	super(action);
	}

	/////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof ActorModulation)
		{	ActorModulation perm = (ActorModulation) o;
			result = action.equals(perm.getAction());
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
/*	public ActorModulation copy()
	{	GeneralAction actionCopy = action; //NOTE doesn't seem necessary to copy the action (same thing for target and third modulation)
		ActorModulation result = new ActorModulation(actionCopy);
		
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
		
		//misc
		result.finished = finished;
		result.frame = frame;
		result.gestureName = gestureName;
		result.strength = strength;
		
		return result;
	}
*/
/*	public ActorModulation cacheCopy(double zoomFactor)
	{	GeneralAction actionCopy = action.cacheCopy(zoomFactor);
		ActorModulation result = new ActorModulation(actionCopy);
		
		// actor restrictions
		for(AbstractAbility ability: actorRestrictions)
			result.actorRestrictions.add(ability.cacheCopy(zoomFactor));

		// target restrictions
		for(AbstractAbility ability: targetRestrictions)
			result.targetRestrictions.add(ability.cacheCopy(zoomFactor));
		
		//misc
		result.frame = frame;
		result.gestureName = gestureName;
		result.strength = strength;
		
		return result;
	}*/
}

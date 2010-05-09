package fr.free.totalboumboum.engine.content.feature.permission;


import java.util.ArrayList;
import java.util.Iterator;

import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.action.GeneralAction;

public class ActorPermission extends AbstractActionPermission
{
	public ActorPermission(GeneralAction action)
	{	super(action);
	}

	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof ActorPermission)
		{	ActorPermission perm = (ActorPermission) o;
			result = action.equals(perm.getAction());
		}
		return result;
	}
	
	public ActorPermission copy()
	{	GeneralAction actionCopy = action.copy();
		ActorPermission result = new ActorPermission(actionCopy);
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

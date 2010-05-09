package fr.free.totalboumboum.engine.content.feature.permission;


import java.util.Iterator;

import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.action.GeneralAction;

public class ThirdPermission extends AbstractActionPermission
{
	public ThirdPermission(GeneralAction action)
	{	super(action);
	}
	
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof ThirdPermission)
		{	ThirdPermission perm = (ThirdPermission) o;
			result = action.equals(perm.getAction());
		}
		return result;
	}

	public ThirdPermission copy()
	{	GeneralAction actionCopy = action.copy();
		ThirdPermission result = new ThirdPermission(actionCopy);
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

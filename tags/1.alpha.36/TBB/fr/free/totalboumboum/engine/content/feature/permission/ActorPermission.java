package fr.free.totalboumboum.engine.content.feature.permission;


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
}

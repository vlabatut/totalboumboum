package fr.free.totalboumboum.engine.content.feature.permission;


import fr.free.totalboumboum.engine.content.feature.action.GeneralAction;

public class TargetPermission extends AbstractActionPermission
{
	public TargetPermission(GeneralAction action)
	{	super(action);
	}
	
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof TargetPermission)
		{	TargetPermission perm = (TargetPermission) o;
			result = action.equals(perm.getAction());
		}
		return result;
	}
}

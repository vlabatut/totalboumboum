package fr.free.totalboumboum.engine.content.feature.permission;


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
}

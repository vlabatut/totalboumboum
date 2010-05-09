package fr.free.totalboumboum.engine.content.feature.ability;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.action.GeneralAction;
import fr.free.totalboumboum.engine.loop.Loop;

public class ActionAbility extends AbstractAbility
{	
	private GeneralAction action;
	
	public ActionAbility(GeneralAction action, Level level)
	{	super(level);
		this.action = action;
	}
	
	public GeneralAction getAction()
	{	return action;
	}

	public String getName()
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

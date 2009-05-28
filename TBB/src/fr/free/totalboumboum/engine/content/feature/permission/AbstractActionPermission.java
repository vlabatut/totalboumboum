package fr.free.totalboumboum.engine.content.feature.permission;

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

import java.util.ArrayList;
import java.util.Iterator;

import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.ability.ActionAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.action.general.GeneralAction;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public abstract class AbstractActionPermission extends AbstractPermission
{	
	/**
	 * action concernée par cette permission
	 */
	protected GeneralAction action;
	/** 
	 * abilités necessaires à l'acteur pour avoir la permission  
	 */
	protected ArrayList<AbstractAbility> actorRestrictions;
	/** 
	 * abilités necessaires à la cible pour avoir la permission  
	 */
	protected ArrayList<AbstractAbility> targetRestrictions;
	
	public AbstractActionPermission(GeneralAction action)
	{	super();
		this.action = action;	
		this.actorRestrictions = new ArrayList<AbstractAbility>();
		this.targetRestrictions = new ArrayList<AbstractAbility>();
	}
	
	public GeneralAction getAction()
	{	return action;			
	}
	
	public void addActorRestriction(AbstractAbility ability)
	{	actorRestrictions.add(ability);
	}

	public void addTargetRestriction(AbstractAbility ability)
	{	targetRestrictions.add(ability);
	}
	
	public String toString()
	{	String result = action.toString();
		result = result + "<"+strength+","+frame+">";
		// actor restrictions
		result = result + "[actorRestr:";
		Iterator<AbstractAbility> i = actorRestrictions.iterator();
		while(i.hasNext())
		{	AbstractAbility ab = i.next();
			result = result + ab.toString();
		}
		result = result + "]";
		// actor restrictions
		result = result + "[targetRestr:";
		i = targetRestrictions.iterator();
		while(i.hasNext())
		{	AbstractAbility ab = i.next();
			result = result + ab.toString();
		}
		result = result + "]";
		//
		return result;
	}
	
	public boolean isAllowingAction(SpecificAction specificAction)
	{	boolean result;
		// action
		result = action.subsume(specificAction);
		// actor restrictions
		{	Sprite actor = specificAction.getActor();
			Iterator<AbstractAbility> i = actorRestrictions.iterator();
			while(i.hasNext() && result)
			{	AbstractAbility ab = i.next();
				if(ab instanceof ActionAbility)
					ab = actor.getAbility((ActionAbility)ab);
				else
					ab = actor.getAbility((StateAbility)ab);
				result = ab.isActive();
			}
		}
		// target restrictions
		{	Sprite target = specificAction.getTarget();
			Iterator<AbstractAbility> i = targetRestrictions.iterator();
			while(i.hasNext() && result)
			{	AbstractAbility ab = i.next();
				if(ab instanceof ActionAbility)
					ab = target.getAbility((ActionAbility)ab);
				else
					ab = target.getAbility((StateAbility)ab);
				result = ab.isActive();
			}
		}
		//	
		return result;		
	}

	public void finish()
	{	if(!finished)
		{	super.finish();
			// action
			action.finish();
			action = null;
			// actor restrictions
			{	Iterator<AbstractAbility> it = actorRestrictions.iterator();
				while(it.hasNext())
				{	AbstractAbility temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// target restrictions
			{	Iterator<AbstractAbility> it = targetRestrictions.iterator();
				while(it.hasNext())
				{	AbstractAbility temp = it.next();
					temp.finish();
					it.remove();
				}
			}
		}
	}
}


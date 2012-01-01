package org.totalboumboum.engine.content.feature.gesture.modulation;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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
import java.util.List;

import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.ability.ActionAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.action.GeneralAction;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class AbstractActionModulation extends AbstractModulation
{	private static final long serialVersionUID = 1L;
	
	public AbstractActionModulation(GeneralAction action)
	{	super();
		this.action = action;	
	}
	
	public AbstractActionModulation(SpecificAction action)
	{	this(action.getGeneralAction());
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** action concernée par cette permission */
	protected GeneralAction action;
	
	public GeneralAction getAction()
	{	return action;			
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTOR RESTRICTIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** abilités necessaires à l'acteur pour que la modulation soit appliquée */
	protected final List<AbstractAbility> actorRestrictions = new ArrayList<AbstractAbility>();
	
	public void addActorRestriction(AbstractAbility ability)
	{	actorRestrictions.add(ability);
	}

	/////////////////////////////////////////////////////////////////
	// TARGET RESTRICTIONS		/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** abilités necessaires à la cible pour que la modulation soit appliquée */
	protected final List<AbstractAbility> targetRestrictions = new ArrayList<AbstractAbility>();
	
	public void addTargetRestriction(AbstractAbility ability)
	{	targetRestrictions.add(ability);
	}
	
	/////////////////////////////////////////////////////////////////
	// STRING					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
	
	/////////////////////////////////////////////////////////////////
	// MODULATE					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * tests if this modulation is related to the specified action
	 */
	public boolean isConcerningAction(SpecificAction specificAction)
	{	boolean result;
	
		// action
		result = action.subsume(specificAction);
		
		// actor restrictions
		if(result)
		{	Sprite actor = specificAction.getActor();
			Iterator<AbstractAbility> i = actorRestrictions.iterator();
			while(i.hasNext() && result)
			{	AbstractAbility ab = i.next();
				if(ab instanceof ActionAbility)
					ab = actor.getAbility(((ActionAbility)ab).getAction());
				else
					ab = actor.modulateStateAbility(((StateAbility)ab).getName());
				result = ab.isActive();
			}
		}
		
		// target restrictions
		if(result)
		{	Sprite target = specificAction.getTarget();
			Iterator<AbstractAbility> i = targetRestrictions.iterator();
			while(i.hasNext() && result)
			{	AbstractAbility ab = i.next();
				if(ab instanceof ActionAbility)
					ab = target.getAbility(((ActionAbility)ab).getAction());
				else
					ab = target.modulateStateAbility(((StateAbility)ab).getName());
				result = ab.isActive();
			}
		}

		return result;		
	}
	public boolean isConcerningAction(GeneralAction generalAction, List<AbstractAbility> actorProperties, List<AbstractAbility> targetProperties)
	{	boolean result;
		// action
		result = action.subsume(generalAction);
		// actor restrictions
		if(result)
			result = actorProperties.containsAll(actorRestrictions);
		// target restrictions
		if(result)
			result = actorProperties.containsAll(targetRestrictions);
		//	
		return result;		
	}
	
	/**
	 * modulates the specified ability. This ability has
	 * previously been tested with isConcerningAction and is 
	 * supposed to concern an action subsumed by this modulation.
	 * The returned ability is a new object, with fields similar to
	 * the specified parameter, excepted for the strength and frame,
	 * whitch have been modulated.
	 */
	public ActionAbility modulate(ActionAbility ability)
	{	ActionAbility result = (ActionAbility)ability.copy();
		//if(getAction().subsume(ability.getAction())) already tested
		{	result.setStrength(getStrength());
			result.setFrame(getFrame());
			result.combine(ability);
		}
		return result;
	}
}


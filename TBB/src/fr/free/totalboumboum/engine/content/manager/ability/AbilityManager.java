package fr.free.totalboumboum.engine.content.manager.ability;

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

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.ability.ActionAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.gesture.action.AbstractAction;
import fr.free.totalboumboum.engine.content.feature.gesture.action.GeneralAction;
import fr.free.totalboumboum.engine.content.feature.gesture.action.SpecificAction;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public class AbilityManager
{	private Sprite sprite;
	private ArrayList<AbstractAbility> directAbilities;
	private ArrayList<AbstractAbility> currentAbilities;
	
	public AbilityManager(Sprite sprite)
	{	this.sprite = sprite;
		currentAbilities = new ArrayList<AbstractAbility>();
		directAbilities = new ArrayList<AbstractAbility>();
	}

	public Sprite getSprite()
	{	return null;
	}
	public Level getLevel()
	{	return sprite.getLevel();
	}
/*	
	public boolean hasAbility(AbstractAbility ability)
	{	return dynamicAbilities.contains(ability);
	}
*/
	public void addDirectAbilities(ArrayList<AbstractAbility> abilities)
	{	Iterator<AbstractAbility> i = abilities.iterator();
		while(i.hasNext())
			addDirectAbility(i.next());
	}
	public void addDirectAbility(AbstractAbility ability)
	{	AbstractAbility copy = ability.copy();
		if(copy instanceof ActionAbility)
		{	ActionAbility ab = (ActionAbility)copy;
			ab.getAction().setActor(sprite.getClass());
		}
		directAbilities.add(copy);
		currentAbilities.add(copy.copy());
	}
	public StateAbility getAbility(String name)
	{	StateAbility result = null;
		Iterator<AbstractAbility> i = currentAbilities.iterator();
		while(i.hasNext() && result==null)
		{	AbstractAbility ab = i.next();
			if(ab instanceof StateAbility)
			{	StateAbility ablt = (StateAbility)ab;
				if(ablt.getName().equalsIgnoreCase(name))
					result = ablt;
			}
		}
		if(result==null)
			result = new StateAbility(name,getLevel());
		return result;
	}
	public StateAbility getAbility(StateAbility ability)
	{	return getAbility(ability.getName());
	}
	public ActionAbility getAbility(AbstractAction action)
	{	ActionAbility result = null;
		Iterator<AbstractAbility> i = currentAbilities.iterator();
		while(i.hasNext() && result==null)
		{	AbstractAbility ab = i.next();
			if(ab instanceof ActionAbility)
			{	ActionAbility ablt = (ActionAbility)ab;
				if(ablt.getAction().subsume(action))
					result = ablt;
			}
		}
		if(result==null)
			if(action instanceof SpecificAction)
				result = new ActionAbility(((SpecificAction)action).getGeneralAction(),getLevel());
			else // if(action instanceof GeneralAction)
				result = new ActionAbility(((GeneralAction)action),getLevel());
		return result;
	}
	public ActionAbility getAbility(ActionAbility ability)
	{	return getAbility(ability.getAction());
	}

	/**
	 *  current abilities :
	 *  	- directAbilities
	 *  	- itemAbilities
	 */
	public void update()
	{	currentAbilities.clear();
		updateAbilities(directAbilities);
		ArrayList<AbstractAbility> itemAbilities = sprite.getItemAbilities();
		updateAbilities(itemAbilities);
		ArrayList<AbstractAbility> modulationAbilities = sprite.getModulationAbilities();
/*		
if(modulationAbilities.size()>0)
	System.out.println();
*/
		updateAbilities(modulationAbilities);
	}
	public void decrementUse(AbstractAbility ability, int delta)
	{	ArrayList<AbstractAbility> itemAbilities = sprite.getItemAbilities();
		AbstractAbility a = null;
		if(itemAbilities.contains(ability))
			a = itemAbilities.get(itemAbilities.indexOf(ability));
		else if(directAbilities.contains(ability))
			a = directAbilities.get(directAbilities.indexOf(ability));
		if(a!=null)
			a.decrementUse(delta);
	}
	
	private void updateAbilities(ArrayList<AbstractAbility> abilities)
	{	// init time values
		double milliPeriod = Configuration.getEngineConfiguration().getMilliPeriod();
		double timeDelta = milliPeriod*sprite.getSpeedCoeff();
		// 
		Iterator<AbstractAbility> i = abilities.iterator();
		while(i.hasNext())
		{	AbstractAbility temp = i.next();
			// possibly removing the ability
			double time = temp.getTime();
			int uses = temp.getUses();
			if(time==0 || uses==0)
				i.remove();
			else
			{	// updating the ability's time
				if(time>0)
					temp.decrementTime(timeDelta);
				// inserting the ability in the list
				insertAbility(temp);
			}
		}
	}
	
	private void insertAbility(AbstractAbility ability)
	{	int index = currentAbilities.indexOf(ability);
		// the ability is not already in the list
		if(index<0)
		{	AbstractAbility cp = ability.copy(); 
			currentAbilities.add(cp);
		}
		// the ability is already in the list
		else
		{	AbstractAbility temp = currentAbilities.get(index);
			// it is combined with the existing ability
			temp.combine(ability);
		}
	}

	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// current abilities
			{	Iterator<AbstractAbility> it = currentAbilities.iterator();
				while(it.hasNext())
				{	AbstractAbility temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// direct abilities
			{	Iterator<AbstractAbility> it = directAbilities.iterator();
				while(it.hasNext())
				{	AbstractAbility temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// misc
			sprite = null;
		}
	}
}

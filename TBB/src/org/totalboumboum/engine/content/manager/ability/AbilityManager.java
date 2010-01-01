package org.totalboumboum.engine.content.manager.ability;

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

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.ability.ActionAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.action.GeneralAction;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.sprite.Sprite;


public class AbilityManager
{	
	public AbilityManager(Sprite sprite)
	{	this.sprite = sprite;
	}

	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Sprite sprite;
	
	public Sprite getSprite()
	{	return null;
	}

/*	
	public boolean hasAbility(AbstractAbility ability)
	{	return dynamicAbilities.contains(ability);
	}
*/
	/////////////////////////////////////////////////////////////////
	// DIRECT ABILITIES	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** abilities provided before the begining of the round */
	private final ArrayList<AbstractAbility> directAbilities = new ArrayList<AbstractAbility>();
	
	public void addDirectAbilities(ArrayList<AbstractAbility> abilities)
	{	Iterator<AbstractAbility> i = abilities.iterator();
		while(i.hasNext())
			addDirectAbility(i.next());
	}
	
	public void addDirectAbility(AbstractAbility ability)
	{	AbstractAbility copy = ability.copy();
		if(copy instanceof ActionAbility)
		{	ActionAbility ab = (ActionAbility)copy;
			ab.getAction().addActor(sprite.getRole());
		}
		directAbilities.add(copy);	//NOTE pourquoi toutes ces copies? la question est: qu'est-ce qui est modifié exactement dans l'ability. p-e qu'il n'est pas nécessaire de copier l'action
		currentAbilities.add(copy.copy());
	}
	
	public ArrayList<AbstractAbility> getDirectAbilities()
	{	return directAbilities;	
	}
	
	/////////////////////////////////////////////////////////////////
	// CURRENT ABILITIES	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** all abilities (direct and items) */
	private final ArrayList<AbstractAbility> currentAbilities = new ArrayList<AbstractAbility>();
	
	public StateAbility getAbility(String name)
	{	StateAbility result = null;
		Iterator<AbstractAbility> i = currentAbilities.iterator();
		while(i.hasNext() && result==null)
		{	AbstractAbility ab = i.next();
			if(ab instanceof StateAbility)
			{	StateAbility ablt = (StateAbility)ab;
				if(ablt.getName().equals(name))
					result = ablt;
			}
		}
		if(result==null)
			result = new StateAbility(name);
		return result;
	}
	
/*	public StateAbility getAbility(StateAbility ability)
	{	return getAbility(ability.getName());
	}
*/
	public ActionAbility getAbility(SpecificAction action)
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
			result = new ActionAbility(action);
		return result;
	}
	
	public ActionAbility getAbility(GeneralAction action)
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
			result = new ActionAbility(action);
		return result;
	}

	/**
	 *  current abilities :
	 *  	- directAbilities
	 *  	- itemAbilities
	 */
	public void update()
	{	currentAbilities.clear();
		updateAbilities(directAbilities);
		ArrayList<AbstractAbility> itemAbilities = sprite.getItemsAbilities();
		updateAbilities(itemAbilities);
//		ArrayList<AbstractAbility> modulationAbilities = sprite.getModulationAbilities();
/*		
if(modulationAbilities.size()>0)
	System.out.println();
*/
//		updateAbilities(modulationAbilities);
	}
	
	public void modifyUse(AbstractAbility ability, int delta)
	{	ArrayList<AbstractAbility> itemAbilities = sprite.getItemsAbilities();
		AbstractAbility a = null;
		if(itemAbilities.contains(ability))
			a = itemAbilities.get(itemAbilities.indexOf(ability));
		else if(directAbilities.contains(ability))
			a = directAbilities.get(directAbilities.indexOf(ability)); //TODO questionable
		if(a!=null)
			a.modifyUse(delta);
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

	/////////////////////////////////////////////////////////////////
	// FINISHED				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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

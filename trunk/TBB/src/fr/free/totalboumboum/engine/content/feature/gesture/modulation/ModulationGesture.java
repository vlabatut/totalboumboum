package fr.free.totalboumboum.engine.content.feature.gesture.modulation;

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

import fr.free.totalboumboum.engine.content.feature.gesture.action.SpecificAction;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public class ModulationGesture
{	
	private ArrayList<StateModulation> stateModulations;
	private ArrayList<ActorModulation> actorPermissions;
	private ArrayList<TargetModulation> targetPermissions;
	private ArrayList<ThirdModulation> thirdPermissions;
	private String name; //debug
	
	public ModulationGesture()
	{	stateModulations = new ArrayList<StateModulation>();
		actorPermissions = new ArrayList<ActorModulation>();
		targetPermissions = new ArrayList<TargetModulation>();
		thirdPermissions = new ArrayList<ThirdModulation>();
	}

	public void addModulation(StateModulation modulation)
	{	stateModulations.add(modulation);
		modulation.setGestureName(name);
	}
	public void addPermission(AbstractActionModulation permission)
	{	if(permission instanceof ActorModulation)
			actorPermissions.add((ActorModulation)permission);
		else if(permission instanceof TargetModulation)
			targetPermissions.add((TargetModulation)permission);
		else if(permission instanceof ThirdModulation)
			thirdPermissions.add((ThirdModulation)permission);
		permission.setGestureName(name);
	}

	public StateModulation getModulation(StateModulation modulation)
	{	StateModulation result = null;
		int index = stateModulations.indexOf(modulation);
		if(index>=0)
			result = stateModulations.get(index);
		return result;
	}
	public ArrayList<StateModulation> getModulations()
	{	return stateModulations;
	}
	public ActorModulation getActorPermission(SpecificAction action)
	{	ActorModulation result = null;
		Iterator<ActorModulation> i = actorPermissions.iterator();
		while(i.hasNext() && result==null)
		{	ActorModulation perm = i.next();
			if(perm.isAllowingAction(action))
				result = perm;
		}
		return result;
	}
	public TargetModulation getTargetPermission(SpecificAction action)
	{	TargetModulation result = null;
		Iterator<TargetModulation> i = targetPermissions.iterator();
		while(i.hasNext() && result==null)
		{	TargetModulation perm = i.next();
			if(perm.isAllowingAction(action))
				result = perm;
		}
		return result;
	}
	public ThirdModulation getThirdPermission(SpecificAction action)
	{	ThirdModulation result = null;
		Iterator<ThirdModulation> i = thirdPermissions.iterator();
		while(i.hasNext() && result==null)
		{	ThirdModulation perm = i.next();
			if(perm.isAllowingAction(action))
				result = perm;
		}
		return result;
	}
	
	public String getName()
	{	return name;
	}
	public void setName(String name)
	{	this.name = name;
	}

	public void setSprite(Sprite sprite)
	{	Class<?> c = sprite.getClass();
		// actor
		Iterator<ActorModulation> ia = actorPermissions.iterator();
		while(ia.hasNext())
		{	ActorModulation temp = ia.next();
			temp.getAction().setActor(c);
		}
		// target
		Iterator<TargetModulation> it = targetPermissions.iterator();
		while(it.hasNext())
		{	TargetModulation temp = it.next();
			temp.getAction().setTarget(c);
		}
	}	

	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// actor permissions
			{	Iterator<ActorModulation> it = actorPermissions.iterator();
				while(it.hasNext())
				{	ActorModulation temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// state permissions
			{	Iterator<StateModulation> it = stateModulations.iterator();
				while(it.hasNext())
				{	StateModulation temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// target permissions
			{	Iterator<TargetModulation> it = targetPermissions.iterator();
				while(it.hasNext())
				{	TargetModulation temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// third permissions
			{	Iterator<ThirdModulation> it = thirdPermissions.iterator();
				while(it.hasNext())
				{	ThirdModulation temp = it.next();
					temp.finish();
					it.remove();
				}
			}
		}
	}
	
	public ModulationGesture copy()
	{	ModulationGesture result = new ModulationGesture();
		// actor permissions
		{	Iterator<ActorModulation> it = actorPermissions.iterator();
			while(it.hasNext())
			{	ActorModulation temp = it.next().copy();
				result.addPermission(temp);
			}
		}
		// state permissions
		{	Iterator<StateModulation> it = stateModulations.iterator();
			while(it.hasNext())
			{	StateModulation temp = it.next().copy();
				result.addModulation(temp);
			}
		}
		// target permissions
		{	Iterator<TargetModulation> it = targetPermissions.iterator();
			while(it.hasNext())
			{	TargetModulation temp = it.next().copy();
				result.addPermission(temp);
			}
		}
		// third permissions
		{	Iterator<ThirdModulation> it = thirdPermissions.iterator();
			while(it.hasNext())
			{	ThirdModulation temp = it.next().copy();
				result.addPermission(temp);
			}
		}
		//
		result.finished = finished;
		result.name = name;
		return result;
	}
}

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.engine.content.feature.gesture.action.SpecificAction;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public class ModulationPack
{	private HashMap<String,ModulationGesture> gestures;
	
	public ModulationPack()
	{	gestures = new HashMap<String,ModulationGesture>();
	}

	public StateModulation getModulation(String gestureName, StateModulation permission)
	{	ModulationGesture gesture = gestures.get(gestureName);
		StateModulation result = null;
		if(gesture!=null)
			result = gesture.getModulation(permission);
		return result;
	}
	public ArrayList<StateModulation> getModulations(String gestureName)
	{	ModulationGesture gesture = gestures.get(gestureName);
		ArrayList<StateModulation> result = new ArrayList<StateModulation>();
		if(gesture!=null)
			result = gesture.getModulations();
		return result;
	}
	public ActorModulation getActorPermission(String gestureName, SpecificAction action)
	{	ModulationGesture gesture = gestures.get(gestureName);
		ActorModulation result = null;
		if(gesture!=null)
			result = gesture.getActorPermission(action);
		return result;
	}
	public TargetModulation getTargetPermission(String gestureName, SpecificAction action)
	{	ModulationGesture gesture = gestures.get(gestureName);
		TargetModulation result = null;
		if(gesture!=null)
			result = gesture.getTargetPermission(action);
		return result;
	}
	public ThirdModulation getThirdPermission(String gestureName, SpecificAction action)
	{	ModulationGesture gesture = gestures.get(gestureName);
		ThirdModulation result = null;
		if(gesture!=null)
			result = gesture.getThirdPermission(action);
		return result;
	}
	
	public void addPermissionGesture(ModulationGesture permissionGesture)
	{	gestures.put(permissionGesture.getName(), permissionGesture);
	}
	public void setPermissionGesture(String name,ModulationGesture permissionGesture)
	{	gestures.put(name,permissionGesture);
	}
	
	public void setSprite(Sprite sprite)
	{	Iterator<Entry<String,ModulationGesture>> i = gestures.entrySet().iterator();
		while(i.hasNext())
		{	Entry<String,ModulationGesture> temp = i.next();
			ModulationGesture perm = temp.getValue();
			perm.setSprite(sprite);
		}
	}

	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// gestures
			{	Iterator<Entry<String,ModulationGesture>> it = gestures.entrySet().iterator();
				while(it.hasNext())
				{	Entry<String,ModulationGesture> t = it.next();
					ModulationGesture temp = t.getValue();
					temp.finish();
					it.remove();
				}
			}
		}
	}
	
	public ModulationPack copy()
	{	ModulationPack result = new ModulationPack();
		Iterator<Entry<String,ModulationGesture>> it = gestures.entrySet().iterator();
		while(it.hasNext())
		{	Entry<String,ModulationGesture> t = it.next();
			ModulationGesture value = t.getValue().copy();
			String key = t.getKey();
			result.setPermissionGesture(key,value);
		}
		result.finished = finished;
		return result;		
	}
}

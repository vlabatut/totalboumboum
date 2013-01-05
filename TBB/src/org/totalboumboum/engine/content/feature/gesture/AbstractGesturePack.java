package org.totalboumboum.engine.content.feature.gesture;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import java.util.HashMap;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class AbstractGesturePack<T extends AbstractGesture<?,?>>
{		
	/////////////////////////////////////////////////////////////////
	// SPRITE NAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected String spriteName;
	
	public String getSpriteName()
	{	return spriteName;
	}
	
	public void setSpriteName(String spriteName)
	{	this.spriteName = spriteName;
	}

	/////////////////////////////////////////////////////////////////
	// GESTURES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected final HashMap<GestureName,T> gestures = new HashMap<GestureName, T>();
	
	public T getGesture(GestureName name)
	{	T result = gestures.get(name);
		//NOTE créer le gesture s'il est manquant?
		return result;
	}

	public void addGesture(T gesture, GestureName name)
	{	gestures.put(name,gesture);
	}
	
	public boolean containsGesture(GestureName name)
	{	return gestures.containsKey(name);		
	}
}

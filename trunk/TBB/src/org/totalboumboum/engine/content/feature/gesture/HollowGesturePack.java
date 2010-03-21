package org.totalboumboum.engine.content.feature.gesture;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.totalboumboum.configuration.profile.PredefinedColor;

public class HollowGesturePack
{	
	/////////////////////////////////////////////////////////////////
	// COLORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PredefinedColor color;

	public PredefinedColor getColor()
	{	return color;
	}
	
	public void setColor(PredefinedColor color)
	{	this.color = color;
	}

	/////////////////////////////////////////////////////////////////
	// GESTURES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final HashMap<GestureName,Gesture> gestures = new HashMap<GestureName, Gesture>();
	
	public Gesture getGesture(GestureName name)
	{	Gesture result = gestures.get(name);
		//NOTE créer le gesture s'il est manquant?
		return result;
	}

	public void addGesture(Gesture gesture, GestureName name)
	{	gestures.put(name,gesture);
	}
	
	public boolean containsGesture(GestureName name)
	{	return gestures.containsKey(name);		
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public HollowGesturePack surfaceCopy()
	{	HollowGesturePack result = new HollowGesturePack();
		// gestures
		for(Entry<GestureName,Gesture> e: gestures.entrySet())
		{	Gesture cp = e.getValue().surfaceCopy();
			GestureName nm = e.getKey();
			result.addGesture(cp,nm);
		}
		
		// misc
		result.scale = scale;
		result.color = color;
		result.spriteName = spriteName;
		return result;
	}
	
	public HollowGesturePack deepCopy(double zoomFactor, PredefinedColor color) throws IOException
	{	HollowGesturePack result = new HollowGesturePack();
		
		// gestures
		for(Entry<GestureName,Gesture> e: gestures.entrySet())
		{	Gesture cp = e.getValue().deepCopy(zoomFactor,scale,color);
			GestureName nm = e.getKey();
			result.addGesture(cp,nm);
		}
		
		// misc
		result.scale = scale;
		result.color = color;
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// gestures
			for(Entry<GestureName,Gesture> e: gestures.entrySet())
			{	Gesture temp = e.getValue();
				temp.finish();
			}
			gestures.clear();
		}
	}
}

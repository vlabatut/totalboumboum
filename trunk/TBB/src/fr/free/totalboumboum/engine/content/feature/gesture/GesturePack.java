package fr.free.totalboumboum.engine.content.feature.gesture;

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

import java.util.HashMap;
import java.util.Map.Entry;

import fr.free.totalboumboum.configuration.profile.PredefinedColor;

public class GesturePack
{	
	public GesturePack()
	{	// init the gesture pack with all possible gestures
		for(GestureName name: GestureName.values())
		{	Gesture gesture = new Gesture();
			gesture.setName(name);
			addGesture(gesture);			
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private double scale;
	
	public double getScale()
	{	return scale;
	}
	
	public void setScale(double scale)
	{	this.scale = scale;
	}

	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
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
		return result;
	}

	public void addGesture(Gesture gesture)
	{	gestures.put(gesture.getName(),gesture);
	}
	
	public boolean containsGesture(GestureName name)
	{	return gestures.containsKey(name);		
	}

	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public GesturePack copy()
	{	GesturePack result = new GesturePack();
		// gestures
		for(Gesture g: gestures.values())
		{	Gesture cp = g.copy();
			result.addGesture(cp);
		}
		// misc
		
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

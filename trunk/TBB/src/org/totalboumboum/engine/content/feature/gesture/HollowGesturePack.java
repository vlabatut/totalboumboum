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
import java.io.Serializable;
import java.util.Map.Entry;

import org.totalboumboum.configuration.profile.PredefinedColor;

public class HollowGesturePack extends AbstractGesturePack<HollowGesture> implements Serializable
{	private static final long serialVersionUID = 1L;

	public HollowGesturePack()
	{	// init the gesture pack with all possible gestures
		for(GestureName name: GestureName.values())
		{	HollowGesture gesture = new HollowGesture();
			gesture.setName(name);
			addGesture(gesture,name);			
		}
	}

	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * used to clone an abstract HollowFactory to be completed
	 * by additional data. All the content is keeped as is (same objects)
	 * but the containers are cloned, since their own content may be changed
	 * through inheritance.
	 */
	public HollowGesturePack copy()
	{	HollowGesturePack result = new HollowGesturePack();
		// gestures
		for(Entry<GestureName,HollowGesture> e: gestures.entrySet())
		{	HollowGesture cp = e.getValue().copy();
			GestureName nm = e.getKey();
			result.addGesture(cp,nm);
		}
		
		// misc
		result.spriteName = spriteName;
		return result;
	}
	
	/**
	 * used when generating an actual Factory from a HollowFactory.
	 * Images names are replaced by the actual images, scalable stuff
	 * is scaled, etc.
	 */
	public GesturePack fill(double zoomFactor, double scale, PredefinedColor color) throws IOException
	{	GesturePack result = new GesturePack();
		
		// gestures
		for(Entry<GestureName,HollowGesture> e: gestures.entrySet())
		{	Gesture cp = e.getValue().fill(zoomFactor,scale,color);
			GestureName nm = e.getKey();
			result.addGesture(cp,nm);
		}
		
		// misc
		result.setScale(scale);
		result.setColor(color);
		
		return result;
	}
}

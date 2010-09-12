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

import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class GesturePack extends AbstractGesturePack<Gesture>
{	
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
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * used when generating a sprite from a factory: the content is the
	 * same, only the containers are copied.
	 * useless for now, since nothing is modified in-game.
	 */
/*	public GesturePack copy()
	{	GesturePack result = new GesturePack();
		
		// gestures
		for(Entry<GestureName,Gesture> e: gestures.entrySet())
		{	Gesture cp = e.getValue().copy();
			GestureName nm = e.getKey();
			result.addGesture(cp,nm);
		}
		
		// misc
		result.scale = scale;
		result.color = color;
		result.spriteName = spriteName;
		return result;
	}
*/	
}

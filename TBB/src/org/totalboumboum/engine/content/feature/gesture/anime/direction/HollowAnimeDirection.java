package org.totalboumboum.engine.content.feature.gesture.anime.direction;

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
import java.util.ArrayList;
import java.util.Iterator;

import org.totalboumboum.configuration.profile.PredefinedColor;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.feature.gesture.anime.step.AbstractAnimeStep;
import org.totalboumboum.engine.content.feature.gesture.anime.step.AnimeStep;

public class HollowAnimeDirection extends AbstractAnimeDirection<AbstractAnimeStep>
{	private static final long serialVersionUID = 1L;

	public HollowAnimeDirection()
	{	gestureName= null;
		steps = new ArrayList<AnimeStep>(0);
		repeat = false;
		proportional = false;
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public HollowAnimeDirection surfaceCopy()
	{	HollowAnimeDirection result = new HollowAnimeDirection();
		
		// steps
		Iterator<AnimeStep> i = getIterator();
		while(i.hasNext())
		{	AnimeStep copyStep = i.next().surfaceCopy(); 
			result.add(copyStep);		
		}
		
		// various fields
		result.setGestureName(gestureName);
		result.setDirection(direction);
		result.setRepeat(repeat);
		result.setProportional(proportional);
		result.setBoundHeight(boundHeight);

		return result;
	}

	public HollowAnimeDirection deepCopy(double zoom, PredefinedColor color) throws IOException
	{	HollowAnimeDirection result = new HollowAnimeDirection();
		
		// steps
		Iterator<AnimeStep> i = getIterator();
		while(i.hasNext())
		{	AnimeStep copyStep = i.next().deepCopy(zoom,color); 
			result.add(copyStep);		
		}
		
		// various fields
		result.gestureName = gestureName;
		result.direction = direction;
		result.repeat = repeat;
		result.proportional = proportional;
		result.boundHeight = boundHeight*zoom;

		return result;
	}

	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public void finish()
	{	if(!finished)
		{	super.finish();
		}
	}
}

package org.totalboumboum.engine.content.feature.gesture.anime.direction;

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

import java.io.IOException;

import org.totalboumboum.engine.content.feature.gesture.anime.step.AnimeStep;
import org.totalboumboum.engine.content.feature.gesture.anime.step.HollowAnimeStep;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HollowAnimeDirection extends AbstractAnimeDirection<HollowAnimeStep>
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/**
	 * used to clone an abstract HollowFactory to be completed
	 * by additional data (useless for now, might be usefull later) 
	 */
/*	public HollowAnimeDirection copy()
	{	HollowAnimeDirection result = new HollowAnimeDirection();
		
		// steps
		for(HollowAnimeStep as: steps)
		{	HollowAnimeStep copyStep = as.copy(); 
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
*/
	/**
	 * used when generating an actual Factory from a HollowFactory
	 */
	public AnimeDirection fill(double zoom, PredefinedColor color) throws IOException
	{	AnimeDirection result = new AnimeDirection();
		
		// steps
		for(HollowAnimeStep as: steps)
		{	AnimeStep copyStep = as.fill(zoom,color); 
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
}

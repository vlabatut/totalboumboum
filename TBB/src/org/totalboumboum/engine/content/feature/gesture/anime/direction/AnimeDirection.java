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

import java.io.Serializable;

import org.totalboumboum.engine.content.feature.gesture.anime.step.AnimeStep;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class AnimeDirection extends AbstractAnimeDirection<AnimeStep> implements Serializable
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/**
	 * used when generating a sprite from a factory: images are not cloned.
	 * but for now, animes are just re-used because they are not modifiable
	 * (unlike some other sprite parts)
	 */
/*	public AnimeDirection copy()
	{	AnimeDirection result = new AnimeDirection();
		
		// steps
		Iterator<AnimeStep> i = getIterator();
		while(i.hasNext())
		{	AnimeStep copyStep = i.next().copy(); 
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
}

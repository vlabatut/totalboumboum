package org.totalboumboum.engine.content.feature.gesture.anime.step;

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

import org.totalboumboum.engine.content.feature.gesture.anime.stepimage.StepImage;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class AnimeStep extends AbstractAnimeStep<StepImage>
{	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * used when generating a sprite from a factory: images are not cloned.
	 * but for now, animes are just re-used because they are not modifiable
	 * (unlike some other sprite parts)
	 */
/*	public AnimeStep copy()
	{	AnimeStep result = new AnimeStep();
		
		// images
		result.images.addAll(images);
		// shifts
		result.xShifts.addAll(xShifts);
		result.yShifts.addAll(yShifts);
		
		// duration
		result.setDuration(duration);	
		
		// shadow
		result.setShadow(shadow,shadowXShift,shadowYShift);
		
		// bound
		result.setBoundYShift(boundYShift);

		return result;
	}
*/
}

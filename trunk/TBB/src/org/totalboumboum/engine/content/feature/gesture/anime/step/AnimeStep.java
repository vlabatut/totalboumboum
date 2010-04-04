package org.totalboumboum.engine.content.feature.gesture.anime.step;

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

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class AnimeStep extends AbstractAnimeStep
{	
	/////////////////////////////////////////////////////////////////
	// IMAGE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<BufferedImage> images = new ArrayList<BufferedImage>();

	public List<BufferedImage> getImages()
	{	return images;
	}
	
	public void addImage(BufferedImage image, double xShift, double yShift)
	{	images.add(image);
		xShifts.add(xShift);
		yShifts.add(yShift);
	}
	
	/////////////////////////////////////////////////////////////////
	// SHADOW			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private BufferedImage shadow = null;

	public boolean hasShadow()
	{	return shadow != null;
	}
	
	public void setShadow(BufferedImage shadow, double shadowXShift, double shadowYShift)
	{	this.shadow = shadow;
		this.shadowXShift = shadowXShift;
		this.shadowYShift = shadowYShift;
	}
	
	public BufferedImage getShadow()
	{	return shadow;
	}

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

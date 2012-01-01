package org.totalboumboum.engine.content.feature.gesture.anime.step;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.engine.content.feature.ImageShift;
import org.totalboumboum.engine.content.feature.gesture.anime.stepimage.AbstractStepImage;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class AbstractAnimeStep<T extends AbstractStepImage>
{	
	public AbstractAnimeStep()
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// DURATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected long duration = 0;

	public long getDuration()
	{	return duration;
	}
	
	public void setDuration(long duration)
	{	this.duration = duration;
	}
	
	/////////////////////////////////////////////////////////////////
	// IMAGES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected List<T> images = new ArrayList<T>();

	public List<T> getImages()
	{	return images;
	}
	
	public void addImage(T image)
	{	images.add(image);
	}
	
	/////////////////////////////////////////////////////////////////
	// SHADOW 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected T shadow = null;

	public T getShadow()
	{	return shadow;
	}
	
	public void setShadow(T shadow)
	{	this.shadow = shadow;		
	}
	
	public boolean hasShadow()
	{	return shadow == null;	
	}

	/////////////////////////////////////////////////////////////////
	// BOUND SHIFTS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ImageShift boundYShift = ImageShift.DOWN;

	public ImageShift getBoundYShift()
	{	return boundYShift;
	}
	
	public void setBoundYShift(ImageShift boundYShift)
	{	this.boundYShift = boundYShift;
	}
}

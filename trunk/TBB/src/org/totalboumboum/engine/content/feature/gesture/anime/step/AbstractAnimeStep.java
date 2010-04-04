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

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.engine.content.feature.ImageShift;

public abstract class AbstractAnimeStep
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
	// SHIFTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected List<Double> xShifts = new ArrayList<Double>();
	protected List<Double> yShifts = new ArrayList<Double>();

	public List<Double> getXShifts()
	{	return xShifts;
	}
	
	public List<Double> getYShifts()
	{	return yShifts;
	}

	/////////////////////////////////////////////////////////////////
	// SHADOW SHIFTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected double shadowXShift = 0;
	protected double shadowYShift = 0;

	public double getShadowXShift()
	{	return shadowXShift;
	}
	public void setShadowXShift(double shadowXShift)
	{	this.shadowXShift = shadowXShift;
	}

	public double getShadowYShift()
	{	return shadowYShift;
	}
	public void setShadowYShift(double shadowYShift)
	{	this.shadowYShift = shadowYShift;
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

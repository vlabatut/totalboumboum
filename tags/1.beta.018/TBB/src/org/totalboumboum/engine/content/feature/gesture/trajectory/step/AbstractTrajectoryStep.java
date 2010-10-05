package org.totalboumboum.engine.content.feature.gesture.trajectory.step;

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

import org.totalboumboum.engine.content.feature.ImageShift;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class AbstractTrajectoryStep
{	
	/////////////////////////////////////////////////////////////////
	// LOCATION SHIFTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected double xShift = 0;
	protected double yShift = 0;
	protected double zShift = 0;

	public double getXShift()
	{	return xShift;
	}
	public void setXShift(double shift)
	{	xShift = shift;
	}

	public double getYShift()
	{	return yShift;
	}
	public void setYShift(double shift)
	{	yShift = shift;
	}

	public double getZShift(Sprite boundToSprite)
	{	double result = zShift;
		if(boundToSprite!=null)
			result = result + boundZShift.getValue(boundToSprite);
		return result;
	}
	public void setZShift(double shift)
	{	zShift = shift;
	}

	/////////////////////////////////////////////////////////////////
	// IMAGE SHIFT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ImageShift boundZShift = ImageShift.DOWN;

	public ImageShift getBoundZShift()
	{	return boundZShift;
	}
	
	public void setBoundZShift(ImageShift boundZShift)
	{	this.boundZShift = boundZShift;
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
}

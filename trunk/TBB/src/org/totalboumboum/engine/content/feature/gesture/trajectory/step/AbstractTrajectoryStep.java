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

import java.io.Serializable;

import org.totalboumboum.engine.content.feature.ImageShift;
import org.totalboumboum.engine.content.sprite.Sprite;


public class AbstractTrajectoryStep implements Serializable
{	private static final long serialVersionUID = 1L;

	private long duration;
	
	public AbstractTrajectoryStep()
	{	xShift = 0;
		yShift = 0;
		zShift = 0;
		duration = 0;
		boundZShift = ImageShift.DOWN;
	}	
	
	/////////////////////////////////////////////////////////////////
	// LOCATION SHIFTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private double xShift;
	private double yShift;
	private double zShift;

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
	private ImageShift boundZShift;

	public ImageShift getBoundZShift()
	{	return boundZShift;
	}
	
	public void setBoundZShift(ImageShift boundZShift)
	{	this.boundZShift = boundZShift;
	}
	
	/////////////////////////////////////////////////////////////////
	// DURATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public long getDuration()
	{	return duration;
	}
	public void setDuration(long duration)
	{	this.duration = duration;
	}	
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// misc
			boundZShift = null;
		}
	}

	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/*	
	public TrajectoryStep surfaceCopy()
	{	TrajectoryStep result = new TrajectoryStep();
		result.boundZShift = boundZShift;
		result.duration = duration;
		result.finished = finished;
		result.xShift = xShift;
		result.yShift = yShift;
		result.zShift = zShift;
		return result;
	}
*/	

	public AbstractTrajectoryStep deepCopy(double zoomFactor)
	{	AbstractTrajectoryStep result = new AbstractTrajectoryStep();
		
		// location shifts
		result.xShift = xShift*zoomFactor;
		result.yShift = yShift*zoomFactor;
		result.zShift = zShift*zoomFactor;

		result.boundZShift = boundZShift;
		result.duration = duration;
		
		result.finished = finished;

		return result;
	}
}

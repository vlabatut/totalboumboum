package org.totalboumboum.engine.content.feature.gesture.trajectory.direction;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.feature.gesture.trajectory.step.AbstractTrajectoryStep;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class AbstractTrajectoryDirection<T extends AbstractTrajectoryStep>
{	
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected GestureName gestureName = null; //debug

	public String getName()
	{	return gestureName+","+direction;
	}
	public void setGestureName(GestureName gestureName)
	{	this.gestureName = gestureName;
	}
	
	/////////////////////////////////////////////////////////////////
	// DIRECTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Direction direction = Direction.NONE; //debug

	public void setDirection(Direction direction)
	{	this.direction = direction;
	}
	public Direction getDirection()
	{	return direction;
	}
	
	/////////////////////////////////////////////////////////////////
	// STEPS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected List<T> steps = new ArrayList<T>();

	public Iterator<T> getIterator()
	{	return steps.iterator();		
	}
	
	public void add(T trajectoryStep)
	{	steps.add(trajectoryStep);		
	}
	
	public void addAll(List<T> trajectorySteps)
	{	steps.addAll(trajectorySteps);		
	}

	/////////////////////////////////////////////////////////////////
	// DURATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Compute the total duration of the animation.
	 * The result is 0 if there is no time limit. 
	 * @return	the duration of the animation
	 */
	public long getTotalDuration()
	{	long result = 0;
		Iterator<T> i = steps.iterator();
		while(i.hasNext())
			result = result + i.next().getDuration();
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// SHIFTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public double getTotalXShift()
	{	double result = 0;
		Iterator<T> i = steps.iterator();
		while(i.hasNext())
			result = result + i.next().getXShift();
		return result;
	}
	
	public double getTotalYShift()
	{	double result = 0;
		Iterator<T> i = steps.iterator();
		while(i.hasNext())
			result = result + i.next().getYShift();
		return result;
	}
	
	public double getTotalZShift(Sprite boundToSprite)
	{	double result = 0;
		Iterator<T> i = steps.iterator();
		while(i.hasNext())
			result = result + i.next().getZShift(boundToSprite);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// REPEAT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean repeat = false;

	public boolean getRepeat()
	{	return repeat;
	}
	
	public void setRepeat(boolean repeat)
	{	this.repeat = repeat;
	}

	/////////////////////////////////////////////////////////////////
	// INTERACTIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected double xInteraction = 0;
	protected double yInteraction = 0;

	public double getXInteraction()
	{	return xInteraction;
	}
	public void setXInteraction(double interaction)
	{	xInteraction = interaction;
	}

	public double getYInteraction()
	{	return yInteraction;
	}
	public void setYInteraction(double interaction)
	{	yInteraction = interaction;
	}

	/////////////////////////////////////////////////////////////////
	// FORCE POSITION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean forceXPosition = false;
	protected boolean forceYPosition = false;
	protected boolean forceZPosition = false;

	public boolean isForceXPosition()
	{	return forceXPosition;
	}
	public void setForceXPosition(boolean forceXPosition)
	{	this.forceXPosition = forceXPosition;
	}

	public boolean isForceYPosition()
	{	return forceYPosition;
	}
	public void setForceYPosition(boolean forceYPosition)
	{	this.forceYPosition = forceYPosition;
	}

	public boolean isForceZPosition()
	{	return forceZPosition;
	}
	public void setForceZPosition(boolean forceZPosition)
	{	this.forceZPosition = forceZPosition;
	}

	/////////////////////////////////////////////////////////////////
	// FORCED POSITION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected double forcedXPosition = 0;
	protected double forcedYPosition = 0;
	protected double forcedZPosition = 0;
	protected long forcedPositionTime = 0;

	public double getForcedXPosition()
	{	return forcedXPosition;
	}
	public void setForcedXPosition(double position)
	{	forcedXPosition = position;
	}

	public double getForcedYPosition()
	{	return forcedYPosition;
	}
	public void setForcedYPosition(double position)
	{	forcedYPosition = position;
	}

	public double getForcedZPosition()
	{	return forcedZPosition;
	}
	public void setForcedZPosition(double position)
	{	forcedZPosition = position;
	}

	public long getForcedPositionTime()
	{	return forcedPositionTime;
	}
	public void setForcedPositionTime(long forcedTime)
	{	this.forcedPositionTime = forcedTime;
	}

	/////////////////////////////////////////////////////////////////
	// PROPORTIONAL		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean proportional = false;

	public boolean getProportional()
	{	return proportional;
	}
	public void setProportional(boolean proportional)
	{	this.proportional = proportional;
	}

	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String toString()
	{	return getName();
	}	
}

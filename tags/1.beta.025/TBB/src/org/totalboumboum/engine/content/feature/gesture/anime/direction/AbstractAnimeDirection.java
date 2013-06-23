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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.feature.gesture.anime.step.AbstractAnimeStep;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class AbstractAnimeDirection<T extends AbstractAnimeStep<?>>
{	private static final long serialVersionUID = 1L;

	public AbstractAnimeDirection()
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// HEIGHT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected double boundHeight = 0; 

	public double getBoundHeight()
	{	return boundHeight;
	}
	public void setBoundHeight(double boundHeight)
	{	this.boundHeight = boundHeight;
	}

	/////////////////////////////////////////////////////////////////
	// STEPS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected List<T> steps = new ArrayList<T>(0);

	public Iterator<T> getIterator()
	{	return steps.iterator();		
	}
	
	public void add(T as)
	{	steps.add(as);		
	}
	
	public void addAll(List<T> l)
	{	steps.addAll(l);		
	}
	
	public int getLength()
	{	return steps.size();
	}

	/////////////////////////////////////////////////////////////////
	// NAME			/////////////////////////////////////////////
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
	protected Direction direction = null; //debug

	public void setDirection(Direction direction)
	{	this.direction = direction;
	}
	
	public Direction getDirection()
	{	return direction;
	}
	
	/////////////////////////////////////////////////////////////////
	// DURATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Computes the total duration of the animation.
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
	// PROPORTIONNAL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean proportional = false;

	public boolean getProportional()
	{	return proportional;
	}
	
	public void setProportional(boolean proportional)
	{	this.proportional = proportional;
	}
}

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.totalboumboum.configuration.profile.PredefinedColor;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.feature.gesture.anime.step.AbstractAnimeStep;
import org.totalboumboum.engine.content.feature.gesture.anime.step.AnimeStep;

public class AbstractAnimeDirection<T extends AbstractAnimeStep>
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
	private ArrayList<T> steps = new ArrayList<T>(0);

	public Iterator<T> getIterator()
	{	return steps.iterator();		
	}
	
	public void add(T as)
	{	steps.add(as);		
	}
	
	public void addAll(ArrayList<T> l)
	{	steps.addAll(l);		
	}
	
	public int getLength()
	{	return steps.size();
	}

	/////////////////////////////////////////////////////////////////
	// NAME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private GestureName gestureName = null; //debug

	public String getName()
	{	return gestureName+","+direction;
	}
	
	public void setGestureName(GestureName gestureName)
	{	this.gestureName = gestureName;
	}
	
	/////////////////////////////////////////////////////////////////
	// DIRECTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Direction direction = null; //debug

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
	private boolean repeat = false;

	public boolean getRepeat()
	{	return repeat;
	}
	
	public void setRepeat(boolean repeat)
	{	this.repeat = repeat;
	}

	/////////////////////////////////////////////////////////////////
	// PROPORTIONNAL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean proportional = false;

	public boolean getProportional()
	{	return proportional;
	}
	
	public void setProportional(boolean proportional)
	{	this.proportional = proportional;
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public AbstractAnimeDirection surfaceCopy()
	{	AbstractAnimeDirection result = new AbstractAnimeDirection();
		
		// steps
		Iterator<AnimeStep> i = getIterator();
		while(i.hasNext())
		{	AnimeStep copyStep = i.next().surfaceCopy(); 
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

	public AbstractAnimeDirection deepCopy(double zoom, PredefinedColor color) throws IOException
	{	AbstractAnimeDirection result = new AbstractAnimeDirection();
		
		// steps
		Iterator<AnimeStep> i = getIterator();
		while(i.hasNext())
		{	AnimeStep copyStep = i.next().deepCopy(zoom,color); 
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

	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean finished = false;
	
	public void finish()
	{	finished = true;

		// images
		for(T step: steps)
			step.finish();
		steps.clear();

		// misc
		direction = null;
	}
}

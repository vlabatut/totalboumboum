package org.totalboumboum.engine.loop.display.misc;

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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.loop.display.Display;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;
import org.totalboumboum.game.round.RoundVariables;

/**
 * Displays a message indicating a
 * screen capture was just performed.
 * 
 * @author Vincent Labatut
 */
public class DisplayScreenCapture extends Display
{
	/**
	 * Builds a standard display object.
	 */
	public DisplayScreenCapture()
	{	//this.loop = loop;
		
		eventNames.add(SystemControlEvent.REQUIRE_PRINT_SCREEN);
	}
	
	/////////////////////////////////////////////////////////////////
	// SHOW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Duration of the flash */
	private final static long DURATION = 200; 
	/** Time left for the flash */
	private long time = 0;
	/** Skip the first iteration, so that the flash doesn't appear on the capture */
	private boolean first = false;

	@Override
	public void switchShow(SystemControlEvent event)
	{	time = RoundVariables.loop.getTotalRealTime();
		first = true;
	}
	
	/**
	 * Returns the time the flash started
	 * (or zero for no flash at all).
	 * 
	 * @return
	 * 		Time the flash started.
	 */
	private synchronized long getTime()
	{	return time;
	}

	/**
	 * Changes the time the flash started.
	 * 
	 * @param time
	 * 		New time start for the flash.
	 */
	private synchronized void setTime(long time)
	{	this.time = time;
	}

	/**
	 * Indicates if the current iteration
	 * is the first one to display the flash.
	 * 
	 * @return
	 * 		{@code true} iff this is the first flash iteration.
	 */
	private synchronized boolean isFirst()
	{	return first;
	}

	/**
	 * Changes the first time indicator.
	 * 
	 * @param first
	 * 		New first time value.
	 */
	private synchronized void setFirstTime(boolean first)
	{	this.first = first;
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Message */
	private final String MESSAGE = "Capture taken";

	@Override
	public String getMessage(SystemControlEvent event)
	{	return MESSAGE;
	}
	
	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void draw(Graphics g)
	{	boolean first = isFirst();
		if(first)
		{	setFirstTime(false);
		}
		else
		{	long time = getTime();
			if(time!=0)
			{	// update time
				long currentTime = RoundVariables.loop.getTotalRealTime();
				long elapsedTime = currentTime - time;
				if(elapsedTime>=DURATION)
				{	setTime(0);
					elapsedTime = DURATION;
				}
				
				// display flash
				Dimension dim = Configuration.getVideoConfiguration().getPanelDimension();
				Rectangle rectangle = new Rectangle(dim);
				Graphics2D g2 = (Graphics2D)g;
				int alpha = 255 - (int)(255.0*elapsedTime/DURATION);
				Color color = new Color(255,255,255,alpha);
				g2.setPaint(color);
				g2.fill(rectangle);
			}
		}
	}
}

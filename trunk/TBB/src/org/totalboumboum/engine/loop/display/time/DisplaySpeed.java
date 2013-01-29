package org.totalboumboum.engine.loop.display.time;

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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.loop.display.Display;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;

/**
 * Displays the current speed.
 * 
 * @author Vincent Labatut
 */
public class DisplaySpeed extends Display
{
	/**
	 * Builds a standard display object.
	 */
	public DisplaySpeed()
	{	eventNames.add(SystemControlEvent.SWITCH_DISPLAY_SPEED);
	}
	
	/////////////////////////////////////////////////////////////////
	// SHOW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether the information should be displayed or not */
	private boolean show = false;
	
	@Override
	public synchronized void switchShow(SystemControlEvent event)
	{	show = !show;		
	}
	
	/**
	 * Returns the value indicating which
	 * information should be displayed.
	 * 
	 * @return
	 * 		Value indicating which information should be displayed.
	 */
	private synchronized boolean getShow()
	{	return show;
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Display message */
	private final String MESSAGE_DISPLAY = "Display game speed";
	/** Hide message */
	private final String MESSAGE_HIDE = "Hide game speed";

	@Override
	public String getMessage(SystemControlEvent event)
	{	String message = null;
		if(getShow())
			message = MESSAGE_DISPLAY;
		else
			message = MESSAGE_HIDE;
		return message;
	}
	
	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void draw(Graphics g)
	{	if(getShow())
		{	Font font = new Font("Dialog", Font.PLAIN, 18);
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics(font);
			String text = "Speed: "+Configuration.getEngineConfiguration().getSpeedCoeff();
			Rectangle2D box = metrics.getStringBounds(text, g);
			int x = 10;
			int y = (int)Math.round(10+box.getHeight()/2);
			g.setColor(Color.BLACK);
			g.drawString(text,x+1,y+1);
			g.setColor(Color.CYAN);
			g.drawString(text,x,y);
		}
	}
}

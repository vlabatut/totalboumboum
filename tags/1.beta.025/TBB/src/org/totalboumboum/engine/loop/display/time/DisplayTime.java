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

import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.loop.display.Display;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.time.TimeUnit;

/**
 * Displays the current time.
 * 
 * @author Vincent Labatut
 */
public class DisplayTime extends Display
{
	/**
	 * Builds a standard display object.
	 * 
	 * @param loop
	 * 		Object used for displaying.
	 */
	public DisplayTime(VisibleLoop loop)
	{	this.loop = loop;
	
		eventNames.add(SystemControlEvent.SWITCH_DISPLAY_TIME);
	}

	/////////////////////////////////////////////////////////////////
	// DATA				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Object used for displaying */
	private VisibleLoop loop;
	
	/////////////////////////////////////////////////////////////////
	// SHOW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates which information should be displayed */
	private boolean show = false;
	/** How the information should be displayed */
	private int mode = 0;
	
	@Override
	public synchronized void switchShow(SystemControlEvent event)
	{	if(event.getIndex()==SystemControlEvent.REGULAR)
			show = !show;
		else
			mode = (mode+1)%3;
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

	/**
	 * Returns the value indicating how
	 * the information should be displayed.
	 * 
	 * @return
	 * 		Value indicating how the information should be displayed.
	 */
	private synchronized int getMode()
	{	return mode;
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Display message */
	private final String MESSAGE_DISPLAY = "Display ";
	/** Hide message */
	private final String MESSAGE_HIDE = "Hide ";
	/** Display message */
	private final String MESSAGE_GAME = "game time";
	/** Display message */
	private final String MESSAGE_ENGINE = "engine time";
	/** Display message */
	private final String MESSAGE_REAL = "real time";

	@Override
	public String getMessage(SystemControlEvent event)
	{	String message = null;
	
		boolean s = getShow();
		if(s)
			message = MESSAGE_DISPLAY;
		else
			message = MESSAGE_HIDE;
		
		int m = getMode();
		switch(m)
		{	case 0: 
				message = message + MESSAGE_GAME;
				break;
			case 1: 
				message = message + MESSAGE_ENGINE;
				break;
			case 2:
				message = message + MESSAGE_REAL;
				break;
		}		
		
		return message;
	}
	
	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Font used for drawing */
	private final Font FONT = new Font("Dialog", Font.PLAIN, 18);
	/** Drawn text */
	private final String TEXT_GAME = "Game time: ";
	/** Drawn text */
	private final String TEXT_ENGINE = "Engine time: ";
	/** Drawn text */
	private final String TEXT_REAL = "Real time: ";
	/** X location */
	private Integer x = 10;
	/** Y location */
	private Integer y = null;
	/** Vertical offset */
	private final int V_OFFSET = 30;
	
	@Override
	public void draw(Graphics g)
	{	boolean s = getShow();
		if(s)
		{	int m = getMode();
			switch(m)
			{	// loop time
				case 0:
				{	g.setFont(FONT);
					FontMetrics metrics = g.getFontMetrics(FONT);
					long time = loop.getTotalGameTime();
					String text = TEXT_GAME+TimeTools.formatTime(time,TimeUnit.HOUR,TimeUnit.MILLISECOND,false);
					Rectangle2D box = metrics.getStringBounds(text, g);
					y = (int)Math.round(V_OFFSET+box.getHeight()/2);
					g.setColor(Color.BLACK);
					g.drawString(text,x+1,y+1);
					g.setColor(Color.CYAN);
					g.drawString(text,x,y);
				}
				break;
				
				// engine time
				case 1:
				{	g.setFont(FONT);
					FontMetrics metrics = g.getFontMetrics(FONT);
					long time = loop.getTotalEngineTime();
					String text = TEXT_ENGINE+TimeTools.formatTime(time,TimeUnit.HOUR,TimeUnit.MILLISECOND,false);
					Rectangle2D box = metrics.getStringBounds(text, g);
					y = (int)Math.round(V_OFFSET+box.getHeight()/2);
					g.setColor(Color.BLACK);
					g.drawString(text,x+1,y+1);
					g.setColor(Color.CYAN);
					g.drawString(text,x,y);
				}
				break;
				
				// real time
				case 2:
				{	g.setFont(FONT);
					FontMetrics metrics = g.getFontMetrics(FONT);
					long time = loop.getTotalRealTime();
					String text = TEXT_REAL+TimeTools.formatTime(time,TimeUnit.HOUR,TimeUnit.MILLISECOND,false);
					Rectangle2D box = metrics.getStringBounds(text, g);
					y = (int)Math.round(V_OFFSET+box.getHeight()/2);
					g.setColor(Color.BLACK);
					g.drawString(text,x+1,y+1);
					g.setColor(Color.CYAN);
					g.drawString(text,x,y);
				}
				break;
			}
		}
	}
}

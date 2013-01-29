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
	private int show = 0;
	
	@Override
	public synchronized void switchShow(SystemControlEvent event)
	{	show = (show+1)%4;
	}
	
	/**
	 * Returns the value indicating which
	 * information should be displayed.
	 * 
	 * @return
	 * 		Value indicating which information should be displayed.
	 */
	private synchronized int getShow()
	{	return show;
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Display message */
	private final String MESSAGE_DISPLAY_GAME = "Display game time";
	/** Display message */
	private final String MESSAGE_DISPLAY_ENGINE = "Display engine time";
	/** Display message */
	private final String MESSAGE_DISPLAY_REAL = "Display real time";
	/** Hide message */
	private final String MESSAGE_HIDE = "Hide all times";

	@Override
	public String getMessage(SystemControlEvent event)
	{	String message = null;
		int s = getShow();
		switch(s)
		{	case 0:
				message = MESSAGE_HIDE;
				break;
			case 1: 
				message = MESSAGE_DISPLAY_GAME;
				break;
			case 2: 
				message = MESSAGE_DISPLAY_ENGINE;
				break;
			case 3:
				message = MESSAGE_DISPLAY_REAL;
				break;
		}			
		return message;
	}
	
	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void draw(Graphics g)
	{	int s = getShow();
		switch(s)
		{	// loop time
			case 1:
			{	Font font = new Font("Dialog", Font.PLAIN, 18);
				g.setFont(font);
				FontMetrics metrics = g.getFontMetrics(font);
				long time = loop.getTotalGameTime();
				String text = "Game time: "+TimeTools.formatTime(time,TimeUnit.HOUR,TimeUnit.MILLISECOND,false);
				Rectangle2D box = metrics.getStringBounds(text, g);
				int x = 10;
				int y = (int)Math.round(30+box.getHeight()/2);
				g.setColor(Color.BLACK);
				g.drawString(text,x+1,y+1);
				g.setColor(Color.CYAN);
				g.drawString(text,x,y);
			}
			break;
			
			// engine time
			case 2:
			{	Font font = new Font("Dialog", Font.PLAIN, 18);
				g.setFont(font);
				FontMetrics metrics = g.getFontMetrics(font);
				long time = loop.getTotalEngineTime();
				String text = "Engine time: "+TimeTools.formatTime(time,TimeUnit.HOUR,TimeUnit.MILLISECOND,false);
				Rectangle2D box = metrics.getStringBounds(text, g);
				int x = 10;
				int y = (int)Math.round(30+box.getHeight()/2);
				g.setColor(Color.BLACK);
				g.drawString(text,x+1,y+1);
				g.setColor(Color.CYAN);
				g.drawString(text,x,y);
			}
			break;
			
			// real time
			case 3:
			{	Font font = new Font("Dialog", Font.PLAIN, 18);
				g.setFont(font);
				FontMetrics metrics = g.getFontMetrics(font);
				long time = loop.getTotalRealTime();
				String text = "Real time: "+TimeTools.formatTime(time,TimeUnit.HOUR,TimeUnit.MILLISECOND,false);
				Rectangle2D box = metrics.getStringBounds(text, g);
				int x = 10;
				int y = (int)Math.round(30+box.getHeight()/2);
				g.setColor(Color.BLACK);
				g.drawString(text,x+1,y+1);
				g.setColor(Color.CYAN);
				g.drawString(text,x,y);
			}
			break;
		}
	}
}

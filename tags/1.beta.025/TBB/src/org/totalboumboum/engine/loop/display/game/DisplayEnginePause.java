package org.totalboumboum.engine.loop.display.game;

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

/**
 * Displays a message indicating the
 * game is currently paused.
 * 
 * @author Vincent Labatut
 */
public class DisplayEnginePause extends Display
{
	/**
	 * Builds a standard display object.
	 * 
	 * @param loop
	 * 		Object used for displaying.
	 */
	public DisplayEnginePause(VisibleLoop loop)
	{	//this.loop = loop;
		
		eventNames.add(SystemControlEvent.SWITCH_ENGINE_PAUSE);
	}
	
	/////////////////////////////////////////////////////////////////
	// SHOW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether the information should be displayed or not */
	private boolean show = false;
	/** How the information should be displayed */
	private boolean mode = false;

	@Override
	public void switchShow(SystemControlEvent event)
	{	if(event.getIndex()==SystemControlEvent.REGULAR)
			show = !show;
		else
			mode = !mode;
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
	private synchronized boolean getMode()
	{	return mode;
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Pause message */
	private final String MESSAGE_PAUSE = "Pause engine";
	/** Unpause message */
	private final String MESSAGE_UNPAUSE = "Unpause engine";
	/** Hiden message */
	private final String MESSAGE_HIDEN = " (hiden)";
	/** Visible message */
	private final String MESSAGE_VISIBLE = "";

	@Override
	public String getMessage(SystemControlEvent event)
	{	String message = null;
		
		boolean s = getShow();
		if(s)
			message = MESSAGE_PAUSE;
		else
			message = MESSAGE_UNPAUSE;
		
		if(s)
		{	boolean m = getMode();
			if(m)
				message = message + MESSAGE_HIDEN;
			else
				message = message + MESSAGE_VISIBLE;
		}

		return message;
	}
	
	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Text displayed */
	private final String TEXT_DISPLAY = "Engine paused";
	/** Font used */
	private final Font FONT = new Font("Dialog", Font.PLAIN, 18);
	/** X position */
	private Integer x = 10;
	/** Y position */
	private Integer y = null;
	/** Drawing color */
	private Color color = Color.MAGENTA;
	/** Vertical offset */
	private final int V_OFFSET = 70;
	
	@Override
	public void draw(Graphics g)
	{	boolean s = getShow();
		boolean m = getMode();
		if(s && !m)
		{	// set font
			g.setFont(FONT);
			// possibly init position
			if(y==null)
			{	FontMetrics metrics = g.getFontMetrics(FONT);
				Rectangle2D box = metrics.getStringBounds(TEXT_DISPLAY,g);
				y = (int)Math.round(V_OFFSET+box.getHeight()/2);
			}
			// draw
			g.setColor(Color.BLACK);
			g.drawString(TEXT_DISPLAY,x+1,y+1);
			g.setColor(color);
			g.drawString(TEXT_DISPLAY,x,y);
		}
	}
}

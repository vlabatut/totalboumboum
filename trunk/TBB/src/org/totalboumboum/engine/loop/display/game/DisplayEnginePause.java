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

	@Override
	public void switchShow(SystemControlEvent event)
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
	private final String MESSAGE_DISPLAY = "Pause engine";
	/** Hide message */
	private final String MESSAGE_HIDE = "Unpause engine";

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
			String text = "Engine paused";
			Rectangle2D box = metrics.getStringBounds(text,g);
			int x = 10;
			int y = (int)Math.round(70+box.getHeight()/2);
			g.setColor(Color.BLACK);
			g.drawString(text,x+1,y+1);
			g.setColor(Color.MAGENTA);
			g.drawString(text,x,y);
		}
	}
}

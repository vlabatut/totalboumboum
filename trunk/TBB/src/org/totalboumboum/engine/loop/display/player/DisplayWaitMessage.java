package org.totalboumboum.engine.loop.display.player;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
 * Displays a wait message,
 * used in network mode.
 *  
 * @author Vincent Labatut
 */
public class DisplayWaitMessage extends Display
{
	/**
	 * Builds a standard display object.
	 * 
	 * @param loop
	 * 		Object used for displaying.
	 */
	public DisplayWaitMessage(VisibleLoop loop)
	{	this.loop = loop;
		
		eventNames.add(SystemControlEvent.SWITCH_ENGINE_PAUSE);
	}
	
	/////////////////////////////////////////////////////////////////
	// DATA				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Object used for displaying */
	private VisibleLoop loop;
	
	/////////////////////////////////////////////////////////////////
	// SHOW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void switchShow(SystemControlEvent event)
	{	// useless here
	}
	
	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String getMessage(SystemControlEvent event)
	{	return null;
	}
	
	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Font used for drawing */
	private final Font FONT = new Font("Dialog", Font.PLAIN, 18);
	/** Text drawn */
	private final String TEXT = "Waiting for other players";
	/** X position */
	private Integer x = 10;
	/** Y position */
	private Integer y = null;
	/** Vertical offset */
	private final int V_OFFSET = 90;
	
	@Override
	public void draw(Graphics g)
	{	if(loop.getEnginePause())
		{	g.setFont(FONT);
			if(y==null)
			{	FontMetrics metrics = g.getFontMetrics(FONT);
				Rectangle2D box = metrics.getStringBounds(TEXT, g);
				y = (int)Math.round(V_OFFSET+box.getHeight()/2);
			}
			
			g.setColor(Color.GRAY);
			g.drawString(TEXT,x+1,y+1);
			g.setColor(Color.RED);
			g.drawString(TEXT,x,y);
		}
	}
}

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
import java.text.NumberFormat;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.loop.display.Display;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;

/**
 * Displays the current FPS.
 * 
 * @author Vincent Labatut
 */
public class DisplayFPS extends Display
{
	/**
	 * Builds a standard display object.
	 * 
	 * @param loop
	 * 		Object used for displaying.
	 */
	public DisplayFPS(VisibleLoop loop)
	{	this.loop = loop;
		
		eventNames.add(SystemControlEvent.SWITCH_DISPLAY_FPS);
	}
	
	/////////////////////////////////////////////////////////////////
	// DATA				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Object used for displaying */
	private VisibleLoop loop;
	
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
	private final String MESSAGE_DISPLAY = "Display FPS/UPS";
	/** Hide message */
	private final String MESSAGE_HIDE = "Hide FPS/UPS";

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
	/** Font used for drawing */
	private final Font FONT = new Font("Dialog", Font.PLAIN, 18);
	/** Prefix text */
	private final String PREFIX = "FPS/UPS/Th: ";
	/** X position */
	private Integer x = 10;
	/** Y position */
	private Integer y = null;
	/** Number format */
	private NumberFormat nf = null;
	/** Vertical offset */
	private final int V_OFFSET = 50;
	
	@Override
	public void draw(Graphics g)
	{	if(getShow())
		{	if(nf==null)
			{	nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(2);
			}
			
			g.setFont(FONT);
			FontMetrics metrics = g.getFontMetrics(FONT);
			
			double fps = loop.getAverageFPS();
			String fpsStr = nf.format(fps); 
			double ups = loop.getAverageUPS();
			String upsStr = nf.format(ups);
			String thFps = Integer.toString(Configuration.getEngineConfiguration().getFps());
			String text = PREFIX+fpsStr+"/"+upsStr+"/"+thFps;
			
			Rectangle2D box = metrics.getStringBounds(text, g);
			y = (int)Math.round(V_OFFSET+box.getHeight()/2);
			g.setColor(Color.BLACK);
			g.drawString(text,x+1,y+1);
			g.setColor(Color.CYAN);
			g.drawString(text,x,y);
		}
	}
}

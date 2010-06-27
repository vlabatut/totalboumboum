package org.totalboumboum.engine.loop.display;

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

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class DisplayFPS implements Display
{
	public DisplayFPS(VisibleLoop loop)
	{	this.loop = loop;
	}
	
	/////////////////////////////////////////////////////////////////
	// LOOP				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private VisibleLoop loop;
	
	/////////////////////////////////////////////////////////////////
	// SHOW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean show = false;
	
	@Override
	public synchronized void switchShow(SystemControlEvent event)
	{	show = !show;		
	}
	
	private synchronized boolean getShow()
	{	return show;
	}

	/////////////////////////////////////////////////////////////////
	// EVENT NAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String getEventName()
	{	return SystemControlEvent.SWITCH_DISPLAY_FPS;
	}

	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void draw(Graphics g)
	{	if(getShow())
		{	g.setColor(Color.CYAN);
			Font font = new Font("Dialog", Font.PLAIN, 18);
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics(font);
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(2);
			nf.setMinimumFractionDigits(2);
			double fps = loop.getAverageFPS();
			String fpsStr = nf.format(fps); 
			double ups = loop.getAverageUPS();
			String upsStr = nf.format(ups);
			String thFps = Integer.toString(Configuration.getEngineConfiguration().getFps());
			String text = "FPS/UPS/Th: "+fpsStr+"/"+upsStr+"/"+thFps;
			Rectangle2D box = metrics.getStringBounds(text, g);
			int x = 10;
			int y = (int)Math.round(50+box.getHeight()/2);
			g.drawString(text, x, y);
		}
	}
}

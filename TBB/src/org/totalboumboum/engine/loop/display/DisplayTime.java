package org.totalboumboum.engine.loop.display;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.time.TimeUnit;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class DisplayTime implements Display
{
	public DisplayTime(VisibleLoop loop)
	{	this.loop = loop;
	}

	/////////////////////////////////////////////////////////////////
	// LOOP				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private VisibleLoop loop;
	
	/////////////////////////////////////////////////////////////////
	// SHOW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int show = 0;
	
	@Override
	public synchronized void switchShow(SystemControlEvent event)
	{	show = (show+1)%4;
	}
	
	private synchronized int getShow()
	{	return show;
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String getMessage(SystemControlEvent event)
	{	String message = null;
		int s = getShow();
		switch(s)
		{	case 0:
				message = "Hide all times";
				break;
			case 1: 
				message = "Display game time";
				break;
			case 2: 
				message = "Display engine time";
				break;
			case 3:
				message = "Display real time";
				break;
		}			
		return message;
	}
	
	/////////////////////////////////////////////////////////////////
	// EVENT NAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<String> eventNames = new ArrayList<String>(Arrays.asList(SystemControlEvent.SWITCH_DISPLAY_TIME));
	
	@Override
	public List<String> getEventNames()
	{	return eventNames;
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

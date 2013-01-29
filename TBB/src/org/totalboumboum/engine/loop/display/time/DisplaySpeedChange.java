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
 * Displays changes in the game speed.
 * 
 * @author Vincent Labatut
 */
public class DisplaySpeedChange extends Display
{
	/**
	 * Builds a standard display object.
	 */
	public DisplaySpeedChange()
	{	eventNames.add(SystemControlEvent.REQUIRE_SPEED_UP);
		eventNames.add(SystemControlEvent.REQUIRE_SLOW_DOWN);
	}
	
	/////////////////////////////////////////////////////////////////
	// SHOW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Total display time */
	private final long MESSAGE_DURATION = 1000;
	/** Remaining display time */
	private long messageTime = 0;
	
	@Override
	public synchronized void switchShow(SystemControlEvent event)
	{	messageTime = System.currentTimeMillis();
	}
	
	/**
	 * Returns the time elapsed since
	 * the message has been displayed.
	 * 
	 * @return
	 * 		A time expressed in ms.
	 */
	private synchronized long getElapsedTime()
	{	long currentTime = System.currentTimeMillis();
		long result = currentTime - messageTime;
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Increase message */
	private final String MESSAGE_INCREASE = "Increase game speed";
	/** Decrease message */
	private final String MESSAGE_DECREASE = "Decrease game speed";

	@Override
	public String getMessage(SystemControlEvent event)
	{	String message;
		if(event.getName().equals(SystemControlEvent.REQUIRE_SPEED_UP))
			message = MESSAGE_INCREASE;
		else //if(event.getName().equals(SystemControlEvent.REQUIRE_SLOW_DOWN))
			message = MESSAGE_DECREASE;
		return message;
	}
	
	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void draw(Graphics g)
	{	long elapsedTime = getElapsedTime();
		if(elapsedTime<MESSAGE_DURATION)
		{	Font font = new Font("Dialog", Font.PLAIN, 18);
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics(font);
			String text = "Speed: "+Configuration.getEngineConfiguration().getSpeedCoeff();
			Rectangle2D box = metrics.getStringBounds(text, g);
			int x = 10;
			int y = (int)Math.round(10+box.getHeight()/2);
			int alpha = Math.round((1-elapsedTime/(float)MESSAGE_DURATION)*255);
			Color background = new Color(0,0,0,alpha);
			g.setColor(background);
			g.drawString(text,x+1,y+1);
			Color foreground = new Color(255,200,0,alpha);
			g.setColor(foreground);
			g.drawString(text,x,y);
		}
	}
}

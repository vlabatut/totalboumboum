package org.totalboumboum.engine.loop.display;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;
import org.totalboumboum.game.round.RoundVariables;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class DisplayFeedbackMessage
{
	public DisplayFeedbackMessage()
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// MESSAGES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final LinkedList<String> messages = new LinkedList<String>();
	private final LinkedList<Long> times = new LinkedList<Long>();
	
	public void processEvent(SystemControlEvent event, Display display)
	{	String message = display.getMessage(event);
		if(message!=null)
		{	messages.offer(message);
			times.offer(RoundVariables.loop.getTotalEngineTime());
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final long MESSAGE_DURATION = 10000;
	
	public void draw(Graphics g)
	{	// init
		Font font = new Font("Dialog", Font.PLAIN, 18);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		Dimension dim = Configuration.getVideoConfiguration().getPanelDimension();
		long currentTime = RoundVariables.loop.getTotalRealTime();
		
		// process each message
		int line=0;
		while(line<messages.size())
		{	int index = messages.size()-line-1;
			String message = messages.get(index);
			long time = times.get(index);
			long elapsedTime = currentTime - time;
			if(elapsedTime>=MESSAGE_DURATION)
			{	messages.remove(index);
				times.remove(index);
			}
			else
			{	
				Rectangle2D box = metrics.getStringBounds(message,g);
				int x = (int)Math.round(dim.getWidth()-box.getWidth()-10);
				int y = (int)Math.round(dim.getHeight()-box.getHeight()-line*20);
				int alpha = Math.round((1-elapsedTime/(float)MESSAGE_DURATION)*255);
				Color background = new Color(0,0,0,alpha);
				g.setColor(background);
				g.drawString(message,x+1,y+1);
				Color foreground = new Color(200,100,100,alpha);
				g.setColor(foreground);
				g.drawString(message,x,y);
				
				line++;
			}
		}
	}
}

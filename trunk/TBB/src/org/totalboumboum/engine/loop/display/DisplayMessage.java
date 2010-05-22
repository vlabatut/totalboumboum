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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.loop.event.control.ControlEvent;
import org.totalboumboum.tools.images.MessageDisplayer;

public class DisplayMessage implements Display
{
	public DisplayMessage()
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAYED TEXT		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static MessageDisplayer messageDisplayers[] = null;

	public static void initMessageDisplayers(String texts[], VisibleLoop loop)
	{	if(messageDisplayers == null)
		{	messageDisplayers = new MessageDisplayer[texts.length+1];
			messageDisplayers[0] = null;
			Dimension dim = Configuration.getVideoConfiguration().getPanelDimension();
			double coef = 0.9;
			Font displayedTextFont = loop.getPanel().getMessageFont(dim.width*coef,dim.height*coef);
			displayedTextFont = displayedTextFont.deriveFont(Font.BOLD);
			int xc = (int)Math.round(dim.width/2);
			int yc = (int)Math.round(dim.height/2);
			for(int i=1;i<messageDisplayers.length;i++)
			{	if(texts[i]!=null)
				{	MessageDisplayer temp = new MessageDisplayer(displayedTextFont,xc,yc);
					temp.setFatten(3);
					temp.setTextColor(new Color(204, 18,128));
					temp.updateText(texts[i]);
					messageDisplayers[i] = temp;
				}
				else
					messageDisplayers[i] = null;
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// SHOW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int show = 0;
	
	@Override
	public void switchShow(ControlEvent event)
	{	show++;		
	}

	/////////////////////////////////////////////////////////////////
	// EVENT NAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String getEventName()
	{	return ControlEvent.REQUIRE_NEXT_MESSAGE;
		
	}

	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void draw(Graphics g)
	{	// basic
//		g.setColor(Color.WHITE);
//		g.setFont(displayedTextFont);
//		FontMetrics metrics = g.getFontMetrics(displayedTextFont);
//		Rectangle2D box = metrics.getStringBounds(displayedText,g);
//		int x = (int)Math.round(pixelLeftX+pixelWidth/2-box.getWidth()/2);
//		int y = (int)Math.round(pixelTopY+pixelHeight/2+box.getHeight()/2);
//		g.drawString(displayedText,x,y);
		// effects
		if(messageDisplayers.length>show && messageDisplayers[show]!=null)
			messageDisplayers[show].paint(g);
	}
}

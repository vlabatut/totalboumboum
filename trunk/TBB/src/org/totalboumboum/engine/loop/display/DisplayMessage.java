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
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;
import org.totalboumboum.tools.images.MessageDisplayer;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class DisplayMessage implements Display
{
	public DisplayMessage(VisibleLoop loop)
	{	if(messageDisplayers == null)
			initMessageDisplayers(loop);
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAYED TEXT		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static MessageDisplayer messageDisplayers[] = null;

	private static void initMessageDisplayers(VisibleLoop loop)
	{	String texts[] = loop.getEntryTexts();
		messageDisplayers = new MessageDisplayer[texts.length];
		Dimension dim = Configuration.getVideoConfiguration().getPanelDimension();
		double coef = 0.9;
		Font displayedTextFont = loop.getPanel().getMessageFont(dim.width*coef,dim.height*coef);
		displayedTextFont = displayedTextFont.deriveFont(Font.BOLD);
		int xc = (int)Math.round(dim.width/2);
		int yc = (int)Math.round(dim.height/2);
		for(int i=0;i<messageDisplayers.length;i++)
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
//for(int i=0;i<texts.length;i++)
//	System.out.print(texts[i]);
//System.out.println();
	}

	/////////////////////////////////////////////////////////////////
	// SHOW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int show = -1;
	
	@Override
	public void switchShow(SystemControlEvent event)
	{	show++;		
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String getMessage(SystemControlEvent event)
	{	return null;
	}
	
	/////////////////////////////////////////////////////////////////
	// EVENT NAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<String> eventNames = new ArrayList<String>(Arrays.asList(SystemControlEvent.REQUIRE_NEXT_MESSAGE));
	
	@Override
	public List<String> getEventNames()
	{	return eventNames;
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
		if(show>=0 && show<messageDisplayers.length && messageDisplayers[show]!=null)
			messageDisplayers[show].paint(g);
	}
}

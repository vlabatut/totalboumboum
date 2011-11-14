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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.totalboumboum.ai.AiAbstractManager;
import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.engine.player.AiPlayer;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class DisplayRealtimeUsage implements Display
{
	public DisplayRealtimeUsage(VisibleLoop loop)
	{	this.players = loop.getPlayers();
	}

	/////////////////////////////////////////////////////////////////
	// LOOP				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<AbstractPlayer> players;
	
	/////////////////////////////////////////////////////////////////
	// SHOW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean show = false;
	private boolean mode = true;
	
	@Override
	public synchronized void switchShow(SystemControlEvent event)
	{	if(event.getIndex()==SystemControlEvent.REGULAR)
			show = !show;
		else
			mode = !mode;
	}
	
	private synchronized boolean getShow()
	{	return show;
	}

	private synchronized boolean getMode()
	{	return mode;
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String getMessage(SystemControlEvent event)
	{	String message = null;
		boolean s = getShow();
		if(s)
		{	boolean m = getMode();
			if(m)
				message = "Display averaged real-time AIs usage";
			else
				message = "Display instant real-time AIs usage";
		}
		else
			message = "Hide all real-time usage";
		return message;
	}
	
	/////////////////////////////////////////////////////////////////
	// EVENT NAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<String> eventNames = new ArrayList<String>(Arrays.asList(SystemControlEvent.SWITCH_DISPLAY_REALTIME_USAGE));
	
	@Override
	public List<String> getEventNames()
	{	return eventNames;
	}

	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void draw(Graphics g)
	{	boolean s = getShow();
		
		if(s)
		{	boolean m = getMode();
			NumberFormat nf = NumberFormat.getIntegerInstance();
			nf.setMinimumIntegerDigits(6);
			if(m)
			{	nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(2);
			}
			String unit = " ms";
			
			int bigSize = 18;
			Font bigFont = new Font("Dialog", Font.PLAIN, bigSize);
			FontMetrics bigMetrics = g.getFontMetrics(bigFont);
			int smallSize = 14;
			Font smallFont = new Font("Dialog", Font.PLAIN, 14);
			FontMetrics smallMetrics = g.getFontMetrics(smallFont);
			Rectangle2D box = smallMetrics.getStringBounds(nf.format(0)+unit,g);
			int maxDurationWidth = (int)box.getWidth();
			int x = 400;
			int y = 90;
			int xLines[] = {10,20,30,40,50,60,70,80,90,100,200,300,400,500};
			
			for(int i=0;i<players.size();i++)
			{	AbstractPlayer player = players.get(i);
				if(player instanceof AiPlayer)
				{	AiAbstractManager<?> aiMgr = ((AiPlayer)player).getArtificialIntelligence();
					List<String> stepNames = new ArrayList<String>(aiMgr.getStepNames());
				
					// draw background
					{	// background color
						int xBg = x - maxDurationWidth - 5;
						int margin = 5;
						int yBg = y - margin;
						int width = maxDurationWidth + margin + 300;
						int height = margin + bigSize + (stepNames.size()+1)*(smallSize+2) + margin;
						g.setColor(new Color(255,255,255,100));
						g.fillRect(xBg,yBg,width,height);
						
						// vertical lines
						g.setColor(new Color(0,0,0,150));
						for(int xLine: xLines)
							g.drawLine(x+xLine,y+bigSize,x+xLine,y+height-2*margin);
					}
					
					// draw the player's name
					{	g.setFont(bigFont);
						String text = "["+player.getName()+"]";
						box = bigMetrics.getStringBounds(text,g);
						int offset = (int)Math.round(box.getHeight()/2);
						y = y + offset;
						g.setColor(Color.black);	
						g.drawString(text,x+1,y+1);
						PredefinedColor color = player.getColor();
						g.setColor(color.getColor());
						g.drawString(text,x,y);
						y = y + offset + 3;
					}					
					
					// get the durations
					HashMap<String,Color> stepColors = aiMgr.getStepColors();
					stepNames.add(0,aiMgr.TOTAL_DURATION);
					HashMap<String,LinkedList<Long>> instantDurations = aiMgr.getInstantDurations();
					HashMap<String,Float> averageDurations = aiMgr.getAverageDurations();
					
					// draw the durations
					g.setFont(smallFont);
					for(String stepName: stepNames)
					{	// get text
						String durationStr;
						Long duration = null;
						if(m)
						{	Float averageDuration = averageDurations.get(stepName);
							if(averageDuration==null)
								averageDuration = 0f;
							duration = (long)(averageDuration*1);
							durationStr = nf.format(averageDuration)+unit;
						}
						else
						{	LinkedList<Long> list = instantDurations.get(stepName);
							if(!list.isEmpty())
								duration = list.getLast();
							if(duration==null)
								duration = 0l;
							durationStr = nf.format(duration)+unit;
						}
						// get max text
						String maxStr;
						{	LinkedList<Long> list = instantDurations.get(stepName);
							long max = Collections.max(list);
							maxStr = "max: "+ nf.format(max) + unit;	
						}						
						
						// draw rectangle
						box = smallMetrics.getStringBounds(stepName,g);
						int height = (int)box.getHeight();
						int width = (int)(duration*1);
//						int width = 100;
						Color color = stepColors.get(stepName);
						g.setColor(color);
						g.fillRect(x,y-height/2,width,height);
						
						// draw inside text
						int colorTotal = color.getRed()+color.getGreen()+color.getBlue();
						Color background;
						if(colorTotal>3*128)
						{	color = new Color(0,0,0,200);
							background = new Color(255,255,255,200);
						}
						else
						{	color = new Color(255,255,255,200);
							background = new Color(0,0,0,200);
						}
						int yText = y + height/3;
						// title
						int xText = x + 2;
						g.setColor(background);
						g.drawString(stepName,xText+1,yText+1);
						g.setColor(color);
						g.drawString(stepName,xText,yText);
						// max
						xText = x + 200;
						g.setColor(background);
						g.drawString(maxStr,xText+1,yText+1);
						g.setColor(color);
						g.drawString(maxStr,xText,yText);
						
						// draw duration text
						box = smallMetrics.getStringBounds(durationStr,g);
						width = (int)box.getWidth();
						background = new Color(255,255,255,200);
						color = new Color(0,0,0,200);
						xText = x - 2 - width;
						g.setColor(background);
						g.drawString(durationStr,xText+1,yText+1);
						g.setColor(color);
						g.drawString(durationStr,xText,yText);
						
						y = y + height;
					}
					
					y = y + 5;
				}
			}
		}
	}
}

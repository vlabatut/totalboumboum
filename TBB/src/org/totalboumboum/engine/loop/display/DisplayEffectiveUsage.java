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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;
import org.totalboumboum.engine.player.AbstractPlayer;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class DisplayEffectiveUsage implements Display
{
	public DisplayEffectiveUsage(VisibleLoop loop)
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
	private boolean mode = true;
	
	@Override
	public synchronized void switchShow(SystemControlEvent event)
	{	if(event.getIndex()==SystemControlEvent.REGULAR)
			show = (show+1)%4;
		else
			mode = !mode;
	}
	
	private synchronized int getShow()
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
		int s = getShow();
		switch(s)
		{	case 0:
				message = "Hide all effective usages";
				break;
			case 1: 
				message = "Display effective overall usage";
				break;
			case 2: 
				message = "Display effective engine-only usage";
				break;
			case 3:
				message = "Display effective AIs-only usage";
				break;
		}
		
		if(s>0)
		{	boolean m = getMode();
			if(m)
				message = message + " in percents";
			else
				message = message + " in ms";
		}
		return message;
	}
	
	/////////////////////////////////////////////////////////////////
	// EVENT NAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<String> eventNames = new ArrayList<String>(Arrays.asList(SystemControlEvent.SWITCH_DISPLAY_EFFECTIVE_USAGE));
	
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
		
		if(s>0)
		{	boolean m = getMode();
			// retrieve CPU usage
			double[] values0;
			if(m)
				values0 = loop.getAverageCpuProportions();
			else
				values0 = loop.getAverageCpu();
			
			// init text
			String text = null;
			switch(s)
			{	case 1: 
					text = "All";
					break;
				case 2:
					text = "Engine";
					break;
				case 3:
					text = "AIs";
					break;
			}
			Font font = new Font("Dialog", Font.PLAIN, 18);
			FontMetrics metrics = g.getFontMetrics(font);
			Rectangle2D box = metrics.getStringBounds(text,g);
			int boxHeight = (int)box.getHeight();
			int margin = boxHeight/8;
			
			// possibly renormalize
			double[] values = new double[values0.length];
			switch(s)
			{	case 1: 
					{	if(m)
							values = values0;
						else
						{	double total = 0;
							for(int i=0;i<values0.length;i++)
								total = total + values0[i];
							for(int i=0;i<values0.length;i++)
								values[i] = values0[i]/total;
						}
					}
					break;
				case 2:
					{	double total = values0[0] + values0[1];
						values[0] = values0[0]/total;
						values[1] = values0[1]/total;
						for(int i=2;i<values0.length;i++)
							values[i] = 0;
					}
					break;
				case 3:
					{	double total = 0;
						for(int i=2;i<values0.length;i++)
							total = total + values0[i];
						values[0] = 0;
						values[1] = 0;
						for(int i=2;i<values0.length;i++)
							values[i] = values0[i]/total;
					}
					break;
			}
			
			// init dimensions
			int height = boxHeight+2*margin;
			int width = 200;
	        int y = 100;
	        int x = 10;
	        
			// init colors
			List<Color> colors = new ArrayList<Color>();
			switch(s)
			{	case 1: 
					{	Color clr = new Color(0,0,0,50);
						colors.add(clr);
						colors.add(clr);
					}
					break;
				case 2:
					{	colors.add(Color.GRAY);
						colors.add(Color.MAGENTA);
					}
					break;
				case 3:
					{	Color clr = new Color(0,0,0,50);
						colors.add(clr);
						colors.add(clr);
					}
					break;
			}
			List<AbstractPlayer> players = loop.getPlayers();
			for(AbstractPlayer player: players)
				colors.add(player.getColor().getColor());
			
			// process the diagram dims
			int widths[] = new int[values0.length];
			int usedWidth = 0;
			for(int i=1;i<values.length;i++)
			{	widths[i] = (int)(width*values[i]);
				usedWidth = usedWidth + widths[i];
			}
	        widths[0] = width - usedWidth;
	        
	        // draw the diagram
			g.setColor(new Color(0,0,0,50));
			g.fillRect(x,y-boxHeight/2,width,height);
			g.setColor(Color.BLACK);
			g.drawRect(x+1,y-boxHeight/2+1,width,height);
			int offset = 0;
			for(int i=0;i<colors.size();i++)
			{	int w = widths[i];
				Color color = colors.get(i);
				g.setColor(color);
				g.fillRect(x+offset,y-boxHeight/2,w,height);
				offset = offset + w;
			}
			g.setColor(Color.WHITE);
			g.drawRect(x,y-boxHeight/2,width,height);
			
			// add the main text
			g.setColor(new Color(255,255,255,50));
			g.setFont(font);
			int yText = (int)Math.round(y+boxHeight/2-margin);
			int xText = x+2*margin;
			g.drawString(text,xText,yText);
			
			// put the details
			//if(getMode())
			{	NumberFormat nf;
				String unit;
				if(m)
				{	nf = NumberFormat.getPercentInstance();
					nf.setMinimumIntegerDigits(2);
					nf.setMinimumFractionDigits(4);
					nf.setMaximumFractionDigits(4);
					unit = "";
				}
				else
				{	nf = NumberFormat.getIntegerInstance();
					nf.setMinimumIntegerDigits(6);
					values = values0;
					unit = " ms ";
				}
				List<AbstractPlayer> plrs = loop.getPlayers();
				
				offset = 0;
				y = y + height;
				for(int i=0;i<colors.size();i++)
				{	if((i<=1 && (s==1 || s==2))
						|| (i>=2 && (s==1 || s==3)))
					{	// init text
						switch(i)
						{	case 0:
								text = "Swing"; 
								break;
							case 1:
								text = "Engine";
								break;
							default:
								text = plrs.get(i-2).getName();
						
						}
						text = nf.format(values[i])+unit+" ["+text+"]";
						// process size and location
						box = metrics.getStringBounds(text,g);
						boxHeight = (int)box.getHeight();
						int boxWidth = (int)box.getWidth();
						// draw background
						int rectMargin = 0;
						int rectHeight = boxHeight + 2*rectMargin;
						int rectWidth = boxWidth + 2*rectMargin;
						g.setColor(new Color(255,255,255,50));
						g.fillRect(x-5,y-rectHeight/2,rectWidth,rectHeight);
						// draw text
						yText = (int)Math.round(y+boxHeight/2-metrics.getDescent());
						g.setColor(new Color(0,0,0,150));
						g.drawString(text,x+1,yText+1);
						g.setColor(colors.get(i));
						g.drawString(text,x,yText);
						// increase offset
						y = y + boxHeight;
					}
				}
			}
		}
	}
}

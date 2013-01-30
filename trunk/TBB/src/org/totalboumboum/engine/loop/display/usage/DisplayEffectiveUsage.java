package org.totalboumboum.engine.loop.display.usage;

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
import java.util.List;

import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.loop.display.Display;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;
import org.totalboumboum.engine.player.AbstractPlayer;

/**
 * Displays how much effective processor time
 * each artificial agent uses during the game.
 * 
 * @author Vincent Labatut
 */
public class DisplayEffectiveUsage extends Display
{
	/**
	 * Builds a standard display object.
	 * 
	 * @param loop
	 * 		Object used for displaying.
	 */
	public DisplayEffectiveUsage(VisibleLoop loop)
	{	this.loop = loop;
		
		eventNames.add(SystemControlEvent.SWITCH_DISPLAY_EFFECTIVE_USAGE);
	}

	/////////////////////////////////////////////////////////////////
	// DATA				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Object used for displaying */
	private VisibleLoop loop;
	
	/////////////////////////////////////////////////////////////////
	// SHOW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates which information should be displayed */
	private int show = 0;
	/** Indicates how the information should be displayed */
	private boolean mode = true;
	
	@Override
	public synchronized void switchShow(SystemControlEvent event)
	{	if(event.getIndex()==SystemControlEvent.REGULAR)
			show = (show+1)%4;
		else
			mode = !mode;
	}
	
	/**
	 * Returns the value indicating which
	 * information should be displayed.
	 * 
	 * @return
	 * 		Value indicating which information should be displayed.
	 */
	private synchronized int getShow()
	{	return show;
	}

	/**
	 * Returns the value indicating how
	 * the information should be displayed.
	 * 
	 * @return
	 * 		Value indicating how the information should be displayed.
	 */
	private synchronized boolean getMode()
	{	return mode;
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Display message */
	private final String MESSAGE_DISPLAY_OVERALL = "Display effective overall usage";
	/** Display message */
	private final String MESSAGE_DISPLAY_ENGINE = "Display effective engine-only usage";
	/** Display message */
	private final String MESSAGE_DISPLAY_AI = "Display effective AIs-only usage";
	/** Hide message */
	private final String MESSAGE_HIDE = "Hide all effective usages";
	/** Units message */
	private final String MESSAGE_UNIT_PERCENT = " in percents";
	/** Units message */
	private final String MESSAGE_UNIT_MS = " in ms";

	@Override
	public String getMessage(SystemControlEvent event)
	{	String message = null;
		int s = getShow();
		switch(s)
		{	case 0:
				message = MESSAGE_HIDE;
				break;
			case 1: 
				message = MESSAGE_DISPLAY_OVERALL;
				break;
			case 2: 
				message = MESSAGE_DISPLAY_ENGINE;
				break;
			case 3:
				message = MESSAGE_DISPLAY_AI;
				break;
		}
		
		if(s>0)
		{	boolean m = getMode();
			if(m)
				message = message + MESSAGE_UNIT_PERCENT;
			else
				message = message + MESSAGE_UNIT_MS;
		}
		return message;
	}
	
	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Font used for drawing */
	private final Font FONT = new Font("Dialog", Font.PLAIN, 18);
	/** Drawn text */
	private final String TEXT_ALL = "All";
	/** Drawn text */
	private final String TEXT_ENGINE = "Engine";
	/** Drawn text */
	private final String TEXT_AGENTS = "AIs";
	/** Drawn text */
	private final String ITEM_ENGINE = "Engine";
	/** Drawn text */
	private final String ITEM_SWING = "Swing";
	/** Bars width */
	private final int WIDTH = 200;
	/** X location */
    private Integer x = 10;
    /** Y location */
    private Integer y = null;
	/** Vertical offset */
	private final int V_OFFSET = 120;
   

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
					text = TEXT_ALL;
					break;
				case 2:
					text = TEXT_ENGINE;
					break;
				case 3:
					text = TEXT_AGENTS;
					break;
			}
			FontMetrics metrics = g.getFontMetrics(FONT);
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
			y = V_OFFSET;
	        
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
			{	widths[i] = (int)(WIDTH*values[i]);
				usedWidth = usedWidth + widths[i];
			}
	        widths[0] = WIDTH - usedWidth;
	        
	        // draw the diagram
			g.setColor(new Color(0,0,0,50));
			g.fillRect(x,y-boxHeight/2,WIDTH,height);
			g.setColor(Color.BLACK);
			g.drawRect(x+1,y-boxHeight/2+1,WIDTH,height);
			int offset = 0;
			for(int i=0;i<colors.size();i++)
			{	int w = widths[i];
				Color color = colors.get(i);
				g.setColor(color);
				g.fillRect(x+offset,y-boxHeight/2,w,height);
				offset = offset + w;
			}
			g.setColor(Color.WHITE);
			g.drawRect(x,y-boxHeight/2,WIDTH,height);
			
			// add the main text
			g.setColor(new Color(255,255,255,50));
			g.setFont(FONT);
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
								text = ITEM_SWING; 
								break;
							case 1:
								text = ITEM_ENGINE;
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

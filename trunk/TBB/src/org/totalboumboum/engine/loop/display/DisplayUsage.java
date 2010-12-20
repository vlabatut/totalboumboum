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
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;
import org.totalboumboum.engine.player.AbstractPlayer;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class DisplayUsage implements Display
{
	public DisplayUsage(VisibleLoop loop)
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
	// EVENT NAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String getEventName()
	{	return SystemControlEvent.SWITCH_DISPLAY_USAGE;
	}

	/////////////////////////////////////////////////////////////////
	// MONITOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
/*	private ThreadMXBean tmxb = null;
	private HashMap<Long,PredefinedColor> threadIds = null;
	private List<PredefinedColor> colors = null;
	
	private void initMonitor()
	{	// init color list
		colors = new ArrayList<PredefinedColor>();
		List<AbstractPlayer> players = loop.getPlayers();
		for(AbstractPlayer player: players)
			colors.add(player.getColor());
		
		// init thread map
		tmxb = ManagementFactory.getThreadMXBean();
		threadIds = new HashMap<Long,PredefinedColor>();
		long ids[] = tmxb.getAllThreadIds();
		ThreadInfo[] infos = tmxb.getThreadInfo(ids);
		for(ThreadInfo info: infos)
		{	String name = info.getThreadName();
			long id = info.getThreadId();
			String[] temp = name.split(":");
			if(temp.length>1)
			{	PredefinedColor color = PredefinedColor.valueOf(temp[0]);
				threadIds.put(id,color);
			}
		}
	}
*/	
	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void draw(Graphics g)
	{	int s = getShow();
	
/*		if(s)
		{	// possibly init the monitor
			if(tmxb==null)
				initMonitor();
			
			// retrieve cpu times
			long[] tids = tmxb.getAllThreadIds();
	        ThreadInfo[] tinfos = tmxb.getThreadInfo(tids);
	        HashMap<PredefinedColor,Long> values = new HashMap<PredefinedColor,Long>();
	        float totalTime = 0;
	        for(int i=0;i<tids.length;i++)
	        {	long id = tids[i];
	        	ThreadInfo info = tinfos[i];
	        	long cpuTime = tmxb.getThreadCpuTime(id);
	            // focus only on running thread
	            if(cpuTime!=-1 && info!=null)
	            {	// separate ai-related threads from other ones
	            	PredefinedColor color = threadIds.get(id);
	            	if(color!=null)
	            		values.put(color,cpuTime);
	            	totalTime = totalTime + cpuTime;
	            }
	        }
			
	        // process the diagram dims
			int height = 25;
			int width = 200;
			int widths[] = new int[colors.size()+1];
			int totalWidth = 0;
			for(int i=0;i<colors.size();i++)
			{	PredefinedColor color = colors.get(i);
				Long cpuTime = values.get(color);
				if(cpuTime==null)
					cpuTime = 0l;
				widths[i+1] = (int)(width*cpuTime/totalTime);
				totalWidth = totalWidth + widths[i+1];
			}
	        widths[0] = width - (int)totalWidth;
	        
	        // draw the diagram
	        int y = 50;
	        int x = 10;
			int offset = widths[0];
			for(int i=0;i<colors.size();i++)
			{	int w = widths[i+1];
				PredefinedColor color = colors.get(i);
				g.setColor(color.getColor());
				g.fillRect(x+offset,y,w,height);
				offset = offset + w;
			}
			g.setColor(Color.BLACK);
			g.drawRect(x,y,width,height);
		}
*/		
		if(s>0)
		{	// retrieve CPU usage
			double[] values0 = loop.getAverageCpu();
			
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
					values = values0;
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
					{	double total = 1 - values0[0] - values0[1];
						for(int i=1;i<values0.length;i++)
							values[i] = values0[i]/total;
						values[0] = 0;
						values[1] = 0;
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
			if(getMode())
			{	NumberFormat nf = NumberFormat.getPercentInstance();
				nf.setMinimumIntegerDigits(2);
				nf.setMinimumFractionDigits(4);
				nf.setMaximumFractionDigits(4);
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
						text = nf.format(values[i])+" ["+text+"]";
						// process size and location
						box = metrics.getStringBounds(text,g);
						boxHeight = (int)box.getHeight();
						yText = (int)Math.round(y+boxHeight/2-metrics.getDescent());
						// draw
						g.setColor(new Color(0,0,0,150));
						g.drawString(text,x+1,yText+1);
						g.setColor(colors.get(i));
						g.drawString(text,x,yText);
						// increase offset
						y = y + boxHeight;
					}
				}
				//"CPU:AIs="+
			}
		}
	}
}

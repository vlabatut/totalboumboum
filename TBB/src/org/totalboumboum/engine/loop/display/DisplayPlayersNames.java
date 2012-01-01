package org.totalboumboum.engine.loop.display;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;
import org.totalboumboum.engine.player.AbstractPlayer;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class DisplayPlayersNames implements Display
{
	public DisplayPlayersNames(VisibleLoop loop)
	{	this.loop = loop;
		this.players = loop.getPlayers();
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private VisibleLoop loop;
	private List<AbstractPlayer> players;	
	
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
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String getMessage(SystemControlEvent event)
	{	String message = null;
		if(getShow())
			message = "Display the players' names";
		else
			message = "Hide the players' names";
		return message;
	}
	
	/////////////////////////////////////////////////////////////////
	// EVENT NAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<String> eventNames = new ArrayList<String>(Arrays.asList(SystemControlEvent.SWITCH_DISPLAY_PLAYERS_NAMES));
	
	@Override
	public List<String> getEventNames()
	{	return eventNames;
	}

	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void draw(Graphics g)
	{	if(getShow())
		{	//Graphics2D g2 = (Graphics2D) g;
			Font font = new Font("Dialog",Font.BOLD,12);
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics(font);
			for(int i=0;i<players.size();i++)
			{	AbstractPlayer player = players.get(i);
				if(!player.isOut())
				{	Sprite s = player.getSprite();
					double posX = s.getCurrentPosX();
					double posY = s.getCurrentPosY();
					Color color = player.getColor().getColor();
					
					// process name size
					String nameText = "["+(i+1)+"] "+player.getName();
					Rectangle2D nameBox = metrics.getStringBounds(nameText,g);
					double nameBoxWidth = nameBox.getWidth();
					double nameBoxHeight = nameBox.getHeight();
					double xMargin = nameBoxWidth/15;
					double yMargin = nameBoxHeight/5;
					
					// process usage size
					NumberFormat nf = NumberFormat.getPercentInstance();
					nf.setMinimumIntegerDigits(2);
					nf.setMaximumFractionDigits(2);
					String usageText = nf.format(loop.getAverageCpuProportions()[i+2]);
					//System.out.println(loop.getAverageCpu()[i+1]);					
					Rectangle2D usageBox = metrics.getStringBounds(usageText,g);
					double usageBoxWidth = usageBox.getWidth();
					double usageBoxHeight = usageBox.getHeight();
					
					// display name
					{	int x = (int)Math.round(posX-nameBoxWidth/2);
						int y = (int)Math.round(posY-2*yMargin-metrics.getDescent());
						Color rectangleColor = new Color(255,255,255,100);
						g.setColor(rectangleColor);
						int arcDim = (int)Math.round(nameBoxWidth/10);
						int rectangleWidth = (int)Math.round(nameBoxWidth+2*xMargin);
						int rectangleHeight = (int)Math.round(nameBoxHeight);
						int rx = (int)Math.round(posX-rectangleWidth/2);
						int ry = (int)Math.round(posY-2*yMargin-rectangleHeight);
						g.fillRoundRect(rx,ry,rectangleWidth,rectangleHeight,arcDim,arcDim);
						g.setColor(Color.BLACK);
						g.drawString(nameText,x+1,y+1);
						g.setColor(color);
						g.drawString(nameText,x,y);
					}
					
					// draw CPU occupation
					{	int x = (int)Math.round(s.getCurrentPosX()-usageBoxWidth/2);
						int y = (int)Math.round(s.getCurrentPosY()+usageBoxHeight-metrics.getDescent());
						g.setColor(Color.BLACK);
						g.drawString(usageText,x+1,y+1);
						g.setColor(color);
						g.drawString(usageText,x,y);
					}
				}
			}
		}
	}
}

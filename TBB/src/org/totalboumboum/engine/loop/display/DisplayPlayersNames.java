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
	{	this.players = loop.getPlayers();
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
	// EVENT NAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String getEventName()
	{	return SystemControlEvent.SWITCH_DISPLAY_PLAYERS_NAMES;
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
				{	String text = "["+(i+1)+"] "+player.getName();
					Sprite s = player.getSprite();
					Rectangle2D box = metrics.getStringBounds(text,g);
					double boxWidth = box.getWidth();
					double boxHeight = box.getHeight();
					int x = (int)Math.round(s.getCurrentPosX()-boxWidth/2);
					int y = (int)Math.round(s.getCurrentPosY()+boxHeight/2-metrics.getDescent());
					Color rectangleColor = new Color(255,255,255,100);
					g.setColor(rectangleColor);
					int arcDim = (int)Math.round(boxWidth/10);
					double xMargin = boxWidth/15;
					double yMargin = boxHeight/5;
					int rectangleWidth = (int)Math.round(boxWidth+2*xMargin);
					int rectangleHeight = (int)Math.round(boxHeight+2*yMargin);
					int rx = (int)Math.round(s.getCurrentPosX()-rectangleWidth/2);
					int ry = (int)Math.round(s.getCurrentPosY()-rectangleHeight/2);
					g.fillRoundRect(rx,ry,rectangleWidth,rectangleHeight,arcDim,arcDim);
					g.setColor(Color.BLACK);
					g.drawString(text,x+1,y+1);
					Color color = player.getColor().getColor();
					g.setColor(color);
					g.drawString(text,x,y);
				}
			}
		}
	}
}

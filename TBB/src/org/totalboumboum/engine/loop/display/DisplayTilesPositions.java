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

import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.loop.event.control.ControlEvent;

public class DisplayTilesPositions implements Display
{
	public DisplayTilesPositions(VisibleLoop loop)
	{	this.level = loop.getLevel();
		this.globalHeight = level.getGlobalHeight();
		this.globalWidth = level.getGlobalWidth();
	}

	/////////////////////////////////////////////////////////////////
	// LOOP				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Level level;
	private int globalHeight;
	private int globalWidth;
	
	/////////////////////////////////////////////////////////////////
	// SHOW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int show = 0;
	
	@Override
	public synchronized void switchShow(ControlEvent event)
	{	show = (show+1)%4;
	}
	
	private synchronized int getShow()
	{	return show;
	}

	/////////////////////////////////////////////////////////////////
	// EVENT NAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String getEventName()
	{	return ControlEvent.SWITCH_DISPLAY_TILES_POSITIONS;
		
	}

	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void draw(Graphics g)
	{	int s = getShow();
		// positions expressed in tiles
		if(s==1)
		{	g.setColor(Color.CYAN);
			Font font = new Font("Dialog", Font.PLAIN, 12);
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics(font);
			for(int line=0;line<globalHeight;line++)
			{	for(int col=0;col<globalWidth;col++)
				{	Tile temp = level.getTile(line,col);
					String text = "("+line+","+col+")";
					Rectangle2D box = metrics.getStringBounds(text, g);
					int x = (int)Math.round(temp.getPosX()-box.getWidth()/2);
					int y = (int)Math.round(temp.getPosY()+box.getHeight()/2);
					g.drawString(text, x, y);
				}
			}
		}
		// positions expressed in pixels
		else if(s==2)
		{	// coordonnées
			g.setColor(Color.CYAN);
			Font font = new Font("Dialog", Font.PLAIN, 12);
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics(font);
			for(int line=0;line<globalHeight;line++)
			{	for(int col=0;col<globalWidth;col++)
				{	Tile temp = level.getTile(line,col);
					String textX = Double.toString(temp.getPosX());
					String textY = Double.toString(temp.getPosY());
					Rectangle2D boxX = metrics.getStringBounds(textX, g);
					Rectangle2D boxY = metrics.getStringBounds(textY, g);
					int x = (int)Math.round(temp.getPosX()-boxX.getWidth()/2);
					int y = (int)Math.round(temp.getPosY());
					g.drawString(textX, x, y);
					x = (int)Math.round(temp.getPosX()-boxY.getWidth()/2);
					y = (int)Math.round(temp.getPosY()+boxY.getHeight());
					g.drawString(textY, x, y);
				}
			}
		}
	}
}

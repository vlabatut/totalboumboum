package org.totalboumboum.engine.loop.display.position;

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

import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.loop.display.Display;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;

/**
 * Displays the position of
 * all tiles constituting the level.
 * 
 * @author Vincent Labatut
 */
public class DisplayTilesPositions extends Display
{
	/**
	 * Builds a standard display object.
	 * 
	 * @param loop
	 * 		Object used for displaying.
	 */
	public DisplayTilesPositions(VisibleLoop loop)
	{	this.level = loop.getLevel();
		this.globalHeight = level.getGlobalHeight();
		this.globalWidth = level.getGlobalWidth();
		
		eventNames.add(SystemControlEvent.SWITCH_DISPLAY_TILES_POSITIONS);
	}

	/////////////////////////////////////////////////////////////////
	// DATA				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Current level */ 
	private Level level;
	/** Height of the current level (in tiles) */
	private int globalHeight;
	/** Width of the current level (in tiles) */
	private int globalWidth;
	
	/////////////////////////////////////////////////////////////////
	// SHOW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether the information should be displayed or not */
	private boolean show = false;
	/** Indicates how the information should be displayed */
	private boolean mode = true;
	
	@Override
	public synchronized void switchShow(SystemControlEvent event)
	{	if(event.getIndex()==SystemControlEvent.REGULAR)
			show = !show;
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
	private synchronized boolean getShow()
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
	private final String MESSAGE_DISPLAY = "Display tile coordinates";
	/** Display message */
	private final String MESSAGE_UNIT_TILES = " (in tiles)";
	/** Display message */
	private final String MESSAGE_UNIT_PIXELS = " (in pixels)";
	/** Hide message */
	private final String MESSAGE_HIDE = "Hide tile coordinates";

	@Override
	public String getMessage(SystemControlEvent event)
	{	String message = null;
		if(getShow())
		{	message = MESSAGE_DISPLAY;
			if(getMode())
				message = message + MESSAGE_UNIT_TILES;
			else
				message = message + MESSAGE_UNIT_PIXELS;
		}
		else
			message = MESSAGE_HIDE;
	
		return message;
	}
	
	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Font used for drawing */
	private final Font FONT = new Font("Dialog", Font.PLAIN, 12);
	
	@Override
	public void draw(Graphics g)
	{	boolean s = getShow();
		boolean m = getMode();
		if(s)
		{	// positions expressed in tiles
			if(m)
			{	g.setFont(FONT);
				FontMetrics metrics = g.getFontMetrics(FONT);
				for(int row=0;row<globalHeight;row++)
				{	for(int col=0;col<globalWidth;col++)
					{	Tile temp = level.getTile(row,col);
						String text = "("+row+","+col+")";
						Rectangle2D box = metrics.getStringBounds(text, g);
						int x = (int)Math.round(temp.getPosX()-box.getWidth()/2);
						int y = (int)Math.round(temp.getPosY()+box.getHeight()/2);
						g.setColor(Color.BLACK);
						g.drawString(text,x+1,y+1);
						g.setColor(Color.CYAN);
						g.drawString(text,x,y);
					}
				}
			}
			
			// positions expressed in pixels
			else
			{	// coordonnées
				g.setFont(FONT);
				FontMetrics metrics = g.getFontMetrics(FONT);
				for(int row=0;row<globalHeight;row++)
				{	for(int col=0;col<globalWidth;col++)
					{	Tile temp = level.getTile(row,col);
						String textX = Double.toString(temp.getPosX());
						String textY = Double.toString(temp.getPosY());
						Rectangle2D boxX = metrics.getStringBounds(textX, g);
						Rectangle2D boxY = metrics.getStringBounds(textY, g);
						int x = (int)Math.round(temp.getPosX()-boxX.getWidth()/2);
						int y = (int)Math.round(temp.getPosY());
						g.setColor(Color.BLACK);
						g.drawString(textX,x+1,y+1);
						g.setColor(Color.CYAN);
						g.drawString(textX,x,y);
						x = (int)Math.round(temp.getPosX()-boxY.getWidth()/2);
						y = (int)Math.round(temp.getPosY()+boxY.getHeight());
						g.setColor(Color.BLACK);
						g.drawString(textY,x+1,y+1);
						g.setColor(Color.CYAN);
						g.drawString(textY,x,y);
					}
				}
			}
		}
	}
}

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
import java.awt.Graphics;

import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;
import org.totalboumboum.game.round.RoundVariables;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class DisplayGrid implements Display
{
	public DisplayGrid(VisibleLoop loop)
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
			message = "Display grid";
		else
			message = "Hide grid";
		return message;
	}
	
	/////////////////////////////////////////////////////////////////
	// EVENT NAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String getEventName()
	{	return SystemControlEvent.SWITCH_DISPLAY_GRID;
	}

	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void draw(Graphics g)
	{	if(getShow())
		{	g.setColor(Color.CYAN);
			// croix					
//			g.drawLine((int)posX, 0, (int)posX, configuration.getPanelDimensionY());
//			g.drawLine(0,(int)posY, configuration.getPanelDimensionX(), (int)posY);
			// grille
			for(int row=0;row<globalHeight;row++)
			{	for(int col=0;col<globalWidth;col++)
				{	Tile temp = level.getTile(row,col);
					g.drawLine((int)temp.getPosX(), (int)temp.getPosY(), (int)temp.getPosX(), (int)temp.getPosY());
					g.drawRect((int)(temp.getPosX()-RoundVariables.scaledTileDimension/2), (int)(temp.getPosY()-RoundVariables.scaledTileDimension/2), (int)RoundVariables.scaledTileDimension, (int)RoundVariables.scaledTileDimension);
				}
			}
		}
	}
}

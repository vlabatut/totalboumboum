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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.AbstractAiManager;
import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.loop.Loop;
import org.totalboumboum.engine.loop.ServerLoop;
import org.totalboumboum.engine.loop.event.control.ControlEvent;
import org.totalboumboum.engine.player.Player;
import org.totalboumboum.game.round.RoundVariables;

public class DisplayAisColors implements Display
{
	public DisplayAisColors(ServerLoop loop)
	{	this.players = loop.getPlayers();
		this.level = loop.getLevel();
	}
	
	/////////////////////////////////////////////////////////////////
	// LOOP				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<Player> players;
	private Level level;
	
	/////////////////////////////////////////////////////////////////
	// SHOW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<Boolean> show = new ArrayList<Boolean>();

	@Override
	public synchronized void switchShow(ControlEvent event)
	{	int index = event.getIndex();
		if(index<show.size())
		{	boolean temp = show.get(index);
			if(players.get(index).getArtificialIntelligence()!=null)
				temp = !temp;
			show.set(index,temp);
		}
	}
	
	private synchronized boolean getShow(int index)
	{	return show.get(index);		
	}

	/////////////////////////////////////////////////////////////////
	// EVENT NAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String getEventName()
	{	return ControlEvent.SWITCH_DISPLAY_AIS_COLORS;
		
	}

	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void draw(Graphics g)
	{	Graphics2D g2 = (Graphics2D)g;
		double tileSize = RoundVariables.scaledTileDimension;
		for(int i=0;i<players.size();i++)
		{	Player player = players.get(i);
			if(player.hasAi())
			{	AbstractAiManager<?> aiMgr = player.getArtificialIntelligence();
				// tile colors
				if(getShow(i))
				{	Color[][] colors = aiMgr.getTileColors();
					for(int line=0;line<level.getGlobalHeight();line++)
					{	for(int col=0;col<level.getGlobalWidth();col++)
						{	Color color = colors[line][col];
							if(color!=null)
							{	Color paintColor = new Color(color.getRed(),color.getGreen(),color.getBlue(),Loop.INFO_ALPHA_LEVEL);
								g2.setPaint(paintColor);
								Tile tile = level.getTile(line,col);
								double x = tile.getPosX()-tileSize/2;
								double y = tile.getPosY()-tileSize/2;
								g2.fill(new Rectangle2D.Double(x,y,tileSize,tileSize));
							}
						}
					}
				}
			}
		}
	}
}

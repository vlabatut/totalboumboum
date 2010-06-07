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
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.AbstractAiManager;
import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.loop.ServerLoop;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.engine.player.AiPlayer;

public class DisplayAisTexts implements Display
{
	public DisplayAisTexts(ServerLoop loop)
	{	this.players = loop.getPlayers();
		this.level = loop.getLevel();
		
		for(int i=0;i<players.size();i++)
			show.add(false);
	}
	
	/////////////////////////////////////////////////////////////////
	// LOOP				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<AbstractPlayer> players;
	private Level level;
	
	/////////////////////////////////////////////////////////////////
	// SHOW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<Boolean> show = new ArrayList<Boolean>();

	@Override
	public synchronized void switchShow(SystemControlEvent event)
	{	int index = event.getIndex();
		if(index<show.size())
		{	boolean temp = show.get(index);
			if(players.get(index) instanceof AiPlayer)
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
	{	return SystemControlEvent.SWITCH_DISPLAY_AIS_TEXTS;
	}

	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void draw(Graphics g)
	{	for(int i=0;i<players.size();i++)
		{	AbstractPlayer player = players.get(i);
			if(player instanceof AiPlayer)
			{	AbstractAiManager<?> aiMgr = ((AiPlayer)player).getArtificialIntelligence();
				// tile texts
				if(getShow(i))
				{	g.setColor(Color.MAGENTA);
					Font font = new Font("Dialog", Font.PLAIN, 15);
					g.setFont(font);
					FontMetrics metrics = g.getFontMetrics(font);
					String[][] texts = aiMgr.getTileTexts();
					for(int line=0;line<level.getGlobalHeight();line++)
					{	for(int col=0;col<level.getGlobalWidth();col++)
						{	String text = texts[line][col];
							if(text!=null)
							{	Tile tile = level.getTile(line,col);
								Rectangle2D box = metrics.getStringBounds(text, g);
								int x = (int)Math.round(tile.getPosX()-box.getWidth()/2);
								int y = (int)Math.round(tile.getPosY()+box.getHeight()/2);
								g.drawString(text, x, y);
							}
						}
					}
				}
			}
		}
	}
}

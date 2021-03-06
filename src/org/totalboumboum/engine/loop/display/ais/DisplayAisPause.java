package org.totalboumboum.engine.loop.display.ais;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.loop.InteractiveLoop;
import org.totalboumboum.engine.loop.display.Display;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.engine.player.AiPlayer;

/**
 * Displays a message indicating
 * an artificial agent is paused.
 * 
 * @author Vincent Labatut
 */
public class DisplayAisPause extends Display
{
	/**
	 * Builds a standard display object.
	 * 
	 * @param loop
	 * 		Object used for displaying.
	 */
	public DisplayAisPause(InteractiveLoop loop)
	{	this.loop = loop;
		this.players = loop.getPlayers();
		
		for(int i=0;i<players.size();i++)
			show.add(false);

		eventNames.add(SystemControlEvent.SWITCH_AIS_PAUSE);
	}
	
	/////////////////////////////////////////////////////////////////
	// DATA				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Object used for displaying */
	private InteractiveLoop loop;
	/** List of players involved in the game */
	private List<AbstractPlayer> players;
	
	/////////////////////////////////////////////////////////////////
	// SHOW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates which information should be displayed */
	private final List<Boolean> show = new ArrayList<Boolean>();

	@Override
	public void switchShow(SystemControlEvent event)
	{	int index = event.getIndex();
		if(index<show.size())
		{	boolean temp = show.get(index);
			AbstractPlayer player = players.get(index);
			if(player instanceof AiPlayer)
			{	// switch
				temp = !(loop.getAiPause(index) && !player.isOut());
				show.set(index,temp);
				
				// message
				if(temp)
					message = MESSAGE_PAUSE + (index+1);
				else
					message = MESSAGE_UNPAUSE + (index+1);
			}
			else
				message = null;
		}
		else
			message = null;
	}
	
	/**
	 * Returns the value indicating which
	 * information should be displayed,
	 * for the specified player.
	 * 
	 * @param index
	 * 		Concerned player.
	 * @return
	 * 		Value indicating which information should be displayed.
	 */
	private synchronized boolean getShow(int index)
	{	return show.get(index);		
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Display message */
	private final String MESSAGE_PAUSE = "Pause player #";
	/** Hide message */
	private final String MESSAGE_UNPAUSE = "Unpause player #";
	/** Current message */
	private String message = null;
	
	@Override
	public String getMessage(SystemControlEvent event)
	{	return message;
	}
	
	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void draw(Graphics g)
	{	Font font = new Font("Dialog", Font.BOLD, 12);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		String text = "AI Paused";
		Rectangle2D box = metrics.getStringBounds(text, g);
		for(int i=0;i<players.size();i++)
		{	AbstractPlayer player = players.get(i);
			Color color = player.getColor().getColor();
			if(getShow(i))
			{	Sprite s = player.getSprite();
				int x = (int)Math.round(s.getCurrentPosX()-box.getWidth()/2);
				int y = (int)Math.round(s.getCurrentPosY()+box.getHeight()/2);
				g.setColor(Color.BLACK);
				g.drawString(text,x+1,y+1);
				g.setColor(color);
				g.drawString(text,x,y);
			}
		}
	}
}

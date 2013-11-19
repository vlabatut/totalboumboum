package org.totalboumboum.engine.loop.display.ais;

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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.AiAbstractManager;
import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.loop.InteractiveLoop;
import org.totalboumboum.engine.loop.Loop;
import org.totalboumboum.engine.loop.display.Display;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.engine.player.AiPlayer;
import org.totalboumboum.game.round.RoundVariables;

/**
 * Displays the colors set
 * by an artificial agent.
 * 
 * @author Vincent Labatut
 */
public class DisplayAisColors extends Display
{
	/**
	 * Builds a standard display object.
	 * 
	 * @param loop
	 * 		Object used for displaying.
	 */
	public DisplayAisColors(InteractiveLoop loop)
	{	this.players = loop.getPlayers();
		this.level = loop.getLevel();
		
		for(int i=0;i<players.size();i++)
			show.add(false);
		
		eventNames.add(SystemControlEvent.SWITCH_DISPLAY_AIS_COLORS);
	}
	
	/////////////////////////////////////////////////////////////////
	// DATA				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List of players involved in the game */
	private List<AbstractPlayer> players;
	/** Current level */
	private Level level;
	
	/////////////////////////////////////////////////////////////////
	// SHOW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates which information should be displayed */
	private final List<Boolean> show = new ArrayList<Boolean>();

	@Override
	public synchronized void switchShow(SystemControlEvent event)
	{	int index = event.getIndex();
		if(index<show.size())
		{	boolean temp = show.get(index);
			if(players.get(index) instanceof AiPlayer)
			{	// switch
				temp = !temp;
				show.set(index,temp);
				
				// message
				if(temp)
					message = MESSAGE_DISPLAY + (index+1);
				else
					message = MESSAGE_HIDE + (index+1);
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
	private final String MESSAGE_DISPLAY = "Display colored tiles for player #";
	/** Hide message */
	private final String MESSAGE_HIDE = "Hide colored tiles for player #";
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
	{	Graphics2D g2 = (Graphics2D)g;
		double tileSize = RoundVariables.scaledTileDimension;
		for(int i=0;i<players.size();i++)
		{	AbstractPlayer player = players.get(i);
			if(player instanceof AiPlayer)
			{	AiAbstractManager<?,?> aiMgr = ((AiPlayer)player).getArtificialIntelligence();
				// tile colors
				if(getShow(i))
				{	List<Color>[][] colors = aiMgr.getTileColors();
					for(int row=0;row<level.getGlobalHeight();row++)
					{	for(int col=0;col<level.getGlobalWidth();col++)
						{	for(Color color: colors[row][col])
							{	if(color!=null)
								{	Color paintColor = new Color(color.getRed(),color.getGreen(),color.getBlue(),Loop.INFO_ALPHA_LEVEL);
									g2.setPaint(paintColor);
									Tile tile = level.getTile(row,col);
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
}

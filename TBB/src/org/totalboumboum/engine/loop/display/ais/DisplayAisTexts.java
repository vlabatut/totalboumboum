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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.AiAbstractManager;
import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.loop.InteractiveLoop;
import org.totalboumboum.engine.loop.display.Display;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.engine.player.AiPlayer;

/**
 * Displays the texts set
 * by an artificial agent.
 * 
 * @author Vincent Labatut
 */
public class DisplayAisTexts extends Display
{
	/**
	 * Builds a standard display object.
	 * 
	 * @param loop
	 * 		Object used for displaying.
	 */
	public DisplayAisTexts(InteractiveLoop loop)
	{	this.players = loop.getPlayers();
		this.level = loop.getLevel();
		
		for(int i=0;i<players.size();i++)
			show.add(false);
		
		eventNames.add(SystemControlEvent.SWITCH_DISPLAY_AIS_TEXTS);
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
	private final String MESSAGE_DISPLAY = "Display texts for player #";
	/** Hide message */
	private final String MESSAGE_HIDE = "Hide texts for player #";
	/** Current message */
	private String message = null;
	
	@Override
	public String getMessage(SystemControlEvent event)
	{	return message;
	}
	
	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Drawn text */
	private final String TEXT_NULL = "null";
	
	@Override
	public void draw(Graphics g)
	{	for(int i=0;i<players.size();i++)
		{	AbstractPlayer player = players.get(i);
			if(player instanceof AiPlayer)
			{	AiAbstractManager<?> aiMgr = ((AiPlayer)player).getArtificialIntelligence();
				Color color = player.getColor().getColor();
				// tile texts
				if(getShow(i))
				{	int type = Font.PLAIN;
					boolean bold = aiMgr.isBold();
					if(bold)
						type = Font.BOLD;
					Font font = new Font("Dialog",type,9);//TODO was 15
					g.setFont(font);
					FontMetrics metrics = g.getFontMetrics(font);
					List<String>[][] texts = aiMgr.getTileTexts();
					for(int row=0;row<level.getGlobalHeight();row++)
					{	for(int col=0;col<level.getGlobalWidth();col++)
						{	Tile tile = level.getTile(row,col);
							List<String> textList = texts[row][col];
							if(textList!=null && !textList.isEmpty())
							{	List<Integer> xList = new ArrayList<Integer>();
								List<Integer> boxHeights = new ArrayList<Integer>();
								double total = 0;
								for(int s=0;s<textList.size();s++)
								{	String text = textList.get(s);
									if(text==null)
										text = TEXT_NULL;
									Rectangle2D box = metrics.getStringBounds(text,g);
									int boxHeight = (int)Math.round(box.getHeight());
									boxHeights.add(boxHeight);
									int x = (int)Math.round(tile.getPosX()-box.getWidth()/2);
									xList.add(x);
									total = total + box.getHeight();
								}
								int y = (int)Math.round(tile.getPosY()+total/2);
								for(int s=textList.size()-1;s>=0;s--)
								{	int x = xList.get(s);
									String text = textList.get(s);
									g.setColor(Color.BLACK);
									g.drawString(text,x+1,y+1);
									g.setColor(color);
									g.drawString(text,x,y);
									int boxHeight = boxHeights.get(s);
									y = y - boxHeight;
								}
							}
						}
					}
				}
			}
		}
	}
}

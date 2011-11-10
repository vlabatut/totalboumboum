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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.block.Block;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;
import org.totalboumboum.engine.content.sprite.fire.Fire;
import org.totalboumboum.engine.content.sprite.floor.Floor;
import org.totalboumboum.engine.content.sprite.hero.Hero;
import org.totalboumboum.engine.content.sprite.item.Item;
import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class DisplaySpritesPositions implements Display
{
	public DisplaySpritesPositions(VisibleLoop loop)
	{	this.level = loop.getLevel();
		this.sprites = level.getSprites();	
	}

	/////////////////////////////////////////////////////////////////
	// LOOP				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Level level;
	private List<Sprite> sprites;
	
	/////////////////////////////////////////////////////////////////
	// SHOW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int show = 0;
	private boolean mode = true;
	
	@Override
	public synchronized void switchShow(SystemControlEvent event)
	{	if(event.getIndex()==SystemControlEvent.REGULAR)
			show = (show+1)%7;
		else
			mode = !mode;
	}
	
	private synchronized int getShow()
	{	return show;
	}
	
	private synchronized boolean getMode()
	{	return mode;
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String getMessage(SystemControlEvent event)
	{	String message = null;
		int s = getShow();
		switch(s)
		{	case 0: 
				message = "Hide all sprites' coordinates";
				break;
			case 1: 
				message = "Show only heroes' coordinates";
				break;
			case 2: 
				message = "Show only blocks' coordinates";
				break;
			case 3: 
				message = "Show only bomb' coordinates";
				break;
			case 4: 
				message = "Show only items' coordinates";
				break;
			case 5: 
				message = "Show only fires' coordinates";
				break;
			case 6: 
				message = "Show all sprites' coordinates";
				break;
		}
		
		boolean m = getMode();
		if(s>0)
		{	if(m)
				message = message + " (in tiles)";
			else
				message = message + " (in pixels)";
		}
	
		return message;
	}
	
	/////////////////////////////////////////////////////////////////
	// EVENT NAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<String> eventNames = new ArrayList<String>(Arrays.asList(SystemControlEvent.SWITCH_DISPLAY_SPRITES_POSITIONS));
	
	@Override
	public List<String> getEventNames()
	{	return eventNames;
	}

	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void draw(Graphics g)
	{	int s = getShow();
		boolean m = getMode();
		if(s>0)
		{	for(Sprite sprite: sprites)
			{	boolean go = false;
				Color color = null;
				if(sprite instanceof Floor)
					go = false;
				if(sprite instanceof Hero)
				{	color = Color.WHITE;
					go = s==1 || s==6;
				}
				else if(sprite instanceof Block)
				{	color = Color.GRAY;
					go = s==2 || s==6;
				}
				else if(sprite instanceof Bomb)
				{	color = Color.WHITE;
					go = s==3 || s==6;
				}
				else if(sprite instanceof Item)
				{	color = Color.WHITE;
					go = s==4 || s==6;
				}
				else if(sprite instanceof Fire)
				{	color = Color.BLACK;
					go = s==5 || s==6;
				}
				if(go)
				{	if(m)
					{	// coordonnées
						Tile tile = sprite.getTile();
						int row = tile.getRow();
						int col = tile.getCol();
						Font font = new Font("Dialog", Font.BOLD, 12);
						g.setFont(font);
						FontMetrics metrics = g.getFontMetrics(font);
						String text = "("+row+","+col+")";
						Rectangle2D box = metrics.getStringBounds(text, g);
						int x = (int)Math.round(sprite.getCurrentPosX()-box.getWidth()/2);
						int y = (int)Math.round(sprite.getCurrentPosY()+box.getHeight()/2);
						g.setColor(Color.BLACK);
						g.drawString(text,x+1,y+1);
						g.setColor(color);
						g.drawString(text,x,y);
					}
					else
					{	// coordonnées
						Font font = new Font("Dialog", Font.BOLD, 12);
						g.setFont(font);
						FontMetrics metrics = g.getFontMetrics(font);
						DecimalFormat nf = new DecimalFormat("000.00") ;
						String textX = nf.format(sprite.getCurrentPosX());
						String textY = nf.format(sprite.getCurrentPosY());
						String textZ = nf.format(sprite.getCurrentPosZ());
						Rectangle2D boxX = metrics.getStringBounds(textX, g);
						Rectangle2D boxY = metrics.getStringBounds(textY, g);
						Rectangle2D boxZ = metrics.getStringBounds(textZ, g);
						int x = (int)Math.round(sprite.getCurrentPosX()-boxX.getWidth()/2);
						int y = (int)Math.round(sprite.getCurrentPosY()-boxY.getHeight()/2);
						g.setColor(Color.BLACK);
						g.drawString(textX,x+1,y+1);
						g.setColor(color);
						g.drawString(textX,x,y);
						x = (int)Math.round(sprite.getCurrentPosX()-boxY.getWidth()/2);
						y = (int)Math.round(sprite.getCurrentPosY()+boxY.getHeight()/2);
						g.setColor(Color.BLACK);
						g.drawString(textY,x+1,y+1);
						g.setColor(color);
						g.drawString(textY,x,y);
						x = (int)Math.round(sprite.getCurrentPosX()-boxZ.getWidth()/2);
						y = (int)Math.round(sprite.getCurrentPosY()+boxY.getHeight()/2+boxZ.getHeight());
						g.setColor(Color.BLACK);
						g.drawString(textZ,x+1,y+1);
						g.setColor(color);
						g.drawString(textZ,x,y);
					}
				}
			}
		}
	}
}

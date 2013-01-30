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
import java.text.DecimalFormat;
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
import org.totalboumboum.engine.loop.display.Display;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;

/**
 * Displays the position of
 * the sprites currently in the game.
 * 
 * @author Vincent Labatut
 */
public class DisplaySpritesPositions extends Display
{
	/**
	 * Builds a standard display object.
	 * 
	 * @param loop
	 * 		Object used for displaying.
	 */
	public DisplaySpritesPositions(VisibleLoop loop)
	{	this.level = loop.getLevel();
		this.sprites = level.getSprites();
		
		eventNames.add(SystemControlEvent.SWITCH_DISPLAY_SPRITES_POSITIONS);
	}

	/////////////////////////////////////////////////////////////////
	// DATA				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Current level */
	private Level level;
	/** Sprites of the current level */
	private List<Sprite> sprites;
	
	/////////////////////////////////////////////////////////////////
	// SHOW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates which information should be displayed*/
	private int show = 0;
	/** Indicates how the information should be displayed */
	private boolean mode = true;
	
	@Override
	public synchronized void switchShow(SystemControlEvent event)
	{	if(event.getIndex()==SystemControlEvent.REGULAR)
			show = (show+1)%7;
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
	private synchronized int getShow()
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
	private final String MESSAGE_DISPLAY_ALL = "Show all sprite coordinates";
	/** Display message */
	private final String MESSAGE_DISPLAY_ONLY_HEROES = "Show only hero coordinates";
	/** Display message */
	private final String MESSAGE_DISPLAY_ONLY_BLOCKS = "Show only block coordinates";
	/** Display message */
	private final String MESSAGE_DISPLAY_ONLY_BOMBS = "Show only bomb coordinates";
	/** Display message */
	private final String MESSAGE_DISPLAY_ONLY_ITEMS = "Show only item coordinates";
	/** Display message */
	private final String MESSAGE_DISPLAY_ONLY_FIRES = "Show only fire coordinates";
	/** Display message */
	private final String MESSAGE_UNIT_TILES = " (in tiles)";
	/** Display message */
	private final String MESSAGE_UNIT_PIXELS = " (in pixels)";
	/** Hide message */
	private final String MESSAGE_HIDE = "Hide all sprite coordinates";

	@Override
	public String getMessage(SystemControlEvent event)
	{	String message = null;
		
		int s = getShow();
		switch(s)
		{	case 0: 
				message = MESSAGE_HIDE;
				break;
			case 1: 
				message = MESSAGE_DISPLAY_ONLY_HEROES;
				break;
			case 2: 
				message = MESSAGE_DISPLAY_ONLY_BLOCKS;
				break;
			case 3: 
				message = MESSAGE_DISPLAY_ONLY_BOMBS;
				break;
			case 4: 
				message = MESSAGE_DISPLAY_ONLY_ITEMS;
				break;
			case 5: 
				message = MESSAGE_DISPLAY_ONLY_FIRES;
				break;
			case 6: 
				message = MESSAGE_DISPLAY_ALL;
				break;
		}
		
		if(s>0)
		{	boolean m = getMode();
			if(m)
				message = message + MESSAGE_UNIT_TILES;
			else
				message = message + MESSAGE_UNIT_PIXELS;
		}
	
		return message;
	}
	
	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Font used for drawing */
	private final Font FONT = new Font("Dialog", Font.BOLD, 12);
	
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
						g.setFont(FONT);
						FontMetrics metrics = g.getFontMetrics(FONT);
						String text = "("+row+","+col+")";
						Rectangle2D box = metrics.getStringBounds(text, g);
						int x = (int)Math.round(sprite.getCurrentPosX()-box.getWidth()/2);
						int y = (int)Math.round(sprite.getCurrentPosY()+box.getHeight()/2);
						// draw
						g.setColor(Color.BLACK);
						g.drawString(text,x+1,y+1);
						g.setColor(color);
						g.drawString(text,x,y);
					}
					else
					{	// coordonnées
						g.setFont(FONT);
						FontMetrics metrics = g.getFontMetrics(FONT);
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

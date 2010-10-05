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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.AbstractAiManager;
import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.loop.InteractiveLoop;
import org.totalboumboum.engine.loop.Loop;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.engine.player.AiPlayer;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.calculus.CalculusTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class DisplayAisPaths implements Display
{
	public DisplayAisPaths(InteractiveLoop loop)
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
	{	return SystemControlEvent.SWITCH_DISPLAY_AIS_PATHS;
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
			{	AbstractAiManager<?> aiMgr = ((AiPlayer)player).getArtificialIntelligence();
				if(getShow(i))
				{	List<List<Tile>> paths = aiMgr.getPaths();
					List<Color> colors = aiMgr.getPathColors();
					Stroke prevStroke = g2.getStroke();
					int thickness = (int)(tileSize/3);
					Stroke stroke = new BasicStroke(thickness,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
					g2.setStroke(stroke);
					for(int j=0;j<paths.size();j++)
					{	List<Tile> path = paths.get(j);
						Color color = colors.get(j);
						if(color!=null && !path.isEmpty())
						{	Color paintColor = new Color(color.getRed(),color.getGreen(),color.getBlue(),Loop.INFO_ALPHA_LEVEL);
							g2.setPaint(paintColor);
							Tile tile2 = path.get(0);
							double x1,x2 = tile2.getPosX();
							double y1,y2 = tile2.getPosY();
							Path2D shape = new Path2D.Double();
							shape.moveTo(x2,y2);
							int k = 1;
							while(k<path.size())
							{	// tiles
								x1 = x2;
								y1 = y2;
								tile2 = path.get(k);							
								x2 = tile2.getPosX();
								y2 = tile2.getPosY();
								// directions (to manage the case where the path cross the level off-scree)
								Direction direction12 = level.getDirection(x1,y1,x2,y2);
								int[] intDir12 = direction12.getIntFromDirection();
								Direction direction21 = direction12.getOpposite();
								int[] intDir21 = direction21.getIntFromDirection();
								// alternative locations
								double x1b = x2 + intDir21[0]*tileSize;
								double y1b = y2 + intDir21[1]*tileSize;
								double x2b = x1 + intDir12[0]*tileSize;
								double y2b = y1 + intDir12[1]*tileSize;
								// compare actual and theoretical positions
								if(!CalculusTools.isRelativelyEqualTo(x1,x1b) || !CalculusTools.isRelativelyEqualTo(y1,y1b))
								{	shape.lineTo(x2b,y2b);
									g2.draw(shape);
									shape = new Path2D.Double();
									shape.moveTo(x1b,y1b);
									shape.lineTo(x2,y2);
								}
								else
									shape.lineTo(x2,y2);
								k++;
							}
							g2.draw(shape);
						}
					}
					g2.setStroke(prevStroke);
				}
			}
		}
	}
}

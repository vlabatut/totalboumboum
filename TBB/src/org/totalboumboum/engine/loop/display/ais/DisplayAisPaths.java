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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.AiAbstractManager;
import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.loop.InteractiveLoop;
import org.totalboumboum.engine.loop.Loop;
import org.totalboumboum.engine.loop.display.Display;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.engine.player.AiPlayer;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.computing.CombinatoricsTools;

/**
 * Displays the paths set
 * by an artificial agent.
 * 
 * @author Vincent Labatut
 */
public class DisplayAisPaths extends Display
{
	/**
	 * Builds a standard display object.
	 * 
	 * @param loop
	 * 		Object used for displaying.
	 */
	public DisplayAisPaths(InteractiveLoop loop)
	{	this.players = loop.getPlayers();
		this.level = loop.getLevel();
		
		for(int i=0;i<players.size();i++)
			show.add(false);
		
		eventNames.add(SystemControlEvent.SWITCH_DISPLAY_AIS_PATHS);
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
	private final String MESSAGE_DISPLAY = "Display paths for player #";
	/** Hide message */
	private final String MESSAGE_HIDE = "Hide paths for player #";
	/** Current message */
	private String message = null;
	
	@Override
	public String getMessage(SystemControlEvent event)
	{	return message;
	}
	
	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Maximal plotted duration */
	private final static long MAX_PAUSE = 2500;
	
	@Override
	public void draw(Graphics g)
	{	Graphics2D g2 = (Graphics2D)g;
		double tileSize = RoundVariables.scaledTileDimension;
		for(int i=0;i<players.size();i++)
		{	AbstractPlayer player = players.get(i);
			if(player instanceof AiPlayer)
			{	AiAbstractManager<?,?> aiMgr = ((AiPlayer)player).getArtificialIntelligence();
				if(getShow(i))
				{	List<List<double[]>> paths = aiMgr.getPaths();
					List<List<Long>> pauses = aiMgr.getPathWaits();
					List<Color> colors = aiMgr.getPathColors();
					Stroke prevStroke = g2.getStroke();
					int thickness = (int)(tileSize/4); //used to be 3
					Stroke stroke = new BasicStroke(thickness,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
					g2.setStroke(stroke);
					double minRadius = tileSize - thickness;
					double maxRadius = tileSize;
					for(int j=0;j<paths.size();j++)
					{	List<double[]> path = paths.get(j);
						List<Long> pause = pauses.get(j);
						
						// possibly complete path with tile centers
						List<double[]> pathCplt = new ArrayList<double[]>();
						List<Long> pauseCplt = new ArrayList<Long>();
						for(int h=0;h<path.size()-1;h++)
						{	double[] coord = path.get(h);
							pathCplt.add(coord);
							long p = pause.get(h);
							pauseCplt.add(p);
							Tile t = level.getTile(coord[0], coord[1]);
							double[] coord1 = path.get(h+1);
							if(coord[0]!=coord1[0] && coord[0]!=coord1[0])
							{	double[] alt1 = {coord[0],coord1[1]};
								double[] alt2 = {coord1[0],coord[1]};
								if(t.containsPoint(alt1[0], alt1[1]) && coord[1]!=alt1[1])
								{	pathCplt.add(alt1);
									pauseCplt.add(0l);
								}
								else if(t.containsPoint(alt2[0], alt2[1]) && coord[0]!=alt2[0])
								{	pathCplt.add(alt2);
									pauseCplt.add(0l);
								}
							}
						}
						pathCplt.add(path.get(path.size()-1));
						pauseCplt.add(pause.get(pause.size()-1));
// old version						
//						// possibly complete path with tile centers
//						List<double[]> pathCplt = new ArrayList<double[]>();
//						pathCplt.add(path.get(0));
//						for(int h=1;h<path.size()-1;h++)
//						{	double[] coord = path.get(h);
//							Tile t = level.getTile(coord[0], coord[1]);
//							boolean addCoord = h==0;
//							if(!addCoord)
//							{	double[] coord0 = path.get(h-1);
//								Tile t0 = level.getTile(coord0[0], coord0[1]);
//								addCoord = !t.equals(t0);
//							}
//							if(addCoord)
//							{	double[] coord1 = {t.getPosX(),t.getPosY()};
//								pathCplt.add(coord1);
//							}
//						}
//						pathCplt.add(path.get(path.size()-1));
						
						// then draw it
						Color color = colors.get(j);
						if(color!=null && !pathCplt.isEmpty())
						{	Color paintColor = new Color(color.getRed(),color.getGreen(),color.getBlue(),Loop.INFO_ALPHA_LEVEL);
							g2.setPaint(paintColor);
							double[] coord2 = pathCplt.get(0);
							double x1,x2 = coord2[0];
							double y1,y2 = coord2[1];
							Path2D shape = new Path2D.Double();
							shape.moveTo(x2,y2);
							int k = 1;
							while(k<pathCplt.size())
							{	// pause
								long p = pauseCplt.get(k);
								if(p>0)
								{	int diameter = (int) (p/MAX_PAUSE * (maxRadius-minRadius) + minRadius);
									double x = x2 - diameter/2;
									double y = y2 - diameter/2;
									Ellipse2D.Double circle = new Ellipse2D.Double(x, y, diameter, diameter);
									g2.fill(circle);
								}
								// tiles
								x1 = x2;
								y1 = y2;
								coord2 = pathCplt.get(k);							
								x2 = coord2[0];
								y2 = coord2[1];
								// distance
								double hDist = level.getHorizontalPixelDistance(x1, x2);
								double vDist = level.getVerticalPixelDistance(y1, y2);
								// directions (to manage the case where the path goes off-screen)
								Direction direction12 = level.getDirection(x1,y1,x2,y2);
								int[] intDir12 = direction12.getIntFromDirection();
								Direction direction21 = direction12.getOpposite();
								int[] intDir21 = direction21.getIntFromDirection();
								// alternative locations
								double x1b = x2 + intDir21[0]*hDist;
								double y1b = y2 + intDir21[1]*vDist;
								double x2b = x1 + intDir12[0]*hDist;
								double y2b = y1 + intDir12[1]*vDist;
								// compare actual and theoretical positions
								if(!CombinatoricsTools.isRelativelyEqualTo(x1,x1b) || !CombinatoricsTools.isRelativelyEqualTo(y1,y1b))
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

package fr.free.totalboumboum.gui.common.content.subpanel.round;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import fr.free.totalboumboum.configuration.game.GameConfiguration;
import fr.free.totalboumboum.configuration.game.QuickMatchDraw;
import fr.free.totalboumboum.gui.common.structure.subpanel.Line;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelLines;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.StringTools;

public class RoundQuickConfigSubPanel extends UntitledSubPanelLines implements MouseListener
{	private static final long serialVersionUID = 1L;
	
	public RoundQuickConfigSubPanel(int width, int height)
	{	super(width,height,LINE_COUNT,false);
		setGameConfiguration(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// GameConfiguration	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static final int LINE_COUNT = 20;
	private static final int LINE_LEVELS_ORDER = 0;
	private static final int LINE_PLAYERS_LOCATION = 1;
	private static final int LINE_TIME_LIMIT = 2;
	private static final int LINE_POINTS_RANKS = 3;
	private static final int LINE_POINTS_VALUES = 4;
	private static final int LINE_POINTS_SHARE = 5;
	private static final int LINE_POINTS_DRAW = 6;

	private GameConfiguration gameConfiguration;

	public GameConfiguration getGameConfiguration()
	{	return gameConfiguration;	
	}
	
	public void setGameConfiguration(GameConfiguration gameConfiguration)
	{	this.gameConfiguration = gameConfiguration;
		
		// sizes
		int nameWidth = (int)(width*0.66);
		int pointsRanksWidth = (width - nameWidth - 7*GuiTools.subPanelMargin) / 5;
		int pointsValuesWidth = (pointsRanksWidth - 3*GuiTools.subPanelMargin - 2*getLineHeight());
		removeAllLines();

		if(gameConfiguration!=null)
		{	
			
			// levels order
			{	Line ln = getLine(LINE_LEVELS_ORDER);
				ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMaxWidth(col,nameWidth);
					ln.setLabelPreferredWidth(col,nameWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LEVELS_ORDER,false);
					col++;
				}
				// value
				{	setLevelsOrder();
					ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
					ln.getLabel(col).addMouseListener(this);
					col++;
				}
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				ln.setBackgroundColor(bg);
			}
			
			// players location
			{	Line ln = getLine(LINE_PLAYERS_LOCATION);
				ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMaxWidth(col,nameWidth);
					ln.setLabelPreferredWidth(col,nameWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_PLAYERS_LOCATION,false);
					col++;
				}
				// value
				{	setPlayersLocation();
					ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
					ln.getLabel(col).addMouseListener(this);
					col++;
				}
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				ln.setBackgroundColor(bg);
			}
			
			// time limit
			{	Line ln = getLine(LINE_TIME_LIMIT);
				ln.addLabel(0);
				ln.addLabel(0);
				ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMaxWidth(col,nameWidth);
					ln.setLabelPreferredWidth(col,nameWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_TIME_TITLE,false);
					col++;
				}
				// minus button
				{	ln.setLabelMaxWidth(col,ln.getHeight());
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_TIME_MINUS,true);
					ln.getLabel(col).addMouseListener(this);
					col++;
				}
				// value
				{	setTimeLimit();
					ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
					col++;
				}
				// plus button
				{	ln.setLabelMaxWidth(col,ln.getHeight());
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_TIME_PLUS,true);
					ln.getLabel(col).addMouseListener(this);
					col++;
				}
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				ln.setBackgroundColor(bg);
			}
			
			// points ranks
			{	Line ln = getLine(LINE_POINTS_RANKS);
				for(int i=0;i<5;i++)
					ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMaxWidth(col,nameWidth);
					ln.setLabelPreferredWidth(col,nameWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_RANKS_TITLE,false);
					col++;
				}
				// values
				for(int i=1;i<=5;i++)
				{	String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_RANKS_VALUE)+i; 
					String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_RANKS_VALUE+GuiKeys.TOOLTIP); 
					ln.setLabelText(col,text,tooltip);
//					ln.setLabelMaxWidth(col,(int)(maxWidth*1.1));
					ln.setLabelMaxWidth(col,pointsRanksWidth);
					col++;
				}
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				ln.setBackgroundColor(bg);
			}
			
			// points values
			{	Line ln = getLine(LINE_POINTS_VALUES);
				for(int i=0;i<5;i++)
				{	ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
				}
				int col = 0;
				// name
				{	ln.setLabelMaxWidth(col,nameWidth);
					ln.setLabelPreferredWidth(col,nameWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_VALUES_TITLE,false);
					col++;
				}
				// values
				for(int i=0;i<5;i++)
				{	// minus button
					{	ln.setLabelMaxWidth(col,ln.getHeight());
						ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_VALUES_MINUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	setPointsValue(i);
						ln.setLabelMaxWidth(col,pointsValuesWidth);
						col++;
					}
					// plus button
					{	ln.setLabelMaxWidth(col,ln.getHeight());
						ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_VALUES_PLUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
				}
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				ln.setBackgroundColor(bg);
			}
			
			// points share
			{	Line ln = getLine(LINE_POINTS_SHARE);
				ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMaxWidth(col,nameWidth);
					ln.setLabelPreferredWidth(col,nameWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_SHARE_TITLE,false);
					col++;
				}
				// value
				{	setPointsShare();
					ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
					ln.getLabel(col).addMouseListener(this);
					col++;
				}
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				ln.setBackgroundColor(bg);
			}
			
			// points draw
			{	Line ln = getLine(LINE_POINTS_DRAW);
				ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMaxWidth(col,nameWidth);
					ln.setLabelPreferredWidth(col,nameWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_DRAW_TITLE,false);
					col++;
				}
				// value
				{	setPointsDraw();
					ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
					ln.getLabel(col).addMouseListener(this);
					col++;
				}
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				ln.setBackgroundColor(bg);
			}
			
			// empty lines
			{	for(int line=LINE_POINTS_DRAW+1;line<LINE_COUNT;line++)
				{	Line ln = getLine(line);
					int col = 0;
					int mw = ln.getWidth();
					ln.setLabelMinWidth(col,mw);
					ln.setLabelPreferredWidth(col,mw);
					ln.setLabelMaxWidth(col,mw);
					col++;
				}
			}
		}
		else
		{	for(int line=0;line<LINE_COUNT;line++)
			{	Line ln = getLine(line);
				int col = 0;
				int mw = ln.getWidth();
				ln.setLabelMinWidth(col,mw);
				ln.setLabelPreferredWidth(col,mw);
				ln.setLabelMaxWidth(col,mw);
				col++;
			}
		}
	}
	
	private void setLevelsOrder()
	{	boolean order = gameConfiguration.getQuickMatchLevelsRandomOrder();
		String key;
		if(order)
			key = GuiKeys.MENU_QUICKMATCH_SETTINGS_LEVELS_ORDER_RANDOM;
		else
			key = GuiKeys.MENU_QUICKMATCH_SETTINGS_LEVELS_ORDER_FIXED;
		getLine(LINE_LEVELS_ORDER).setLabelKey(1,key,false);
	}
	
	private void setPlayersLocation()
	{	boolean location = gameConfiguration.getQuickMatchPlayersRandomLocation();
		String key;
		if(location)
			key = GuiKeys.MENU_QUICKMATCH_SETTINGS_PLAYERS_LOCATION_RANDOM;
		else
			key = GuiKeys.MENU_QUICKMATCH_SETTINGS_PLAYERS_LOCATION_FIXED;
		getLine(LINE_PLAYERS_LOCATION).setLabelKey(1,key,false);
	}
	
	private void setTimeLimit()
	{	int time = gameConfiguration.getQuickMatchLimitTime();
		String text;
		if(time<=0)
			text = new Character('\u221E').toString();
		else 
			text = StringTools.formatTimeWithSeconds(time);
		String tooltip = text;
		getLine(LINE_TIME_LIMIT).setLabelText(1,text,tooltip);
	}
	
	private void setPointsValue(int index)
	{	ArrayList<Integer> points = gameConfiguration.getQuickMatchPoints();
		for(int i=0;i<points.size();i++)
		{	String text = Integer.toString(points.get(i));
			String tooltip = text;
			getLine(LINE_POINTS_VALUES).setLabelText(1+i,text,tooltip);
		}
	}
	
	private void setPointsShare()
	{	boolean share = gameConfiguration.getQuickMatchPointsShare();
		String key;
		if(share)
			key = GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_SHARE_VAR;
		else
			key = GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_SHARE_YOK;
		getLine(LINE_POINTS_SHARE).setLabelKey(1,key,false);
	}
	
	private void setPointsDraw()
	{	QuickMatchDraw draw = gameConfiguration.getQuickMatchPointsDraw();
		String key;
		if(draw==QuickMatchDraw.AUTOKILL)
			key = GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_DRAW_AUTOKILL;
		else if(draw==QuickMatchDraw.BOTH)
			key = GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_DRAW_BOTH;
		else //if(draw==QuickMatchDraw.TIME)
			key = GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_DRAW_TIME;
		getLine(LINE_POINTS_DRAW).setLabelKey(1,key,false);
	}
	
	/////////////////////////////////////////////////////////////////
	// MOUSE LISTENER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	@Override
	public void mouseClicked(MouseEvent e)
	{	
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{	
	}

	@Override
	public void mouseExited(MouseEvent e)
	{	
	}

	@Override
	public void mousePressed(MouseEvent e)
	{	
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
}

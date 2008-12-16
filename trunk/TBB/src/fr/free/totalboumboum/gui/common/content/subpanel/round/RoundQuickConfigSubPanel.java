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
import java.util.Arrays;
import java.util.List;

import javax.swing.Box;

import fr.free.totalboumboum.configuration.game.GameConfiguration;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.gui.common.structure.subpanel.Line;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelLines;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelTable;
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
		int lines = 5;
		int colSubs = 2;
		int colGroups = 1;
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
					ln.setLabelKey(col,GuiKeys.COMMON_ROUND_LEVELS_ORDER,false);
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
					ln.setLabelKey(col,GuiKeys.COMMON_ROUND_PLAYERS_LOCATION,false);
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
					ln.setLabelKey(col,GuiKeys.COMMON_ROUND_TIME_LIMIT,false);
					col++;
				}
				// minus button
				{	ln.setLabelMaxWidth(col,ln.getHeight());
					ln.setLabelKey(col,GuiKeys.COMMON_ROUND_TIME_LIMIT_MINUS,true);
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
					ln.setLabelKey(col,GuiKeys.COMMON_ROUND_TIME_LIMIT_PLUS,true);
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
					ln.setLabelKey(col,GuiKeys.COMMON_ROUND_POINTS_RANKS,false);
					col++;
				}
				// values
				for(int i=1;i<=5;i++)
				{	String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.COMMON_ROUND_POINTS_RANKS_VALUE+GuiKeys)+i; 
					String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.COMMON_ROUND_POINTS_RANKS_VALUE+GuiKeys.TOOLTIP); 
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
					ln.setLabelKey(col,GuiKeys.COMMON_ROUND_POINTS_VALUES,false);
					col++;
				}
				// values
				for(int i=0;i<5;i++)
				{	// minus button
					{	ln.setLabelMaxWidth(col,ln.getHeight());
						ln.setLabelKey(col,GuiKeys.COMMON_ROUND_POINTS_VALUES_MINUS,true);
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
						ln.setLabelKey(col,GuiKeys.COMMON_ROUND_POINTS_VALUES_PLUS,true);
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
					ln.setLabelKey(col,GuiKeys.COMMON_ROUND_POINTS_SHARE,false);
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
					ln.setLabelKey(col,GuiKeys.COMMON_ROUND_POINTS_DRAW,false);
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

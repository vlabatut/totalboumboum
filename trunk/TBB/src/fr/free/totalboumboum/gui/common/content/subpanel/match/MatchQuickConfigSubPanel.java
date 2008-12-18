package fr.free.totalboumboum.gui.common.content.subpanel.match;

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

import javax.swing.JLabel;

import fr.free.totalboumboum.configuration.game.GameConfiguration;
import fr.free.totalboumboum.gui.common.structure.subpanel.Line;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelLines;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class MatchQuickConfigSubPanel extends UntitledSubPanelLines implements MouseListener
{	private static final long serialVersionUID = 1L;
	
	public MatchQuickConfigSubPanel(int width, int height)
	{	super(width,height,LINE_COUNT,false);
		setGameConfiguration(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// GameConfiguration	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static final int POINTS_DELTA = 1;
	private static final int POINTS_MAX = Integer.MAX_VALUE;
	private static final int ROUNDS_DELTA = 1;
	private static final int ROUNDS_MAX = Integer.MAX_VALUE;
	private static final int LINE_COUNT = 20;
	private static final int LINE_LIMIT_POINTS = 0;
	private static final int LINE_LIMIT_ROUNDS = 1;

	private GameConfiguration gameConfiguration;

	public GameConfiguration getGameConfiguration()
	{	return gameConfiguration;	
	}
	
	public void setGameConfiguration(GameConfiguration gameConfiguration)
	{	this.gameConfiguration = gameConfiguration;
		
		// sizes
		int nameWidth = (int)(width*0.66);
		removeAllLines();

		if(gameConfiguration!=null)
		{				
			// points limit
			{	Line ln = getLine(LINE_LIMIT_POINTS);
				ln.addLabel(0);
				ln.addLabel(0);
				ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMaxWidth(col,nameWidth);
					ln.setLabelPreferredWidth(col,nameWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_POINTS_TITLE,false);
					col++;
				}
				// minus button
				{	ln.setLabelMaxWidth(col,ln.getHeight());
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_POINTS_MINUS,true);
					ln.getLabel(col).addMouseListener(this);
					col++;
				}
				// value
				{	setPointsLimit();
					ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
					col++;
				}
				// plus button
				{	ln.setLabelMaxWidth(col,ln.getHeight());
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_POINTS_PLUS,true);
					ln.getLabel(col).addMouseListener(this);
					col++;
				}
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				ln.setBackgroundColor(bg);
			}
			
			// rounds limit
			{	Line ln = getLine(LINE_LIMIT_ROUNDS);
				ln.addLabel(0);
				ln.addLabel(0);
				ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMaxWidth(col,nameWidth);
					ln.setLabelPreferredWidth(col,nameWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_ROUNDS_TITLE,false);
					col++;
				}
				// minus button
				{	ln.setLabelMaxWidth(col,ln.getHeight());
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_ROUNDS_MINUS,true);
					ln.getLabel(col).addMouseListener(this);
					col++;
				}
				// value
				{	setRoundsLimit();
					ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
					col++;
				}
				// plus button
				{	ln.setLabelMaxWidth(col,ln.getHeight());
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_ROUNDS_PLUS,true);
					ln.getLabel(col).addMouseListener(this);
					col++;
				}
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				ln.setBackgroundColor(bg);
			}
			
			
			// empty lines
			{	for(int line=LINE_LIMIT_ROUNDS+1;line<LINE_COUNT;line++)
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
	
	private void setPointsLimit()
	{	int limit = gameConfiguration.getQuickMatchLimitPoints();
		String text;
		if(limit<=0)
			text = new Character('\u221E').toString();
		else
			text = Integer.toString(limit);
		String tooltip = text;
		getLine(LINE_LIMIT_POINTS).setLabelText(1,text,tooltip);
	}
		
	private void setRoundsLimit()
	{	int limit = gameConfiguration.getQuickMatchLimitRounds();
		String text;
		if(limit<=0)
			text = new Character('\u221E').toString();
		else
			text = Integer.toString(limit);
		String tooltip = text;
		getLine(LINE_LIMIT_ROUNDS).setLabelText(1,text,tooltip);
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
	{	JLabel label = (JLabel)e.getComponent();
		int[] pos = getLabelPosition(label);
		switch(pos[0])
		{	// points limit
			case LINE_LIMIT_POINTS:
				int limitPoints = gameConfiguration.getQuickMatchLimitPoints();
				// minus
				if(pos[1]==1)
				{	if(limitPoints>0)
						limitPoints = limitPoints - POINTS_DELTA;
				}
				// plus
				else //if(pos[1]==3)
				{	if(limitPoints<POINTS_MAX)
						limitPoints = limitPoints + POINTS_DELTA;
				}
				// common
				gameConfiguration.setQuickMatchLimitPoints(limitPoints);
				setPointsLimit();
				break;
				// points limit
			case LINE_LIMIT_ROUNDS:
				int limitRounds = gameConfiguration.getQuickMatchLimitRounds();
				// minus
				if(pos[1]==1)
				{	if(limitRounds>0)
						limitRounds = limitRounds - ROUNDS_DELTA;
				}
				// plus
				else //if(pos[1]==3)
				{	if(limitRounds<ROUNDS_MAX)
						limitPoints = limitRounds + ROUNDS_DELTA;
				}
				// common
				gameConfiguration.setQuickMatchLimitRounds(limitRounds);
				setPointsLimit();
				break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
}

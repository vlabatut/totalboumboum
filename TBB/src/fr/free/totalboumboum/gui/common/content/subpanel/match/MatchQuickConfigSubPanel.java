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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import fr.free.totalboumboum.configuration.game.quickmatch.QuickMatchConfiguration;
import fr.free.totalboumboum.gui.common.structure.subpanel.Line;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelLines;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class MatchQuickConfigSubPanel extends UntitledSubPanelLines implements MouseListener
{	private static final long serialVersionUID = 1L;
	
	public MatchQuickConfigSubPanel(int width, int height)
	{	super(width,height,LINE_COUNT,false);
		setQuickMatchConfiguration(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// GameConfiguration	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static final int POINTS_DELTA = 1;
	private static final int POINTS_MAX = Integer.MAX_VALUE;
	private static final int ROUNDS_DELTA = 1;
	private static final int ROUNDS_MAX = Integer.MAX_VALUE;
	private static final int LINE_COUNT = 8;
	private static final int LINE_LIMIT_POINTS = 0;
	private static final int LINE_LIMIT_ROUNDS = 1;

	private QuickMatchConfiguration quickMatchConfiguration;

	public QuickMatchConfiguration getQuickMatchConfiguration()
	{	return quickMatchConfiguration;	
	}
	
	public void setQuickMatchConfiguration(QuickMatchConfiguration quickMatchConfiguration)
	{	this.quickMatchConfiguration = quickMatchConfiguration;
		
		// sizes
		int nameWidth = (int)(width*0.66);
		removeAllLines();
		for(int i=0;i<LINE_COUNT;i++)
			addLine(0);

		if(quickMatchConfiguration!=null)
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
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
					ln.setLabelForeground(col,GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
					col++;
				}
				// minus button
				{	ln.setLabelMaxWidth(col,ln.getHeight());
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_POINTS_MINUS,true);
					ln.getLabel(col).addMouseListener(this);
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
					col++;
				}
				// value
				{	setPointsLimit();
					ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
					ln.setLabelForeground(col,GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
					col++;
				}
				// plus button
				{	ln.setLabelMaxWidth(col,ln.getHeight());
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_POINTS_PLUS,true);
					ln.getLabel(col).addMouseListener(this);
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
					col++;
				}
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
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
					ln.setLabelForeground(col,GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
					col++;
				}
				// minus button
				{	ln.setLabelMaxWidth(col,ln.getHeight());
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_ROUNDS_MINUS,true);
					ln.getLabel(col).addMouseListener(this);
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
					col++;
				}
				// value
				{	setRoundsLimit();
					ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
					ln.setLabelForeground(col,GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
					col++;
				}
				// plus button
				{	ln.setLabelMaxWidth(col,ln.getHeight());
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_ROUNDS_PLUS,true);
					ln.getLabel(col).addMouseListener(this);
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
					col++;
				}
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
	{	int limit = quickMatchConfiguration.getLimitPoints();
		String text;
		if(limit<=0)
			text = new Character('\u221E').toString();
		else
			text = Integer.toString(limit);
		String tooltip = text;
		getLine(LINE_LIMIT_POINTS).setLabelText(2,text,tooltip);
	}
		
	private void setRoundsLimit()
	{	int limit = quickMatchConfiguration.getLimitRounds();
		String text;
		if(limit<=0)
			text = new Character('\u221E').toString();
		else
			text = Integer.toString(limit);
		String tooltip = text;
		getLine(LINE_LIMIT_ROUNDS).setLabelText(2,text,tooltip);
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
				int limitPoints = quickMatchConfiguration.getLimitPoints();
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
				quickMatchConfiguration.setLimitPoints(limitPoints);
				setPointsLimit();
				break;
				// points limit
			case LINE_LIMIT_ROUNDS:
				int limitRounds = quickMatchConfiguration.getLimitRounds();
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
				quickMatchConfiguration.setLimitRounds(limitRounds);
				setRoundsLimit();
				break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
}

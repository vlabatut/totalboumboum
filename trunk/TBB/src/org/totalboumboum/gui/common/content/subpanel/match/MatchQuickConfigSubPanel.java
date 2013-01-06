package org.totalboumboum.gui.common.content.subpanel.match;

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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.configuration.game.quickmatch.QuickMatchConfiguration;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.subpanel.container.LinesSubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.content.Line;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.gui.tools.GuiTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class MatchQuickConfigSubPanel extends LinesSubPanel implements MouseListener
{	private static final long serialVersionUID = 1L;
	
	public MatchQuickConfigSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,LINE_COUNT,1,false);
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
		reinit(LINE_COUNT,1);
		int iconWidth = getLineHeight();
		int nameWidth = (int)(getDataWidth()*0.66);
		
		if(quickMatchConfiguration!=null)
		{				
			// points limit
			{	Line ln = getLine(LINE_LIMIT_POINTS);
				ln.addLabel(0);
				ln.addLabel(0);
				ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMinWidth(col,nameWidth);
					ln.setLabelPrefWidth(col,nameWidth);
					ln.setLabelMaxWidth(col,nameWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_POINTS_TITLE,false);
					ln.setLabelBackground(col,GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND);
					ln.setLabelForeground(col,GuiColorTools.COLOR_TABLE_HEADER_FOREGROUND);
					col++;
				}
				// minus button
				{	ln.setLabelMinWidth(col,iconWidth);
					ln.setLabelPrefWidth(col,iconWidth);
					ln.setLabelMaxWidth(col,iconWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_POINTS_MINUS,true);
					ln.getLabel(col).addMouseListener(this);
					ln.setLabelBackground(col,GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND);
					col++;
				}
				// value
				{	int valueWidth = getDataWidth() - 2*iconWidth - nameWidth - 3*GuiSizeTools.subPanelMargin;
					ln.setLabelMinWidth(col,valueWidth);
					ln.setLabelPrefWidth(col,valueWidth);
					ln.setLabelMaxWidth(col,valueWidth);
					ln.setLabelBackground(col,GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND);
					ln.setLabelForeground(col,GuiColorTools.COLOR_TABLE_REGULAR_FOREGROUND);
					col++;
				}
				// plus button
				{	ln.setLabelMinWidth(col,iconWidth);
					ln.setLabelPrefWidth(col,iconWidth);
					ln.setLabelMaxWidth(col,iconWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_POINTS_PLUS,true);
					ln.getLabel(col).addMouseListener(this);
					ln.setLabelBackground(col,GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND);
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
				{	ln.setLabelMinWidth(col,nameWidth);
					ln.setLabelPrefWidth(col,nameWidth);
					ln.setLabelMaxWidth(col,nameWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_ROUNDS_TITLE,false);
					ln.setLabelBackground(col,GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND);
					ln.setLabelForeground(col,GuiColorTools.COLOR_TABLE_HEADER_FOREGROUND);
					col++;
				}
				// minus button
				{	ln.setLabelMinWidth(col,iconWidth);
					ln.setLabelPrefWidth(col,iconWidth);
					ln.setLabelMaxWidth(col,iconWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_ROUNDS_MINUS,true);
					ln.getLabel(col).addMouseListener(this);
					ln.setLabelBackground(col,GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND);
					col++;
				}
				// value
				{	int valueWidth = getDataWidth() - 2*iconWidth - nameWidth - 3*GuiSizeTools.subPanelMargin;
					ln.setLabelMinWidth(col,valueWidth);
					ln.setLabelPrefWidth(col,valueWidth);
					ln.setLabelMaxWidth(col,valueWidth);
					ln.setLabelBackground(col,GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND);
					ln.setLabelForeground(col,GuiColorTools.COLOR_TABLE_REGULAR_FOREGROUND);
					col++;
				}
				// plus button
				{	ln.setLabelMinWidth(col,iconWidth);
					ln.setLabelPrefWidth(col,iconWidth);
					ln.setLabelMaxWidth(col,iconWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_ROUNDS_PLUS,true);
					ln.getLabel(col).addMouseListener(this);
					ln.setLabelBackground(col,GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND);
					col++;
				}
			}
			setPointsLimit();
			setRoundsLimit();			
			
			// empty lines
			{	for(int line=LINE_LIMIT_ROUNDS+1;line<LINE_COUNT;line++)
				{	Line ln = getLine(line);
					int col = 0;
					int mw = ln.getWidth();
					ln.setLabelMinWidth(col,mw);
					ln.setLabelPrefWidth(col,mw);
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
				ln.setLabelPrefWidth(col,mw);
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
		setLimitsBackground();
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
		setLimitsBackground();
	}
	
	private void setLimitsBackground()
	{	// set colors
		Color hbg,dbg;
		if(quickMatchConfiguration.getLimitPoints()<=0 && quickMatchConfiguration.getLimitRounds()<=0)
		{	hbg = GuiColorTools.COLOR_TABLE_SELECTED_DARK_BACKGROUND;
			dbg = GuiColorTools.COLOR_TABLE_SELECTED_BACKGROUND;
		}
		else
		{	hbg = GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND;
			dbg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
		}
		// set background
		Line line = getLine(LINE_LIMIT_POINTS);
		line.setLabelBackground(0,hbg);
		line.setLabelBackground(1,hbg);
		line.setLabelBackground(2,dbg);
		line.setLabelBackground(3,hbg);
		line = getLine(LINE_LIMIT_ROUNDS);
		line.setLabelBackground(0,hbg);
		line.setLabelBackground(1,hbg);
		line.setLabelBackground(2,dbg);
		line.setLabelBackground(3,hbg);
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
	{	MyLabel label = (MyLabel)e.getComponent();
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
				fireMatchQuickConfigModified();
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
						limitRounds = limitRounds + ROUNDS_DELTA;
				}
				// common
				quickMatchConfiguration.setLimitRounds(limitRounds);
				setRoundsLimit();
				fireMatchQuickConfigModified();
				break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}

	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<MatchQuickConfigSubPanelListener> listeners = new ArrayList<MatchQuickConfigSubPanelListener>();
	
	public void addListener(MatchQuickConfigSubPanelListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);		
	}

	public void removeListener(MatchQuickConfigSubPanelListener listener)
	{	listeners.remove(listener);		
	}
	
	private void fireMatchQuickConfigModified()
	{	for(MatchQuickConfigSubPanelListener listener: listeners)
			listener.matchQuickConfigModified();
	}
}

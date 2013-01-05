package org.totalboumboum.gui.common.content.subpanel.round;

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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.configuration.game.quickmatch.QuickMatchConfiguration;
import org.totalboumboum.configuration.game.quickmatch.QuickMatchDraw;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.subpanel.container.LinesSubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.content.Line;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.time.TimeUnit;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class RoundQuickConfigSubPanel extends LinesSubPanel implements MouseListener
{	private static final long serialVersionUID = 1L;
	
	public RoundQuickConfigSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,LINE_COUNT,1,false);
	
		setQuickMatchConfiguration(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// GameConfiguration	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static final int TIME_MAX = Integer.MAX_VALUE;
	private static final int TIME_DELTA = 10000;
	private static final int POINTS_MAX = Integer.MAX_VALUE;
	private static final int POINTS_DELTA = 1;

	private static final int LINE_COUNT = 8;
	private static final int LINE_LEVELS_ORDER = 0;
	private static final int LINE_PLAYERS_LOCATION = 1;
	private static final int LINE_TIME_LIMIT = 2;
	private static final int LINE_POINTS_RANKS = 3;
	private static final int LINE_POINTS_VALUES = 4;
	private static final int LINE_POINTS_SHARE = 5;
	private static final int LINE_POINTS_DRAW = 6;
	private static final int LINE_SUDDEN_DEATH = 7;

	private QuickMatchConfiguration quickMatchConfiguration;

	public QuickMatchConfiguration getQuickMatchConfiguration()
	{	return quickMatchConfiguration;	
	}
	
	public void setQuickMatchConfiguration(QuickMatchConfiguration quickMatchConfiguration)
	{	this.quickMatchConfiguration = quickMatchConfiguration;
		
		// sizes
		reinit(LINE_COUNT,1);
		int iconWidth = getLineHeight();
		int nameWidth = (int)(getDataWidth()*0.33);
		int pointsRanksWidth = (getDataWidth() - nameWidth - GameData.CONTROL_COUNT*GuiTools.subPanelMargin) / GameData.CONTROL_COUNT;
		int firstPointsRankWidth = getDataWidth() - nameWidth - GameData.CONTROL_COUNT*GuiTools.subPanelMargin - (GameData.CONTROL_COUNT-1)*pointsRanksWidth;
		int pointsValuesWidth = pointsRanksWidth - 2*GuiTools.subPanelMargin - 2*iconWidth;
//		getDataWidth() - nameWidth - 7*GuiTools.subPanelMargin - (GameConstants.CONTROL_COUNT-1)*pointsRanksWidth;
		int firstPointsValuesWidth = firstPointsRankWidth - 2*GuiTools.subPanelMargin - 2*iconWidth;
		
		if(quickMatchConfiguration!=null)
		{	// levels order
			{	Line ln = getLine(LINE_LEVELS_ORDER);
				ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMinWidth(col,nameWidth);
					ln.setLabelPrefWidth(col,nameWidth);
					ln.setLabelMaxWidth(col,nameWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LEVELS_ORDER_TITLE,false);
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
					ln.setLabelForeground(col,GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
					col++;
				}
				// value
				{	setLevelsOrder();
					int valueWidth = getDataWidth() - nameWidth - GuiTools.subPanelMargin;
					ln.setLabelMinWidth(col,valueWidth);
					ln.setLabelPrefWidth(col,valueWidth);
					ln.setLabelMaxWidth(col,valueWidth);
					ln.getLabel(col).addMouseListener(this);
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
					ln.setLabelForeground(col,GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
					col++;
				}
			}
			
			// players location
			{	Line ln = getLine(LINE_PLAYERS_LOCATION);
				ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMinWidth(col,nameWidth);
					ln.setLabelPrefWidth(col,nameWidth);
					ln.setLabelMaxWidth(col,nameWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_PLAYERS_LOCATION_TITLE,false);
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
					ln.setLabelForeground(col,GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
					col++;
				}
				// value
				{	setPlayersLocation();
					int valueWidth = getDataWidth() - nameWidth - GuiTools.subPanelMargin;
					ln.setLabelMinWidth(col,valueWidth);
					ln.setLabelPrefWidth(col,valueWidth);
					ln.setLabelMaxWidth(col,valueWidth);
					ln.getLabel(col).addMouseListener(this);
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
					ln.setLabelForeground(col,GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
					col++;
				}
			}
			
			// time limit
			{	Line ln = getLine(LINE_TIME_LIMIT);
				ln.addLabel(0);
				ln.addLabel(0);
				ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMinWidth(col,nameWidth);
					ln.setLabelPrefWidth(col,nameWidth);
					ln.setLabelMaxWidth(col,nameWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_TIME_TITLE,false);
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
					ln.setLabelForeground(col,GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
					col++;
				}
				// minus button
				{	ln.setLabelMinWidth(col,iconWidth);
					ln.setLabelPrefWidth(col,iconWidth);
					ln.setLabelMaxWidth(col,iconWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_TIME_MINUS,true);
					ln.getLabel(col).addMouseListener(this);
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
					col++;
				}
				// value
				{	setTimeLimit();
					int valueWidth = getDataWidth() - 2*iconWidth - nameWidth - 3*GuiTools.subPanelMargin;
					ln.setLabelMinWidth(col,valueWidth);
					ln.setLabelPrefWidth(col,valueWidth);
					ln.setLabelMaxWidth(col,valueWidth);
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
					ln.setLabelForeground(col,GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
					col++;
				}
				// plus button
				{	ln.setLabelMinWidth(col,iconWidth);
					ln.setLabelPrefWidth(col,iconWidth);
					ln.setLabelMaxWidth(col,iconWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_TIME_PLUS,true);
					ln.getLabel(col).addMouseListener(this);
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
					col++;
				}
			}
			
			// points ranks
			{	Line ln = getLine(LINE_POINTS_RANKS);
				for(int i=0;i<GameData.CONTROL_COUNT;i++)
					ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMinWidth(col,nameWidth);
					ln.setLabelPrefWidth(col,nameWidth);
					ln.setLabelMaxWidth(col,nameWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_RANKS_TITLE,false);
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
					ln.setLabelForeground(col,GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
					col++;
				}
				// values
				for(int i=1;i<=GameData.CONTROL_COUNT;i++)
				{	String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_RANKS_VALUE)+i; 
					String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_RANKS_VALUE+GuiKeys.TOOLTIP)+i; 
					ln.setLabelText(col,text,tooltip);
					int size;
					if(i==1)
						size = firstPointsRankWidth;
					else
						size = pointsRanksWidth;
					ln.setLabelMinWidth(col,size);
					ln.setLabelPrefWidth(col,size);
					ln.setLabelMaxWidth(col,size);					
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
					ln.setLabelForeground(col,GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
					col++;
				}
			}
			
			// points values
			{	Line ln = getLine(LINE_POINTS_VALUES);
				for(int i=0;i<GameData.CONTROL_COUNT;i++)
				{	ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
				}
				int col = 0;
				// name
				{	ln.setLabelMinWidth(col,nameWidth);
					ln.setLabelPrefWidth(col,nameWidth);
					ln.setLabelMaxWidth(col,nameWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_VALUES_TITLE,false);
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
					ln.setLabelForeground(col,GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
					col++;
				}
				// values
				for(int i=0;i<GameData.CONTROL_COUNT;i++)
				{	// minus button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_VALUES_MINUS,true);
						ln.getLabel(col).addMouseListener(this);
						ln.setLabelBackground(col,GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
						col++;
					}
					// value
					{	setPointsValue(i);
						int size;
						if(i==0)
							size = firstPointsValuesWidth;
						else
							size = pointsValuesWidth;
						ln.setLabelMinWidth(col,size);
						ln.setLabelPrefWidth(col,size);
						ln.setLabelMaxWidth(col,size);					
						ln.setLabelBackground(col,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
						ln.setLabelForeground(col,GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
						col++;
					}
					// plus button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_VALUES_PLUS,true);
						ln.getLabel(col).addMouseListener(this);
						ln.setLabelBackground(col,GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
						col++;
					}
				}
			}
			
			// points share
			{	Line ln = getLine(LINE_POINTS_SHARE);
				ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMinWidth(col,nameWidth);
					ln.setLabelPrefWidth(col,nameWidth);
					ln.setLabelMaxWidth(col,nameWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_SHARE_TITLE,false);
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
					ln.setLabelForeground(col,GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
					col++;
				}
				// value
				{	setPointsShare();
					int valueWidth = getDataWidth() - nameWidth - GuiTools.subPanelMargin;
					ln.setLabelMinWidth(col,valueWidth);
					ln.setLabelPrefWidth(col,valueWidth);
					ln.setLabelMaxWidth(col,valueWidth);
					ln.getLabel(col).addMouseListener(this);
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
					ln.setLabelForeground(col,GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
					col++;
				}
			}
			
			// points draw
			{	Line ln = getLine(LINE_POINTS_DRAW);
				ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMinWidth(col,nameWidth);
					ln.setLabelPrefWidth(col,nameWidth);
					ln.setLabelMaxWidth(col,nameWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_DRAW_TITLE,false);
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
					ln.setLabelForeground(col,GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
					col++;
				}
				// value
				{	setPointsDraw();
					int valueWidth = getDataWidth() - nameWidth - GuiTools.subPanelMargin;
					ln.setLabelMinWidth(col,valueWidth);
					ln.setLabelPrefWidth(col,valueWidth);
					ln.setLabelMaxWidth(col,valueWidth);
					ln.getLabel(col).addMouseListener(this);
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
					ln.setLabelForeground(col,GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
					col++;
				}
			}
			
			// sudden death
			{	Line ln = getLine(LINE_SUDDEN_DEATH);
				ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMinWidth(col,nameWidth);
					ln.setLabelPrefWidth(col,nameWidth);
					ln.setLabelMaxWidth(col,nameWidth);
					ln.setLabelKey(col,GuiKeys.MENU_QUICKMATCH_SETTINGS_SUDDEN_DEATH_TITLE,false);
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
					ln.setLabelForeground(col,GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
					col++;
				}
				// value
				{	setSuddenDeathDisabled();
					int valueWidth = getDataWidth() - nameWidth - GuiTools.subPanelMargin;
					ln.setLabelMinWidth(col,valueWidth);
					ln.setLabelPrefWidth(col,valueWidth);
					ln.setLabelMaxWidth(col,valueWidth);
					ln.getLabel(col).addMouseListener(this);
					ln.setLabelBackground(col,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
					ln.setLabelForeground(col,GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
					col++;
				}
			}
			
			// empty lines
			{	for(int line=LINE_SUDDEN_DEATH+1;line<LINE_COUNT;line++)
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
	
	private void setLevelsOrder()
	{	boolean order = quickMatchConfiguration.getLevelsRandomOrder();
		String key;
		if(order)
			key = GuiKeys.MENU_QUICKMATCH_SETTINGS_LEVELS_ORDER_RANDOM;
		else
			key = GuiKeys.MENU_QUICKMATCH_SETTINGS_LEVELS_ORDER_FIXED;
		getLine(LINE_LEVELS_ORDER).setLabelKey(1,key,false);
	}
	
	private void setPlayersLocation()
	{	boolean location = quickMatchConfiguration.getPlayersRandomLocation();
		String key;
		if(location)
			key = GuiKeys.MENU_QUICKMATCH_SETTINGS_PLAYERS_LOCATION_RANDOM;
		else
			key = GuiKeys.MENU_QUICKMATCH_SETTINGS_PLAYERS_LOCATION_FIXED;
		getLine(LINE_PLAYERS_LOCATION).setLabelKey(1,key,false);
	}
	
	private void setTimeLimit()
	{	int time = quickMatchConfiguration.getLimitTime();
		String text;
		if(time<=0)
			text = new Character('\u221E').toString();
		else 
			text = TimeTools.formatTime(time,TimeUnit.SECOND,TimeUnit.MILLISECOND,false);
		String tooltip = text;
		getLine(LINE_TIME_LIMIT).setLabelText(2,text,tooltip);
	}
	
	private void setPointsValue(int index)
	{	List<Integer> points = quickMatchConfiguration.getPoints();
		int i = 1+(index*3)+1;
		String text = Integer.toString(points.get(index));
		String tooltip = text;
		getLine(LINE_POINTS_VALUES).setLabelText(i,text,tooltip);
	}
	
	private void setPointsShare()
	{	boolean share = quickMatchConfiguration.getPointsShare();
		String key;
		if(share)
			key = GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_SHARE_VAR;
		else
			key = GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_SHARE_YOK;
		getLine(LINE_POINTS_SHARE).setLabelKey(1,key,false);
	}
	
	private void setPointsDraw()
	{	QuickMatchDraw draw = quickMatchConfiguration.getPointsDraw();
		String key = null;
		if(draw==QuickMatchDraw.AUTOKILL)
			key = GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_DRAW_AUTOKILL;
		else if(draw==QuickMatchDraw.BOTH)
			key = GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_DRAW_BOTH;
		else if(draw==QuickMatchDraw.NONE)
			key = GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_DRAW_NONE;
		else if(draw==QuickMatchDraw.TIME)
			key = GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_DRAW_TIME;
		getLine(LINE_POINTS_DRAW).setLabelKey(1,key,false);
	}
	
	private void setSuddenDeathDisabled()
	{	boolean suddenDeathDisabled = quickMatchConfiguration.getSuddenDeathDisabled();
		String key;
		if(suddenDeathDisabled)
			key = GuiKeys.MENU_QUICKMATCH_SETTINGS_SUDDEN_DEATH_YOK;
		else
			key = GuiKeys.MENU_QUICKMATCH_SETTINGS_SUDDEN_DEATH_VAR;
		getLine(LINE_SUDDEN_DEATH).setLabelKey(1,key,false);
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
		{	// levels order
			case LINE_LEVELS_ORDER:
				boolean levelsOrder = !quickMatchConfiguration.getLevelsRandomOrder();
				quickMatchConfiguration.setLevelsRandomOrder(levelsOrder);
				setLevelsOrder();
				fireRoundQuickConfigModified();
				break;
			// players location
			case LINE_PLAYERS_LOCATION:
				boolean playersLocation = !quickMatchConfiguration.getPlayersRandomLocation();
				quickMatchConfiguration.setPlayersRandomLocation(playersLocation);
				setPlayersLocation();
				fireRoundQuickConfigModified();
				break;
			// time limit
			case LINE_TIME_LIMIT:
				int limitTime = quickMatchConfiguration.getLimitTime();
				// minus
				if(pos[1]==1)
				{	if(limitTime>0)
						limitTime = limitTime - TIME_DELTA;
				}
				// plus
				else //if(pos[1]==3)
				{	if(limitTime<TIME_MAX)
						limitTime = limitTime + TIME_DELTA;
				}
				// common
				quickMatchConfiguration.setLimitTime(limitTime);
				setTimeLimit();
				fireRoundQuickConfigModified();
				break;
			// time limit
			case LINE_POINTS_VALUES:
				int index = (pos[1]-1)/3;
				int minus = 1+(index*3);
				//int plus = 1+(index*3)+1;
				int pointsValues = quickMatchConfiguration.getPoints().get(index);
				// minus
				if(pos[1]==minus)
				{	if(pointsValues>0)
						pointsValues = pointsValues - POINTS_DELTA;
				}
				// plus
				else //if(pos[1]==plus)
				{	if(pointsValues<POINTS_MAX)
						pointsValues = pointsValues + POINTS_DELTA;
				}
				// common
				quickMatchConfiguration.getPoints().set(index,pointsValues);
				setPointsValue(index);
				fireRoundQuickConfigModified();
				break;
			// points share
			case LINE_POINTS_SHARE:
				boolean pointsShare = !quickMatchConfiguration.getPointsShare();
				quickMatchConfiguration.setPointsShare(pointsShare);
				setPointsShare();
				fireRoundQuickConfigModified();
				break;			
			// points draw
			case LINE_POINTS_DRAW:
				QuickMatchDraw pointsDraw = quickMatchConfiguration.getPointsDraw();
				pointsDraw = QuickMatchDraw.getNext(pointsDraw);
				quickMatchConfiguration.setPointsDraw(pointsDraw);
				setPointsDraw();
				fireRoundQuickConfigModified();
				break;
			// points share
			case LINE_SUDDEN_DEATH:
				boolean suddenDeath = !quickMatchConfiguration.getSuddenDeathDisabled();
				quickMatchConfiguration.setSuddenDeathDisabled(suddenDeath);
				setSuddenDeathDisabled();
				fireRoundQuickConfigModified();
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
	private List<RoundQuickConfigSubPanelListener> listeners = new ArrayList<RoundQuickConfigSubPanelListener>();
	
	public void addListener(RoundQuickConfigSubPanelListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);		
	}

	public void removeListener(RoundQuickConfigSubPanelListener listener)
	{	listeners.remove(listener);		
	}
	
	private void fireRoundQuickConfigModified()
	{	for(RoundQuickConfigSubPanelListener listener: listeners)
			listener.roundQuickConfigModified();
	}
}

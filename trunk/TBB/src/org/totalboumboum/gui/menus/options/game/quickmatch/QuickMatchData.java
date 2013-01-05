package org.totalboumboum.gui.menus.options.game.quickmatch;

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

import org.totalboumboum.configuration.game.quickmatch.QuickMatchConfiguration;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.LinesSubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel.Mode;
import org.totalboumboum.gui.common.structure.subpanel.content.Line;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class QuickMatchData extends EntitledDataPanel implements MouseListener
{	
	private static final long serialVersionUID = 1L;
	
	private static final int LINE_COUNT = 20;

	private static final int LINE_USE_PLAYERS = 0;
	private static final int LINE_USE_LEVELS = 1;
	private static final int LINE_USE_SETTINGS = 2;

	private LinesSubPanel optionsPanel;

	public QuickMatchData(SplitMenuPanel container)
	{	super(container);
		
		// title
		String key = GuiKeys.MENU_OPTIONS_GAME_QUICKMATCH_TITLE;
		setTitleKey(key);
		
		// data
		{	int w = getDataWidth();
			int h = getDataHeight();
			optionsPanel = new LinesSubPanel(w,h,Mode.BORDER,LINE_COUNT,1,false);
			int titleWidth = (int)(optionsPanel.getDataWidth()*0.66);
			int iconWidth = optionsPanel.getDataWidth() - titleWidth - GuiTools.subPanelMargin;
			
			// #0 use last players
			{	Line ln = optionsPanel.getLine(LINE_USE_PLAYERS);
				ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMinWidth(col,titleWidth);
					ln.setLabelPrefWidth(col,titleWidth);
					ln.setLabelMaxWidth(col,titleWidth);
					ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_GAME_QUICKMATCH_PLAYERS_USE,false);
					col++;
				}
				// value
				{	ln.setLabelMinWidth(col,iconWidth);
					ln.setLabelPrefWidth(col,iconWidth);
					ln.setLabelMaxWidth(col,iconWidth);
//					setUsePlayers();
					ln.getLabel(col).addMouseListener(this);
					col++;
				}
				Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
				ln.setBackgroundColor(bg);
			}

			// #1 use last levels
			{	Line ln = optionsPanel.getLine(LINE_USE_LEVELS);
				ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMinWidth(col,titleWidth);
					ln.setLabelPrefWidth(col,titleWidth);
					ln.setLabelMaxWidth(col,titleWidth);
					ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_GAME_QUICKMATCH_LEVELS_USE,false);
					col++;
				}
				// value
				{	ln.setLabelMinWidth(col,iconWidth);
					ln.setLabelPrefWidth(col,iconWidth);
					ln.setLabelMaxWidth(col,iconWidth);
//					setUseLevels();
					ln.getLabel(col).addMouseListener(this);
					col++;
				}
				Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
				ln.setBackgroundColor(bg);
			}
			
			// #2 use last settings
			{	Line ln = optionsPanel.getLine(LINE_USE_SETTINGS);
				ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMinWidth(col,titleWidth);
					ln.setLabelPrefWidth(col,titleWidth);
					ln.setLabelMaxWidth(col,titleWidth);
					ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_GAME_QUICKMATCH_SETTINGS_USE,false);
					col++;
				}
				// value
				{	ln.setLabelMinWidth(col,iconWidth);
					ln.setLabelPrefWidth(col,iconWidth);
					ln.setLabelMaxWidth(col,iconWidth);
//					setUseSettings();
					ln.getLabel(col).addMouseListener(this);
					col++;
				}
				Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
				ln.setBackgroundColor(bg);
			}

			// EMPTY
			{	for(int line=LINE_USE_SETTINGS+1;line<LINE_COUNT;line++)
				{	Line ln = optionsPanel.getLine(line);
					int col = 0;
					int maxWidth = ln.getWidth();
					ln.setLabelMinWidth(col,maxWidth);
					ln.setLabelPrefWidth(col,maxWidth);
					ln.setLabelMaxWidth(col,maxWidth);
					col++;
				}
			}
		}

		setDataPart(optionsPanel);
	}

	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
	{	//
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
		int[] pos = optionsPanel.getLabelPosition(label);
		switch(pos[0])
		{	// levels use
			case LINE_USE_LEVELS:
				boolean useLevels = !quickMatchConfiguration.getUseLastLevels();
				quickMatchConfiguration.setUseLastLevels(useLevels);
				setUseLevels();
				break;
			// players use
			case LINE_USE_PLAYERS:
				boolean usePlayers = !quickMatchConfiguration.getUseLastPlayers();
				quickMatchConfiguration.setUseLastPlayers(usePlayers);
				setUsePlayers();
				break;
			// settings use
			case LINE_USE_SETTINGS:
				boolean useSettings = !quickMatchConfiguration.getUseLastSettings();
				quickMatchConfiguration.setUseLastSettings(useSettings);
				setUseSettings();
				break;
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}

	/////////////////////////////////////////////////////////////////
	// CONFIGURATION				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private QuickMatchConfiguration quickMatchConfiguration;

	public void setQuickMatchConfiguration(QuickMatchConfiguration quickMatchConfiguration)
	{	this.quickMatchConfiguration = quickMatchConfiguration;
		refreshOptions();
	}
	
	public QuickMatchConfiguration getQuickMatchConfiguration()
	{	return quickMatchConfiguration;	
	}

	/////////////////////////////////////////////////////////////////
	// OPTIONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void refreshOptions()
	{	setUseLevels();
		setUsePlayers();
		setUseSettings();
	}
	
	private void setUseLevels()
	{	boolean useLevels = quickMatchConfiguration.getUseLastLevels();
		String key;
		if(useLevels)
			key = GuiKeys.MENU_OPTIONS_GAME_QUICKMATCH_LEVELS_TRUE;
		else
			key = GuiKeys.MENU_OPTIONS_GAME_QUICKMATCH_LEVELS_FALSE;
		optionsPanel.getLine(LINE_USE_LEVELS).setLabelKey(1,key,true);
	}
	
	private void setUsePlayers()
	{	boolean usePlayers = quickMatchConfiguration.getUseLastPlayers();
		String key;
		if(usePlayers)
			key = GuiKeys.MENU_OPTIONS_GAME_QUICKMATCH_PLAYERS_TRUE;
		else
			key = GuiKeys.MENU_OPTIONS_GAME_QUICKMATCH_PLAYERS_FALSE;
		optionsPanel.getLine(LINE_USE_PLAYERS).setLabelKey(1,key,true);
	}

	private void setUseSettings()
	{	boolean useSettings = quickMatchConfiguration.getUseLastSettings();
		String key;
		if(useSettings)
			key = GuiKeys.MENU_OPTIONS_GAME_QUICKMATCH_SETTINGS_TRUE;
		else
			key = GuiKeys.MENU_OPTIONS_GAME_QUICKMATCH_SETTINGS_FALSE;
		optionsPanel.getLine(LINE_USE_SETTINGS).setLabelKey(1,key,true);
	}
}

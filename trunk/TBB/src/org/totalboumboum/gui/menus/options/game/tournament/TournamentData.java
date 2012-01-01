package org.totalboumboum.gui.menus.options.game.tournament;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import org.totalboumboum.configuration.game.tournament.TournamentConfiguration;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.LinesSubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel.Mode;
import org.totalboumboum.gui.common.structure.subpanel.content.Line;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class TournamentData extends EntitledDataPanel implements MouseListener
{	
	private static final long serialVersionUID = 1L;
	
	private static final int LINE_COUNT = 20;

	private static final int LINE_USE_PLAYERS = 0;
	private static final int LINE_USE_TOURNAMENT = 1;
	private static final int LINE_AUTOSAVE = 2;
	private static final int LINE_AUTOLOAD = 3;

	private LinesSubPanel optionsPanel;

	public TournamentData(SplitMenuPanel container)
	{	super(container);
		
		// title
		String key = GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_TITLE;
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
					ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_PLAYERS_USE,false);
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
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				ln.setBackgroundColor(bg);
			}

			// #1 use last settings
			{	Line ln = optionsPanel.getLine(LINE_USE_TOURNAMENT);
				ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMinWidth(col,titleWidth);
					ln.setLabelPrefWidth(col,titleWidth);
					ln.setLabelMaxWidth(col,titleWidth);
					ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_TOURNAMENT_USE,false);
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
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				ln.setBackgroundColor(bg);
			}

			// #2 auto save
			{	Line ln = optionsPanel.getLine(LINE_AUTOSAVE);
				ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMinWidth(col,titleWidth);
					ln.setLabelPrefWidth(col,titleWidth);
					ln.setLabelMaxWidth(col,titleWidth);
					ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_AUTOSAVE_TITLE,false);
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
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				ln.setBackgroundColor(bg);
			}

			// #3 auto load
			{	Line ln = optionsPanel.getLine(LINE_AUTOLOAD);
				ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMinWidth(col,titleWidth);
					ln.setLabelPrefWidth(col,titleWidth);
					ln.setLabelMaxWidth(col,titleWidth);
					ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_AUTOLOAD_TITLE,false);
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
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				ln.setBackgroundColor(bg);
			}

			// EMPTY
			{	for(int line=LINE_AUTOLOAD+1;line<LINE_COUNT;line++)
				{	Line ln = optionsPanel.getLine(line);
					int col = 0;
					int minWidth = ln.getWidth();
					ln.setLabelMinWidth(col,minWidth);
					ln.setLabelPrefWidth(col,minWidth);
					ln.setLabelMaxWidth(col,minWidth);
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
		{	// players use
			case LINE_USE_PLAYERS:
				boolean usePlayers = !tournamentConfiguration.getUseLastPlayers();
				tournamentConfiguration.setUseLastPlayers(usePlayers);
				setUsePlayers();
				break;
			// tournament use
			case LINE_USE_TOURNAMENT:
				boolean useTournament = !tournamentConfiguration.getUseLastTournament();
				tournamentConfiguration.setUseLastTournament(useTournament);
				setUseTournament();
				break;
			// auto save
			case LINE_AUTOSAVE:
				boolean autoSave = !tournamentConfiguration.getAutoSave();
				tournamentConfiguration.setAutoSave(autoSave);
				setAutoSave();
				break;
			// auto load
			case LINE_AUTOLOAD:
				boolean autoLoad = !tournamentConfiguration.getAutoLoad();
				tournamentConfiguration.setAutoLoad(autoLoad);
				setAutoLoad();
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
	private TournamentConfiguration tournamentConfiguration;

	public void setTournamentConfiguration(TournamentConfiguration quickMatchConfiguration)
	{	this.tournamentConfiguration = quickMatchConfiguration;
		refreshOptions();
	}
	
	public TournamentConfiguration getTournamentConfiguration()
	{	return tournamentConfiguration;	
	}

	/////////////////////////////////////////////////////////////////
	// OPTIONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void refreshOptions()
	{	setUsePlayers();
		setUseTournament();
		setAutoLoad();
		setAutoSave();
	}
	
	private void setUsePlayers()
	{	boolean usePlayers = tournamentConfiguration.getUseLastPlayers();
		String key;
		if(usePlayers)
			key = GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_PLAYERS_TRUE;
		else
			key = GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_PLAYERS_FALSE;
		optionsPanel.getLine(LINE_USE_PLAYERS).setLabelKey(1,key,true);
	}

	private void setUseTournament()
	{	boolean useTournament = tournamentConfiguration.getUseLastTournament();
		String key;
		if(useTournament)
			key = GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_TOURNAMENT_TRUE;
		else
			key = GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_TOURNAMENT_FALSE;
		optionsPanel.getLine(LINE_USE_TOURNAMENT).setLabelKey(1,key,true);
	}

	private void setAutoSave()
	{	boolean autoSave = tournamentConfiguration.getAutoSave();
		String key;
		if(autoSave)
			key = GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_AUTOSAVE_TRUE;
		else
			key = GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_AUTOSAVE_FALSE;
		optionsPanel.getLine(LINE_AUTOSAVE).setLabelKey(1,key,true);
	}

	private void setAutoLoad()
	{	boolean autoLoad = tournamentConfiguration.getAutoLoad();
		String key;
		if(autoLoad)
			key = GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_AUTOLOAD_TRUE;
		else
			key = GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_AUTOLOAD_FALSE;
		optionsPanel.getLine(LINE_AUTOLOAD).setLabelKey(1,key,true);
	}
}

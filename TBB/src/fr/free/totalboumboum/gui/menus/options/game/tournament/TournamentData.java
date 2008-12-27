package fr.free.totalboumboum.gui.menus.options.game.tournament;

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

import fr.free.totalboumboum.configuration.game.tournament.TournamentConfiguration;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.Line;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelLines;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class TournamentData extends EntitledDataPanel implements MouseListener
{	
	private static final long serialVersionUID = 1L;
	
	private static final int LINE_COUNT = 20;

	private static final int LINE_USE_PLAYERS = 0;
	private static final int LINE_USE_TOURNAMENT = 1;

	private UntitledSubPanelLines optionsPanel;

	public TournamentData(SplitMenuPanel container)
	{	super(container);
		
		// title
		String key = GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_TITLE;
		setTitleKey(key);
		
		// data
		{	int w = getDataWidth();
			int h = getDataHeight();
			optionsPanel = new UntitledSubPanelLines(w,h,LINE_COUNT,false);
			int tWidth = (int)(w*0.66);
			
			// #0 use last players
			{	Line ln = optionsPanel.getLine(LINE_USE_PLAYERS);
				ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMaxWidth(col,tWidth);
					ln.setLabelPreferredWidth(col,tWidth);
					ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_PLAYERS_USE,false);
					col++;
				}
				// value
				{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
//					setUsePlayers();
					ln.getLabel(col).addMouseListener(this);
					col++;
				}
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				ln.setBackgroundColor(bg);
			}

			// #2 use last settings
			{	Line ln = optionsPanel.getLine(LINE_USE_TOURNAMENT);
				ln.addLabel(0);
				int col = 0;
				// name
				{	ln.setLabelMaxWidth(col,tWidth);
					ln.setLabelPreferredWidth(col,tWidth);
					ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_TOURNAMENT_USE,false);
					col++;
				}
				// value
				{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
//					setUseSettings();
					ln.getLabel(col).addMouseListener(this);
					col++;
				}
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				ln.setBackgroundColor(bg);
			}

			// EMPTY
			{	for(int line=LINE_USE_TOURNAMENT+1;line<LINE_COUNT;line++)
				{	Line ln = optionsPanel.getLine(line);
					int col = 0;
					int mw = ln.getWidth();
					ln.setLabelMinWidth(col,mw);
					ln.setLabelPreferredWidth(col,mw);
					ln.setLabelMaxWidth(col,mw);
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
	{	JLabel label = (JLabel)e.getComponent();
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
}

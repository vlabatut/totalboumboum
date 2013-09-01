package org.totalboumboum.gui.menus.tournament;

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
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;

import org.totalboumboum.configuration.game.tournament.TournamentConfiguration;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.content.subpanel.tournament.TournamentMiscSubPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.common.structure.subpanel.BasicPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel.Mode;
import org.totalboumboum.gui.menus.tournament.select.SelectTournamentSplitPanel;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;

/**
 * This class allows selecting
 * the tournament to be played.
 * 
 * @author Vincent Labatut
 */
public class SettingsData extends EntitledDataPanel implements MouseListener
{	/** Class id */
	private static final long serialVersionUID = 1L;
		
	/**
	 * Creates a new panel to select a tournament.
	 * 
	 * @param container
	 * 		Container of this panel.
	 */
	public SettingsData(SplitMenuPanel container)
	{	super(container);
		
		// title
		setTitleKey(GuiKeys.MENU_TOURNAMENT_SETTINGS_TITLE);
		
		BasicPanel mainPanel;
		// data
		{	mainPanel = new BasicPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS); 
				mainPanel.setLayout(layout);
			}
			
			int margin = GuiSizeTools.panelMargin;
			tournamentHeight = (int)(dataHeight*SPLIT_RATIO); 
			miscHeight = dataHeight - tournamentHeight - margin;
			mainPanel.setOpaque(false);
			
			// tournament panel
			{	tournamentPanel = makeTournamentPanel(dataWidth,tournamentHeight);
				mainPanel.add(tournamentPanel);
			}
			
			mainPanel.add(Box.createRigidArea(new Dimension(GuiSizeTools.panelMargin,GuiSizeTools.panelMargin)));
			
			// misc panel
			{	miscPanel = new TournamentMiscSubPanel(dataWidth,miscHeight,15);
				mainPanel.add(miscPanel);
			}
			
			setDataPart(mainPanel);
		}
	}
		
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
	{	AbstractTournament tournament = tournamentConfiguration.getTournament();
		miscPanel.setTournament(tournament);
		refreshTournament();
//		if(tournament.getAllowedPlayerNumbers().contains(tournamentConfiguration.getProfilesSelection().getProfileCount()))
//			miscPanel.selectAllowedPlayers(false);
//		else
//			miscPanel.selectAllowedPlayers(true);
	}
	
	/////////////////////////////////////////////////////////////////
	// MOUSE LISTENER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void mouseClicked(MouseEvent e)
	{	//
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{	//
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{	//
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{	fireMousePressed(e);
		
		SelectTournamentSplitPanel selectTournamentPanel = new SelectTournamentSplitPanel(container.getMenuContainer(),container,tournamentConfiguration);
		getMenuContainer().replaceWith(selectTournamentPanel);
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{	//
	}

	/////////////////////////////////////////////////////////////////
	// CONFIGURATION				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Current tournament configuration */
	private TournamentConfiguration tournamentConfiguration;

	/**
	 * Sets up the tournament configuration.
	 * 
	 * @param tournamentConfiguration
	 * 		Tournament configuration to set up.
	 */
	public void setTournamentConfiguration(TournamentConfiguration tournamentConfiguration)
	{	this.tournamentConfiguration = tournamentConfiguration;
		refresh();
	}
	
	/**
	 * Returns the current configuration
	 * for this tournament.
	 * 
	 * @return
	 * 		The current tournament configuration.
	 */
	public TournamentConfiguration getTournamentConfiguration()
	{	return tournamentConfiguration;	
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Ratio between both parts of the panel */
	private static final float SPLIT_RATIO = 0.06f;
	/** Panel displaying various data regarding the tournament */
	private TournamentMiscSubPanel miscPanel;
	/** Panel letting the user select the tournament */
	private TableSubPanel tournamentPanel;
	/** Height of the tournament panel */
	private int tournamentHeight;
	/** Height of the information panel */
	private int miscHeight;

	/**
	 * Updates the information displayed
	 * in this pabel, after a change
	 * in the tournament configuration.
	 */
	private void refreshTournament()
	{	String tournamentFile = tournamentConfiguration.getTournamentName().toString();
		if(tournamentFile!=null)
			tournamentPanel.setLabelText(0,0,tournamentFile.toString(),tournamentFile.toString());
		else
			tournamentPanel.setLabelText(0,0,null,null);
	}

	/**
	 * Builds and initializes the panel
	 * displaying the current tournament
	 * configuration.
	 * 
	 * @param width
	 * 		Width of the panel to be built.
	 * @param height
	 * 		Height of the panel to be built.
	 * @return
	 * 		The resulting panel.
	 */
	private TableSubPanel makeTournamentPanel(int width, int height)
	{	int cols = 2;
		int lines = 1;
		int margin = GuiSizeTools.subPanelMargin;
		TableSubPanel result = new TableSubPanel(width,height,Mode.BORDER,lines,cols,false);
		@SuppressWarnings("unused")
		int headerHeight = result.getHeaderHeight();
		int lineHeight = result.getLineHeight();
		int iconWidth = lineHeight;
		int fileWidth = result.getDataWidth() - (iconWidth + (cols-1)*margin);		
		
		{	int line = 0;
			int col = 0;
			// name
			{	// size
				result.setColSubMinWidth(col,fileWidth);
				result.setColSubPrefWidth(col,fileWidth);
				result.setColSubMaxWidth(col,fileWidth);
				// color
				Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
				result.setLabelBackground(line,col,bg);
				// next
				col++;
			}
			// browse
			{	// size
				result.setColSubMinWidth(col,iconWidth);
				result.setColSubPrefWidth(col,iconWidth);
				result.setColSubMaxWidth(col,iconWidth);
				// icon
				String key = GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_SELECT;
				result.setLabelKey(line,col,key,true);
				// color
				Color bg = GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND;
				result.setLabelBackground(line,col,bg);
				// listener
				MyLabel lbl = result.getLabel(line,col);
				lbl.addMouseListener(this);
				lbl.setMouseSensitive(true);
				// next
				col++;
			}
		}
		
		return result;	
	}
}

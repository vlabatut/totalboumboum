package org.totalboumboum.gui.menus.tournament.select;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import org.totalboumboum.configuration.game.tournament.TournamentConfiguration;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.DataPanelListener;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.menus.explore.tournaments.select.SelectedTournamentData;
import org.totalboumboum.gui.tools.GuiButtonTools;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiFontTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.tools.files.FilePaths;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class SelectTournamentMenu extends InnerMenuPanel implements DataPanelListener
{	private static final long serialVersionUID = 1L;
	
	private TournamentConfiguration tournamentConfiguration;
	private String folder;
	private SelectedTournamentData tournamentData;

	public SelectTournamentMenu(SplitMenuPanel container, MenuPanel parent, TournamentConfiguration tournamentConfiguration)
	{	super(container, parent);
		this.tournamentConfiguration = tournamentConfiguration;
	
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiColorTools.COLOR_COMMON_BACKGROUND);

		// sizes
		int buttonWidth = getWidth();
		int buttonHeight = GuiSizeTools.buttonTextHeight;
		List<String> texts = GuiKeys.getKeysLike(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON);
		int fontSize = GuiFontTools.getOptimalFontSize(buttonWidth*0.8, buttonHeight*0.9, texts);

		// buttons
		add(Box.createVerticalGlue());
		buttonConfirm = GuiButtonTools.createButton(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_CONFIRM,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiSizeTools.buttonVerticalSpace)));
		buttonCancel = GuiButtonTools.createButton(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_CANCEL,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createVerticalGlue());		

		// panels
		folder = FilePaths.getTournamentsPath();
		tournamentData = new SelectedTournamentData(container,folder);
		container.setDataPart(tournamentData);
		tournamentData.addListener(this);
		refreshButtons();
	}

	/////////////////////////////////////////////////////////////////
	// BUTTONS						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	private JButton buttonCancel;
	private JButton buttonConfirm;

	private void refreshButtons()
	{	AbstractTournament tournament = tournamentData.getSelectedTournament();
		if(tournament==null)
			buttonConfirm.setEnabled(false);
		else
			buttonConfirm.setEnabled(true);
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTION LISTENER				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_CANCEL))
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_CONFIRM))
		{	String newName = tournamentData.getSelectedTournamentFile();
			if(newName!=null)
			{	StringBuffer oldName = tournamentConfiguration.getTournamentName();
				if(oldName!=null && !oldName.toString().equals(newName))
					tournamentConfiguration.resetAutoAdvanceIndex();
				tournamentConfiguration.setTournamentName(new StringBuffer(newName));
				AbstractTournament tournament = tournamentData.getSelectedTournament();
				tournamentConfiguration.setTournament(tournament);
			}
			parent.refresh();
			replaceWith(parent);
	    }
	} 
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
	{	//
	}
	
	/////////////////////////////////////////////////////////////////
	// DATA PANEL LISTENER	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void dataPanelSelectionChanged(Object object)
	{	refreshButtons();
	}

	@Override
	public void mousePressed(MouseEvent e)
	{	// nothing to do here
	}
}

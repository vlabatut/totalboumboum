package fr.free.totalboumboum.gui.menus.quickmatch.players;

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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.configuration.profile.ProfilesConfiguration;
import fr.free.totalboumboum.configuration.profile.ProfilesSelection;
import fr.free.totalboumboum.game.tournament.single.SingleTournament;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.menus.quickmatch.levels.SelectedLevelSplitPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class PlayersMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	private SelectedLevelSplitPanel levelsPanel;
	private PlayersData profilesData;
		
	@SuppressWarnings("unused")
	private JButton buttonQuit;
	@SuppressWarnings("unused")
	private JButton buttonPrevious;
	@SuppressWarnings("unused")
	private JButton buttonNext;
	
	public PlayersMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container,parent);
	
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.LINE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
		
		// sizes
		int buttonWidth = getHeight();
		int buttonHeight = getHeight();

		// buttons
		buttonQuit = GuiTools.createButton(GuiKeys.MENU_QUICKMATCH_PLAYERS_BUTTON_QUIT,buttonWidth,buttonHeight,1,this);
		add(Box.createHorizontalGlue());
		buttonPrevious = GuiTools.createButton(GuiKeys.MENU_QUICKMATCH_PLAYERS_BUTTON_PREVIOUS,buttonWidth,buttonHeight,1,this);
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		buttonNext = GuiTools.createButton(GuiKeys.MENU_QUICKMATCH_PLAYERS_BUTTON_NEXT,buttonWidth,buttonHeight,1,this);
		
		// panels
		profilesData = new PlayersData(container);
		container.setDataPart(profilesData);
	}
	
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT					/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private SingleTournament tournament;
	
	public void setTournament(SingleTournament tournament)
	{	// init tournament
		this.tournament = tournament;
		// init data
		ProfilesSelection profilesSelection = new ProfilesSelection();
		if(Configuration.getGameConfiguration().getUseLastPlayers())
			profilesSelection = Configuration.getGameConfiguration().getQuickMatchSelectedProfiles();		
		profilesData.setProfilesSelection(profilesSelection);
		// transmit
		if(levelsPanel!=null)
			levelsPanel.setTournament(tournament);
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTION LISTENER				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_PLAYERS_BUTTON_QUIT))
		{	getFrame().setMainMenuPanel();
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_PLAYERS_BUTTON_PREVIOUS))				
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_PLAYERS_BUTTON_NEXT))
		{	// set profiles in tournament
			ArrayList<Profile> selectedProfiles = profilesData.getSelectedProfiles();
			tournament.setProfiles(selectedProfiles);
			// set profiles in configuration
			ProfilesSelection profilesSelection = ProfilesConfiguration.getSelection(selectedProfiles);
			Configuration.getGameConfiguration().setQuickMatchSelectedProfiles(profilesSelection);
			// set levels panel
			if(levelsPanel==null)
			{	levelsPanel = new SelectedLevelSplitPanel(container.getContainer(),container);
				levelsPanel.setTournament(tournament);
			}			
			replaceWith(levelsPanel);
	    }
	} 

	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// PAINT				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
    protected void paintComponent(Graphics g)
	{	//g.clearRect(0, 0, getWidth(), getHeight());
//		getParent().paintComponents(g);
		super.paintComponent(g);
    }
}

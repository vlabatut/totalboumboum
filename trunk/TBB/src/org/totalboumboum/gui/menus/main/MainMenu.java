package org.totalboumboum.gui.menus.main;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.gui.common.structure.MenuContainer;
import org.totalboumboum.gui.common.structure.dialog.outside.ModalDialogPanelListener;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.SimpleMenuPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.menus.about.AboutModalDialogPanel;
import org.totalboumboum.gui.menus.explore.ExploreSplitPanel;
import org.totalboumboum.gui.menus.network.NetworkContainer;
import org.totalboumboum.gui.menus.options.OptionsSplitPanel;
import org.totalboumboum.gui.menus.profiles.select.SelectedProfileSplitPanel;
import org.totalboumboum.gui.menus.quickmatch.QuickMatchContainer;
import org.totalboumboum.gui.menus.replay.ReplayContainer;
import org.totalboumboum.gui.menus.statistics.players.PlayerStatisticsSplitPanel;
import org.totalboumboum.gui.menus.tournament.TournamenuContainer;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class MainMenu extends SimpleMenuPanel implements ModalDialogPanelListener
{	private static final long serialVersionUID = 1L;
		
	public MainMenu(MenuContainer container, MenuPanel parent) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	super(container,parent);
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);

		// background
		image = GuiConfiguration.getMiscConfiguration().getBackground();
		
		// sizes
		int buttonWidth = GuiTools.buttonTextWidth;
		int buttonHeight = GuiTools.buttonTextHeight;
		List<String> texts = GuiKeys.getKeysLike(GuiKeys.MENU_MAIN_BUTTON);
		int fontSize = GuiTools.getOptimalFontSize(buttonWidth*0.8, buttonHeight*0.9, texts);
		
		// buttons
		add(Box.createVerticalGlue());
		buttonOptions = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_OPTIONS,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonProfiles = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_PROFILES,buttonWidth,buttonHeight,fontSize,this);
		buttonStats = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_STATISTICS,buttonWidth,buttonHeight,fontSize,this);
		buttonResources = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_RESOURCES,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonTournament = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_TOURNAMENT,buttonWidth,buttonHeight,fontSize,this);
		buttonQuickMatch = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_QUICKMATCH,buttonWidth,buttonHeight,fontSize,this);
		buttonNetworkGame = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_NETWORK,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonLoad = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_LOAD,buttonWidth,buttonHeight,fontSize,this);
		buttonReplay = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_REPLAY,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonAbout = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_ABOUT,buttonWidth,buttonHeight,fontSize,this);
		buttonQuit = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_QUIT,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createVerticalGlue());		
	}
	
	/////////////////////////////////////////////////////////////////
	// BUTTONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	private JButton buttonOptions;
	@SuppressWarnings("unused")
	private JButton buttonProfiles;
	@SuppressWarnings("unused")
	private JButton buttonStats;
	@SuppressWarnings("unused")
	private JButton buttonResources;
	@SuppressWarnings("unused")
	private JButton buttonAbout;
	@SuppressWarnings("unused")
	private JButton buttonTournament;
	@SuppressWarnings("unused")
	private JButton buttonQuickMatch;
	@SuppressWarnings("unused")
	private JButton buttonNetworkGame;
	@SuppressWarnings("unused")
	private JButton buttonLoad;
	@SuppressWarnings("unused")
	private JButton buttonReplay;
	@SuppressWarnings("unused")
	private JButton buttonQuit;
	
	/////////////////////////////////////////////////////////////////
	// PANELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private TournamenuContainer tournamentContainer;
	private QuickMatchContainer quickMatchContainer;
	private NetworkContainer networkContainer;
	
	/////////////////////////////////////////////////////////////////
	// JCOMPONENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private BufferedImage image;

	@Override
	public void paintComponent(Graphics g)
	{	g.drawImage(image, 0, 0, null);
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTION LISTENER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_OPTIONS))
		{	OptionsSplitPanel optionsMenuPanel = new OptionsSplitPanel(getMenuContainer(),this);
			replaceWith(optionsMenuPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_PROFILES))
		{	SelectedProfileSplitPanel profilesMenuPanel = new SelectedProfileSplitPanel(getMenuContainer(),this);
			replaceWith(profilesMenuPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_RESOURCES))
		{	ExploreSplitPanel exploreMenuPanel = new ExploreSplitPanel(getMenuContainer(),this);
			replaceWith(exploreMenuPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_STATISTICS))
		{	PlayerStatisticsSplitPanel statisticsMenuPanel = new PlayerStatisticsSplitPanel(getMenuContainer(),this);
			replaceWith(statisticsMenuPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_LOAD))
		{	if(tournamentContainer==null)
				tournamentContainer = new TournamenuContainer(getMenuContainer(),this);
			tournamentContainer.initLoad();
			replaceWith(tournamentContainer);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_REPLAY))
		{	ReplayContainer replayContainer = new ReplayContainer(getMenuContainer(),this);
			replaceWith(replayContainer);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_QUIT))
		{	getFrame().exit(false);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_TOURNAMENT))
		{	if(tournamentContainer==null)
				tournamentContainer = new TournamenuContainer(getMenuContainer(),this);
			tournamentContainer.initTournament();
			replaceWith(tournamentContainer);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_QUICKMATCH))
		{	if(quickMatchContainer==null)
				quickMatchContainer = new QuickMatchContainer(getMenuContainer(),this);
			quickMatchContainer.initTournament();
			replaceWith(quickMatchContainer);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_NETWORK))
		{	if(networkContainer==null)
				networkContainer = new NetworkContainer(getMenuContainer(),this);
			networkContainer.initTournament();
			replaceWith(networkContainer);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_ABOUT))
		{	
/*			
			ArrayList<String> text = new ArrayList<String>();
			text.add("blablablablab !");
			text.add("blëblëblëblëb !");
			InfoModalDialogPanel aboutPanel = new InfoModalDialogPanel(this,"Test","Ceci est un test",text);
			aboutPanel.addListener(this);
			getFrame().setModalDialog(aboutPanel);
*/			
			AboutModalDialogPanel aboutPanel = new AboutModalDialogPanel(this);
			aboutPanel.addListener(this);
			getFrame().setModalDialog(aboutPanel);
	    }
	}
	
	/////////////////////////////////////////////////////////////////
	// REFRESH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
	{	
/*		
		if(tournamentMainPanel!=null)
			buttonQuickMatch.setEnabled(false);
		if(quickMatchSplitPanel!=null)
			buttonTournament.setEnabled(false);
*/		
	}
	
	/////////////////////////////////////////////////////////////////
	// MODAL DIALOG PANEL LISTENER	/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void modalDialogButtonClicked(String buttonCode)
	{	getFrame().unsetModalDialog();
	}
}

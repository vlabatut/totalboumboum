package fr.free.totalboumboum.gui.menus.options.game;

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
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.configuration.profile.ProfileLoader;
import fr.free.totalboumboum.configuration.profile.ProfilesSelection;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.InnerDataPanel;
import fr.free.totalboumboum.gui.common.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.menus.options.game.quickstart.QuickstartSplitPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class GameMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private JButton buttonQuickStart;
	private JButton buttonQuickMatch;
	private JButton buttonTournament;
	
	@SuppressWarnings("unused")
	private JButton buttonBack;

	private InnerDataPanel optionsData;

	public GameMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);

		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);

		// sizes
		int buttonWidth = getWidth();
		int buttonHeight = GuiTools.buttonTextHeight;
		ArrayList<String> texts = GuiKeys.getKeysLike(GuiKeys.MENU_OPTIONS_GAME_BUTTON);
		int fontSize = GuiTools.getOptimalFontSize(buttonWidth*0.8, buttonHeight*0.9, texts);

		// buttons
		add(Box.createVerticalGlue());
		buttonQuickStart = GuiTools.createButton(GuiKeys.MENU_OPTIONS_GAME_BUTTON_QUICKSTART,buttonWidth,buttonHeight,fontSize,this);
		buttonQuickMatch = GuiTools.createButton(GuiKeys.MENU_OPTIONS_GAME_BUTTON_QUICKMATCH,buttonWidth,buttonHeight,fontSize,this);
buttonQuickMatch.setEnabled(false);
		buttonTournament = GuiTools.createButton(GuiKeys.MENU_OPTIONS_GAME_BUTTON_TOURNAMENT,buttonWidth,buttonHeight,fontSize,this);
buttonTournament.setEnabled(false);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonBack = GuiTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_BACK,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createVerticalGlue());		

		// panels
		optionsData = new GameData(container);
		container.setDataPart(optionsData);
}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_GAME_BUTTON_QUICKSTART))
		{	try
			{	ProfilesSelection profilesSelection = Configuration.getProfilesConfiguration().getQuickStartSelected();
				ArrayList<Profile> profiles = ProfileLoader.loadProfiles(profilesSelection);
				String roundFile = Configuration.getGameConfiguration().getQuickstartName();
				QuickstartSplitPanel quickstartPanel = new QuickstartSplitPanel(container.getContainer(),container,profiles,roundFile);
				replaceWith(quickstartPanel);
			}
			catch (IllegalArgumentException e1)
			{	e1.printStackTrace();
			}
			catch (SecurityException e1)
			{	e1.printStackTrace();
			}
			catch (ParserConfigurationException e1)
			{	e1.printStackTrace();
			}
			catch (SAXException e1)
			{	e1.printStackTrace();
			}
			catch (IOException e1)
			{	e1.printStackTrace();
			}
			catch (IllegalAccessException e1)
			{	e1.printStackTrace();
			}
			catch (NoSuchFieldException e1)
			{	e1.printStackTrace();
			}
			catch (ClassNotFoundException e1)
			{	e1.printStackTrace();
			}
		}
	else if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_GAME_BUTTON_QUICKMATCH))
		{	
//			VideoSplitPanel videoPanel = new VideoSplitPanel(container.getContainer(),container);
//			replaceWith(videoPanel);
		}
		else if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_GAME_BUTTON_TOURNAMENT))
		{	
//			VideoSplitPanel videoPanel = new VideoSplitPanel(container.getContainer(),container);
//			replaceWith(videoPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_BACK))
		{	replaceWith(parent);
	    }
	} 
	
	public void refresh()
	{	//
	}
}

package fr.free.totalboumboum.gui.menus.main;

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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.gui.common.structure.MenuContainer;
import fr.free.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.SimpleMenuPanel;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.menus.explore.ExploreSplitPanel;
import fr.free.totalboumboum.gui.menus.options.OptionsSplitPanel;
import fr.free.totalboumboum.gui.menus.profiles.select.SelectedProfileSplitPanel;
import fr.free.totalboumboum.gui.menus.quickmatch.QuickMatchContainer;
import fr.free.totalboumboum.gui.menus.tournament.TournamentSplitPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class MainMenu extends SimpleMenuPanel
{	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;

	private MenuPanel tournamentMainPanel;
	private QuickMatchContainer quickMatchContainer;
	
	@SuppressWarnings("unused")
	private JButton buttonOptions;
	@SuppressWarnings("unused")
	private JButton buttonProfiles;
	private JButton buttonStats;
	@SuppressWarnings("unused")
	private JButton buttonResources;
	private JButton buttonAbout;
	@SuppressWarnings("unused")
	private JButton buttonTournament;
	@SuppressWarnings("unused")
	private JButton buttonQuickMatch;
	@SuppressWarnings("unused")
	private JButton buttonLoad;
	@SuppressWarnings("unused")
	private JButton buttonQuit;
	
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
		ArrayList<String> texts = GuiKeys.getKeysLike(GuiKeys.MENU_MAIN_BUTTON);
		int fontSize = GuiTools.getOptimalFontSize(buttonWidth*0.8, buttonHeight*0.9, texts);
		
		// buttons
		add(Box.createVerticalGlue());
		buttonOptions = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_OPTIONS,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonProfiles = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_PROFILES,buttonWidth,buttonHeight,fontSize,this);
		buttonStats = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_STATISTICS,buttonWidth,buttonHeight,fontSize,this);
buttonStats.setEnabled(false);
		buttonResources = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_RESOURCES,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonTournament = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_TOURNAMENT,buttonWidth,buttonHeight,fontSize,this);
		buttonQuickMatch = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_QUICKMATCH,buttonWidth,buttonHeight,fontSize,this);
		buttonLoad = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_LOAD,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonAbout = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_ABOUT,buttonWidth,buttonHeight,fontSize,this);
buttonAbout.setEnabled(false);
		buttonQuit = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_QUIT,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createVerticalGlue());		
	}
	
	@Override
	public void paintComponent(Graphics g)
	{	g.drawImage(image, 0, 0, null);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_OPTIONS))
		{	OptionsSplitPanel optionsMenuPanel = new OptionsSplitPanel(getContainer(),this);
			replaceWith(optionsMenuPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_PROFILES))
		{	SelectedProfileSplitPanel profilesMenuPanel = new SelectedProfileSplitPanel(getContainer(),this);
			replaceWith(profilesMenuPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_RESOURCES))
		{	ExploreSplitPanel exploreMenuPanel = new ExploreSplitPanel(getContainer(),this);
			replaceWith(exploreMenuPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_STATISTICS))
		{	
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_LOAD))
		{	
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_QUIT))
		{	getFrame().exit();
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_TOURNAMENT))
		{	if(tournamentMainPanel==null)
			{	try
				{	tournamentMainPanel = new TournamentSplitPanel(getContainer(),this);
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
			}
			replaceWith(tournamentMainPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_QUICKMATCH))
		{	if(quickMatchContainer==null)
				quickMatchContainer = new QuickMatchContainer(getContainer(),this);
			quickMatchContainer.initTournament();
			replaceWith(quickMatchContainer);
	    }
	}
	
	public void refresh()
	{	
/*		
		if(tournamentMainPanel!=null)
			buttonQuickMatch.setEnabled(false);
		if(quickMatchSplitPanel!=null)
			buttonTournament.setEnabled(false);
*/		
	}
}

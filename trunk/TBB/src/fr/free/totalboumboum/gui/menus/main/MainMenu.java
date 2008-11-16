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

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.gui.ais.select.SelectedAiSplitPanel;
import fr.free.totalboumboum.gui.common.MenuContainer;
import fr.free.totalboumboum.gui.common.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.SimpleMenuPanel;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.game.match.MatchSplitPanel;
import fr.free.totalboumboum.gui.heroes.select.SelectedHeroSplitPanel;
import fr.free.totalboumboum.gui.levels.select.SelectedLevelSplitPanel;
import fr.free.totalboumboum.gui.menus.tournament.TournamentSplitPanel;
import fr.free.totalboumboum.gui.options.OptionsSplitPanel;
import fr.free.totalboumboum.gui.profiles.select.SelectedProfileSplitPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class MainMenu extends SimpleMenuPanel
{	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;

	private MenuPanel tournamentMainPanel;
	private MenuPanel quickmatchGamePanel;
	
	@SuppressWarnings("unused")
	private JButton buttonOptions;
	@SuppressWarnings("unused")
	private JButton buttonProfiles;
	private JButton buttonStats;
	@SuppressWarnings("unused")
	private JButton buttonAi;
	@SuppressWarnings("unused")
	private JButton buttonHeroes;
	@SuppressWarnings("unused")
	private JButton buttonLevels;
	private JButton buttonAbout;
	private JButton buttonTournament;
	private JButton buttonQuickMatch;
	
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
		buttonHeroes = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_HEROES,buttonWidth,buttonHeight,fontSize,this);
		buttonAi = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_AI,buttonWidth,buttonHeight,fontSize,this);
		buttonLevels = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_LEVELS,buttonWidth,buttonHeight,fontSize,this);
		buttonAbout = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_ABOUT,buttonWidth,buttonHeight,fontSize,this);
buttonAbout.setEnabled(false);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonTournament = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_TOURNAMENT,buttonWidth,buttonHeight,fontSize,this);
		buttonQuickMatch = GuiTools.createButton(GuiKeys.MENU_MAIN_BUTTON_QUICKMATCH,buttonWidth,buttonHeight,fontSize,this);
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
		else if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_AI))
		{	SelectedAiSplitPanel aiMenuPanel = new SelectedAiSplitPanel(getContainer(),this);
			replaceWith(aiMenuPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_STATISTICS))
		{	
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_HEROES))
		{	SelectedHeroSplitPanel heroMenuPanel = new SelectedHeroSplitPanel(getContainer(),this);
			replaceWith(heroMenuPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_LEVELS))
		{	SelectedLevelSplitPanel levelMenuPanel = new SelectedLevelSplitPanel(getContainer(),this);
			replaceWith(levelMenuPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_MAIN_BUTTON_TOURNAMENT))
		{	if(tournamentMainPanel==null)
			{	try
				{	Configuration.getGameConfiguration().loadLastTournament();
					tournamentMainPanel = new TournamentSplitPanel(getContainer(),this);
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
				catch (ClassNotFoundException e1)
				{	e1.printStackTrace();
				}
				catch (IllegalArgumentException e1)
				{	e1.printStackTrace();
				}
				catch (SecurityException e1)
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
		{	if(quickmatchGamePanel==null)
			{	try
				{	Configuration.getGameConfiguration().loadQuickmatch();
					AbstractTournament tournament = Configuration.getGameConfiguration().getTournament();
					tournament.init();
					tournament.progress();
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
				catch (ClassNotFoundException e1)
				{	e1.printStackTrace();
				}
				catch (IllegalArgumentException e1)
				{	e1.printStackTrace();
				}
				catch (SecurityException e1)
				{	e1.printStackTrace();
				}
				catch (IllegalAccessException e1)
				{	e1.printStackTrace();
				}
				catch (NoSuchFieldException e1)
				{	e1.printStackTrace();
				}
				quickmatchGamePanel = new MatchSplitPanel(getContainer(),this);
			}			
			replaceWith(quickmatchGamePanel);
	    }
	}
	
	public void refresh()
	{	if(tournamentMainPanel!=null)
			buttonQuickMatch.setEnabled(false);
		if(quickmatchGamePanel!=null)
			buttonTournament.setEnabled(false);
	}
}

package fr.free.totalboumboum.gui.menus.quickmatch.levels;

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
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.game.LevelsSelection;
import fr.free.totalboumboum.engine.container.level.HollowLevel;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.tournament.single.SingleTournament;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.menus.quickmatch.settings.SettingsSplitPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class LevelsMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	private SettingsSplitPanel settingsPanel;

	@SuppressWarnings("unused")
	private JButton buttonQuit;
	@SuppressWarnings("unused")
	private JButton buttonPrevious;
	@SuppressWarnings("unused")
	private JButton buttonNext;

	private LevelsData levelData;

	public LevelsMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.LINE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);

		// sizes
		int buttonWidth = getHeight();
		int buttonHeight = getHeight();

		// buttons
		buttonQuit = GuiTools.createButton(GuiKeys.MENU_QUICKMATCH_LEVELS_BUTTON_QUIT,buttonWidth,buttonHeight,1,this);
		add(Box.createHorizontalGlue());
		buttonPrevious = GuiTools.createButton(GuiKeys.MENU_QUICKMATCH_LEVELS_BUTTON_PREVIOUS,buttonWidth,buttonHeight,1,this);
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		buttonNext = GuiTools.createButton(GuiKeys.MENU_QUICKMATCH_LEVELS_BUTTON_NEXT,buttonWidth,buttonHeight,1,this);

		// panels
		levelData = new LevelsData(container);
		container.setDataPart(levelData);
	}
	
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT					/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private SingleTournament tournament;
	
	public void setTournament(SingleTournament tournament)
	{	// init tournament
		this.tournament = tournament;
		// init data
		LevelsSelection levelsSelection = new LevelsSelection();
		if(Configuration.getGameConfiguration().getQuickMatchUseLastLevels())
			levelsSelection = Configuration.getGameConfiguration().getQuickMatchSelectedLevels();
		levelData.setLevelsSelection(levelsSelection);
		// transmit
		if(settingsPanel!=null)
		{	settingsPanel.setTournament(tournament);
		}			
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTION LISTENER				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_LEVELS_BUTTON_QUIT))
		{	getFrame().setMainMenuPanel();
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_LEVELS_BUTTON_PREVIOUS))				
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_LEVELS_BUTTON_NEXT))
		{	LevelsSelection levelsSelection = levelData.getLevelsSelection();
			// set levels in match
			Match match = tournament.getCurrentMatch();
			match.clearRounds();
			for(int i=0;i<levelsSelection.getLevelCount();i++)
			{	String folderName = levelsSelection.getFolderName(i);
				String packName = levelsSelection.getPackName(i);
				Round round = new Round(match);
				match.addRound(round);
				String path = packName+File.separator+folderName;
		    	try
				{	HollowLevel hollowLevel = new HollowLevel(path);
			    	round.setHollowLevel(hollowLevel);
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
			}
			// set levels in configuration
			Configuration.getGameConfiguration().setQuickMatchSelectedLevels(levelsSelection);
			// set levels panel
			if(settingsPanel==null)
			{	settingsPanel = new SettingsSplitPanel(container.getContainer(),container);
			}			
			settingsPanel.setTournament(tournament);
			replaceWith(settingsPanel);
	    }
/*			
		{	
			if(matchSplitPanel==null)
			{	//TODO temporaire
				try
				{	// load
					Configuration.getGameConfiguration().loadQuickmatch();
					SingleTournament tournament = (SingleTournament)Configuration.getGameConfiguration().getTournament();
					ArrayList<Profile> selectedProfiles = profilesData.getSelectedProfiles();
					tournament.init(selectedProfiles);
					tournament.progress();
					// GUI
					matchSplitPanel = new MatchSplitPanel(container.getContainer(),container);
					Match match = tournament.getCurrentMatch();
					matchSplitPanel.setMatch(match);
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
			replaceWith(matchSplitPanel);
	    }
*/			
	} 
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void refresh()
	{	//
	}
}

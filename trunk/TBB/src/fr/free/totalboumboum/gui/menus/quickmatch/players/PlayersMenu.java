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
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.menus.quickmatch.match.MatchSplitPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class PlayersMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	private MatchSplitPanel matchSplitPanel;
	private PlayersData profilesData;
		
	@SuppressWarnings("unused")
	private JButton buttonQuit;
	@SuppressWarnings("unused")
	private JButton buttonPrevious;
	private JToggleButton buttonDescription;
	private JToggleButton buttonResults;
	private JToggleButton buttonStatistics;
	@SuppressWarnings("unused")
	private JButton buttonNext;
	
	public PlayersMenu(SplitMenuPanel container, MenuPanel parent, ArrayList<Profile> selectedProfiles)
	{	super(container,parent);
	
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.LINE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
		
		// sizes
		int buttonWidth = getHeight();
		int buttonHeight = getHeight();
//		ArrayList<String> texts = GuiKeys.getKeysLike(GuiKeys.MENU_TOURNAMENT_BUTTON);
//		int fontSize = GuiTools.getOptimalFontSize(buttonWidth, buttonHeight, texts);

		// buttons
		buttonQuit = GuiTools.createButton(GuiKeys.MENU_QUICKMATCH_BUTTON_QUIT,buttonWidth,buttonHeight,1,this);
		add(Box.createHorizontalGlue());
		buttonPrevious = GuiTools.createButton(GuiKeys.MENU_QUICKMATCH_BUTTON_PREVIOUS,buttonWidth,buttonHeight,1,this);
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
	    ButtonGroup group = new ButtonGroup();
	    buttonDescription = GuiTools.createToggleButton(GuiKeys.MENU_QUICKMATCH_BUTTON_DESCRIPTION,buttonWidth,buttonHeight,1,this);
	    buttonDescription.setEnabled(false);		
	    group.add(buttonDescription);
	    buttonResults = GuiTools.createToggleButton(GuiKeys.MENU_QUICKMATCH_BUTTON_RESULTS,buttonWidth,buttonHeight,1,this);
	    buttonResults.setEnabled(false);		
	    group.add(buttonResults);
	    buttonStatistics = GuiTools.createToggleButton(GuiKeys.MENU_QUICKMATCH_BUTTON_STATISTICS,buttonWidth,buttonHeight,1,this);
	    buttonStatistics.setEnabled(false);		
	    group.add(buttonStatistics);
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		buttonNext = GuiTools.createButton(GuiKeys.MENU_QUICKMATCH_BUTTON_NEXT,buttonWidth,buttonHeight,1,this);
		
		profilesData = new PlayersData(container,selectedProfiles);
		container.setDataPart(profilesData);
/*		
		Match match = Configuration.getGameConfiguration().getTournament().getCurrentMatch();
		match.setPanel(this);
*/		
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_BUTTON_QUIT))
		{	getFrame().setMainMenuPanel();
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_BUTTON_PREVIOUS))				
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_BUTTON_DESCRIPTION))
		{	// never used
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_BUTTON_RESULTS))
		{	// never used
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_BUTTON_STATISTICS))
		{	// never used
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_BUTTON_NEXT))
		{	if(matchSplitPanel==null)
			{	//TODO temporaire
				try
				{	Configuration.getGameConfiguration().loadQuickmatch();
					AbstractTournament tournament = Configuration.getGameConfiguration().getTournament();
					ArrayList<Profile> selectedProfiles = profilesData.getSelectedProfiles();
					tournament.init(selectedProfiles);
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
				
				// GUI
				matchSplitPanel = new MatchSplitPanel(container.getContainer(),container);			
			}
			replaceWith(matchSplitPanel);
	    }
	} 

	@Override
	public void refresh()
	{	
	}
	
	@Override
    protected void paintComponent(Graphics g)
	{	//g.clearRect(0, 0, getWidth(), getHeight());
//		getParent().paintComponents(g);
		super.paintComponent(g);
    }
}

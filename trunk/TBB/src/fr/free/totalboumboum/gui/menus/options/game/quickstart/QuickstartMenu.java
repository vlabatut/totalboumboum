package fr.free.totalboumboum.gui.menus.options.game.quickstart;

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
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.game.GameConfiguration;
import fr.free.totalboumboum.configuration.game.GameConfigurationSaver;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.configuration.profile.ProfilesConfiguration;
import fr.free.totalboumboum.configuration.profile.ProfilesConfigurationSaver;
import fr.free.totalboumboum.configuration.profile.ProfilesSelection;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class QuickstartMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	private QuickstartData quickstartPanel;
	
	@SuppressWarnings("unused")
	private JButton buttonConfirm;
	@SuppressWarnings("unused")
	private JButton buttonCancel;
	
	private ArrayList<Profile> selectedProfiles;
	private String roundFile;
	
	public QuickstartMenu(SplitMenuPanel container, MenuPanel parent, ArrayList<Profile> selectedProfiles, String roundFile)
	{	super(container,parent);
		this.selectedProfiles = selectedProfiles;
		this.roundFile = roundFile;
		ArrayList<Profile> selectedProfilesCopy = new ArrayList<Profile>();
		for(Profile p: selectedProfiles)
			selectedProfilesCopy.add(p.copy());		
	
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
	
		// sizes
		int buttonWidth = getWidth();
		int buttonHeight = GuiTools.buttonTextHeight;
		ArrayList<String> texts = GuiKeys.getKeysLike(GuiKeys.MENU_OPTIONS_BUTTON);
		int fontSize = GuiTools.getOptimalFontSize(buttonWidth*0.8, buttonHeight*0.9, texts);
	
		// buttons
		add(Box.createVerticalGlue());
		buttonConfirm = GuiTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_CONFIRM,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonCancel = GuiTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_CANCEL,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createVerticalGlue());		
	
		// panels
		quickstartPanel = new QuickstartData(container,selectedProfilesCopy,roundFile);
		container.setDataPart(quickstartPanel);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_CONFIRM))
		{	ArrayList<Profile> sProfiles = quickstartPanel.getSelectedProfiles();
			String sRound = quickstartPanel.getSelectedRound();
			try
			{	// round name
				if(!sRound.equalsIgnoreCase(roundFile))
				{	GameConfiguration gameConfiguration = Configuration.getGameConfiguration();
					gameConfiguration.setQuickstartName(sRound);
					GameConfigurationSaver.saveGameConfiguration(gameConfiguration);
				}
				// selected profiles
				{	boolean save = false;
					Iterator<Profile> it1 = selectedProfiles.iterator();
					Iterator<Profile> it2 = sProfiles.iterator();
					while(!save && it1.hasNext() && it2.hasNext())
					{	Profile p1 = it1.next();
						Profile p2 = it2.next();
						if(!p1.isTheSame(p2))
							save = true;
					}
					if(it1.hasNext() || it2.hasNext())
						save = true;
					if(save)
					{	ProfilesSelection profilesSelection = ProfilesConfiguration.getSelection(sProfiles);
						ProfilesConfiguration profilesConfiguration = Configuration.getProfilesConfiguration();
						profilesConfiguration.setQuickStartSelected(profilesSelection);
						ProfilesConfigurationSaver.saveProfilesConfiguration(profilesConfiguration);
					}
					
				}
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
			replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_CANCEL))
		{	replaceWith(parent);
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

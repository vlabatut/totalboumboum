package fr.free.totalboumboum.gui.menus.options.game.quickstart.profile;

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
import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.configuration.profile.ProfileLoader;
import fr.free.totalboumboum.configuration.profile.ProfilesConfiguration;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.menus.profiles.select.SelectedProfileData;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class SelectProfileMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private JButton buttonCancel;
	@SuppressWarnings("unused")
	private JButton buttonConfirm;

	private int index;
	private ArrayList<Profile> profiles;
	
	private SelectedProfileData profileData;

	public SelectProfileMenu(SplitMenuPanel container, MenuPanel parent, int index, ArrayList<Profile> profiles)
	{	super(container, parent);
		this.index = index;
		this.profiles = profiles;
	
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
		profileData = new SelectedProfileData(container);
		container.setDataPart(profileData);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_CANCEL))
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_CONFIRM))
		{	ProfilesConfiguration profilesConfiguration = Configuration.getProfilesConfiguration();
			Profile profile = profileData.getSelectedProfile();
			if(profile!=null && !profiles.contains(profile))
			{	// check if color is free
				PredefinedColor selectedColor = profile.getSpriteColor();
				while(!profilesConfiguration.isFreeColor(profiles,selectedColor))
					selectedColor = profilesConfiguration.getNextFreeColor(profiles,profile,selectedColor);
				profile.getSelectedSprite().setColor(selectedColor);
				try
				{	ProfileLoader.reloadPortraits(profile);
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
				// add to profiles list
				if(index<profiles.size())
					profiles.set(index,profile);
				else
					profiles.add(profile);
			}
			parent.refresh();
			replaceWith(parent);
	    }
	} 
	
	public void refresh()
	{	//
	}
}

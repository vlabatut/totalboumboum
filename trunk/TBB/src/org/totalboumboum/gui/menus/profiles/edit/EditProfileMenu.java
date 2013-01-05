package org.totalboumboum.gui.menus.profiles.edit;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.profiles.ProfilesConfiguration;
import org.totalboumboum.configuration.profiles.ProfilesConfigurationSaver;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.profile.ProfileSaver;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.menus.profiles.select.SelectedProfileSplitPanel;
import org.totalboumboum.gui.tools.GuiFontTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class EditProfileMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	public EditProfileMenu(SplitMenuPanel container, MenuPanel parent, Profile profile, String profileId)
	{	super(container, parent);
		this.profile = profile;
		this.profileId = profileId;
	
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);

		// sizes
		int buttonWidth = getWidth();
		int buttonHeight = GuiTools.buttonTextHeight;
		List<String> texts = GuiKeys.getKeysLike(GuiKeys.MENU_PROFILES_BUTTON);
		int fontSize = GuiFontTools.getOptimalFontSize(buttonWidth*0.8, buttonHeight*0.9, texts);

		// buttons
		add(Box.createVerticalGlue());
		buttonConfirm = GuiTools.createButton(GuiKeys.MENU_PROFILES_BUTTON_CONFIRM,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonCancel = GuiTools.createButton(GuiKeys.MENU_PROFILES_BUTTON_CANCEL,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createVerticalGlue());		

		// panels
		profileData = new EditProfileData(container,profile);
		container.setDataPart(profileData);
	}
	
	/////////////////////////////////////////////////////////////////
	// PANELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private EditProfileData profileData;

	/////////////////////////////////////////////////////////////////
	// BUTTONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	private JButton buttonConfirm;
	@SuppressWarnings("unused")
	private JButton buttonCancel;

	/////////////////////////////////////////////////////////////////
	// PROFILE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Profile profile;
	private String profileId;

	/////////////////////////////////////////////////////////////////
	// ACTION LISTENER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_PROFILES_BUTTON_CONFIRM))
		{	Profile newProfile = profileData.getProfile();
			if(!profile.isTheSame(newProfile))
			{	try
				{	ProfileSaver.saveProfile(newProfile,profileId);
					if(!profile.getName().equals(newProfile.getName()))
					{	//ProfilesConfiguration profilesConfiguration = Configuration.getProfilesConfiguration();
						ProfilesConfiguration profilesConfiguration = Configuration.getProfilesConfiguration();
						profilesConfiguration.addProfile(profileId,newProfile.getName());
						ProfilesConfigurationSaver.saveProfilesConfiguration(profilesConfiguration);
					}
				}
				catch (IOException e1)
				{	e1.printStackTrace();
				}
				catch (ParserConfigurationException e1)
				{	e1.printStackTrace();
				}
				catch (SAXException e1)
				{	e1.printStackTrace();
				}
			}
			parent.refresh();
			String id = profile.getId();
			((SelectedProfileSplitPanel)parent).setSelectedProfile(id);
			replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_PROFILES_BUTTON_CANCEL))
		{	replaceWith(parent);
	    }
	} 
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void refresh()
	{	//
	}
}

package fr.free.totalboumboum.gui.profiles.select;

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
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.configuration.profile.ProfileSaver;
import fr.free.totalboumboum.configuration.profile.ProfilesConfiguration;
import fr.free.totalboumboum.configuration.profile.ProfilesConfigurationSaver;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.profiles.edit.EditProfileSplitPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;

public class SelectedProfileMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private JButton buttonBack;
	@SuppressWarnings("unused")
	private JButton buttonNew;
	@SuppressWarnings("unused")
	private JButton buttonModify;
	@SuppressWarnings("unused")
	private JButton buttonDelete;

	private SelectedProfileData profileData;

	public SelectedProfileMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);

		// sizes
		int buttonWidth = getWidth();
		int buttonHeight = GuiTools.buttonTextHeight;
		ArrayList<String> texts = GuiKeys.getKeysLike(GuiKeys.MENU_TOURNAMENT_BUTTON);
		int fontSize = GuiTools.getOptimalFontSize(buttonWidth, buttonHeight, texts);

		// buttons
		add(Box.createVerticalGlue());
		buttonNew = GuiTools.createButton(GuiKeys.MENU_PROFILES_BUTTON_NEW,buttonWidth,buttonHeight,fontSize,this);
		buttonModify = GuiTools.createButton(GuiKeys.MENU_PROFILES_BUTTON_MODIFY,buttonWidth,buttonHeight,fontSize,this);
		buttonDelete = GuiTools.createButton(GuiKeys.MENU_PROFILES_BUTTON_DELETE,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonBack = GuiTools.createButton(GuiKeys.MENU_PROFILES_BUTTON_BACK,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createVerticalGlue());		

		// panels
		profileData = new SelectedProfileData(container);
		container.setDataPart(profileData);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_PROFILES_BUTTON_BACK))
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_PROFILES_BUTTON_NEW))
		{	try
			{	// refresh counter
				ProfilesConfiguration profilesConfig = Configuration.getProfilesConfiguration();
				int lastProfile = profilesConfig.getLastProfile();
				int nextProfile = lastProfile+1;
				profilesConfig.setLastProfile(nextProfile);
				// create profile
				Profile newProfile = new Profile();
				String key = GuiKeys.MENU_PROFILES_LIST_NEW_PROFILE;
				String name = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
				newProfile.setName(name);
				String spritePack = "superbomberman1";
				newProfile.setSpritePack(spritePack);
				String spriteFolder = "shirobon";
				newProfile.setSpriteFolder(spriteFolder);
				PredefinedColor spriteColor = PredefinedColor.WHITE;
				newProfile.setSpriteColor(spriteColor,0);
				// create file
				String fileName = Integer.toString(nextProfile)/*+FileTools.EXTENSION_DATA*/;			
				ProfileSaver.saveProfile(newProfile, fileName);
				// add in config
				profilesConfig.addProfile(fileName, name);
				ProfilesConfigurationSaver.saveProfilesConfiguration(profilesConfig);
				// rebuild panel
				profileData = new SelectedProfileData(container);
				container.setDataPart(profileData);
				profileData.setSelectedProfile(fileName);
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
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_PROFILES_BUTTON_MODIFY))
		{	Profile profile = profileData.getSelectedProfile();
			if(profile!=null)
			{	String profileFile = profileData.getSelectedProfileFile();
				EditProfileSplitPanel editPanel = new EditProfileSplitPanel(container.getContainer(),container,profile,profileFile);
				replaceWith(editPanel);
			}
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_PROFILES_BUTTON_DELETE))
		{	Profile profile = profileData.getSelectedProfile();
			if(profile!=null)
			{	try
				{	String profileFile = profileData.getSelectedProfileFile();
					// delete file
					String path = FileTools.getProfilesPath()+File.separator+profileFile+FileTools.EXTENSION_DATA;
					File file = new File(path);
					file.delete();
					// delete entry in config
					ProfilesConfiguration profilesConfig = Configuration.getProfilesConfiguration();
					profilesConfig.removeProfile(profileFile);
					ProfilesConfigurationSaver.saveProfilesConfiguration(profilesConfig);
					// rebuild panel
					profileData = new SelectedProfileData(container);
					container.setDataPart(profileData);
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
				
			}
	    }
	} 
	
	public void refresh()
	{	//
	}
}

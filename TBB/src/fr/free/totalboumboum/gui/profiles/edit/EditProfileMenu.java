package fr.free.totalboumboum.gui.profiles.edit;

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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.configuration.profile.ProfileSaver;
import fr.free.totalboumboum.configuration.profile.ProfilesConfiguration;
import fr.free.totalboumboum.configuration.profile.ProfilesConfigurationSaver;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class EditProfileMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private JButton buttonConfirm;
	@SuppressWarnings("unused")
	private JButton buttonCancel;

	private EditProfileData profileData;
	private Profile profile;
	private String profileFile;

	public EditProfileMenu(SplitMenuPanel container, MenuPanel parent, Profile profile, String profileFile)
	{	super(container, parent);
		this.profile = profile;
		this.profileFile = profileFile;
	
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);

		// buttons
		add(Box.createVerticalGlue());
		buttonConfirm = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_PROFILES_BUTTON_CONFIRM,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.getSize(GuiTools.MENU_VERTICAL_BUTTON_SPACE))));
		buttonCancel = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_PROFILES_BUTTON_CANCEL,this);
		add(Box.createVerticalGlue());		

		// panels
		profileData = new EditProfileData(container,profile);
		container.setDataPart(profileData);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiTools.MENU_PROFILES_BUTTON_CONFIRM))
		{	Profile newProfile = profileData.getProfile();
			boolean hasChanged = 
				profile.hasAi() && (!profile.getAiName().equals(newProfile.getAiName())
						|| profile.getAiPackname().equals(newProfile.getAiPackname()))
				|| profile.getName().equals(newProfile.getName())
				|| profile.getSpriteColor().equals(newProfile.getSpriteColor())
				|| profile.getSpriteFolder().equals(newProfile.getSpriteFolder())
				|| profile.getSpritePack().equals(newProfile.getSpritePack());
			if(hasChanged)
			{	try
				{	ProfileSaver.saveProfile(newProfile,profileFile);
					if(!profile.getName().equals(newProfile.getName()))
					{	//ProfilesConfiguration profilesConfiguration = Configuration.getProfilesConfiguration();

						
/*
 * TODO PB : annuler au niveau du premier menu une action qui a �t� enregistr�e dans un profil au niveau de l'�dition de profil
 * >> tout g�rer au niveau du premier menu, r�pertorier les changements ?	
 * au lieu d'envoyer le profil original, faut envoyer une copie directe, puis comparer la copie et l'enregistrer si modif					
 */
						ProfilesConfiguration profilesConfiguration = Configuration.getProfilesConfiguration();
						profilesConfiguration.addProfile(profileFile,newProfile.getName());
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
			replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_PROFILES_BUTTON_CANCEL))
		{	replaceWith(parent);
	    }
	} 
	
	public void refresh()
	{	//
	}
}

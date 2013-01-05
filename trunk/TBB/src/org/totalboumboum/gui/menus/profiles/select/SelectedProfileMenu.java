package org.totalboumboum.gui.menus.profiles.select;

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
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.profiles.ProfilesConfiguration;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.gui.common.structure.dialog.outside.InputModalDialogPanel;
import org.totalboumboum.gui.common.structure.dialog.outside.ModalDialogPanelListener;
import org.totalboumboum.gui.common.structure.dialog.outside.QuestionModalDialogPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.DataPanelListener;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.menus.profiles.edit.EditProfileSplitPanel;
import org.totalboumboum.gui.tools.GuiButtonTools;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiFontTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class SelectedProfileMenu extends InnerMenuPanel implements DataPanelListener,ModalDialogPanelListener
{	private static final long serialVersionUID = 1L;
	
	private SelectedProfileData profileData;

	public SelectedProfileMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiColorTools.COLOR_COMMON_BACKGROUND);

		// sizes
		int buttonWidth = getWidth();
		int buttonHeight = GuiTools.buttonTextHeight;
		List<String> texts = GuiKeys.getKeysLike(GuiKeys.MENU_PROFILES_BUTTON);
		int fontSize = GuiFontTools.getOptimalFontSize(buttonWidth*0.8, buttonHeight*0.9, texts);

		// buttons
		add(Box.createVerticalGlue());
		buttonNew = GuiButtonTools.createButton(GuiKeys.MENU_PROFILES_BUTTON_NEW,buttonWidth,buttonHeight,fontSize,this);
		buttonModify = GuiButtonTools.createButton(GuiKeys.MENU_PROFILES_BUTTON_MODIFY,buttonWidth,buttonHeight,fontSize,this);
		buttonDelete = GuiButtonTools.createButton(GuiKeys.MENU_PROFILES_BUTTON_DELETE,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonBack = GuiButtonTools.createButton(GuiKeys.MENU_PROFILES_BUTTON_BACK,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createVerticalGlue());		

		// panels
		profileData = new SelectedProfileData(container);
		container.setDataPart(profileData);
		profileData.addListener(this);
		refreshButtons();
	}

	/////////////////////////////////////////////////////////////////
	// BUTTONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	private JButton buttonBack;
	@SuppressWarnings("unused")
	private JButton buttonNew;
	private JButton buttonModify;
	private JButton buttonDelete;

	private void refreshButtons()
	{	Profile profile = profileData.getSelectedProfile();
		if(profile==null)
		{	buttonModify.setEnabled(false);
			buttonDelete.setEnabled(false);
		}
		else
		{	buttonModify.setEnabled(true);
			buttonDelete.setEnabled(true);
		}
	}

	/////////////////////////////////////////////////////////////////
	// ACTION LISTENER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_PROFILES_BUTTON_BACK))
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_PROFILES_BUTTON_NEW))
		{	String key = GuiKeys.MENU_PROFILES_NEW_TITLE;
			List<String> text = new ArrayList<String>();
			text.add(GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_PROFILES_NEW_QUESTION));
			String defaultText = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_PROFILES_NEW_NAME);
			inputPanel = new InputModalDialogPanel(getMenuParent(),key,text,defaultText);
			inputPanel.addListener(this);
			getFrame().setModalDialog(inputPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_PROFILES_BUTTON_MODIFY))
		{	Profile profile = profileData.getSelectedProfile();
			if(profile!=null)
			{	String profileId = profileData.getSelectedProfileId();
				EditProfileSplitPanel editPanel = new EditProfileSplitPanel(container.getMenuContainer(),container,profile,profileId);
				replaceWith(editPanel);
			}
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_PROFILES_BUTTON_DELETE))
		{	String key = GuiKeys.MENU_PROFILES_DELETE_TITLE;
			List<String> text = new ArrayList<String>();
			text.add(GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_PROFILES_DELETE_QUESTION));
			questionPanel = new QuestionModalDialogPanel(getMenuParent(),key,text);
			questionPanel.addListener(this);
			getFrame().setModalDialog(questionPanel);
	    }
	} 
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void refresh()
	{	profileData.removeListener(this);
		profileData = new SelectedProfileData(container);
		container.setDataPart(profileData);
		profileData.addListener(this);
	}

	/////////////////////////////////////////////////////////////////
	// DATA PANEL LISTENER	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void dataPanelSelectionChanged(Object object)
	{	refreshButtons();
	}

	/////////////////////////////////////////////////////////////////
	// MODAL DIALOG PANEL LISTENER	/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private InputModalDialogPanel inputPanel = null;
	private QuestionModalDialogPanel questionPanel = null;
	
	@Override
	public void modalDialogButtonClicked(String buttonCode)
	{	getFrame().unsetModalDialog();
		if(inputPanel!=null)
		{	String input = inputPanel.getInput();
			inputPanel.removeListener(this);
			inputPanel = null;
			if(buttonCode.equals(GuiKeys.COMMON_DIALOG_CONFIRM))
			{	// create & save
				try
				{	// create profile
					ProfilesConfiguration profilesConfig = Configuration.getProfilesConfiguration();
					String id = profilesConfig.createProfile(input);
					// rebuild panel
					refresh();
					profileData.setSelectedProfile(id);
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
				catch (IllegalArgumentException e)
				{	e.printStackTrace();
				}
				catch (SecurityException e)
				{	e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{	e.printStackTrace();
				}
				catch (NoSuchFieldException e)
				{	e.printStackTrace();
				}
				catch (ClassNotFoundException e)
				{	e.printStackTrace();
				}
			}
		}
		else if(questionPanel!=null)
		{	questionPanel.removeListener(this);
			questionPanel = null;				
			Profile profile = profileData.getSelectedProfile();
			String id = profileData.getReplacementProfileId();
			if(profile!=null)
			{	try
				{	// delete profile
					ProfilesConfiguration profilesConfig = Configuration.getProfilesConfiguration();
					profilesConfig.deleteProfile(profile);
					// rebuild panel
					refresh();
					if(id!=null)
						profileData.setSelectedProfile(id);
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
				catch (IllegalArgumentException e)
				{	e.printStackTrace();
				}
				catch (SecurityException e)
				{	e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{	e.printStackTrace();
				}
				catch (NoSuchFieldException e)
				{	e.printStackTrace();
				}
				catch (ClassNotFoundException e)
				{	e.printStackTrace();
				}
			}
		}
	}
}

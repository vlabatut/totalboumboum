package fr.free.totalboumboum.gui.profiles.ai;

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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import fr.free.totalboumboum.ai.AiPreview;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.gui.ais.select.SelectedAiData;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class SelectedAiMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private JButton buttonCancel;
	@SuppressWarnings("unused")
	private JButton buttonConfirm;

	private SelectedAiData aiData;
	private Profile profile;

	public SelectedAiMenu(SplitMenuPanel container, MenuPanel parent, Profile profile)
	{	super(container, parent);
		this.profile = profile;
	
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
		aiData = new SelectedAiData(container);
		container.setDataPart(aiData);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiTools.MENU_PROFILES_BUTTON_CANCEL))
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_PROFILES_BUTTON_CONFIRM))
		{	AiPreview aiPreview = aiData.getSelectedAiPreview();
			profile.setAiName(aiPreview.getFolder());
			profile.setAiPackname(aiPreview.getPack());
			replaceWith(parent);
	    }
	} 
	
	public void refresh()
	{	//
	}
}

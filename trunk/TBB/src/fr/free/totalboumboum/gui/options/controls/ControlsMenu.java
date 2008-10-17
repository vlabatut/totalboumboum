package fr.free.totalboumboum.gui.options.controls;

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

import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.options.controls.ControlsData;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class ControlsMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private JButton buttonPrevious;
	@SuppressWarnings("unused")
	private JButton buttonNext;
	
	@SuppressWarnings("unused")
	private JButton buttonConfirm;
	@SuppressWarnings("unused")
	private JButton buttonCancel;

	private ControlsData controlsData[] = new ControlsData[5];
	
	private int selected = 0;

	public ControlsMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);

		// buttons
		add(Box.createVerticalGlue());
		buttonPrevious = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_OPTIONS_BUTTON_PREVIOUS,this,getConfiguration());
		buttonNext = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_OPTIONS_BUTTON_NEXT,this,getConfiguration());
		add(Box.createRigidArea(new Dimension(0,GuiTools.getSize(GuiTools.MENU_VERTICAL_BUTTON_SPACE))));
		buttonConfirm = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_OPTIONS_BUTTON_CONFIRM,this,getConfiguration());
		add(Box.createRigidArea(new Dimension(0,GuiTools.getSize(GuiTools.MENU_VERTICAL_BUTTON_SPACE))));
		buttonCancel = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_OPTIONS_BUTTON_CANCEL,this,getConfiguration());
		add(Box.createVerticalGlue());		

		// panels
		for(int i=0;i<controlsData.length;i++)
			controlsData[i] = new ControlsData(container,i+1);
		container.setDataPart(controlsData[0]);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiTools.MENU_OPTIONS_BUTTON_NEXT))
		{	selected = (selected + 1) % controlsData.length;
			container.setDataPart(controlsData[selected]);
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_OPTIONS_BUTTON_PREVIOUS))
		{	selected = (selected + controlsData.length - 1) % controlsData.length;
			container.setDataPart(controlsData[selected]);
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_OPTIONS_BUTTON_CONFIRM))
		{	//TODO modifications à enregistrer
			replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_OPTIONS_BUTTON_CANCEL))
		{	replaceWith(parent);
	    }
	} 
	
	public void refresh()
	{	//
	}
}

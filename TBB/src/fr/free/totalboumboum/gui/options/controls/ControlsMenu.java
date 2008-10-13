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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import fr.free.totalboumboum.gui.common.InnerDataPanel;
import fr.free.totalboumboum.gui.common.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.MenuContainer;
import fr.free.totalboumboum.gui.common.MenuPanel;
import fr.free.totalboumboum.gui.common.SimpleMenuPanel;
import fr.free.totalboumboum.gui.common.SplitMenuPanel;
import fr.free.totalboumboum.gui.menus.tournament.TournamentData;
import fr.free.totalboumboum.gui.options.controls.ControlsData;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class ControlsMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	private JButton buttonConfirm;
	private JButton buttonCancel;

	private ControlsData controlsData;

	public ControlsMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);

		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);

		// size
		int height = GuiTools.getSize(GuiTools.VERTICAL_SPLIT_MENU_PANEL_HEIGHT);
		int width = GuiTools.getSize(GuiTools.VERTICAL_SPLIT_MENU_PANEL_WIDTH);
		setPreferredSize(new Dimension(width,height));
		
		// buttons
		add(Box.createVerticalGlue());
		buttonConfirm = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_OPTIONS_BUTTON_CONFIRM,this,getConfiguration());
		add(Box.createRigidArea(new Dimension(0,GuiTools.getSize(GuiTools.MENU_VERTICAL_BUTTON_SPACE))));
		buttonCancel = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_OPTIONS_BUTTON_CANCEL,this,getConfiguration());
		add(Box.createVerticalGlue());		

		// panels
		int h = GuiTools.getSize(GuiTools.VERTICAL_SPLIT_DATA_PANEL_HEIGHT);
		int w = GuiTools.getSize(GuiTools.VERTICAL_SPLIT_DATA_PANEL_WIDTH);
		InnerDataPanel controlsPanel = new ControlsData(container,w,h);
		container.setDataPart(controlsPanel);
		dataPart = controlsPanel;
}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiTools.MENU_OPTIONS_BUTTON_CONFIRM))
		{	//TODO modifications à implémenter
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_OPTIONS_BUTTON_CANCEL))
		{	replaceWith(parent);
	    }
	} 
	
	public void refresh()
	{	//
	}
}

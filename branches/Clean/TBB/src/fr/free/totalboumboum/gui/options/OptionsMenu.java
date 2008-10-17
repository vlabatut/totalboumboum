package fr.free.totalboumboum.gui.options;

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
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import fr.free.totalboumboum.gui.common.MenuContainer;
import fr.free.totalboumboum.gui.common.MenuPanel;
import fr.free.totalboumboum.gui.common.SimpleMenuPanel;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class OptionsMenu extends SimpleMenuPanel
{	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;

	@SuppressWarnings("unused")
	private JButton buttonControls;
	private JButton buttonGameplay;
	private JButton buttonVideo;
	@SuppressWarnings("unused")
	private JButton buttonBack;
	
	public OptionsMenu(MenuContainer container, MenuPanel parent)
	{	super(container, parent);
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		// size
		setPreferredSize(getConfiguration().getPanelDimension());
		// background
		image = getConfiguration().getBackground();
		
		// buttons
		add(Box.createVerticalGlue());
		buttonControls = GuiTools.createPrincipalVerticalMenuButton(GuiTools.MENU_OPTIONS_BUTTON_CONTROLS,this,getConfiguration());
buttonControls.setEnabled(false);
		buttonGameplay = GuiTools.createPrincipalVerticalMenuButton(GuiTools.MENU_OPTIONS_BUTTON_GAMEPLAY,this,getConfiguration());
buttonGameplay.setEnabled(false);
		buttonVideo = GuiTools.createPrincipalVerticalMenuButton(GuiTools.MENU_OPTIONS_BUTTON_VIDEO,this,getConfiguration());
buttonVideo.setEnabled(false);
		add(Box.createRigidArea(new Dimension(0,GuiTools.getSize(GuiTools.MENU_VERTICAL_BUTTON_SPACE))));
		buttonBack = GuiTools.createPrincipalVerticalMenuButton(GuiTools.MENU_OPTIONS_BUTTON_BACK,this,getConfiguration());
		add(Box.createVerticalGlue());		
	}
	
	@Override
	public void paintComponent(Graphics g)
	{	g.drawImage(image, 0, 0, null);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiTools.MENU_OPTIONS_BUTTON_VIDEO))
		{	
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_OPTIONS_BUTTON_GAMEPLAY))
		{	
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_OPTIONS_BUTTON_BACK))
		{	replaceWith(parent);
	    }
	} 
	
	public void refresh()
	{	// nothing to do here
	}
}

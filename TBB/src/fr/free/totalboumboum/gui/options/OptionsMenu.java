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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.gui.common.InnerDataPanel;
import fr.free.totalboumboum.gui.common.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.MenuContainer;
import fr.free.totalboumboum.gui.common.MenuPanel;
import fr.free.totalboumboum.gui.common.SimpleMenuPanel;
import fr.free.totalboumboum.gui.common.SplitMenuPanel;
import fr.free.totalboumboum.gui.menus.tournament.TournamentData;
import fr.free.totalboumboum.gui.options.controls.ControlsData;
import fr.free.totalboumboum.gui.options.controls.ControlsSplitPanel;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class OptionsMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	private JButton buttonControls;
	private JButton buttonGameplay;
	private JButton buttonVideo;
	private JButton buttonBack;

	private InnerDataPanel optionsData;

	public OptionsMenu(SplitMenuPanel container, MenuPanel parent)
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
		buttonControls = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_OPTIONS_BUTTON_CONTROLS,this,getConfiguration());
		buttonGameplay = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_OPTIONS_BUTTON_GAMEPLAY,this,getConfiguration());
buttonGameplay.setEnabled(false);
		buttonVideo = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_OPTIONS_BUTTON_VIDEO,this,getConfiguration());
buttonVideo.setEnabled(false);
		add(Box.createRigidArea(new Dimension(0,GuiTools.getSize(GuiTools.MENU_VERTICAL_BUTTON_SPACE))));
		buttonBack = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_OPTIONS_BUTTON_BACK,this,getConfiguration());
		add(Box.createVerticalGlue());		

		// panels
		optionsData = new OptionsData(container);
		container.setDataPart(optionsData);
		dataPart = optionsData;
}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiTools.MENU_OPTIONS_BUTTON_VIDEO))
		{	
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_OPTIONS_BUTTON_GAMEPLAY))
		{	
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_OPTIONS_BUTTON_CONTROLS))
		{	ControlsSplitPanel controlsPanel;
			try
			{	controlsPanel = new ControlsSplitPanel(container.getContainer(),container);
				replaceWith(controlsPanel);
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
			catch (IllegalAccessException e1)
			{	e1.printStackTrace();
			}
			catch (NoSuchFieldException e1)
			{	e1.printStackTrace();
			} 
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_OPTIONS_BUTTON_BACK))
		{	replaceWith(parent);
	    }
	} 
	
	public void refresh()
	{	//
	}
}

package fr.free.totalboumboum.gui.menus.options;

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

import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.InnerDataPanel;
import fr.free.totalboumboum.gui.common.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.menus.options.advanced.AdvancedSplitPanel;
import fr.free.totalboumboum.gui.menus.options.controls.ControlsSplitPanel;
import fr.free.totalboumboum.gui.menus.options.gui.GuiSplitPanel;
import fr.free.totalboumboum.gui.menus.options.video.VideoSplitPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class OptionsMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private JButton buttonAdvanced;
	@SuppressWarnings("unused")
	private JButton buttonControls;
	private JButton buttonGameplay;
	@SuppressWarnings("unused")
	private JButton buttonVideo;
	@SuppressWarnings("unused")
	private JButton buttonBack;
	@SuppressWarnings("unused")
	private JButton buttonGui;

	private InnerDataPanel optionsData;

	public OptionsMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);

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
		buttonControls = GuiTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_CONTROLS,buttonWidth,buttonHeight,fontSize,this);
		buttonGameplay = GuiTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_GAMEPLAY,buttonWidth,buttonHeight,fontSize,this);
buttonGameplay.setEnabled(false);
		buttonVideo = GuiTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_VIDEO,buttonWidth,buttonHeight,fontSize,this);
		buttonGui = GuiTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_GUI,buttonWidth,buttonHeight,fontSize,this);
		buttonAdvanced = GuiTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_ADVANCED,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonBack = GuiTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_BACK,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createVerticalGlue());		

		// panels
		optionsData = new OptionsData(container);
		container.setDataPart(optionsData);
}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_ADVANCED))
		{	try
			{	AdvancedSplitPanel advancedPanel = new AdvancedSplitPanel(container.getContainer(),container);
				replaceWith(advancedPanel);
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
	else if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_GUI))
		{	try
			{	GuiSplitPanel guiPanel = new GuiSplitPanel(container.getContainer(),container);
				replaceWith(guiPanel);
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
		else if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_VIDEO))
		{	try
			{	VideoSplitPanel videoPanel = new VideoSplitPanel(container.getContainer(),container);
				replaceWith(videoPanel);
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
		else if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_GAMEPLAY))
		{	
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_CONTROLS))
		{	try
			{	ControlsSplitPanel controlsPanel = new ControlsSplitPanel(container.getContainer(),container);
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
		else if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_BACK))
		{	replaceWith(parent);
	    }
	} 
	
	public void refresh()
	{	//
	}
}

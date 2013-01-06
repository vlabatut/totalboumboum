package org.totalboumboum.gui.menus.options.controls;

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
import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.configuration.controls.ControlsConfigurationSaver;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.menus.options.controls.ControlsData;
import org.totalboumboum.gui.tools.GuiButtonTools;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiFontTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.tools.GameData;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
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

	private ControlsData controlsData[] = new ControlsData[GameData.CONTROL_COUNT];
	
	private int selected = 0;

	public ControlsMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiColorTools.COLOR_COMMON_BACKGROUND);

		// sizes
		int buttonWidth = getWidth();
		int buttonHeight = GuiSizeTools.buttonTextHeight;
		List<String> texts = GuiKeys.getKeysLike(GuiKeys.MENU_OPTIONS_BUTTON);
		int fontSize = GuiFontTools.getOptimalFontSize(buttonWidth*0.8, buttonHeight*0.9, texts);

		// buttons
		add(Box.createVerticalGlue());
		buttonPrevious = GuiButtonTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_PREVIOUS,buttonWidth,buttonHeight,fontSize,this);
		buttonNext = GuiButtonTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_NEXT,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiSizeTools.buttonVerticalSpace)));
		buttonConfirm = GuiButtonTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_CONFIRM,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiSizeTools.buttonVerticalSpace)));
		buttonCancel = GuiButtonTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_CANCEL,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createVerticalGlue());		

		// panels
		for(int i=0;i<controlsData.length;i++)
			controlsData[i] = new ControlsData(container,i+1);
		container.setDataPart(controlsData[0]);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_NEXT))
		{	controlsData[selected].deselect();
			selected = (selected + 1) % controlsData.length;
			container.setDataPart(controlsData[selected]);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_PREVIOUS))
		{	controlsData[selected].deselect();
			selected = (selected + controlsData.length - 1) % controlsData.length;
			container.setDataPart(controlsData[selected]);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_CONFIRM))
		{	for(int i=0;i<controlsData.length;i++)
			{	ControlSettings controlSettings = controlsData[i].getControlSettings();
				Configuration.getControlsConfiguration().putControlSettings(i+1,controlSettings);
			}
			try
			{	ControlsConfigurationSaver.saveControlsConfiguration(Configuration.getControlsConfiguration());
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
//TODO propager éventuellement au round (car il n'y a pas modification mais remplacement, donc si c déjà affecté à un player..
			replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_CANCEL))
		{	replaceWith(parent);
	    }
	} 
	
	public void refresh()
	{	//
	}
}

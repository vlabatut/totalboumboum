package org.totalboumboum.gui.menus.options;

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

import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.InnerDataPanel;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.menus.options.advanced.AdvancedSplitPanel;
import org.totalboumboum.gui.menus.options.ais.AisSplitPanel;
import org.totalboumboum.gui.menus.options.controls.ControlsSplitPanel;
import org.totalboumboum.gui.menus.options.game.GameSplitPanel;
import org.totalboumboum.gui.menus.options.gui.GuiSplitPanel;
import org.totalboumboum.gui.menus.options.statistics.StatisticsSplitPanel;
import org.totalboumboum.gui.menus.options.video.VideoSplitPanel;
import org.totalboumboum.gui.tools.GuiFontTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class OptionsMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private JButton buttonAdvanced;
	@SuppressWarnings("unused")
	private JButton buttonControls;
	@SuppressWarnings("unused")
	private JButton buttonGameplay;
	@SuppressWarnings("unused")
	private JButton buttonVideo;
	@SuppressWarnings("unused")
	private JButton buttonBack;
	@SuppressWarnings("unused")
	private JButton buttonGui;
	@SuppressWarnings("unused")
	private JButton buttonAis;
	@SuppressWarnings("unused")
	private JButton buttonStatistics;

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
		List<String> texts = GuiKeys.getKeysLike(GuiKeys.MENU_OPTIONS_BUTTON);
		int fontSize = GuiFontTools.getOptimalFontSize(buttonWidth*0.8, buttonHeight*0.9, texts);

		// buttons
		add(Box.createVerticalGlue());
		buttonControls = GuiTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_CONTROLS,buttonWidth,buttonHeight,fontSize,this);
		buttonGameplay = GuiTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_GAME,buttonWidth,buttonHeight,fontSize,this);
		buttonVideo = GuiTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_VIDEO,buttonWidth,buttonHeight,fontSize,this);
		buttonGui = GuiTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_GUI,buttonWidth,buttonHeight,fontSize,this);
		buttonStatistics = GuiTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_STATISTICS,buttonWidth,buttonHeight,fontSize,this);
		buttonAis = GuiTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_AIS,buttonWidth,buttonHeight,fontSize,this);
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
			{	AdvancedSplitPanel advancedPanel = new AdvancedSplitPanel(container.getMenuContainer(),container);
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
		else if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_AIS))
		{	try
			{	AisSplitPanel guiPanel = new AisSplitPanel(container.getMenuContainer(),container);
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
		else if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_GUI))
		{	try
			{	GuiSplitPanel guiPanel = new GuiSplitPanel(container.getMenuContainer(),container);
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
		else if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_STATISTICS))
		{	try
			{	StatisticsSplitPanel statisticsPanel = new StatisticsSplitPanel(container.getMenuContainer(),container);
				replaceWith(statisticsPanel);
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
			{	VideoSplitPanel videoPanel = new VideoSplitPanel(container.getMenuContainer(),container);
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
		else if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_GAME))
		{	// TODO certainement à compléter
			GameSplitPanel gamePanel = new GameSplitPanel(container.getMenuContainer(),container);
			replaceWith(gamePanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_CONTROLS))
		{	try
			{	ControlsSplitPanel controlsPanel = new ControlsSplitPanel(container.getMenuContainer(),container);
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

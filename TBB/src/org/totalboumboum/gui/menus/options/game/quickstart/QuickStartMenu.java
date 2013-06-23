package org.totalboumboum.gui.menus.options.game.quickstart;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.game.quickstart.QuickStartConfiguration;
import org.totalboumboum.configuration.game.quickstart.QuickStartConfigurationSaver;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.DataPanelListener;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class QuickStartMenu extends InnerMenuPanel implements DataPanelListener
{	private static final long serialVersionUID = 1L;
	
	private QuickStartData quickstartPanel;
	
	public QuickStartMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container,parent);
	
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
	
		// sizes
		int buttonWidth = getWidth();
		int buttonHeight = GuiTools.buttonTextHeight;
		List<String> texts = GuiKeys.getKeysLike(GuiKeys.MENU_OPTIONS_BUTTON);
		int fontSize = GuiTools.getOptimalFontSize(buttonWidth*0.8, buttonHeight*0.9, texts);
	
		// buttons
		add(Box.createVerticalGlue());
		buttonConfirm = GuiTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_CONFIRM,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonCancel = GuiTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_CANCEL,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createVerticalGlue());		
	
		// panels
		quickstartPanel = new QuickStartData(container);
		quickstartPanel.addListener(this);
		container.setDataPart(quickstartPanel);
		initConfiguration();
	}
	
	/////////////////////////////////////////////////////////////////
	// CONFIGURATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private QuickStartConfiguration quickStartConfiguration;
	
	public void initConfiguration()
	{	quickStartConfiguration = Configuration.getGameConfiguration().getQuickStartConfiguration();
		quickstartPanel.setQuickStartConfiguration(quickStartConfiguration.copy());
		refreshButtons();
	}

	/////////////////////////////////////////////////////////////////
	// BUTTONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private JButton buttonConfirm;
	@SuppressWarnings("unused")
	private JButton buttonCancel;
		
	private void refreshButtons()
	{	QuickStartConfiguration copyConfiguration = quickstartPanel.getQuickStartConfiguration();
		Set<Integer> allowedPlayers = copyConfiguration.getAllowedPlayers();
		int playersNumber = copyConfiguration.getProfilesSelection().getProfileCount();
		if(allowedPlayers.contains(playersNumber))
			buttonConfirm.setEnabled(true);
		else
			buttonConfirm.setEnabled(false);		
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTION LISTENER				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_CONFIRM))
		{	QuickStartConfiguration copyConfiguration = quickstartPanel.getQuickStartConfiguration();
			if(quickStartConfiguration.hasChanged(copyConfiguration))
			{	Configuration.getGameConfiguration().setQuickStartConfiguration(copyConfiguration);
				try
				{	QuickStartConfigurationSaver.saveQuickStartConfiguration(copyConfiguration);
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
			}
			replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_CANCEL))
		{	replaceWith(parent);
	    }
	} 

	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// PAINT						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
    protected void paintComponent(Graphics g)
	{	//g.clearRect(0, 0, getWidth(), getHeight());
//		getParent().paintComponents(g);
		super.paintComponent(g);
    }

	/////////////////////////////////////////////////////////////////
	// DATA PANEL LISTENER			/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void dataPanelSelectionChanged(Object object)
	{	refreshButtons();
	}
}

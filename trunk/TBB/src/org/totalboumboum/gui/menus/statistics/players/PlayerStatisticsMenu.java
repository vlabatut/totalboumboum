package org.totalboumboum.gui.menus.statistics.players;

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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.tools.GuiButtonTools;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiFontTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.gui.tools.GuiImageTools;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.overall.OverallStatsSaver;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class PlayerStatisticsMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	private JToggleButton buttonGlicko2;
	private JToggleButton buttonScores;
	private JToggleButton buttonConfrontations;
	private JToggleButton buttonTable;
	private JToggleButton buttonPlot;
	private JToggleButton buttonSelection;
	@SuppressWarnings("unused")
	private JButton buttonBack;

	private PlayerStatisticsDataTable statsDataTable;
	private PlayerStatisticsDataPlot statsDataPlot;

	private boolean displayTable = true;
	private String currentView = null;
	
	public PlayerStatisticsMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiColorTools.COLOR_COMMON_BACKGROUND);

		// sizes
		int buttonWidth = getWidth();
		int buttonHeight = GuiSizeTools.buttonTextHeight;
		List<String> texts = GuiKeys.getKeysLike(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON);
		int fontSize = GuiFontTools.getOptimalFontSize(buttonWidth*0.8, buttonHeight*0.9, texts);

		// buttons
		add(Box.createVerticalGlue());
	    {	ButtonGroup group = new ButtonGroup();
	    	buttonGlicko2 = GuiButtonTools.createToggleButton(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_GLICKO2,buttonWidth,buttonHeight,fontSize,this);
	    	buttonGlicko2.setSelected(true);
	    	group.add(buttonGlicko2);
	    	buttonScores = GuiButtonTools.createToggleButton(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_SCORES,buttonWidth,buttonHeight,fontSize,this);
	    	group.add(buttonScores);
	    	buttonConfrontations = GuiButtonTools.createToggleButton(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_CONFRONTATIONS,buttonWidth,buttonHeight,fontSize,this);
	    	group.add(buttonConfrontations);
	    	buttonSelection = GuiButtonTools.createToggleButton(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_SELECTION,buttonWidth,buttonHeight,fontSize,this);
	    	group.add(buttonSelection);
	    }
		add(Box.createRigidArea(new Dimension(0,GuiSizeTools.buttonVerticalSpace)));
		{	ButtonGroup group = new ButtonGroup();
			buttonTable = GuiButtonTools.createToggleButton(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_TABLE,buttonWidth,buttonHeight,fontSize,this);
			buttonTable.setSelected(true);
			group.add(buttonTable);
			buttonPlot = GuiButtonTools.createToggleButton(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_PLOT,buttonWidth,buttonHeight,fontSize,this);
			group.add(buttonPlot);
		}
		add(Box.createRigidArea(new Dimension(0,GuiSizeTools.buttonVerticalSpace)));
		{	buttonBack = GuiButtonTools.createButton(GuiKeys.MENU_RESOURCES_AI_BUTTON_BACK,buttonWidth,buttonHeight,fontSize,this);
		}
		add(Box.createVerticalGlue());

		// panels
		statsDataTable = new PlayerStatisticsDataTable(container);
		statsDataPlot = new PlayerStatisticsDataPlot(container);
		container.setDataPart(statsDataTable);
		displayTable = true;
		currentView = GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_GLICKO2;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{	String command = e.getActionCommand();
		if(command.equals(GuiKeys.MENU_RESOURCES_AI_BUTTON_BACK))
		{	replaceWith(parent);
	    }
		
		else if(command.equals(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_SELECTION))
		{	if(!displayTable)
				container.setDataPart(statsDataTable);
			statsDataTable.setView(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_SELECTION);
			buttonTable.setEnabled(false);
			buttonPlot.setEnabled(false);
		}

		else if(command.equals(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_TABLE))
		{	statsDataTable.setView(currentView);
			container.setDataPart(statsDataTable);
			displayTable = true;
		}
		else if(command.equals(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_PLOT))
		{	statsDataPlot.setView(currentView);
//			statsDataPlot.refresh();
			container.setDataPart(statsDataPlot);
			displayTable = false;
		}
		
		else
		{	if(displayTable)
			{	statsDataTable.setView(command);
				container.setDataPart(statsDataTable);
			}
			else
			{	statsDataPlot.setView(command);
				container.setDataPart(statsDataPlot);
			}
			if(!buttonTable.isEnabled())
			{	buttonTable.setEnabled(true);
				buttonPlot.setEnabled(true);
				// this allows saving the player selection
				try
				{	GameStatistics.saveStatistics();
				}
				catch (IllegalArgumentException e1)
				{	e1.printStackTrace();
				}
				catch (SecurityException e1)
				{	e1.printStackTrace();
				}
				catch (IllegalAccessException e1)
				{	e1.printStackTrace();
				}
				catch (NoSuchFieldException e1)
				{	e1.printStackTrace();
				}
				catch (ClassNotFoundException e1)
				{	e1.printStackTrace();
				}
				catch (IOException e1)
				{	e1.printStackTrace();
				}
				catch (ParserConfigurationException e1)
				{	e1.printStackTrace();
				}
				catch (SAXException e1)
				{	e1.printStackTrace();
				}
			}
			currentView = command;
		}
	} 
	
	@Override
	public void refresh()
	{	//
	}
}

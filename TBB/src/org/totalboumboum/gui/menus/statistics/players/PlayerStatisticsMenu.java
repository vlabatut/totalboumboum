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
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.tools.GuiButtonTools;
import org.totalboumboum.gui.tools.GuiFontTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;

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
	@SuppressWarnings("unused")
	private JButton buttonBack;

	private PlayerStatisticsData statsData;

	public PlayerStatisticsMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);

		// sizes
		int buttonWidth = getWidth();
		int buttonHeight = GuiTools.buttonTextHeight;
		List<String> texts = GuiKeys.getKeysLike(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON);
		int fontSize = GuiFontTools.getOptimalFontSize(buttonWidth*0.8, buttonHeight*0.9, texts);

		// buttons
		add(Box.createVerticalGlue());
	    ButtonGroup group = new ButtonGroup();
	    buttonGlicko2 = GuiButtonTools.createToggleButton(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_GLICKO2,buttonWidth,buttonHeight,fontSize,this);
	    buttonGlicko2.setSelected(true);
	    group.add(buttonGlicko2);
		buttonScores = GuiButtonTools.createToggleButton(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_SCORES,buttonWidth,buttonHeight,fontSize,this);
	    group.add(buttonScores);
		buttonConfrontations = GuiButtonTools.createToggleButton(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_CONFRONTATIONS,buttonWidth,buttonHeight,fontSize,this);
	    group.add(buttonConfrontations);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonBack = GuiButtonTools.createButton(GuiKeys.MENU_RESOURCES_AI_BUTTON_BACK,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createVerticalGlue());

		// panels
		statsData = new PlayerStatisticsData(container);
		container.setDataPart(statsData);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_RESOURCES_AI_BUTTON_BACK))
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_GLICKO2))
		{	statsData.setView(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_GLICKO2);
		}
		else if(e.getActionCommand().equals(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_SCORES))
		{	statsData.setView(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_SCORES);
		}
		else if(e.getActionCommand().equals(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_CONFRONTATIONS))
		{	statsData.setView(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_CONFRONTATIONS);
		}
	} 
	
	public void refresh()
	{	//
	}
}

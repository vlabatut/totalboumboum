package org.totalboumboum.gui.menus.options.game;

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
import javax.swing.JButton;

import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.InnerDataPanel;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.menus.options.game.quickmatch.QuickMatchSplitPanel;
import org.totalboumboum.gui.menus.options.game.quickstart.QuickStartSplitPanel;
import org.totalboumboum.gui.menus.options.game.tournament.TournamentSplitPanel;
import org.totalboumboum.gui.tools.GuiButtonTools;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiFontTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.gui.tools.GuiImageTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class GameMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private JButton buttonQuickStart;
	@SuppressWarnings("unused")
	private JButton buttonQuickMatch;
	@SuppressWarnings("unused")
	private JButton buttonTournament;
	
	@SuppressWarnings("unused")
	private JButton buttonBack;

	private InnerDataPanel optionsData;

	public GameMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);

		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiColorTools.COLOR_COMMON_BACKGROUND);

		// sizes
		int buttonWidth = getWidth();
		int buttonHeight = GuiSizeTools.buttonTextHeight;
		List<String> texts = GuiKeys.getKeysLike(GuiKeys.MENU_OPTIONS_GAME_BUTTON);
		int fontSize = GuiFontTools.getOptimalFontSize(buttonWidth*0.8, buttonHeight*0.9, texts);

		// buttons
		add(Box.createVerticalGlue());
		buttonQuickStart = GuiButtonTools.createButton(GuiKeys.MENU_OPTIONS_GAME_BUTTON_QUICKSTART,buttonWidth,buttonHeight,fontSize,this);
		buttonQuickMatch = GuiButtonTools.createButton(GuiKeys.MENU_OPTIONS_GAME_BUTTON_QUICKMATCH,buttonWidth,buttonHeight,fontSize,this);
		buttonTournament = GuiButtonTools.createButton(GuiKeys.MENU_OPTIONS_GAME_BUTTON_TOURNAMENT,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiSizeTools.buttonVerticalSpace)));
		buttonBack = GuiButtonTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_BACK,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createVerticalGlue());		

		// panels
		optionsData = new GameData(container);
		container.setDataPart(optionsData);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_GAME_BUTTON_QUICKSTART))
		{	QuickStartSplitPanel quickstartPanel = new QuickStartSplitPanel(container.getMenuContainer(),container);
			replaceWith(quickstartPanel);
		}
		else if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_GAME_BUTTON_QUICKMATCH))
		{	QuickMatchSplitPanel quickmatchPanel = new QuickMatchSplitPanel(container.getMenuContainer(),container);
			replaceWith(quickmatchPanel);
		}
		else if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_GAME_BUTTON_TOURNAMENT))
		{	TournamentSplitPanel tournamentPanel = new TournamentSplitPanel(container.getMenuContainer(),container);
			replaceWith(tournamentPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_BACK))
		{	replaceWith(parent);
		}
	} 
	
	/////////////////////////////////////////////////////////////////
	// PAINT						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void refresh()
	{	//
	}
}

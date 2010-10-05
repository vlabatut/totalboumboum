package org.totalboumboum.gui.menus.explore;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
import org.totalboumboum.gui.menus.explore.ais.select.SelectedAiSplitPanel;
import org.totalboumboum.gui.menus.explore.heroes.select.SelectedHeroSplitPanel;
import org.totalboumboum.gui.menus.explore.levels.select.SelectedLevelSplitPanel;
import org.totalboumboum.gui.menus.explore.matches.select.SelectedMatchSplitPanel;
import org.totalboumboum.gui.menus.explore.rounds.select.SelectedRoundSplitPanel;
import org.totalboumboum.gui.menus.explore.tournaments.select.SelectedTournamentSplitPanel;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ExploreMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private JButton buttonAi;
	@SuppressWarnings("unused")
	private JButton buttonHeroes;
	
	private JButton buttonInstances;
	@SuppressWarnings("unused")
	private JButton buttonLevels;

	@SuppressWarnings("unused")
	private JButton buttonTournaments;
	@SuppressWarnings("unused")
	private JButton buttonMatches;
	@SuppressWarnings("unused")
	private JButton buttonRounds;
	
	@SuppressWarnings("unused")
	private JButton buttonBack;

	private InnerDataPanel optionsData;

	public ExploreMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);

		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);

		// sizes
		int buttonWidth = getWidth();
		int buttonHeight = GuiTools.buttonTextHeight;
		List<String> texts = GuiKeys.getKeysLike(GuiKeys.MENU_RESOURCES_BUTTON);
		int fontSize = GuiTools.getOptimalFontSize(buttonWidth*0.8, buttonHeight*0.9, texts);

		// buttons
		add(Box.createVerticalGlue());
		buttonHeroes = GuiTools.createButton(GuiKeys.MENU_RESOURCES_BUTTON_HEROES,buttonWidth,buttonHeight,fontSize,this);
		buttonAi = GuiTools.createButton(GuiKeys.MENU_RESOURCES_BUTTON_AI,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonInstances = GuiTools.createButton(GuiKeys.MENU_RESOURCES_BUTTON_INSTANCES,buttonWidth,buttonHeight,fontSize,this);
buttonInstances.setEnabled(false);
		buttonLevels = GuiTools.createButton(GuiKeys.MENU_RESOURCES_BUTTON_LEVELS,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonRounds = GuiTools.createButton(GuiKeys.MENU_RESOURCES_BUTTON_ROUNDS,buttonWidth,buttonHeight,fontSize,this);
		buttonMatches = GuiTools.createButton(GuiKeys.MENU_RESOURCES_BUTTON_MATCHES,buttonWidth,buttonHeight,fontSize,this);
		buttonTournaments = GuiTools.createButton(GuiKeys.MENU_RESOURCES_BUTTON_TOURNAMENTS,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonBack = GuiTools.createButton(GuiKeys.MENU_RESOURCES_BUTTON_BACK,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createVerticalGlue());		

		// panels
		optionsData = new ExploreData(container);
		container.setDataPart(optionsData);
}
	
	public void actionPerformed(ActionEvent e)
	{	
		if(e.getActionCommand().equals(GuiKeys.MENU_RESOURCES_BUTTON_AI))
		{	SelectedAiSplitPanel aiMenuPanel = new SelectedAiSplitPanel(container.getMenuContainer(),container);
			replaceWith(aiMenuPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_RESOURCES_BUTTON_HEROES))
		{	SelectedHeroSplitPanel heroMenuPanel = new SelectedHeroSplitPanel(container.getMenuContainer(),container);
			replaceWith(heroMenuPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_RESOURCES_BUTTON_INSTANCES))
		{	//SelectedLevelSplitPanel levelMenuPanel = new SelectedLevelSplitPanel(getContainer(),this);
			//replaceWith(levelMenuPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_RESOURCES_BUTTON_LEVELS))
		{	SelectedLevelSplitPanel levelMenuPanel = new SelectedLevelSplitPanel(container.getMenuContainer(),container);
			replaceWith(levelMenuPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_RESOURCES_BUTTON_TOURNAMENTS))
		{	SelectedTournamentSplitPanel tournamentMenuPanel = new SelectedTournamentSplitPanel(container.getMenuContainer(),container);
			replaceWith(tournamentMenuPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_RESOURCES_BUTTON_MATCHES))
		{	SelectedMatchSplitPanel matchMenuPanel = new SelectedMatchSplitPanel(container.getMenuContainer(),container);
			replaceWith(matchMenuPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_RESOURCES_BUTTON_ROUNDS))
		{	SelectedRoundSplitPanel roundMenuPanel = new SelectedRoundSplitPanel(container.getMenuContainer(),container);
			replaceWith(roundMenuPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_RESOURCES_BUTTON_BACK))
		{	replaceWith(parent);
	    }
	} 
	
	public void refresh()
	{	//
	}
}

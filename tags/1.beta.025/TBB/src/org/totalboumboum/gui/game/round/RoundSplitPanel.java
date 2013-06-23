package org.totalboumboum.gui.game.round;

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

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.totalboumboum.game.round.Round;
import org.totalboumboum.gui.common.structure.MenuContainer;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiSizeTools;

/**
 * This class is the main panel used
 * when displaying the round
 * during game. It contains a menu
 * panel (bottom menu) and a data
 * panel (actual content).
 * 
 * @author Vincent Labatut
 */
public class RoundSplitPanel extends SplitMenuPanel
{	/** Class id */
	private static final long serialVersionUID = 1L;
	/** Background image */
	private BufferedImage image;

	/**
	 * Builds a standard panel.
	 * 
	 * @param container
	 * 		Container of the panel
	 * @param parent
	 * 		Parent menu.
	 */
	public RoundSplitPanel(MenuContainer container, MenuPanel parent)
	{	super(container,parent,BorderLayout.PAGE_END,GuiSizeTools.HORIZONTAL_SPLIT_RATIO);
	
		// background
		image = GuiConfiguration.getMiscConfiguration().getDarkBackground();
		
		// panels
		RoundMenu menu = new RoundMenu(this,parent);
		setMenuPart(menu);
	}

	/////////////////////////////////////////////////////////////////
	// REFRESH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Update the buttons depending on 
	 * the tournament state.
	 */
	public void refreshButtons()
	{	((RoundMenu)menuPart).refreshButtons();
	}
	
	/////////////////////////////////////////////////////////////////
	// AUTO ADVANCE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Automatically clicks on the appropriate
	 * buttons in order to progress in the tournament.
	 * Used to automatically chain many tournaments,
	 * while evaluating agents.
	 */
	public void autoAdvance()
	{	((RoundMenu)menuPart).autoAdvance();
	}
	
	/////////////////////////////////////////////////////////////////
	// ROUND			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Changes the round displayed in this panel,
	 * in order to go on playing (and not just
	 * browse its statistics, like {@link #setRoundStats}).
	 * 
	 * @param round
	 * 		The new round to display.
	 */
	public void setRound(Round round)
	{	RoundMenu tm = (RoundMenu)getMenuPart();
		tm.setRound(round);
	}
	
	/**
	 * Sets a round loaded in order
	 * to browse its statistics (and not
	 * to go on playing, like {@link #setRound}).
	 * 
	 * @param round
	 * 		The loaded round.
	 */
	public void setRoundStats(Round round)
	{	RoundMenu tm = (RoundMenu)getMenuPart();
		tm.setBrowseOnly(true);
		//round.rewind();
		tm.setRound(round);
	}

	/**
	 * Returns the round displayed
	 * in this panel.
	 * 
	 * @return
	 * 		The current round.
	 */
	public Round getRound()
	{	return ((RoundMenu)menuPart).getRound();	
	}

	/////////////////////////////////////////////////////////////////
	// PAINT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void paintComponent(Graphics g)
	{	g.drawImage(image, 0, 0, null);
	}
}

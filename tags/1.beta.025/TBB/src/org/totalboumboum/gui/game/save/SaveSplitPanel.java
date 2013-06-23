package org.totalboumboum.gui.game.save;

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

import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.structure.MenuContainer;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiSizeTools;

/**
 * Main panel for the save tournament menu.
 * 
 * @author Vincent Labatut
 */
public class SaveSplitPanel extends SplitMenuPanel
{	/** Class id */
	private static final long serialVersionUID = 1L; 
	/** Background image */
	private BufferedImage image;

	/**
	 * Builds a standard panel.
	 * 
	 * @param container
	 * 		Swing container of this panel.
	 * @param parent
	 * 		Parent menu item.
	 */
	public SaveSplitPanel(MenuContainer container, MenuPanel parent)
	{	super(container,parent,BorderLayout.LINE_START,GuiSizeTools.VERTICAL_SPLIT_RATIO);
	
		// background
		image = GuiConfiguration.getMiscConfiguration().getDarkBackground();
		
		// panels
		setMenuPart(new SaveMenu(this,parent));
	}
	
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Changes the tournament handled
	 * by this menu.
	 * 
	 * @param tournament
	 * 		New tournament.
	 */
	public void setTournament(AbstractTournament tournament)
	{	((SaveMenu)getMenuPart()).setTournament(tournament);
	}

	/////////////////////////////////////////////////////////////////
	// PAINT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void paintComponent(Graphics g)
	{	g.drawImage(image, 0, 0, null);
	}
}

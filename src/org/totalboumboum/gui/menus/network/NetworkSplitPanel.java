package org.totalboumboum.gui.menus.network;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

import org.totalboumboum.gui.common.structure.MenuContainer;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.game.tournament.TournamentSplitPanel;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.gui.tools.GuiImageTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class NetworkSplitPanel extends SplitMenuPanel
{	private static final long serialVersionUID = 1L; 

	private BufferedImage image;
	
	public NetworkSplitPanel(MenuContainer container, MenuPanel parent)
	{	super(container,parent,BorderLayout.PAGE_END,GuiSizeTools.HORIZONTAL_SPLIT_RATIO);
	
		// background
		image = GuiConfiguration.getMiscConfiguration().getDarkBackground();
		
		// panels
		NetworkMenu menu = new NetworkMenu(this,parent);
		setMenuPart(menu);
	}
	
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT					/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void initTournament()
	{	((NetworkMenu)getMenuPart()).initTournament();
	}

	public void setTournamentPanel(TournamentSplitPanel tournamentPanel)
	{	((NetworkMenu)getMenuPart()).setTournamentPanel(tournamentPanel);
	}

	/////////////////////////////////////////////////////////////////
	// PAINT						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void paintComponent(Graphics g)
	{	g.drawImage(image, 0, 0, null);
	}
}

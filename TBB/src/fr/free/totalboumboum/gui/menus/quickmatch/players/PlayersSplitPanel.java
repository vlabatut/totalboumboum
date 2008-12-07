package fr.free.totalboumboum.gui.menus.quickmatch.players;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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
import java.util.ArrayList;

import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.gui.common.structure.MenuContainer;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class PlayersSplitPanel extends SplitMenuPanel
{	private static final long serialVersionUID = 1L;

	private BufferedImage image;

	public PlayersSplitPanel(MenuContainer container, MenuPanel parent)
	{	super(container,parent,BorderLayout.PAGE_END,GuiTools.HORIZONTAL_SPLIT_RATIO);
	
		// background
		image = GuiConfiguration.getMiscConfiguration().getDarkBackground();
	    
		// panels
		PlayersMenu menu = new PlayersMenu(this,parent);
		setMenuPart(menu);
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYERS						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void setSelectedProfiles(ArrayList<Profile> selectedProfiles)
	{	((PlayersMenu)menuPart).setSelectedProfiles(selectedProfiles);
	}
	
	public ArrayList<Profile> getSelectedProfiles()
	{	return ((PlayersMenu)menuPart).getSelectedProfiles();	
	}
	
	/////////////////////////////////////////////////////////////////
	// PAINT				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void paintComponent(Graphics g)
	{	g.drawImage(image, 0, 0, null);
	}
}

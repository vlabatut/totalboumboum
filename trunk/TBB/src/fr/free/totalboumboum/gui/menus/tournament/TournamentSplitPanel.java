package fr.free.totalboumboum.gui.menus.tournament;

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
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.gui.common.MenuContainer;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class TournamentSplitPanel extends SplitMenuPanel
{	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;

	public TournamentSplitPanel(MenuContainer container, MenuPanel parent) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	super(container,parent,BorderLayout.LINE_START,GuiTools.VERTICAL_SPLIT_RATIO);
		
		// background
		image = GuiConfiguration.getMiscConfiguration().getDarkBackground();
		
		// panels
		TournamentMenu menu = new TournamentMenu(this,parent);
		setMenuPart(menu);
	}

	
	@Override
	public void paintComponent(Graphics g)
	{	g.drawImage(image, 0, 0, null);
	}
}

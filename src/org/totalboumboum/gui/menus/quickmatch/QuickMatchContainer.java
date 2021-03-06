package org.totalboumboum.gui.menus.quickmatch;

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

import org.totalboumboum.gui.common.structure.MenuContainer;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class QuickMatchContainer extends MenuPanel implements MenuContainer
{	private static final long serialVersionUID = 1L;

	public QuickMatchContainer(MenuContainer container, MenuPanel parent)
	{	super(container.getMenuWidth(),container.getMenuHeight());
		
		// layout
		BorderLayout layout = new BorderLayout(); 
		setLayout(layout);
	
		setOpaque(false);
	
		// fields
		this.container = container;
		this.parent = parent;
		
		// split panel
		quickMatchSplitPanel = new QuickMatchSplitPanel(this,parent);
		setMenuPanel(quickMatchSplitPanel);
	}	
	
	/////////////////////////////////////////////////////////////////
	// REFRESH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
	{	menuPart.refresh();
	}

	/////////////////////////////////////////////////////////////////
	// SPLIT PANELS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private QuickMatchSplitPanel quickMatchSplitPanel;
	
	public void initTournament()
	{	if(quickMatchSplitPanel.initTournament())
			setMenuPanel(quickMatchSplitPanel);
		else
			setMenuPanel(quickMatchSplitPanel.getTournamentPanel());
	}

	/////////////////////////////////////////////////////////////////
	// MENU CONTAINER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected MenuPanel menuPart;

	@Override
	public int getMenuHeight()
	{	return height;
	}

	@Override
	public int getMenuWidth()
	{	return width;
	}

	@Override
	public void setMenuPanel(MenuPanel newPanel)
	{	if(this.menuPart!=null)
			remove(menuPart);
		menuPart = newPanel;
	//	menuPart.refresh();
		add(menuPart);
		validate();
		repaint();		
	}
}

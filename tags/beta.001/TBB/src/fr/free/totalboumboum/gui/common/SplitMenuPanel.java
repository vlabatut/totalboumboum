package fr.free.totalboumboum.gui.common;

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

public abstract class SplitMenuPanel extends MenuPanel implements MenuContainer
{	private static final long serialVersionUID = 1L;
	protected String menuLocation;
	
	public SplitMenuPanel(MenuContainer container, MenuPanel parent, String menuLocation)
	{	// fields
		this.container = container;
		this.parent = parent;
		this.menuLocation = menuLocation;
		// layout
		BorderLayout layout = new BorderLayout(); 
		setLayout(layout);
	}	
	
	public void refresh()
	{	dataPart.refresh();
		menuPart.refresh();
	}

	/////////////////////////////////////////////////////////////////
	// MENU PART		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected InnerMenuPanel menuPart;
	
	public void setMenuPart(InnerMenuPanel menuPart)
	{	if(this.menuPart!=null)
			remove(this.menuPart);
		this.menuPart = menuPart;
		this.menuPart.refresh();
		add(menuPart, menuLocation);
		validate();
		repaint();		
	}

	/////////////////////////////////////////////////////////////////
	// DATA PART		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ContentPanel dataPart;

	public void setDataPart(ContentPanel dataPart)
	{	if(this.dataPart!=null)
			remove(this.dataPart);
		this.dataPart = dataPart;
		this.dataPart.refresh();
		add(dataPart, BorderLayout.CENTER);
		validate();
		repaint();		
	}
	
	/////////////////////////////////////////////////////////////////
	// MENU PANEL		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void setMenuPanel(MenuPanel newPanel)
	{	setDataPart(newPanel);
	}
}

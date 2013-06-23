package org.totalboumboum.gui.common.structure.panel;

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

import org.totalboumboum.gui.common.structure.MenuContainer;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class SplitMenuPanel extends MenuPanel implements MenuContainer
{	private static final long serialVersionUID = 1L;

	public SplitMenuPanel(MenuContainer container, MenuPanel parent, String menuLocation, float split)
	{	super(container.getMenuWidth(),container.getMenuHeight());
		
		// fields
		this.container = container;
		this.parent = parent;
		this.menuLocation = menuLocation;
		
		// layout
		BorderLayout layout = new BorderLayout(); 
		setLayout(layout);
		
		// size
		if(menuLocation.equals(BorderLayout.LINE_START) || menuLocation.equals(BorderLayout.LINE_END))
		{	menuWidth = (int)(split*width);
			dataWidth = width - menuWidth;
			menuHeight = height;
			dataHeight = height;			
		}
		else if(menuLocation.equals(BorderLayout.PAGE_START) || menuLocation.equals(BorderLayout.PAGE_END))
		{	menuHeight = (int)(split*height);
			dataHeight = height - menuHeight;
			menuWidth = width;
			dataWidth = width;			
		}
	}	
	
	/////////////////////////////////////////////////////////////////
	// REFRESH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void refresh()
	{	dataPart.refresh();
		menuPart.refresh();
	}
	
	/////////////////////////////////////////////////////////////////
	// MENU PART		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected InnerMenuPanel menuPart;
	protected int menuHeight;
	protected int menuWidth;
	protected String menuLocation;
		
	public void setMenuPart(InnerMenuPanel menuPart)
	{	if(this.menuPart!=null)
			remove(this.menuPart);
		this.menuPart = menuPart;
//		this.menuPart.refresh();
		add(menuPart, menuLocation);
		validate();
		repaint();		
	}
	
	public InnerMenuPanel getMenuPart()
	{	return menuPart;
	}
	
	public int getMenuHeight()
	{	return menuHeight;	
	}
	public int getMenuWidth()
	{	return menuWidth;	
	}

	/////////////////////////////////////////////////////////////////
	// DATA PART		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ContentPanel dataPart;
	protected int dataHeight;
	protected int dataWidth;

	public ContentPanel getDataPart()
	{	return dataPart;
	}
	
	public void setDataPart(ContentPanel dataPart)
	{	if(this.dataPart!=null)
			remove(this.dataPart);
		this.dataPart = dataPart;
//		this.dataPart.refresh();
		add(dataPart, BorderLayout.CENTER);
		validate();
		repaint();		
	}
	public int getDataHeight()
	{	return dataHeight;	
	}
	public int getDataWidth()
	{	return dataWidth;	
	}
	
	/////////////////////////////////////////////////////////////////
	// MENU PANEL		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void setMenuPanel(MenuPanel newPanel)
	{	setDataPart(newPanel);
	}
}

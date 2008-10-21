package fr.free.totalboumboum.gui.common.panel.menu;

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

import fr.free.totalboumboum.gui.common.ButtonAware;
import fr.free.totalboumboum.gui.common.panel.ContentPanel;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.menus.main.MainFrame;

public abstract class InnerMenuPanel extends ContentPanel implements ButtonAware
{	private static final long serialVersionUID = 1L;

	public InnerMenuPanel(SplitMenuPanel container, MenuPanel parent)
	{	super(container.getMenuWidth(),container.getMenuHeight());
		this.container = container;
		this.parent = parent;
	}	
	
	public abstract void refresh();

	/////////////////////////////////////////////////////////////////
	// CONTAINER		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected SplitMenuPanel container;
	
	public SplitMenuPanel getContainer()
	{	return container;
	}
	
	public void replaceWith(MenuPanel newPanel)
	{	container.getContainer().setMenuPanel(newPanel);
	}

	/////////////////////////////////////////////////////////////////
	// DATA PART		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public ContentPanel getDataPart()
	{	return container.getDataPart();	
	}

	/////////////////////////////////////////////////////////////////
	// PARENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected MenuPanel parent;
	
	public MenuPanel getMenuParent()
	{	return parent;
	}

	public void setMenuParent(MenuPanel parent)
	{	this.parent = parent;
	}
	
	/////////////////////////////////////////////////////////////////
	// FRAME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public MainFrame getFrame()
	{	return container.getFrame();
	}
}

package org.totalboumboum.gui.common.structure;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.frames.NormalFrame;

/**
 * 
 * @author Vincent Labatut
 *
 */
public interface MenuContainer
{	
	/////////////////////////////////////////////////////////////////
	// FRAME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public NormalFrame getFrame();

	/////////////////////////////////////////////////////////////////
	// MENU PANEL		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int getMenuHeight();
	public int getMenuWidth();
	public void setMenuPanel(MenuPanel newPanel);
}

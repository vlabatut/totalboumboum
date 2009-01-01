package fr.free.totalboumboum.gui.menus.about;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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


import fr.free.totalboumboum.gui.common.structure.dialog.ModalDialogPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class AboutModalDialogPanel extends ModalDialogPanel<AboutSubPanel>
{	private static final long serialVersionUID = 1L;

	public AboutModalDialogPanel(MenuPanel parent)
	{	super(parent,new AboutSubPanel((int)(parent.getFrame().getMenuWidth()*GuiTools.MODAL_DIALOG_RATIO),(int)(parent.getFrame().getMenuHeight()*GuiTools.MODAL_DIALOG_RATIO)));
		AboutSubPanel subPanel = getSubPanel();
		subPanel.addListener(this);
	}

	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
	{	
	}

	/////////////////////////////////////////////////////////////////
	// MODAL DIALOG SUB PANEL LISTENER	/////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void modalDialogButtonClicked(String buttonCode)
	{	fireModalDialogButtonClicked(buttonCode);
	}
}

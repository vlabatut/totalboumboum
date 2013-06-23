package org.totalboumboum.gui.common.structure.dialog.outside;

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

import java.util.List;

import org.totalboumboum.gui.common.structure.dialog.inside.InfoSubPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.gui.tools.GuiImageTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class InfoModalDialogPanel extends ModalDialogPanel<InfoSubPanel>
{	private static final long serialVersionUID = 1L;

	public InfoModalDialogPanel(MenuPanel parent, String key, List<String> text)
	{	super(parent,new InfoSubPanel((int)(parent.getFrame().getMenuWidth()*GuiSizeTools.MODAL_DIALOG_RATIO),(int)(parent.getFrame().getMenuHeight()*GuiSizeTools.MODAL_DIALOG_RATIO),key,text));
		InfoSubPanel subPanel = getSubPanel();
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

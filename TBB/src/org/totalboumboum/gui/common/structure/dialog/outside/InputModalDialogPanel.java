package org.totalboumboum.gui.common.structure.dialog.outside;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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

import org.totalboumboum.gui.common.structure.dialog.inside.InputSubPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.tools.GuiTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class InputModalDialogPanel extends ModalDialogPanel<InputSubPanel>
{	private static final long serialVersionUID = 1L;

	public InputModalDialogPanel(MenuPanel parent, String key, List<String> text, String defaultText)
	{	super(parent,new InputSubPanel((int)(parent.getFrame().getMenuWidth()*GuiTools.MODAL_DIALOG_RATIO),(int)(parent.getFrame().getMenuHeight()*GuiTools.MODAL_DIALOG_RATIO),key,text,defaultText));
		InputSubPanel subPanel = getSubPanel();
		subPanel.addListener(this);
	}

	/////////////////////////////////////////////////////////////////
	// INPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String getInput()
	{	return getSubPanel().getInput();	
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

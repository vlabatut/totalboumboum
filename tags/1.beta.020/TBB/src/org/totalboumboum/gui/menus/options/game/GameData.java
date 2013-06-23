package org.totalboumboum.gui.menus.options.game;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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

import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;

import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.InnerDataPanel;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class GameData extends InnerDataPanel
{	private static final long serialVersionUID = 1L;
	
	public GameData(SplitMenuPanel container)
	{	super(container);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setOpaque(false);
	}

	public void actionPerformed(ActionEvent e)
	{	/*System.out.println(e.getActionCommand());
		if(e.getActionCommand().equals(BUTTON_NEW))
		{	
	    }
		else if(e.getActionCommand().equals(BUTTON_LOAD))
		{	
	    }
		else if(e.getActionCommand().equals(BUTTON_EDIT))
		{	
	    }
		else if(e.getActionCommand().equals(BUTTON_PLAYERS))
		{	
	    }
		else if(e.getActionCommand().equals(BUTTON_START))
		{	
	    }
		else if(e.getActionCommand().equals(BUTTON_BACK))
		{	switchToPanel(tournamentMainPanel);
	    }*/
	}

	public void refresh()
	{	// NOTE à compléter
	}
}

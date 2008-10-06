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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;

import fr.free.totalboumboum.gui.common.InnerDataPanel;
import fr.free.totalboumboum.gui.common.SplitMenuPanel;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class TournamentData extends InnerDataPanel
{	private static final long serialVersionUID = 1L;
	
	public TournamentData(SplitMenuPanel container)
	{	super(container);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// size
		int height = GuiTools.getSize(GuiTools.VERTICAL_SPLIT_DATA_PANEL_HEIGHT);
		int width = GuiTools.getSize(GuiTools.VERTICAL_SPLIT_DATA_PANEL_WIDTH);
		setPreferredSize(new Dimension(width,height));
		
		// background
//		setBackground(Color.YELLOW);
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
	{	// NOTE � compl�ter
	}

	@Override
	public void updateData()
	{	// no use
	}
}

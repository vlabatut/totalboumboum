package fr.free.totalboumboum.gui.common.content.subpanel.archive;

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
import java.util.ArrayList;

import fr.free.totalboumboum.configuration.GameConstants;
import fr.free.totalboumboum.game.archive.GameArchive;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class ArchivePlayersSubPanel extends UntitledSubPanelTable
{	private static final long serialVersionUID = 1L;
	
	public ArchivePlayersSubPanel(int width, int height)
	{	super(width,height,1,1,1,true);
		
		setGameArchive(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// ARCHIVE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private GameArchive gameArchive;

	public GameArchive getGameArchive()
	{	return gameArchive;	
	}
	
	public void setGameArchive(GameArchive gameArchive)
	{	this.gameArchive = gameArchive;
		
		// sizes
		int colSubs = 1;
		int colGroups = 1;
		int lines = GameConstants.MAX_PROFILES_COUNT;
		reinit(colGroups,colSubs,lines);
		
		// header
		int line = 0;
		{	String key = GuiKeys.COMMON_ARCHIVE_PLAYERS;
			setLabelKey(line,0,key,true);
			Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
			setLabelBackground(line,0,bg);
			line++;
		}
				
		// data
		if(gameArchive!=null)
		{	// text
			ArrayList<String> textValues = gameArchive.getPlayers();
			ArrayList<String> tooltipValues = textValues;
			// content
			while(line<lines && (line-1)<textValues.size())
			{	String text = textValues.get(line-1);
				String tooltip = tooltipValues.get(line-1);
				setLabelText(line,0,text,tooltip);
				Color fg = GuiTools.COLOR_TABLE_REGULAR_FOREGROUND;
				setLabelForeground(line,0,fg);
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				setLabelBackground(line,0,bg);
			}
		}
		
		// empty lines
		while(line<lines)
		{	String text = null;
			String tooltip = null;
			setLabelText(line,0,text,tooltip);
			Color bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
			setLabelBackground(line,0,bg);
			line++;
		}
		
		int maxWidth = width-2*GuiTools.subPanelMargin;
		setColSubMaxWidth(0,maxWidth);
		setColSubPreferredWidth(0,maxWidth);
	}
}

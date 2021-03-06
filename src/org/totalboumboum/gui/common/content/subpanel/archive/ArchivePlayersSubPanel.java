package org.totalboumboum.gui.common.content.subpanel.archive;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
import java.util.List;

import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.stream.file.archive.GameArchive;
import org.totalboumboum.tools.GameData;

/**
 * Panel displaying players involved in a saved game.
 * 
 * @author Vincent Labatut
 */
public class ArchivePlayersSubPanel extends TableSubPanel
{	/** Class id */
	private static final long serialVersionUID = 1L;
	/** Number of columns by group */
	private static final int COL_SUBS = 1;
	/** Number of column groups */
	private static final int COL_GROUPS = 1;
	/** Number of lines */
	private static final int LINES = GameData.MAX_PROFILES_COUNT;
	
	/**
	 * Builds a new panel.
	 * 
	 * @param width
	 * 		Width of the panel.
	 * @param height
	 * 		Height of the panel.
	 */
	public ArchivePlayersSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,LINES,COL_GROUPS,COL_SUBS,true);
		setGameArchive(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// ARCHIVE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Object displayed in this panel */
	private GameArchive gameArchive;
	
	/**
	 * Returns the object displayed by this panel.
	 * 
	 * @return
	 * 		Object displayed by this panel.
	 */
	public GameArchive getGameArchive()
	{	return gameArchive;	
	}
	
	/**
	 * Changes the object displayed by this panel.
	 * 
	 * @param gameArchive
	 * 		Object displayed by this panel.
	 */
	public void setGameArchive(GameArchive gameArchive)
	{	this.gameArchive = gameArchive;
		
		// sizes
		reinit(LINES,COL_GROUPS,COL_SUBS);
		
		// header
		int line = 0;
		{	String key = GuiKeys.COMMON_ARCHIVE_PLAYERS;
			setLabelKey(line,0,key,true);
			Color bg = GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND;
			setLabelBackground(line,0,bg);
			line++;
		}
				
		// data
		if(gameArchive!=null)
		{	// text
			List<String> textValues = gameArchive.getPlayers();
			List<String> tooltipValues = textValues;
			// content
			while(line<LINES && (line-1)<textValues.size())
			{	String text = textValues.get(line-1);
				String tooltip = tooltipValues.get(line-1);
				setLabelText(line,0,text,tooltip);
				Color fg = GuiColorTools.COLOR_TABLE_REGULAR_FOREGROUND;
				setLabelForeground(line,0,fg);
				Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
				setLabelBackground(line,0,bg);
				line++;
			}
		}
		
		// empty lines
		while(line<LINES)
		{	String text = null;
			String tooltip = null;
			setLabelText(line,0,text,tooltip);
			Color bg = GuiColorTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
			setLabelBackground(line,0,bg);
			line++;
		}
		
		int maxWidth = getDataWidth();
		setColSubMaxWidth(0,maxWidth);
		setColSubPrefWidth(0,maxWidth);
	}
}

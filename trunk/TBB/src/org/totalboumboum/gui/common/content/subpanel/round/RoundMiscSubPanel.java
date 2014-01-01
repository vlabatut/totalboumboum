package org.totalboumboum.gui.common.content.subpanel.round;

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
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.engine.container.level.players.Players;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;

/**
 * Panel displaying round info.
 * 
 * @author Vincent Labatut
 */
public class RoundMiscSubPanel extends TableSubPanel
{	/** Class id */
	private static final long serialVersionUID = 1L;
	/** Number of lines */
	private static final int LINES = 5;
	/** Number of columns by groups */
	private static final int COL_SUBS = 2;
	/** Number of column groups */
	private static final int COL_GROUPS = 1;
	
	/**
	 * Builds a new panel.
	 * 
	 * @param width
	 * 		Width of the panel.
	 * @param height
	 * 		Height of the panel.
	 */
	public RoundMiscSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,LINES,COL_GROUPS,COL_SUBS,true);
		
		setRound(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// ROUND			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Round round;

	public Round getRound()
	{	return round;	
	}
	
	public void setRound(Round round)
	{	this.round = round;
		
		// sizes
		reinit(LINES,COL_GROUPS,COL_SUBS);
		
		// icons
		List<String> keys = new ArrayList<String>();
		if(showTitle)
			keys.add(GuiKeys.COMMON_ROUND_TITLE);
		if(showAuthor)
			keys.add(GuiKeys.COMMON_ROUND_AUTHOR);
		if(showAllowedPlayerNumbers)
			keys.add(GuiKeys.COMMON_ROUND_ALLOWED_PLAYERS);
		if(showLevelFolder)
			keys.add(GuiKeys.COMMON_ROUND_LEVEL_FOLDER);
		if(showLevelPack)
			keys.add(GuiKeys.COMMON_ROUND_LEVEL_PACK);
		
		if(round!=null)
		{	// text
			List<String> values = new ArrayList<String>();
			if(showTitle)
				values.add(round.getName());
			if(showAuthor)
				values.add(round.getAuthor());
			if(showAllowedPlayerNumbers)
				values.add(Players.formatAllowedPlayerNumbers(round.getAllowedPlayerNumbers()));
			if(showLevelFolder)
				values.add(round.getHollowLevel().getLevelInfo().getFolder());
			if(showLevelPack)
				values.add(round.getHollowLevel().getLevelInfo().getPackName());
			
			// content
			for(int line=0;line<keys.size();line++)
			{	// header
				int colSub = 0;
				{	setLabelKey(line,colSub,keys.get(line),true);
					Color bg = GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND;
					setLabelBackground(line,colSub,bg);
					colSub++;
				}
				// data
				{	String text = values.get(line);
					String tooltip = text;
					setLabelText(line,colSub,text,tooltip);
					Color fg = GuiColorTools.COLOR_TABLE_HEADER_FOREGROUND;
					setLabelForeground(line,0,fg);
					Color bg;
					if(line>0)
						bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					else
						bg = GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND;
					setLabelBackground(line,colSub,bg);
					colSub++;
				}
			}
		}
		else
		{	for(int line=0;line<keys.size();line++)
			{	// header
				int colSub = 0;
				{	setLabelKey(line,colSub,keys.get(line),true);
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					setLabelBackground(line,colSub,bg);
					colSub++;
				}
				// data
				{	String text = null;
					String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(keys.get(line)+GuiKeys.TOOLTIP);
					setLabelText(line,colSub,text,tooltip);
					Color bg;
					if(line>0)
						bg = GuiColorTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
					else
						bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					setLabelBackground(line,colSub,bg);
					colSub++;
				}
			}
		}
		
		// col widths
		int iconWidth = getHeaderHeight();
		setColSubMinWidth(0,iconWidth);
		setColSubPrefWidth(0,iconWidth);
		setColSubMaxWidth(0,iconWidth);
		int maxWidth = getDataWidth()-(COL_SUBS-1)*GuiSizeTools.subPanelMargin-iconWidth;
		setColSubMinWidth(1,maxWidth);
		setColSubPrefWidth(1,maxWidth);
		setColSubMaxWidth(1,maxWidth);
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean showTitle = true;
	private boolean showAuthor = true;
	private boolean showAllowedPlayerNumbers = true;
	private boolean showLevelFolder = true;
	private boolean showLevelPack = true;

	public void setShowTitle(boolean showTitle)
	{	this.showTitle = showTitle;
	}

	public void setShowAuthor(boolean showAuthor)
	{	this.showAuthor = showAuthor;
	}

	public void setShowAllowedPlayerNumbers(boolean showAllowedPlayerNumbers)
	{	this.showAllowedPlayerNumbers = showAllowedPlayerNumbers;
	}

	public void setShowLevelFolder(boolean showLevelFolder)
	{	this.showLevelFolder = showLevelFolder;
	}

	public void setShowLevelPack(boolean showLevelPack)
	{	this.showLevelPack = showLevelPack;
	}
}

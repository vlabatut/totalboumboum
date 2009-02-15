package fr.free.totalboumboum.gui.common.content.subpanel.round;

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

import java.awt.Color;
import java.util.ArrayList;

import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.gui.common.structure.subpanel.outside.SubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.outside.TableSubPanel;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.StringTools;

public class RoundMiscSubPanel extends TableSubPanel
{	private static final long serialVersionUID = 1L;
	
	public RoundMiscSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,1,1,1,true);
		
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
		int lines = 5;
		int colSubs = 2;
		int colGroups = 1;
		reinit(colGroups,colSubs,lines);
		
		// icons
		ArrayList<String> keys = new ArrayList<String>();
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
			ArrayList<String> values = new ArrayList<String>();
			if(showTitle)
				values.add(round.getName());
			if(showAuthor)
				values.add(round.getAuthor());
			if(showAllowedPlayerNumbers)
				values.add(StringTools.formatAllowedPlayerNumbers(round.getAllowedPlayerNumbers()));
			if(showLevelFolder)
				values.add(round.getHollowLevel().getFolderName());
			if(showLevelPack)
				values.add(round.getHollowLevel().getPackName());
			
			// content
			for(int line=0;line<keys.size();line++)
			{	// header
				int colSub = 0;
				{	setLabelKey(line,colSub,keys.get(line),true);
					Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					setLabelBackground(line,colSub,bg);
					colSub++;
				}
				// data
				{	String text = values.get(line);
					String tooltip = text;
					setLabelText(line,colSub,text,tooltip);
					Color fg = GuiTools.COLOR_TABLE_HEADER_FOREGROUND;
					setLabelForeground(line,0,fg);
					Color bg;
					if(line>0)
						bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					else
						bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
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
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					setLabelBackground(line,colSub,bg);
					colSub++;
				}
				// data
				{	String text = null;
					String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(keys.get(line)+GuiKeys.TOOLTIP);
					setLabelText(line,colSub,text,tooltip);
					Color bg;
					if(line>0)
						bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
					else
						bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					setLabelBackground(line,colSub,bg);
					colSub++;
				}
			}
		}
		
		int maxWidth = getDataWidth()-3*GuiTools.subPanelMargin-getHeaderHeight();
		setColSubMaxWidth(1,maxWidth);
		setColSubPreferredWidth(1,maxWidth);
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

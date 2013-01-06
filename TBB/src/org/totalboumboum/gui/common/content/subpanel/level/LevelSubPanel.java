package org.totalboumboum.gui.common.content.subpanel.level;

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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.engine.container.level.players.Players;
import org.totalboumboum.engine.container.level.preview.LevelPreview;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.gui.tools.GuiTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class LevelSubPanel extends TableSubPanel
{	private static final long serialVersionUID = 1L;
	private static final int LINES = 8;
	private static final int COL_SUBS = 2;
	private static final int COL_GROUPS = 1;
	
	public LevelSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,LINES,COL_GROUPS,COL_SUBS,true);
		
		setLevelPreview(null,8);
	}
	
	/////////////////////////////////////////////////////////////////
	// LEVEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private LevelPreview levelPreview;
	
	public LevelPreview getLevelPreview()
	{	return levelPreview;	
	}
	
	public void setLevelPreview(LevelPreview levelPreview, int lines)
	{	this.levelPreview = levelPreview;
		
		// sizes
		if(showTitle)
		{	setMode(Mode.TITLE);
			String titleKey = GuiKeys.COMMON_LEVEL_TITLE;
			setTitleKey(titleKey,true);
		}
		else
			setMode(Mode.BORDER);
		reinit(lines,COL_GROUPS,COL_SUBS);
		
		// icons
		List<String> keys = new ArrayList<String>();
		if(showName)
			keys.add(GuiKeys.COMMON_LEVEL_NAME);
		if(showPack)
			keys.add(GuiKeys.COMMON_LEVEL_PACK);
		if(showSource)
			keys.add(GuiKeys.COMMON_LEVEL_SOURCE);
		if(showAuthor)
			keys.add(GuiKeys.COMMON_LEVEL_AUTHOR);
		if(showInstance)
			keys.add(GuiKeys.COMMON_LEVEL_INSTANCE);
		if(showTheme)
			keys.add(GuiKeys.COMMON_LEVEL_THEME);
		if(showDimension)
			keys.add(GuiKeys.COMMON_LEVEL_DIMENSION);
		if(showAllowedPlayerNumbers)
			keys.add(GuiKeys.COMMON_LEVEL_ALLOWED_PLAYERS);
		
		if(levelPreview!=null)
		{	// text
			List<String> values = new ArrayList<String>();
			if(showName)
				values.add(levelPreview.getLevelInfo().getTitle());
			if(showPack)
				values.add(levelPreview.getLevelInfo().getPackName());
			if(showSource)
				values.add(levelPreview.getLevelInfo().getSource());
			if(showAuthor)
				values.add(levelPreview.getLevelInfo().getAuthor());
			if(showInstance)
				values.add(levelPreview.getLevelInfo().getInstanceName());
			if(showTheme)
				values.add(levelPreview.getLevelInfo().getThemeName());
			if(showDimension)
				values.add(Integer.toString(levelPreview.getLevelInfo().getVisibleHeight())+new Character('\u00D7').toString()+Integer.toString(levelPreview.getLevelInfo().getVisibleWidth()));
			if(showAllowedPlayerNumbers)
				values.add(Players.formatAllowedPlayerNumbers(levelPreview.getAllowedPlayerNumbers()));
			
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
					if(line==0 && !showTitle)
						bg = GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND;
					else
						bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
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
					if(line==0 && !showTitle)
						bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					else
						bg = GuiColorTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
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
	private boolean showName = true;
	private boolean showPack = true;
	private boolean showSource = true;
	private boolean showAuthor = true;
	private boolean showInstance = true;
	private boolean showTheme = true;
	private boolean showDimension = true;
	private boolean showAllowedPlayerNumbers = true;

	public void setShowTitle(boolean showTitle)
	{	this.showTitle = showTitle;
		setLevelPreview(levelPreview,getLineCount());
	}

	public void setShowPack(boolean showPack)
	{	this.showPack = showPack;
	}

	public void setShowName(boolean showName)
	{	this.showName = showName;
	}

	public void setShowSource(boolean showSource)
	{	this.showSource = showSource;
	}

	public void setShowAuthor(boolean showAuthor)
	{	this.showAuthor = showAuthor;
	}

	public void setShowInstance(boolean showInstance)
	{	this.showInstance = showInstance;
	}

	public void setShowTheme(boolean showTheme)
	{	this.showTheme = showTheme;
	}

	public void setShowDimension(boolean showDimension)
	{	this.showDimension = showDimension;
	}

	public void setShowAllowedPlayerNumbers(boolean showAllowedPlayerNumbers)
	{	this.showAllowedPlayerNumbers = showAllowedPlayerNumbers;
	}
}

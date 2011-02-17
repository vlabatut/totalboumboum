package org.totalboumboum.gui.common.content.subpanel.archive;

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

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.totalboumboum.game.tournament.TournamentType;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.stream.file.archive.GameArchive;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ArchiveMiscSubPanel extends TableSubPanel
{	private static final long serialVersionUID = 1L;
	private static final int COL_SUBS = 2;
	private static final int COL_GROUPS = 1;
	
	public ArchiveMiscSubPanel(int width, int height, int lines)
	{	super(width,height,SubPanel.Mode.BORDER,lines,COL_GROUPS,COL_SUBS,true);
		
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
		reinit(getLineCount(),COL_GROUPS,COL_SUBS);
		
		// icons
		ArrayList<String> keys = new ArrayList<String>();
		if(showName)
			keys.add(GuiKeys.COMMON_ARCHIVE_NAME);
		if(showType)
			keys.add(GuiKeys.COMMON_ARCHIVE_TYPE);
		if(showConfrontations)
			keys.add(GuiKeys.COMMON_ARCHIVE_CONFRONTATIONS);
		if(showStart)
			keys.add(GuiKeys.COMMON_ARCHIVE_START);
		if(showSave)
			keys.add(GuiKeys.COMMON_ARCHIVE_SAVE);
		
		if(gameArchive!=null)
		{	// text
			ArrayList<String> textValues = new ArrayList<String>();
			ArrayList<String> tooltipValues = new ArrayList<String>();
			if(showName)
			{	textValues.add(gameArchive.getName());
				tooltipValues.add(gameArchive.getName());
			}
			if(showType)
			{	String key = "";
				TournamentType type = gameArchive.getType();
				if(type==TournamentType.CUP)
					key = GuiKeys.COMMON_ARCHIVE_TYPES_CUP;
				else if(type==TournamentType.LEAGUE)
					key = GuiKeys.COMMON_ARCHIVE_TYPES_LEAGUE;
				else if(type==TournamentType.SEQUENCE)
					key = GuiKeys.COMMON_ARCHIVE_TYPES_SEQUENCE;
				else if(type==TournamentType.SINGLE)
					key = GuiKeys.COMMON_ARCHIVE_TYPES_SINGLE;
				textValues.add(GuiConfiguration.getMiscConfiguration().getLanguage().getText(key));
				tooltipValues.add(GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP));
			}
			if(showConfrontations)
			{	String matches = Integer.toString(gameArchive.getPlayedMatches());
				String rounds = Integer.toString(gameArchive.getPlayedRounds());
				String text = matches+" : "+rounds;
				textValues.add(text);
				tooltipValues.add(text);
			}
			if(showStart)
			{	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String start = sdf.format(gameArchive.getStartDate());
				textValues.add(start);
				tooltipValues.add(start);
			}
			
			if(showSave)
			{	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String save = sdf.format(gameArchive.getSaveDate());
				textValues.add(save);
				tooltipValues.add(save);
			}

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
				{	String text = textValues.get(line);
					String tooltip = tooltipValues.get(line);
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
		
		// col widths
		int iconWidth = getHeaderHeight();
		setColSubMinWidth(0,iconWidth);
		setColSubPrefWidth(0,iconWidth);
		setColSubMaxWidth(0,iconWidth);
		int maxWidth = getDataWidth()-(COL_SUBS-1)*GuiTools.subPanelMargin-iconWidth;
		setColSubMinWidth(1,maxWidth);
		setColSubPrefWidth(1,maxWidth);
		setColSubMaxWidth(1,maxWidth);
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean showName = true;
	private boolean showType = true;
	private boolean showConfrontations = true;
	private boolean showStart = true;
	private boolean showSave = true;

	public void setShowName(boolean showName)
	{	this.showName = showName;
	}

	public void setShowType(boolean showType)
	{	this.showType = showType;
	}

	public void setShowConfrontations(boolean showConfrontations)
	{	this.showConfrontations = showConfrontations;
	}

	public void setShowStart(boolean showStart)
	{	this.showStart = showStart;
	}

	public void setShowSave(boolean showSave)
	{	this.showSave = showSave;
	}

}

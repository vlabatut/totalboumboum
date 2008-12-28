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
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import fr.free.totalboumboum.game.archive.GameArchive;
import fr.free.totalboumboum.game.archive.TournamentType;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class ArchiveMiscSubPanel extends UntitledSubPanelTable
{	private static final long serialVersionUID = 1L;
	
	public ArchiveMiscSubPanel(int width, int height, int lines)
	{	super(width,height,1,1,1,true);
		
		this.lines = lines;
		setGameArchive(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// ARCHIVE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private GameArchive gameArchive;
	private int lines;

	public GameArchive getGameArchive()
	{	return gameArchive;	
	}
	
	public void setGameArchive(GameArchive gameArchive)
	{	this.gameArchive = gameArchive;
		
		// sizes
		int colSubs = 2;
		int colGroups = 1;
		reinit(colGroups,colSubs,lines);
		
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
				textValues.add(matches+" ; "+rounds);
				tooltipValues.add(matches);
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
		
		int maxWidth = width-3*GuiTools.subPanelMargin-getHeaderHeight();
		setColSubMaxWidth(1,maxWidth);
		setColSubPreferredWidth(1,maxWidth);
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

package org.totalboumboum.gui.common.content.subpanel.tournament;

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
import java.util.Set;

import org.totalboumboum.engine.container.level.players.Players;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.game.tournament.TournamentType;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;

/**
 * Panel displaying tournament details.
 * 
 * @author Vincent Labatut
 */
public class TournamentMiscSubPanel extends TableSubPanel
{	/** Class id */
	private static final long serialVersionUID = 1L;
	/** Number of columns by group */
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
	 * @param lines
	 * 		Number of lines in the panel table.
	 */
	public TournamentMiscSubPanel(int width, int height, int lines)
	{	super(width,height,SubPanel.Mode.BORDER,lines,COL_GROUPS,COL_SUBS,true);
	
		setTournament(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Tournament currently displayed */
	private AbstractTournament tournament;
	/** Position of the line displaying the allowed numbers of players */
	private int allowedPlayersLine;
	
	/**
	 * Returns the currently displayed tournament.
	 * 
	 * @return
	 * 		Current tournament.
	 */
	public AbstractTournament getTournament()
	{	return tournament;	
	}
	
	/**
	 * Changes the displayed tournament.
	 * 
	 * @param tournament
	 * 		New tournament to display.
	 */
	public void setTournament(AbstractTournament tournament)
	{	this.tournament = tournament;
		
		// sizes
		reinit(getLineCount(),COL_GROUPS,COL_SUBS);
		
		// icons
		List<String> keys = new ArrayList<String>();
		allowedPlayersLine = 0;
		if(showName)
		{	keys.add(GuiKeys.COMMON_TOURNAMENT_NAME);
			allowedPlayersLine++;
		}
		if(showAuthor)
		{	keys.add(GuiKeys.COMMON_TOURNAMENT_AUTHOR);
			allowedPlayersLine++;
		}
		if(showType)
		{	keys.add(GuiKeys.COMMON_TOURNAMENT_TYPE);
			allowedPlayersLine++;
		}
		if(showAllowedPlayerNumbers)
			keys.add(GuiKeys.COMMON_TOURNAMENT_ALLOWED_PLAYERS);
		
		if(tournament!=null)
		{	// text
			List<String> textValues = new ArrayList<String>();
			List<String> tooltipValues = new ArrayList<String>();
			if(showName)
			{	textValues.add(tournament.getName());
				tooltipValues.add(tournament.getName());
			}
			if(showAuthor)
			{	textValues.add(tournament.getAuthor());
				tooltipValues.add(tournament.getAuthor());
			}
			if(showType)
			{	TournamentType type = tournament.getType();
				String key = GuiKeys.COMMON_TOURNAMENT_TYPES+type.stringFormat();
				textValues.add(GuiConfiguration.getMiscConfiguration().getLanguage().getText(key));
				tooltipValues.add(GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP));
			}
			if(showAllowedPlayerNumbers)
			{	Set<Integer> allowedPlayers = tournament.getAllowedPlayerNumbers();
				textValues.add(Players.formatAllowedPlayerNumbers(allowedPlayers));
				tooltipValues.add(Players.formatAllowedPlayerNumbers(allowedPlayers));
			}
			
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
				{	String text = textValues.get(line);
					String tooltip = tooltipValues.get(line);
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
		int maxWidth = getDataWidth()-(COL_SUBS-1)*GuiSizeTools.subPanelMargin - iconWidth;
		setColSubMinWidth(1,maxWidth);
		setColSubPrefWidth(1,maxWidth);
		setColSubMaxWidth(1,maxWidth);
	}
	
	/**
	 * Puts specific colors on the allowed numbers of players.
	 * 
	 * @param flag
	 * 		Wheter or not to show the allowed numbers of players.
	 */
	public void selectAllowedPlayers(boolean flag)
	{	Color hbg,dbg;
		if(showAllowedPlayerNumbers && flag)
		{	hbg = GuiColorTools.COLOR_TABLE_SELECTED_DARK_BACKGROUND;
			dbg = GuiColorTools.COLOR_TABLE_SELECTED_BACKGROUND;
		}
		else
		{	hbg = GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND;
			dbg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
		}
		setLabelBackground(allowedPlayersLine,0,hbg);
		setLabelBackground(allowedPlayersLine,1,dbg);
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether or not to show the tournament name */
	private boolean showName = true;
	/** Whether or not to show the tournament author */
	private boolean showAuthor = true;
	/** Whether or not to show the tournament type */
	private boolean showType = true;
	/** Whether or not to show the allowed numbers of players */
	private boolean showAllowedPlayerNumbers = true;
	
	/**
	 * Enables/disables the displaying of the tournament name.
	 * 
	 * @param showName
	 * 		{@code true} to enable display.
	 */
	public void setShowName(boolean showName)
	{	this.showName = showName;
	}

	/**
	 * Enables/disables the displaying of the tournament author.
	 * 
	 * @param showAuthor
	 * 		{@code true} to enable display.
	 */
	public void setShowAuthor(boolean showAuthor)
	{	this.showAuthor = showAuthor;
	}

	/**
	 * Enables/disables the displaying of the tournament type.
	 * 
	 * @param showType
	 * 		{@code true} to enable display.
	 */
	public void setShowType(boolean showType)
	{	this.showType = showType;
	}

	/**
	 * Enables/disables the displaying of the allowed numbers of players.
	 * 
	 * @param showAllowedPlayerNumbers
	 * 		{@code true} to enable display.
	 */
	public void setShowAllowedPlayerNumbers(boolean showAllowedPlayerNumbers)
	{	this.showAllowedPlayerNumbers = showAllowedPlayerNumbers;
	}

}

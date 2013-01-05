package org.totalboumboum.gui.common.content.subpanel.tournament;

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
import java.util.Set;

import org.totalboumboum.engine.container.level.players.Players;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.game.tournament.cup.CupTournament;
import org.totalboumboum.game.tournament.league.LeagueTournament;
import org.totalboumboum.game.tournament.sequence.SequenceTournament;
import org.totalboumboum.game.tournament.single.SingleTournament;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class TournamentMiscSubPanel extends TableSubPanel
{	private static final long serialVersionUID = 1L;
	private static final int COL_SUBS = 2;
	private static final int COL_GROUPS = 1;
	
	public TournamentMiscSubPanel(int width, int height, int lines)
	{	super(width,height,SubPanel.Mode.BORDER,lines,COL_GROUPS,COL_SUBS,true);
	
		setTournament(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AbstractTournament tournament;
	private int allowedPlayersLine;

	public AbstractTournament getTournament()
	{	return tournament;	
	}
	
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
			{	String key = "";
				if(tournament instanceof CupTournament)
					key = GuiKeys.COMMON_TOURNAMENT_TYPES_CUP;
				else if(tournament instanceof LeagueTournament)
					key = GuiKeys.COMMON_TOURNAMENT_TYPES_LEAGUE;
				else if(tournament instanceof SequenceTournament)
					key = GuiKeys.COMMON_TOURNAMENT_TYPES_SEQUENCE;
				else if(tournament instanceof SingleTournament)
					key = GuiKeys.COMMON_TOURNAMENT_TYPES_SINGLE;
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
		int maxWidth = getDataWidth()-(COL_SUBS-1)*GuiTools.subPanelMargin - iconWidth;
		setColSubMinWidth(1,maxWidth);
		setColSubPrefWidth(1,maxWidth);
		setColSubMaxWidth(1,maxWidth);
	}
	
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
	private boolean showName = true;
	private boolean showAuthor = true;
	private boolean showType = true;
	private boolean showAllowedPlayerNumbers = true;

	public void setShowName(boolean showName)
	{	this.showName = showName;
	}

	public void setShowAuthor(boolean showAuthor)
	{	this.showAuthor = showAuthor;
	}

	public void setShowType(boolean showType)
	{	this.showType = showType;
	}

	public void setShowAllowedPlayerNumbers(boolean showAllowedPlayerNumbers)
	{	this.showAllowedPlayerNumbers = showAllowedPlayerNumbers;
	}

}

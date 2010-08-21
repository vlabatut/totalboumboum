package org.totalboumboum.gui.common.content.subpanel.game;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.totalboumboum.engine.container.level.players.Players;
import org.totalboumboum.game.tournament.TournamentType;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.network.game.GameInfo;
import org.totalboumboum.network.host.HostState;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class GameInfoSubPanel extends TableSubPanel
{	private static final long serialVersionUID = 1L;
	private static final int LINES = 8;
	private static final int COL_SUBS = 2;
	private static final int COL_GROUPS = 1;
	
	public GameInfoSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,LINES,COL_GROUPS,COL_SUBS,true);
		
		setGameInfo(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// ROUND			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private GameInfo gameInfo;

	public GameInfo getGameInfo()
	{	return gameInfo;	
	}
	
	public void setGameInfo(GameInfo gameInfo)
	{	this.gameInfo = gameInfo;
		
		// sizes
		reinit(LINES,COL_GROUPS,COL_SUBS);
		
		// icons
		List<String> keys = new ArrayList<String>();
		if(showTournamentName)
			keys.add(GuiKeys.COMMON_GAME_INFO_TOURNAMENT_NAME);
		if(showTournamentType)
			keys.add(GuiKeys.COMMON_GAME_INFO_TOURNAMENT_TYPE);
		if(showAllowedPlayers)
			keys.add(GuiKeys.COMMON_GAME_INFO_ALLOWED_PLAYERS);
		if(showPlayerCount)
			keys.add(GuiKeys.COMMON_GAME_INFO_PLAYER_COUNT);
		if(showAverageScore)
			keys.add(GuiKeys.COMMON_GAME_INFO_AVERAGE_SCORE);
		if(showTournamentState)
			keys.add(GuiKeys.COMMON_GAME_INFO_TOURNAMENT_STATE);
		
		if(gameInfo!=null)
		{	// text
			List<String> texts = new ArrayList<String>();
			List<String> tooltips = new ArrayList<String>();
			if(showTournamentName)
			{	String text = "?";
				if(text!=null)
					text = gameInfo.getTournamentName();
				texts.add(text);
				tooltips.add(text);
			}
			if(showTournamentType)
			{	TournamentType type = gameInfo.getTournamentType();
				String text = "ERROR";
				String tooltip = text;
				if(type!=null)
				{	String key = "";
					if(type.equals(TournamentType.CUP))
						key = GuiKeys.COMMON_GAME_INFO_TOURNAMENT_TYPE_DATA_CUP;
					else if(type.equals(TournamentType.LEAGUE))
						key = GuiKeys.COMMON_GAME_INFO_TOURNAMENT_TYPE_DATA_LEAGUE;
					else if(type.equals(TournamentType.SEQUENCE))
						key = GuiKeys.COMMON_GAME_INFO_TOURNAMENT_TYPE_DATA_SEQUENCE;
					else if(type.equals(TournamentType.SINGLE))
						key = GuiKeys.COMMON_GAME_INFO_TOURNAMENT_TYPE_DATA_SINGLE;
					else if(type.equals(TournamentType.TURNING))
						key = GuiKeys.COMMON_GAME_INFO_TOURNAMENT_TYPE_DATA_TURNING;
					text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
					tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP);
				}
				texts.add(text);
				tooltips.add(tooltip);
			}
			if(showAllowedPlayers)
			{	Set<Integer> value = gameInfo.getAllowedPlayers();
				String text = "?";
				if(value!=null)
					text = Players.formatAllowedPlayerNumbers(value);
				texts.add(text);
				tooltips.add(text);
			}
			if(showPlayerCount)
			{	Integer value = gameInfo.getPlayerCount();
				String text = "?";
				if(value!=null)
					text = Integer.toString(value);
				texts.add(text);
				tooltips.add(text);
			}
			if(showAverageScore)
			{	Double value = gameInfo.getAverageScore();
				String text = "?";
				if(value!=null)
				{	NumberFormat nf = NumberFormat.getInstance();
					nf.setMaximumFractionDigits(2);
					nf.setMinimumFractionDigits(2);
					text = nf.format(value);
				}
				texts.add(text);
				tooltips.add(text);
			}
			if(showTournamentState)
			{	HostState state = gameInfo.getHostInfo().getState();
				String text = "ERROR";
				String tooltip = text;
				if(state!=null)
				{	String key = "";
					if(state.equals(HostState.CLOSED))
						key = GuiKeys.COMMON_GAME_INFO_TOURNAMENT_STATE_DATA_CLOSED;
					else if(state.equals(HostState.FINISHED))
						key = GuiKeys.COMMON_GAME_INFO_TOURNAMENT_STATE_DATA_FINISHED;
					else if(state.equals(HostState.OPEN))
						key = GuiKeys.COMMON_GAME_INFO_TOURNAMENT_STATE_DATA_OPEN;
					else if(state.equals(HostState.PLAYING))
						key = GuiKeys.COMMON_GAME_INFO_TOURNAMENT_STATE_DATA_PLAYING;
					else if(state.equals(HostState.UNKOWN))
						key = GuiKeys.COMMON_GAME_INFO_TOURNAMENT_STATE_DATA_UNKNOWN;
					text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
					tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP);
				}
				texts.add(text);
				tooltips.add(tooltip);
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
				{	String text = texts.get(line);
					String tooltip = tooltips.get(line);
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
	private boolean showAllowedPlayers = true;
	private boolean showAverageScore = true;
	private boolean showPlayerCount = true;
	private boolean showTournamentName = true;
	private boolean showTournamentState = true;
	private boolean showTournamentType = true;

	public void setShowAllowedPlayers(boolean showAllowedPlayers)
	{	this.showAllowedPlayers = showAllowedPlayers;
	}

	public void setShowAverageScore(boolean showAverageScore)
	{	this.showAverageScore = showAverageScore;
	}

	public void setShowPlayerCount(boolean showPlayerCount)
	{	this.showPlayerCount = showPlayerCount;
	}

	public void setShowTournamentName(boolean showTournamentName)
	{	this.showTournamentName = showTournamentName;
	}

	public void setShowTournamentState(boolean showTournamentState)
	{	this.showTournamentState = showTournamentState;
	}

	public void setShowTournamentType(boolean showTournamentType)
	{	this.showTournamentType = showTournamentType;
	}
}

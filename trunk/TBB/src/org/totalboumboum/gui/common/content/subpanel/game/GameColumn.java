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

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.totalboumboum.configuration.profile.Portraits;
import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.container.level.players.Players;
import org.totalboumboum.game.network.game.GameInfo;
import org.totalboumboum.game.network.host.HostState;
import org.totalboumboum.game.tournament.TournamentType;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.detailed.Score;
import org.totalboumboum.statistics.glicko2.jrs.PlayerRating;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.statistics.overall.PlayerStats;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.time.TimeUnit;

/**
 * 
 * @author Vincent Labatut
 *
 */
public enum GameColumn
{	BUTTON,
	PREFERRED,
	HOST_NAME,
	HOST_IP,
	TOURNAMENT_TYPE,
	PLAYER_COUNT,
	ALLOWED_PLAYER,
	AVERAGE_LEVEL,
	TOURNAMENT_STATE;
	
	public boolean isInverted()
	{	boolean result = false;			

		if(this==BUTTON)
			result = false;
		else if(this==PREFERRED)
			result = false;
		else if(this==HOST_NAME)
			result = false;
		else if(this==HOST_IP)
			result = false;
		else if(this==TOURNAMENT_TYPE)
			result = false;
		else if(this==PLAYER_COUNT)
			result = true;
		else if(this==ALLOWED_PLAYER)
			result = true;
		else if(this==AVERAGE_LEVEL)
			result = true;
		else if(this==TOURNAMENT_STATE)
			result = true;

		return result;
	}
	
	public String getHeaderKey()
	{	String result = null;			

		if(this==BUTTON)
			result = null;
		else if(this==PREFERRED)
			result = GuiKeys.COMMON_GAME_LIST_HEADER_PREFERRED;
		else if(this==HOST_NAME)
			result = GuiKeys.COMMON_GAME_LIST_HEADER_HOST_NAME;
		else if(this==HOST_IP)
			result = GuiKeys.COMMON_GAME_LIST_HEADER_HOST_IP;
		else if(this==TOURNAMENT_TYPE)
			result = GuiKeys.COMMON_GAME_LIST_HEADER_TOURNAMENT_TYPE;
		else if(this==PLAYER_COUNT)
			result = GuiKeys.COMMON_GAME_LIST_HEADER_PLAYER_COUNT;
		else if(this==ALLOWED_PLAYER)
			result = GuiKeys.COMMON_GAME_LIST_HEADER_ALLOWED_PLAYER;
		else if(this==AVERAGE_LEVEL)
			result = GuiKeys.COMMON_GAME_LIST_HEADER_AVERAGE_LEVEL;
		else if(this==TOURNAMENT_STATE)
			result = GuiKeys.COMMON_GAME_LIST_HEADER_TOURNAMENT_STATE;

		return result;
	}
	
	public void setLabelContent(GameListSubPanel container, TableSubPanel panel, int colWidths[], int line, int col, GameInfo gameInfo)
	{	if(this==GENERAL_BUTTON)
		{	String key;
			if(playerRating==null)
				key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_REGISTER;
			else
				key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_UNREGISTER;
			panel.setLabelKey(line,col,key,true);
			MyLabel label = panel.getLabel(line,col);
			label.addMouseListener(container);
			label.setMouseSensitive(true);
		}
//TODO 	BUTTON
	
		else if(this==PREFERRED)
		{	String key;
			if(gameInfo.isPreferred())
				key = GuiKeys.COMMON_GAME_LIST_DATA_FAV_PREFERRED;
			else
				key = GuiKeys.COMMON_GAME_LIST_DATA_FAV_REGULAR;
			if(key!=null)
			{	panel.setLabelKey(line,col,key,true);
				MyLabel label = panel.getLabel(line,col);
				label.addMouseListener(container);
				label.setMouseSensitive(true);
			}
		}
		else if(this==HOST_NAME)
		{	// content
			String text = gameInfo.getHostInfo().getName();
			String tooltip = text;
			panel.setLabelText(line,col,text,tooltip);
			// column width
			int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
			if(temp>colWidths[col])
				colWidths[col] = temp;
		}
		else if(this==HOST_IP)
		{	// content
			String text = gameInfo.getHostInfo().getLastIp().getHostName();
			String tooltip = text;
			panel.setLabelText(line,col,text,tooltip);
//			MyLabel label = panel.getLabel(line,col);
//			label.addMouseListener(container);
//			label.setMouseSensitive(true);
// TODO better doing it in the specifc subpanel			
			// column width
			int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
			if(temp>colWidths[col])
				colWidths[col] = temp;
		}
		else if(this==TOURNAMENT_TYPE)
		{	TournamentType type = gameInfo.getTournamentType();
			String key;
			if(type.equals(TournamentType.CUP))
				key = GuiKeys.COMMON_GAME_LIST_DATA_TYPE_CUP;
			else if(type.equals(TournamentType.LEAGUE))
				key = GuiKeys.COMMON_GAME_LIST_DATA_TYPE_LEAGUE;
			else if(type.equals(TournamentType.SEQUENCE))
				key = GuiKeys.COMMON_GAME_LIST_DATA_TYPE_SEQUENCE;
			else if(type.equals(TournamentType.SINGLE))
				key = GuiKeys.COMMON_GAME_LIST_DATA_TYPE_SINGLE;
			if(key!=null)
				panel.setLabelKey(line,col,key,true);
		}
		else if(this==PLAYER_COUNT)
		{	// content
			String text = Integer.toString(gameInfo.getPlayerCount());
			String tooltip = text;
			panel.setLabelText(line,col,text,tooltip);
			// column width
			int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
			if(temp>colWidths[col])
				colWidths[col] = temp;
		}
		else if(this==ALLOWED_PLAYER)
		{	// content
			Set<Integer> value = gameInfo.getAllowedPlayers();
			String text = Players.formatAllowedPlayerNumbers(value);
			String tooltip = text;
			panel.setLabelText(line,col,text,tooltip);
			// column width
			int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
			if(temp>colWidths[col])
				colWidths[col] = temp;
		}
		else if(this==AVERAGE_LEVEL)
		{	// content
			double mean = gameInfo.getAverageScore();
			NumberFormat nfText = NumberFormat.getInstance();
			nfText.setMaximumFractionDigits(0);
			String text = nfText.format(mean);
			NumberFormat nfTooltip = NumberFormat.getInstance();
			nfTooltip.setMaximumFractionDigits(6);
			String tooltip = nfTooltip.format(mean);
			panel.setLabelText(line,col,text,tooltip);
			// column width
			int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
			if(temp>colWidths[col])
				colWidths[col] = temp;
		}
		else if(this==TOURNAMENT_STATE)
		{	HostState state = gameInfo.getHostInfo().getState();
			String key;
			if(state.equals(HostState.CLOSED))
				key = GuiKeys.COMMON_GAME_LIST_DATA_STATE_CLOSED;
			else if(HostState.equals(HostState.FINISHED))
				key = GuiKeys.COMMON_GAME_LIST_DATA_STATE_FINISHED;
			else if(HostState.equals(HostState.OPEN))
				key = GuiKeys.COMMON_GAME_LIST_DATA_STATE_OPEN;
			else if(HostState.equals(HostState.PLAYING))
				key = GuiKeys.COMMON_GAME_LIST_DATA_STATE_PLAYING;
			else if(HostState.equals(HostState.UNKOWN))
				key = GuiKeys.COMMON_GAME_LIST_DATA_STATE_UNKOWN;
			if(key!=null)
				panel.setLabelKey(line,col,key,true);
		}
	}

	@SuppressWarnings("rawtypes")
	public void updateValues(GameListSubPanel container, HashMap<String,List<Comparable>> playersScores, RankingService rankingService, HashMap<String,Profile> profilesMap)
	{	playersScores.clear();
		for(Entry<String,Profile> entry: profilesMap.entrySet())
		{	// init
			Profile profile = entry.getValue();
			String playerId = entry.getKey();
			PlayerRating playerRating = rankingService.getPlayerRating(playerId);
			HashMap<String,PlayerStats> playersStats = GameStatistics.getPlayersStats();
			PlayerStats playerStats = playersStats.get(playerId);
			int playerRank = rankingService.getPlayerRank(playerId);
			//int playersCount = rankingService.getPlayers().size();
			int previousRank = playerStats.getPreviousRank();
			long totalRoundsPlayed = playerStats.getRoundsPlayed();
			List<Comparable> list = new ArrayList<Comparable>();
			// process
			if(this==GENERAL_BUTTON || this==GENERAL_RANK)
			{	int rank = Integer.MAX_VALUE;
				if(playerRating!=null)
					rank = rankingService.getPlayerRank(playerId);
				list.add(rank);
				list.add(playerId);
			}
			else if(this==GENERAL_EVOLUTION)
			{	int evolution;
				if(playerRating!=null)
				{	if(previousRank==-1)
						evolution = Integer.MIN_VALUE-2;
					else if(previousRank<playerRank)
						evolution = playerRank-previousRank;
					else if(previousRank>playerRank)
						evolution = playerRank-previousRank;
					else
						evolution = playerRank-previousRank;
				}
				else if(previousRank!=-1)
					evolution = Integer.MIN_VALUE-1;
				else
					evolution = Integer.MIN_VALUE;
				list.add(evolution);
				list.add(playerId);
			}
			else if(this==GENERAL_PORTRAIT)
			{	String spriteName = profile.getSpritePack()+File.separator+profile.getSpriteFolder();
				list.add(spriteName);
				list.add(playerId);
			}
			else if(this==GENERAL_TYPE)
			{	Boolean type = profile.getAiName()!=null;
				list.add(type);
				list.add(playerId);
			}
			else if(this==GENERAL_NAME)
			{	String name = profile.getName();
				list.add(name);
				list.add(playerId);
			}
			// glicko-2
			else if(this==GLICKO_MEAN)
			{	double mean = 0;
				double stdev = 0;
				if(playerRating!=null)
				{	mean = playerRating.getRating();
					stdev = playerRating.getRatingDeviation();
				}
				list.add(mean);
				list.add(stdev);
				list.add(playerId);
			}
			else if(this==GLICKO_DEVIATION)
			{	double stdev = 0;
				if(playerRating!=null)
					stdev = playerRating.getRatingDeviation();
				list.add(stdev);
				list.add(playerId);
			}
			else if(this==GLICKO_VOLATILITY)
			{	double volatility = 0;
				if(playerRating!=null)
					volatility = playerRating.getRatingVolatility();
				list.add(volatility);
				list.add(playerId);
			}
			else if(this==GLICKO_ROUNDCOUNT)
			{	int roundcount = 0;
				if(playerRating!=null)
					roundcount = playerRating.getRoundcount();
				list.add(roundcount);
				list.add(playerId);
			}
			// scores
			else if(this==SCORE_BOMBS)
			{	double bombs = playerStats.getScore(Score.BOMBS);
				if(container.hasMean())
				{	if(totalRoundsPlayed>0)
						bombs = bombs / totalRoundsPlayed;
				}
				list.add(bombs);
				list.add(playerId);
			}
			else if(this==SCORE_BOMBINGS)
			{	double bombings = playerStats.getScore(Score.BOMBINGS);
				if(container.hasMean())
				{	if(totalRoundsPlayed>0)
						bombings = bombings / totalRoundsPlayed;
				}
				list.add(bombings);
				list.add(playerId);
			}
			else if(this==SCORE_BOMBEDS)
			{	double bombeds = playerStats.getScore(Score.BOMBEDS);
				if(container.hasMean())
				{	if(totalRoundsPlayed>0)
						bombeds = bombeds / totalRoundsPlayed;
				}
				list.add(bombeds);
				list.add(playerId);
			}
			else if(this==SCORE_ITEMS)
			{	double items = playerStats.getScore(Score.ITEMS);
				if(container.hasMean())
				{	if(totalRoundsPlayed>0)
						items = items / totalRoundsPlayed;
				}
				list.add(items);
				list.add(playerId);
			}
			else if(this==SCORE_CROWNS)
			{	double crowns = playerStats.getScore(Score.CROWNS);
				if(container.hasMean())
				{	if(totalRoundsPlayed>0)
						crowns = crowns / totalRoundsPlayed;
				}
				list.add(crowns);
				list.add(playerId);
			}
			else if(this==SCORE_PAINTINGS)
			{	double paintings = playerStats.getScore(Score.CROWNS);
				if(container.hasMean())
				{	if(totalRoundsPlayed>0)
						paintings = paintings / totalRoundsPlayed;
				}
				list.add(paintings);
				list.add(playerId);
			}
			else if(this==SCORE_SELF_BOMBINGS)
			{	double selfBombings = playerStats.getScore(Score.SELF_BOMBINGS);
				if(container.hasMean())
				{	if(totalRoundsPlayed>0)
						selfBombings = selfBombings / totalRoundsPlayed;
				}
				list.add(selfBombings);
				list.add(playerId);
			}
			else if(this==SCORE_TIME)
			{	double time = playerStats.getScore(Score.TIME);
				if(container.hasMean())
				{	if(totalRoundsPlayed>0)
						time = time / totalRoundsPlayed;
				}
				list.add(time);
				list.add(playerId);
			}
			// confrontations
			else if(this==ROUNDS_PLAYED)
			{	double roundPlayed = totalRoundsPlayed;
				if(container.hasMean())
				{	if(totalRoundsPlayed>0)
						roundPlayed = roundPlayed / totalRoundsPlayed;
				}
				list.add(roundPlayed);
				list.add(playerId);
			}
			else if(this==ROUNDS_WON)
			{	double roundsWon = playerStats.getRoundsWon();
				if(container.hasMean())
				{	if(totalRoundsPlayed>0)
						roundsWon = roundsWon / totalRoundsPlayed;
				}
				list.add(roundsWon);
				list.add(playerId);
			}
			else if(this==ROUNDS_DRAWN)
			{	double roundsDrawn = playerStats.getRoundsDrawn();
				if(container.hasMean())
				{	if(totalRoundsPlayed>0)
						roundsDrawn = roundsDrawn / totalRoundsPlayed;
				}
				list.add(roundsDrawn);
				list.add(playerId);
			}
			else if(this==ROUNDS_LOST)
			{	double roundsLost = playerStats.getRoundsLost();
				if(container.hasMean())
				{	if(totalRoundsPlayed>0)
						roundsLost = roundsLost / totalRoundsPlayed;
				}
				list.add(roundsLost);
				list.add(playerId);
			}
			//
			playersScores.put(playerId,list);
		}
	}
}	

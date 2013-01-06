package org.totalboumboum.gui.common.content.subpanel.statistics;

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

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.totalboumboum.game.profile.Portraits;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiFontTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiImageTools;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.detailed.Score;
import org.totalboumboum.statistics.glicko2.jrs.PlayerRating;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.statistics.overall.PlayerStats;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.time.TimeUnit;

/**
 * This class represents one column
 * in the table displaying player
 * statistics.
 * 
 * @author Vincent Labatut
 */
public enum StatisticColumn
{	
	/////////////////////////////////////////////////////////////////
	// GENERAL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Button to register/unregister player */
	GENERAL_BUTTON,
	/** Overall ranking */
	GENERAL_RANK,
	/** Evolution since last ranking */
	GENERAL_EVOLUTION,
	/** Player portrait */
	GENERAL_PORTRAIT,
	/** Player type */
	GENERAL_TYPE,
	/** Player Name */
	GENERAL_NAME,
	
	/////////////////////////////////////////////////////////////////
	// GLICKO-2			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Current Glicko-2 mean */
	GLICKO_MEAN,
	/** Current Glicko-2 deviation */
	GLICKO_DEVIATION,
	/** Current Glicko-2 volatility */
	GLICKO_VOLATILITY,
	/** Number of rounds played since last Glicko-2 update */
	GLICKO_ROUNDCOUNT,
	
	/////////////////////////////////////////////////////////////////
	// SCORES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Number of bombs dropped */
	SCORE_BOMBS,
	/** Number of kills */
	SCORE_BOMBINGS,
	/** Number of times killed */
	SCORE_BOMBEDS,
	/** Number of items picked up */
	SCORE_ITEMS,
	/** Number of crowns picked up */
	SCORE_CROWNS,
	/** Number of tiles painted */
	SCORE_PAINTINGS,
	/** Number of self-kills */
	SCORE_SELF_BOMBINGS,
	/** Amount of time played */
	SCORE_TIME,
	
	/////////////////////////////////////////////////////////////////
	// CONFRONTATIONS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Number of rounds played */
	ROUNDS_PLAYED,
	/** Number of rounds won */
	ROUNDS_WON,
	/** Number of rounds drawn */
	ROUNDS_DRAWN,
	/** Number of rounds lost */
	ROUNDS_LOST;

	/////////////////////////////////////////////////////////////////
	// SORT			/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Indicates if this column sort
	 * should be inverted to get
	 * a natural result.
	 * 
	 * @return 
	 * 		{@code true} if this column should be inverted.
	 */
	public boolean isInverted()
	{	boolean result = false;			
		// general
		if(this==GENERAL_BUTTON || this==GENERAL_RANK)
			result = false;
		else if(this==GENERAL_EVOLUTION)
			result = false;
		else if(this==GENERAL_PORTRAIT)
			result = false;
		else if(this==GENERAL_TYPE)
			result = false;
		else if(this==GENERAL_NAME)
			result = false;
		// glicko-2
		else if(this==GLICKO_MEAN)
			result = true;
		else if(this==GLICKO_DEVIATION)
			result = true;
		else if(this==GLICKO_VOLATILITY)
			result = true;
		else if(this==GLICKO_ROUNDCOUNT)
			result = true;
		// scores
		else if(this==SCORE_BOMBS)
			result = true;
		else if(this==SCORE_BOMBINGS)
			result = true;
		else if(this==SCORE_BOMBEDS)
			result = true;
		else if(this==SCORE_ITEMS)
			result = true;
		else if(this==SCORE_CROWNS)
			result = true;
		else if(this==SCORE_PAINTINGS)
			result = true;
		else if(this==SCORE_SELF_BOMBINGS)
			result = true;
		else if(this==SCORE_TIME)
			result = true;
		// confrontations
		else if(this==ROUNDS_PLAYED)
			result = true;
		else if(this==ROUNDS_WON)
			result = true;
		else if(this==ROUNDS_DRAWN)
			result = true;
		else if(this==ROUNDS_LOST)
			result = true;
		//
		return result;
	}
	
	/**
	 * Returns the GUI key for the
	 * header of this column.
	 * 
	 * @return
	 * 		A GUI key.
	 */
	public String getHeaderKey()
	{	String result = null;			
		// general
		if(this==GENERAL_BUTTON)
			result = null;
		else if(this==GENERAL_RANK)
			result = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_HEADER_RANK;
		else if(this==GENERAL_EVOLUTION)
			result = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_HEADER_EVOLUTION;
		else if(this==GENERAL_PORTRAIT)
			result = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_HEADER_PORTRAIT;
		else if(this==GENERAL_TYPE)
			result = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_HEADER_TYPE;
		else if(this==GENERAL_NAME)
			result = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_HEADER_NAME;
		// glicko-2
		else if(this==GLICKO_MEAN)
			result = GuiKeys.COMMON_STATISTICS_PLAYER_GLICKO2_HEADER_MEAN;
		else if(this==GLICKO_DEVIATION)
			result = GuiKeys.COMMON_STATISTICS_PLAYER_GLICKO2_HEADER_STANDARD_DEVIATION;
		else if(this==GLICKO_VOLATILITY)
			result = GuiKeys.COMMON_STATISTICS_PLAYER_GLICKO2_HEADER_VOLATILITY;
		else if(this==GLICKO_ROUNDCOUNT)
			result = GuiKeys.COMMON_STATISTICS_PLAYER_GLICKO2_HEADER_ROUND_COUNT;
		// scores
		else if(this==SCORE_BOMBS)
			result = GuiKeys.COMMON_STATISTICS_PLAYER_SCORES_HEADER_BOMBS;
		else if(this==SCORE_BOMBINGS)
			result = GuiKeys.COMMON_STATISTICS_PLAYER_SCORES_HEADER_BOMBINGS;
		else if(this==SCORE_BOMBEDS)
			result = GuiKeys.COMMON_STATISTICS_PLAYER_SCORES_HEADER_BOMBEDS;
		else if(this==SCORE_ITEMS)
			result = GuiKeys.COMMON_STATISTICS_PLAYER_SCORES_HEADER_ITEMS;
		else if(this==SCORE_CROWNS)
			result = GuiKeys.COMMON_STATISTICS_PLAYER_SCORES_HEADER_CROWNS;
		else if(this==SCORE_PAINTINGS)
			result = GuiKeys.COMMON_STATISTICS_PLAYER_SCORES_HEADER_PAINTINGS;
		else if(this==SCORE_SELF_BOMBINGS)
			result = GuiKeys.COMMON_STATISTICS_PLAYER_SCORES_HEADER_SELF_BOMBINGS;
		else if(this==SCORE_TIME)
			result = GuiKeys.COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_TIME_PLAYED;
		// confrontations
		else if(this==ROUNDS_PLAYED)
			result = GuiKeys.COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_PLAYED;
		else if(this==ROUNDS_WON)
			result = GuiKeys.COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_WON;
		else if(this==ROUNDS_DRAWN)
			result = GuiKeys.COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_DRAWN;
		else if(this==ROUNDS_LOST)
			result = GuiKeys.COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_LOST;
		//
		return result;
	}
	
	/**
	 * Appropriately sets the specified label
	 * for this column in the specified table,
	 * given the specified context.
	 * 
	 * @param container
	 * 		Panel containing the table.
	 * @param panel
	 * 		Table containing the column.
	 * @param colWidths
	 * 		Size of the columns.
	 * @param line
	 * 		Row for the considered label.
	 * @param col
	 * 		Position of this column.
	 * @param playerId
	 * 		Id of the considered player.
	 * @param playerRank
	 * 		Rank of the considered player.
	 * @param profile
	 * 		Profile of the considered player.
	 * @param playerRating
	 * 		Glicko-2 rating of the considered player.
	 * @param playerStats
	 * 		Detailed stats of the considered player.
	 */
	public void setLabelContent(PlayerStatisticSubPanel container, TableSubPanel panel, int colWidths[], int line, int col, String playerId, int playerRank, Profile profile, PlayerRating playerRating, PlayerStats playerStats)
	{	// general
		if(this==GENERAL_BUTTON)
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
		else if(this==GENERAL_RANK)
		{	if(playerRating!=null)
			{	String text = Integer.toString(playerRank);
				String tooltip = text;
				panel.setLabelText(line,col,text,tooltip);
				int temp = GuiFontTools.getPixelWidth(panel.getLineFontSize(),text);
				if(temp>colWidths[col])
					colWidths[col] = temp;
			}
			else
			{	String key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_NO_RANK;
				panel.setLabelKey(line,col,key,false);
			}
		}
		else if(this==GENERAL_EVOLUTION)
		{	int previousRank = playerStats.getPreviousRank();
			String key = null;
			String tooltip = "";
			if(playerRating!=null)
			{	if(previousRank==-1)
				{	key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_ENTER;
					tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);				
				}
				else if(previousRank<playerRank)
				{	key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_DOWN;
					tooltip = Integer.toString(previousRank-playerRank);
				}
				else if(previousRank>playerRank)
				{	key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_UP;
					tooltip = "+"+Integer.toString(previousRank-playerRank);				
				}
				else
				{	key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_SAME;
					tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);;				
				}
			}
			else if(previousRank!=-1)
			{	key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_EXIT;
				tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);			
			}
			if(key!=null)
			{	BufferedImage icon = GuiImageTools.getIcon(key);
				panel.setLabelIcon(line,col,icon,tooltip);	
			}
/*
 * TODO 
 * éventuellement regénérer les stats pour compléter le nouveau champ
 * 			
 */
		}
		else if(this==GENERAL_PORTRAIT)
		{	BufferedImage image = profile.getPortraits().getOutgamePortrait(Portraits.OUTGAME_HEAD);
			String tooltip = profile.getSpriteName();
			panel.setLabelIcon(line,col,image,tooltip);
		}		
		else if(this==GENERAL_TYPE)
		{	String aiName = profile.getAiName();
			String key;
			if(profile.isRemote())
				key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_REMOTE;
			else if(aiName==null)
				key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_HUMAN;
			else
				key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_COMPUTER;
			panel.setLabelKey(line,col,key,true);
		}
		else if(this==GENERAL_NAME)
		{	String text = profile.getName();
			String tooltip = profile.getName();
			panel.setLabelText(line,col,text,tooltip);
		}
		// glicko-2
		else if(this==GLICKO_MEAN)
		{	if(playerRating!=null)
			{	double mean = playerRating.getRating();
				NumberFormat nfText = NumberFormat.getInstance();
				nfText.setMaximumFractionDigits(0);
				String text = nfText.format(mean);
				NumberFormat nfTooltip = NumberFormat.getInstance();
				nfTooltip.setMaximumFractionDigits(6);
				String tooltip = nfTooltip.format(mean);
				panel.setLabelText(line,col,text,tooltip);
				int temp = GuiFontTools.getPixelWidth(panel.getLineFontSize(),text);
				if(temp>colWidths[col])
					colWidths[col] = temp;
			}
		}
		else if(this==GLICKO_DEVIATION)
		{	if(playerRating!=null)
			{	double stdev = playerRating.getRatingDeviation();
				NumberFormat nfText = NumberFormat.getInstance();
				nfText.setMaximumFractionDigits(0);
				String text = nfText.format(stdev);
				NumberFormat nfTooltip = NumberFormat.getInstance();
				nfTooltip.setMaximumFractionDigits(6);
				String tooltip = nfTooltip.format(stdev);
				panel.setLabelText(line,col,text,tooltip);
				int temp = GuiFontTools.getPixelWidth(panel.getLineFontSize(),text);
				if(temp>colWidths[col])
					colWidths[col] = temp;
			}
		}
		else if(this==GLICKO_VOLATILITY)
		{	if(playerRating!=null)
			{	double variability = playerRating.getRatingVolatility();
				NumberFormat nfText = NumberFormat.getInstance();
				nfText.setMaximumFractionDigits(2);
				nfText.setMinimumFractionDigits(2);
				String text = nfText.format(variability);
				NumberFormat nfTooltip = NumberFormat.getInstance();
				nfTooltip.setMaximumFractionDigits(6);
				String tooltip = nfTooltip.format(variability);
				panel.setLabelText(line,col,text,tooltip);
				int temp = GuiFontTools.getPixelWidth(panel.getLineFontSize(),text);
				if(temp>colWidths[col])
					colWidths[col] = temp;
			}
		}
		else if(this==GLICKO_ROUNDCOUNT)
		{	if(playerRating!=null)
			{	int roundcount = playerRating.getRoundcount();
				String text = Integer.toString(roundcount);
				String tooltip = text;
				panel.setLabelText(line,col,text,tooltip);
				int temp = GuiFontTools.getPixelWidth(panel.getLineFontSize(),text);
				if(temp>colWidths[col])
					colWidths[col] = temp;
			}
			col++;
		}
		// scores
		else if(this==SCORE_BOMBS)
		{	setScoreLabel(container,panel,colWidths,line,col,playerId,playerRank,profile,playerRating,playerStats,Score.BOMBS);
		}			
		else if(this==SCORE_BOMBINGS)
		{	setScoreLabel(container,panel,colWidths,line,col,playerId,playerRank,profile,playerRating,playerStats,Score.BOMBINGS);
		}			
		else if(this==SCORE_SELF_BOMBINGS)
		{	setScoreLabel(container,panel,colWidths,line,col,playerId,playerRank,profile,playerRating,playerStats,Score.SELF_BOMBINGS);
		}			
		else if(this==SCORE_BOMBEDS)
		{	setScoreLabel(container,panel,colWidths,line,col,playerId,playerRank,profile,playerRating,playerStats,Score.BOMBEDS);
		}			
		else if(this==SCORE_ITEMS)
		{	setScoreLabel(container,panel,colWidths,line,col,playerId,playerRank,profile,playerRating,playerStats,Score.ITEMS);
		}			
		else if(this==SCORE_CROWNS)
		{	setScoreLabel(container,panel,colWidths,line,col,playerId,playerRank,profile,playerRating,playerStats,Score.CROWNS);
		}			
		else if(this==SCORE_PAINTINGS)
		{	setScoreLabel(container,panel,colWidths,line,col,playerId,playerRank,profile,playerRating,playerStats,Score.PAINTINGS);
		}			
		else if(this==SCORE_TIME)
		{	long timePlayed = playerStats.getScore(Score.TIME);
			String text = TimeTools.formatTime(timePlayed,TimeUnit.HOUR,TimeUnit.MINUTE,false);
			String tooltip = TimeTools.formatTime(timePlayed,TimeUnit.HOUR,TimeUnit.MILLISECOND,true);
			if(container.hasMean())
			{	long value = 0;
				long roundsPlayed = playerStats.getRoundsPlayed();
				if(roundsPlayed>0)
					value = timePlayed / roundsPlayed;
				text = TimeTools.formatTime(value,TimeUnit.MINUTE,TimeUnit.SECOND,false);
				tooltip = TimeTools.formatTime(value,TimeUnit.HOUR,TimeUnit.MILLISECOND,true);							
			}
			panel.setLabelText(line,col,text,tooltip);
			int temp = GuiFontTools.getPixelWidth(panel.getLineFontSize(),text);
			if(temp>colWidths[col])
				colWidths[col] = temp;
			col++;
		}
		// confrontations
		else if(this==ROUNDS_PLAYED)
		{	long roundsPlayed = playerStats.getRoundsPlayed();
			setConfrontationsLabel(container,panel,colWidths,line,col,playerId,playerRank,profile,playerRating,playerStats,roundsPlayed);
		}
		else if(this==ROUNDS_WON)
		{	long roundsWon = playerStats.getRoundsWon();
			setConfrontationsLabel(container,panel,colWidths,line,col,playerId,playerRank,profile,playerRating,playerStats,roundsWon);
		}
		else if(this==ROUNDS_DRAWN)
		{	long roundsDrawn = playerStats.getRoundsDrawn();
			setConfrontationsLabel(container,panel,colWidths,line,col,playerId,playerRank,profile,playerRating,playerStats,roundsDrawn);
		}
		else if(this==ROUNDS_LOST)
		{	long roundsLost = playerStats.getRoundsLost();
			setConfrontationsLabel(container,panel,colWidths,line,col,playerId,playerRank,profile,playerRating,playerStats,roundsLost);
		}
	}

	/**
	 * Sets the content of a label containing
	 * a score.
	 * 
	 * @param container
	 * 		Panel containing the table.
	 * @param panel
	 * 		Table containing the column.
	 * @param colWidths
	 * 		Size of the columns.
	 * @param line
	 * 		Row for the considered label.
	 * @param col
	 * 		Position of this column.
	 * @param playerId
	 * 		Id of the considered player.
	 * @param playerRank
	 * 		Rank of the considered player.
	 * @param profile
	 * 		Profile of the considered player.
	 * @param playerRating
	 * 		Glicko-2 rating of the considered player.
	 * @param playerStats
	 * 		Detailed stats of the considered player.
	 * @param score
	 * 		Score of the considered player.
	 */
	private void setScoreLabel(PlayerStatisticSubPanel container, TableSubPanel panel, int colWidths[], int line, int col, String playerId, int playerRank, Profile profile, PlayerRating playerRating, PlayerStats playerStats, Score score)
	{	long scoreValue = playerStats.getScore(score);
		String text;
		String tooltip;
		if(container.hasMean())
		{	NumberFormat nfText = NumberFormat.getInstance();
			nfText.setMaximumFractionDigits(2);
			nfText.setMinimumFractionDigits(2);
			NumberFormat nfTooltip = NumberFormat.getInstance();
			nfTooltip.setMaximumFractionDigits(6);
			double value = 0;
			long roundsPlayed = playerStats.getRoundsPlayed();
			if(roundsPlayed>0)
				value = scoreValue / (double)roundsPlayed;
			text = nfText.format(value);
			tooltip = nfTooltip.format(value);
		}
		else
		{	text = Long.toString(scoreValue);
			tooltip = text;
		}
		panel.setLabelText(line,col,text,tooltip);
		int temp = GuiFontTools.getPixelWidth(panel.getLineFontSize(),text);
		if(temp>colWidths[col])
			colWidths[col] = temp;
	}
	
	/**
	 * Sets the content of a label containing
	 * a confrontation stat.
	 * 
	 * @param container
	 * 		Panel containing the table.
	 * @param panel
	 * 		Table containing the column.
	 * @param colWidths
	 * 		Size of the columns.
	 * @param line
	 * 		Row for the considered label.
	 * @param col
	 * 		Position of this column.
	 * @param playerId
	 * 		Id of the considered player.
	 * @param playerRank
	 * 		Rank of the considered player.
	 * @param profile
	 * 		Profile of the considered player.
	 * @param playerRating
	 * 		Glicko-2 rating of the considered player.
	 * @param playerStats
	 * 		Detailed stats of the considered player.
	 * @param confrontations
	 * 		Confrontations of the considered player.
	 */
	private void setConfrontationsLabel(PlayerStatisticSubPanel container, TableSubPanel panel, int colWidths[], int line, int col, String playerId, int playerRank, Profile profile, PlayerRating playerRating, PlayerStats playerStats, long confrontations)
	{	String text = Long.toString(confrontations);
		String tooltip = text;
		if(container.hasMean())
		{	double value = 0;
			long roundsPlayed = playerStats.getRoundsPlayed();
			if(roundsPlayed>0)
				value = 100 * confrontations / (double)roundsPlayed;
			NumberFormat nfText = NumberFormat.getInstance();
			nfText.setMaximumFractionDigits(0);
			text = nfText.format(value)+"%";
			NumberFormat nfTooltip = NumberFormat.getInstance();
			nfTooltip.setMaximumFractionDigits(4);
			tooltip = nfTooltip.format(value)+"%";
		}
		panel.setLabelText(line,col,text,tooltip);
		int temp = GuiFontTools.getPixelWidth(panel.getLineFontSize(),text);
		if(temp>colWidths[col])
			colWidths[col] = temp;
	}
	
	/**
	 * Sort the players depending on this column.
	 * 
	 * @param container
	 * 		Panel containing the table.
	 * @param playersScores
	 * 		Detailed stats of the players.
	 * @param rankingService
	 * 		Glicko-2 rankings.
	 * @param profilesMap
	 * 		Map of all registered player profiles.
	 */
	public void updateValues(PlayerStatisticSubPanel container, Map<String,List<Comparable<?>>> playersScores, RankingService rankingService, Map<String,Profile> profilesMap)
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
			List<Comparable<?>> list = new ArrayList<Comparable<?>>();
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
			{	Integer type;
				if(profile.isRemote())
					type = 0;
				else if(profile.hasAi())
					type = 1;
				else
					type = 2;
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

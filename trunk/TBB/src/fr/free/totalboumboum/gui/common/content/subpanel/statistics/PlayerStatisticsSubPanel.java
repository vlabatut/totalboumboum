package fr.free.totalboumboum.gui.common.content.subpanel.statistics;

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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JLabel;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.profile.Portraits;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.configuration.profile.ProfileLoader;
import fr.free.totalboumboum.game.GameData;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.statistics.GameStatistics;
import fr.free.totalboumboum.game.statistics.glicko2.jrs.PlayerRating;
import fr.free.totalboumboum.game.statistics.glicko2.jrs.RankingService;
import fr.free.totalboumboum.game.statistics.raw.Score;
import fr.free.totalboumboum.gui.common.structure.subpanel.outside.SubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.outside.TableSubPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiStringTools;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.StringTools;

public class PlayerStatisticsSubPanel extends TableSubPanel implements MouseListener
{	private static final long serialVersionUID = 1L;
	private static final int LINES = 16+1;
	private static final int COLS = 2;
	
	public PlayerStatisticsSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,LINES,1,COLS,true);
		
		setPlayerIds(null,null,LINES-1);
	}
		
	/////////////////////////////////////////////////////////////////
	// PLAYER IDS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<Integer> playerIds;
	private int lines;
	
	public ArrayList<Integer> getPlayerIds()
	{	return playerIds;	
	}
	
	public void setPlayerIds(ArrayList<Integer> playerIds, ArrayList<Integer> playerRanks, int lines)
	{	if(playerIds==null)
			playerIds = new ArrayList<Integer>();
		this.playerIds = playerIds;

		// sizes
		int cols = COLS;
		if(showPortrait) 
			cols++;
		if(showType) 
			cols++;
		if(showMean) 
			cols = cols+4;
		if(showStdev) 
			cols++;
		if(showVolatility) 
			cols++;
		if(showRoundcount) 
			cols++;
		if(showScores) 
			cols++;
		if(showRoundsPlayed) 
			cols++;
		if(showRoundsWon) 
			cols++;
		if(showRoundsDrawn) 
			cols++;
		if(showRoundsLost) 
			cols++;
		if(showTimePlayed) 
			cols++;
		reinit(lines,cols);
		
		// col widths
		int headerHeight = getHeaderHeight();
		int rankWidth = headerHeight;
		int portraitWidth = headerHeight;
		int typeWidth = headerHeight;
		int meanWidth = headerHeight;
		int stdevWidth = headerHeight;
		int volatilityWidth = headerHeight;
		int roundcountWidth = headerHeight;
		int scoresWidth[] = {headerHeight,headerHeight,headerHeight,headerHeight};
		int roundsPlayedWidth = headerHeight;
		int roundsWonWidth = headerHeight;
		int roundsLostWidth = headerHeight;
		int roundsDrawnWidth = headerHeight;
		int timePlayedWidth = headerHeight;

		// headers
		{	int col = 0;
			// rank
			{	String key = GuiKeys.MENU_STATISTICS_PLAYER_COMMON_HEADER_RANK;
				setLabelKey(0,col,key,true);
				col++;
			}
			// portrait
			if(showPortrait) 
			{	String key = GuiKeys.MENU_STATISTICS_PLAYER_COMMON_HEADER_PORTRAIT;
				setLabelKey(0,col,key,true);
				col++;
			}
			// type
			if(showType) 
			{	String key = GuiKeys.MENU_STATISTICS_PLAYER_COMMON_HEADER_TYPE;
				setLabelKey(0,col,key,true);
				col++;
			}
			// name
			{	String key = GuiKeys.MENU_STATISTICS_PLAYER_COMMON_HEADER_NAME;
				setLabelKey(0,col,key,true);
				col++;
			}
			if(showMean) 
			{	String key = GuiKeys.MENU_STATISTICS_PLAYER_GLICKO2_HEADER_MEAN;
				setLabelKey(0,col,key,true);
				col++;
			}
			if(showStdev) 
			{	String key = GuiKeys.MENU_STATISTICS_PLAYER_GLICKO2_HEADER_STANDARD_DEVIATION;
				setLabelKey(0,col,key,true);
				col++;
			}
			if(showVolatility) 
			{	String key = GuiKeys.MENU_STATISTICS_PLAYER_GLICKO2_HEADER_VOLATILITY;
				setLabelKey(0,col,key,true);
				col++;
			}
			if(showRoundcount) 
			{	String key = GuiKeys.MENU_STATISTICS_PLAYER_GLICKO2_HEADER_ROUND_COUNT;
				setLabelKey(0,col,key,true);
				col++;
			}
			if(showScores) 
			{	String headerPrefix = GuiKeys.MENU_STATISTICS_PLAYER_SCORES_HEADER;
				String keys[] = 
				{	headerPrefix+GuiKeys.BOMBS,
					headerPrefix+GuiKeys.ITEMS,
					headerPrefix+GuiKeys.BOMBEDS,
					headerPrefix+GuiKeys.BOMBINGS
				};
				for(int c=0;c<keys.length;c++)
					setLabelKey(0,col+c,keys[c],true);
				col = col+keys.length;
			}
			if(showRoundsPlayed) 
			{	String key = GuiKeys.MENU_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_PLAYED;
				setLabelKey(0,col,key,true);
				col++;
			}
			if(showRoundsWon) 
			{	String key = GuiKeys.MENU_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_WON;
				setLabelKey(0,col,key,true);
				col++;
			}
			if(showRoundsDrawn) 
			{	String key = GuiKeys.MENU_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_DRAWN;
				setLabelKey(0,col,key,true);
				col++;
			}
			if(showRoundsLost) 
			{	String key = GuiKeys.MENU_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_LOST;
				setLabelKey(0,col,key,true);
				col++;
			}
			if(showTimePlayed) 
			{	String key = GuiKeys.MENU_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_TIME_PLAYED;
				setLabelKey(0,col,key,true);
				col++;
			}
		}
		
		// data
		{	// init
			RankingService rankingService = GameStatistics.getRankingService();
			NumberFormat nfText = NumberFormat.getInstance();
			nfText.setMaximumFractionDigits(2);
			nfText.setMinimumFractionDigits(2);
			NumberFormat nfTooltip = NumberFormat.getInstance();
			nfTooltip.setMaximumFractionDigits(6);
			// process each playerId
			int line = 1;
			while(line<=lines && line<=playerIds.size())
			{	// init
				int col = 0;
				int playerId = playerIds.get(line-1);
				int playerRank = playerRanks.get(line-1);
				Profile profile = ProfileLoader.loadProfile(playerId);
				PlayerRating playerRating = rankingService.getPlayerRating(playerId);
				// color
				Color clr = profile.getSpriteColor().getColor();
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				setLineBackground(line,bg);
				// rank
				{	String text = Integer.toString(playerRank);
					String tooltip = text;
					setLabelText(line,col,text,tooltip);
					int temp = GuiTools.getPixelWidth(getLineFontSize(),text);
					if(temp>rankWidth)
						rankWidth = temp;
					col++;
				}
				// portrait
				if(showPortrait)
				{	BufferedImage image = profile.getPortraits().getOutgamePortrait(Portraits.OUTGAME_HEAD);
					String tooltip = profile.getSpriteName();
					setLabelIcon(line,col,image,tooltip);
					col++;
				}
				// type
				if(showType)
				{	String aiName = profile.getAiName();
					String key;
					if(aiName==null)
						key = GuiKeys.MENU_STATISTICS_PLAYER_COMMON_DATA_HUMAN;
					else
						key = GuiKeys.MENU_STATISTICS_PLAYER_COMMON_DATA_COMPUTER;
					setLabelKey(line,col,key,true);
					col++;
				}
				// name
				{	String text = profile.getName();
					String tooltip = profile.getName();
					setLabelText(line,col,text,tooltip);
					col++;
				}
				// mean
				if(showMean)
				{	double mean = playerRating.getRating();
					String text = nfText.format(mean);
					String tooltip = nfTooltip.format(mean);
					setLabelText(line,col,text,tooltip);
					int temp = GuiTools.getPixelWidth(getLineFontSize(),text);
					if(temp>meanWidth)
						meanWidth = temp;
					col++;
				}
				// standard-deviation
				if(showStdev)
				{	double stdev = playerRating.getRatingDeviation();
					String text = nfText.format(stdev);
					String tooltip = nfTooltip.format(stdev);
					setLabelText(line,col,text,tooltip);
					int temp = GuiTools.getPixelWidth(getLineFontSize(),text);
					if(temp>stdevWidth)
						stdevWidth = temp;
					col++;
				}
				// variability
				if(showVolatility)
				{	double variability = playerRating.getRatingVolatility();
					String text = nfText.format(variability);
					String tooltip = nfTooltip.format(variability);
					setLabelText(line,col,text,tooltip);
					int temp = GuiTools.getPixelWidth(getLineFontSize(),text);
					if(temp>volatilityWidth)
						volatilityWidth = temp;
					col++;
				}
				// scores
				if(showScores)
				{	String[] scores = 
					{	nfText.format(stats.getScores(Score.BOMBS)[profileIndex]),
						nfText.format(stats.getScores(Score.ITEMS)[profileIndex]),
						nfText.format(stats.getScores(Score.BOMBEDS)[profileIndex]),
						nfText.format(stats.getScores(Score.BOMBINGS)[profileIndex]),
					};
					for(int j=0;j<scores.length;j++)
					{	String text = scores[j];
						String tooltip = scores[j];
						setLabelText(line,col,text,tooltip);
						int temp = GuiTools.getPixelWidth(getLineFontSize(),text);
						if(temp>scoresWidth[j])
							scoresWidth[j] = temp;
						col++;
					}
				}			
				// rounds played
				if(showRoundsPlayed)
				{	int roundsPlayed = ;
					String text = Integer.toString(roundsPlayed);
					String tooltip = text;
					setLabelText(line,col,text,tooltip);
					int temp = GuiTools.getPixelWidth(getLineFontSize(),text);
					if(temp>roundsPlayedWidth)
						roundsPlayedWidth = temp;
					col++;
				}
				// rounds won
				if(showRoundsWon)
				{	int roundsWon = ;
					String text = Integer.toString(roundsWon);
					String tooltip = text;
					setLabelText(line,col,text,tooltip);
					int temp = GuiTools.getPixelWidth(getLineFontSize(),text);
					if(temp>roundsWonWidth)
						roundsWonWidth = temp;
					col++;
				}
				// rounds drawn
				if(showRoundsDrawn)
				{	int roundsDrawn = ;
					String text = Integer.toString(roundsDrawn);
					String tooltip = text;
					setLabelText(line,col,text,tooltip);
					int temp = GuiTools.getPixelWidth(getLineFontSize(),text);
					if(temp>roundsDrawnWidth)
						roundsDrawnWidth = temp;
					col++;
				}
				// rounds lost
				if(showRoundsLost)
				{	int roundsLost = ;
					String text = Integer.toString(roundsLost);
					String tooltip = text;
					setLabelText(line,col,text,tooltip);
					int temp = GuiTools.getPixelWidth(getLineFontSize(),text);
					if(temp>roundsLostWidth)
						roundsLostWidth = temp;
					col++;
				}
				// time played
				if(showTimePlayed)
				{	long timePlayed = ;
					String text = StringTools.formatTimeWithSeconds(timePlayed);
					String tooltip = text;
					setLabelText(line,col,text,tooltip);
					int temp = GuiTools.getPixelWidth(getLineFontSize(),text);
					if(temp>timePlayedWidth)
						timePlayedWidth = temp;
					col++;
				}
				//
				line++;
			}
		}
		
		// NOTE rang, portrait, type (IA/Humain), nom, moyenne, e-t, volatilité, scores(bombes, items, kills, deaths), rounds, victoires, nul, défaites, temps
		// NOTE rang et nom obligatoires
		
		
		
		
		int rankWidth = getRanksWidth();
		rankWidth = Math.max(iconWidth,rankWidth);
		int nameWidth = getDataWidth() - (cols-1)*GuiTools.subPanelMargin - 2*iconWidth - rankWidth;
		if(showControls)
			nameWidth = nameWidth - controlWidth;
		
		
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean showPortrait = true;
	private boolean showType = true;
	private boolean showMean = true;
	private boolean showStdev = true;
	private boolean showVolatility = true;
	private boolean showRoundcount = true;
	private boolean showScores = true;
	private boolean showRoundsPlayed = true;
	private boolean showRoundsWon = true;
	private boolean showRoundsLost = true;
	private boolean showRoundsDrawn = true;
	private boolean showTimePlayed = true;
	
	public void setShowPortrait(boolean showPortrait)
	{	this.showPortrait = showPortrait;
		setPlayerIds(playerIds,lines-1);
	}

	public void setShowType(boolean showType)
	{	this.showType = showType;
		setPlayerIds(playerIds,lines-1);
	}

	public void setShowMean(boolean showMean)
	{	this.showMean = showMean;
		setPlayerIds(playerIds,lines-1);
	}

	public void setShowStdev(boolean showStdev)
	{	this.showStdev = showStdev;
		setPlayerIds(playerIds,lines-1);
	}

	public void setShowVolatility(boolean showVolatility)
	{	this.showVolatility = showVolatility;
		setPlayerIds(playerIds,lines-1);
	}

	public void setShowRoundcount(boolean showRoundcount)
	{	this.showRoundcount = showRoundcount;
		setPlayerIds(playerIds,lines-1);
	}

	public void setShowScores(boolean showScores)
	{	this.showScores = showScores;
		setPlayerIds(playerIds,lines-1);
	}

	public void setShowRoundsPlayed(boolean showRoundsPlayed)
	{	this.showRoundsPlayed = showRoundsPlayed;
		setPlayerIds(playerIds,lines-1);
	}

	public void setShowRoundsWon(boolean showRoundsWon)
	{	this.showRoundsWon = showRoundsWon;
		setPlayerIds(playerIds,lines-1);
	}

	public void setShowRoundsLost(boolean showRoundsLost)
	{	this.showRoundsLost = showRoundsLost;
		setPlayerIds(playerIds,lines-1);
	}

	public void setShowRoundsDrawn(boolean showRoundsDrawn)
	{	this.showRoundsDrawn = showRoundsDrawn;
		setPlayerIds(playerIds,lines-1);
	}

	public void setShowTimePlayed(boolean showTimePlayed)
	{	this.showTimePlayed = showTimePlayed;
		setPlayerIds(playerIds,lines-1);
	}

	/////////////////////////////////////////////////////////////////
	// MOUSE LISTENER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	@Override
	public void mouseClicked(MouseEvent e)
	{	
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{	
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{	
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{	JLabel label = (JLabel)e.getComponent();
		int[] pos = getLabelPositionSimple(label);
		// controls
		if(pos[1]==2)
		{	Profile profile = players.get(pos[0]-1);
			int index = profile.getControlSettingsIndex();
			if(profile.hasAi())
			{	if(index==GameData.CONTROL_COUNT)
					index = 0;
				else
					index = Configuration.getProfilesConfiguration().getNextFreeControls(players,index);
			}
			else
				index = Configuration.getProfilesConfiguration().getNextFreeControls(players,index);
			profile.setControlSettingsIndex(index);
			setLabelText(pos[0],pos[1],controlTexts.get(index),controlTooltips.get(index));
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}

	// NOTE rang, portrait, type (IA/Humain), nom, moyenne, e-t, volatilité, scores
	public enum RankCriterion
	{	MEAN,
		ROUNDCOUNT,
		BOMBS,
		BOMBINGS,
		BOMBEDS,
		ITEMS,
		CROWNS,
		PAINTINGS,
		TIME_PLAYED,
		ROUNDS_PLAYED,
		ROUNDS_WON,
		ROUNDS_DRAWN,
		ROUNDS_LOST,		
	}
}

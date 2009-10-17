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
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.JLabel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.Portraits;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.configuration.profile.ProfileLoader;
import fr.free.totalboumboum.statistics.GameStatistics;
import fr.free.totalboumboum.statistics.general.PlayerStats;
import fr.free.totalboumboum.statistics.glicko2.jrs.PlayerRating;
import fr.free.totalboumboum.statistics.glicko2.jrs.RankingService;
import fr.free.totalboumboum.statistics.detailed.Score;
import fr.free.totalboumboum.gui.common.structure.subpanel.outside.SubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.outside.TableSubPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.StringTools;

public class PlayerStatisticsSubPanel extends TableSubPanel implements MouseListener
{	private static final long serialVersionUID = 1L;
	private static final int LINES = 16+1;
	private static final int COLS = 3;
	
	public PlayerStatisticsSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,LINES,1,COLS,true);
		
		try
		{	setPlayerIds(null,LINES);
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
	}
		
	/////////////////////////////////////////////////////////////////
	// PLAYER IDS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<Integer> playerIds;
	private int lines;
	
	public List<Integer> getPlayerIds()
	{	return playerIds;	
	}
	
	public void setPlayerIds(List<Integer> playerIds, int lines) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	if(playerIds==null)
			playerIds = new ArrayList<Integer>();		
		this.playerIds = playerIds;
		this.lines = lines;
		
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
		reinit(lines+1,cols);
		
		// col widths
		int headerHeight = getHeaderHeight();
		int buttonWidth = headerHeight;
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
			// button
			{	col++;
			}
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
			// mean
			if(showMean) 
			{	String key = GuiKeys.MENU_STATISTICS_PLAYER_GLICKO2_HEADER_MEAN;
				setLabelKey(0,col,key,true);
				col++;
			}
			// standard-deviation
			if(showStdev) 
			{	String key = GuiKeys.MENU_STATISTICS_PLAYER_GLICKO2_HEADER_STANDARD_DEVIATION;
				setLabelKey(0,col,key,true);
				col++;
			}
			// volatility
			if(showVolatility) 
			{	String key = GuiKeys.MENU_STATISTICS_PLAYER_GLICKO2_HEADER_VOLATILITY;
				setLabelKey(0,col,key,true);
				col++;
			}
			// roundcount
			if(showRoundcount) 
			{	String key = GuiKeys.MENU_STATISTICS_PLAYER_GLICKO2_HEADER_ROUND_COUNT;
				setLabelKey(0,col,key,true);
				col++;
			}
			//scores
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
			// rounds played
			if(showRoundsPlayed) 
			{	String key = GuiKeys.MENU_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_PLAYED;
				setLabelKey(0,col,key,true);
				col++;
			}
			// rounds won
			if(showRoundsWon) 
			{	String key = GuiKeys.MENU_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_WON;
				setLabelKey(0,col,key,true);
				col++;
			}
			// rounds drawn
			if(showRoundsDrawn) 
			{	String key = GuiKeys.MENU_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_DRAWN;
				setLabelKey(0,col,key,true);
				col++;
			}
			// rounds lost
			if(showRoundsLost) 
			{	String key = GuiKeys.MENU_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_LOST;
				setLabelKey(0,col,key,true);
				col++;
			}
			// time played
			if(showTimePlayed) 
			{	String key = GuiKeys.MENU_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_TIME_PLAYED;
				setLabelKey(0,col,key,true);
				col++;
			}
		}
		
		// data
		{	// init
			RankingService rankingService = GameStatistics.getRankingService();
			HashMap<Integer,PlayerStats> playersStats = GameStatistics.getPlayersStats();
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
				Profile profile = ProfileLoader.loadProfile(playerId);
				PlayerRating playerRating = rankingService.getPlayerRating(playerId);
				PlayerStats playerStats = playersStats.get(playerId);
				// color
				Color clr = profile.getSpriteColor().getColor();
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				setLineBackground(line,bg);
				// button
				{	String key;
					if(playerRating==null)
						key = GuiKeys.MENU_STATISTICS_PLAYER_COMMON_BUTTON_REGISTER;
					else
						key = GuiKeys.MENU_STATISTICS_PLAYER_COMMON_BUTTON_UNREGISTER;
					setLabelKey(line,col,key,true);
					col++;
					JLabel label = getLabel(line,col);
					label.addMouseListener(this);
					col++;
				}
				// rank
				{	if(playerRating==null)
					{	String text = Integer.toString(rankingService.getPlayerRank(playerId));
						String tooltip = text;
						setLabelText(line,col,text,tooltip);
						int temp = GuiTools.getPixelWidth(getLineFontSize(),text);
						if(temp>rankWidth)
							rankWidth = temp;
					}
					else
					{	String key = GuiKeys.MENU_STATISTICS_PLAYER_COMMON_DATA_NO_RANK;
						setLabelKey(line,col,key,false);
					}
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
				// roundcount
				if(showRoundcount)
				{	int roundcount = playerRating.getRoundcount();
					String text = Integer.toString(roundcount);
					String tooltip = text;
					setLabelText(line,col,text,tooltip);
					int temp = GuiTools.getPixelWidth(getLineFontSize(),text);
					if(temp>roundcountWidth)
						volatilityWidth = temp;
					col++;
				}
				// scores
				if(showScores)
				{	String[] scores = 
					{	nfText.format(playerStats.getScore(Score.BOMBS)),
						nfText.format(playerStats.getScore(Score.ITEMS)),
						nfText.format(playerStats.getScore(Score.BOMBEDS)),
						nfText.format(playerStats.getScore(Score.BOMBINGS)),
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
				{	long roundsPlayed = playerStats.getRoundsPlayed();
					String text = Long.toString(roundsPlayed);
					String tooltip = text;
					setLabelText(line,col,text,tooltip);
					int temp = GuiTools.getPixelWidth(getLineFontSize(),text);
					if(temp>roundsPlayedWidth)
						roundsPlayedWidth = temp;
					col++;
				}
				// rounds won
				if(showRoundsWon)
				{	long roundsWon = playerStats.getRoundsWon();
					String text = Long.toString(roundsWon);
					String tooltip = text;
					setLabelText(line,col,text,tooltip);
					int temp = GuiTools.getPixelWidth(getLineFontSize(),text);
					if(temp>roundsWonWidth)
						roundsWonWidth = temp;
					col++;
				}
				// rounds drawn
				if(showRoundsDrawn)
				{	long roundsDrawn = playerStats.getRoundsDrawn();
					String text = Long.toString(roundsDrawn);
					String tooltip = text;
					setLabelText(line,col,text,tooltip);
					int temp = GuiTools.getPixelWidth(getLineFontSize(),text);
					if(temp>roundsDrawnWidth)
						roundsDrawnWidth = temp;
					col++;
				}
				// rounds lost
				if(showRoundsLost)
				{	long roundsLost = playerStats.getRoundsLost();
					String text = Long.toString(roundsLost);
					String tooltip = text;
					setLabelText(line,col,text,tooltip);
					int temp = GuiTools.getPixelWidth(getLineFontSize(),text);
					if(temp>roundsLostWidth)
						roundsLostWidth = temp;
					col++;
				}
				// time played
				if(showTimePlayed)
				{	long timePlayed = playerStats.getScore(Score.TIME);
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
		
		// col widths
		{	int nameWidth = getDataWidth() - (cols-1)*GuiTools.subPanelMargin;
			int col = 0;
			// buttons
			{	setColSubMinWidth(col,buttonWidth);
				setColSubPrefWidth(col,buttonWidth);
				setColSubMaxWidth(col,buttonWidth);
				nameWidth = nameWidth - buttonWidth;
				col++;
			}
			// rank
			{	setColSubMinWidth(col,rankWidth);
				setColSubPrefWidth(col,rankWidth);
				setColSubMaxWidth(col,rankWidth);
				nameWidth = nameWidth - rankWidth;
				col++;
			}
			// portrait
			if(showPortrait) 
			{	setColSubMinWidth(col,portraitWidth);
				setColSubPrefWidth(col,portraitWidth);
				setColSubMaxWidth(col,portraitWidth);
				nameWidth = nameWidth - portraitWidth;
				col++;
			}
			// type
			if(showType) 
			{	setColSubMinWidth(col,typeWidth);
				setColSubPrefWidth(col,typeWidth);
				setColSubMaxWidth(col,typeWidth);
				nameWidth = nameWidth - typeWidth;
				col++;
			}
			// name
			int colName = col;
			col++;
			// mean
			if(showMean) 
			{	setColSubMinWidth(col,meanWidth);
				setColSubPrefWidth(col,meanWidth);
				setColSubMaxWidth(col,meanWidth);
				nameWidth = nameWidth - meanWidth;
				col++;
			}
			// standard-deviation
			if(showStdev) 
			{	setColSubMinWidth(col,stdevWidth);
				setColSubPrefWidth(col,stdevWidth);
				setColSubMaxWidth(col,stdevWidth);
				nameWidth = nameWidth - stdevWidth;
				col++;
			}
			// volatility
			if(showVolatility) 
			{	setColSubMinWidth(col,volatilityWidth);
				setColSubPrefWidth(col,volatilityWidth);
				setColSubMaxWidth(col,volatilityWidth);
				nameWidth = nameWidth - volatilityWidth;
				col++;
			}
			// roundcount
			if(showRoundcount) 
			{	setColSubMinWidth(col,roundcountWidth);
				setColSubPrefWidth(col,roundcountWidth);
				setColSubMaxWidth(col,roundcountWidth);
				nameWidth = nameWidth - roundcountWidth;
				col++;
			}
			// scores
			if(showScores) 
			{	for(int i=0;i<scoresWidth.length;i++)
				{	setColSubMinWidth(col,scoresWidth[i]);
					setColSubPrefWidth(col,scoresWidth[i]);
					setColSubMaxWidth(col,scoresWidth[i]);
					nameWidth = nameWidth - scoresWidth[i];
					col++;
				}
			}
			// rounds played
			if(showRoundsPlayed) 
			{	setColSubMinWidth(col,roundsPlayedWidth);
				setColSubPrefWidth(col,roundsPlayedWidth);
				setColSubMaxWidth(col,roundsPlayedWidth);
				nameWidth = nameWidth - roundsPlayedWidth;
				col++;
			}
			// rounds won
			if(showRoundsWon) 
			{	setColSubMinWidth(col,roundsWonWidth);
				setColSubPrefWidth(col,roundsWonWidth);
				setColSubMaxWidth(col,roundsWonWidth);
				nameWidth = nameWidth - roundsWonWidth;
				col++;
			}
			// rounds drawn
			if(showRoundsDrawn) 
			{	setColSubMinWidth(col,roundsDrawnWidth);
				setColSubPrefWidth(col,roundsDrawnWidth);
				setColSubMaxWidth(col,roundsDrawnWidth);
				nameWidth = nameWidth - roundsDrawnWidth;
				col++;
			}
			// rounds lost
			if(showRoundsLost) 
			{	setColSubMinWidth(col,roundsLostWidth);
				setColSubPrefWidth(col,roundsLostWidth);
				setColSubMaxWidth(col,roundsLostWidth);
				nameWidth = nameWidth - roundsLostWidth;
				col++;
			}
			// time played 
			if(showTimePlayed) 
			{	setColSubMinWidth(col,timePlayedWidth);
				setColSubPrefWidth(col,timePlayedWidth);
				setColSubMaxWidth(col,timePlayedWidth);
				nameWidth = nameWidth - timePlayedWidth;
				col++;
			}
			// name
			{	setColSubMinWidth(colName,nameWidth);
				setColSubPrefWidth(colName,nameWidth);
				setColSubMaxWidth(colName,nameWidth);
			}
		}		
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
		try
		{	setPlayerIds(playerIds,lines);
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
	}

	public void setShowType(boolean showType)
	{	this.showType = showType;
		try
		{	setPlayerIds(playerIds,lines);
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
	}

	public void setShowMean(boolean showMean)
	{	this.showMean = showMean;
		try
		{	setPlayerIds(playerIds,lines);
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
	}

	public void setShowStdev(boolean showStdev)
	{	this.showStdev = showStdev;
		try
		{	setPlayerIds(playerIds,lines);
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
	}

	public void setShowVolatility(boolean showVolatility)
	{	this.showVolatility = showVolatility;
		try
		{	setPlayerIds(playerIds,lines);
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
	}

	public void setShowRoundcount(boolean showRoundcount)
	{	this.showRoundcount = showRoundcount;
		try
		{	setPlayerIds(playerIds,lines);
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
	}

	public void setShowScores(boolean showScores)
	{	this.showScores = showScores;
		try
		{	setPlayerIds(playerIds,lines);
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
	}

	public void setShowRoundsPlayed(boolean showRoundsPlayed)
	{	this.showRoundsPlayed = showRoundsPlayed;
		try
		{	setPlayerIds(playerIds,lines);
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
	}

	public void setShowRoundsWon(boolean showRoundsWon)
	{	this.showRoundsWon = showRoundsWon;
		try
		{	setPlayerIds(playerIds,lines);
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
	}

	public void setShowRoundsLost(boolean showRoundsLost)
	{	this.showRoundsLost = showRoundsLost;
		try
		{	setPlayerIds(playerIds,lines);
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
	}

	public void setShowRoundsDrawn(boolean showRoundsDrawn)
	{	this.showRoundsDrawn = showRoundsDrawn;
		try
		{	setPlayerIds(playerIds,lines);
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
	}

	public void setShowTimePlayed(boolean showTimePlayed)
	{	this.showTimePlayed = showTimePlayed;
		try
		{	setPlayerIds(playerIds,lines);
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
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
		// add/remove
		if(pos[0]>0 && pos[1]==1)
		{	int playerId = playerIds.get(pos[0]-1);
			RankingService rankingService = GameStatistics.getRankingService();
			Set<Integer> playersIds = rankingService.getPlayers();
			if(playersIds.contains(playerId))
				firePlayerStatisticsPlayerRegistered(playerId);
			else
				firePlayerStatisticsPlayerUnregistered(playerId);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}

	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<PlayerStatisticsSubPanelListener> listeners = new ArrayList<PlayerStatisticsSubPanelListener>();
	
	public void addListener(PlayerStatisticsSubPanelListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);		
	}

	public void removeListener(PlayerStatisticsSubPanelListener listener)
	{	listeners.remove(listener);		
	}
	
	private void firePlayerStatisticsPlayerRegistered(int playerId)
	{	for(PlayerStatisticsSubPanelListener listener: listeners)
			listener.playerStatisticsPlayerRegistered(playerId);
	}

	private void firePlayerStatisticsPlayerUnregistered(int playerId)
	{	for(PlayerStatisticsSubPanelListener listener: listeners)
			listener.playerStatisticsPlayerUnregistered(playerId);
	}
}

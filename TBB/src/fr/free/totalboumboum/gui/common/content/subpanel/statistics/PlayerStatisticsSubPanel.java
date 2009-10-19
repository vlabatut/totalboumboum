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
import fr.free.totalboumboum.gui.common.content.subpanel.statistics.PlayerStatisticBrowserSubPanel.RankCriterion;
import fr.free.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.StringTools;
import fr.free.totalboumboum.tools.StringTools.TimeUnit;

public class PlayerStatisticsSubPanel extends TableSubPanel implements MouseListener
{	private static final long serialVersionUID = 1L;
	private static final int LINES = 16+1;
	private static final int COLS = 3;
	
	public PlayerStatisticsSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.NOTHING,LINES,1,COLS,true);
		setOpaque(false);
		
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
	private RankCriterion[] rankCriterions;
	
	public List<Integer> getPlayerIds()
	{	return playerIds;	
	}
	
	public void setPlayerIds(List<Integer> playerIds, int lines) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	if(playerIds==null)
			playerIds = new ArrayList<Integer>();		
		this.playerIds = playerIds;
		this.lines = lines;
		lines = lines + 1;
		
		// sizes
		int cols = COLS;
		if(showPortrait) 
			cols++;
		if(showType) 
			cols++;
		if(showMean) 
			cols++;
		if(showStdev) 
			cols++;
		if(showVolatility) 
			cols++;
		if(showRoundcount) 
			cols++;
		if(showScores) 
			cols = cols+4;
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
		rankCriterions = new RankCriterion[cols];
		
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
			{	rankCriterions[col] = null;
				col++;
			}
			// rank
			{	String key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_HEADER_RANK;
				setLabelKey(0,col,key,true);
				rankCriterions[col] = RankCriterion.MEAN;
				col++;
			}
			// portrait
			if(showPortrait) 
			{	String key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_HEADER_PORTRAIT;
				setLabelKey(0,col,key,true);
				rankCriterions[col] = null;
				col++;
			}
			// type
			if(showType) 
			{	String key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_HEADER_TYPE;
				setLabelKey(0,col,key,true);
				rankCriterions[col] = null;
				col++;
			}
			// name
			{	String key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_HEADER_NAME;
				setLabelKey(0,col,key,true);
				rankCriterions[col] = null;
				col++;
			}
			// mean
			if(showMean) 
			{	String key = GuiKeys.COMMON_STATISTICS_PLAYER_GLICKO2_HEADER_MEAN;
				setLabelKey(0,col,key,true);
				rankCriterions[col] = RankCriterion.MEAN;
				col++;
			}
			// standard-deviation
			if(showStdev) 
			{	String key = GuiKeys.COMMON_STATISTICS_PLAYER_GLICKO2_HEADER_STANDARD_DEVIATION;
				setLabelKey(0,col,key,true);
				rankCriterions[col] = null;
				col++;
			}
			// volatility
			if(showVolatility) 
			{	String key = GuiKeys.COMMON_STATISTICS_PLAYER_GLICKO2_HEADER_VOLATILITY;
				setLabelKey(0,col,key,true);
				rankCriterions[col] = null;
				col++;
			}
			// roundcount
			if(showRoundcount) 
			{	String key = GuiKeys.COMMON_STATISTICS_PLAYER_GLICKO2_HEADER_ROUND_COUNT;
				setLabelKey(0,col,key,true);
				rankCriterions[col] = RankCriterion.ROUNDCOUNT;
				col++;
			}
			//scores
			if(showScores) 
			{	String headerPrefix = GuiKeys.COMMON_STATISTICS_PLAYER_SCORES_HEADER;
				String keys[] = 
				{	headerPrefix+GuiKeys.BOMBS,
					headerPrefix+GuiKeys.ITEMS,
					headerPrefix+GuiKeys.BOMBEDS,
					headerPrefix+GuiKeys.BOMBINGS
				};
				RankCriterion rc[] = 
				{	RankCriterion.BOMBS,
					RankCriterion.ITEMS,
					RankCriterion.BOMBEDS,
					RankCriterion.BOMBINGS
				};
				for(int c=0;c<keys.length;c++)
				{	setLabelKey(0,col+c,keys[c],true);
					rankCriterions[col+c] = rc[c];				
				}
				col = col+keys.length;
			}
			// rounds played
			if(showRoundsPlayed) 
			{	String key = GuiKeys.COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_PLAYED;
				setLabelKey(0,col,key,true);
				rankCriterions[col] = RankCriterion.ROUNDS_PLAYED;
				col++;
			}
			// rounds won
			if(showRoundsWon) 
			{	String key = GuiKeys.COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_WON;
				setLabelKey(0,col,key,true);
				rankCriterions[col] = RankCriterion.ROUNDS_WON;
				col++;
			}
			// rounds drawn
			if(showRoundsDrawn) 
			{	String key = GuiKeys.COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_DRAWN;
				setLabelKey(0,col,key,true);
				rankCriterions[col] = RankCriterion.ROUNDS_DRAWN;
				col++;
			}
			// rounds lost
			if(showRoundsLost) 
			{	String key = GuiKeys.COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_LOST;
				setLabelKey(0,col,key,true);
				rankCriterions[col] = RankCriterion.ROUNDS_LOST;
				col++;
			}
			// time played
			if(showTimePlayed) 
			{	String key = GuiKeys.COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_TIME_PLAYED;
				setLabelKey(0,col,key,true);
				rankCriterions[col] = RankCriterion.TIME_PLAYED;
				col++;
			}
		}
		
		// data
		{	// init
			RankingService rankingService = GameStatistics.getRankingService();
			HashMap<Integer,PlayerStats> playersStats = GameStatistics.getPlayersStats();
			// process each playerId
			int line = 1;
			while(line<lines && line<=playerIds.size())
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
						key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_REGISTER;
					else
						key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_UNREGISTER;
					setLabelKey(line,col,key,true);
					JLabel label = getLabel(line,col);
					label.addMouseListener(this);
					col++;
				}
				// rank
				{	if(playerRating!=null)
					{	String text = Integer.toString(rankingService.getPlayerRank(playerId));
						String tooltip = text;
						setLabelText(line,col,text,tooltip);
						int temp = GuiTools.getPixelWidth(getLineFontSize(),text);
						if(temp>rankWidth)
							rankWidth = temp;
					}
					else
					{	String key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_NO_RANK;
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
						key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_HUMAN;
					else
						key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_COMPUTER;
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
					NumberFormat nfText = NumberFormat.getInstance();
					nfText.setMaximumFractionDigits(0);
					String text = nfText.format(mean);
					NumberFormat nfTooltip = NumberFormat.getInstance();
					nfTooltip.setMaximumFractionDigits(6);
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
					NumberFormat nfText = NumberFormat.getInstance();
					nfText.setMaximumFractionDigits(0);
					String text = nfText.format(stdev);
					NumberFormat nfTooltip = NumberFormat.getInstance();
					nfTooltip.setMaximumFractionDigits(6);
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
					NumberFormat nfText = NumberFormat.getInstance();
					nfText.setMaximumFractionDigits(2);
					nfText.setMinimumFractionDigits(2);
					String text = nfText.format(variability);
					NumberFormat nfTooltip = NumberFormat.getInstance();
					nfTooltip.setMaximumFractionDigits(6);
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
						roundcountWidth = temp;
					col++;
				}
				// scores
				if(showScores)
				{	String[] scores = 
					{	Long.toString(playerStats.getScore(Score.BOMBS)),
						Long.toString(playerStats.getScore(Score.ITEMS)),
						Long.toString(playerStats.getScore(Score.BOMBEDS)),
						Long.toString(playerStats.getScore(Score.BOMBINGS)),
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
					String text = StringTools.formatTime(timePlayed,TimeUnit.HOUR,TimeUnit.MINUTE);
					String tooltip = StringTools.formatTime(timePlayed,TimeUnit.HOUR,TimeUnit.MILLISECOND);
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
			
/*			if(roundcountCol!=-1)
			{	String key = GuiKeys.COMMON_STATISTICS_PLAYER_GLICKO2_HEADER_ROUND_COUNT;
				String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
				String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP);
				String avrgStr = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_MEAN);
				float avrgRoundcount = totalRoundcount/(float)normRoundcount;
				tooltip = tooltip+" ("+avrgStr+":"+avrgRoundcount+")";
				setLabelText(0,roundcountCol,text,tooltip);
			}
*/
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
	
	// NOTE button, rank, portrait, type, name, mean, stdev, volatility, rouncount,	scores*4, played, won, drawn, lost, time 	
	@Override
	public void mousePressed(MouseEvent e)
	{	JLabel label = (JLabel)e.getComponent();
		int[] pos = getLabelPositionSimple(label);
		// change order
		if(pos[0]==0)
		{	RankCriterion rankCriterion = rankCriterions[pos[1]];
			firePlayerStatisticsComparatorChanged(rankCriterion);
		}
		// add/remove
		else if(pos[1]==1)
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

	private void firePlayerStatisticsComparatorChanged(RankCriterion rankCriterion)
	{	for(PlayerStatisticsSubPanelListener listener: listeners)
			listener.playerStatisticsComparatorChanged(rankCriterion);
	}
}

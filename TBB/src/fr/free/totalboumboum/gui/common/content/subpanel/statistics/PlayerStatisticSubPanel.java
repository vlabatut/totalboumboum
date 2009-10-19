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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.free.totalboumboum.configuration.profile.Portraits;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.gui.common.structure.subpanel.container.EmptySubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.content.EmptyContentPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.statistics.GameStatistics;
import fr.free.totalboumboum.statistics.detailed.Score;
import fr.free.totalboumboum.statistics.general.PlayerStats;
import fr.free.totalboumboum.statistics.glicko2.jrs.PlayerRating;
import fr.free.totalboumboum.statistics.glicko2.jrs.RankingService;
import fr.free.totalboumboum.tools.StringTools;
import fr.free.totalboumboum.tools.StringTools.TimeUnit;

public class PlayerStatisticSubPanel extends EmptySubPanel implements MouseListener
{	private static final long serialVersionUID = 1L;
	private final static int COL_PREVIOUS = 0;
	private final static int COL_NEXT = 2;

	public PlayerStatisticSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER);

		// set panel
		EmptyContentPanel dataPanel = getDataPanel();
		dataPanel.setOpaque(false);
		
		// background
		{	Color bg = GuiTools.COLOR_COMMON_BACKGROUND;
			setBackground(bg);
		}
		
		// layout
		{	BoxLayout layout = new BoxLayout(dataPanel,BoxLayout.PAGE_AXIS); 
			dataPanel.setLayout(layout);
		}
		
		// sizes
		int buttonHeight = GuiTools.subPanelTitleHeight;
		int upWidth = (getDataWidth() - GuiTools.subPanelMargin)/2;
		int downWidth = getDataWidth()- upWidth - GuiTools.subPanelMargin;
		int mainPanelHeight = getDataHeight() - buttonHeight - GuiTools.subPanelMargin;
		
		// main panel
		{	mainPanel = new TableSubPanel(getDataWidth(),mainPanelHeight,Mode.NOTHING,1,1,true);
			dataPanel.add(mainPanel);
		}
		
		dataPanel.add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)));
		
		// buttons
		{	// buttons panel
			buttonsPanel = new JPanel();
			buttonsPanel.setOpaque(false);
			Dimension dimension = new Dimension(getDataWidth(),buttonHeight);
			buttonsPanel.setMinimumSize(dimension);
			buttonsPanel.setPreferredSize(dimension);
			buttonsPanel.setMaximumSize(dimension);
			BoxLayout layout = new BoxLayout(buttonsPanel,BoxLayout.LINE_AXIS); 
			buttonsPanel.setLayout(layout);
			buttonsPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
			buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			// up button
			{	JLabel label = new JLabel();
				label.setOpaque(true);
				label.setBackground(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
				Dimension dim = new Dimension(upWidth,buttonHeight);
				label.setMinimumSize(dim);
				label.setPreferredSize(dim);
				label.setMaximumSize(dim);
				String key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_PREVIOUS;
				GuiTools.setLabelKey(label,key,true);
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setVerticalAlignment(JLabel.CENTER);
				label.addMouseListener(this);
				buttonsPanel.add(label);
			}
			buttonsPanel.add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)));
			// down
			{	JLabel label = new JLabel();
				label.setOpaque(true);
				label.setBackground(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
				Dimension dim = new Dimension(downWidth,buttonHeight);
				label.setMinimumSize(dim);
				label.setPreferredSize(dim);
				label.setMaximumSize(dim);
				String key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_NEXT;
				GuiTools.setLabelKey(label,key,true);
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setVerticalAlignment(JLabel.CENTER);
				label.addMouseListener(this);
				buttonsPanel.add(label);
			}
			dataPanel.add(buttonsPanel);
		}
		
		// pages
		currentPage = 0;
		setPlayersIds(null,16);
		setSort(RankCriterion.MEAN);
	}
	
	/////////////////////////////////////////////////////////////////
	// PAGES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<Integer> playersIds;
	private HashMap<Integer,Profile> profilesMap;
	private int currentPage = 0;
	private final ArrayList<TableSubPanel> listPanels = new ArrayList<TableSubPanel>();
	private int pageCount;	
	private int lines;
	private TableSubPanel mainPanel;
	private JPanel buttonsPanel;
	
	public HashMap<Integer,Profile> getPlayersProfiles()
	{	return profilesMap;	
	}
	
	public List<Integer> getPlayersIds()
	{	return playersIds;	
	}
	
	public void setPlayersIds(HashMap<Integer,Profile> profilesMap, int lines)
	{	// init
		this.lines = lines;
		lines++;
		if(profilesMap==null)
			profilesMap = new HashMap<Integer,Profile>();		
		this.profilesMap = profilesMap;
		listPanels.clear();
		
		// sorting players
		playersIds = new ArrayList<Integer>(profilesMap.keySet());
		Collections.sort(playersIds,comparator);
		if(inverted)
		{	List<Integer> temp = playersIds;
			playersIds = new ArrayList<Integer>();
			for(Integer i: temp)
				playersIds.add(0,i);
		}
		
		// pages
		pageCount = getPageCount();
		if(currentPage>=pageCount)
			currentPage = pageCount-1;
		
		// cols
		int cols = 0,colName;
		rankCriterions.clear();
		List<String> headerKeys = new ArrayList<String>();
		List<Integer> colWidths = new ArrayList<Integer>();
		// button
		{	headerKeys.add(null);
			rankCriterions.add(null);
			colWidths.add(0);
			cols++;
		}
		// rank
		{	headerKeys.add(GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_HEADER_RANK);
			rankCriterions.add(RankCriterion.MEAN);
			colWidths.add(0);
			cols++;
		}
		// portrait
		if(showPortrait) 
		{	headerKeys.add(GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_HEADER_PORTRAIT);
			rankCriterions.add(null);
			colWidths.add(0);
			cols++;
		}
		// type
		if(showType) 
		{	headerKeys.add(GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_HEADER_TYPE);
			rankCriterions.add(null);
			colWidths.add(0);
			cols++;
		}
		// name
		{	headerKeys.add(GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_HEADER_NAME);
			rankCriterions.add(null);
			colWidths.add(0);
			colName = cols;
			cols++;
		}
		// mean
		if(showMean) 
		{	headerKeys.add(GuiKeys.COMMON_STATISTICS_PLAYER_GLICKO2_HEADER_MEAN);
			rankCriterions.add(RankCriterion.MEAN);
			colWidths.add(0);
			cols++;
		}
		// standard-deviation
		if(showStdev) 
		{	headerKeys.add(GuiKeys.COMMON_STATISTICS_PLAYER_GLICKO2_HEADER_STANDARD_DEVIATION);
			rankCriterions.add(null);
			colWidths.add(0);
			cols++;
		}
		// volatility
		if(showVolatility) 
		{	headerKeys.add(GuiKeys.COMMON_STATISTICS_PLAYER_GLICKO2_HEADER_VOLATILITY);
			rankCriterions.add(null);
			colWidths.add(0);
			cols++;
		}
		// roundcount
		if(showRoundcount) 
		{	headerKeys.add(GuiKeys.COMMON_STATISTICS_PLAYER_GLICKO2_HEADER_ROUND_COUNT);
			rankCriterions.add(RankCriterion.ROUNDCOUNT);
			colWidths.add(0);
			cols++;
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
			{	headerKeys.add(keys[c]);
				rankCriterions.add(rc[c]);				
				colWidths.add(0);
			}
			cols = cols+keys.length;
		}
		// rounds played
		if(showRoundsPlayed) 
		{	headerKeys.add(GuiKeys.COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_PLAYED);
			rankCriterions.add(RankCriterion.ROUNDS_PLAYED);
			colWidths.add(0);
			cols++;
		}
		// rounds won
		if(showRoundsWon) 
		{	headerKeys.add(GuiKeys.COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_WON);
			rankCriterions.add(RankCriterion.ROUNDS_WON);
			colWidths.add(0);
			cols++;
		}
		// rounds drawn
		if(showRoundsDrawn) 
		{	headerKeys.add(GuiKeys.COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_DRAWN);
			rankCriterions.add(RankCriterion.ROUNDS_DRAWN);
			colWidths.add(0);
			cols++;
		}
		// rounds lost
		if(showRoundsLost) 
		{	headerKeys.add(GuiKeys.COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_LOST);
			rankCriterions.add(RankCriterion.ROUNDS_LOST);
			colWidths.add(0);
			cols++;
		}
		// time played
		if(showTimePlayed) 
		{	headerKeys.add(GuiKeys.COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_TIME_PLAYED);
			rankCriterions.add(RankCriterion.TIME_PLAYED);
			colWidths.add(0);
			cols++;
		}
		
		// building all pages
		int headerHeight = 0;
		RankingService rankingService = GameStatistics.getRankingService();
		HashMap<Integer,PlayerStats> playersStats = GameStatistics.getPlayersStats();
		int idIndex = 0;
		for(int panelIndex=0;panelIndex<pageCount;panelIndex++)
		{	// build the panel			
			int mainPanelWidth = mainPanel.getWidth();
			int mainPanelHeight = mainPanel.getHeight();
			TableSubPanel panel = new TableSubPanel(mainPanelWidth,mainPanelHeight,Mode.NOTHING,lines,cols,true);
			panel.setOpaque(false);
			
			// headers
			if(panelIndex==0)
				headerHeight = panel.getHeaderHeight();
			for(int h=0;h<headerKeys.size();h++)
			{	String key = headerKeys.get(h);
				if(key!=null)
					panel.setLabelKey(0,h,key,true);
				JLabel label = panel.getLabel(0,h);
				label.addMouseListener(this);
				if(headerHeight>colWidths.get(h))
					colWidths.set(h,headerHeight);
			}

			// data
			{	// process each playerId
				int line = 1;
				while(line<lines && idIndex<playersIds.size())
				{	// init
					int col = 0;
					int playerId = playersIds.get(idIndex);
					idIndex++;
					Profile profile = profilesMap.get(playerId);
					PlayerRating playerRating = rankingService.getPlayerRating(playerId);
					PlayerStats playerStats = playersStats.get(playerId);
					// color
					Color clr = profile.getSpriteColor().getColor();
					int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
					panel.setLineBackground(line,bg);
					// button
					{	String key;
						if(playerRating==null)
							key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_REGISTER;
						else
							key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_UNREGISTER;
						panel.setLabelKey(line,col,key,true);
						JLabel label = panel.getLabel(line,col);
						label.addMouseListener(this);
						col++;
					}
					// rank
					{	if(playerRating!=null)
						{	String text = Integer.toString(rankingService.getPlayerRank(playerId));
							String tooltip = text;
							panel.setLabelText(line,col,text,tooltip);
							int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
							if(temp>colWidths.get(col))
								colWidths.set(col,temp);
						}
						else
						{	String key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_NO_RANK;
							panel.setLabelKey(line,col,key,false);
						}
						col++;
					}
					// portrait
					if(showPortrait)
					{	BufferedImage image = profile.getPortraits().getOutgamePortrait(Portraits.OUTGAME_HEAD);
						String tooltip = profile.getSpriteName();
						panel.setLabelIcon(line,col,image,tooltip);
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
						panel.setLabelKey(line,col,key,true);
						col++;
					}
					// name
					{	String text = profile.getName();
						String tooltip = profile.getName();
						panel.setLabelText(line,col,text,tooltip);
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
						panel.setLabelText(line,col,text,tooltip);
						int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
						if(temp>colWidths.get(col))
							colWidths.set(col,temp);
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
						panel.setLabelText(line,col,text,tooltip);
						int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
						if(temp>colWidths.get(col))
							colWidths.set(col,temp);
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
						panel.setLabelText(line,col,text,tooltip);
						int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
						if(temp>colWidths.get(col))
							colWidths.set(col,temp);
						col++;
					}
					// roundcount
					if(showRoundcount)
					{	int roundcount = playerRating.getRoundcount();
						String text = Integer.toString(roundcount);
						String tooltip = text;
						panel.setLabelText(line,col,text,tooltip);
						int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
						if(temp>colWidths.get(col))
							colWidths.set(col,temp);
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
							panel.setLabelText(line,col,text,tooltip);
							int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
							if(temp>colWidths.get(col))
								colWidths.set(col,temp);
							col++;
						}
					}			
					// rounds played
					if(showRoundsPlayed)
					{	long roundsPlayed = playerStats.getRoundsPlayed();
						String text = Long.toString(roundsPlayed);
						String tooltip = text;
						panel.setLabelText(line,col,text,tooltip);
						int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
						if(temp>colWidths.get(col))
							colWidths.set(col,temp);
						col++;
					}
					// rounds won
					if(showRoundsWon)
					{	long roundsWon = playerStats.getRoundsWon();
						String text = Long.toString(roundsWon);
						String tooltip = text;
						panel.setLabelText(line,col,text,tooltip);
						int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
						if(temp>colWidths.get(col))
							colWidths.set(col,temp);
						col++;
					}
					// rounds drawn
					if(showRoundsDrawn)
					{	long roundsDrawn = playerStats.getRoundsDrawn();
						String text = Long.toString(roundsDrawn);
						String tooltip = text;
						panel.setLabelText(line,col,text,tooltip);
						int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
						if(temp>colWidths.get(col))
							colWidths.set(col,temp);
						col++;
					}
					// rounds lost
					if(showRoundsLost)
					{	long roundsLost = playerStats.getRoundsLost();
						String text = Long.toString(roundsLost);
						String tooltip = text;
						panel.setLabelText(line,col,text,tooltip);
						int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
						if(temp>colWidths.get(col))
							colWidths.set(col,temp);
						col++;
					}
					// time played
					if(showTimePlayed)
					{	long timePlayed = playerStats.getScore(Score.TIME);
						String text = StringTools.formatTime(timePlayed,TimeUnit.HOUR,TimeUnit.MINUTE);
						String tooltip = StringTools.formatTime(timePlayed,TimeUnit.HOUR,TimeUnit.MILLISECOND);
						panel.setLabelText(line,col,text,tooltip);
						int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
						if(temp>colWidths.get(col))
							colWidths.set(col,temp);
						col++;
					}
					//
					line++;
				}
			}
						
			// add panel to list
			listPanels.add(panel);
		}
		
		// col widths
		{	// process name width
			int nameWidth = getDataWidth() - (cols-1)*GuiTools.subPanelMargin;
			for(int c=0;c<colWidths.size();c++)
			{	if(c!=colName)
					nameWidth = nameWidth - colWidths.get(c);
			}
			colWidths.set(colName,nameWidth);
			
			// set widths in all panels
			for(TableSubPanel panel: listPanels)
			{	for(int c=0;c<colWidths.size();c++)
				{	int width = colWidths.get(c);
					panel.setColSubMinWidth(c,width);
					panel.setColSubPrefWidth(c,width);
					panel.setColSubMaxWidth(c,width);
				}
			}
		}
		
		refreshList();
	}
	
	private int getPageCount()
	{	int result = playersIds.size()/lines;
		if(playersIds.size()%lines>0)
			result++;
		else if(result==0)
			result = 1;
		return result;
	}		
	
	private void refreshList()
	{	EmptyContentPanel dataPanel = getDataPanel();
		// remove the old panel
		int index = GuiTools.indexOfComponent(dataPanel,mainPanel);
		dataPanel.remove(index);
		
		// put the new one
		mainPanel = listPanels.get(currentPage);
		dataPanel.add(mainPanel,index);
		
		// refresh
		validate();
		repaint();
	}
	
	public void refresh()
	{	setPlayersIds(profilesMap,lines);
	}

	/////////////////////////////////////////////////////////////////
	// SORT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<RankCriterion> rankCriterions = new ArrayList<RankCriterion>();
	private RankCriterion rankCriterion = null;
	private Comparator<Integer> comparator;
	private boolean inverted = false;
	
	public void setSort(RankCriterion sort)
	{	if(rankCriterion==sort)
			inverted = !inverted;
		else if(sort==RankCriterion.MEAN)
		{	comparator = new Comparator<Integer>()
			{	@Override
				public int compare(Integer playerId1, Integer playerId2)
				{	int result;
					RankingService rankingService = GameStatistics.getRankingService();
					PlayerRating playerRating1 = rankingService.getPlayerRating(playerId1);
					PlayerRating playerRating2 = rankingService.getPlayerRating(playerId2);
					double value1 = 0;
					double stdev1 = 0;
					if(playerRating1!=null)
					{	value1 = playerRating1.getRating();
						stdev1 = playerRating1.getRatingDeviation();
					}
					double value2 = 0;
					double stdev2 = 0;
					if(playerRating2!=null)
					{	value2 = playerRating2.getRating();
						stdev2 = playerRating2.getRatingDeviation();
					}
					if(value1>value2)
						result = -1;
					else if(value1<value2)
						result = 1;
					else if(stdev1<stdev2)
						result = -1;
					else if(stdev1>stdev2)
						result = 1;
					else
						result = playerId1-playerId2;
					return result;
				}
			};			
		}
		else if(sort==RankCriterion.ROUNDCOUNT)
		{	comparator = new Comparator<Integer>()
			{	@Override
				public int compare(Integer playerId1, Integer playerId2)
				{	int result;
					RankingService rankingService = GameStatistics.getRankingService();
					PlayerRating playerRating1 = rankingService.getPlayerRating(playerId1);
					PlayerRating playerRating2 = rankingService.getPlayerRating(playerId2);
					double value1 = 0;
					if(playerRating1!=null)
						value1 = playerRating1.getRoundcount();
					double value2 = 0;
					if(playerRating2!=null)
						value2 = playerRating2.getRoundcount();
					if(value1>value2)
						result = -1;
					else if(value1<value2)
						result = 1;
					else
						result = 0;
					return result;
				}
			};			
		}
		else if(sort==RankCriterion.BOMBS)
		{	comparator = new Comparator<Integer>()
			{	@Override
				public int compare(Integer playerId1, Integer playerId2)
				{	int result;
					HashMap<Integer,PlayerStats> playersStats = GameStatistics.getPlayersStats();
					double value1 = playersStats.get(playerId1).getScore(Score.BOMBS);
					double value2 = playersStats.get(playerId2).getScore(Score.BOMBS);
					if(value1>value2)
						result = -1;
					else if(value1<value2)
						result = 1;
					else
						result = 0;
					return result;
				}
			};			
		}
		else if(sort==RankCriterion.ITEMS)
		{	comparator = new Comparator<Integer>()
			{	@Override
				public int compare(Integer playerId1, Integer playerId2)
				{	int result;
					HashMap<Integer,PlayerStats> playersStats = GameStatistics.getPlayersStats();
					double value1 = playersStats.get(playerId1).getScore(Score.ITEMS);
					double value2 = playersStats.get(playerId2).getScore(Score.ITEMS);
					if(value1>value2)
						result = -1;
					else if(value1<value2)
						result = 1;
					else
						result = 0;
					return result;
				}
			};			
		}
		else if(sort==RankCriterion.BOMBEDS)
		{	comparator = new Comparator<Integer>()
			{	@Override
				public int compare(Integer playerId1, Integer playerId2)
				{	int result;
					HashMap<Integer,PlayerStats> playersStats = GameStatistics.getPlayersStats();
					double value1 = playersStats.get(playerId1).getScore(Score.BOMBEDS);
					double value2 = playersStats.get(playerId2).getScore(Score.BOMBEDS);
					if(value1>value2)
						result = -1;
					else if(value1<value2)
						result = 1;
					else
						result = 0;
					return result;
				}
			};			
		}
		else if(sort==RankCriterion.BOMBINGS)
		{	comparator = new Comparator<Integer>()
			{	@Override
				public int compare(Integer playerId1, Integer playerId2)
				{	int result;
					HashMap<Integer,PlayerStats> playersStats = GameStatistics.getPlayersStats();
					double value1 = playersStats.get(playerId1).getScore(Score.BOMBINGS);
					double value2 = playersStats.get(playerId2).getScore(Score.BOMBINGS);
					if(value1>value2)
						result = -1;
					else if(value1<value2)
						result = 1;
					else
						result = 0;
					return result;
				}
			};			
		}
		else if(sort==RankCriterion.CROWNS)
		{	comparator = new Comparator<Integer>()
			{	@Override
				public int compare(Integer playerId1, Integer playerId2)
				{	int result;
					HashMap<Integer,PlayerStats> playersStats = GameStatistics.getPlayersStats();
					double value1 = playersStats.get(playerId1).getScore(Score.CROWNS);
					double value2 = playersStats.get(playerId2).getScore(Score.CROWNS);
					if(value1>value2)
						result = -1;
					else if(value1<value2)
						result = 1;
					else
						result = 0;
					return result;
				}
			};			
		}
		else if(sort==RankCriterion.PAINTINGS)
		{	comparator = new Comparator<Integer>()
			{	@Override
				public int compare(Integer playerId1, Integer playerId2)
				{	int result;
					HashMap<Integer,PlayerStats> playersStats = GameStatistics.getPlayersStats();
					double value1 = playersStats.get(playerId1).getScore(Score.CROWNS);
					double value2 = playersStats.get(playerId2).getScore(Score.CROWNS);
					if(value1>value2)
						result = -1;
					else if(value1<value2)
						result = 1;
					else
						result = 0;
					return result;
				}
			};			
		}
		else if(sort==RankCriterion.TIME_PLAYED)
		{	comparator = new Comparator<Integer>()
			{	@Override
				public int compare(Integer playerId1, Integer playerId2)
				{	int result;
					HashMap<Integer,PlayerStats> playersStats = GameStatistics.getPlayersStats();
					double value1 = playersStats.get(playerId1).getScore(Score.TIME);
					double value2 = playersStats.get(playerId2).getScore(Score.TIME);
					if(value1>value2)
						result = -1;
					else if(value1<value2)
						result = 1;
					else
						result = 0;
					return result;
				}
			};			
		}
		else if(sort==RankCriterion.ROUNDS_PLAYED)
		{	comparator = new Comparator<Integer>()
			{	@Override
				public int compare(Integer playerId1, Integer playerId2)
				{	int result;
					HashMap<Integer,PlayerStats> playersStats = GameStatistics.getPlayersStats();
					double value1 = playersStats.get(playerId1).getRoundsPlayed();
					double value2 = playersStats.get(playerId2).getRoundsPlayed();
					if(value1>value2)
						result = -1;
					else if(value1<value2)
						result = 1;
					else
						result = 0;
					return result;
				}
			};			
		}
		else if(sort==RankCriterion.ROUNDS_WON)
		{	comparator = new Comparator<Integer>()
			{	@Override
				public int compare(Integer playerId1, Integer playerId2)
				{	int result;
					HashMap<Integer,PlayerStats> playersStats = GameStatistics.getPlayersStats();
					double value1 = playersStats.get(playerId1).getRoundsWon();
					double value2 = playersStats.get(playerId2).getRoundsWon();
					if(value1>value2)
						result = -1;
					else if(value1<value2)
						result = 1;
					else
						result = 0;
					return result;
				}
			};			
		}
		else if(sort==RankCriterion.ROUNDS_DRAWN)
		{	comparator = new Comparator<Integer>()
			{	@Override
				public int compare(Integer playerId1, Integer playerId2)
				{	int result;
					HashMap<Integer,PlayerStats> playersStats = GameStatistics.getPlayersStats();
					double value1 = playersStats.get(playerId1).getRoundsDrawn();
					double value2 = playersStats.get(playerId2).getRoundsDrawn();
					if(value1>value2)
						result = -1;
					else if(value1<value2)
						result = 1;
					else
						result = 0;
					return result;
				}
			};			
		}
		else if(sort==RankCriterion.ROUNDS_LOST)
		{	comparator = new Comparator<Integer>()
			{	@Override
				public int compare(Integer playerId1, Integer playerId2)
				{	int result;
					HashMap<Integer,PlayerStats> playersStats = GameStatistics.getPlayersStats();
					double value1 = playersStats.get(playerId1).getRoundsLost();
					double value2 = playersStats.get(playerId2).getRoundsLost();
					if(value1>value2)
						result = -1;
					else if(value1<value2)
						result = 1;
					else
						result = 0;
					return result;
				}
			};			
		}
	
		if(rankCriterion!=null)
		{	rankCriterion = sort;
			refresh();
		}
		else
		{	rankCriterion = sort;
		}
	}
	
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
		int pos = GuiTools.indexOfComponent(buttonsPanel,label);
		// bottom buttons
		if(pos!=-1)
		{	// previous page
			if(pos==COL_PREVIOUS)
			{	if(currentPage>0)
				{	currentPage--;
					refreshList();
				}
			}
			// next page
			else if(pos==COL_NEXT)
			{	if(currentPage<getPageCount()-1)
				{	currentPage++;
					refreshList();
				}
			}
		}
		// table buttons
		else
		{	int[] p = mainPanel.getLabelPositionSimple(label);
			// change order
			if(p[0]==0)
			{	RankCriterion rc = rankCriterions.get(p[1]);
				if(rc!=null)
					setSort(rc);
			}
			// add/remove
			else if(p[1]==0)
			{	int playerId = playersIds.get((currentPage*lines)+p[0]-1);
				RankingService rankingService = GameStatistics.getRankingService();
				Set<Integer> playersIds = rankingService.getPlayers();
				if(playersIds.contains(playerId))
					GameStatistics.getRankingService().deregisterPlayer(playerId);
				else
					GameStatistics.getRankingService().registerPlayer(playerId);
				refresh();
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY								/////////////////////////
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
		refresh();
	}

	public void setShowType(boolean showType)
	{	this.showType = showType;
		refresh();
	}

	public void setShowMean(boolean showMean)
	{	this.showMean = showMean;
		refresh();
	}

	public void setShowStdev(boolean showStdev)
	{	this.showStdev = showStdev;
		refresh();
	}

	public void setShowVolatility(boolean showVolatility)
	{	this.showVolatility = showVolatility;
		refresh();
	}

	public void setShowRoundcount(boolean showRoundcount)
	{	this.showRoundcount = showRoundcount;
		refresh();
	}

	public void setShowScores(boolean showScores)
	{	this.showScores = showScores;
		refresh();
	}

	public void setShowRoundsPlayed(boolean showRoundsPlayed)
	{	this.showRoundsPlayed = showRoundsPlayed;
		refresh();
	}

	public void setShowRoundsWon(boolean showRoundsWon)
	{	this.showRoundsWon = showRoundsWon;
		refresh();
	}

	public void setShowRoundsLost(boolean showRoundsLost)
	{	this.showRoundsLost = showRoundsLost;
		refresh();
	}

	public void setShowRoundsDrawn(boolean showRoundsDrawn)
	{	this.showRoundsDrawn = showRoundsDrawn;
		refresh();
	}

	public void setShowTimePlayed(boolean showTimePlayed)
	{	this.showTimePlayed = showTimePlayed;
		refresh();
	}
}

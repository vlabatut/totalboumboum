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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.gui.common.structure.subpanel.container.EmptySubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.content.EmptyContentPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.statistics.GameStatistics;
import fr.free.totalboumboum.statistics.detailed.Score;
import fr.free.totalboumboum.statistics.general.PlayerStats;
import fr.free.totalboumboum.statistics.glicko2.jrs.PlayerRating;
import fr.free.totalboumboum.statistics.glicko2.jrs.RankingService;

public class PlayerStatisticBrowserSubPanel extends EmptySubPanel implements MouseListener, PlayerStatisticsSubPanelListener
{	private static final long serialVersionUID = 1L;
	private final static int COL_PREVIOUS = 0;
	private final static int COL_NEXT = 2;

	public PlayerStatisticBrowserSubPanel(int width, int height)
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
		{	mainPanel = new JPanel();
			Dimension dimension = new Dimension(getDataWidth(),mainPanelHeight);
			mainPanel.setMinimumSize(dimension);
			mainPanel.setPreferredSize(dimension);
			mainPanel.setMaximumSize(dimension);
			dataPanel.add(mainPanel);
		}
		
		dataPanel.add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)));
		
		// buttons
		{	// buttons panel
			JPanel buttonsPanel = new JPanel();
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
	private int currentPage = 0;
	private ArrayList<PlayerStatisticsSubPanel> listPanels;
	private int pageCount;	
	private int lines;
	private JPanel mainPanel;
	
	public List<Integer> getPlayersIds()
	{	return playersIds;	
	}
	
	public void setPlayersIds(List<Integer> playersIds, int lines)
	{	// init
		this.lines = lines;
		if(playersIds==null)
			playersIds = new ArrayList<Integer>();		
		this.playersIds = playersIds;
		listPanels = new ArrayList<PlayerStatisticsSubPanel>();
		
		// init
		pageCount = getPageCount();
		if(currentPage>=pageCount)
			currentPage = pageCount-1;
		
		// sorting players
		TreeSet<Integer> temp = new TreeSet<Integer>(comparator);
		for(Integer playerId:playersIds)
			temp.add(playerId);
		List<Integer> sortedPlayersIds;;
		if(inverted)
		{	sortedPlayersIds = new ArrayList<Integer>();
			for(Integer i: temp)
				sortedPlayersIds.add(0,i);
		}
		else
			sortedPlayersIds = new ArrayList<Integer>(temp);
		
		// building all pages
		int index = 0;
		for(int panelIndex=0;panelIndex<pageCount;panelIndex++)
		{	// build the players list
			int i = 0;
			List<Integer> idList = new ArrayList<Integer>();
			while(index<sortedPlayersIds.size() && i<lines)
			{	idList.add(sortedPlayersIds.get(index));
				index++;
				i++;
			}
			// build the panel			
			int mainPanelHeight = mainPanel.getPreferredSize().height;
			PlayerStatisticsSubPanel panel = new PlayerStatisticsSubPanel(getDataWidth(),mainPanelHeight);
			try
			{	panel.setPlayerIds(idList,lines);
			}
			catch (IllegalArgumentException e)
			{	e.printStackTrace();
			}
			catch (SecurityException e)
			{	e.printStackTrace();
			}
			catch (ParserConfigurationException e)
			{	e.printStackTrace();
			}
			catch (SAXException e)
			{	e.printStackTrace();
			}
			catch (IOException e)
			{	e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{	e.printStackTrace();
			}
			catch (NoSuchFieldException e)
			{	e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{	e.printStackTrace();
			}
			// add to list
			listPanels.add(panel);
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
	{	setPlayersIds(playersIds,lines);
	}

	/////////////////////////////////////////////////////////////////
	// SORT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private RankCriterion rankCriterion = null;
	private Comparator<Integer> comparator;
	private boolean inverted = false;
	
	public void setSort(RankCriterion sort)
	{	if(sort==RankCriterion.MEAN)
		{	comparator = new Comparator<Integer>()
			{	@Override
				public int compare(Integer playerId1, Integer playerId2)
				{	int result;
					RankingService rankingService = GameStatistics.getRankingService();
					PlayerRating playerRating1 = rankingService.getPlayerRating(playerId1);
					PlayerRating playerRating2 = rankingService.getPlayerRating(playerId2);
					double value1 = 0;
					if(playerRating1!=null)
						value1 = playerRating1.getRating();
					double value2 = 0;
					if(playerRating2!=null)
						value2 = playerRating2.getRating();
					if(value1>value2)
						result = 1;
					else if(value1<value2)
						result = -1;
					else
						result = 0;
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
						result = 1;
					else if(value1<value2)
						result = -1;
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
						result = 1;
					else if(value1<value2)
						result = -1;
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
						result = 1;
					else if(value1<value2)
						result = -1;
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
						result = 1;
					else if(value1<value2)
						result = -1;
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
						result = 1;
					else if(value1<value2)
						result = -1;
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
						result = 1;
					else if(value1<value2)
						result = -1;
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
						result = 1;
					else if(value1<value2)
						result = -1;
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
						result = 1;
					else if(value1<value2)
						result = -1;
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
						result = 1;
					else if(value1<value2)
						result = -1;
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
						result = 1;
					else if(value1<value2)
						result = -1;
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
						result = 1;
					else if(value1<value2)
						result = -1;
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
						result = 1;
					else if(value1<value2)
						result = -1;
					else
						result = 0;
					return result;
				}
			};			
		}
	
		if(rankCriterion!=null)
		{	rankCriterion = sort;
			setPlayersIds(playersIds,lines);
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
		int pos = GuiTools.indexOfComponent(mainPanel,label);
		
		// previous page
		if(pos==COL_PREVIOUS)
		{	if(currentPage>0)
			{	currentPage--;
				refreshList();
			}
		}
		// parent
		else if(pos==COL_NEXT)
		{	if(currentPage<getPageCount()-1)
			{	currentPage++;
				refreshList();
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYER STATISTIC SUBPANEL LISTENER	/////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void playerStatisticsPlayerRegistered(int playerId)
	{	GameStatistics.getRankingService().registerPlayer(playerId);
		refreshList();
	}

	@Override
	public void playerStatisticsPlayerUnregistered(int playerId)
	{	GameStatistics.getRankingService().deregisterPlayer(playerId);
		refreshList();
	}

	@Override
	public void playerStatisticsComparatorChanged(RankCriterion rankCriterion)
	{	if(this.rankCriterion==rankCriterion)
			inverted = !inverted;
		else
			setSort(rankCriterion);
		refreshList();
	}

	/////////////////////////////////////////////////////////////////
	// DISPLAY								/////////////////////////
	/////////////////////////////////////////////////////////////////
	public void setShowPortrait(boolean showPortrait)
	{	for(PlayerStatisticsSubPanel panel: listPanels)
			panel.setShowPortrait(showPortrait);
	}

	public void setShowType(boolean showType)
	{	for(PlayerStatisticsSubPanel panel: listPanels)
			panel.setShowType(showType);
	}

	public void setShowMean(boolean showMean)
	{	for(PlayerStatisticsSubPanel panel: listPanels)
			panel.setShowMean(showMean);
	}

	public void setShowStdev(boolean showStdev)
	{	for(PlayerStatisticsSubPanel panel: listPanels)
			panel.setShowStdev(showStdev);
	}

	public void setShowVolatility(boolean showVolatility)
	{	for(PlayerStatisticsSubPanel panel: listPanels)
			panel.setShowVolatility(showVolatility);
	}

	public void setShowRoundcount(boolean showRoundcount)
	{	for(PlayerStatisticsSubPanel panel: listPanels)
			panel.setShowRoundcount(showRoundcount);
	}

	public void setShowScores(boolean showScores)
	{	for(PlayerStatisticsSubPanel panel: listPanels)
			panel.setShowScores(showScores);
	}

	public void setShowRoundsPlayed(boolean showRoundsPlayed)
	{	for(PlayerStatisticsSubPanel panel: listPanels)
			panel.setShowRoundsPlayed(showRoundsPlayed);
	}

	public void setShowRoundsWon(boolean showRoundsWon)
	{	for(PlayerStatisticsSubPanel panel: listPanels)
			panel.setShowRoundsWon(showRoundsWon);
	}

	public void setShowRoundsLost(boolean showRoundsLost)
	{	for(PlayerStatisticsSubPanel panel: listPanels)
			panel.setShowRoundsLost(showRoundsLost);
	}

	public void setShowRoundsDrawn(boolean showRoundsDrawn)
	{	for(PlayerStatisticsSubPanel panel: listPanels)
			panel.setShowRoundsDrawn(showRoundsDrawn);
	}

	public void setShowTimePlayed(boolean showTimePlayed)
	{	for(PlayerStatisticsSubPanel panel: listPanels)
			panel.setShowTimePlayed(showTimePlayed);
	}
}

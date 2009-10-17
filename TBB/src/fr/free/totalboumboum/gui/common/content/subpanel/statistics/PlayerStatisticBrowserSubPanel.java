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
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.gui.common.structure.subpanel.inside.TableContentPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.outside.LinesSubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.outside.SubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.outside.TableSubPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.statistics.GameStatistics;
import fr.free.totalboumboum.statistics.detailed.Score;
import fr.free.totalboumboum.statistics.general.PlayerStats;
import fr.free.totalboumboum.statistics.glicko2.jrs.RankingService;

public class PlayerStatisticBrowserSubPanel extends LinesSubPanel implements MouseListener, PlayerStatisticsSubPanelListener
{	private static final long serialVersionUID = 1L;
	private static final int LINES = 2;

	public PlayerStatisticBrowserSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,LINES,1,false);
		
		// pages
		setPlayersIds(null,16);
	}
	
	/////////////////////////////////////////////////////////////////
	// PAGES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<Integer> playersIds;
	private final static int COL_PREVIOUS = 0;
	private final static int COL_NEXT = 1;
	private int currentPage = 0;
	private ArrayList<PlayerStatisticsSubPanel> listPanels;
	private int pageCount;	
	private int lines;
	
	public List<Integer> getPlayersIds()
	{	return playersIds;	
	}
	
	public void setPlayersIds(List<Integer> playersIds, int lines)
	{	// init
		this.lines = 16;
		this.playersIds = playersIds;
		if(playersIds==null)
			this.playersIds = new ArrayList<Integer>();
		listPanels = new ArrayList<PlayerStatisticsSubPanel>();
		currentPage = 0;
		
		// size
		pageCount = getPageCount();
		int menuHeight = (int)(1.5*(getDataHeight() - lines*GuiTools.panelMargin)/(lines+1));
		int panelHeight = getDataHeight() - menuHeight - GuiTools.panelMargin;
		
		// sorting players
		TreeSet<Integer> temp = new TreeSet<Integer>(comparator);
		List<Integer> sortedPlayersIds = new ArrayList<Integer>(temp);
		
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
			PlayerStatisticsSubPanel panel = new PlayerStatisticsSubPanel(getDataWidth(),panelHeight);
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
	{	int result = playersIds.size()/LINES;
		if(playersIds.size()%LINES>0)
			result++;
		else if(result==0)
			result = 1;
		return result;
	}		
	
	private void refreshList()
	{	PlayerStatisticsSubPanel p = listPanels.get(currentPage);
		setDataPanel(p);
		validate();
		repaint();
	}
	
	public void refresh()
	{	String selectedFileName = getSelectedFileName();
		setFileNames(fileNames);
		setSelectedFileName(selectedFileName);
	}

	/////////////////////////////////////////////////////////////////
	// SORT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Comparator<Integer> comparator;
	
	public void setSort(RankCriterion sort)
	{	if(sort==RankCriterion.MEAN)
		{	comparator = new Comparator<Integer>()
			{	@Override
				public int compare(Integer playerId1, Integer playerId2)
				{	int result;
					RankingService rankingService = GameStatistics.getRankingService();
					double value1 = rankingService.getPlayerRating(playerId1).getRating();
					double value2 = rankingService.getPlayerRating(playerId2).getRating();
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
					double value1 = rankingService.getPlayerRating(playerId1).getRoundcount();
					double value2 = rankingService.getPlayerRating(playerId2).getRoundcount();
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
	
		setPlayersIds(playersIds,lines);
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
		int[] pos = listPanels.get(currentPage).getLabelPosition(label);
		
		// previous page
		if(pos[0]==linePrevious)
		{	if(currentPage>0)
			{	selectName(-1);
				currentPage--;
				refreshList();
			}
		}
		// parent
		else if(pos[0]==lineParent && showParent)
		{	selectName(-1);
			refreshList();
			fireFileBrowserParentClicked();
		}
		// next page
		else if(pos[0]==lineNext)
		{	if(currentPage<getPageCount()-1)
			{	selectName(-1);
				currentPage++;
				refreshList();
			}
		}
		// select a name
		else if(pos[0]>=0)
		{	selectName(pos[0]);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<PlayerStatisticBrowserSubPanelListener> listeners = new ArrayList<PlayerStatisticBrowserSubPanelListener>();
	
	public void addListener(PlayerStatisticBrowserSubPanelListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);		
	}

	public void removeListener(PlayerStatisticBrowserSubPanelListener listener)
	{	listeners.remove(listener);		
	}
	
	private void fireFileBrowserSelectionChanged()
	{	for(PlayerStatisticBrowserSubPanelListener listener: listeners)
			listener.fileBrowserSelectionChanged();
	}

	private void fireFileBrowserParentClicked()
	{	for(PlayerStatisticBrowserSubPanelListener listener: listeners)
			listener.fileBrowserParentReached();
	}

	/////////////////////////////////////////////////////////////////
	// PLAYER STATISTIC SUBPANEL LISTENER	/////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void playerStatisticsPlayerRegistered(int playerId)
	{	// TODO Auto-generated method stub		
	}

	@Override
	public void playerStatisticsPlayerUnregistered(int playerId)
	{	// TODO Auto-generated method stub		
	}
}

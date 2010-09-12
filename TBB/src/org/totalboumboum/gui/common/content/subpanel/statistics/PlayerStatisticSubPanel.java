package org.totalboumboum.gui.common.content.subpanel.statistics;

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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
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
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.subpanel.container.EmptySubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.common.structure.subpanel.content.EmptyContentPanel;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.glicko2.jrs.PlayerRating;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.statistics.overall.PlayerStats;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class PlayerStatisticSubPanel extends EmptySubPanel implements MouseListener
{	private static final long serialVersionUID = 1L;
	private final static int COL_PREVIOUS = 0;
	private final static int COL_TYPE = 2;
	private final static int COL_RANKS = 4;
	private final static int COL_SUM_MEAN = 6;
	private final static int COL_NEXT = 8;

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
		int regularButtonWidth = (getDataWidth() - 4*GuiTools.subPanelMargin)/5;
		int centerButtonWidth = getDataWidth()- 4*regularButtonWidth - 4*GuiTools.subPanelMargin;
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
			{	MyLabel label = new MyLabel();
				label.setOpaque(true);
				label.setBackground(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
				Dimension dim = new Dimension(regularButtonWidth,buttonHeight);
				label.setMinimumSize(dim);
				label.setPreferredSize(dim);
				label.setMaximumSize(dim);
				String key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_PREVIOUS;
				label.setKey(key,true);
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setVerticalAlignment(JLabel.CENTER);
				label.addMouseListener(this);
				label.setMouseSensitive(true);
				buttonsPanel.add(label);
			}
			buttonsPanel.add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)));
			// type
			{	MyLabel label = new MyLabel();
				label.setOpaque(true);
				label.setBackground(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
				Dimension dim = new Dimension(regularButtonWidth,buttonHeight);
				label.setMinimumSize(dim);
				label.setPreferredSize(dim);
				label.setMaximumSize(dim);
				String key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_HUMAN;
				label.setKey(key,true);
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setVerticalAlignment(JLabel.CENTER);
				label.addMouseListener(this);
				label.setMouseSensitive(true);
				buttonsPanel.add(label);
			}
			buttonsPanel.add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)));
			// ranks
			{	MyLabel label = new MyLabel();
				label.setOpaque(true);
				label.setBackground(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
				Dimension dim = new Dimension(regularButtonWidth,buttonHeight);
				label.setMinimumSize(dim);
				label.setPreferredSize(dim);
				label.setMaximumSize(dim);
				String key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_ALLRANKS;
				label.setKey(key,true);
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setVerticalAlignment(JLabel.CENTER);
				label.addMouseListener(this);
				label.setMouseSensitive(true);
				buttonsPanel.add(label);
			}
			buttonsPanel.add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)));
			// sum/mean
			{	MyLabel label = new MyLabel();
				label.setOpaque(true);
				label.setBackground(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
				Dimension dim = new Dimension(centerButtonWidth,buttonHeight);
				label.setMinimumSize(dim);
				label.setPreferredSize(dim);
				label.setMaximumSize(dim);
				String key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_MEAN;
				label.setKey(key,true);
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setVerticalAlignment(JLabel.CENTER);
				label.addMouseListener(this);
				label.setMouseSensitive(true);
				buttonsPanel.add(label);
			}
			buttonsPanel.add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)));
			// down
			{	MyLabel label = new MyLabel();
				label.setOpaque(true);
				label.setBackground(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
				Dimension dim = new Dimension(regularButtonWidth,buttonHeight);
				label.setMinimumSize(dim);
				label.setPreferredSize(dim);
				label.setMaximumSize(dim);
				String key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_NEXT;
				label.setKey(key,true);
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setVerticalAlignment(JLabel.CENTER);
				label.addMouseListener(this);
				label.setMouseSensitive(true);
				buttonsPanel.add(label);
			}
			dataPanel.add(buttonsPanel);
		}
		
		// pages
		currentPage = 0;
		sortCriterion = StatisticColumn.GLICKO_MEAN;
		setPlayersIds(null,16);
	}
	
	/////////////////////////////////////////////////////////////////
	// PAGES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<String> playersIds;
	private HashMap<String,Profile> profilesMap;
	@SuppressWarnings("rawtypes")
	private HashMap<String,List<Comparable>> playersScores = new HashMap<String, List<Comparable>>();
	private int currentPage = 0;
	private final List<TableSubPanel> listPanels = new ArrayList<TableSubPanel>();
	private int pageCount;	
	private int lines;
	private TableSubPanel mainPanel;
	private JPanel buttonsPanel;
	
	public HashMap<String,Profile> getPlayersProfiles()
	{	return profilesMap;	
	}
	
	public List<String> getPlayersIds()
	{	return playersIds;	
	}
	
	public void setPlayersIds(HashMap<String,Profile> profilesMap, int lines)
	{	// init
		this.lines = lines;
		lines++;
		if(profilesMap==null)
			profilesMap = new HashMap<String,Profile>();
		this.profilesMap = profilesMap;
		listPanels.clear();
		
		// sorting players
		RankingService rankingService = GameStatistics.getRankingService();
		sortCriterion.updateValues(this,playersScores,rankingService,profilesMap);
		playersIds = new ArrayList<String>(profilesMap.keySet());
		Collections.sort(playersIds,new Comparator<String>()
		{	@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public int compare(String playerId1, String playerId2)
			{	int result = 0;
				List<Comparable> list1 = playersScores.get(playerId1);
				List<Comparable> list2 = playersScores.get(playerId2);
				int index = 0;
				while(result==0 && index<list1.size())
				{	Comparable o1 = list1.get(index);
					Comparable o2 = list2.get(index);
					result = o1.compareTo(o2);
					index++;
				}
				return result;
			}
		});
		if((inverted && !sortCriterion.isInverted())
			|| (!inverted && sortCriterion.isInverted()))
		{	List<String> temp = playersIds;
			playersIds = new ArrayList<String>();
			for(String i: temp)
				playersIds.add(0,i);
		}
		
		// cols
		int cols = columns.size();
		String headerKeys[] = new String[cols];
		int colWidths[] = new int[cols];
		for(int col=0;col<columns.size();col++)
		{	StatisticColumn rc = columns.get(col);
			headerKeys[col] = rc.getHeaderKey();
			colWidths[col] = 0;
		}
		
		// building all pages
		pageCount = 0;
		int headerHeight = 0;
		HashMap<String,PlayerStats> playersStats = GameStatistics.getPlayersStats();
		int idIndex = 0;
		do
		{	// build the panel			
			int mainPanelWidth = mainPanel.getWidth();
			int mainPanelHeight = mainPanel.getHeight();
			TableSubPanel panel = new TableSubPanel(mainPanelWidth,mainPanelHeight,Mode.NOTHING,lines,cols,true);
			panel.setOpaque(false);
			
			// headers
			if(pageCount==0)
				headerHeight = panel.getHeaderHeight();
			for(int h=0;h<cols;h++)
			{	String key = headerKeys[h];
				if(key!=null)
					panel.setLabelKey(0,h,key,true);
				MyLabel label = panel.getLabel(0,h);
				label.addMouseListener(this);
				label.setMouseSensitive(true);
				if(headerHeight>colWidths[h])
					colWidths[h] = headerHeight;
			}

			// data: process each playerId
			int line = 1;
			while(line<lines && idIndex<playersIds.size())
			{	// init
				String playerId = playersIds.get(idIndex);
				idIndex++;
				Profile profile = profilesMap.get(playerId);
				PlayerRating playerRating = rankingService.getPlayerRating(playerId);
				PlayerStats playerStats = playersStats.get(playerId);
				int playerRank = rankingService.getPlayerRank(playerId);
				if(((type==Type.AI && profile.hasAi()) || (type==Type.HUMAN && !profile.hasAi()) || type==Type.BOTH)
					&& ((ranks==Ranks.ALL_RANKS && playerRating!=null) || (ranks==Ranks.NO_RANKS && playerRating==null) || ranks==Ranks.ALL))
				{	// color
					Color clr = profile.getSpriteColor().getColor();
					int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
					if(playerRating==null)
						alpha = alpha/3;
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
					panel.setLineBackground(line,bg);
					// columns
					for(int col=0;col<cols;col++)
					{	StatisticColumn column = columns.get(col);
						column.setLabelContent(this,panel,colWidths,line,col,playerId,playerRank,profile,playerRating,playerStats);
					}
					// next line
					line++;
				}
			}
						
			// add panel to list
			if(line>1 || pageCount==0)
			{	listPanels.add(panel);
				pageCount++;
			}
		}
		while(idIndex<playersIds.size());
		
		// col widths
		{	int colName = columns.indexOf(StatisticColumn.GENERAL_NAME);
			// process name width
			if(colName!=-1)
			{	colWidths[colName] = getDataWidth() - (cols-1)*GuiTools.subPanelMargin;
				for(int col=0;col<cols;col++)
				{	if(col!=colName)
						colWidths[colName] = colWidths[colName] - colWidths[col];
				}
			}
			
			// set widths in all panels
			for(TableSubPanel panel: listPanels)
			{	for(int col=0;col<cols;col++)
				{	int width = colWidths[col];
					panel.setColSubMinWidth(col,width);
					panel.setColSubPrefWidth(col,width);
					panel.setColSubMaxWidth(col,width);
				}
			}
		}

		if(currentPage>=pageCount)
			currentPage = pageCount-1;

		refreshList();
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
	// COLUMNS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<StatisticColumn> columns = new ArrayList<StatisticColumn>();
	
	public void setColumns(List<StatisticColumn> columns)
	{	if(columns==null)
			columns = new ArrayList<StatisticColumn>();
		this.columns = columns;
		refresh();
	}
	
	/////////////////////////////////////////////////////////////////
	// SORT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private StatisticColumn sortCriterion = null;
	private boolean inverted = false;
	private boolean mean = false;
	private Type type = Type.BOTH;
	private Ranks ranks = Ranks.ALL;
	
	public boolean hasMean()
	{	return mean;
	}	
	
	public void setSort(StatisticColumn sort)
	{	if(sortCriterion==sort)
			inverted = !inverted;		
		else
		{	inverted = false;
			sortCriterion = sort;
		}
		refresh();
	}
	
	private enum Type
	{	HUMAN,
		AI,
		BOTH;
	
		public Type getNext()
		{	Type[] values = Type.values();
			int index = (this.ordinal()+1)%values.length;
			Type result = values[index];
			return result;
		}
	}
	
	private enum Ranks
	{	ALL,
		ALL_RANKS,
		NO_RANKS;
	
		public Ranks getNext()
		{	Ranks[] values = Ranks.values();
			int index = (this.ordinal()+1)%values.length;
			Ranks result = values[index];
			return result;
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
	{	MyLabel label = (MyLabel)e.getComponent();
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
			// switch type
			else if(pos==COL_TYPE)
			{	type = type.getNext();
				String key;
				if(type==Type.HUMAN)
					key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_AI;
				else if(type==Type.AI)
					key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_BOTH;
				else
					key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_HUMAN;
				MyLabel lbl = (MyLabel)buttonsPanel.getComponent(COL_TYPE);
				lbl.setKey(key,true);
				refresh();
			}
			// ranks
			else if(pos==COL_RANKS)
			{	ranks = ranks.getNext();
				String key;
				if(ranks==Ranks.ALL)
					key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_ALLRANKS;
				else if(ranks==Ranks.ALL_RANKS)
					key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_NORANKS;
				else
					key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_ALL;
				MyLabel lbl = (MyLabel)buttonsPanel.getComponent(COL_RANKS);
				lbl.setKey(key,true);
				refresh();
			}
			// mean/sum
			else if(pos==COL_SUM_MEAN)
			{	String key;
				if(mean)
					key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_MEAN;
				else
					key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_SUM;
				MyLabel lbl = (MyLabel)buttonsPanel.getComponent(COL_SUM_MEAN);
				lbl.setKey(key,true);
				mean = !mean;
				refresh();
			}
			// next page
			else if(pos==COL_NEXT)
			{	if(currentPage<pageCount-1)
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
			{	StatisticColumn rc = columns.get(p[1]);
				if(rc!=null)
					setSort(rc);
			}
			// add/remove
			else if(p[1]==0)
			{	String playerId = playersIds.get((currentPage*lines)+p[0]-1);
				RankingService rankingService = GameStatistics.getRankingService();
				Set<String> playersIds = rankingService.getPlayers();
				if(playersIds.contains(playerId))
					GameStatistics.getRankingService().deregisterPlayer(playerId);
				else
					GameStatistics.getRankingService().registerPlayer(playerId);
				// save the new rankings
				try
				{	GameStatistics.saveStatistics();
				}
				catch (IllegalArgumentException e1)
				{	e1.printStackTrace();
				}
				catch (SecurityException e1)
				{	e1.printStackTrace();
				}
				catch (IOException e1)
				{	e1.printStackTrace();
				}
				catch (ParserConfigurationException e1)
				{	e1.printStackTrace();
				}
				catch (SAXException e1)
				{	e1.printStackTrace();
				}
				catch (IllegalAccessException e1)
				{	e1.printStackTrace();
				}
				catch (NoSuchFieldException e1)
				{	e1.printStackTrace();
				}
				catch (ClassNotFoundException e1)
				{	e1.printStackTrace();
				}
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
	public void addColumn(int index, StatisticColumn column)
	{	columns.add(index,column);
		refresh();
	}

	public void setColumn(int index, StatisticColumn column)
	{	columns.set(index,column);
		refresh();
	}

	public void removeColumn(int index)
	{	columns.remove(index);
		refresh();
	}
}

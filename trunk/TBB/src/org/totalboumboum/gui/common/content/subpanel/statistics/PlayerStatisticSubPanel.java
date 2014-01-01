package org.totalboumboum.gui.common.content.subpanel.statistics;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.totalboumboum.tools.GameData;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiMiscTools;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.glicko2.jrs.PlayerRating;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.statistics.overall.PlayerStats;
import org.totalboumboum.tools.images.PredefinedColor;
import org.xml.sax.SAXException;

/**
 * This class displays the overall statistics
 * for all players, including glicko-2 rankings
 * and more detailed info such as scores or confrontations.
 * 
 * @author Vincent Labatut
 */
public class PlayerStatisticSubPanel extends EmptySubPanel implements MouseListener
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a standard player statistic panel.
	 * 
	 * @param width
	 * 		Width in pixels.
	 * @param height
	 * 		Height in pixels.
	 */
	public PlayerStatisticSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER);

		// set panel
		EmptyContentPanel dataPanel = getDataPanel();
		dataPanel.setOpaque(false);
		
		// background
		{	Color bg = GuiColorTools.COLOR_COMMON_BACKGROUND;
			setBackground(bg);
		}
		
		// layout
		{	BoxLayout layout = new BoxLayout(dataPanel,BoxLayout.PAGE_AXIS); 
			dataPanel.setLayout(layout);
		}
		
		// sizes
		int buttonHeight = GuiSizeTools.subPanelTitleHeight;
		int regularButtonWidth = (getDataWidth() - 4*GuiSizeTools.subPanelMargin)/5;
		int centerButtonWidth = getDataWidth()- 4*regularButtonWidth - 4*GuiSizeTools.subPanelMargin;
		int mainPanelHeight = getDataHeight() - buttonHeight - GuiSizeTools.subPanelMargin;
		
		// main panel
		{	mainPanel = new TableSubPanel(getDataWidth(),mainPanelHeight,Mode.NOTHING,1,1,true);
			dataPanel.add(mainPanel);
		}
		
		dataPanel.add(Box.createRigidArea(new Dimension(GuiSizeTools.subPanelMargin,GuiSizeTools.subPanelMargin)));
		
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
				label.setBackground(GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND);
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
			buttonsPanel.add(Box.createRigidArea(new Dimension(GuiSizeTools.subPanelMargin,GuiSizeTools.subPanelMargin)));
			// type
			{	MyLabel label = new MyLabel();
				label.setOpaque(true);
				label.setBackground(GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND);
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
			buttonsPanel.add(Box.createRigidArea(new Dimension(GuiSizeTools.subPanelMargin,GuiSizeTools.subPanelMargin)));
			// ranks
			{	MyLabel label = new MyLabel();
				label.setOpaque(true);
				label.setBackground(GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND);
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
			buttonsPanel.add(Box.createRigidArea(new Dimension(GuiSizeTools.subPanelMargin,GuiSizeTools.subPanelMargin)));
			// sum/mean
			{	MyLabel label = new MyLabel();
				label.setOpaque(true);
				label.setBackground(GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND);
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
			buttonsPanel.add(Box.createRigidArea(new Dimension(GuiSizeTools.subPanelMargin,GuiSizeTools.subPanelMargin)));
			// down
			{	MyLabel label = new MyLabel();
				label.setOpaque(true);
				label.setBackground(GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND);
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
	// TABLE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List of player ids */
	private List<String> playersIds;
	/** Map of player profiles */
	private HashMap<String,Profile> profilesMap;
	/** List of player data */
	private Map<String,List<Comparable<?>>> playersScores = new HashMap<String, List<Comparable<?>>>();
	/** Page currently displayed */
	private int currentPage = 0;
	/** List of panels constituting the table */
	private final List<TableSubPanel> listPanels = new ArrayList<TableSubPanel>();
	/** Number of pages in the table */
	private int pageCount;
	/** Number of rows in the table */
	private int lines;
	/** Main panel for the table */
	private TableSubPanel mainPanel;

	/**
	 * Returns the map of profiles
	 * for all the players displayed in
	 * the table.
	 * 
	 * @return
	 * 		Map of profiles.
	 */
	public HashMap<String,Profile> getPlayersProfiles()
	{	return profilesMap;	
	}
	
	/**
	 * Returns the ids of the players
	 * displayed in the table.
	 * 
	 * @return
	 * 		A list of ids.
	 */
	public List<String> getPlayersIds()
	{	return playersIds;	
	}
	
	/**
	 * Changes the data displayed by this panel.
	 * The table is upadated.
	 * 
	 * @param profilesMap
	 * 		Map of player profiles.
	 * @param lines
	 * 		Number of rows in the table.
	 */
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
				List<Comparable<?>> list1 = playersScores.get(playerId1);
				List<Comparable<?>> list2 = playersScores.get(playerId2);
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
		Map<String,PlayerStats> playersStats = GameStatistics.getPlayersStats();
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
					int alpha = GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
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
			{	colWidths[colName] = getDataWidth() - (cols-1)*GuiSizeTools.subPanelMargin;
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
	
	/**
	 * Updates the table panel.
	 */
	private void refreshList()
	{	EmptyContentPanel dataPanel = getDataPanel();
		
		// remove the old panel
		int index = GuiMiscTools.indexOfComponent(dataPanel,mainPanel);
		dataPanel.remove(index);
		
		// put the new one
		mainPanel = listPanels.get(currentPage);
		dataPanel.add(mainPanel,index);
		
		// refresh
		validate();
		repaint();
	}
	
	/**
	 * Resets and redraws this panel.
	 */
	public void refresh()
	{	setPlayersIds(profilesMap,lines);
	}

	/////////////////////////////////////////////////////////////////
	// BUTTONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Column of the 'previous page' button */
	private final static int COL_PREVIOUS = 0;
	/** Column of the 'player type' button */
	private final static int COL_TYPE = 2;
	/** Column of the 'ranked-only/all players' button */
	private final static int COL_RANKS = 4;
	/** Column of the 'sum/mean' button */
	private final static int COL_SUM_MEAN = 6;
	/** Column of the 'next page' button */
	private final static int COL_NEXT = 8;
	/** Panel containing the buttons */
	private JPanel buttonsPanel;

	/////////////////////////////////////////////////////////////////
	// COLUMNS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List of columns composing the table */
	private List<StatisticColumn> columns = new ArrayList<StatisticColumn>();
	
	/**
	 * Change the columns composing the table.
	 * 
	 * @param columns
	 * 		New list of columns.
	 */
	public void setColumns(List<StatisticColumn> columns)
	{	if(columns==null)
			columns = new ArrayList<StatisticColumn>();
		this.columns = columns;
		refresh();
	}
	
	/////////////////////////////////////////////////////////////////
	// SORT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Column used to sort the table rows */
	private StatisticColumn sortCriterion = null;
	/** Whether the sort should be natural or inverted */
	private boolean inverted = false;
	/** Whether sums or means should be displayed */
	private boolean mean = false;
	/** Which type of players should be displayed */
	private Type type = Type.BOTH;
	/** Whether only ranked or all players should be displayed */
	private Ranks ranks = Ranks.ALL;
	
	/**
	 * Indicates if the mean or sums
	 * should be displayed.
	 * 
	 * @return
	 * 		{@code true} if means should be displayed.
	 */
	public boolean hasMean()
	{	return mean;
	}	
	
	/**
	 * Change the column used
	 * to sort the rows.
	 * 
	 * @param sort
	 * 		New sort criterion.
	 */
	public void setSort(StatisticColumn sort)
	{	if(sortCriterion==sort)
			inverted = !inverted;		
		else
		{	inverted = false;
			sortCriterion = sort;
		}
		refresh();
	}
	
	/**
	 * Type of players
	 * displayed in the table.
	 * 
	 * @author Vincent Labatut
	 */
	private enum Type
	{	/** Human players only */
		HUMAN,
		/** Artificial intelligences only */
		AI,
		/** All players */
		BOTH;
		
		/**
		 * Cycle through all {@code Type}
		 * values.
		 * 
		 * @return
		 * 		The next {@code Type} value.
		 */
		public Type getNext()
		{	Type[] values = Type.values();
			int index = (this.ordinal()+1)%values.length;
			Type result = values[index];
			return result;
		}
	}
	
	/**
	 * Filter players according to
	 * their rank/no rank.
	 * 
	 * @author Vincent Labatut
	 */
	private enum Ranks
	{	/** All players */
		ALL,
		/** Only ranked players */
		ALL_RANKS,
		/** Only non-ranked players */
		NO_RANKS;
	
		/**
		 * Cycle through all {@code Ranks}
		 * values.
		 * 
		 * @return
		 * 		The next {@code Ranks} value.
		 */
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
	{	//
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{	//
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{	//
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{	MyLabel label = (MyLabel)e.getComponent();
		int pos = GuiMiscTools.indexOfComponent(buttonsPanel,label);
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
			{	String buttonKey = label.getKey();
				// register/unregister
				if(buttonKey.equals(GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_REGISTER) || buttonKey.equals(GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_UNREGISTER))
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
				else if(buttonKey.equals(GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_SELECT) || buttonKey.equals(GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_UNSELECT))
				{	String playerId = playersIds.get((currentPage*lines)+p[0]-1);
					Map<String,PlayerStats> playersStats = GameStatistics.getPlayersStats();
					PlayerStats playerStats = playersStats.get(playerId);
					if(playerStats.isSelected())
						playerStats.setSelectedColor(null);
					else
					{	// check number of selected players
						int selectedNbr = 0;
						for(PlayerStats playerStat: playersStats.values())
						{	PredefinedColor c = playerStat.getSelectedColor();
							if(c!=null)
								selectedNbr++;
						}
						if(selectedNbr<GameData.MAX_PROFILES_COUNT)
						{	// select player
							Profile profile = profilesMap.get(playerId);
							PredefinedColor color = profile.getSpriteColor();
							if(!isFreeColor(color))
								color = getNextFreeColor(color);
							playerStats.setSelectedColor(color);
						}
					}
					// 
					refresh();
				}
			}
			else
			{	String buttonKey = label.getKey();
			
				if(buttonKey.equals(GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_COLOR))
				{	String playerId = playersIds.get((currentPage*lines)+p[0]-1);
					Map<String,PlayerStats> playersStats = GameStatistics.getPlayersStats();
					PlayerStats playerStats = playersStats.get(playerId);
					PredefinedColor color = playerStats.getSelectedColor();
					if(color!=null)
					{	color = getNextFreeColor(color);
						playerStats.setSelectedColor(color);
						refresh();
					}
				}
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{	//
	}
	
	/////////////////////////////////////////////////////////////////
	// COLORS								/////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the next color not already used (by a selected player),
	 * using the specified color as a starting point.
	 * 
	 * @param color
	 * 		Starting point (for cycling).
	 * @return
	 * 		Next free color.
	 */
	public PredefinedColor getNextFreeColor(PredefinedColor color)
	{	PredefinedColor result = null;
		
		// used colors
		Map<String,PlayerStats> playersStats = GameStatistics.getPlayersStats();
		List<PredefinedColor> usedColors = new ArrayList<PredefinedColor>();
		for(PlayerStats p: playersStats.values())
		{	PredefinedColor clr = p.getSelectedColor();
			if(clr!=null)
				usedColors.add(clr);
		}
		
		// preferred colors
		List<PredefinedColor> preferredColors = new ArrayList<PredefinedColor>();
		for(PredefinedColor c: PredefinedColor.values())
		{	if(c==color || (!usedColors.contains(c) && !preferredColors.contains(c)))
				preferredColors.add(c);
		}
		
		// select a color
		int currentColorIndex = preferredColors.indexOf(color);
		int index = (currentColorIndex+1) % preferredColors.size();
		if(index<preferredColors.size())
			result = preferredColors.get(index);
		
		return result;
	}

	/**
	 * Determines if the specified color
	 * is already used by another selected player.
	 * 
	 * @param color
	 * 		Color to be checked.
	 * @return
	 * 		{@code true} iff the color is not currently used
	 * 		by any other selected player. 
	 */
	public boolean isFreeColor(PredefinedColor color)
	{	boolean result = true;
		Map<String,PlayerStats> playersStats = GameStatistics.getPlayersStats();
		Iterator<PlayerStats> it = playersStats.values().iterator();
		while(it.hasNext() && result)
		{	PlayerStats p = it.next();
			PredefinedColor clr = p.getSelectedColor();
			result = clr!=color;
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// DISPLAY								/////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Adds a new column to the table.
	 * 
	 * @param index 
	 * 		Position of the new column.
	 * @param column 
	 * 		New column to insert in the table.
	 * 
	 */
	public void addColumn(int index, StatisticColumn column)
	{	columns.add(index,column);
		refresh();
	}

	/**
	 * Changes one of the column of the table.
	 * 
	 * @param index
	 * 		Position of the column to be exchanged.
	 * @param column
	 * 		Column used for the substitution.
	 */
	public void setColumn(int index, StatisticColumn column)
	{	columns.set(index,column);
		refresh();
	}

	/**
	 * Removes one column from the table.
	 * 
	 * @param index
	 * 		Position of the column to remove.
	 */
	public void removeColumn(int index)
	{	columns.remove(index);
		refresh();
	}
}

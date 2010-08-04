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

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.game.network.game.GameInfo;
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
public class GameListSubPanel extends EmptySubPanel implements MouseListener
{	private static final long serialVersionUID = 1L;
	private final static int COL_PREVIOUS = 0;
	private final static int COL_NEXT = 2;
	
	public GameListSubPanel(int width, int height)
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
		int buttonWidth = (getDataWidth() - 1*GuiTools.subPanelMargin)/2;
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
				Dimension dim = new Dimension(buttonWidth,buttonHeight);
				label.setMinimumSize(dim);
				label.setPreferredSize(dim);
				label.setMaximumSize(dim);
				String key = GuiKeys.COMMON_GAME_LIST_BUTTON_PREVIOUS;
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
				Dimension dim = new Dimension(buttonWidth,buttonHeight);
				label.setMinimumSize(dim);
				label.setPreferredSize(dim);
				label.setMaximumSize(dim);
				String key = GuiKeys.COMMON_GAME_LIST_BUTTON_NEXT;
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
		sortCriterion = GameColumn.PLAYED;
		setGameInfos(null,16);
	}
	
	/////////////////////////////////////////////////////////////////
	// PAGES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<String> gamesIds;
	private HashMap<String,GameInfo> gamesMap;
	@SuppressWarnings("rawtypes")
	private HashMap<String,List<Comparable>> valuesMap = new HashMap<String, List<Comparable>>();
	private int currentPage = 0;
	private final List<TableSubPanel> listPanels = new ArrayList<TableSubPanel>();
	private int pageCount;	
	private int lines;
	private TableSubPanel mainPanel;
	private JPanel buttonsPanel;
	
	public HashMap<String,GameInfo> getGameInfos()
	{	return gamesMap;
	}
	
	public void setGameInfos(HashMap<String,GameInfo> gamesMap, int lines)
	{	// init
		this.lines = lines;
		lines++;
		if(gamesMap==null)
			gamesMap = new HashMap<String,GameInfo>();
		this.gamesMap = gamesMap;
		listPanels.clear();
		
		// sorting players
		sortCriterion.updateValues(this,valuesMap,gamesMap);
		gamesIds = new ArrayList<String>(gamesMap.keySet());
		Collections.sort(gamesIds,new Comparator<String>()
		{	@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public int compare(String playerId1, String playerId2)
			{	int result = 0;
				List<Comparable> list1 = valuesMap.get(playerId1);
				List<Comparable> list2 = valuesMap.get(playerId2);
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
		{	List<String> temp = gamesIds;
			gamesIds = new ArrayList<String>();
			for(String i: temp)
				gamesIds.add(0,i);
		}
		
		// cols
		int cols = columns.size();
		String headerKeys[] = new String[cols];
		int colWidths[] = new int[cols];
		for(int col=0;col<columns.size();col++)
		{	GameColumn rc = columns.get(col);
			headerKeys[col] = rc.getHeaderKey();
			colWidths[col] = 0;
		}
		
		// building all pages
		pageCount = 0;
		int headerHeight = 0;
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

			// data: process each game id
			int line = 1;
			while(line<lines && idIndex<gamesIds.size())
			{	// init
				String gameId = gamesIds.get(idIndex);
				idIndex++;
				GameInfo gameInfo = gamesMap.get(gameId);
				// columns
				for(int col=0;col<cols;col++)
				{	GameColumn column = columns.get(col);
					column.setLabelContent(this,panel,colWidths,line,col,gameInfo);
				}
				// next line
				line++;
			}
						
			// add panel to list
			if(line>1 || pageCount==0)
			{	listPanels.add(panel);
				pageCount++;
			}
		}
		while(idIndex<gamesIds.size());
		
		// col widths
		{	int colName = columns.indexOf(GameColumn.HOST_NAME);
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
	{	setGameInfos(gamesMap,lines);
	}

	/////////////////////////////////////////////////////////////////
	// COLUMNS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<GameColumn> columns = new ArrayList<GameColumn>();
	
	public void setColumns(List<GameColumn> columns)
	{	if(columns==null)
			columns = new ArrayList<GameColumn>();
		this.columns = columns;
		refresh();
	}
	
	/////////////////////////////////////////////////////////////////
	// SORT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private GameColumn sortCriterion = null;
	private boolean inverted = false;
	
	public void setSort(GameColumn sort)
	{	if(sortCriterion==sort)
			inverted = !inverted;		
		else
		{	inverted = false;
			sortCriterion = sort;
		}
		refresh();
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
			{	GameColumn rc = columns.get(p[1]);
				if(rc!=null)
					setSort(rc);
			}
			// preferred
			else if(p[1]==0)
			{	
				String gameId = gamesIds.get((currentPage*lines)+p[0]-1);
				GameInfo gameInfo = 
				
/**
 * TODO
 * 	- ici : faut juste changer le statut de favori et changer l'affichage ?
 * 		>> non, dans tous les cas faut émettre car on ne sait pas de quelle liste il s'agit
 * 			donc niveau config ça pourrait être n'importe quoi
 *      >> en fait, vu qu'on ne fait plus de liste de favoris, ça serait
 *      surement mieux de mettre les boutons dans la table comme pour les stats des joueurs
 *      >> non car à cause des subpanels annexes, faut sortir de cette classe pr faire une màj de toute façon
 *  - émettre un evt pr la suppression
 *  - général: dans cette classe, faut gérer la sélection de game (ligne)
 */
			}
			// add/remove
			else if(p[1]==2)
			{	
				// TODO add/remove from the direct connexion list
				
				String gameId = gamesIds.get((currentPage*lines)+p[0]-1);
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
	public void addColumn(int index, GameColumn column)
	{	columns.add(index,column);
		refresh();
	}

	public void setColumn(int index, GameColumn column)
	{	columns.set(index,column);
		refresh();
	}

	public void removeColumn(int index)
	{	columns.remove(index);
		refresh();
	}
}

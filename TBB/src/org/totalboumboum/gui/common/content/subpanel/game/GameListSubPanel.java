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
import java.util.Iterator;
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
import org.totalboumboum.game.network.host.HostInfo;
import org.totalboumboum.game.tournament.cup.CupLeg;
import org.totalboumboum.game.tournament.cup.CupPart;
import org.totalboumboum.game.tournament.cup.CupTournament;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.content.subpanel.leg.LegSubPanelListener;
import org.totalboumboum.gui.common.content.subpanel.part.PartSubPanel;
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
	private final static int COL_DISPLAY = 2;
	private final static int COL_NEXT = 4;
	
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
		int regularButtonWidth = (getDataWidth() - 2*GuiTools.subPanelMargin)/3;
		int centerButtonWidth = getDataWidth()- 2*regularButtonWidth - 2*GuiTools.subPanelMargin;
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
				String key = GuiKeys.COMMON_GAME_LIST_BUTTON_PREVIOUS;
				label.setKey(key,true);
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setVerticalAlignment(JLabel.CENTER);
				label.addMouseListener(this);
				label.setMouseSensitive(true);
				buttonsPanel.add(label);
			}
			buttonsPanel.add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)));
			// central
			{	MyLabel label = new MyLabel();
				label.setOpaque(true);
				label.setBackground(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
				Dimension dim = new Dimension(centerButtonWidth,buttonHeight);
				label.setMinimumSize(dim);
				label.setPreferredSize(dim);
				label.setMaximumSize(dim);
				String key = GuiKeys.COMMON_GAME_LIST_BUTTON_CENTRAL;
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
	private String selectedId = null;
	private int colWidths[];
	
	public HashMap<String,GameInfo> getGameInfos()
	{	return gamesMap;
	}
	
	public List<String> getGamesIds()
	{	return gamesIds;	
	}
		
	public void setGameInfos(HashMap<String,GameInfo> gamesMap, int lines)
	{	// init
		this.lines = lines;
		lines++;
		if(gamesMap==null)
			gamesMap = new HashMap<String,GameInfo>();
		this.gamesMap = gamesMap;
		listPanels.clear();
		
		// sorting games
		sortCriterion.updateValues(valuesMap,gamesMap);
		gamesIds = new ArrayList<String>(valuesMap.keySet());
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
		colWidths = new int[cols];
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
		selectGame(selectedId);
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
	
	public GameInfo getSelectedGame()
	{	GameInfo result = null;
		if(selectedId!=null)
			result = gamesMap.get(selectedId);
		return result;
	}

	private void selectGame(String gameId)
	{	// init
		int index;
		boolean fire = gameId==selectedId;
	
		// unselect
		if(selectedId!=null)
		{	// get line
			index = gamesIds.indexOf(selectedId);
			int page = index/lines;
			int line = index%lines;
			// change color
			TableSubPanel panel = listPanels.get(page);
			Color bg = GuiTools.COLOR_COMMON_BACKGROUND;
			panel.setLineBackground(line,bg);
			// update selected id
			selectedId = null;
		}
			
		//select
		if(gameId!=null)
		{	// update selected id
			selectedId = gameId;
			// get page
			index = gamesIds.indexOf(selectedId);
			int page = index/lines;
			// refresh page
			if(page!=currentPage)
			{	currentPage = page;
				refreshList();
			}
			// get line
			int line = index%lines;
			// change color
			TableSubPanel panel = listPanels.get(page);
			Color bg = GuiTools.COLOR_TABLE_SELECTED_PALE_BACKGROUND;
			panel.setLineBackground(line,bg);
		}
		
		// update listeners
		if(fire)
			fireGameSelectionChanged(gameId);
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
			// switch type
			else if(pos==COL_DISPLAY)
			{	String key;
				DisplayMode dm;
				if(displayMode.equals(DisplayMode.CENTRAL))
				{	dm = DisplayMode.DIRECT;
					key = GuiKeys.COMMON_GAME_LIST_BUTTON_DIRECT;
				}
				else
				{	dm = DisplayMode.CENTRAL;
					key = GuiKeys.COMMON_GAME_LIST_BUTTON_CENTRAL;
				}
				if(selectedId!=null)
				{	GameInfo gameInfo = gamesMap.get(selectedId);
					if(!gameInfo.getHostInfo().getType().equals(dm))
						selectGame(null);
				}
				MyLabel lbl = (MyLabel)buttonsPanel.getComponent(COL_DISPLAY);
				lbl.setKey(key,true);
				setDisplayMode(dm);
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
			else if(p[1]==(columns.indexOf(GameColumn.PREFERRED)*2))
			{	// change preference
				String gameId = gamesIds.get((currentPage*lines)+p[0]-1);
				GameInfo gameInfo = gamesMap.get(gameId);
				HostInfo hostInfo = gameInfo.getHostInfo();
				boolean preferred = hostInfo.isPreferred();
				hostInfo.setPreferred(!preferred);
				// change icon
				TableSubPanel panel = listPanels.get(currentPage);
				GameColumn.PREFERRED.setLabelContent(this,panel,colWidths,p[0],p[1],gameInfo);
//TODO definie a specific function to update a GameInfo, which will fire the adapted event				
				// fire GUI event
				fireGameLineModified(gameInfo);
				
/**
 * TODO
 * 	- ici : faut juste changer le statut de favori et changer l'affichage ?
 * 		>> non, dans tous les cas faut �mettre car on ne sait pas de quelle liste il s'agit
 * 			donc niveau config �a pourrait �tre n'importe quoi
 *      >> en fait, vu qu'on ne fait plus de liste de favoris, �a serait
 *      surement mieux de mettre les boutons dans la table comme pour les stats des joueurs
 *  - �mettre un evt pr la suppression
 *  - g�n�ral: dans cette classe, faut g�rer la s�lection de game (ligne)
 */
			}
			// add/remove
			else if(p[1]==(columns.indexOf(GameColumn.BUTTON)*2))
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
			// select line
			else if(p[1]==(columns.indexOf(GameColumn.HOST_NAME)*2))
			{	String gameId = gamesIds.get((currentPage*lines)+p[0]-1);
				selectGame(gameId);
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
	private DisplayMode displayMode = DisplayMode.CENTRAL;
	
	private enum DisplayMode
	{	CENTRAL,
		DIRECT;
	}
	
	public void setDisplayMode(DisplayMode displayMode)
	{	this.displayMode = displayMode;
		
		if(displayMode.equals(DisplayMode.CENTRAL))
		{	columns.clear();
			columns.add(GameColumn.PREFERRED);
			columns.add(GameColumn.HOST_NAME);
			columns.add(GameColumn.TOURNAMENT_TYPE);
			columns.add(GameColumn.PLAYER_COUNT);
			columns.add(GameColumn.TOURNAMENT_STATE);
		}
		else if(displayMode.equals(DisplayMode.DIRECT))
		{	columns.clear();
			columns.add(GameColumn.PREFERRED);
			columns.add(GameColumn.BUTTON);
			columns.add(GameColumn.HOST_NAME);
			columns.add(GameColumn.TOURNAMENT_TYPE);
			columns.add(GameColumn.PLAYER_COUNT);
			columns.add(GameColumn.TOURNAMENT_STATE);
		}
		
		refresh();
	}

/*	
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

	public boolean isShowAddButton()
	{	boolean result = false;
		Iterator<GameColumn> col = columns.iterator();
		while(!result && col.hasNext())
		{	GameColumn c = col.next();
			result = c==GameColumn.BUTTON;
		}
		return result;
	}
*/	

	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<GameListSubPanelListener> listeners = new ArrayList<GameListSubPanelListener>();
	
	public void addListener(GameListSubPanelListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);		
	}

	public void removeListener(GameListSubPanelListener listener)
	{	listeners.remove(listener);		
	}
	
	private void fireGameSelectionChanged(String gameId)
	{	for(GameListSubPanelListener listener: listeners)
			listener.gameSelectionChanged(gameId);
	}

	private void fireGameLineModified(GameInfo gameInfo)
	{	for(GameListSubPanelListener listener: listeners)
			listener.gameLineModified(gameInfo);
	}

	private void fireGameBeforeClicked()
	{	for(GameListSubPanelListener listener: listeners)
			listener.gameBeforeClicked();
	}

	private void fireGameAfterClicked()
	{	for(GameListSubPanelListener listener: listeners)
			listener.gameAfterClicked();
	}
}

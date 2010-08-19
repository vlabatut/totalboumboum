package org.totalboumboum.gui.menus.network;

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

import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.totalboumboum.game.network.game.GameInfo;
import org.totalboumboum.gui.common.content.subpanel.game.GameInfoSubPanel;
import org.totalboumboum.gui.common.content.subpanel.game.GameListSubPanel;
import org.totalboumboum.gui.common.content.subpanel.game.GameListSubPanelListener;
import org.totalboumboum.gui.common.content.subpanel.host.HostInfoSubPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.common.structure.subpanel.BasicPanel;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class GamesData extends EntitledDataPanel implements GameListSubPanelListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.6f;
	private final int GAME_LIST_LINES = 16;
	
	public GamesData(SplitMenuPanel container)
	{	super(container);
		HashMap<String,GameInfo> gamesMap = new HashMap<String, GameInfo>(); //TODO 
		
		// title
		{	String key = GuiKeys.MENU_NETWORK_GAMES_TITLE;
			setTitleKey(key);
		}
		
		// data
		{	BasicPanel mainPanel = new BasicPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(mainPanel,BoxLayout.LINE_AXIS); 
				mainPanel.setLayout(layout);
			}
			
			int margin = GuiTools.panelMargin;
			int leftWidth = (int)(dataWidth*SPLIT_RATIO); 
			int rightWidth = dataWidth - leftWidth - margin; 
			mainPanel.setOpaque(false);
			
			// game list panel
			{	listPanel = new GameListSubPanel(leftWidth,dataHeight);
				listPanel.setGameInfos(gamesMap,GAME_LIST_LINES);
				listPanel.addListener(this);
				mainPanel.add(listPanel);
			}
			
			mainPanel.add(Box.createHorizontalGlue());
			
			// right panel
			{	JPanel rightPanel = new JPanel();
				{	BoxLayout layout = new BoxLayout(rightPanel,BoxLayout.PAGE_AXIS); 
					rightPanel.setLayout(layout);
				}
				rightPanel.setOpaque(false);
				Dimension dim = new Dimension(rightWidth,dataHeight);
				rightPanel.setPreferredSize(dim);
				rightPanel.setMinimumSize(dim);
				rightPanel.setMaximumSize(dim);
				int upHeight = (dataHeight - margin)/2;
				int downHeight = dataHeight - upHeight - margin;
				
				// host panel
				{	hostPanel = new HostInfoSubPanel(rightWidth,upHeight);
					//hostPanel.addListener(this);
					rightPanel.add(hostPanel);
				}

				rightPanel.add(Box.createVerticalGlue());
				
				// game panel
				{	gamePanel = new GameInfoSubPanel(rightWidth,downHeight);
					rightPanel.add(gamePanel);
				}
				
				mainPanel.add(rightPanel);
			}

			setDataPart(mainPanel);
		}
	}
		
	/////////////////////////////////////////////////////////////////
	// PANELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	private GameListSubPanel listPanel;
	private GameInfoSubPanel gamePanel;
	private HostInfoSubPanel hostPanel;

	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
	{	// TODO useless here (?)
	}
	
	/////////////////////////////////////////////////////////////////
	// GAME LIST SUBPANEL LISTENER		/////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void gameSelectionChanged(String gameId)
	{	
	}

	@Override
	public void gameLineModified(GameInfo gameInfo)
	{	
	}

	@Override
	public void gameBeforeClicked()
	{	
	}

	@Override
	public void gameAfterClicked()
	{	
	}
}

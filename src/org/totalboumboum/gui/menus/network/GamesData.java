package org.totalboumboum.gui.menus.network;

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

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.connections.ConnectionsConfiguration;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.content.subpanel.game.GameInfoSubPanel;
import org.totalboumboum.gui.common.content.subpanel.game.GameListSubPanel;
import org.totalboumboum.gui.common.content.subpanel.game.GameListSubPanelListener;
import org.totalboumboum.gui.common.content.subpanel.host.HostInfoSubPanel;
import org.totalboumboum.gui.common.content.subpanel.host.HostInfoSubPanelListener;
import org.totalboumboum.gui.common.structure.dialog.outside.InputModalDialogPanel;
import org.totalboumboum.gui.common.structure.dialog.outside.ModalDialogPanelListener;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.common.structure.subpanel.BasicPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.gui.tools.GuiImageTools;
import org.totalboumboum.stream.network.client.ClientGeneralConnection;
import org.totalboumboum.stream.network.client.ClientGeneralConnectionListener;
import org.totalboumboum.stream.network.client.ClientIndividualConnection;
import org.totalboumboum.stream.network.data.game.GameInfo;
import org.totalboumboum.stream.network.data.host.HostInfo;
import org.totalboumboum.stream.network.data.host.HostState;
import org.totalboumboum.tools.network.NetworkTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class GamesData extends EntitledDataPanel implements GameListSubPanelListener,ModalDialogPanelListener,ClientGeneralConnectionListener,HostInfoSubPanelListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.6f;
	private final int GAME_LIST_LINES = 16;
	
	public GamesData(SplitMenuPanel container)
	{	super(container);
	
		ConnectionsConfiguration config = Configuration.getConnectionsConfiguration();
		try
		{	config.initClientConnection();
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
		ClientGeneralConnection generalConnection = config.getClientConnection();
		generalConnection.addListener(this);
		List<GameInfo> gamesList = generalConnection.getGameList();
		gamesMap = new HashMap<String, GameInfo>();
		for(GameInfo gi: gamesList)
			gamesMap.put(gi.getHostInfo().getId(),gi);
/*		
try
{	HostInfo hostInfo = new HostInfo();
	hostInfo.setCentral(true);
	hostInfo.setDirect(true);
	hostInfo.setId("132456");
	hostInfo.setLastIp("127.0.0.1");
	hostInfo.setName("example1");
	hostInfo.setPreferred(true);
	hostInfo.setState(HostState.OPEN);
	hostInfo.setUses(213);
	GameInfo gameInfo = new GameInfo();
	gameInfo.setAllowedPlayers(new TreeSet<Integer>(Arrays.asList(1,2,3,4,5)));
	gameInfo.setAverageScore(1234.56);
	gameInfo.setHostInfo(hostInfo);
	gameInfo.setPlayerCount(2);
	gameInfo.setTournamentName("My Tournament 1");
	gameInfo.setTournamentType(TournamentType.CUP);
	gamesMap.put(gameInfo.getHostInfo().getId(),gameInfo);
	
	hostInfo = new HostInfo();
	hostInfo.setCentral(false);
	hostInfo.setDirect(true);
	hostInfo.setId("321456");
	hostInfo.setLastIp("127.0.0.2");
	hostInfo.setName("example2");
	hostInfo.setPreferred(true);
	hostInfo.setState(HostState.PLAYING);
	hostInfo.setUses(456);
	gameInfo = new GameInfo();
	gameInfo.setAllowedPlayers(new TreeSet<Integer>(Arrays.asList(4,5)));
	gameInfo.setAverageScore(1893.43);
	gameInfo.setHostInfo(hostInfo);
	gameInfo.setPlayerCount(4);
	gameInfo.setTournamentName("My Tournament 2");
	gameInfo.setTournamentType(TournamentType.LEAGUE);
	gamesMap.put(gameInfo.getHostInfo().getId(),gameInfo);

	hostInfo = new HostInfo();
	hostInfo.setCentral(true);
	hostInfo.setDirect(true);
	hostInfo.setId("285259");
	hostInfo.setLastIp("127.0.0.3");
	hostInfo.setName("example3");
	hostInfo.setPreferred(false);
	hostInfo.setState(HostState.FINISHED);
	hostInfo.setUses(52);
	gameInfo = new GameInfo();
	gameInfo.setAllowedPlayers(new TreeSet<Integer>(Arrays.asList(1,2,4)));
	gameInfo.setAverageScore(985.27);
	gameInfo.setHostInfo(hostInfo);
	gameInfo.setPlayerCount(2);
	gameInfo.setTournamentName("My Tournament 3");
	gameInfo.setTournamentType(TournamentType.SEQUENCE);
	gamesMap.put(gameInfo.getHostInfo().getId(),gameInfo);

	hostInfo = new HostInfo();
	hostInfo.setCentral(true);
	hostInfo.setDirect(true);
	hostInfo.setId("741285");
	hostInfo.setLastIp("127.0.0.4");
	hostInfo.setName("example4");
	hostInfo.setPreferred(false);
	hostInfo.setState(HostState.CLOSED);
	hostInfo.setUses(18);
	gameInfo = new GameInfo();
	gameInfo.setAllowedPlayers(new TreeSet<Integer>(Arrays.asList(1,2,3,4)));
	gameInfo.setAverageScore(1287.97);
	gameInfo.setHostInfo(hostInfo);
	gameInfo.setPlayerCount(1);
	gameInfo.setTournamentName("My Tournament 4");
	gameInfo.setTournamentType(TournamentType.SINGLE);
	gamesMap.put(gameInfo.getHostInfo().getId(),gameInfo);

	hostInfo = new HostInfo();
	hostInfo.setCentral(true);
	hostInfo.setDirect(true);
	hostInfo.setId("258417");
	hostInfo.setLastIp("127.0.0.5");
	hostInfo.setName("example5");
	hostInfo.setPreferred(false);
	hostInfo.setState(HostState.UNKOWN);
	hostInfo.setUses(1856);
	gameInfo = new GameInfo();
	gameInfo.setAllowedPlayers(new TreeSet<Integer>(Arrays.asList(1,2,3,4,5)));
	gameInfo.setAverageScore(1111.22);
	gameInfo.setHostInfo(hostInfo);
	gameInfo.setPlayerCount(3);
	gameInfo.setTournamentName("My Tournament 5");
	gameInfo.setTournamentType(TournamentType.CUP);
	gamesMap.put(gameInfo.getHostInfo().getId(),gameInfo);
}
catch (UnknownHostException e)
{	e.printStackTrace();
}
*/		
		// title
		{	String key = GuiKeys.MENU_NETWORK_GAMES_TITLE;
			setTitleKey(key);
		}
		
		// data
		{	BasicPanel mainPanel = new BasicPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(mainPanel,BoxLayout.LINE_AXIS); 
				mainPanel.setLayout(layout);
			}
			
			int margin = GuiSizeTools.panelMargin;
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
					hostPanel.addListener(this);
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
			generalConnection.requestGameInfos();
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// GAMES MAP		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	private HashMap<String,GameInfo> gamesMap;
	
	public GameInfo getSelectedGame()
	{	GameInfo result = listPanel.getSelectedGame();
		return result;
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
	{	// useless here (?)
	}
	
	/////////////////////////////////////////////////////////////////
	// GAME LIST SUBPANEL LISTENER		/////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void gameSelectionChanged(String gameId)
	{	GameInfo gameInfo = gamesMap.get(gameId);
		gamePanel.setGameInfo(gameInfo);
		HostInfo hostInfo = null;
		if(gameInfo!=null)
		{	hostInfo = gameInfo.getHostInfo();
			ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
			connection.refreshConnection(gameInfo);
		}
		hostPanel.setHostInfo(hostInfo);
		fireDataPanelSelectionChange(gameInfo);
	}

	@Override
	public void gameLineModified(GameInfo gameInfo)
	{	if(gamePanel!=null) //TODO this method should be synch'd
		{	GameInfo gi = gamePanel.getGameInfo();
			if(gameInfo==gi)
			{	gamePanel.setGameInfo(gameInfo);
				HostInfo hostInfo = gameInfo.getHostInfo();
				hostPanel.setHostInfo(hostInfo);
				//validate();
				//repaint();
				fireDataPanelSelectionChange(gameInfo);
			}
		}
	}

	@Override
	public void refreshGameRequested(GameInfo gameInfo)
	{	ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
		connection.refreshConnection(gameInfo);
	}

	@Override
	public void gameBeforeClicked()
	{	
	}

	@Override
	public void gameAfterClicked()
	{	
	}

	@Override
	public void gameAddClicked()
	{	String defaultText = "xxx.xxx.xxx.xxx:xxxx";
		String key = GuiKeys.MENU_NETWORK_GAMES_ADD_HOST_TITLE;
		List<String> text = new ArrayList<String>();
		text.add(GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_NETWORK_GAMES_ADD_HOST_TEXT));
		inputModalNew = new InputModalDialogPanel(container.getMenuParent(),key,text,defaultText);
		inputModalNew.addListener(this);
		getFrame().setModalDialog(inputModalNew);
	}

	/////////////////////////////////////////////////////////////////
	// MODAL DIALOG PANEL LISTENER	/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private InputModalDialogPanel inputModalNew = null;
	private InputModalDialogPanel inputModalSet = null;
	
	@Override
	public void modalDialogButtonClicked(String buttonCode)
	{	// new host
		if(inputModalNew!=null)
		{	String input = inputModalNew.getInput();
			if(buttonCode.equals(GuiKeys.COMMON_DIALOG_CANCEL))
			{	getFrame().unsetModalDialog();
				inputModalNew.removeListener(this);
				inputModalNew = null;
			}
			else if(buttonCode.equals(GuiKeys.COMMON_DIALOG_CONFIRM))
			{	if(NetworkTools.validateIPAddressPort(input))
				{	getFrame().unsetModalDialog();
					inputModalNew.removeListener(this);
					inputModalNew = null;
					// create new host
					ConnectionsConfiguration config = Configuration.getConnectionsConfiguration();
					HostInfo hostInfo = new HostInfo();
					String[] info = input.split(":");
					hostInfo.setLastIp(info[0]);
					hostInfo.setLastPort(Integer.parseInt(info[1]));
					ClientGeneralConnection connection = config.getClientConnection();
					connection.createConnection(hostInfo);
					
					// refresh the GUI
					//listPanel.refresh();
					//listPanel.selectGame(gameId);
					//getDataPart().refresh();
					//refreshButtons();
				}
			}
		}
		// simple address change
		else if(inputModalSet!=null)
		{	String input = inputModalSet.getInput();
			if(buttonCode.equals(GuiKeys.COMMON_DIALOG_CANCEL))
			{	getFrame().unsetModalDialog();
				inputModalSet.removeListener(this);
				inputModalSet = null;
			}
			else if(buttonCode.equals(GuiKeys.COMMON_DIALOG_CONFIRM))
			{	if(NetworkTools.validateIPAddressPort(input))
				{	getFrame().unsetModalDialog();
					inputModalSet.removeListener(this);
					inputModalSet = null;
					
					GameInfo gameInfo = gamePanel.getGameInfo();
					if(gameInfo!=null)
					{	HostInfo hostInfo = gameInfo.getHostInfo();
						// only if not already connected
						if(hostInfo.getState()==HostState.UNKOWN)
						{	String[] info = input.split(":");
							hostInfo.setLastIp(info[0]);
							hostInfo.setLastPort(Integer.parseInt(info[1]));
							ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
							connection.refreshConnection(gameInfo);

							// refresh the GUI
							listPanel.updateGame(gameInfo);
							hostPanel.setHostInfo(hostInfo);
							//getDataPart().refresh();
							//refreshButtons();
						}
					}
				}
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// CLIENT CONNECTION LISTENER	/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void connectionAdded(ClientIndividualConnection connection, int index)
	{	GameInfo gameInfo = connection.getGameInfo();
		String gameId = gameInfo.getHostInfo().getId();
		gamesMap.put(gameId,gameInfo);
		listPanel.setGameInfos(gamesMap,GAME_LIST_LINES);
		listPanel.selectGame(gameId);
	}

	@Override
	public void connectionRemoved(ClientIndividualConnection connection,int index)
	{	GameInfo gameInfo = connection.getGameInfo();
		String gameId = gameInfo.getHostInfo().getId();
		gamesMap.remove(gameId);
		listPanel.setGameInfos(gamesMap,GAME_LIST_LINES);
	}

	@Override
	public void connectionGameInfoChanged(ClientIndividualConnection connection, int index, String oldId)
	{	if(listPanel!=null)
		{	GameInfo gameInfo = connection.getGameInfo();
			if(oldId!=null)
			{	gamesMap.remove(oldId);
				String gameId = gameInfo.getHostInfo().getId();
				gamesMap.put(gameId,gameInfo);
				String selectedId = listPanel.getSelectedGame().getHostInfo().getId();
				listPanel.setGameInfos(gamesMap,GAME_LIST_LINES);
				if(selectedId==null || selectedId.equals(gameId))
					listPanel.selectGame(gameId);
			}
			else
				listPanel.updateGame(gameInfo);
		}
// unnecessary since the table is going to fire an event anyway		
//		GameInfo gi = gamePanel.getGameInfo();
//		if(gameInfo==gi)
//		{	gamePanel.setGameInfo(gameInfo);
//			HostInfo hostInfo = gameInfo.getHostInfo();
//			hostPanel.setHostInfo(hostInfo);
//			//validate();
//			//repaint();
//		}
	}

	@Override
	public void connectionActiveConnectionLost(ClientIndividualConnection connection, int index)
	{	// useless at this stage
	}

	@Override
	public void connectionProfilesChanged(ClientIndividualConnection connection, int index)
	{	// useless at this stage
	}

	@Override
	public void connectionTournamentStarted(AbstractTournament tournament)
	{	// useless at this stage
	}
	
	/////////////////////////////////////////////////////////////////
	// HOST INFO SUBPANEL LISTENER	/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void ipClicked()
	{	GameInfo gameInfo = gamePanel.getGameInfo();
		if(gameInfo!=null)
		{	HostInfo hostInfo = gameInfo.getHostInfo();
			if(hostInfo.getState()==HostState.UNKOWN)
			{	String defaultText = hostInfo.getLastIp()+":"+hostInfo.getLastPort();
				String key = GuiKeys.MENU_NETWORK_GAMES_ADD_HOST_TITLE;
				List<String> text = new ArrayList<String>();
				text.add(GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_NETWORK_GAMES_SET_HOST_TEXT));
				inputModalSet = new InputModalDialogPanel(container.getMenuParent(),key,text,defaultText);
				inputModalSet.addListener(this);
				getFrame().setModalDialog(inputModalSet);
			}
		}
	}
}

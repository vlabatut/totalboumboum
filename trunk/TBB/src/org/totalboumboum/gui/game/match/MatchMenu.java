package org.totalboumboum.gui.game.match;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.match.MatchRenderPanel;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.game.match.description.MatchDescription;
import org.totalboumboum.gui.game.match.results.MatchResults;
import org.totalboumboum.gui.game.match.statistics.MatchStatistics;
import org.totalboumboum.gui.game.round.RoundSplitPanel;
import org.totalboumboum.gui.game.save.SaveSplitPanel;
import org.totalboumboum.gui.game.tournament.TournamentSplitPanel;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.stream.network.client.ClientGeneralConnection;
import org.totalboumboum.stream.network.client.ClientGeneralConnectionListener;
import org.totalboumboum.stream.network.client.ClientIndividualConnection;
import org.totalboumboum.stream.network.client.ClientState;
import org.totalboumboum.stream.network.server.ServerGeneralConnection;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class MatchMenu extends InnerMenuPanel implements MatchRenderPanel,ClientGeneralConnectionListener
{	private static final long serialVersionUID = 1L;
		
	public MatchMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container,parent);
	
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.LINE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
		
		// sizes
		int buttonWidth = getHeight();
		int buttonHeight = getHeight();

		// buttons
		buttonQuit = GuiTools.createButton(GuiKeys.GAME_MATCH_BUTTON_QUIT,buttonWidth,buttonHeight,1,this);
		buttonSave = GuiTools.createButton(GuiKeys.GAME_MATCH_BUTTON_SAVE,buttonWidth,buttonHeight,1,this);
		buttonRecord = GuiTools.createToggleButton(GuiKeys.GAME_MATCH_BUTTON_RECORD_GAMES,buttonWidth,buttonHeight,1,this);
//buttonRecord.setEnabled(false);		
		add(Box.createHorizontalGlue());
		buttonTournament = GuiTools.createButton(GuiKeys.GAME_MATCH_BUTTON_CURRENT_TOURNAMENT,buttonWidth,buttonHeight,1,this);
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
	    ButtonGroup group = new ButtonGroup();
	    buttonDescription = GuiTools.createToggleButton(GuiKeys.GAME_MATCH_BUTTON_DESCRIPTION,buttonWidth,buttonHeight,1,this);
		buttonDescription.setSelected(true);
	    group.add(buttonDescription);
	    buttonResults = GuiTools.createToggleButton(GuiKeys.GAME_MATCH_BUTTON_RESULTS,buttonWidth,buttonHeight,1,this);
	    group.add(buttonResults);
	    buttonStatistics = GuiTools.createToggleButton(GuiKeys.GAME_MATCH_BUTTON_STATISTICS,buttonWidth,buttonHeight,1,this);
//buttonStatistics.setEnabled(false);		
	    group.add(buttonStatistics);
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		buttonRound = GuiTools.createButton(GuiKeys.GAME_MATCH_BUTTON_NEXT_ROUND,buttonWidth,buttonHeight,1,this);
		
		// panels
		{	matchDescription = new MatchDescription(container);
			container.setDataPart(matchDescription);
			matchResults = new MatchResults(container);
			matchStatistics = new MatchStatistics(container);		
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// MATCH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Match match;

	public void setMatch(Match match)
	{	// match
		if(match!=null)
		{	match.setPanel(null);
			List<Profile> profiles = match.getProfiles();
			List<Integer> controls = new ArrayList<Integer>();
			for(Profile p: profiles)
			{	int control = p.getControlSettingsIndex();
				if(controls.contains(control))
					p.setControlSettingsIndex(0);
				else
					controls.add(control);
			}
		}
		this.match = match;
		match.setPanel(this);
		
		// panels
		matchDescription.setMatch(match);
		matchResults.setMatch(match);
		matchStatistics.setMatch(match);	
		
		// buttons
		refreshButtons();
		
		// connection
		ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
		if(connection!=null)
			connection.addListener(this);
	}
	
	public Match getMatch()
	{	return match;	
	}
/*	
	private void saveTournament()
	{	AbstractTournament tournament = match.getTournament();
		TournamentConfiguration tournamentConfiguration = Configuration.getGameConfiguration().getTournamentConfiguration();
		AbstractTournament tournamentConf = tournamentConfiguration.getTournament();
		if(tournament==tournamentConf && tournamentConfiguration.getAutoSave())
		{	try
			{	// filenames
				String folderShort = FileTools.FOLDER_DEFAULT;
				String folder = FileTools.getSavesPath()+File.separator+folderShort;
				String backup = FileTools.getSavesPath()+File.separator+FileTools.FOLDER_DEFAULT_BACKUP;
				String dataFileName = FileTools.FILE_ARCHIVE+FileTools.EXTENSION_DATA;
				String xmlFileName = FileTools.FILE_ARCHIVE+FileTools.EXTENSION_XML;
				// backup
				File oldFile,newFile;
				oldFile = new File(folder,dataFileName);
				newFile = new File(backup,dataFileName);
				if(newFile.exists())
					newFile.delete();
				oldFile.renameTo(newFile);
				oldFile = new File(folder,xmlFileName);
				newFile = new File(backup,xmlFileName);
				if(newFile.exists())
					newFile.delete();
				oldFile.renameTo(newFile);
				// save
				GameArchive.saveGame(folderShort,tournament);
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
		}
	}
*/
	
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void quitTournament()
	{	// end match
		match.cancel();
		
		// end possible connection
		Configuration.getConnectionsConfiguration().terminateConnection();
		
		// set main menu frame
		getFrame().setMainMenuPanel();
    }

	/////////////////////////////////////////////////////////////////
	// BUTTONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	private JButton buttonQuit;
	private JButton buttonSave;
	private JToggleButton buttonRecord;
	private JButton buttonTournament;
	private JToggleButton buttonDescription;
	private JToggleButton buttonResults;
	private JToggleButton buttonStatistics;
	private JButton buttonRound;
	
	private Thread thread = null;
	
	public void refreshButtons()
	{	if(match!=null)
		{	if(match.isOver())
			{	// Round
				buttonRound.setEnabled(false);
				// Finish
				GuiTools.setButtonContent(GuiKeys.GAME_MATCH_BUTTON_FINISH, buttonTournament);
			}
			else
			{	// Round
				buttonRound.setEnabled(true);
				Round round = match.getCurrentRound();
				if(round==null || round.isOver())
					GuiTools.setButtonContent(GuiKeys.GAME_MATCH_BUTTON_NEXT_ROUND, buttonRound);
				else
					GuiTools.setButtonContent(GuiKeys.GAME_MATCH_BUTTON_CURRENT_ROUND, buttonRound);
				// Tournament
				GuiTools.setButtonContent(GuiKeys.GAME_MATCH_BUTTON_CURRENT_TOURNAMENT, buttonTournament);
			}
		}
		else
		{	// play
			buttonRound.setEnabled(false);
		}
	
		// record game
		ServerGeneralConnection serverConnection = Configuration.getConnectionsConfiguration().getServerConnection();
		ClientGeneralConnection clientConnection = Configuration.getConnectionsConfiguration().getClientConnection();
		boolean connectionState = serverConnection==null && clientConnection==null;
		buttonSave.setEnabled(connectionState);
		
		// record replay
		boolean recordGames = Configuration.getEngineConfiguration().isRecordRounds();
		buttonRecord.setSelected(recordGames);
	}
	
	public void autoAdvance()
	{	if(Configuration.getAisConfiguration().getAutoAdvance())
		{	// go to round
			if(buttonRound.isEnabled())
			{	thread = new Thread("TBB.autoadvance")
				{	public void run()
					{	try
						{	sleep(Configuration.getAisConfiguration().getAutoAdvanceDelay());
							SwingUtilities.invokeLater(new Runnable()
							{	public void run()
								{	buttonRound.doClick();
								}
							});				
						}
						catch (InterruptedException e)
						{	//e.printStackTrace();
						}
					}			
				};
				thread.start();
			}
			// go back to tournament
			else if(buttonTournament.isEnabled())
			{	thread = new Thread("TBB.autoadvance")
				{	public void run()
					{	try
						{	sleep(Configuration.getAisConfiguration().getAutoAdvanceDelay());
							SwingUtilities.invokeLater(new Runnable()
							{	public void run()
								{	buttonTournament.doClick();
								}
							});				
						}
						catch (InterruptedException e)
						{	//e.printStackTrace();
						}
					}			
				};
				thread.start();
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// PANELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private RoundSplitPanel roundPanel;
	private MatchDescription matchDescription;
	private MatchResults matchResults;
	private MatchStatistics matchStatistics;
		
	private void refreshPanels()
	{	matchDescription.refresh();
		matchResults.refresh();
		matchStatistics.refresh();	
	}

	/////////////////////////////////////////////////////////////////
	// ACTION PERFORMED	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
	public void actionPerformed(ActionEvent e)
	{	// possibly interrupt any pending button-related thread first
		if(thread!=null && thread.isAlive())
			thread.interrupt();
		
		// process the event
		if(e.getActionCommand().equals(GuiKeys.GAME_MATCH_BUTTON_QUIT))
		{	quitTournament();
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_MATCH_BUTTON_SAVE))
		{	SaveSplitPanel savePanel = new SaveSplitPanel(container.getMenuContainer(),container);
			savePanel.setTournament(match.getTournament());
			replaceWith(savePanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_MATCH_BUTTON_RECORD_GAMES))
		{	boolean recordGames = buttonRecord.isSelected();
			Configuration.getEngineConfiguration().setRecordRounds(recordGames);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_MATCH_BUTTON_CURRENT_TOURNAMENT))				
		{	parent.refresh();

			// possibly updating client state
			ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
			if(connection!=null)
				connection.getActiveConnection().setState(ClientState.BROWSING_TOURNAMENT);
			
			replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_MATCH_BUTTON_FINISH))
		{	match.finish();
			parent.refresh();
			((TournamentSplitPanel)parent).autoAdvance();

			// possibly updating client state
			ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
			if(connection!=null)
				connection.getActiveConnection().setState(ClientState.BROWSING_TOURNAMENT);
			
			replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_MATCH_BUTTON_DESCRIPTION))
		{	container.setDataPart(matchDescription);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_MATCH_BUTTON_RESULTS))
		{	container.setDataPart(matchResults);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_MATCH_BUTTON_STATISTICS))
		{	container.setDataPart(matchStatistics);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_MATCH_BUTTON_CURRENT_ROUND))
		{	Round round = match.getCurrentRound();
			if(roundPanel==null || roundPanel.getRound()!=round)
			{	roundPanel = new RoundSplitPanel(container.getMenuContainer(),container);
				roundPanel.setRound(round);
			}
			else
				((RoundSplitPanel)roundPanel).refreshButtons();

			// possibly updating client state
			ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
			if(connection!=null)
				connection.getActiveConnection().setState(ClientState.BROWSING_ROUND);
			
			replaceWith(roundPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_MATCH_BUTTON_NEXT_ROUND))
		{	try
			{	match.progress();
			}
			catch (IllegalArgumentException e1)
			{	e1.printStackTrace();
			}
			catch (SecurityException e1)
			{	e1.printStackTrace();
			}
			catch (ParserConfigurationException e1)
			{	e1.printStackTrace();
			}
			catch (SAXException e1)
			{	e1.printStackTrace();
			}
			catch (IOException e1)
			{	e1.printStackTrace();
			}
			catch (ClassNotFoundException e1)
			{	e1.printStackTrace();
			}
			catch (IllegalAccessException e1)
			{	e1.printStackTrace();
			}
			catch (NoSuchFieldException e1)
			{	e1.printStackTrace();
			}
			roundPanel = new RoundSplitPanel(container.getMenuContainer(),container);
			Round round = match.getCurrentRound();		
			roundPanel.setRound(round);
			roundPanel.autoAdvance();

			// possibly updating client state
			ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
			if(connection!=null)
				connection.getActiveConnection().setState(ClientState.BROWSING_ROUND);
			
			replaceWith(roundPanel);
	    }
	} 

	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
	public void refresh()
	{	refreshPanels();
		refreshButtons();
	}

	/////////////////////////////////////////////////////////////////
	// MATCH RENDER PANEL	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
	public void matchOver()
	{	SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	matchResults.refresh();
//				saveTournament();
				buttonResults.doClick();
			}
		});	
	}

	@Override
	public void roundOver()
	{	SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	matchResults.refresh();
				matchStatistics.refresh();
//				saveTournament();
				buttonResults.doClick();
			}
		});	
	}
	
	/////////////////////////////////////////////////////////////////
	// PAINT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
    protected void paintComponent(Graphics g)
	{	//g.clearRect(0, 0, getWidth(), getHeight());
//		getParent().paintComponents(g);
		super.paintComponent(g);
    }

	/////////////////////////////////////////////////////////////////
	// CLIENT GENERAL CONNECTION	/////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
	public void connectionAdded(ClientIndividualConnection connection, int index)
	{	// useless here
	}

	@Override
	public void connectionRemoved(ClientIndividualConnection connection, int index)
	{	
	}

	@Override
	public void connectionGameInfoChanged(ClientIndividualConnection connection, int index, String oldId)
	{	// useless here
	}

	@Override
	public void connectionActiveConnectionLost(ClientIndividualConnection connection, int index)
	{	// TODO maybe a reconnection can be worked out...
		if(connection.getState()==ClientState.BROWSING_MATCH)
			quitTournament();
	}

	@Override
	public void connectionProfilesChanged(ClientIndividualConnection connection, int index)
	{	// useless here
	}

	@Override
	public void connectionTournamentStarted(AbstractTournament tournament)
	{	// useless here
	}
}

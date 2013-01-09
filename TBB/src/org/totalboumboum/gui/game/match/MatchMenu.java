package org.totalboumboum.gui.game.match;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
import org.totalboumboum.gui.tools.GuiButtonTools;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.stream.network.client.ClientGeneralConnection;
import org.totalboumboum.stream.network.client.ClientGeneralConnectionListener;
import org.totalboumboum.stream.network.client.ClientIndividualConnection;
import org.totalboumboum.stream.network.client.ClientState;
import org.totalboumboum.stream.network.server.ServerGeneralConnection;
import org.xml.sax.SAXException;

/**
 * This class handles the bottom menu used
 * in the panel displaying the match
 * during game.
 * 
 * @author Vincent Labatut
 */
public class MatchMenu extends InnerMenuPanel implements MatchRenderPanel,ClientGeneralConnectionListener
{	/** Class id */
	private static final long serialVersionUID = 1L;
		
	/**
	 * Builds a standard panel.
	 * 
	 * @param container
	 * 		Container of the panel.
	 * @param parent
	 * 		Parent menu.
	 */
	public MatchMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container,parent);
	
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.LINE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiColorTools.COLOR_COMMON_BACKGROUND);
		
		// sizes
		int buttonWidth = getHeight();
		int buttonHeight = getHeight();

		// buttons
		buttonQuit = GuiButtonTools.createButton(GuiKeys.GAME_MATCH_BUTTON_QUIT,buttonWidth,buttonHeight,1,this);
		buttonSave = GuiButtonTools.createButton(GuiKeys.GAME_MATCH_BUTTON_SAVE,buttonWidth,buttonHeight,1,this);
		buttonRecord = GuiButtonTools.createToggleButton(GuiKeys.GAME_MATCH_BUTTON_RECORD_GAMES,buttonWidth,buttonHeight,1,this);
//buttonRecord.setEnabled(false);		
		add(Box.createHorizontalGlue());
		buttonTournament = GuiButtonTools.createButton(GuiKeys.GAME_MATCH_BUTTON_CURRENT_TOURNAMENT,buttonWidth,buttonHeight,1,this);
		add(Box.createRigidArea(new Dimension(GuiSizeTools.buttonHorizontalSpace,0)));
	    ButtonGroup group = new ButtonGroup();
	    buttonDescription = GuiButtonTools.createToggleButton(GuiKeys.GAME_MATCH_BUTTON_DESCRIPTION,buttonWidth,buttonHeight,1,this);
		buttonDescription.setSelected(true);
	    group.add(buttonDescription);
	    buttonResults = GuiButtonTools.createToggleButton(GuiKeys.GAME_MATCH_BUTTON_RESULTS,buttonWidth,buttonHeight,1,this);
	    group.add(buttonResults);
	    buttonStatistics = GuiButtonTools.createToggleButton(GuiKeys.GAME_MATCH_BUTTON_STATISTICS,buttonWidth,buttonHeight,1,this);
//buttonStatistics.setEnabled(false);		
	    group.add(buttonStatistics);
		add(Box.createRigidArea(new Dimension(GuiSizeTools.buttonHorizontalSpace,0)));
		buttonRound = GuiButtonTools.createButton(GuiKeys.GAME_MATCH_BUTTON_NEXT_ROUND,buttonWidth,buttonHeight,1,this);
		
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
	/** Match displayed by this panel */
	private Match match;
	/** Whether the user can play or only browse the results */
	private boolean browseOnly = false;

	/**
	 * Sets up the interaction mode of this panel:
	 * either the user can play normally, or he
	 * can only browse the statistics of the 
	 * rounds already played before.
	 * 
	 * @param browseOnly
	 * 		If {@code true}, then the user can only browse
	 * 		past results, and not play new rounds.
	 */
	public void setBrowseOnly(boolean browseOnly)
	{	this.browseOnly = browseOnly;
	}

	/**
	 * Changes the match handled
	 * by this menu.
	 * 
	 * @param match
	 * 		New match.
	 */
	public void setMatch(Match match)
	{	// match
		if(match!=null)
		{	match.setPanel(null);
			if(!browseOnly)
			{	List<Profile> profiles = match.getProfiles();
				List<Integer> controls = new ArrayList<Integer>();
				for(Profile p: profiles)
				{	int control = p.getControlSettingsIndex();
					if(controls.contains(control))
						p.setControlSettingsIndex(0);
					else
						controls.add(control);
				}
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
		if(!browseOnly)
		{	ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
			if(connection!=null)
				connection.addListener(this);
		}
	}
	
	/**
	 * Returns the match currently
	 * handled by this menu.
	 * 
	 * @return
	 * 		Current match.
	 */
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
	/**
	 * Quits the current tournament for good,
	 * and go back to the main menu.
	 */
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
	/** Definitely quits the tournament */
	@SuppressWarnings("unused")
	private JButton buttonQuit;
	/** Records the current tournament */
	private JButton buttonSave;
	/** Whether rounds should be recorded or not */
	private JToggleButton buttonRecord;
	/** Goes to the tournament panel */
	private JButton buttonTournament;
	/** Displays the match detailed description */
	private JToggleButton buttonDescription;
	/** Displays the match results */ 
	private JToggleButton buttonResults;
	/** Displays the match stat plots */
	private JToggleButton buttonStatistics;
	/** Goes to the next round */
	private JButton buttonRound;
	/** Thread used for the auto-advance system */
	private Thread thread = null;
	
	/**
	 * Update the buttons depending on 
	 * the match state.
	 */
	public void refreshButtons()
	{	if(browseOnly)
		{	if(match!=null)
			{	AbstractTournament tournament = match.getTournament();
				// previous button
				{	// first match
					if(tournament.isFirstMatch(match))
					{	GuiButtonTools.setButtonContent(GuiKeys.GAME_MATCH_BUTTON_CURRENT_TOURNAMENT, buttonTournament);
					}
					// otherwise
					else
					{	GuiButtonTools.setButtonContent(GuiKeys.GAME_MATCH_BUTTON_PREVIOUS_ROUND, buttonTournament);
					}
				}
				// next button
				{	GuiButtonTools.setButtonContent(GuiKeys.GAME_MATCH_BUTTON_NEXT_ROUND, buttonRound);
				}
				buttonTournament.setEnabled(true);
				buttonRound.setEnabled(true);
			}
			else
			{	// play
				buttonRound.setEnabled(false);
			}
		
			// record game & replay
			buttonSave.setEnabled(false);
			buttonRecord.setSelected(false);
			buttonRecord.setEnabled(false);
		}
		else
		{	if(match!=null)
			{	if(match.isOver())
				{	// Round
					buttonRound.setEnabled(false);
					// Finish
					GuiButtonTools.setButtonContent(GuiKeys.GAME_MATCH_BUTTON_FINISH, buttonTournament);
				}
				else
				{	// Round
					buttonRound.setEnabled(true);
					Round round = match.getCurrentRound();
					if(round==null || round.isOver())
						GuiButtonTools.setButtonContent(GuiKeys.GAME_MATCH_BUTTON_NEXT_ROUND, buttonRound);
					else
						GuiButtonTools.setButtonContent(GuiKeys.GAME_MATCH_BUTTON_CURRENT_ROUND, buttonRound);
					// Tournament
					GuiButtonTools.setButtonContent(GuiKeys.GAME_MATCH_BUTTON_CURRENT_TOURNAMENT, buttonTournament);
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
	}
	
	/**
	 * Automatically clicks on the appropriate
	 * buttons in order to progress in the tournament.
	 * Used to automatically chain many tournaments,
	 * while evaluating agents.
	 */
	public void autoAdvance()
	{	if(Configuration.getAisConfiguration().getAutoAdvance())
		{	// go to round
			if(buttonRound.isEnabled())
			{	thread = new Thread("TBB.autoadvance")
				{	@Override
					public void run()
					{	try
						{	sleep(Configuration.getAisConfiguration().getAutoAdvanceDelay());
							SwingUtilities.invokeLater(new Runnable()
							{	@Override
								public void run()
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
				{	@Override
					public void run()
					{	try
						{	sleep(Configuration.getAisConfiguration().getAutoAdvanceDelay());
							SwingUtilities.invokeLater(new Runnable()
							{	@Override
								public void run()
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
	/** Panel containing the round */
	private RoundSplitPanel roundPanel;
	/** Panel describing the match */
	private MatchDescription matchDescription;
	/** Panel containing the detailed results of the match */
	private MatchResults matchResults;
	/** Panel giving the evolution of the match statistics */
	private MatchStatistics matchStatistics;
		
	/**
	 * Update the panels of this menu.
	 */
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
			if(!browseOnly)
			{	ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
				if(connection!=null)
					connection.getActiveConnection().setState(ClientState.BROWSING_TOURNAMENT);
			}
			
			replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_MATCH_BUTTON_FINISH))
		{	//match.finish(); //NOTE in order to avoid getting empty matches in the saves
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
		{	if(browseOnly)
			{	Round round = match.getCurrentRound();
//TODO TODO
round = new Round(match);
match.rounds.add(round);
int index = match.getRounds().indexOf(round);
round.stats = match.getStats().getStatisticRounds().get(index);
round.match = match;
round.currentPoints = round.stats.getPoints();
			
				if(roundPanel==null || roundPanel.getRound()!=round)
				{	roundPanel = new RoundSplitPanel(container.getMenuContainer(),container);
					roundPanel.setRoundStats(round);
				}
				else
					((RoundSplitPanel)roundPanel).refreshButtons();
			}
			else
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
			}
			
			replaceWith(roundPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_MATCH_BUTTON_PREVIOUS_ROUND))
		{	//if(!browseOnly)
			{	AbstractTournament tournament = match.getTournament();
				tournament.regressStat();
				Match previousMatch = tournament.getCurrentMatch();
				Round round = previousMatch.getCurrentRound();
				if(roundPanel==null || roundPanel.getRound()!=round)
				{	roundPanel = new RoundSplitPanel(container.getMenuContainer(),container);
					roundPanel.setRoundStats(round);
				}
				else
					((RoundSplitPanel)roundPanel).refreshButtons();
			}
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
		{	@Override
			public void run()
			{	matchResults.refresh();
//				saveTournament();
				buttonResults.doClick();
			}
		});	
	}

	@Override
	public void roundOver()
	{	SwingUtilities.invokeLater(new Runnable()
		{	@Override
			public void run()
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

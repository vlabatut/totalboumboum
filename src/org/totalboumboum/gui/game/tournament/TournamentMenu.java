package org.totalboumboum.gui.game.tournament;

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
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.ai.AisConfiguration.AutoAdvance;
import org.totalboumboum.configuration.game.tournament.TournamentConfiguration;
import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.game.tournament.TournamentRenderPanel;
import org.totalboumboum.game.tournament.cup.CupTournament;
import org.totalboumboum.game.tournament.league.LeagueTournament;
import org.totalboumboum.game.tournament.sequence.SequenceTournament;
import org.totalboumboum.game.tournament.single.SingleTournament;
import org.totalboumboum.game.tournament.turning.TurningTournament;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.DataPanelListener;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.game.match.MatchSplitPanel;
import org.totalboumboum.gui.game.round.RoundSplitPanel;
import org.totalboumboum.gui.game.save.SaveSplitPanel;
import org.totalboumboum.gui.game.tournament.description.CupDescription;
import org.totalboumboum.gui.game.tournament.description.LeagueDescription;
import org.totalboumboum.gui.game.tournament.description.SequenceDescription;
import org.totalboumboum.gui.game.tournament.description.SingleDescription;
import org.totalboumboum.gui.game.tournament.description.TournamentDescription;
import org.totalboumboum.gui.game.tournament.description.TurningDescription;
import org.totalboumboum.gui.game.tournament.results.CupResults;
import org.totalboumboum.gui.game.tournament.results.LeagueResults;
import org.totalboumboum.gui.game.tournament.results.SequenceResults;
import org.totalboumboum.gui.game.tournament.results.SingleResults;
import org.totalboumboum.gui.game.tournament.results.TournamentResults;
import org.totalboumboum.gui.game.tournament.results.TurningResults;
import org.totalboumboum.gui.game.tournament.statistics.OthersStatistics;
import org.totalboumboum.gui.game.tournament.statistics.SingleStatistics;
import org.totalboumboum.gui.game.tournament.statistics.TournamentStatistics;
import org.totalboumboum.gui.menus.main.MainMenu;
import org.totalboumboum.gui.tools.GuiButtonTools;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.stream.file.archive.GameArchive;
import org.totalboumboum.stream.network.client.ClientGeneralConnection;
import org.totalboumboum.stream.network.client.ClientGeneralConnectionListener;
import org.totalboumboum.stream.network.client.ClientIndividualConnection;
import org.totalboumboum.stream.network.client.ClientState;
import org.totalboumboum.stream.network.server.ServerGeneralConnection;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.xml.sax.SAXException;

/**
 * This class handles the bottom menu used
 * in the panel displaying the tournament
 * during game.
 * 
 * @author Vincent Labatut
 */
public class TournamentMenu extends InnerMenuPanel implements TournamentRenderPanel,ClientGeneralConnectionListener,DataPanelListener
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a standard panel.
	 * 
	 * @param container
	 * 		Container of the panel
	 * @param parent
	 * 		Parent menu.
	 */
	public TournamentMenu(SplitMenuPanel container, MenuPanel parent)
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
		buttonQuit = GuiButtonTools.createButton(GuiKeys.GAME_TOURNAMENT_BUTTON_QUIT,buttonWidth,buttonHeight,1,this);
		buttonSave = GuiButtonTools.createButton(GuiKeys.GAME_TOURNAMENT_BUTTON_SAVE,buttonWidth,buttonHeight,1,this);
		buttonRecord = GuiButtonTools.createToggleButton(GuiKeys.GAME_TOURNAMENT_BUTTON_RECORD_GAMES,buttonWidth,buttonHeight,1,this);
buttonRecord.setEnabled(!GameData.PRODUCTION);		
		add(Box.createHorizontalGlue());
		buttonMenu = GuiButtonTools.createButton(GuiKeys.GAME_TOURNAMENT_BUTTON_MENU,buttonWidth,buttonHeight,1,this);
		add(Box.createRigidArea(new Dimension(GuiSizeTools.buttonHorizontalSpace,0)));
	    ButtonGroup group = new ButtonGroup();
	    buttonDescription = GuiButtonTools.createToggleButton(GuiKeys.GAME_TOURNAMENT_BUTTON_DESCRIPTION,buttonWidth,buttonHeight,1,this);
	    group.add(buttonDescription);
	    buttonResults = GuiButtonTools.createToggleButton(GuiKeys.GAME_TOURNAMENT_BUTTON_RESULTS,buttonWidth,buttonHeight,1,this);
	    group.add(buttonResults);
	    buttonStatistics = GuiButtonTools.createToggleButton(GuiKeys.GAME_TOURNAMENT_BUTTON_STATISTICS,buttonWidth,buttonHeight,1,this);
	    group.add(buttonStatistics);
		add(Box.createRigidArea(new Dimension(GuiSizeTools.buttonHorizontalSpace,0)));
		buttonMatch = GuiButtonTools.createButton(GuiKeys.GAME_TOURNAMENT_BUTTON_NEXT_MATCH,buttonWidth,buttonHeight,1,this);
	}
	
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Tournament displayed by this panel */
	private AbstractTournament tournament;
	/** Whether the user can play or only browses the results */
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
	 * Changes the tournament handled
	 * by this menu.
	 * 
	 * @param tournament
	 * 		New tournament.
	 */
	public void setTournament(AbstractTournament tournament)
	{	buttonDescription.setSelected(true);
		matchPanel = null;
		
		// tournament
		this.tournament = tournament;
		if(tournament==null)
		{
			// nothing to do here
		}
		else
		{	// panels
			tournamentDescription = null;
			if(tournament instanceof CupTournament)
			{	CupTournament trnmt = (CupTournament) tournament;
				// create
				CupDescription trnmtDescription = new CupDescription(container);
				tournamentDescription = trnmtDescription;
				tournamentDescription.addListener(this);
				container.setDataPart(tournamentDescription);
				CupResults trnmtResults = new CupResults(container);
				tournamentResults = trnmtResults;
				tournamentResults.addListener(this);
				OthersStatistics ts = new OthersStatistics(container);
				tournamentStatistics = ts;
				tournamentStatistics.addListener(this);
				// set tournament
				trnmtDescription.setTournament(trnmt);
				trnmtResults.setTournament(trnmt);
				ts.setTournament(trnmt);	
			}
			else if(tournament instanceof LeagueTournament)
			{	LeagueTournament trnmt = (LeagueTournament) tournament;
				// create
				LeagueDescription trnmtDescription = new LeagueDescription(container);
				tournamentDescription = trnmtDescription;
				tournamentDescription.addListener(this);
				container.setDataPart(tournamentDescription);
				LeagueResults trnmtResults = new LeagueResults(container);
				tournamentResults = trnmtResults;
				tournamentResults.addListener(this);
				OthersStatistics ts = new OthersStatistics(container);
				tournamentStatistics = ts;
				tournamentStatistics.addListener(this);
				// set tournament
				trnmtDescription.setTournament(trnmt);
				trnmtResults.setTournament(trnmt);
				ts.setTournament(trnmt);	
			}
			else if(tournament instanceof SequenceTournament)
			{	SequenceTournament trnmt = (SequenceTournament) tournament;
				// create
				SequenceDescription trnmtDescription = new SequenceDescription(container);
				tournamentDescription = trnmtDescription;
				tournamentDescription.addListener(this);
				container.setDataPart(tournamentDescription);
				SequenceResults trnmtResults = new SequenceResults(container);
				tournamentResults = trnmtResults;
				tournamentResults.addListener(this);
				OthersStatistics ts = new OthersStatistics(container);
				tournamentStatistics = ts;
				tournamentStatistics.addListener(this);
				// set tournament
				trnmtDescription.setTournament(trnmt);
				trnmtResults.setTournament(trnmt);
				ts.setTournament(trnmt);	
			}
			else if(tournament instanceof SingleTournament)
			{	if(!browseOnly)
					tournament.progress();//TODO progressStats for browseOnly?
				SingleTournament trnmt = (SingleTournament) tournament;
				// create
				SingleDescription trnmtDescription = new SingleDescription(container);
				tournamentDescription = trnmtDescription;
				tournamentDescription.addListener(this);
				container.setDataPart(tournamentDescription);
				SingleResults trnmtResults = new SingleResults(container);
				tournamentResults = trnmtResults;
				tournamentResults.addListener(this);
				SingleStatistics ts = new SingleStatistics(container);
				tournamentStatistics = ts;
				tournamentStatistics.addListener(this);
				// set tournament
				trnmtDescription.setTournament(trnmt);
				trnmtResults.setTournament(trnmt);
				ts.setTournament(trnmt);
				// change button
				GuiButtonTools.setButtonContent(GuiKeys.GAME_MATCH_BUTTON_NEXT_ROUND,buttonMatch);
			}
			else if(tournament instanceof TurningTournament)
			{	TurningTournament trnmt = (TurningTournament) tournament;
				// create
				TurningDescription trnmtDescription = new TurningDescription(container);
				tournamentDescription = trnmtDescription;
				tournamentDescription.addListener(this);
				container.setDataPart(tournamentDescription);
				TurningResults trnmtResults = new TurningResults(container);
				tournamentResults = trnmtResults;
				tournamentResults.addListener(this);
				OthersStatistics ts = new OthersStatistics(container);
				tournamentStatistics = ts;
				tournamentStatistics.addListener(this);
				// set tournament
				trnmtDescription.setTournament(trnmt);
				trnmtResults.setTournament(trnmt);
				ts.setTournament(trnmt);	
			}
			tournament.setPanel(this);
		}
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
	 * Returns the tournament currently
	 * handled by this menu.
	 * 
	 * @return
	 * 		Current tournament.
	 */
	public AbstractTournament getTournament()
	{	return tournament;	
	}

	/**
	 * Save the current tournament.
	 */
	private void saveTournament()
	{	TournamentConfiguration tournamentConfiguration = Configuration.getGameConfiguration().getTournamentConfiguration();
		AbstractTournament tournamentConf = tournamentConfiguration.getTournament();
		if(tournament==tournamentConf && tournamentConfiguration.getAutoSave())
		{	try
			{	// filenames
				String autosave = FilePaths.getSavesPath()+File.separator+FileNames.FILE_AUTOSAVE;
				String backup = FilePaths.getSavesPath()+File.separator+FileNames.FILE_AUTOSAVE_BACKUP;
				String dataFileName = FileNames.FILE_ARCHIVE+FileNames.EXTENSION_DATA;
				String xmlFileName = FileNames.FILE_ARCHIVE+FileNames.EXTENSION_XML;
				// backup
				File autosaveFile,backupFile;
				autosaveFile = new File(autosave,dataFileName);
				backupFile = new File(backup,dataFileName);
				if(backupFile.exists())
					backupFile.delete();
				autosaveFile.renameTo(backupFile);
				autosaveFile = new File(autosave,xmlFileName);
				backupFile = new File(backup,xmlFileName);
				if(backupFile.exists())
					backupFile.delete();
				autosaveFile.renameTo(backupFile);
				// save
				GameArchive.saveGame(FileNames.FILE_AUTOSAVE,tournament);
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

	/**
	 * Quits the current tournament for good,
	 * and go back to the main menu.
	 */
	private void quitTournament()
	{	// end tournament
		tournament.cancel();
		tournament.finish();

		// end possible connection
		Configuration.getConnectionsConfiguration().terminateConnection();
		
		// set main menu frame
		getFrame().setMainMenuPanel();
    }

	/////////////////////////////////////////////////////////////////
	// BUTTONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Definitely quits the tournament */
	private JButton buttonQuit;
	/** Records the current tournament */
	private JButton buttonSave;
	/** Whether rounds should be recorded or not */
	private JToggleButton buttonRecord;
	/** Goes to the main menu without quitting the tournament */
	private JButton buttonMenu;
	/** Displays the tournament detailed description */
	private JToggleButton buttonDescription;
	/** Displays the tournament results */ 
	private JToggleButton  buttonResults;
	/** Displays the tournament stat plots */
	private JToggleButton  buttonStatistics;
	/** Goes to the next match */
	private JButton buttonMatch;

	/**
	 * Update the buttons depending on 
	 * the tournament state.
	 */
	public void refreshButtons()
	{	if(browseOnly)
		{	if(tournament!=null)
			{	if(tournament instanceof SingleTournament)
				{	if(!tournament.hasBegun())
					{	GuiButtonTools.setButtonContent(GuiKeys.GAME_ROUND_BUTTON_NEXT_ROUND, buttonMatch);
						buttonMatch.setEnabled(false);
						GuiButtonTools.setButtonContent(GuiKeys.GAME_TOURNAMENT_BUTTON_FINISH, buttonMenu);
						buttonMenu.setEnabled(true);
					}
					else
					{	buttonMatch.setEnabled(true);
						Match match = tournament.getCurrentMatch();
						Round round = match.getCurrentRound();
						GuiButtonTools.setButtonContent(GuiKeys.GAME_ROUND_BUTTON_NEXT_ROUND, buttonMatch);
						if(round==null)
							buttonMatch.setEnabled(false);
						else
							buttonMatch.setEnabled(true);
						GuiButtonTools.setButtonContent(GuiKeys.GAME_TOURNAMENT_BUTTON_MENU, buttonMenu);
						buttonMenu.setEnabled(true);
					}				
				}
				else
				{	if(!tournament.hasBegun())
					{	buttonMatch.setEnabled(false);
						GuiButtonTools.setButtonContent(GuiKeys.GAME_TOURNAMENT_BUTTON_FINISH, buttonMenu);
					}
					else
					{	List<Match> playedMatches = tournament.getPlayedMatches();
						GuiButtonTools.setButtonContent(GuiKeys.GAME_ROUND_BUTTON_NEXT_MATCH, buttonMatch);
						if(playedMatches.isEmpty())
							buttonMatch.setEnabled(false);
						else
							buttonMatch.setEnabled(true);
						GuiButtonTools.setButtonContent(GuiKeys.GAME_TOURNAMENT_BUTTON_MENU, buttonMenu);
						buttonMenu.setEnabled(true);
					}
				}
			}
			else
			{	buttonMatch.setEnabled(false);
				GuiButtonTools.setButtonContent(GuiKeys.GAME_TOURNAMENT_BUTTON_FINISH, buttonMenu);
				buttonMenu.setEnabled(true);
			}
		
			// record game & replay
			buttonSave.setEnabled(false);
			buttonMenu.setEnabled(true);
			buttonRecord.setSelected(false);
			buttonRecord.setEnabled(false);
		}
	
		else
		{	if(tournament!=null)
			{	if(tournament instanceof SingleTournament)
				{	if(tournament.isOver())
					{	buttonMatch.setEnabled(false);
						GuiButtonTools.setButtonContent(GuiKeys.GAME_TOURNAMENT_BUTTON_FINISH, buttonMenu);
					}
					else
					{	buttonMatch.setEnabled(true);
						Match match = tournament.getCurrentMatch();
						Round round = match.getCurrentRound();
						if(round==null || round.isOver())
							GuiButtonTools.setButtonContent(GuiKeys.GAME_MATCH_BUTTON_NEXT_ROUND, buttonMatch);
						else
							GuiButtonTools.setButtonContent(GuiKeys.GAME_MATCH_BUTTON_CURRENT_ROUND, buttonMatch);
						GuiButtonTools.setButtonContent(GuiKeys.GAME_TOURNAMENT_BUTTON_MENU, buttonMenu);
					}				
				}
				else
				{	if(tournament.isOver())
					{	buttonMatch.setEnabled(false);
						GuiButtonTools.setButtonContent(GuiKeys.GAME_TOURNAMENT_BUTTON_FINISH, buttonMenu);
					}
					else
					{	buttonMatch.setEnabled(true);
						Match match = tournament.getCurrentMatch();
						if(match==null || match.isOver())
							GuiButtonTools.setButtonContent(GuiKeys.GAME_TOURNAMENT_BUTTON_NEXT_MATCH, buttonMatch);
						else
							GuiButtonTools.setButtonContent(GuiKeys.GAME_TOURNAMENT_BUTTON_CURRENT_MATCH, buttonMatch);
						GuiButtonTools.setButtonContent(GuiKeys.GAME_TOURNAMENT_BUTTON_MENU, buttonMenu);
					}
				}
			}
			else
			{	buttonMatch.setEnabled(false);
			}
		
			// record game
			ServerGeneralConnection serverConnection = Configuration.getConnectionsConfiguration().getServerConnection();
			ClientGeneralConnection clientConnection = Configuration.getConnectionsConfiguration().getClientConnection();
			boolean connectionState = serverConnection==null && clientConnection==null;
			buttonSave.setEnabled(connectionState);
			buttonMenu.setEnabled(connectionState);
			
			// record replay
			boolean recordGames = Configuration.getEngineConfiguration().isRecordRounds();
			buttonRecord.setSelected(recordGames);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// AUTO ADVANCE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Thread used for the auto-advance system */
	private Thread thread = null;

	/**
	 * Automatically clicks on the appropriate
	 * buttons in order to progress in the tournament.
	 * Used to automatically chain many tournaments,
	 * while evaluating agents.
	 */
	public void autoAdvance()
	{	if(Configuration.getAisConfiguration().getAutoAdvance()!=AutoAdvance.NONE && !browseOnly)
		{	// go to next match
			if(buttonMatch.isEnabled())
			{	thread = new Thread("TBB.autoadvance")
				{	@Override
					public void run()
					{	try
						{	sleep(Configuration.getAisConfiguration().getAutoAdvanceDelay());
							SwingUtilities.invokeLater(new Runnable()
							{	@Override
								public void run()
								{	buttonMatch.doClick();
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
			// go back to main menu (only for 'tournament' auto-advance mode)
			else if(buttonQuit.isEnabled() && Configuration.getAisConfiguration().getAutoAdvance()==AutoAdvance.TOURNAMENT && !browseOnly)
			{	thread = new Thread("TBB.autoadvance")
				{	@Override
					public void run()
					{	try
						{	sleep(Configuration.getAisConfiguration().getAutoAdvanceDelay());
							SwingUtilities.invokeLater(new Runnable()
							{	@Override
								public void run()
								{	buttonQuit.doClick();
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
	/** Panel containing the match (or the round, for a single tournament) */
	private MenuPanel matchPanel;
	/** Panel describing the tournament */
	private TournamentDescription<?> tournamentDescription;
	/** Panel containing the detailed results of the tournament */
	private TournamentResults<?> tournamentResults;
	/** Panel giving the evolution of the tournament statistics */
	private TournamentStatistics<?> tournamentStatistics;

	/**
	 * Update the panels of this menu.
	 */
	private void refreshPanels()
	{	tournamentDescription.refresh();
		tournamentResults.refresh();
		tournamentStatistics.refresh();	
	}

	/////////////////////////////////////////////////////////////////
	// ACTION LISTENER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void actionPerformed(ActionEvent e)
	{	// possibly interrupt any pending button-related thread first
		if(thread!=null && thread.isAlive())
			thread.interrupt();
		
		// process the event
		if(e.getActionCommand().equals(GuiKeys.GAME_TOURNAMENT_BUTTON_QUIT))
		{	((MainMenu)parent).autoAdvance();
			quitTournament();
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_TOURNAMENT_BUTTON_SAVE))
		{	SaveSplitPanel savePanel = new SaveSplitPanel(container.getMenuContainer(),container);
			savePanel.setTournament(tournament);
			replaceWith(savePanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_TOURNAMENT_BUTTON_RECORD_GAMES))
		{	boolean recordGames = buttonRecord.isSelected();
			Configuration.getEngineConfiguration().setRecordRounds(recordGames);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_TOURNAMENT_BUTTON_MENU))
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_TOURNAMENT_BUTTON_FINISH))
		{	tournament.finish();
			parent.refresh();
			replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_TOURNAMENT_BUTTON_DESCRIPTION))
		{	container.setDataPart(tournamentDescription);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_TOURNAMENT_BUTTON_RESULTS))
		{	container.setDataPart(tournamentResults);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_TOURNAMENT_BUTTON_STATISTICS))
		{	container.setDataPart(tournamentStatistics);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_TOURNAMENT_BUTTON_CURRENT_MATCH))
		{	Match match = tournament.getCurrentMatch();		
			if(matchPanel==null || ((MatchSplitPanel)matchPanel).getMatch()!=match)
			{	MatchSplitPanel mPanel = new MatchSplitPanel(container.getMenuContainer(),container);
				matchPanel = mPanel;
				mPanel.setMatch(match);
			}
			else
				((MatchSplitPanel)matchPanel).refreshButtons();

			// possibly updating client state
			ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
			if(connection!=null)
				connection.getActiveConnection().setState(ClientState.BROWSING_MATCH);
			
			replaceWith(matchPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_MATCH_BUTTON_CURRENT_ROUND))
		{	Match match = tournament.getCurrentMatch();		
			Round round = match.getCurrentRound();
			if(matchPanel==null || ((RoundSplitPanel)matchPanel).getRound()!=round)
			{	RoundSplitPanel rPanel = new RoundSplitPanel(container.getMenuContainer(),container);
				matchPanel = rPanel;
				rPanel.setRound(round);
			}
			else
				((RoundSplitPanel)matchPanel).refreshButtons();

			// possibly updating client state
			ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
			if(connection!=null)
				connection.getActiveConnection().setState(ClientState.BROWSING_ROUND);
			
			replaceWith(matchPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_ROUND_BUTTON_NEXT_MATCH))
		{	//if(browseOnly)
			{	//tournament.progressStat();
				Match match = tournament.getCurrentMatch();		
				if(matchPanel==null || ((MatchSplitPanel)matchPanel).getMatch()!=match)
				{	MatchSplitPanel mPanel = new MatchSplitPanel(container.getMenuContainer(),container);
					mPanel.setMatchStats(match);
					matchPanel = mPanel;
				}
				else
					((MatchSplitPanel)matchPanel).refreshButtons();
				
				replaceWith(matchPanel);
			}
		}
		else if(e.getActionCommand().equals(GuiKeys.GAME_TOURNAMENT_BUTTON_NEXT_MATCH))
		{	tournament.progress();
			MatchSplitPanel mPanel = new MatchSplitPanel(container.getMenuContainer(),container);
			matchPanel = mPanel;
			Match match = tournament.getCurrentMatch();		
			mPanel.setMatch(match);
			mPanel.autoAdvance();
			// possibly updating client state
			ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
			if(connection!=null)
				connection.getActiveConnection().setState(ClientState.BROWSING_MATCH);
			
			replaceWith(matchPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_ROUND_BUTTON_NEXT_ROUND))
		{	//if(browseOnly)
			{	Match match = tournament.getCurrentMatch();		
				//match.progressStat();
				Round round = match.getCurrentRound();
				if(matchPanel==null || ((RoundSplitPanel)matchPanel).getRound()!=round)
				{	RoundSplitPanel rPanel = new RoundSplitPanel(container.getMenuContainer(),container);
					matchPanel = rPanel;
					rPanel.setRoundStats(round);
				}
				else
					((RoundSplitPanel)matchPanel).refreshButtons();
				
				replaceWith(matchPanel);
			}
		}
		else if(e.getActionCommand().equals(GuiKeys.GAME_MATCH_BUTTON_NEXT_ROUND))
		{	Match match = tournament.getCurrentMatch();		
			try
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
			RoundSplitPanel rPanel = new RoundSplitPanel(container.getMenuContainer(),container);
			matchPanel = rPanel;
			Round round = match.getCurrentRound();
			rPanel.setRound(round);
			rPanel.autoAdvance();

			// possibly updating client state
			ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
			if(connection!=null)
				connection.getActiveConnection().setState(ClientState.BROWSING_ROUND);
			
			replaceWith(matchPanel);
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
	// TOURNAMENT RENDER PANEL	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
	public void roundOver()
	{	saveTournament();
		// refresh only the Single match tournaments
		if(tournament instanceof SingleTournament)
		{	SwingUtilities.invokeLater(new Runnable()
			{	@Override
				public void run()
				{	tournamentResults.refresh();
					buttonResults.doClick();
				}
			});
		}
	}

	@Override
	public void matchOver()
	{	saveTournament();
		SwingUtilities.invokeLater(new Runnable()
		{	@Override
			public void run()
			{	tournamentResults.refresh();
				buttonResults.doClick();
			}
		});	
	}

	@Override
	public void tournamentOver()
	{	SwingUtilities.invokeLater(new Runnable()
		{	@Override
			public void run()
			{	tournamentResults.refresh();
				saveTournament();
				buttonResults.doClick();
			}
		});	
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
	{	//
	}

	@Override
	public void connectionGameInfoChanged(ClientIndividualConnection connection, int index, String oldId)
	{	// useless here
	}

	@Override
	public void connectionActiveConnectionLost(ClientIndividualConnection connection, int index)
	{	// TODO maybe a reconnection can be worked out...
		if(connection.getState()==ClientState.BROWSING_TOURNAMENT)
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

	/////////////////////////////////////////////////////////////////
	// DATA PANEL LISTENER			/////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
	public void dataPanelSelectionChanged(Object object)
	{	// not used here
	}

	@Override
	public void mousePressed(MouseEvent e)
	{	// possibly interrupt any pending button-related thread first
		if(thread!=null && thread.isAlive())
			thread.interrupt();
	}
}

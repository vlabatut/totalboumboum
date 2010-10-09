package org.totalboumboum.gui.game.tournament;

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
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.game.tournament.TournamentConfiguration;
import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.game.tournament.TournamentRenderPanel;
import org.totalboumboum.game.tournament.cup.CupTournament;
import org.totalboumboum.game.tournament.league.LeagueTournament;
import org.totalboumboum.game.tournament.sequence.SequenceTournament;
import org.totalboumboum.game.tournament.single.SingleTournament;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
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
import org.totalboumboum.gui.game.tournament.results.CupResults;
import org.totalboumboum.gui.game.tournament.results.LeagueResults;
import org.totalboumboum.gui.game.tournament.results.SequenceResults;
import org.totalboumboum.gui.game.tournament.results.SingleResults;
import org.totalboumboum.gui.game.tournament.results.TournamentResults;
import org.totalboumboum.gui.game.tournament.statistics.TournamentStatistics;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.stream.file.archive.GameArchive;
import org.totalboumboum.stream.network.client.ClientGeneralConnection;
import org.totalboumboum.stream.network.server.ServerGeneralConnection;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class TournamentMenu extends InnerMenuPanel implements TournamentRenderPanel
{	private static final long serialVersionUID = 1L;
		
	public TournamentMenu(SplitMenuPanel container, MenuPanel parent)
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
		buttonQuit = GuiTools.createButton(GuiKeys.GAME_TOURNAMENT_BUTTON_QUIT,buttonWidth,buttonHeight,1,this);
		buttonSave = GuiTools.createButton(GuiKeys.GAME_TOURNAMENT_BUTTON_SAVE,buttonWidth,buttonHeight,1,this);
		buttonRecord = GuiTools.createToggleButton(GuiKeys.GAME_TOURNAMENT_BUTTON_RECORD_GAMES,buttonWidth,buttonHeight,1,this);
		add(Box.createHorizontalGlue());
		buttonMenu = GuiTools.createButton(GuiKeys.GAME_TOURNAMENT_BUTTON_MENU,buttonWidth,buttonHeight,1,this);
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
	    ButtonGroup group = new ButtonGroup();
	    buttonDescription = GuiTools.createToggleButton(GuiKeys.GAME_TOURNAMENT_BUTTON_DESCRIPTION,buttonWidth,buttonHeight,1,this);
	    group.add(buttonDescription);
	    buttonResults = GuiTools.createToggleButton(GuiKeys.GAME_TOURNAMENT_BUTTON_RESULTS,buttonWidth,buttonHeight,1,this);
	    group.add(buttonResults);
	    buttonStatistics = GuiTools.createToggleButton(GuiKeys.GAME_TOURNAMENT_BUTTON_STATISTICS,buttonWidth,buttonHeight,1,this);
buttonStatistics.setEnabled(false);		
	    group.add(buttonStatistics);
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		buttonMatch = GuiTools.createButton(GuiKeys.GAME_TOURNAMENT_BUTTON_NEXT_MATCH,buttonWidth,buttonHeight,1,this);
	}
	
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AbstractTournament tournament;

	public void setTournament(AbstractTournament tournament)
	{	buttonDescription.setSelected(true);
		matchPanel = null;
		
		// tournament
		this.tournament = tournament;
		if(tournament==null)
		{
			
		}
		else
		{	// panels
			tournamentDescription = null;
			if(tournament instanceof SequenceTournament)
			{	SequenceTournament trnmt = (SequenceTournament) tournament;
				// create
				SequenceDescription trnmtDescription = new SequenceDescription(container);
				tournamentDescription = trnmtDescription;
				container.setDataPart(tournamentDescription);
				SequenceResults trnmtResults = new SequenceResults(container);
				tournamentResults = trnmtResults;
				tournamentStatistics = new TournamentStatistics(container);
				// set tournament
				trnmtDescription.setTournament(trnmt);
				trnmtResults.setTournament(trnmt);
				tournamentStatistics.setTournament(trnmt);	
			}
			else if(tournament instanceof CupTournament)
			{	CupTournament trnmt = (CupTournament) tournament;
				// create
				CupDescription trnmtDescription = new CupDescription(container);
				tournamentDescription = trnmtDescription;
				container.setDataPart(tournamentDescription);
				CupResults trnmtResults = new CupResults(container);
				tournamentResults = trnmtResults;
				tournamentStatistics = new TournamentStatistics(container);
				// set tournament
				trnmtDescription.setTournament(trnmt);
				trnmtResults.setTournament(trnmt);
				tournamentStatistics.setTournament(trnmt);	
			}
			else if(tournament instanceof LeagueTournament)
			{	LeagueTournament trnmt = (LeagueTournament) tournament;
				// create
				LeagueDescription trnmtDescription = new LeagueDescription(container);
				tournamentDescription = trnmtDescription;
				container.setDataPart(tournamentDescription);
				LeagueResults trnmtResults = new LeagueResults(container);
				tournamentResults = trnmtResults;
				tournamentStatistics = new TournamentStatistics(container);
				// set tournament
				trnmtDescription.setTournament(trnmt);
				trnmtResults.setTournament(trnmt);
				tournamentStatistics.setTournament(trnmt);	
			}
			else if(tournament instanceof SingleTournament)
			{	tournament.progress();
				SingleTournament trnmt = (SingleTournament) tournament;
				// create
				SingleDescription trnmtDescription = new SingleDescription(container);
				tournamentDescription = trnmtDescription;
				container.setDataPart(tournamentDescription);
				SingleResults trnmtResults = new SingleResults(container);
				tournamentResults = trnmtResults;
				tournamentStatistics = new TournamentStatistics(container);
				// set tournament
				trnmtDescription.setTournament(trnmt);
				trnmtResults.setTournament(trnmt);
				tournamentStatistics.setTournament(trnmt);
				// change button
				GuiTools.setButtonContent(GuiKeys.GAME_MATCH_BUTTON_NEXT_ROUND,buttonMatch);
			}
			tournament.setPanel(this);
		}
		// buttons
		refreshButtons();
	}
	
	public AbstractTournament getTournament()
	{	return tournament;	
	}

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

	/////////////////////////////////////////////////////////////////
	// BUTTONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	private JButton buttonQuit;
	private JButton buttonSave;
	private JToggleButton buttonRecord;
	private JButton buttonMenu;
	private JToggleButton buttonDescription;
	private JToggleButton  buttonResults;
	private JToggleButton  buttonStatistics;
	private JButton buttonMatch;
	
	private Thread thread = null;

	public void refreshButtons()
	{	if(tournament!=null)
		{	if(tournament instanceof SingleTournament)
			{	if(tournament.isOver())
				{	buttonMatch.setEnabled(false);
					GuiTools.setButtonContent(GuiKeys.GAME_TOURNAMENT_BUTTON_FINISH, buttonMenu);
				}
				else
				{	buttonMatch.setEnabled(true);
					Match match = tournament.getCurrentMatch();
					Round round = match.getCurrentRound();
					if(round==null || round.isOver())
						GuiTools.setButtonContent(GuiKeys.GAME_MATCH_BUTTON_NEXT_ROUND, buttonMatch);
					else
						GuiTools.setButtonContent(GuiKeys.GAME_MATCH_BUTTON_CURRENT_ROUND, buttonMatch);
					GuiTools.setButtonContent(GuiKeys.GAME_TOURNAMENT_BUTTON_MENU, buttonMenu);
				}				
			}
			else
			{	if(tournament.isOver())
				{	buttonMatch.setEnabled(false);
					GuiTools.setButtonContent(GuiKeys.GAME_TOURNAMENT_BUTTON_FINISH, buttonMenu);
				}
				else
				{	buttonMatch.setEnabled(true);
					Match match = tournament.getCurrentMatch();
					if(match==null || match.isOver())
						GuiTools.setButtonContent(GuiKeys.GAME_TOURNAMENT_BUTTON_NEXT_MATCH, buttonMatch);
					else
						GuiTools.setButtonContent(GuiKeys.GAME_TOURNAMENT_BUTTON_CURRENT_MATCH, buttonMatch);
					GuiTools.setButtonContent(GuiKeys.GAME_TOURNAMENT_BUTTON_MENU, buttonMenu);
				}
			}
		}
		else
		{	buttonMatch.setEnabled(false);
		}
	
		// record game
		ServerGeneralConnection serverConnection = Configuration.getConnectionsConfiguration().getServerConnection();
		ClientGeneralConnection clientConnection = Configuration.getConnectionsConfiguration().getClientConnection();
		boolean saveState = serverConnection==null && clientConnection==null;
		buttonSave.setEnabled(saveState);
		
		// record replay
		boolean recordGames = Configuration.getEngineConfiguration().isRecordRounds();
		buttonRecord.setSelected(recordGames);
	}
	
	public void autoAdvance()
	{	if(Configuration.getAisConfiguration().getAutoAdvance())
		{	// go to match
			if(buttonMatch.isEnabled())
			{	thread = new Thread()
				{	public void run()
					{	try
						{	sleep(Configuration.getAisConfiguration().getAutoAdvanceDelay());
							SwingUtilities.invokeLater(new Runnable()
							{	public void run()
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
		}
	}

	/////////////////////////////////////////////////////////////////
	// PANELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private MenuPanel matchPanel;
	private TournamentDescription<?> tournamentDescription;
	private TournamentResults<?> tournamentResults;
	private TournamentStatistics tournamentStatistics;

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
		{	tournament.cancel();
			getFrame().setMainMenuPanel();
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
			replaceWith(matchPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_TOURNAMENT_BUTTON_NEXT_MATCH))
		{	tournament.progress();
			MatchSplitPanel mPanel = new MatchSplitPanel(container.getMenuContainer(),container);
			matchPanel = mPanel;
			Match match = tournament.getCurrentMatch();		
			mPanel.setMatch(match);
			mPanel.autoAdvance();
			replaceWith(matchPanel);
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
			{	public void run()
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
		{	public void run()
			{	tournamentResults.refresh();
				buttonResults.doClick();
			}
		});	
	}

	@Override
	public void tournamentOver()
	{	SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	tournamentResults.refresh();
				saveTournament();
				buttonResults.doClick();
			}
		});	
	}
}

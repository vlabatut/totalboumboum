package fr.free.totalboumboum.gui.game.tournament;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.game.tournament.TournamentConfiguration;
import fr.free.totalboumboum.game.archive.GameArchive;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.game.tournament.TournamentRenderPanel;
import fr.free.totalboumboum.game.tournament.cup.CupTournament;
import fr.free.totalboumboum.game.tournament.league.LeagueTournament;
import fr.free.totalboumboum.game.tournament.sequence.SequenceTournament;
import fr.free.totalboumboum.game.tournament.single.SingleTournament;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.game.match.MatchSplitPanel;
import fr.free.totalboumboum.gui.game.round.RoundSplitPanel;
import fr.free.totalboumboum.gui.game.save.SaveSplitPanel;
import fr.free.totalboumboum.gui.game.tournament.description.CupDescription;
import fr.free.totalboumboum.gui.game.tournament.description.SequenceDescription;
import fr.free.totalboumboum.gui.game.tournament.description.SingleDescription;
import fr.free.totalboumboum.gui.game.tournament.description.TournamentDescription;
import fr.free.totalboumboum.gui.game.tournament.results.SequenceResults;
import fr.free.totalboumboum.gui.game.tournament.results.SingleResults;
import fr.free.totalboumboum.gui.game.tournament.results.TournamentResults;
import fr.free.totalboumboum.gui.game.tournament.statistics.TournamentStatistics;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;

public class TournamentMenu extends InnerMenuPanel implements TournamentRenderPanel
{	private static final long serialVersionUID = 1L;
	
	private MenuPanel matchPanel;
	private TournamentDescription<?> tournamentDescription;
	private TournamentResults<?> tournamentResults;
	private TournamentStatistics tournamentStatistics;
		
	@SuppressWarnings("unused")
	private JButton buttonQuit;
	@SuppressWarnings("unused")
	private JButton buttonSave;
	private JButton buttonMenu;
	private JToggleButton buttonDescription;
	private JToggleButton  buttonResults;
	private JToggleButton  buttonStatistics;
	private JButton buttonMatch;
	
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
			{	
/*				
				CupTournament trnmt = (CupTournament) tournament;
				// create
				CupDescription trnmtDescription = new CupDescription(container);
				tournamentDescription = trnmtDescription;
				container.setDataPart(tournamentDescription);
				SequenceResults trnmtResults = new SequenceResults(container);
				tournamentResults = trnmtResults;
				tournamentStatistics = new TournamentStatistics(container);
				// set tournament
				trnmtDescription.setTournament(trnmt);
				trnmtResults.setTournament(trnmt);
				tournamentStatistics.setTournament(trnmt);	
*/				
			}
			else if(tournament instanceof LeagueTournament)
			{
				// NOTE à compléter
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
		{	String folder = FileTools.FOLDER_DEFAULT;
			try
			{	GameArchive.saveGame(folder, tournament);
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
	// REFRESH	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void refreshButtons()
	{	if(tournament!=null)
		{	
			if(tournament instanceof SingleTournament)
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
	}
	
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
	{	if(e.getActionCommand().equals(GuiKeys.GAME_TOURNAMENT_BUTTON_QUIT))
		{	tournament.cancel();
			getFrame().setMainMenuPanel();
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_TOURNAMENT_BUTTON_SAVE))
		{	SaveSplitPanel savePanel = new SaveSplitPanel(container.getContainer(),container);
			savePanel.setTournament(tournament);
			replaceWith(savePanel);
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
			{	MatchSplitPanel mPanel = new MatchSplitPanel(container.getContainer(),container);
				matchPanel = mPanel;
				mPanel.setMatch(match);
			}
			replaceWith(matchPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_MATCH_BUTTON_CURRENT_ROUND))
		{	Match match = tournament.getCurrentMatch();		
			Round round = match.getCurrentRound();
			if(matchPanel==null || ((RoundSplitPanel)matchPanel).getRound()!=round)
			{	RoundSplitPanel rPanel = new RoundSplitPanel(container.getContainer(),container);
				matchPanel = rPanel;
				rPanel.setRound(round);
			}							
			replaceWith(matchPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_TOURNAMENT_BUTTON_NEXT_MATCH))
		{	tournament.progress();
			MatchSplitPanel mPanel = new MatchSplitPanel(container.getContainer(),container);
			matchPanel = mPanel;
			Match match = tournament.getCurrentMatch();		
			mPanel.setMatch(match);
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
			RoundSplitPanel rPanel = new RoundSplitPanel(container.getContainer(),container);
			matchPanel = rPanel;
			Round round = match.getCurrentRound();
			rPanel.setRound(round);
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
	public void matchOver()
	{	SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	tournamentResults.refresh();
				saveTournament();
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

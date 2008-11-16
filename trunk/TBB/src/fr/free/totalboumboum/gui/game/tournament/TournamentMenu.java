package fr.free.totalboumboum.gui.game.tournament;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.game.tournament.TournamentRenderPanel;
import fr.free.totalboumboum.game.tournament.cup.CupTournament;
import fr.free.totalboumboum.game.tournament.league.LeagueTournament;
import fr.free.totalboumboum.game.tournament.sequence.SequenceTournament;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.InnerDataPanel;
import fr.free.totalboumboum.gui.common.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.game.match.MatchSplitPanel;
import fr.free.totalboumboum.gui.game.tournament.description.SequenceDescription;
import fr.free.totalboumboum.gui.game.tournament.results.SequenceResults;
import fr.free.totalboumboum.gui.game.tournament.statistics.TournamentStatistics;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class TournamentMenu extends InnerMenuPanel implements TournamentRenderPanel
{	private static final long serialVersionUID = 1L;
	
	private MenuPanel matchPanel;
	private InnerDataPanel tournamentDescription;
	private InnerDataPanel tournamentResults;
	private InnerDataPanel tournamentStatistics;
		
	@SuppressWarnings("unused")
	private JButton buttonQuit;
	private JButton buttonMenu;
	private JToggleButton buttonDescription;
	private JToggleButton  buttonResults;
	private JToggleButton  buttonStatistics;
	private JButton buttonMatch;
	
	public TournamentMenu(SplitMenuPanel container, MenuPanel parent) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	super(container,parent);
	
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.LINE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
		
		// buttons
		buttonQuit = GuiTools.createHorizontalMenuButton(GuiKeys.GAME_TOURNAMENT_BUTTON_QUIT,this);
		add(Box.createHorizontalGlue());
		buttonMenu = GuiTools.createHorizontalMenuButton(GuiKeys.GAME_TOURNAMENT_BUTTON_MENU,this);
		add(Box.createRigidArea(new Dimension(GuiTools.getSize(GuiTools.MENU_HORIZONTAL_BUTTON_SPACE),0)));
	    ButtonGroup group = new ButtonGroup();
		buttonDescription = GuiTools.createHorizontalMenuToggleButton(GuiKeys.GAME_TOURNAMENT_BUTTON_DESCRIPTION,this);
		buttonDescription.setSelected(true);
	    group.add(buttonDescription);
		buttonResults = GuiTools.createHorizontalMenuToggleButton(GuiKeys.GAME_TOURNAMENT_BUTTON_RESULTS,this);
	    group.add(buttonResults);
		buttonStatistics = GuiTools.createHorizontalMenuToggleButton(GuiKeys.GAME_TOURNAMENT_BUTTON_STATISTICS,this);
buttonStatistics.setEnabled(false);		
	    group.add(buttonStatistics);
		add(Box.createRigidArea(new Dimension(GuiTools.getSize(GuiTools.MENU_HORIZONTAL_BUTTON_SPACE),0)));
		buttonMatch = GuiTools.createHorizontalMenuButton(GuiKeys.GAME_TOURNAMENT_BUTTON_NEXT_MATCH,this);
		
		// panels
		tournamentDescription = null;
		AbstractTournament tournament = Configuration.getGameConfiguration().getTournament();
		if(tournament instanceof SequenceTournament)
		{	tournamentDescription = new SequenceDescription(container);
			container.setDataPart(tournamentDescription);
			tournamentResults = new SequenceResults(container);
			tournamentStatistics = new TournamentStatistics(container);		
		}
		else if(tournament instanceof CupTournament)
		{
			// NOTE à compléter
		}
		else if(tournament instanceof LeagueTournament)
		{
			// NOTE à compléter
		}
		tournament.setPanel(this);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.GAME_TOURNAMENT_BUTTON_QUIT))
		{	getFrame().setMainMenuPanel();
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_TOURNAMENT_BUTTON_MENU))
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_TOURNAMENT_BUTTON_FINISH))
		{	AbstractTournament tournament = Configuration.getGameConfiguration().getTournament();
			tournament.finish();
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
		{	replaceWith(matchPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_TOURNAMENT_BUTTON_NEXT_MATCH))
		{	AbstractTournament tournament = Configuration.getGameConfiguration().getTournament();
			tournament.progress();
			matchPanel = new MatchSplitPanel(container.getContainer(),container);
			replaceWith(matchPanel);
	    }
	}

	@Override
	public void refresh()
	{	AbstractTournament tournament = Configuration.getGameConfiguration().getTournament(); 
		if(tournament.isOver())
		{	// match
			buttonMatch.setEnabled(false);
			// finish
			GuiTools.setButtonContent(GuiKeys.GAME_TOURNAMENT_BUTTON_FINISH, buttonMenu);
		}
		else
		{	// match
			buttonMatch.setEnabled(true);
			Match match = tournament.getCurrentMatch();
			if(match==null || match.isOver())
				GuiTools.setButtonContent(GuiKeys.GAME_TOURNAMENT_BUTTON_NEXT_MATCH, buttonMatch);
			else
				GuiTools.setButtonContent(GuiKeys.GAME_TOURNAMENT_BUTTON_CURRENT_MATCH, buttonMatch);
			// menu
			GuiTools.setButtonContent(GuiKeys.GAME_TOURNAMENT_BUTTON_MENU, buttonMenu);
		}
	}

	@Override
	public void matchOver()
	{	SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	tournamentResults.updateData();
				buttonResults.doClick();
			}
		});	
	}

	@Override
	public void tournamentOver()
	{	SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	tournamentResults.updateData();
				buttonResults.doClick();
			}
		});	
	}
}

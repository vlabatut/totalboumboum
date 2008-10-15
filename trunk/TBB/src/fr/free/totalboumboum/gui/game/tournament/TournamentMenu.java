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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.game.tournament.TournamentRenderPanel;
import fr.free.totalboumboum.game.tournament.cup.CupTournament;
import fr.free.totalboumboum.game.tournament.league.LeagueTournament;
import fr.free.totalboumboum.game.tournament.sequence.SequenceTournament;
import fr.free.totalboumboum.game.tournament.single.SingleTournament;
import fr.free.totalboumboum.gui.common.panel.ContentPanel;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.InnerDataPanel;
import fr.free.totalboumboum.gui.common.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.game.match.MatchSplitPanel;
import fr.free.totalboumboum.gui.game.tournament.description.SequenceDescription;
import fr.free.totalboumboum.gui.game.tournament.description.TournamentDescription;
import fr.free.totalboumboum.gui.game.tournament.results.SequenceResults;
import fr.free.totalboumboum.gui.game.tournament.results.TournamentResults;
import fr.free.totalboumboum.gui.game.tournament.statistics.TournamentStatistics;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class TournamentMenu extends InnerMenuPanel implements TournamentRenderPanel
{	private static final long serialVersionUID = 1L;
	
	private MenuPanel matchPanel;
	private InnerDataPanel tournamentDescription;
	private InnerDataPanel tournamentResults;
	private InnerDataPanel tournamentStatistics;
		
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
		
		// size
		int height = GuiTools.getSize(GuiTools.HORIZONTAL_SPLIT_MENU_PANEL_HEIGHT);
		int width = GuiTools.getSize(GuiTools.HORIZONTAL_SPLIT_MENU_PANEL_WIDTH);
		setPreferredSize(new Dimension(width,height));
		
		// buttons
		buttonQuit = GuiTools.createHorizontalMenuButton(GuiTools.GAME_TOURNAMENT_BUTTON_QUIT,this,getConfiguration());
		add(Box.createHorizontalGlue());
		buttonMenu = GuiTools.createHorizontalMenuButton(GuiTools.GAME_TOURNAMENT_BUTTON_MENU,this,getConfiguration());
		add(Box.createRigidArea(new Dimension(GuiTools.getSize(GuiTools.MENU_HORIZONTAL_BUTTON_SPACE),0)));
	    ButtonGroup group = new ButtonGroup();
		buttonDescription = GuiTools.createHorizontalMenuToggleButton(GuiTools.GAME_TOURNAMENT_BUTTON_DESCRIPTION,this,getConfiguration());
		buttonDescription.setSelected(true);
	    group.add(buttonDescription);
		buttonResults = GuiTools.createHorizontalMenuToggleButton(GuiTools.GAME_TOURNAMENT_BUTTON_RESULTS,this,getConfiguration());
	    group.add(buttonResults);
		buttonStatistics = GuiTools.createHorizontalMenuToggleButton(GuiTools.GAME_TOURNAMENT_BUTTON_STATISTICS,this,getConfiguration());
buttonStatistics.setEnabled(false);		
	    group.add(buttonStatistics);
		add(Box.createRigidArea(new Dimension(GuiTools.getSize(GuiTools.MENU_HORIZONTAL_BUTTON_SPACE),0)));
		buttonMatch = GuiTools.createHorizontalMenuButton(GuiTools.GAME_TOURNAMENT_BUTTON_NEXT_MATCH,this,getConfiguration());
		
		// panels
		tournamentDescription = null;
		AbstractTournament tournament = getConfiguration().getCurrentTournament();
		if(tournament instanceof SequenceTournament)
		{	int h = GuiTools.getSize(GuiTools.HORIZONTAL_SPLIT_DATA_PANEL_HEIGHT);
			int w = GuiTools.getSize(GuiTools.HORIZONTAL_SPLIT_DATA_PANEL_WIDTH);
			tournamentDescription = new SequenceDescription(container,w,h);
			container.setDataPart(tournamentDescription);
			dataPart = tournamentDescription;
			tournamentResults = new SequenceResults(container,w,h);
			tournamentStatistics = new TournamentStatistics(container,w,h);		
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
	{	if(e.getActionCommand().equals(GuiTools.GAME_TOURNAMENT_BUTTON_QUIT))
		{	getFrame().setMainMenuPanel();
	    }
		else if(e.getActionCommand().equals(GuiTools.GAME_TOURNAMENT_BUTTON_MENU))
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiTools.GAME_TOURNAMENT_BUTTON_FINISH))
		{	AbstractTournament tournament = getConfiguration().getCurrentTournament();
			tournament.finish();
			replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiTools.GAME_TOURNAMENT_BUTTON_DESCRIPTION))
		{	container.setDataPart(tournamentDescription);
			dataPart = tournamentDescription;
	    }
		else if(e.getActionCommand().equals(GuiTools.GAME_TOURNAMENT_BUTTON_RESULTS))
		{	container.setDataPart(tournamentResults);
			dataPart = tournamentResults;
	    }
		else if(e.getActionCommand().equals(GuiTools.GAME_TOURNAMENT_BUTTON_STATISTICS))
		{	container.setDataPart(tournamentStatistics);
			dataPart = tournamentStatistics;
	    }
		else if(e.getActionCommand().equals(GuiTools.GAME_TOURNAMENT_BUTTON_CURRENT_MATCH))
		{	replaceWith(matchPanel);
	    }
		else if(e.getActionCommand().equals(GuiTools.GAME_TOURNAMENT_BUTTON_NEXT_MATCH))
		{	AbstractTournament tournament = getConfiguration().getCurrentTournament();
			tournament.progress();
			matchPanel = new MatchSplitPanel(container.getContainer(),container);
			replaceWith(matchPanel);
	    }
	}

	@Override
	public void refresh()
	{	AbstractTournament tournament = getConfiguration().getCurrentTournament(); 
		if(tournament.isOver())
		{	// match
			buttonMatch.setEnabled(false);
			// finish
			GuiTools.setButtonContent(GuiTools.GAME_TOURNAMENT_BUTTON_FINISH, buttonMenu, getConfiguration());
		}
		else
		{	// match
			buttonMatch.setEnabled(true);
			Match match = tournament.getCurrentMatch();
			if(match==null || match.isOver())
				GuiTools.setButtonContent(GuiTools.GAME_TOURNAMENT_BUTTON_NEXT_MATCH, buttonMatch, getConfiguration());
			else
				GuiTools.setButtonContent(GuiTools.GAME_TOURNAMENT_BUTTON_CURRENT_MATCH, buttonMatch, getConfiguration());
			// menu
			GuiTools.setButtonContent(GuiTools.GAME_TOURNAMENT_BUTTON_MENU, buttonMenu, getConfiguration());
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

package fr.free.totalboumboum.gui.game.tournament;

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
import fr.free.totalboumboum.gui.SwingTools;
import fr.free.totalboumboum.gui.game.match.MatchSplitPanel;
import fr.free.totalboumboum.gui.game.tournament.description.SequenceDescription;
import fr.free.totalboumboum.gui.game.tournament.description.TournamentDescription;
import fr.free.totalboumboum.gui.game.tournament.results.TournamentResults;
import fr.free.totalboumboum.gui.game.tournament.statistics.TournamentStatistics;
import fr.free.totalboumboum.gui.generic.ContentPanel;
import fr.free.totalboumboum.gui.generic.InnerDataPanel;
import fr.free.totalboumboum.gui.generic.InnerMenuPanel;
import fr.free.totalboumboum.gui.generic.MenuPanel;
import fr.free.totalboumboum.gui.generic.SplitMenuPanel;
import fr.free.totalboumboum.tools.GuiTools;

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
		// size
		int height = SwingTools.getSize(SwingTools.HORIZONTAL_SPLIT_MENU_PANEL_HEIGHT);
		int width = SwingTools.getSize(SwingTools.HORIZONTAL_SPLIT_MENU_PANEL_WIDTH);
		setPreferredSize(new Dimension(width,height));
		// background
		setBackground(Color.LIGHT_GRAY);
		
		// buttons
		buttonQuit = SwingTools.createHorizontalMenuButton(GuiTools.TOURNAMENT_BUTTON_QUIT,this,getConfiguration());
		add(Box.createHorizontalGlue());
		buttonMenu = SwingTools.createHorizontalMenuButton(GuiTools.TOURNAMENT_BUTTON_MENU,this,getConfiguration());
		add(Box.createRigidArea(new Dimension(SwingTools.getSize(SwingTools.MENU_HORIZONTAL_BUTTON_SPACE),0)));
	    ButtonGroup group = new ButtonGroup();
		buttonDescription = SwingTools.createHorizontalMenuToggleButton(GuiTools.TOURNAMENT_BUTTON_DESCRIPTION,this,getConfiguration());
		buttonDescription.setSelected(true);
	    group.add(buttonDescription);
		buttonResults = SwingTools.createHorizontalMenuToggleButton(GuiTools.TOURNAMENT_BUTTON_RESULTS,this,getConfiguration());
	    group.add(buttonResults);
		buttonStatistics = SwingTools.createHorizontalMenuToggleButton(GuiTools.TOURNAMENT_BUTTON_STATISTICS,this,getConfiguration());
	    group.add(buttonStatistics);
		add(Box.createRigidArea(new Dimension(SwingTools.getSize(SwingTools.MENU_HORIZONTAL_BUTTON_SPACE),0)));
		buttonMatch = SwingTools.createHorizontalMenuButton(GuiTools.TOURNAMENT_BUTTON_NEXT_MATCH,this,getConfiguration());
		
		// panels
		tournamentDescription = null;
		AbstractTournament tournament = getConfiguration().getTournament();
		if(tournament instanceof SequenceTournament)
		{	tournamentDescription = new SequenceDescription(container);
			container.setDataPart(tournamentDescription);
			dataPart = tournamentDescription;
			tournamentResults = new TournamentResults(container);
			tournamentStatistics = new TournamentStatistics(container);		
		}
		else if(tournament instanceof SingleTournament)
		{
			// NOTE à compléter
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
	{	System.out.println(e.getActionCommand());
		if(e.getActionCommand().equals(GuiTools.TOURNAMENT_BUTTON_QUIT))
		{	getFrame().setMainMenuPanel();
	    }
		else if(e.getActionCommand().equals(GuiTools.TOURNAMENT_BUTTON_MENU))
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiTools.TOURNAMENT_BUTTON_FINISH))
		{	AbstractTournament tournament = getConfiguration().getTournament();
			tournament.finish();
			replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiTools.TOURNAMENT_BUTTON_DESCRIPTION))
		{	container.setDataPart(tournamentDescription);
			dataPart = tournamentDescription;
	    }
		else if(e.getActionCommand().equals(GuiTools.TOURNAMENT_BUTTON_RESULTS))
		{	container.setDataPart(tournamentResults);
			dataPart = tournamentResults;
	    }
		else if(e.getActionCommand().equals(GuiTools.TOURNAMENT_BUTTON_STATISTICS))
		{	container.setDataPart(tournamentStatistics);
			dataPart = tournamentStatistics;
	    }
		else if(e.getActionCommand().equals(GuiTools.TOURNAMENT_BUTTON_CURRENT_MATCH))
		{	replaceWith(matchPanel);
	    }
		else if(e.getActionCommand().equals(GuiTools.TOURNAMENT_BUTTON_NEXT_MATCH))
		{	AbstractTournament tournament = getConfiguration().getTournament();
			tournament.progress();
			matchPanel = new MatchSplitPanel(container.getContainer(),container);
			replaceWith(matchPanel);
	    }
	}

	@Override
	public void refresh()
	{	AbstractTournament tournament = getConfiguration().getTournament(); 
		if(tournament.isOver())
		{	// match
			buttonMatch.setEnabled(false);
			// finish
			SwingTools.setButtonContent(GuiTools.TOURNAMENT_BUTTON_FINISH, buttonMenu, getConfiguration());
		}
		else
		{	// match
			buttonMatch.setEnabled(true);
			Match match = tournament.getCurrentMatch();
			if(match==null || match.isOver())
				SwingTools.setButtonContent(GuiTools.TOURNAMENT_BUTTON_NEXT_MATCH, buttonMatch, getConfiguration());
			else
				SwingTools.setButtonContent(GuiTools.TOURNAMENT_BUTTON_CURRENT_MATCH, buttonMatch, getConfiguration());
			// menu
			SwingTools.setButtonContent(GuiTools.TOURNAMENT_BUTTON_MENU, buttonMenu, getConfiguration());
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

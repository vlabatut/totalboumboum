package fr.free.totalboumboum.gui.game.round;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.round.RoundRenderPanel;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.game.tournament.cup.CupTournament;
import fr.free.totalboumboum.game.tournament.league.LeagueTournament;
import fr.free.totalboumboum.game.tournament.sequence.SequenceTournament;
import fr.free.totalboumboum.game.tournament.single.SingleTournament;
import fr.free.totalboumboum.gui.SwingTools;
import fr.free.totalboumboum.gui.game.loop.LoopPanel;
import fr.free.totalboumboum.gui.game.match.description.MatchDescription;
import fr.free.totalboumboum.gui.game.match.results.MatchResults;
import fr.free.totalboumboum.gui.game.match.statistics.MatchStatistics;
import fr.free.totalboumboum.gui.game.round.description.RoundDescription;
import fr.free.totalboumboum.gui.game.round.results.RoundResults;
import fr.free.totalboumboum.gui.game.round.statistics.RoundStatistics;
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

public class RoundMenu extends InnerMenuPanel implements RoundRenderPanel
{	private static final long serialVersionUID = 1L;
	
	private LoopPanel loopPanel;
	private InnerDataPanel roundDescription;
	private InnerDataPanel roundResults;
	private InnerDataPanel roundStatistics;
		
	private JButton buttonQuit;
	private JButton buttonMatch;
	private JToggleButton buttonDescription;
	private JToggleButton buttonResults;
	private JToggleButton buttonStatistics;
	private JButton buttonPlay;
	
	private JProgressBar loadProgressBar;
	
	public RoundMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container,parent);
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.LINE_AXIS); 
		setLayout(layout);
		// background
		setBackground(Color.LIGHT_GRAY);
		// size
		int height = SwingTools.getSize(SwingTools.HORIZONTAL_SPLIT_MENU_PANEL_HEIGHT);
		int width = SwingTools.getSize(SwingTools.HORIZONTAL_SPLIT_MENU_PANEL_WIDTH);
		setPreferredSize(new Dimension(width,height));
		
		// buttons
		buttonQuit = SwingTools.createHorizontalMenuButton(GuiTools.ROUND_BUTTON_QUIT,this,getConfiguration());
		add(Box.createHorizontalGlue());
		buttonMatch = SwingTools.createHorizontalMenuButton(GuiTools.ROUND_BUTTON_CURRENT_MATCH,this,getConfiguration());
		add(Box.createRigidArea(new Dimension(SwingTools.getSize(SwingTools.MENU_HORIZONTAL_BUTTON_SPACE),0)));
	    ButtonGroup group = new ButtonGroup();
		buttonDescription = SwingTools.createHorizontalMenuToggleButton(GuiTools.ROUND_BUTTON_DESCRIPTION,this,getConfiguration());
		buttonDescription.setSelected(true);
	    group.add(buttonDescription);
		buttonResults = SwingTools.createHorizontalMenuToggleButton(GuiTools.ROUND_BUTTON_RESULTS,this,getConfiguration());
	    group.add(buttonResults);
		buttonStatistics = SwingTools.createHorizontalMenuToggleButton(GuiTools.ROUND_BUTTON_STATISTICS,this,getConfiguration());
	    group.add(buttonStatistics);
		add(Box.createRigidArea(new Dimension(SwingTools.getSize(SwingTools.MENU_HORIZONTAL_BUTTON_SPACE),0)));
		buttonPlay = SwingTools.createHorizontalMenuButton(GuiTools.ROUND_BUTTON_PLAY,this,getConfiguration());
		// panels
		roundDescription = null;
		roundDescription = new RoundDescription(container);
		container.setDataPart(roundDescription);
		dataPart = roundDescription;
		roundResults = new RoundResults(container);
		roundStatistics = new RoundStatistics(container);		
//		round.init();
		// round
		Round round = getConfiguration().getTournament().getCurrentMatch().getCurrentRound();
		round.setPanel(this);
	}
	
	public void actionPerformed(ActionEvent e)
	{	System.out.println(e.getActionCommand());
		if(e.getActionCommand().equals(GuiTools.ROUND_BUTTON_QUIT))
		{	getFrame().setMainMenuPanel();
	    }
		else if(e.getActionCommand().equals(GuiTools.ROUND_BUTTON_CURRENT_MATCH))
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiTools.ROUND_BUTTON_FINISH))
		{	Round round = getConfiguration().getTournament().getCurrentMatch().getCurrentRound();
			round.finish();
			replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiTools.ROUND_BUTTON_DESCRIPTION))
		{	container.setDataPart(roundDescription);
			dataPart = roundDescription;
	    }
		else if(e.getActionCommand().equals(GuiTools.ROUND_BUTTON_RESULTS))
		{	container.setDataPart(roundResults);
			dataPart = roundResults;
	    }
		else if(e.getActionCommand().equals(GuiTools.ROUND_BUTTON_STATISTICS))
		{	container.setDataPart(roundStatistics);
			dataPart = roundStatistics;
	    }
		else if(e.getActionCommand().equals(GuiTools.ROUND_BUTTON_PLAY))
		{	buttonPlay.setEnabled(false);
			buttonQuit.setEnabled(false);
			buttonMatch.setEnabled(false);
			Round round = getConfiguration().getTournament().getCurrentMatch().getCurrentRound();
			int limit = round.getProfiles().size()+3;
			loadProgressBar = new JProgressBar(0,limit);
			Font font = getConfiguration().getFont().deriveFont((float)SwingTools.getSize(SwingTools.GAME_PROGRESSBAR_FONT_SIZE));
			loadProgressBar.setFont(font);
			loadProgressBar.setStringPainted(true); 
			String text = getConfiguration().getLanguage().getText(GuiTools.ROUND_PROGRESSBAR_BOMBSET);
			loadProgressBar.setString(text);
			int width = Integer.MAX_VALUE;
			int height = SwingTools.getSize(SwingTools.MENU_HORIZONTAL_BUTTON_HEIGHT);
			Dimension dim = new Dimension(width,height);
			loadProgressBar.setMaximumSize(dim);
//			loadProgressBar.setPreferredSize(dim);
			remove(1);
			add(loadProgressBar,1);
			validate();
			repaint();
			try
			{
				round.progress();
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
//			Thread t = new Thread(new RoundThreadLoader(round));
//			t.start();				
	    }
	} 

	@Override
	public void refresh()
	{	Round round = getConfiguration().getTournament().getCurrentMatch().getCurrentRound();
		if(round.isOver())
		{	// play
			buttonPlay.setEnabled(false);
			// finish
			SwingTools.setButtonContent(GuiTools.ROUND_BUTTON_FINISH, buttonMatch, getConfiguration());
		}
		else
		{	// play
			buttonPlay.setEnabled(true);
			// match
			SwingTools.setButtonContent(GuiTools.ROUND_BUTTON_CURRENT_MATCH, buttonMatch, getConfiguration());
		}
	} 

	@Override
	public Image getImage()
	{	return getFrame().getImage();
	}

	@Override
	public void roundOver()
	{	SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	// remove progress bar
				remove(1);
				add(Box.createHorizontalGlue(),1);
				//
				buttonMatch.setEnabled(true);
				buttonQuit.setEnabled(true);
				roundResults.updateData();
				buttonResults.doClick();
			}
		});	
	}

	@Override
	public void loadStepOver()
	{	int val = loadProgressBar.getValue();
		loadProgressBar.setValue(val+1);
		String text;
		switch(val)
		{	// itemset
			case 0:
				text = getConfiguration().getLanguage().getText(GuiTools.ROUND_PROGRESSBAR_ITEMSET);
				loadProgressBar.setString(text);
				loadProgressBar.repaint();
				break;
			// theme
			case 1:
				text = getConfiguration().getLanguage().getText(GuiTools.ROUND_PROGRESSBAR_THEME);
				loadProgressBar.setString(text);
				loadProgressBar.repaint();
				break;
			// zone
			case 2:
				text = getConfiguration().getLanguage().getText(GuiTools.ROUND_PROGRESSBAR_ZONE);
				loadProgressBar.setString(text);
				loadProgressBar.repaint();
				break;
			// players
			default:
				Round round = getConfiguration().getTournament().getCurrentMatch().getCurrentRound();
				if(val==round.getProfiles().size()+3)
				{	text = getConfiguration().getLanguage().getText(GuiTools.ROUND_PROGRESSBAR_COMPLETE);
					loadProgressBar.setString(text);
					loadProgressBar.repaint();
					loopPanel = new LoopPanel(container.getContainer(),container);
					replaceWith(loopPanel);
					loopPanel.start();
				}
				else
				{	text = getConfiguration().getLanguage().getText(GuiTools.ROUND_PROGRESSBAR_PLAYER)+" "+(val-2);
					loadProgressBar.setString(text);
					loadProgressBar.repaint();
				}
				break;
		}
	}
}

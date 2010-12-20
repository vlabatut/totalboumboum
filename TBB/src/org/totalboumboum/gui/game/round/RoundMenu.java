package org.totalboumboum.gui.game.round;

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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.round.RoundRenderPanel;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.game.tournament.single.SingleTournament;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.game.loop.LoopPanel;
import org.totalboumboum.gui.game.match.MatchSplitPanel;
import org.totalboumboum.gui.game.round.description.RoundDescription;
import org.totalboumboum.gui.game.round.results.RoundResults;
import org.totalboumboum.gui.game.round.statistics.RoundStatistics;
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
public class RoundMenu extends InnerMenuPanel implements RoundRenderPanel,ClientGeneralConnectionListener
{	private static final long serialVersionUID = 1L;
	
	public RoundMenu(SplitMenuPanel container, MenuPanel parent)
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
		buttonQuit = GuiTools.createButton(GuiKeys.GAME_ROUND_BUTTON_QUIT,buttonWidth,buttonHeight,1,this);
		buttonSave = GuiTools.createButton(GuiKeys.GAME_ROUND_BUTTON_SAVE,buttonWidth,buttonHeight,1,this);
		buttonRecord = GuiTools.createToggleButton(GuiKeys.GAME_ROUND_BUTTON_RECORD_GAMES,buttonWidth,buttonHeight,1,this);
		add(Box.createHorizontalGlue());
		buttonMatch = GuiTools.createButton(GuiKeys.GAME_ROUND_BUTTON_CURRENT_MATCH,buttonWidth,buttonHeight,1,this);
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
	    ButtonGroup group = new ButtonGroup();
	    buttonDescription = GuiTools.createToggleButton(GuiKeys.GAME_ROUND_BUTTON_DESCRIPTION,buttonWidth,buttonHeight,1,this);
		buttonDescription.setSelected(true);
	    group.add(buttonDescription);
	    buttonResults = GuiTools.createToggleButton(GuiKeys.GAME_ROUND_BUTTON_RESULTS,buttonWidth,buttonHeight,1,this);
	    group.add(buttonResults);
	    buttonStatistics = GuiTools.createToggleButton(GuiKeys.GAME_ROUND_BUTTON_STATISTICS,buttonWidth,buttonHeight,1,this);
buttonStatistics.setEnabled(false);		
	    group.add(buttonStatistics);
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		buttonPlay = GuiTools.createButton(GuiKeys.GAME_ROUND_BUTTON_PLAY,buttonWidth,buttonHeight,1,this);
		buttonPlay.setEnabled(false);		
		
		// panels
		roundDescription = new RoundDescription(container);
		container.setDataPart(roundDescription);
		roundResults = new RoundResults(container);
		roundStatistics = new RoundStatistics(container);
	}

	/////////////////////////////////////////////////////////////////
	// BUTTONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private JButton buttonQuit;
	private JButton buttonSave;
	private JToggleButton buttonRecord;
	private JButton buttonMatch;
	private JToggleButton buttonDescription;
	private JToggleButton buttonResults;
	private JToggleButton buttonStatistics;
	private JButton buttonPlay;
	
	private Thread thread = null;
	
	public void refreshButtons()
	{	if(round!=null)
		{	if(round.isOver())
			{	// play
				buttonPlay.setEnabled(false);
				// finish
				GuiTools.setButtonContent(GuiKeys.GAME_ROUND_BUTTON_FINISH, buttonMatch);
			}
			else
			{	// play
				buttonPlay.setEnabled(true);
				// match
				GuiTools.setButtonContent(GuiKeys.GAME_ROUND_BUTTON_CURRENT_MATCH, buttonMatch);
			}
		}
		else
		{	// play
			buttonPlay.setEnabled(false);
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
		{	// play round
			if(buttonPlay.isEnabled())
			{	thread = new Thread("TBB.autoadvance")
				{	public void run()
					{	try
						{	sleep(Configuration.getAisConfiguration().getAutoAdvanceDelay());
							SwingUtilities.invokeLater(new Runnable()
							{	public void run()
								{	buttonPlay.doClick();
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
			// go back to match
			else if(buttonMatch.isEnabled())
			{	thread = new Thread("TBB.autoadvance")
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
	// ROUND			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Round round;

	public void setRound(Round round)
	{	// round
		if(round!=null)
			round.setPanel(null);
		this.round = round;
		round.setPanel(this);
		
		// panels
		roundDescription.setRound(round);
		roundResults.setRound(round);
		roundStatistics.setRound(round);	
		
		// buttons
		refreshButtons();
		
		// connection
		ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
		if(connection!=null)
			connection.addListener(this);
	}
	
	public Round getRound()
	{	return round;	
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void quitTournament()
	{	// end round
		round.cancel();
		
		// end possible connection
		Configuration.getConnectionsConfiguration().terminateConnection();
		
		// set main menu frame
		getFrame().setMainMenuPanel();
    }

	/////////////////////////////////////////////////////////////////
	// PANELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private LoopPanel loopPanel;
	private RoundDescription roundDescription;
	private RoundResults roundResults;
	private RoundStatistics roundStatistics;

	private void refreshPanels()
	{	roundDescription.refresh();
		roundResults.refresh();
		roundStatistics.refresh();	
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
		if(e.getActionCommand().equals(GuiKeys.GAME_ROUND_BUTTON_QUIT))
		{	quitTournament();
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_ROUND_BUTTON_SAVE))
		{	SaveSplitPanel savePanel = new SaveSplitPanel(container.getMenuContainer(),container);
			savePanel.setTournament(round.getMatch().getTournament());
			replaceWith(savePanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_ROUND_BUTTON_RECORD_GAMES))
		{	boolean recordGames = buttonRecord.isSelected();
			Configuration.getEngineConfiguration().setRecordRounds(recordGames);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_ROUND_BUTTON_CURRENT_MATCH))
		{	parent.refresh();

			// possibly updating client state
			ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
			if(connection!=null)
			{	if(round.getMatch().getTournament() instanceof SingleTournament)
					connection.getActiveConnection().setState(ClientState.BROWSING_TOURNAMENT);
				else
					connection.getActiveConnection().setState(ClientState.BROWSING_MATCH);
			}
			
			replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_ROUND_BUTTON_FINISH))
		{	round.finish();
			parent.refresh();
			if(parent instanceof MatchSplitPanel)
				((MatchSplitPanel)parent).autoAdvance();
			else
				((TournamentSplitPanel)parent).autoAdvance();

			// possibly updating client state
			ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
			if(connection!=null)
			{	if(round.getMatch().getTournament() instanceof SingleTournament)
					connection.getActiveConnection().setState(ClientState.BROWSING_TOURNAMENT);
				else
					connection.getActiveConnection().setState(ClientState.BROWSING_MATCH);
			}
			
			replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_ROUND_BUTTON_DESCRIPTION))
		{	container.setDataPart(roundDescription);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_ROUND_BUTTON_RESULTS))
		{	container.setDataPart(roundResults);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_ROUND_BUTTON_STATISTICS))
		{	container.setDataPart(roundStatistics);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_ROUND_BUTTON_PLAY))
		{	// init
			List<Profile> profiles = round.getProfiles();
		
			// common
			buttonPlay.setEnabled(false);
			buttonQuit.setEnabled(false);
			buttonRecord.setEnabled(false);
			buttonSave.setEnabled(false);
			buttonMatch.setEnabled(false);
			int fontSize = GuiTools.getFontSize(getHeight()*0.6);
			Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)fontSize);
			int width = Integer.MAX_VALUE;
			int height = getHeight();
			Dimension dim = new Dimension(width,height);
			
			// simulation
			if(round.isSimulated())
			{	// create progress bar
				progressBar = new JProgressBar();
				progressBar.setFont(font);
				progressBar.setStringPainted(true); 
				String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_ROUND_PROGRESSBAR_SIMULATION);
				progressBar.setString(text);
				progressBar.setMaximumSize(dim);
				remove(progressBarPosition);
				add(progressBar,progressBarPosition);
				validate();
				repaint();
				// start simulation
				round.simulate();
			}
			// actual game
			else
			{	// create progress bar
				int limit = profiles.size()+4;
				progressBar = new JProgressBar(0,limit);
				progressBar.setFont(font);
				progressBar.setStringPainted(true); 
				String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_ROUND_PROGRESSBAR_FIRESETMAP);
				progressBar.setString(text);
				progressBar.setMaximumSize(dim);
				remove(progressBarPosition);
				add(progressBar,progressBarPosition);
				validate();
				repaint();
				// round advance
				try
				{	round.progress();
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
	// ROUND RENDER PANEL	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private JProgressBar progressBar;
	private final int progressBarPosition = 3; 
	
	@Override
	public void roundOver()
	{	SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	// remove progress bar
				remove(progressBarPosition);
				add(Box.createHorizontalGlue(),progressBarPosition);
				//
				buttonMatch.setEnabled(true);
				buttonQuit.setEnabled(true);
				buttonRecord.setEnabled(true);
				buttonSave.setEnabled(true);
				roundResults.refresh();
				buttonResults.doClick();
			}
		});
	}

	@Override
	public void loadStepOver()
	{	int val = progressBar.getValue();
		String text;
		switch(val)
		{	// firesetmap
			case 0:
				text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_ROUND_PROGRESSBAR_BOMBSET);
				progressBar.setString(text);
				progressBar.repaint();
				break;
			// itemset
			case 1:
				text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_ROUND_PROGRESSBAR_ITEMSET);
				progressBar.setString(text);
				progressBar.repaint();
				break;
			// theme
			case 2:
				text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_ROUND_PROGRESSBAR_THEME);
				progressBar.setString(text);
				progressBar.repaint();
				break;
			// players
			default:
				if(val==round.getProfiles().size()+3)
				{	text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_ROUND_PROGRESSBAR_COMPLETE);
					progressBar.setString(text);
					progressBar.repaint();
					loopPanel = new LoopPanel(container.getMenuContainer(),container,(VisibleLoop)round.getLoop());
					replaceWith(loopPanel);
					loopPanel.start();
				}
				else
				{	text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_ROUND_PROGRESSBAR_PLAYER)+" "+(val-2);
					progressBar.setString(text);
					progressBar.repaint();
				}
				break;
		}
		progressBar.setValue(val+1);
	}

	@Override
	public void simulationStepOver()
	{	int val = progressBar.getValue();
		val++;
		progressBar.setValue(val);
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
		if(connection.getState()==ClientState.BROWSING_ROUND)
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

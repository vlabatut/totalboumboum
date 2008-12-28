package fr.free.totalboumboum.gui.game.round;

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
import java.awt.Font;
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

import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.round.RoundRenderPanel;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.game.loop.LoopPanel;
import fr.free.totalboumboum.gui.game.round.description.RoundDescription;
import fr.free.totalboumboum.gui.game.round.results.RoundResults;
import fr.free.totalboumboum.gui.game.round.statistics.RoundStatistics;
import fr.free.totalboumboum.gui.game.save.SaveSplitPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class RoundMenu extends InnerMenuPanel implements RoundRenderPanel
{	private static final long serialVersionUID = 1L;
	
	private LoopPanel loopPanel;
	private RoundDescription roundDescription;
	private RoundResults roundResults;
	private RoundStatistics roundStatistics;
		
	private JButton buttonQuit;
	private JButton buttonSave;
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
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
		
		// sizes
		int buttonWidth = getHeight();
		int buttonHeight = getHeight();

		// buttons
		buttonQuit = GuiTools.createButton(GuiKeys.GAME_ROUND_BUTTON_QUIT,buttonWidth,buttonHeight,1,this);
		buttonSave = GuiTools.createButton(GuiKeys.GAME_TOURNAMENT_BUTTON_SAVE,buttonWidth,buttonHeight,1,this);
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
	}
	
	public Round getRound()
	{	return round;	
	}

	/////////////////////////////////////////////////////////////////
	// REFRESH	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void refreshButtons()
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
	}
	
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
	{	if(e.getActionCommand().equals(GuiKeys.GAME_ROUND_BUTTON_QUIT))
		{	round.cancel();
			getFrame().setMainMenuPanel();
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_TOURNAMENT_BUTTON_SAVE))
		{	SaveSplitPanel savePanel = new SaveSplitPanel(container.getContainer(),container);
			savePanel.setTournament(round.getMatch().getTournament());
			replaceWith(savePanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_ROUND_BUTTON_CURRENT_MATCH))
		{	parent.refresh();
			replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_ROUND_BUTTON_FINISH))
		{	round.finish();
			parent.refresh();
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
		{	buttonPlay.setEnabled(false);
			buttonQuit.setEnabled(false);
			buttonSave.setEnabled(false);
			buttonMatch.setEnabled(false);
			int limit = round.getProfiles().size()+3;
			loadProgressBar = new JProgressBar(0,limit);
			int fontSize = GuiTools.getFontSize(getHeight()*0.6);
			Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)fontSize);
			loadProgressBar.setFont(font);
			loadProgressBar.setStringPainted(true); 
			String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_ROUND_PROGRESSBAR_BOMBSET);
			loadProgressBar.setString(text);
			int width = Integer.MAX_VALUE;
			int height = getHeight();
			Dimension dim = new Dimension(width,height);
			loadProgressBar.setMaximumSize(dim);
			remove(2);
			add(loadProgressBar,2);
			validate();
			repaint();
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
	@Override
	public void roundOver()
	{	SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	// remove progress bar
				remove(2);
				add(Box.createHorizontalGlue(),2);
				//
				buttonMatch.setEnabled(true);
				buttonQuit.setEnabled(true);
				buttonSave.setEnabled(true);
				roundResults.refresh();
				buttonResults.doClick();
			}
		});	
	}

	@Override
	public void loadStepOver()
	{	int val = loadProgressBar.getValue();
		String text;
		switch(val)
		{	// itemset
			case 0:
				text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_ROUND_PROGRESSBAR_ITEMSET);
				loadProgressBar.setString(text);
				loadProgressBar.repaint();
				break;
			// theme
			case 1:
				text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_ROUND_PROGRESSBAR_THEME);
				loadProgressBar.setString(text);
				loadProgressBar.repaint();
				break;
			// players
			default:
				if(val==round.getProfiles().size()+2)
				{	text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_ROUND_PROGRESSBAR_COMPLETE);
					loadProgressBar.setString(text);
					loadProgressBar.repaint();
					loopPanel = new LoopPanel(container.getContainer(),container,round.getLoop());
					replaceWith(loopPanel);
					loopPanel.start();
				}
				else
				{	text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_ROUND_PROGRESSBAR_PLAYER)+" "+(val-1);
					loadProgressBar.setString(text);
					loadProgressBar.repaint();
				}
				break;
		}
		loadProgressBar.setValue(val+1);
	}
}

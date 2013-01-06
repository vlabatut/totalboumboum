package org.totalboumboum.gui.menus.replay.round;

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

import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.round.RoundRenderPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.game.loop.LoopPanel;
import org.totalboumboum.gui.game.round.description.RoundDescription;
import org.totalboumboum.gui.game.round.results.RoundResults;
import org.totalboumboum.gui.game.round.statistics.RoundStatistics;
import org.totalboumboum.gui.tools.GuiButtonTools;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiFontTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.gui.tools.GuiImageTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class RoundMenu extends InnerMenuPanel implements RoundRenderPanel
{	private static final long serialVersionUID = 1L;
	
	public RoundMenu(SplitMenuPanel container, MenuPanel parent)
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
		buttonQuit = GuiButtonTools.createButton(GuiKeys.MENU_REPLAY_ROUND_BUTTON_QUIT,buttonWidth,buttonHeight,1,this);
		add(Box.createHorizontalGlue());
		buttonBack = GuiButtonTools.createButton(GuiKeys.MENU_REPLAY_ROUND_BUTTON_BACK,buttonWidth,buttonHeight,1,this);
		add(Box.createRigidArea(new Dimension(GuiSizeTools.buttonHorizontalSpace,0)));
	    ButtonGroup group = new ButtonGroup();
	    buttonDescription = GuiButtonTools.createToggleButton(GuiKeys.GAME_ROUND_BUTTON_DESCRIPTION,buttonWidth,buttonHeight,1,this);
		buttonDescription.setSelected(true);
	    group.add(buttonDescription);
	    buttonResults = GuiButtonTools.createToggleButton(GuiKeys.GAME_ROUND_BUTTON_RESULTS,buttonWidth,buttonHeight,1,this);
	    group.add(buttonResults);
	    buttonStatistics = GuiButtonTools.createToggleButton(GuiKeys.GAME_ROUND_BUTTON_STATISTICS,buttonWidth,buttonHeight,1,this);
buttonStatistics.setEnabled(false);		
	    group.add(buttonStatistics);
		add(Box.createRigidArea(new Dimension(GuiSizeTools.buttonHorizontalSpace,0)));
		buttonReplay = GuiButtonTools.createButton(GuiKeys.MENU_REPLAY_ROUND_BUTTON_REPLAY,buttonWidth,buttonHeight,1,this);
		buttonReplay.setEnabled(false);		
		
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
	private JButton buttonBack;
	private JToggleButton buttonDescription;
	private JToggleButton buttonResults;
	private JToggleButton buttonStatistics;
	private JButton buttonReplay;
	
	private Thread thread = null;
	
	private void refreshButtons()
	{	if(round!=null)
		{	if(round.isOver())
			{	// play
				buttonReplay.setEnabled(false);
				// finish
				GuiButtonTools.setButtonContent(GuiKeys.MENU_REPLAY_ROUND_BUTTON_QUIT, buttonQuit);
			}
			else
			{	// play
				buttonReplay.setEnabled(true);
				// match
				GuiButtonTools.setButtonContent(GuiKeys.MENU_REPLAY_ROUND_BUTTON_BACK, buttonBack);
			}
		}
		else
		{	// play
			buttonReplay.setEnabled(false);
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
	}
	
	public Round getRound()
	{	return round;	
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
		if(e.getActionCommand().equals(GuiKeys.MENU_REPLAY_ROUND_BUTTON_QUIT))
		{	round.cancel();
			getFrame().setMainMenuPanel();
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_REPLAY_ROUND_BUTTON_BACK))
		{	parent.refresh();
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
		else if(e.getActionCommand().equals(GuiKeys.MENU_REPLAY_ROUND_BUTTON_REPLAY))
		{	// init
			List<Profile> profiles = round.getProfiles();
		
			// common
			buttonReplay.setEnabled(false);
			buttonQuit.setEnabled(false);
			buttonBack.setEnabled(false);
			int fontSize = GuiFontTools.getFontSize(getHeight()*0.6);
			Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)fontSize);
			int width = Integer.MAX_VALUE;
			int height = getHeight();
			Dimension dim = new Dimension(width,height);
			
			// create progress bar
			int limit = profiles.size()+4;
			progressBar = new JProgressBar(0,limit);
			progressBar.setFont(font);
			progressBar.setStringPainted(true); 
			String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_ROUND_PROGRESSBAR_FIRESETMAP);
			progressBar.setString(text);
			progressBar.setMaximumSize(dim);
			remove(2);
			add(progressBar,2);
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
	
	@Override
	public void roundOver()
	{	SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	// remove progress bar
				remove(2);
				add(Box.createHorizontalGlue(),2);
				//
				buttonBack.setEnabled(true);
				buttonQuit.setEnabled(true);
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
}

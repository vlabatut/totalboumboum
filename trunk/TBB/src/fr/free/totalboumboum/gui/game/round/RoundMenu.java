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

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.round.RoundRenderPanel;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.InnerDataPanel;
import fr.free.totalboumboum.gui.common.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.game.loop.LoopPanel;
import fr.free.totalboumboum.gui.game.round.description.RoundDescription;
import fr.free.totalboumboum.gui.game.round.results.RoundResults;
import fr.free.totalboumboum.gui.game.round.statistics.RoundStatistics;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

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
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
		
		// buttons
		buttonQuit = GuiTools.createHorizontalMenuButton(GuiKeys.GAME_ROUND_BUTTON_QUIT,this);
		add(Box.createHorizontalGlue());
		buttonMatch = GuiTools.createHorizontalMenuButton(GuiKeys.GAME_ROUND_BUTTON_CURRENT_MATCH,this);
		add(Box.createRigidArea(new Dimension(GuiTools.getSize(GuiTools.MENU_HORIZONTAL_BUTTON_SPACE),0)));
	    ButtonGroup group = new ButtonGroup();
		buttonDescription = GuiTools.createHorizontalMenuToggleButton(GuiKeys.GAME_ROUND_BUTTON_DESCRIPTION,this);
		buttonDescription.setSelected(true);
	    group.add(buttonDescription);
		buttonResults = GuiTools.createHorizontalMenuToggleButton(GuiKeys.GAME_ROUND_BUTTON_RESULTS,this);
	    group.add(buttonResults);
		buttonStatistics = GuiTools.createHorizontalMenuToggleButton(GuiKeys.GAME_ROUND_BUTTON_STATISTICS,this);
buttonStatistics.setEnabled(false);		
	    group.add(buttonStatistics);
		add(Box.createRigidArea(new Dimension(GuiTools.getSize(GuiTools.MENU_HORIZONTAL_BUTTON_SPACE),0)));
		buttonPlay = GuiTools.createHorizontalMenuButton(GuiKeys.GAME_ROUND_BUTTON_PLAY,this);
		
		// panels
		{	roundDescription = null;
			roundDescription = new RoundDescription(container);
			container.setDataPart(roundDescription);
			roundResults = new RoundResults(container);
			roundStatistics = new RoundStatistics(container);
		}
		
		// round
		Round round = Configuration.getGameConfiguration().getTournament().getCurrentMatch().getCurrentRound();
		round.setPanel(this);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.GAME_ROUND_BUTTON_QUIT))
		{	getFrame().setMainMenuPanel();
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_ROUND_BUTTON_CURRENT_MATCH))
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_ROUND_BUTTON_FINISH))
		{	Round round = Configuration.getGameConfiguration().getTournament().getCurrentMatch().getCurrentRound();
			round.finish();
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
			buttonMatch.setEnabled(false);
			Round round = Configuration.getGameConfiguration().getTournament().getCurrentMatch().getCurrentRound();
			int limit = round.getProfiles().size()+2;
			loadProgressBar = new JProgressBar(0,limit);
			Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)GuiTools.getSize(GuiTools.GAME_PROGRESSBAR_FONT_SIZE));
			loadProgressBar.setFont(font);
			loadProgressBar.setStringPainted(true); 
			String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_ROUND_PROGRESSBAR_BOMBSET);
			loadProgressBar.setString(text);
			int width = Integer.MAX_VALUE;
			int height = GuiTools.getSize(GuiTools.MENU_HORIZONTAL_BUTTON_HEIGHT);
			Dimension dim = new Dimension(width,height);
			loadProgressBar.setMaximumSize(dim);
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
	    }
	} 

	@Override
	public void refresh()
	{	Round round = Configuration.getGameConfiguration().getTournament().getCurrentMatch().getCurrentRound();
		if(round.isOver())
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
				Round round = Configuration.getGameConfiguration().getTournament().getCurrentMatch().getCurrentRound();
				if(val==round.getProfiles().size()+2)
				{	text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_ROUND_PROGRESSBAR_COMPLETE);
					loadProgressBar.setString(text);
					loadProgressBar.repaint();
					loopPanel = new LoopPanel(container.getContainer(),container);
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
	}
}

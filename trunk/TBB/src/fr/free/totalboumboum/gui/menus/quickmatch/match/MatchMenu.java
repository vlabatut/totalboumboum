package fr.free.totalboumboum.gui.menus.quickmatch.match;

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
import java.awt.Graphics;
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
import fr.free.totalboumboum.game.match.MatchRenderPanel;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.InnerDataPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.game.match.description.MatchDescription;
import fr.free.totalboumboum.gui.game.match.results.MatchResults;
import fr.free.totalboumboum.gui.game.match.statistics.MatchStatistics;
import fr.free.totalboumboum.gui.game.round.RoundSplitPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class MatchMenu extends InnerMenuPanel implements MatchRenderPanel
{	private static final long serialVersionUID = 1L;
	
	private RoundSplitPanel roundPanel;
	private InnerDataPanel matchDescription;
	private InnerDataPanel matchResults;
	private InnerDataPanel matchStatistics;
		
	@SuppressWarnings("unused")
	private JButton buttonQuit;
	private JButton buttonTournament;
	private JToggleButton buttonDescription;
	private JToggleButton buttonResults;
	private JToggleButton buttonStatistics;
	private JButton buttonRound;
	
	private Match match;
	
	public MatchMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container,parent);
	
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.LINE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
		
		// sizes
		int buttonWidth = getHeight();
		int buttonHeight = getHeight();
//		ArrayList<String> texts = GuiKeys.getKeysLike(GuiKeys.MENU_TOURNAMENT_BUTTON);
//		int fontSize = GuiTools.getOptimalFontSize(buttonWidth, buttonHeight, texts);

		// buttons
		buttonQuit = GuiTools.createButton(GuiKeys.GAME_MATCH_BUTTON_QUIT,buttonWidth,buttonHeight,1,this);
		add(Box.createHorizontalGlue());
		buttonTournament = GuiTools.createButton(GuiKeys.GAME_MATCH_BUTTON_CURRENT_TOURNAMENT,buttonWidth,buttonHeight,1,this);
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
	    ButtonGroup group = new ButtonGroup();
	    buttonDescription = GuiTools.createToggleButton(GuiKeys.GAME_MATCH_BUTTON_DESCRIPTION,buttonWidth,buttonHeight,1,this);
		buttonDescription.setSelected(true);
	    group.add(buttonDescription);
	    buttonResults = GuiTools.createToggleButton(GuiKeys.GAME_MATCH_BUTTON_RESULTS,buttonWidth,buttonHeight,1,this);
	    group.add(buttonResults);
	    buttonStatistics = GuiTools.createToggleButton(GuiKeys.GAME_MATCH_BUTTON_STATISTICS,buttonWidth,buttonHeight,1,this);
buttonStatistics.setEnabled(false);		
	    group.add(buttonStatistics);
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		buttonRound = GuiTools.createButton(GuiKeys.GAME_MATCH_BUTTON_NEXT_ROUND,buttonWidth,buttonHeight,1,this);
		
		// panels
		{	matchDescription = new MatchDescription(container);
			container.setDataPart(matchDescription);
			matchResults = new MatchResults(container);
			matchStatistics = new MatchStatistics(container);		
		}
		
		match = Configuration.getGameConfiguration().getTournament().getCurrentMatch();
		match.setPanel(this);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.GAME_MATCH_BUTTON_QUIT))
		{	getFrame().setMainMenuPanel();
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_MATCH_BUTTON_CURRENT_TOURNAMENT))				
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_MATCH_BUTTON_FINISH))
		{	match.finish();
			replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_MATCH_BUTTON_DESCRIPTION))
		{	container.setDataPart(matchDescription);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_MATCH_BUTTON_RESULTS))
		{	container.setDataPart(matchResults);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_MATCH_BUTTON_STATISTICS))
		{	container.setDataPart(matchStatistics);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_MATCH_BUTTON_CURRENT_ROUND))
		{	replaceWith(roundPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_MATCH_BUTTON_NEXT_ROUND))
		{	try
			{	Configuration.getGameConfiguration().getTournament().getCurrentMatch().progress();
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
			roundPanel = new RoundSplitPanel(container.getContainer(),container);
			Round round = match.getCurrentRound();		
			roundPanel.setRound(round);
			replaceWith(roundPanel);
	    }
	} 

	@Override
	public void refresh()
	{	if(match.isOver())
		{	// Round
			buttonRound.setEnabled(false);
			// Finish
			GuiTools.setButtonContent(GuiKeys.GAME_MATCH_BUTTON_FINISH, buttonTournament);
		}
		else
		{	// Round
			buttonRound.setEnabled(true);
			Round round = match.getCurrentRound();
			if(round==null || round.isOver())
				GuiTools.setButtonContent(GuiKeys.GAME_MATCH_BUTTON_NEXT_ROUND, buttonRound);
			else
				GuiTools.setButtonContent(GuiKeys.GAME_MATCH_BUTTON_CURRENT_ROUND, buttonRound);
			// Tournament
			GuiTools.setButtonContent(GuiKeys.GAME_MATCH_BUTTON_CURRENT_TOURNAMENT, buttonTournament);
		}
	}

	@Override
	public void matchOver()
	{	SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	matchResults.refresh();
				buttonResults.doClick();
			}
		});	
	}

	@Override
	public void roundOver()
	{	SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	matchResults.refresh();
				buttonResults.doClick();
			}
		});	
	}
	
	@Override
    protected void paintComponent(Graphics g)
	{	//g.clearRect(0, 0, getWidth(), getHeight());
//		getParent().paintComponents(g);
		super.paintComponent(g);
    }
}

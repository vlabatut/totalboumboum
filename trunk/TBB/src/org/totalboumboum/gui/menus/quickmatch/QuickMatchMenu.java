package org.totalboumboum.gui.menus.quickmatch;

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
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.game.quickmatch.LevelsSelection;
import org.totalboumboum.configuration.game.quickmatch.QuickMatchConfiguration;
import org.totalboumboum.configuration.game.quickmatch.QuickMatchConfigurationSaver;
import org.totalboumboum.configuration.game.quickmatch.QuickMatchDraw;
import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.configuration.profile.ProfilesConfiguration;
import org.totalboumboum.configuration.profile.ProfilesSelection;
import org.totalboumboum.engine.container.level.hollow.HollowLevel;
import org.totalboumboum.engine.container.level.hollow.HollowLevelLoader;
import org.totalboumboum.game.limit.Comparisons;
import org.totalboumboum.game.limit.LimitConfrontation;
import org.totalboumboum.game.limit.LimitLastStanding;
import org.totalboumboum.game.limit.LimitPoints;
import org.totalboumboum.game.limit.LimitTime;
import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.game.limit.MatchLimit;
import org.totalboumboum.game.limit.RoundLimit;
import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.points.PointsConstant;
import org.totalboumboum.game.points.PointsProcessor;
import org.totalboumboum.game.points.PointsRankpoints;
import org.totalboumboum.game.points.PointsScores;
import org.totalboumboum.game.points.PointsTotal;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.tournament.single.SingleTournament;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.DataPanelListener;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.game.tournament.TournamentSplitPanel;
import org.totalboumboum.gui.menus.quickmatch.LevelsData;
import org.totalboumboum.gui.menus.quickmatch.PlayersData;
import org.totalboumboum.gui.menus.quickmatch.SettingsData;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.statistics.detailed.Score;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class QuickMatchMenu extends InnerMenuPanel implements DataPanelListener
{	private static final long serialVersionUID = 1L;
	
	public QuickMatchMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.LINE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);

		// buttons
		initButtons();	

		// panels
		playersData = new PlayersData(container);
		playersData.addListener(this);
		levelsData = new LevelsData(container);
		levelsData.addListener(this);
		settingsData = new SettingsData(container);
		settingsData.addListener(this);
		tournamentPanel = new TournamentSplitPanel(container.getMenuContainer(),getMenuParent());
	}
	
	/////////////////////////////////////////////////////////////////
	// BUTTONS						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private JButton buttonQuit;
	private JButton buttonPlayersPrevious;
	private JButton buttonPlayersNext;
	private JButton buttonLevelsPrevious;
	private JButton buttonLevelsNext;
	private JButton buttonSettingsPrevious;
	private JButton buttonSettingsNext;
	private JButton buttonPublish;
	private JToggleButton buttonBlockPlayers;
	private int buttonWidth;
	private int buttonHeight;

	private void initButtons()
	{	buttonWidth = getHeight();
		buttonHeight = getHeight();
		//
		buttonQuit = GuiTools.createButton(GuiKeys.MENU_QUICKMATCH_SETTINGS_BUTTON_QUIT,buttonWidth,buttonHeight,1,this);
		buttonPlayersPrevious = GuiTools.createButton(GuiKeys.MENU_QUICKMATCH_PLAYERS_BUTTON_PREVIOUS,buttonWidth,buttonHeight,1,this);
		buttonLevelsPrevious = GuiTools.createButton(GuiKeys.MENU_QUICKMATCH_LEVELS_BUTTON_PREVIOUS,buttonWidth,buttonHeight,1,this);
		buttonSettingsPrevious = GuiTools.createButton(GuiKeys.MENU_QUICKMATCH_SETTINGS_BUTTON_PREVIOUS,buttonWidth,buttonHeight,1,this);
		buttonPlayersNext = GuiTools.createButton(GuiKeys.MENU_QUICKMATCH_PLAYERS_BUTTON_NEXT,buttonWidth,buttonHeight,1,this);
		buttonLevelsNext = GuiTools.createButton(GuiKeys.MENU_QUICKMATCH_LEVELS_BUTTON_NEXT,buttonWidth,buttonHeight,1,this);
		buttonSettingsNext = GuiTools.createButton(GuiKeys.MENU_QUICKMATCH_SETTINGS_BUTTON_NEXT,buttonWidth,buttonHeight,1,this);
		buttonPublish = GuiTools.createButton(GuiKeys.MENU_QUICKMATCH_SETTINGS_BUTTON_PUBLISH,buttonWidth,buttonHeight,1,this);
		buttonBlockPlayers = GuiTools.createToggleButton(GuiKeys.MENU_QUICKMATCH_SETTINGS_BUTTON_BLOCK_PLAYERS,buttonWidth,buttonHeight,1,this);
		removeAll();
	}
	
	private void setButtonsPlayers()
	{	removeAll();
		add(buttonQuit);
		add(Box.createHorizontalGlue());
		add(buttonPlayersPrevious);
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(buttonPublish);
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		add(buttonPlayersNext);
		refreshButtons();
	}
	
	private void setButtonsLevels()
	{	removeAll();
		add(buttonQuit);
		add(Box.createHorizontalGlue());
		add(buttonLevelsPrevious);
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		add(buttonLevelsNext);
		refreshButtons();
	}
	
	private void setButtonsSettings()
	{	removeAll();
		add(buttonQuit);
		add(Box.createHorizontalGlue());
		add(buttonSettingsPrevious);
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		add(buttonSettingsNext);
		refreshButtons();
	}
	
	private void refreshButtons()
	{	LevelsSelection levelsSelection = quickMatchConfiguration.getLevelsSelection();
	
		// levels
		if(quickMatchConfiguration.getLevelsSelection().getLevelCount()==0)
			buttonLevelsNext.setEnabled(false);
		else
			buttonLevelsNext.setEnabled(true);

		// settings
		if(quickMatchConfiguration.getLimitPoints()<=0 && quickMatchConfiguration.getLimitRounds()<=0)
			buttonSettingsNext.setEnabled(false);
		else
			buttonSettingsNext.setEnabled(true);
		
		// players
		if(tournament==null || !levelsSelection.getAllowedPlayerNumbers().contains(playersData.getSelectedProfiles().size()))
				buttonPlayersNext.setEnabled(false);
			else
				buttonPlayersNext.setEnabled(true);
	}
	
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT					/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private SingleTournament tournament;
	private QuickMatchConfiguration quickMatchConfiguration;
	
	public boolean initTournament()
	{	boolean result = false;
		// init tournament
		if(tournament==null || tournament.isOver())
		{	tournament = new SingleTournament();
			Match match = new Match(tournament);
			tournament.setMatch(match);
			// init configuration
			quickMatchConfiguration = Configuration.getGameConfiguration().getQuickMatchConfiguration().copy();
			if(!quickMatchConfiguration.getUseLastPlayers())
				quickMatchConfiguration.reinitPlayers();		
			if(!quickMatchConfiguration.getUseLastLevels())
				quickMatchConfiguration.reinitLevels();
			if(!quickMatchConfiguration.getUseLastSettings())
				quickMatchConfiguration.reinitSettings();
			
			// set panel
			levelsData.setQuickMatchConfiguration(quickMatchConfiguration);
			container.setDataPart(levelsData);
			setButtonsLevels();
			result = true;
		}		
		// existing (unfinished) tournament
		else if(!tournament.hasBegun())
			//replaceWith(matchPanel);
			result = true;
		return result;
	}

	private void setTournamentPlayers()
	{	List<Profile> selectedProfiles = playersData.getSelectedProfiles();
		tournament.setProfiles(selectedProfiles);
	}
	
	private void setTournamentLevels()
	{	LevelsSelection levelsSelection = quickMatchConfiguration.getLevelsSelection();
		Match match = tournament.getCurrentMatch();
		match.clearRounds();
		for(int i=0;i<levelsSelection.getLevelCount();i++)
		{	String folderName = levelsSelection.getFolderName(i);
			String packName = levelsSelection.getPackName(i);
			Round round = new Round(match);
			match.addRound(round);
	    	try
			{	HollowLevel hollowLevel = HollowLevelLoader.loadHollowLevel(packName,folderName);
		    	round.setHollowLevel(hollowLevel);
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
		}
	}
	
	private void setTournamentSettings()
	{	Match match = tournament.getCurrentMatch();
		// random order
		{	boolean randomOrder = quickMatchConfiguration.getLevelsRandomOrder();
			match.setRandomOrder(randomOrder);
		}
		
		// limits
		{	Limits<MatchLimit> limits = new Limits<MatchLimit>();
			PointsProcessor pointsProcessor = new PointsTotal();
			// round limit
			{	int roundsLimit = quickMatchConfiguration.getLimitRounds();
				if(roundsLimit>0)
				{	MatchLimit limit = new LimitConfrontation(roundsLimit,Comparisons.GREATEREQ,pointsProcessor);
					limits.addLimit(limit);
				}
			}
			// points limit
			{	int pointsLimit = quickMatchConfiguration.getLimitPoints();
				if(pointsLimit>0)
				{	MatchLimit limit = new LimitPoints(pointsLimit,Comparisons.GREATEREQ,pointsProcessor,pointsProcessor);
					limits.addLimit(limit);
				}
			}
			match.setLimits(limits);
		}
		
		// round settings
		{	// limits
			Limits<RoundLimit> limits = new Limits<RoundLimit>();
			boolean share = quickMatchConfiguration.getPointsShare();
			List<Integer> pts = quickMatchConfiguration.getPoints();
			float[] values = new float[pts.size()];
			for(int i=0;i<values.length;i++)
				values[i] = pts.get(i);
			PointsProcessor source = new PointsScores(Score.TIME);
			ArrayList<PointsProcessor> sources = new ArrayList<PointsProcessor>();
			sources.add(source);
			PointsProcessor normalPP = new PointsRankpoints(sources,values,false,share);
			PointsProcessor drawPP = new PointsConstant(0);
			QuickMatchDraw draw = quickMatchConfiguration.getPointsDraw();
			long time = quickMatchConfiguration.getLimitTime();
			if(time>0)
			{	// time limit
				RoundLimit limit;
				if(draw==QuickMatchDraw.TIME || draw==QuickMatchDraw.BOTH)
					limit = new LimitTime(time,Comparisons.GREATEREQ,drawPP);
				else
					limit = new LimitTime(time,Comparisons.GREATEREQ,normalPP);
				limits.addLimit(limit);						
			}
			{	// last standing limit
				RoundLimit limit;
				if(draw==QuickMatchDraw.AUTOKILL || draw==QuickMatchDraw.BOTH)
				{	limit = new LimitLastStanding(0,Comparisons.LESSEQ,drawPP);
					limits.addLimit(limit);
				}
				limit = new LimitLastStanding(1,Comparisons.LESSEQ,normalPP);
				limits.addLimit(limit);
			}
			// random location
			boolean randomLocation = quickMatchConfiguration.getPlayersRandomLocation();
			// rounds
			List<Round> rounds = match.getRounds();
			for(Round r: rounds)
			{	r.setLimits(limits);
				r.setRandomLocation(randomLocation);
			}
		}
		
	}
	
	/////////////////////////////////////////////////////////////////
	// PANELS						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private TournamentSplitPanel tournamentPanel;
	private PlayersData playersData;
	private LevelsData levelsData;
	private SettingsData settingsData;
	
	public TournamentSplitPanel getTournamentPanel()
	{	return tournamentPanel;		
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTION LISTENER				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_SETTINGS_BUTTON_QUIT))
		{	tournament = null;
			getFrame().setMainMenuPanel();
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_PLAYERS_BUTTON_PREVIOUS))				
		{	setButtonsSettings();
			container.setDataPart(settingsData);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_LEVELS_BUTTON_PREVIOUS))				
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_SETTINGS_BUTTON_PREVIOUS))				
		{	setButtonsLevels();
			container.setDataPart(levelsData);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_PLAYERS_BUTTON_NEXT))
		{	// set players in tournament
			setTournamentPlayers();
			
			// synch game options
			ProfilesSelection profilesSelection = ProfilesConfiguration.getSelection(tournament.getProfiles());
			quickMatchConfiguration.setProfilesSelection(profilesSelection);
			
			// save quick match options
			try
			{	QuickMatchConfigurationSaver.saveQuickMatchConfiguration(quickMatchConfiguration);
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
			
			// synch game options
			Configuration.getGameConfiguration().setQuickMatchConfiguration(quickMatchConfiguration);
			
			// match panel
			tournamentPanel.setTournament(tournament);
			replaceWith(tournamentPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_LEVELS_BUTTON_NEXT))
		{	// set levels in tournament
			setTournamentLevels();
			
			// set settings panel
			settingsData.setQuickMatchConfiguration(quickMatchConfiguration);
			setButtonsSettings();
			container.setDataPart(settingsData);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_SETTINGS_BUTTON_NEXT))
		{	// implements settings in tournament
			setTournamentSettings();
			
			// set players panel
			playersData.setQuickMatchConfiguration(quickMatchConfiguration);
			setButtonsPlayers();
			container.setDataPart(playersData);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_PUBLISH))
		{	// update buttons
			buttonPlayersPrevious.setEnabled(false);
			int index = GuiTools.indexOfComponent(this,buttonPublish);
			remove(index);
			add(buttonBlockPlayers,index);
			revalidate();
			
			// set up the connection
			//TODO
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_SETTINGS_BUTTON_BLOCK_PLAYERS))
		{	// TODO
	    }
	} 
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void refresh()
	{	refreshButtons();
	}

	/////////////////////////////////////////////////////////////////
	// DATA PANEL LISTENER	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void dataPanelSelectionChanged()
	{	refreshButtons();
	}
}

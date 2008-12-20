package fr.free.totalboumboum.gui.menus.quickmatch.settings;

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
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.game.quickmatch.QuickMatchConfiguration;
import fr.free.totalboumboum.configuration.game.quickmatch.QuickMatchDraw;
import fr.free.totalboumboum.game.limit.ComparatorCode;
import fr.free.totalboumboum.game.limit.LimitConfrontation;
import fr.free.totalboumboum.game.limit.LimitLastStanding;
import fr.free.totalboumboum.game.limit.LimitPoints;
import fr.free.totalboumboum.game.limit.LimitTime;
import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.limit.MatchLimit;
import fr.free.totalboumboum.game.limit.RoundLimit;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.points.PointsConstant;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.points.PointsRankpoints;
import fr.free.totalboumboum.game.points.PointsScores;
import fr.free.totalboumboum.game.points.PointsTotal;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.statistics.Score;
import fr.free.totalboumboum.game.tournament.single.SingleTournament;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.menus.quickmatch.match.MatchSplitPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class SettingsMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	private MatchSplitPanel matchPanel;

	@SuppressWarnings("unused")
	private JButton buttonQuit;
	@SuppressWarnings("unused")
	private JButton buttonPrevious;
	@SuppressWarnings("unused")
	private JButton buttonNext;

	private SettingsData settingsData;

	public SettingsMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.LINE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);

		// sizes
		int buttonWidth = getHeight();
		int buttonHeight = getHeight();

		// buttons
		buttonQuit = GuiTools.createButton(GuiKeys.MENU_QUICKMATCH_SETTINGS_BUTTON_QUIT,buttonWidth,buttonHeight,1,this);
		add(Box.createHorizontalGlue());
		buttonPrevious = GuiTools.createButton(GuiKeys.MENU_QUICKMATCH_SETTINGS_BUTTON_PREVIOUS,buttonWidth,buttonHeight,1,this);
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		buttonNext = GuiTools.createButton(GuiKeys.MENU_QUICKMATCH_SETTINGS_BUTTON_NEXT,buttonWidth,buttonHeight,1,this);

		// panels
		settingsData = new SettingsData(container);
		container.setDataPart(settingsData);
	}
	
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT					/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private SingleTournament tournament;
	
	public void setTournament(SingleTournament tournament)
	{	// init tournament
		this.tournament = tournament;
		// init data
		QuickMatchConfiguration quickMatchConfiguration = Configuration.getGameConfiguration().getQuickMatchConfiguration();
		settingsData.setQuickMatchConfiguration(quickMatchConfiguration);
		// transmit
		if(matchPanel!=null)
		{	matchPanel.setTournament(tournament);
		}			
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTION LISTENER				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_SETTINGS_BUTTON_QUIT))
		{	getFrame().setMainMenuPanel();
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_SETTINGS_BUTTON_PREVIOUS))				
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_SETTINGS_BUTTON_NEXT))
		{	QuickMatchConfiguration quickMatchConfiguration = settingsData.getQuickMatchConfiguration();
			// set settings in match
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
						{	MatchLimit limit = new LimitConfrontation(roundsLimit,ComparatorCode.GREATER,pointsProcessor);
							limits.addLimit(limit);
						}
					}
					// points limit
					{	int pointsLimit = quickMatchConfiguration.getLimitPoints();
						if(pointsLimit>0)
						{	MatchLimit limit = new LimitPoints(pointsLimit,ComparatorCode.GREATEREQ,pointsProcessor,pointsProcessor);
							limits.addLimit(limit);
						}
					}
					match.setLimits(limits);
				}
				// round settings
				{	// limits
					Limits<RoundLimit> limits = new Limits<RoundLimit>();
					boolean share = quickMatchConfiguration.getPointsShare();
					ArrayList<Integer> pts = quickMatchConfiguration.getPoints();
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
							limit = new LimitTime(time,ComparatorCode.GREATEREQ,drawPP);
						else
							limit = new LimitTime(time,ComparatorCode.GREATEREQ,normalPP);
						limits.addLimit(limit);						
					}
					{	// last standing limit
						RoundLimit limit;
						if(draw==QuickMatchDraw.AUTOKILL || draw==QuickMatchDraw.BOTH)
						{	limit = new LimitLastStanding(0,ComparatorCode.LESSEQ,drawPP);
							limits.addLimit(limit);
						}
						limit = new LimitLastStanding(1,ComparatorCode.LESSEQ,normalPP);
						limits.addLimit(limit);
					}
					// rounds
					ArrayList<Round> rounds = match.getRounds();
					for(Round r: rounds)
					{	r.setLimits(limits);
					}
				}
			}
			// set match panel
			if(matchPanel==null)
			{	matchPanel = new MatchSplitPanel(container.getContainer(),container);
			}			
			matchPanel.setTournament(tournament);
			replaceWith(matchPanel);
	    }
	} 
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void refresh()
	{	//
	}
}

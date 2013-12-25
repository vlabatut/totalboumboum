package org.totalboumboum.gui.game.tournament.description;

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

import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.totalboumboum.game.limit.Limit;
import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.game.limit.MatchLimit;
import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.points.AbstractPointsProcessor;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.tournament.single.SingleTournament;
import org.totalboumboum.gui.common.content.subpanel.limits.LimitsSubPanel;
import org.totalboumboum.gui.common.content.subpanel.limits.LimitsSubPanelListener;
import org.totalboumboum.gui.common.content.subpanel.players.PlayersListSubPanel;
import org.totalboumboum.gui.common.content.subpanel.points.PointsSubPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.subpanel.BasicPanel;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;

/**
 * This class handles the display of the
 * description of a single tournament, during a game.
 * 
 * @author Vincent Labatut
 */
public class SingleDescription extends TournamentDescription<SingleTournament> implements LimitsSubPanelListener
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a standard panel.
	 * 
	 * @param container
	 * 		Container of the panel.
	 */
	public SingleDescription(SplitMenuPanel container)
	{	super(container);
	
		// title
		String key = GuiKeys.GAME_MATCH_DESCRIPTION_TITLE;
		setTitleKey(key);
		
		// data
		{	BasicPanel infoPanel = new BasicPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(infoPanel,BoxLayout.LINE_AXIS); 
				infoPanel.setLayout(layout);
			}
			
			int margin = GuiSizeTools.panelMargin;
			int leftWidth = (int)(dataWidth*SPLIT_RATIO); 
			int rightWidth = dataWidth - leftWidth - margin; 
			infoPanel.setOpaque(false);
			
			// players panel
			{	playersPanel = new PlayersListSubPanel(leftWidth,dataHeight);
				infoPanel.add(playersPanel);
			}
			
			infoPanel.add(Box.createHorizontalGlue());
			
			// right panel
			{	JPanel rightPanel = new JPanel();
				{	BoxLayout layout = new BoxLayout(rightPanel,BoxLayout.PAGE_AXIS); 
					rightPanel.setLayout(layout);
				}
				rightPanel.setOpaque(false);
				int upHeight = (dataHeight - margin)/2;
				int downHeight = dataHeight - upHeight - margin;
				
				// limit panel
				{	limitsPanel = new LimitsSubPanel<MatchLimit>(rightWidth,downHeight,GuiKeys.MATCH);
					limitsPanel.addListener(this);
					rightPanel.add(limitsPanel);
				}

				rightPanel.add(Box.createVerticalGlue());
				
				// points panel
				{	pointsPanel = new PointsSubPanel(rightWidth,downHeight,GuiKeys.MATCH);
					rightPanel.add(pointsPanel);
				}
				
				infoPanel.add(rightPanel);
			}

			setDataPart(infoPanel);
		}
	}

	/////////////////////////////////////////////////////////////////
	// PANELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/** Ratio used to split the scren */
	private static final float SPLIT_RATIO = 0.6f;
	/** Pannel displaying the list of players */
	private PlayersListSubPanel playersPanel;
	/** Panel displaying the limits */
	private LimitsSubPanel<MatchLimit> limitsPanel;
	/** Pannel displaying the point */
	private PointsSubPanel pointsPanel;

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/**
	 * Changes the tournament displayed
	 * in this panel.
	 * 
	 * @param tournament
	 * 		New tournament.
	 */
	@Override
	public void setTournament(SingleTournament tournament)
	{	// init
		this.tournament = tournament;
		Match match = tournament.getCurrentMatch();
		Limits<MatchLimit> limits = null;
		if(match!=null)
		{	limits = match.getLimits();
		}
		// players
		List<Profile> players = match.getProfiles();
		playersPanel.setPlayers(players);
		// limits & points
		limitsPanel.setLimits(limits);
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
	public void refresh()
	{	// nothing to do here
	}

	/////////////////////////////////////////////////////////////////
	// LIMITS 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
	public void limitSelectionChanged()
	{	Limit limit = limitsPanel.getSelectedLimit();
		AbstractPointsProcessor pointsProcessor = null;
		if(limit!=null)
			pointsProcessor = limit.getPointProcessor();
		pointsPanel.setPointsProcessor(pointsProcessor);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{	fireMousePressed(e);
	}
}

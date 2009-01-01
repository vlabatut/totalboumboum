package fr.free.totalboumboum.gui.game.tournament.description;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.limit.Limit;
import fr.free.totalboumboum.game.limit.TournamentLimit;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.tournament.sequence.SequenceTournament;
import fr.free.totalboumboum.gui.common.content.subpanel.limits.LimitsSubPanelListener;
import fr.free.totalboumboum.gui.common.content.subpanel.limits.LimitsSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.players.PlayersListSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.points.PointsSubPanel;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class SequenceDescription extends TournamentDescription<SequenceTournament> implements LimitsSubPanelListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.6f;

	private PlayersListSubPanel playersPanel;
	private LimitsSubPanel<TournamentLimit> limitsPanel;
	private PointsSubPanel pointsPanel;

	public SequenceDescription(SplitMenuPanel container)
	{	super(container);
		
		// data
		{	SubPanel infoPanel = new SubPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(infoPanel,BoxLayout.LINE_AXIS); 
				infoPanel.setLayout(layout);
			}

			int margin = GuiTools.panelMargin;
			int leftWidth = (int)(dataWidth*SPLIT_RATIO); 
			int rightWidth = dataWidth - leftWidth - margin; 
			infoPanel.setOpaque(false);
			
			// players panel
			{	playersPanel = new PlayersListSubPanel(leftWidth,dataHeight);
				playersPanel.setShowControls(false);
				infoPanel.add(playersPanel);
			}
			
			infoPanel.add(Box.createHorizontalGlue());
			
			// right panel
			{	JPanel rightPanel = new JPanel();
				{	BoxLayout layout = new BoxLayout(rightPanel,BoxLayout.PAGE_AXIS); 
					rightPanel.setLayout(layout);
				}
				rightPanel.setOpaque(false);
				Dimension dim = new Dimension(rightWidth,dataHeight);
				rightPanel.setPreferredSize(dim);
				rightPanel.setMinimumSize(dim);
				rightPanel.setMaximumSize(dim);
				int upHeight = (dataHeight - margin)/2;
				int downHeight = dataHeight - upHeight - margin;
				
				// limit panel
				{	limitsPanel = new LimitsSubPanel<TournamentLimit>(rightWidth,downHeight,GuiKeys.TOURNAMENT);
					limitsPanel.addListener(this);
					rightPanel.add(limitsPanel);
				}

				rightPanel.add(Box.createVerticalGlue());
				
				// points panel
				{	pointsPanel = new PointsSubPanel(rightWidth,downHeight,GuiKeys.TOURNAMENT);
					rightPanel.add(pointsPanel);
					limitSelectionChange();
				}
				
				infoPanel.add(rightPanel);
			}

			setDataPart(infoPanel);
		}
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public void setTournament(SequenceTournament tournament)
	{	// init
		this.tournament = tournament;
		// players
		ArrayList<Profile> players = tournament.getProfiles();
		playersPanel.setPlayers(players);
		// limits
		limitsPanel.setLimits(tournament.getLimits());		
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
	public void limitSelectionChange()
	{	Limit limit = limitsPanel.getSelectedLimit();
		PointsProcessor pointsProcessor = null;
		if(limit!=null)
			pointsProcessor = limit.getPointProcessor();
		pointsPanel.setPointsProcessor(pointsProcessor);
	}
}

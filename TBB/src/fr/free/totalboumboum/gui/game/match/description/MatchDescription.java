package fr.free.totalboumboum.gui.game.match.description;

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

import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.limit.Limit;
import fr.free.totalboumboum.game.limit.MatchLimit;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.gui.common.content.subpanel.limits.LimitsListener;
import fr.free.totalboumboum.gui.common.content.subpanel.limits.LimitsSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.players.PlayersListSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.points.PointsSubPanel;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class MatchDescription extends EntitledDataPanel implements LimitsListener
{	
	private static final long serialVersionUID = 1L;
	
	private static final float SPLIT_RATIO = 0.6f;
	
	private JPanel rightPanel;

	private PlayersListSubPanel playersPanel;
	private LimitsSubPanel<MatchLimit> limitsPanel;
	private PointsSubPanel pointsPanel;
	
	public MatchDescription(SplitMenuPanel container)
	{	super(container);
	
		// title
		String key = GuiKeys.GAME_MATCH_DESCRIPTION_TITLE;
		setTitleKey(key);
		
		// data
		{	SubPanel infoPanel = new SubPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(infoPanel,BoxLayout.LINE_AXIS); 
				infoPanel.setLayout(layout);
			}
			
			int margin = GuiTools.panelMargin;
			int leftWidth = (int)(dataWidth*SPLIT_RATIO); 
			int rightWidth = dataWidth - leftWidth - margin; 
			Match match = Configuration.getGameConfiguration().getTournament().getCurrentMatch();			
			infoPanel.setOpaque(false);
			
			// players panel
			{	ArrayList<Profile> players = match.getProfiles();
				playersPanel = new PlayersListSubPanel(leftWidth,dataHeight);
				playersPanel.setPlayers(players);
				infoPanel.add(playersPanel);
			}
			
			infoPanel.add(Box.createHorizontalGlue());
			
			// right panel
			{	rightPanel = new JPanel();
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
					limitSelectionChange();
//					SubPanel pointsPanel = RoundDescription.makePointsPanel(rightWidth,downHeight,match.getPointProcessor(),GuiKey.MATCH);
//					rightPanel.add(pointsPanel);
				}
				
				infoPanel.add(rightPanel);
			}

			setDataPart(infoPanel);
			
			limitsPanel.setLimits(match.getLimits());
		}
	}

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

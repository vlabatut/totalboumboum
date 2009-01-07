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

import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;

import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.tournament.cup.CupLeg;
import fr.free.totalboumboum.game.tournament.cup.CupTournament;
import fr.free.totalboumboum.gui.common.content.subpanel.leg.LegSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.leg.LegSubPanelListener;
import fr.free.totalboumboum.gui.common.content.subpanel.players.PlayersListSubPanel;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class CupDescription extends TournamentDescription<CupTournament> implements LegSubPanelListener
{	private static final long serialVersionUID = 1L;

	public CupDescription(SplitMenuPanel container)
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

			// legs panel
			{	legsPanel = new LegSubPanel(leftWidth,dataHeight);
				legsPanel.setLeg(null,LEGS_PER_PAGE);
				infoPanel.add(legsPanel);
			}

			infoPanel.add(Box.createHorizontalGlue());

			// players panel
			{	playersPanel = new PlayersListSubPanel(rightWidth,dataHeight);
				playersPanel.setShowControls(false);
				infoPanel.add(playersPanel);
			}

			setDataPart(infoPanel);
		}
	}
	/////////////////////////////////////////////////////////////////
	// PANELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	private static final float SPLIT_RATIO = 0.5f;
	private static final int LEGS_PER_PAGE = 2;
	private PlayersListSubPanel playersPanel;
	private LegSubPanel legsPanel;

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public void setTournament(CupTournament tournament)
	{	// init
		this.tournament = tournament;
		// players
		ArrayList<Profile> players = tournament.getProfiles();
		playersPanel.setPlayers(players);
		// legs
		ArrayList<CupLeg> legs = tournament.getLegs();
		legsPanel.setLeg(legs.get(0),LEGS_PER_PAGE);		
	}

	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	@Override
	public void refresh()
	{	// nothing to do here
	}

	/////////////////////////////////////////////////////////////////
	// LEG PANEL LISTENER 	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	@Override
	public void legSelectionChanged()
	{	
	}

	@Override
	public void legAfterClicked()
	{	
	}

	@Override
	public void legBeforeClicked()
	{	
	}
}

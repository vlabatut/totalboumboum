package org.totalboumboum.gui.game.tournament.results;

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

import org.totalboumboum.game.tournament.cup.CupLeg;
import org.totalboumboum.game.tournament.cup.CupTournament;
import org.totalboumboum.gui.common.content.subpanel.leg.LegSubPanel;
import org.totalboumboum.gui.common.content.subpanel.leg.LegSubPanelListener;
import org.totalboumboum.gui.common.content.subpanel.results.HomogenResultsSubPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.subpanel.BasicPanel;
import org.totalboumboum.gui.tools.GuiSizeTools;

/**
 * This class handles the display of the
 * result of a cup, during a game.
 * 
 * @author Vincent Labatut
 */
public class CupResults extends TournamentResults<CupTournament> implements LegSubPanelListener
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Builds a standard panel.
	 * 
	 * @param container
	 * 		Container of the panel.
	 */
	public CupResults(SplitMenuPanel container)
	{	super(container);
		
		// data
		{	BasicPanel infoPanel = new BasicPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(infoPanel,BoxLayout.LINE_AXIS); 
				infoPanel.setLayout(layout);
			}
	
			int margin = GuiSizeTools.panelMargin;
			int rightWidth = (int)(dataWidth*SPLIT_RATIO); 
			int leftWidth = dataWidth - rightWidth - margin; 
			infoPanel.setOpaque(false);
	
			// players panel
			{	resultsPanel = new HomogenResultsSubPanel(leftWidth,dataHeight);
				resultsPanel.setShowTime(false);
				resultsPanel.setShowConfrontations(false);
				resultsPanel.setShowPoints(false);
				resultsPanel.setShowTotal(false);
				infoPanel.add(resultsPanel);
			}
	
			infoPanel.add(Box.createHorizontalGlue());
	
			// legs panel
			{	legsPanel = new LegSubPanel(rightWidth,dataHeight);
				legsPanel.setLeg(null,LEGS_PER_PAGE);
				legsPanel.addListener(this);
				infoPanel.add(legsPanel);
			}
	
			setDataPart(infoPanel);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// PANELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/** Split ratio */
	private static final float SPLIT_RATIO = 0.5f;
	/** Number of legs displayed by page */
	private static final int LEGS_PER_PAGE = 2;
	/** Panel displaying the results */
	private HomogenResultsSubPanel resultsPanel;
	/** Panel used to display legs */
	private LegSubPanel legsPanel;

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void setTournament(CupTournament tournament)
	{	this.tournament = tournament;
		resultsPanel.setStatisticHolder(tournament);
		// legs
		List<CupLeg> legs = tournament.getLegs();
		legsPanel.setLeg(legs.get(0),LEGS_PER_PAGE);		
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
	public void refresh()
	{	setTournament(tournament);
		legsPanel.setLeg(tournament.getCurrentLeg(),LEGS_PER_PAGE);
	}

	/////////////////////////////////////////////////////////////////
	// LEG PANEL LISTENER 	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void legSelectionChanged()
	{	//	
	}

	@Override
	public void legAfterClicked()
	{	//
	}

	@Override
	public void legBeforeClicked()
	{	//
	}

	@Override
	public void mousePressed(MouseEvent e)
	{	fireMousePressed(e);
	}
}

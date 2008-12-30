package fr.free.totalboumboum.gui.menus.quickmatch;

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

import javax.swing.Box;
import javax.swing.BoxLayout;

import fr.free.totalboumboum.configuration.game.quickmatch.QuickMatchConfiguration;
import fr.free.totalboumboum.gui.common.content.subpanel.match.MatchQuickConfigSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.match.MatchQuickConfigSubPanelListener;
import fr.free.totalboumboum.gui.common.content.subpanel.round.RoundQuickConfigSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.round.RoundQuickConfigSubPanelListener;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class SettingsData extends EntitledDataPanel implements MatchQuickConfigSubPanelListener, RoundQuickConfigSubPanelListener
{	
	private static final long serialVersionUID = 1L;
	
	private MatchQuickConfigSubPanel matchPanel;
	private RoundQuickConfigSubPanel roundPanel;
	
	public SettingsData(SplitMenuPanel container)
	{	super(container);
		
		// title
		setTitleKey(GuiKeys.MENU_QUICKMATCH_SETTINGS_TITLE);
		
		SubPanel mainPanel;
		// data
		{	mainPanel = new SubPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS); 
				mainPanel.setLayout(layout);
			}
			
			int panelHeight = (dataHeight - GuiTools.panelMargin)/2; 
			mainPanel.setOpaque(false);
			
			// match
			{	matchPanel = new MatchQuickConfigSubPanel(dataWidth,panelHeight);
				mainPanel.add(matchPanel);
				matchPanel.addListener(this);
			}
			
			mainPanel.add(Box.createRigidArea(new Dimension(GuiTools.panelMargin,GuiTools.panelMargin)));
			
			// commands panel
			{	roundPanel = new RoundQuickConfigSubPanel(dataWidth,panelHeight);
				mainPanel.add(roundPanel);
				roundPanel.addListener(this);
			}
			
			setDataPart(mainPanel);
		}
	}
		
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
	{	// nothing to do here
	}
	
	/////////////////////////////////////////////////////////////////
	// GAME CONFIGURATION			/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private QuickMatchConfiguration quickMatchConfiguration = null;

	public void setQuickMatchConfiguration(QuickMatchConfiguration quickMatchConfiguration)
	{	this.quickMatchConfiguration = quickMatchConfiguration;
		matchPanel.setQuickMatchConfiguration(quickMatchConfiguration);
		roundPanel.setQuickMatchConfiguration(quickMatchConfiguration);
	}
	
	public QuickMatchConfiguration getQuickMatchConfiguration()
	{	return quickMatchConfiguration;
	}

	/////////////////////////////////////////////////////////////////
	// MATCH QUICK CONFIGURATION LISTENER	/////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void matchQuickConfigModified()
	{	fireDataPanelSelectionChange();
	}

	/////////////////////////////////////////////////////////////////
	// ROUND QUICK CONFIGURATION LISTENER	/////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void roundQuickConfigModified()
	{	fireDataPanelSelectionChange();
	}
}

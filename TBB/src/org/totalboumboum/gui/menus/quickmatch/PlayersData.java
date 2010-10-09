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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.game.quickmatch.QuickMatchConfiguration;
import org.totalboumboum.configuration.profiles.ProfilesSelection;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.profile.ProfileLoader;
import org.totalboumboum.gui.common.content.subpanel.players.PlayersSelectionSubPanel;
import org.totalboumboum.gui.common.content.subpanel.players.PlayersSelectionSubPanelListener;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.menus.quickmatch.hero.SelectHeroSplitPanel;
import org.totalboumboum.gui.menus.quickmatch.profile.SelectProfileSplitPanel;
import org.totalboumboum.gui.tools.GuiKeys;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class PlayersData extends EntitledDataPanel implements PlayersSelectionSubPanelListener
{	
	private static final long serialVersionUID = 1L;
	
	private PlayersSelectionSubPanel playersPanel;
	
	public PlayersData(SplitMenuPanel container)
	{	super(container);
		
		// title
		String key = GuiKeys.MENU_QUICKMATCH_PLAYERS_TITLE;
		setTitleKey(key);
		
		// data
		playersPanel = new PlayersSelectionSubPanel(dataWidth,dataHeight);
		playersPanel.addListener(this);
		setDataPart(playersPanel);
	}

	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
	{	playersPanel.refresh();
	}
		
	/////////////////////////////////////////////////////////////////
	// CONFIGURATION				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	//private QuickMatchConfiguration quickMatchConfiguration;
	
	public void setQuickMatchConfiguration(QuickMatchConfiguration quickMatchConfiguration)
	{	//this.quickMatchConfiguration = quickMatchConfiguration;
		ProfilesSelection profilesSelection = quickMatchConfiguration.getProfilesSelection();
		List<Profile> selectedProfiles = new ArrayList<Profile>();
		try
		{	selectedProfiles = ProfileLoader.loadProfiles(profilesSelection);
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
		catch (IllegalAccessException e1)
		{	e1.printStackTrace();
		}
		catch (NoSuchFieldException e1)
		{	e1.printStackTrace();
		}
		catch (ClassNotFoundException e1)
		{	e1.printStackTrace();
		}
		Set<Integer> allowedPlayers = quickMatchConfiguration.getLevelsSelection().getAllowedPlayerNumbers();
		
		// remove distant profiles
		Iterator<Profile> it = selectedProfiles.iterator();
		while(it.hasNext())
		{	Profile profile = it.next();
			if(profile.isRemote())
				it.remove();
		}
		
		playersPanel.setPlayers(selectedProfiles,allowedPlayers);
	}
	
	public List<Profile> getSelectedProfiles()
	{	return playersPanel.getPlayers();	
	}

	/////////////////////////////////////////////////////////////////
	// PLAYER SELECTION LISTENER	/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void playerSelectionHeroSet(int index)
	{	Profile profile = playersPanel.getPlayer(index);
		SelectHeroSplitPanel selectHeroPanel = new SelectHeroSplitPanel(container.getMenuContainer(),container,profile);
		getMenuContainer().replaceWith(selectHeroPanel);	
	}

	@Override
	public void playerSelectionPlayerAdded(int index)
	{	SelectProfileSplitPanel selectProfilePanel = new SelectProfileSplitPanel(container.getMenuContainer(),container,index,getSelectedProfiles());
		getMenuContainer().replaceWith(selectProfilePanel);
	}

	@Override
	public void playerSelectionPlayerRemoved(int index)
	{	fireDataPanelSelectionChange(index);
	}

	@Override
	public void playerSelectionProfileSet(int index)
	{	SelectProfileSplitPanel selectProfilePanel = new SelectProfileSplitPanel(container.getMenuContainer(),container,index,getSelectedProfiles());
		getMenuContainer().replaceWith(selectProfilePanel);
	}

	@Override
	public void playerSelectionPlayersAdded()
	{	// TODO Auto-generated method stub
	}

	@Override
	public void playerSelectionColorSet(int index)
	{	// TODO Auto-generated method stub
	}

	@Override
	public void playerSelectionControlsSet(int index)
	{	// TODO Auto-generated method stub
	}
}

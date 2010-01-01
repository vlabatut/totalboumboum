package org.totalboumboum.gui.menus.tournament;

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


import java.awt.BorderLayout;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.game.tournament.TournamentConfiguration;
import org.totalboumboum.game.archive.GameArchive;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.structure.MenuContainer;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.game.tournament.TournamentSplitPanel;
import org.totalboumboum.gui.menus.tournament.load.LoadSplitPanel;
import org.totalboumboum.tools.files.FileTools;
import org.xml.sax.SAXException;


public class TournamenuContainer extends MenuPanel implements MenuContainer
{	private static final long serialVersionUID = 1L;

	public TournamenuContainer(MenuContainer container, MenuPanel parent)
	{	super(container.getMenuWidth(),container.getMenuHeight());
		
		// layout
		BorderLayout layout = new BorderLayout(); 
		setLayout(layout);
	
		setOpaque(false);
	
		// fields
		this.container = container;
		this.parent = parent;
		
		// split panel
		tournamentSplitPanel = new TournamentSplitPanel(this,parent);
		menuSplitPanel = new TournamenuSplitPanel(this,parent);
		menuSplitPanel.setTournamentPanel(tournamentSplitPanel);
		loadSplitPanel = new LoadSplitPanel(this,parent);
		loadSplitPanel.setTournamentPanel(tournamentSplitPanel);
		setMenuPanel(menuSplitPanel);
	}	
	
	public void refresh()
	{	menuPart.refresh();
	}

	
/*
	public void setTournament(AbstractTournament tournament)
	{	this.tournament = tournament;
		tournamentConfiguration.setTournament(tournament);
		tournamentPanel.setTournament(tournament);
//		replaceWith(tournamentPanel);
	}
	
*/
	
	
	
	/////////////////////////////////////////////////////////////////
	// SPLIT PANELS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private TournamenuSplitPanel menuSplitPanel;
	private TournamentSplitPanel tournamentSplitPanel;
	private LoadSplitPanel loadSplitPanel;
	private boolean firstTime = true;
	
	public void initTournament()
	{	TournamentConfiguration tournamentConfiguration = Configuration.getGameConfiguration().getTournamentConfiguration();
		if(tournamentConfiguration.getAutoLoad() && firstTime)
		{	firstTime = false;
			try
			{	String folder = FileTools.FOLDER_AUTOSAVE;
				AbstractTournament tournament = GameArchive.loadGame(folder);
				tournamentSplitPanel.setTournament(tournament);
				Configuration.getGameConfiguration().getTournamentConfiguration().setTournament(tournament);
				setMenuPanel(tournamentSplitPanel);
			}
			catch (IOException e1)
			{	e1.printStackTrace();
			}
			catch (ClassNotFoundException e1)
			{	e1.printStackTrace();
			}
			catch (ParserConfigurationException e1)
			{	e1.printStackTrace();
			}
			catch (SAXException e1)
			{	e1.printStackTrace();
			}
		}
		else
		{	AbstractTournament tournament = tournamentConfiguration.getTournament();
			if(tournament==null || tournament.isOver())
			{	menuSplitPanel.initTournament();
				setMenuPanel(menuSplitPanel);		
			}
			else if(tournament.hasBegun())
				setMenuPanel(tournamentSplitPanel);
			else
				setMenuPanel(menuSplitPanel);
		}
	}
	
	public void initLoad()
	{	loadSplitPanel.refresh();
		setMenuPanel(loadSplitPanel);
	}
	
	/////////////////////////////////////////////////////////////////
	// MENU CONTAINER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected MenuPanel menuPart;

	@Override
	public int getMenuHeight()
	{	return height;
	}

	@Override
	public int getMenuWidth()
	{	return width;
	}

	@Override
	public void setMenuPanel(MenuPanel newPanel)
	{	if(this.menuPart!=null)
		remove(this.menuPart);
		this.menuPart = newPanel;
	//	this.menuPart.refresh();
		add(menuPart);
		validate();
		repaint();		
	}

}

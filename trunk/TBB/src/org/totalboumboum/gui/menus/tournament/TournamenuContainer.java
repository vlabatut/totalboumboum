package org.totalboumboum.gui.menus.tournament;

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

import java.awt.BorderLayout;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.game.tournament.TournamentConfiguration;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.structure.MenuContainer;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.game.tournament.TournamentSplitPanel;
import org.totalboumboum.gui.menus.tournament.load.LoadSplitPanel;
import org.totalboumboum.stream.file.archive.GameArchive;
import org.totalboumboum.tools.files.FileNames;
import org.xml.sax.SAXException;

/**
 * Class containing both tournament settings and actual tournament panels.
 * 
 * @author Vincent Labatut
 */
public class TournamenuContainer extends MenuPanel implements MenuContainer
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new container for tournament
	 * setting/playing.
	 * 
	 * @param container
	 * 		Container of this panel.
	 * @param parent
	 * 		Parent menu of this menu.
	 */
	public TournamenuContainer(MenuContainer container, MenuPanel parent)
	{	super(container.getMenuWidth(),container.getMenuHeight());
		
		// layout
		BorderLayout layout = new BorderLayout(); 
		setLayout(layout);
	
		setOpaque(false);
	
		// fields
		this.container = container;
		this.parent = parent;
		
		// split panels
		tournamentSplitPanel = new TournamentSplitPanel(this,parent);
		menuSplitPanel = new TournamenuSplitPanel(this,parent);
		menuSplitPanel.setTournamentPanel(tournamentSplitPanel);
		loadSplitPanel = new LoadSplitPanel(this,parent);
		loadSplitPanel.setTournamentPanel(tournamentSplitPanel);
		setMenuPanel(menuSplitPanel);
	}	
	
	/////////////////////////////////////////////////////////////////
	// REFRESH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
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
	/** Panel used to setup the tournament */
	private TournamenuSplitPanel menuSplitPanel;
	/** Panel used to display the tournament */
	private TournamentSplitPanel tournamentSplitPanel;
	/** Panel used to load some previously started tournament */
	private LoadSplitPanel loadSplitPanel;
	/** First time a tournament is launched in this session (might load previous settings) */
	private boolean firstTime = true;
	
	/**
	 * Initializes the panel: decides whether some settings
	 * (new tournament) or actual tournament (already started tournament)
	 * should be displayed in this container. 
	 */
	public void initTournament()
	{	TournamentConfiguration tournamentConfiguration = Configuration.getGameConfiguration().getTournamentConfiguration();
		if(tournamentConfiguration.getAutoLoad() && firstTime)
		{	firstTime = false;
			try
			{	String folder = FileNames.FILE_AUTOSAVE;
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
	
	/**
	 * Initializes the loading panel,
	 * allowing to continue a previously
	 * started tournament.
	 */
	public void initLoad()
	{	loadSplitPanel.refresh();
		setMenuPanel(loadSplitPanel);
	}
	
	/////////////////////////////////////////////////////////////////
	// MENU CONTAINER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Menu of this container */
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

	/////////////////////////////////////////////////////////////////
	// AUTO ADVANCE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Automatically clicks on the appropriate
	 * buttons in order to progress in the tournament.
	 * Used to automatically chain many tournaments,
	 * while evaluating agents.
	 */
	public void autoAdvance()
	{	// we only transmit the order to the tournament menu
		if(this.menuPart==menuSplitPanel)
			((TournamenuSplitPanel)menuPart).autoAdvance();
	}
}

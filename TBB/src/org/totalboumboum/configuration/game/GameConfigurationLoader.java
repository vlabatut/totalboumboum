package org.totalboumboum.configuration.game;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.game.quickmatch.QuickMatchConfigurationLoader;
import org.totalboumboum.configuration.game.quickstart.QuickStartConfigurationLoader;
import org.totalboumboum.configuration.game.tournament.TournamentConfigurationLoader;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class GameConfigurationLoader
{	
	public static GameConfiguration loadGameConfiguration() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	GameConfiguration result = new GameConfiguration();
		result.setTournamentConfiguration(TournamentConfigurationLoader.loadTournamentConfiguration());
		result.setQuickMatchConfiguration(QuickMatchConfigurationLoader.loadQuickMatchConfiguration());
		result.setQuickStartConfiguration(QuickStartConfigurationLoader.loadQuickStartConfiguration());
		return result;
	}
}

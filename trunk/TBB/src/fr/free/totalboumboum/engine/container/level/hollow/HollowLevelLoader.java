package fr.free.totalboumboum.engine.container.level.hollow;

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

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.level.info.LevelInfo;
import fr.free.totalboumboum.engine.container.level.info.LevelInfoLoader;
import fr.free.totalboumboum.engine.container.level.instance.Instance;
import fr.free.totalboumboum.engine.container.level.players.Players;
import fr.free.totalboumboum.engine.container.level.players.PlayersLoader;
import fr.free.totalboumboum.engine.container.level.zone.Zone;
import fr.free.totalboumboum.engine.container.level.zone.ZoneLoader;

public class HollowLevelLoader
{	
	public static HollowLevel loadHollowLevel(String pack, String folder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		HollowLevel result = new HollowLevel();
		
		// level info
		LevelInfo levelInfo = LevelInfoLoader.loadLevelInfo(pack,folder);
		result.setLevelInfo(levelInfo);
		// instance
		Instance instance = new Instance(levelInfo.getInstance());
		result.setInstance(instance);
		// players
		Players players = PlayersLoader.loadPlayers(folder);
		result.setPlayers(players);
		// zone
		Zone zone = ZoneLoader.loadZone(folder,levelInfo.getGlobalHeight(),levelInfo.getGlobalWidth());
		result.setZone(zone);
		
		return result;
    }
}

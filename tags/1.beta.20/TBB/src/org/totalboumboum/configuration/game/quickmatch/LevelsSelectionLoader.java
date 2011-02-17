package org.totalboumboum.configuration.game.quickmatch;

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

import java.util.List;
import java.util.TreeSet;

import org.jdom.Element;
import org.totalboumboum.tools.xml.XmlNames;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class LevelsSelectionLoader
{	
	@SuppressWarnings("unchecked")
	public static LevelsSelection loadLevelsSelection(Element root)
	{	LevelsSelection result = new LevelsSelection();
		List<Element> playersElt = root.getChildren(XmlNames.LEVEL);
		for(Element elt: playersElt)
			loadLevelElement(elt,result);
		return result;
	}

	private static void loadLevelElement(Element root, LevelsSelection result)
	{	// pack
		String packName = root.getAttributeValue(XmlNames.PACK);
		
		// folder
		String folderName = root.getAttributeValue(XmlNames.FOLDER);
		
		// allowed players
		TreeSet<Integer> allowedPlayers = new TreeSet<Integer>();
		String apStr = root.getAttributeValue(XmlNames.ALLOWED_PLAYERS);
		String[] split = apStr.split(" ");
		for(String s: split)
		{	int value = Integer.parseInt(s);
			allowedPlayers.add(value);
		}
		
		// result
		result.addLevel(packName,folderName,allowedPlayers);
	}
}

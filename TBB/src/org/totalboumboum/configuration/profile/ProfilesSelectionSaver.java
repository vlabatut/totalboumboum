package org.totalboumboum.configuration.profile;

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

import org.jdom.Element;
import org.totalboumboum.tools.XmlTools;


public class ProfilesSelectionSaver
{	
	public static void saveProfilesSelection(Element result, ProfilesSelection profilesSelection)
	{	int size = profilesSelection.getProfileCount();
		for(int i=0;i<size;i++)
		{	Element playerElt = savePlayerElement(i,profilesSelection);
			result.addContent(playerElt);
		}
	}
	
	private static Element savePlayerElement(int index, ProfilesSelection profilesSelection)
	{	Element result = new Element(XmlTools.PLAYER);
		
		// file name
		int id = profilesSelection.getIds(index);
		String idStr = Integer.toString(id);
		result.setAttribute(XmlTools.FILE,idStr);
		
		// color
		String color = profilesSelection.getColor(index).toString();
		result.setAttribute(XmlTools.COLOR,color);
		
		// controls
		String controlsIndex = Integer.toString(profilesSelection.getControlsIndex(index));
		result.setAttribute(XmlTools.CONTROLS,controlsIndex);
		
		// sprite
		String hero[] = profilesSelection.getHero(index);
		result.setAttribute(XmlTools.SPRITE_PACK,hero[0]);
		result.setAttribute(XmlTools.SPRITE_FOLDER,hero[1]);
		
		return result;
	}
}

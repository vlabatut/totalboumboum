package org.totalboumboum.configuration.profiles;

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

import org.jdom.Element;
import org.totalboumboum.tools.xml.XmlNames;

/**
 * 
 * @author Vincent Labatut
 *
 */
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
	{	Element result = new Element(XmlNames.PLAYER);
		
		// file name
		String id = profilesSelection.getId(index);
		//String idStr = Integer.toString(id);
		result.setAttribute(XmlNames.FILE,id);
		
		// color
		String color = profilesSelection.getColor(index).toString();
		result.setAttribute(XmlNames.COLOR,color);
		
		// controls
		String controlsIndex = Integer.toString(profilesSelection.getControlsIndex(index));
		result.setAttribute(XmlNames.CONTROLS,controlsIndex);
		
		// sprite
		String hero[] = profilesSelection.getHero(index);
		result.setAttribute(XmlNames.SPRITE_PACK,hero[0]);
		result.setAttribute(XmlNames.SPRITE_FOLDER,hero[1]);
		
		return result;
	}
}

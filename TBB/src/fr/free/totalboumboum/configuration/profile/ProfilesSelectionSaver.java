package fr.free.totalboumboum.configuration.profile;

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

import java.util.Locale;

import org.jdom.Element;

import fr.free.totalboumboum.tools.XmlTools;

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
	{	Element result = new Element(XmlTools.ELT_PLAYER);
		
		// file name
		String fileName = profilesSelection.getFileName(index);
		result.setAttribute(XmlTools.ATT_FILE,fileName);
		
		// color
		String color = profilesSelection.getColor(index).toString().toLowerCase(Locale.ENGLISH);
		result.setAttribute(XmlTools.ATT_COLOR,color);
		
		// controls
		String controlsIndex = Integer.toString(profilesSelection.getControlsIndex(index));
		result.setAttribute(XmlTools.ATT_CONTROLS,controlsIndex);
		
		// sprite
		String hero[] = profilesSelection.getHero(index);
		result.setAttribute(XmlTools.ATT_SPRITE_PACK,hero[1]);
		result.setAttribute(XmlTools.ATT_SPRITE_FOLDER,hero[1]);
		
		return result;
	}
}

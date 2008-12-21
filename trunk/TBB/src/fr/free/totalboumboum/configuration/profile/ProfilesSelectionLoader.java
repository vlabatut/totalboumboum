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

import java.util.List;
import java.util.Locale;

import org.jdom.Element;

import fr.free.totalboumboum.tools.XmlTools;

public class ProfilesSelectionLoader
{	
	@SuppressWarnings("unchecked")
	public static ProfilesSelection loadProfilesSelection(Element root)
	{	ProfilesSelection result = new ProfilesSelection();
		List<Element> playersElt = root.getChildren(XmlTools.ELT_PLAYER);
		for(Element elt: playersElt)
			loadPlayerElement(elt,result);
		return result;
	}

	private static void loadPlayerElement(Element root, ProfilesSelection result)
	{	// file
		String fileName = root.getAttributeValue(XmlTools.ATT_FILE);
		
		// color
		String colorStr = root.getAttributeValue(XmlTools.ATT_COLOR);
		PredefinedColor color = PredefinedColor.valueOf(colorStr.toUpperCase(Locale.ENGLISH));
		
		// controls
		String controlsStr = root.getAttributeValue(XmlTools.ATT_CONTROLS);
		int controlsIndex = Integer.parseInt(controlsStr);

		// sprite
		String spriteFolder = root.getAttributeValue(XmlTools.ATT_SPRITE_FOLDER);
		String spritePack = root.getAttributeValue(XmlTools.ATT_SPRITE_PACK);
		String[] hero = {spritePack,spriteFolder};
		
		result.addProfile(fileName,color,controlsIndex,hero);
	}
}

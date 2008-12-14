package fr.free.totalboumboum.configuration.game;

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

import org.jdom.Element;

import fr.free.totalboumboum.tools.XmlTools;

public class LevelsSelectionSaver
{	
	public static void saveLevelsSelection(Element result, LevelsSelection levelsSelection)
	{	int size = levelsSelection.getLevelCount();
		for(int i=0;i<size;i++)
		{	Element levelElt = saveLevelElement(i,levelsSelection);
			result.addContent(levelElt);
		}
	}
	
	private static Element saveLevelElement(int index, LevelsSelection levelsSelection)
	{	Element result = new Element(XmlTools.ELT_LEVEL);
		// folder pack
		String packName = levelsSelection.getPackName(index);
		result.setAttribute(XmlTools.ATT_PACK,packName);
		// folder name
		String folderName = levelsSelection.getFolderName(index);
		result.setAttribute(XmlTools.ATT_FOLDER,folderName);
		//
		return result;
	}
}

package org.totalboumboum.engine.content.sprite.item;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.feature.ability.AbilityLoader;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.gesture.HollowGesturePack;
import org.totalboumboum.engine.content.feature.gesture.anime.HollowAnimesLoader;
import org.totalboumboum.engine.content.feature.gesture.modulation.ModulationsLoader;
import org.totalboumboum.engine.content.feature.gesture.trajectory.HollowTrajectoriesLoader;
import org.totalboumboum.engine.content.sprite.HollowSpriteFactoryLoader;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.xml.XmlNames;
import org.xml.sax.SAXException;

public class HollowItemFactoryLoader extends HollowSpriteFactoryLoader
{	
	public static HollowItemFactory loadItemFactory(String folderPath, String itemName, List<String> itemrefs, List<List<AbstractAbility>> itemAbilities, List<Float> probabilities, HashMap<String,HollowItemFactory> abstractItems, boolean isAbstract) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		HollowItemFactory result = new HollowItemFactory();
		Element root = HollowSpriteFactoryLoader.openFile(folderPath);
		Element elt = root.getChild(XmlNames.GENERAL);
		String folder;
		
		// GENERAL
		String baseStr = root.getAttributeValue(XmlNames.BASE);
		HollowGesturePack gesturePack;
		List<AbstractAbility> abilities;
		if(baseStr==null)
		{	// create object
			result = new HollowItemFactory();
			// gestures pack
			gesturePack = new HollowGesturePack();
			result.setGesturePack(gesturePack);
			// abilities
			abilities = new ArrayList<AbstractAbility>();
			result.setAbilities(abilities);
		}
		else
		{	// base
			HollowItemFactory base = abstractItems.get(baseStr);
			result = base.copy();
			result.setBase(baseStr);
			// gestures pack
			gesturePack = result.getGesturePack();
			// abilities
			abilities = result.getAbilities();
		}		
		// name
		String name = elt.getAttribute(XmlNames.NAME).getValue().trim();
		result.setName(name);
//if(name==null)
//	System.out.println();
		result.itemName = itemName;		
		
		// ABILITIES
		folder = folderPath+File.separator+FileNames.FOLDER_ABILITIES;
		AbilityLoader.loadAbilityPack(folder,abilities);
		
		// ANIMES
		folder = folderPath+File.separator+FileNames.FOLDER_ANIMES;
		HollowAnimesLoader.loadAnimes(folder,gesturePack);
		
		//EXPLOSION
		String explosionName = loadExplosionElement(root);
		if(explosionName!=null)
			result.setExplosionName(explosionName);
		
		//MODULATIONS
		folder = folderPath+File.separator+FileNames.FOLDER_MODULATIONS;
		ModulationsLoader.loadModulations(folder,gesturePack,Role.ITEM);
		
		// TRAJECTORIES
		folder = folderPath+File.separator+FileNames.FOLDER_TRAJECTORIES;
		HollowTrajectoriesLoader.loadTrajectories(folder,gesturePack);
		
		// ITEM ABILITIES / ITEMREFS
		result.setItemAbilities(itemAbilities,itemrefs,probabilities);
		
		// result
		if(!isAbstract)
			initDefaultGestures(gesturePack,Role.ITEM);
		return result;
	}	
}

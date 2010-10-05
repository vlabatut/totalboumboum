package org.totalboumboum.engine.content.sprite.floor;

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

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HollowFloorFactoryLoader extends HollowSpriteFactoryLoader
{	
	public static HollowFloorFactory loadFloorFactory(String folderPath, HashMap<String,HollowFloorFactory> abstractFloors, boolean isAbstract) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		HollowFloorFactory result = new HollowFloorFactory();
		Element root = HollowSpriteFactoryLoader.openFile(folderPath);
		Element elt = root.getChild(XmlNames.GENERAL);
		String folder;
		
		// GENERAL
		String baseStr = elt.getAttributeValue(XmlNames.BASE);
		HollowGesturePack gesturePack;
		List<AbstractAbility> abilities;
		if(baseStr==null)
		{	// create object
			result = new HollowFloorFactory();
			// gestures pack
			gesturePack = new HollowGesturePack();
			result.setGesturePack(gesturePack);
			// abilities
			abilities = new ArrayList<AbstractAbility>();
			result.setAbilities(abilities);
		}
		else
		{	// base
			HollowFloorFactory base = abstractFloors.get(baseStr);
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
		
		// ABILITIES
		folder = folderPath+File.separator+FileNames.FILE_ABILITIES;
		AbilityLoader.loadAbilityPack(folder,abilities);
		
		// ANIMES
		folder = folderPath+File.separator+FileNames.FILE_ANIMES;
		HollowAnimesLoader.loadAnimes(folder,gesturePack);
		
		//EXPLOSION
		String explosionName = loadExplosionElement(root);
		if(explosionName!=null)
			result.setExplosionName(explosionName);
		
		//MODULATIONS
		folder = folderPath+File.separator+FileNames.FILE_MODULATIONS;
		ModulationsLoader.loadModulations(folder,gesturePack,Role.FLOOR);
		
		// TRAJECTORIES
		folder = folderPath+File.separator+FileNames.FILE_TRAJECTORIES;
		HollowTrajectoriesLoader.loadTrajectories(folder,gesturePack);
		
		// result
		if(!isAbstract)
			initDefaultGestures(gesturePack,Role.FLOOR);
		return result;
	}	
}

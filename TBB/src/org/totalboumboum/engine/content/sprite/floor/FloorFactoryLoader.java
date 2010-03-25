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

import javax.xml.parsers.ParserConfigurationException;


import org.jdom.Element;
import org.totalboumboum.configuration.profile.PredefinedColor;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.feature.ability.AbilityLoader;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.gesture.GesturePack;
import org.totalboumboum.engine.content.feature.gesture.anime.HollowAnimesLoader;
import org.totalboumboum.engine.content.feature.gesture.modulation.ModulationsLoader;
import org.totalboumboum.engine.content.feature.gesture.trajectory.HollowTrajectoriesLoader;
import org.totalboumboum.engine.content.sprite.SpriteFactoryLoader;
import org.totalboumboum.tools.files.FileNames;
import org.xml.sax.SAXException;

public class FloorFactoryLoader extends SpriteFactoryLoader
{	
	public static FloorFactory loadFloorFactory(String folderPath, HashMap<String,FloorFactory> abstractFloors, boolean isAbstract) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		FloorFactory result = new FloorFactory();
		Element root = SpriteFactoryLoader.openFile(folderPath);
		String folder;
		
		// GENERAL
		loadGeneralElement(root,result,abstractFloors);	
		GesturePack gesturePack = result.getGesturePack();
		
		// ABILITIES
		ArrayList<AbstractAbility> abilities = result.getAbilities();
		folder = folderPath+File.separator+FileNames.FOLDER_ABILITIES;
		AbilityLoader.loadAbilityPack(folder,abilities);
		
		// ANIMES
		folder = folderPath+File.separator+FileNames.FOLDER_ANIMES;
		HollowAnimesLoader.loadAnimes(folder,gesturePack,FloorFactory.getAnimeReplacements());
		
		//EXPLOSION
		String explosionName = loadExplosionElement(root);
		if(explosionName!=null)
			result.setExplosionName(explosionName);
		
		//MODULATIONS
		folder = folderPath+File.separator+FileNames.FOLDER_MODULATIONS;
		ModulationsLoader.loadModulations(folder,gesturePack,Role.FLOOR);
		
		// TRAJECTORIES
		folder = folderPath+File.separator+FileNames.FILE_TRAJECTORIES;
		HollowTrajectoriesLoader.loadTrajectories(folder,gesturePack);
		
		// BOMBSET
		PredefinedColor bombsetColor = null;
		result.setBombsetColor(bombsetColor);

		// result
		if(!isAbstract)
			initDefaultGestures(gesturePack,Role.FLOOR);
		return result;
	}	
}

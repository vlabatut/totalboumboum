package org.totalboumboum.engine.content.sprite.bomb;

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
import org.totalboumboum.engine.container.bombset.Bombset;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.feature.ability.AbilityLoader;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.gesture.GesturePack;
import org.totalboumboum.engine.content.feature.gesture.anime.HollowAnimesLoader;
import org.totalboumboum.engine.content.feature.gesture.modulation.ModulationsLoader;
import org.totalboumboum.engine.content.feature.gesture.trajectory.HollowTrajectoriesLoader;
import org.totalboumboum.engine.content.sprite.HollowSpriteFactoryLoader;
import org.totalboumboum.tools.files.FileNames;
import org.xml.sax.SAXException;

public class BombFactoryLoader extends HollowSpriteFactoryLoader
{	
	/*
	 * load everything except the animes (cf. completeBombFactory)
	 * because the bombset is always loaded in a neutral way, without graphics.
	 * it is then completed depending on the needed colors (but the rest of the features stay the same) 
	 */
	public static BombFactory loadBombFactory(String folderPath, String bombName, HashMap<String,BombFactory> abstractBombs) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		BombFactory result = new BombFactory(bombName);
		Element root = HollowSpriteFactoryLoader.openFile(folderPath);
		String folder;
		
		// GENERAL
		loadGeneralElement(root,result,abstractBombs);
		GesturePack gesturePack = result.getGesturePack();
		ArrayList<AbstractAbility> abilities = result.getAbilities();
		
		// ABILITIES
		folder = folderPath+File.separator+FileNames.FOLDER_ABILITIES;
		AbilityLoader.loadAbilityPack(folder,abilities);
		
		//EXPLOSION
		String explosionName = loadExplosionElement(root);
		if(explosionName!=null)
			result.setExplosionName(explosionName);
		
		//MODULATIONS
		folder = folderPath+File.separator+FileNames.FOLDER_MODULATIONS;
		ModulationsLoader.loadModulations(folder,gesturePack,Role.BOMB);
		
		// TRAJECTORIES
		folder = folderPath+File.separator+FileNames.FOLDER_TRAJECTORIES;
		HollowTrajectoriesLoader.loadTrajectories(folder,gesturePack);
		
		// result
		return result;
	}	

	/*
	 * load the animes only
	 * (complete the sprite depending on the specified color)
	 */
	public static void completeBombFactory(BombFactory result, String folderPath, PredefinedColor color, Bombset bombset, HashMap<String,String> abstractBombs) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String folder;
		
		// GENERAL
		GesturePack gesturePack = result.getGesturePack();
		String baseStr = result.getBase();
		if(baseStr!=null)
		{	String path = abstractBombs.get(baseStr)+File.separator+FileNames.FOLDER_ANIMES;
			// NOTE this won't work in case of multiple inheritance (only the direct parent's animation will be loaded)
			if(color==null)
				HollowAnimesLoader.loadAnimes(path,gesturePack,BombFactory.getAnimeReplacements());
			else
				HollowAnimesLoader.loadAnimes(path,gesturePack,color,BombFactory.getAnimeReplacements());
		}
		
		// ANIMES
		folder = folderPath+File.separator+FileNames.FOLDER_ANIMES;
		if(color==null)
			HollowAnimesLoader.loadAnimes(folder,gesturePack,BombFactory.getAnimeReplacements());
		else
			HollowAnimesLoader.loadAnimes(folder,gesturePack,color,BombFactory.getAnimeReplacements());
		
		// BOMBSET
		result.setBombsetColor(color);
		
		initDefaultGestures(gesturePack,Role.BOMB);
	}	
}

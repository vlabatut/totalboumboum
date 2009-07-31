package fr.free.totalboumboum.engine.content.sprite.bomb;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;


import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.engine.container.bombset.Bombset;
import fr.free.totalboumboum.engine.content.feature.Role;
import fr.free.totalboumboum.engine.content.feature.ability.AbilityLoader;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.explosion.Explosion;
import fr.free.totalboumboum.engine.content.feature.gesture.GesturePack;
import fr.free.totalboumboum.engine.content.feature.gesture.anime.AnimesLoader;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.ModulationsLoader;
import fr.free.totalboumboum.engine.content.feature.gesture.trajectory.TrajectoriesLoader;
import fr.free.totalboumboum.engine.content.sprite.SpriteFactoryLoader;
import fr.free.totalboumboum.tools.FileTools;

public class BombFactoryLoader extends SpriteFactoryLoader
{	
	/*
	 * load everything except the animes (cf. completeBombFactory)
	 * because the bombset is always loaded in a neutral way, without graphics.
	 * it is then completed depending on the needed colors (but the rest of the features stay the same) 
	 */
	public static BombFactory loadBombFactory(String folderPath, String bombName/*, PredefinedColor color, Bombset bombset*/, HashMap<String,BombFactory> abstractBombs) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		BombFactory result = new BombFactory(bombName);
		Element root = SpriteFactoryLoader.openFile(folderPath);
		String folder;
		
		// GENERAL
		loadGeneralElement(root,result,abstractBombs);
		GesturePack gesturePack = result.getGesturePack();
		ArrayList<AbstractAbility> abilities = result.getAbilities();
		
		// ABILITIES
		folder = folderPath+File.separator+FileTools.FOLDER_ABILITIES;
		AbilityLoader.loadAbilityPack(folder,abilities);
		
		//EXPLOSION
		Explosion exp = loadExplosionElement(root);
		if(exp!=null)
			result.setExplosion(exp); 
		
		//MODULATIONS
		folder = folderPath+File.separator+FileTools.FOLDER_MODULATIONS;
		ModulationsLoader.loadModulations(folder,gesturePack,Role.BOMB);
		initDefaultGesture(gesturePack,Role.BOMB);
		
		// TRAJECTORIES
		folder = folderPath+File.separator+FileTools.FOLDER_TRAJECTORIES;
		TrajectoriesLoader.loadTrajectories(folder,gesturePack);
		
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
		{	String path = abstractBombs.get(baseStr)+File.separator+FileTools.FOLDER_ANIMES;
			// NOTE this won't work in case of multiple inheritance (only the direct parent's animation will be loaded)
			if(color==null)
				AnimesLoader.loadAnimes(path,gesturePack,BombFactory.getAnimeReplacements());
			else
				AnimesLoader.loadAnimes(path,gesturePack,color,BombFactory.getAnimeReplacements());
		}
		
		// ANIMES
		folder = folderPath+File.separator+FileTools.FOLDER_ANIMES;
		if(color==null)
			AnimesLoader.loadAnimes(folder,gesturePack,BombFactory.getAnimeReplacements());
		else
			AnimesLoader.loadAnimes(folder,gesturePack,color,BombFactory.getAnimeReplacements());
		
		// BOMBSET
		result.setBombset(bombset);
//if(result.getName()==null)
//	System.out.println();
	}	
}

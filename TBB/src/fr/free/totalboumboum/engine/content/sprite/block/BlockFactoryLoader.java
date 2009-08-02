package fr.free.totalboumboum.engine.content.sprite.block;

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

import fr.free.totalboumboum.configuration.GameVariables;
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

public class BlockFactoryLoader extends SpriteFactoryLoader
{	
	public static BlockFactory loadBlockFactory(String folderPath, HashMap<String,BlockFactory> abstractBlocks) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		BlockFactory result = new BlockFactory();
		Element root = SpriteFactoryLoader.openFile(folderPath);
		String folder;
		
		// GENERAL
		loadGeneralElement(root,result,abstractBlocks);	
		GesturePack gesturePack = result.getGesturePack();
		ArrayList<AbstractAbility> abilities = result.getAbilities();
		
		// ABILITIES
		folder = folderPath+File.separator+FileTools.FOLDER_ABILITIES;
		AbilityLoader.loadAbilityPack(folder,abilities);
		
		// ANIMES
		folder = folderPath+File.separator+FileTools.FOLDER_ANIMES;
		AnimesLoader.loadAnimes(folder,gesturePack,BlockFactory.getAnimeReplacements());
		
		//EXPLOSION
		Explosion exp = loadExplosionElement(root);
		if(exp!=null)
			result.setExplosion(exp); 
		
		//MODULATIONS
		folder = folderPath+File.separator+FileTools.FOLDER_MODULATIONS;
		ModulationsLoader.loadModulations(folder,gesturePack,Role.BLOCK);
		
		// TRAJECTORIES
		folder = folderPath+File.separator+FileTools.FOLDER_TRAJECTORIES;
		TrajectoriesLoader.loadTrajectories(folder,gesturePack);
		
		// BOMBSET
		Bombset bombset = GameVariables.level.getBombset();
		result.setBombset(bombset);

		// result
		initDefaultGestures(gesturePack,Role.BLOCK);
		return result;
	}	
}

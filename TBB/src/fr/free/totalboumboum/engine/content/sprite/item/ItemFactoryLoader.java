package fr.free.totalboumboum.engine.content.sprite.item;

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

import javax.xml.parsers.ParserConfigurationException;


import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.bombset.Bombset;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.ability.AbilityLoader;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.explosion.Explosion;
import fr.free.totalboumboum.engine.content.feature.gesture.GesturePack;
import fr.free.totalboumboum.engine.content.feature.gesture.anime.AnimesLoader;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.ModulationsLoader;
import fr.free.totalboumboum.engine.content.feature.gesture.trajectory.TrajectoriesLoader;
import fr.free.totalboumboum.engine.content.sprite.SpriteFactoryLoader;
import fr.free.totalboumboum.tools.FileTools;

public class ItemFactoryLoader extends SpriteFactoryLoader
{	
	public static ItemFactory loadItemFactory(String folderPath, Level level, String itemName, ArrayList<AbstractAbility> itemAbilities) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		ItemFactory result = new ItemFactory(level,itemName);
		Element root = SpriteFactoryLoader.openFile(folderPath);
		String folder;
		GesturePack gesturePack = new GesturePack();
		result.setGesturePack(gesturePack);
		
		// GENERAL
		loadGeneralElement(root,result);
		
		// ABILITIES
		folder = folderPath+File.separator+FileTools.FILE_ABILITIES;
		ArrayList<AbstractAbility> abilities = new ArrayList<AbstractAbility>();
		AbilityLoader.loadAbilityPack(folder,level,abilities);
		result.setAbilities(abilities);
		
		// ANIMES
		folder = folderPath+File.separator+FileTools.FILE_ANIMES;
		AnimesLoader.loadAnimes(folder,gesturePack,level,ItemFactory.getAnimeReplacements());
		
		//EXPLOSION
		Explosion explosion = loadExplosionElement(root,level);
		result.setExplosion(explosion);
		
		//MODULATIONS
		folder = folderPath+File.separator+FileTools.FILE_MODULATIONS;
		ModulationsLoader.loadModulations(folder,gesturePack,level);
		
		// TRAJECTORIES
		folder = folderPath+File.separator+FileTools.FILE_TRAJECTORIES;
		TrajectoriesLoader.loadTrajectories(folder,gesturePack,level);
		
		// BOMBSET
		Bombset bombset = level.getBombset();
		result.setBombset(bombset);

		// ITEM ABILITIES
		result.setItemAbilities(itemAbilities);
		
		// result
		return result;
	}	
}

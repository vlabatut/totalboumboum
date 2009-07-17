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
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;


import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.GameVariables;
import fr.free.totalboumboum.engine.container.bombset.Bombset;
import fr.free.totalboumboum.engine.content.feature.ability.AbilityLoader;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.explosion.Explosion;
import fr.free.totalboumboum.engine.content.feature.gesture.GesturePack;
import fr.free.totalboumboum.engine.content.feature.gesture.anime.AnimesLoader;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.ModulationsLoader;
import fr.free.totalboumboum.engine.content.feature.gesture.trajectory.TrajectoriesLoader;
import fr.free.totalboumboum.engine.content.sprite.SpriteFactoryLoader;
import fr.free.totalboumboum.engine.content.sprite.bomb.BombFactory;
import fr.free.totalboumboum.tools.FileTools;

public class ItemFactoryLoader extends SpriteFactoryLoader
{	
	public static ItemFactory loadItemFactory(String folderPath, String itemName, ArrayList<AbstractAbility> itemAbilities, HashMap<String,ItemFactory> abstractItems) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		ItemFactory result = new ItemFactory(itemName);
		Element root = SpriteFactoryLoader.openFile(folderPath);
		String folder;
		
		// GENERAL
		loadGeneralElement(root,result);
		String baseStr = result.getBase();
		GesturePack gesturePack;
		ArrayList<AbstractAbility> abilities;
		Explosion explosion;
		if(baseStr!=null)
		{	ItemFactory base = abstractItems.get(baseStr);
			gesturePack = base.getGesturePack().copy();
			abilities = base.getAbilities();
			explosion = base.getExplosion();
		}
		else
		{	gesturePack = new GesturePack();
			abilities = new ArrayList<AbstractAbility>();
			explosion = new Explosion();
		}
		result.setGesturePack(gesturePack);
		result.setAbilities(abilities);
		result.setExplosion(explosion);
		
//TODO généraliser tout ça dans spriteFactoryLoader
//TODO vérifier que ça a été fait pour les fire (et autres ?)
//TODO définir la clé associée au sprite dans le set plutot que dans le fichier indiv du sprite
		
		// ABILITIES
		folder = folderPath+File.separator+FileTools.FILE_ABILITIES;
		ArrayList<AbstractAbility> abilities = new ArrayList<AbstractAbility>();
		AbilityLoader.loadAbilityPack(folder,abilities);
		result.setAbilities(abilities);
		
		// ANIMES
		folder = folderPath+File.separator+FileTools.FILE_ANIMES;
		AnimesLoader.loadAnimes(folder,gesturePack,ItemFactory.getAnimeReplacements());
		
		//EXPLOSION
		Explosion explosion = loadExplosionElement(root);
		result.setExplosion(explosion);
		
		//MODULATIONS
		folder = folderPath+File.separator+FileTools.FILE_MODULATIONS;
		ModulationsLoader.loadModulations(folder,gesturePack);
		
		// TRAJECTORIES
		folder = folderPath+File.separator+FileTools.FILE_TRAJECTORIES;
		TrajectoriesLoader.loadTrajectories(folder,gesturePack);
		
		// BOMBSET
		Bombset bombset = GameVariables.level.getBombset();
		result.setBombset(bombset);

		// ITEM ABILITIES
		result.setItemAbilities(itemAbilities);
		
		// result
		return result;
	}	
}

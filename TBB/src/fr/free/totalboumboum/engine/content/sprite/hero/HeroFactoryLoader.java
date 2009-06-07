package fr.free.totalboumboum.engine.content.sprite.hero;

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
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;


import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.engine.container.bombset.Bombset;
import fr.free.totalboumboum.engine.container.bombset.BombsetLoader;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.gesture.anime.AnimePack;
import fr.free.totalboumboum.engine.content.feature.gesture.anime.AnimesLoader;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.ModulationPack;
import fr.free.totalboumboum.engine.content.feature.gesture.trajectory.TrajectoryPack;
import fr.free.totalboumboum.engine.content.sprite.SpriteFactoryLoader;
import fr.free.totalboumboum.tools.FileTools;

public class HeroFactoryLoader extends SpriteFactoryLoader
{	
	public static HeroFactory loadHeroFactory(String folderPath, Level level, PredefinedColor color, ArrayList<AbstractAbility> ablts, ModulationPack permissions, TrajectoryPack trajectories) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		HeroFactory result = new HeroFactory(level);
		Element root = SpriteFactoryLoader.openFile(folderPath);
		String folder;
		
		// GENERAL
		loadGeneralElement(root,result);
		
		// ABILITIES
//		folder = level.getInstancePath()+File.separator+FileTools.FOLDER_HEROES;
//		folder = folder + File.separator+FileTools.FOLDER_ABILITIES;
//		ArrayList<AbstractAbility> abilities = AbilityLoader.loadAbilityPack(folder,level);
		ArrayList<AbstractAbility> abilities = new ArrayList<AbstractAbility>();
		Iterator<AbstractAbility> i = ablts.iterator();
		while (i.hasNext())
		{	AbstractAbility temp = i.next().copy();
			abilities.add(temp);
		}
		result.setAbilities(abilities);
		
		// ANIMES
		folder = folderPath+File.separator+FileTools.FILE_ANIMES;
		AnimePack animePack = AnimesLoader.loadAnimePack(folder,level,color);
		result.setAnimePack(animePack);
		
		//EXPLOSION
		loadExplosionElement(root,level,result);
		
		//PERMISSIONS
//		folder = level.getInstancePath()+File.separator+FileTools.FOLDER_HEROES;
//		folder = folder + File.separator+FileTools.FOLDER_PERMISSIONS;
//		PermissionPack permissionPack = PermissionPackLoader.loadPermissionPack(folder,level);
		ModulationPack permissionPack = permissions.copy();
		result.setPermissionPack(permissionPack);
		
		// TRAJECTORIES
//		folder = level.getInstancePath()+File.separator+FileTools.FOLDER_HEROES;
//		folder = folder + File.separator+FileTools.FOLDER_TRAJECTORIES;
//		TrajectoryPack trajectoryPack = TrajectoryPackLoader.loadTrajectoryPack(folder,level);
		TrajectoryPack trajectoryPack = trajectories.copy();
		result.setTrajectoryPack(trajectoryPack);
		
		// BOMBSET
		folder = level.getInstancePath()+File.separator+FileTools.FOLDER_BOMBS;
		Bombset bombset = BombsetLoader.loadBombset(folder,level,color);
		result.setBombset(bombset);

		// result
		return result;
	}	
}

package fr.free.totalboumboum.engine.content.sprite.fire;

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
import fr.free.totalboumboum.engine.content.feature.gesture.anime.AnimePack;
import fr.free.totalboumboum.engine.content.feature.gesture.anime.AnimePackLoader;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.PermissionPack;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.PermissionPackLoader;
import fr.free.totalboumboum.engine.content.feature.gesture.trajectory.TrajectoryPack;
import fr.free.totalboumboum.engine.content.feature.gesture.trajectory.TrajectoryPackLoader;
import fr.free.totalboumboum.engine.content.sprite.SpriteFactoryLoader;
import fr.free.totalboumboum.tools.FileTools;

public class FireFactoryLoader extends SpriteFactoryLoader
{	
	public static FireFactory loadFireFactory(String folderPath, Level level) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		FireFactory result = new FireFactory(level);
		Element root = SpriteFactoryLoader.openFile(folderPath);
		String folder;
		
		// GENERAL
		loadGeneralElement(root,result);
		
		// ABILITIES
		folder = folderPath+File.separator+FileTools.FILE_ABILITIES;
		ArrayList<AbstractAbility> abilities = AbilityLoader.loadAbilityPack(folder,level);
		result.setAbilities(abilities);
		
		// ANIMES
		folder = folderPath+File.separator+FileTools.FILE_ANIMES;
		AnimePack animePack = AnimePackLoader.loadAnimePack(folder,level);
		result.setAnimePack(animePack);
		
		//EXPLOSION
		loadExplosionElement(root,level,result);
		
		//PERMISSIONS
		folder = folderPath+File.separator+FileTools.FILE_PERMISSIONS;
		PermissionPack permissionPack = PermissionPackLoader.loadPermissionPack(folder,level);
		result.setPermissionPack(permissionPack);
		
		// TRAJECTORIES
		folder = folderPath+File.separator+FileTools.FILE_TRAJECTORIES;
		TrajectoryPack trajectoryPack = TrajectoryPackLoader.loadTrajectoryPack(folder,level);
		result.setTrajectoryPack(trajectoryPack);
		
		// BOMBSET
		Bombset bombset = new Bombset();
		result.setBombset(bombset);

		// result
		return result;
	}	
}

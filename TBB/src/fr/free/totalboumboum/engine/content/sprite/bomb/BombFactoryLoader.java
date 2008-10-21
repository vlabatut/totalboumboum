package fr.free.totalboumboum.engine.content.sprite.bomb;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.engine.container.bombset.Bombset;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.ability.AbilityLoader;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.anime.AnimePack;
import fr.free.totalboumboum.engine.content.feature.anime.AnimePackLoader;
import fr.free.totalboumboum.engine.content.feature.permission.PermissionPack;
import fr.free.totalboumboum.engine.content.feature.permission.PermissionPackLoader;
import fr.free.totalboumboum.engine.content.feature.trajectory.TrajectoryPack;
import fr.free.totalboumboum.engine.content.feature.trajectory.TrajectoryPackLoader;
import fr.free.totalboumboum.engine.content.sprite.SpriteFactoryLoader;
import fr.free.totalboumboum.tools.FileTools;

public class BombFactoryLoader extends SpriteFactoryLoader
{	
	public static BombFactory loadBombFactory(String folderPath, Level level, String bombName, PredefinedColor color, Bombset bombset) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		BombFactory result = new BombFactory(level,bombName);
		Element root = SpriteFactoryLoader.openFile(folderPath);
		String folder;
		
		// ABILITIES
		folder = folderPath+File.separator+FileTools.FILE_ABILITIES;
		ArrayList<AbstractAbility> abilities = AbilityLoader.loadAbilityPack(folder,level);
		result.setAbilities(abilities);
		
		// ANIMES
		folder = folderPath+File.separator+FileTools.FILE_ANIMES;
		AnimePack animePack;
		if(color==null)
			animePack = AnimePackLoader.loadAnimePack(folder,level);
		else
			animePack = AnimePackLoader.loadAnimePack(folder,level,color);
		result.setAnimePack(animePack);
		
		//EXPLOSION
		loadExplosion(root,level,result);
		
		//PERMISSIONS
		folder = folderPath+File.separator+FileTools.FILE_PERMISSIONS;
		PermissionPack permissionPack = PermissionPackLoader.loadPermissionPack(folder,level);
		result.setPermissionPack(permissionPack);
		
		// TRAJECTORIES
		folder = folderPath+File.separator+FileTools.FILE_TRAJECTORIES;
		TrajectoryPack trajectoryPack = TrajectoryPackLoader.loadTrajectoryPack(folder,level);
		result.setTrajectoryPack(trajectoryPack);
		
		// BOMBSET
		result.setBombset(bombset);

		// result
		return result;
	}	
}

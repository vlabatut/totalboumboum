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

import javax.xml.parsers.ParserConfigurationException;


import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.engine.container.bombset.Bombset;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.ability.AbilityLoader;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.gesture.GesturePack;
import fr.free.totalboumboum.engine.content.feature.gesture.anime.AnimesLoader;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.ModulationsLoader;
import fr.free.totalboumboum.engine.content.feature.gesture.trajectory.TrajectoriesLoader;
import fr.free.totalboumboum.engine.content.sprite.SpriteFactoryLoader;
import fr.free.totalboumboum.tools.FileTools;

public class BombFactoryLoader extends SpriteFactoryLoader
{	
	public static BombFactory loadBombFactory(String folderPath, Level level, String bombName, PredefinedColor color, Bombset bombset) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		BombFactory result = new BombFactory(level,bombName);
		Element root = SpriteFactoryLoader.openFile(folderPath);
		String folder;
		GesturePack gesturePack = new GesturePack();
		result.setGesturePack(gesturePack);
		
		// GENERAL
		loadGeneralElement(root,result);
		
		// ABILITIES
		folder = folderPath+File.separator+FileTools.FILE_ABILITIES;
		ArrayList<AbstractAbility> abilities = AbilityLoader.loadAbilityPack(folder,level);
		result.setAbilities(abilities);
		
		// ANIMES
		folder = folderPath+File.separator+FileTools.FILE_ANIMES;
		folder = folderPath+File.separator+FileTools.FILE_ANIMES;
		if(color==null)
			AnimesLoader.loadAnimes(folder,gesturePack,level);
		else
			AnimesLoader.loadAnimes(folder,gesturePack,level,color);
		
		//EXPLOSION
		loadExplosionElement(root,level,result);
		
		//MODULATIONS
		folder = folderPath+File.separator+FileTools.FILE_MODULATIONS;
		ModulationsLoader.loadModulations(folder,gesturePack,level);
		
		// TRAJECTORIES
		folder = folderPath+File.separator+FileTools.FILE_TRAJECTORIES;
		TrajectoriesLoader.loadTrajectories(folder,gesturePack,level);
		
		// BOMBSET
		result.setBombset(bombset);

		// result
		return result;
	}	
}

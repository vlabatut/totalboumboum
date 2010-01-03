package org.totalboumboum.engine.content.sprite.hero;

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
import org.totalboumboum.engine.content.feature.gesture.anime.AnimesLoader;
import org.totalboumboum.engine.content.feature.gesture.modulation.ModulationsLoader;
import org.totalboumboum.engine.content.feature.gesture.trajectory.TrajectoriesLoader;
import org.totalboumboum.engine.content.sprite.SpriteFactoryLoader;
import org.totalboumboum.tools.files.FileNames;
import org.xml.sax.SAXException;

public class HeroFactoryLoader extends SpriteFactoryLoader
{	
	/* 
	 * load the base HeroFactory (i.e. no graphics nor bombset)
	 */
	public static HeroFactory loadHeroFactory(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		HeroFactory result = new HeroFactory();
		Element root = SpriteFactoryLoader.openFile(folderPath);
		String folder;
		
		// GENERAL
		loadGeneralElement(root,result,new HashMap<String,HeroFactory>());
		GesturePack gesturePack = result.getGesturePack();

		// ABILITIES
		ArrayList<AbstractAbility> abilities = result.getAbilities();
		folder = folderPath + File.separator+FileNames.FOLDER_ABILITIES;
		AbilityLoader.loadAbilityPack(folder,abilities);

		// EXPLOSION
		String explosionName = loadExplosionElement(root);
		if(explosionName!=null)
			result.setExplosionName(explosionName);
		
		// MODULATIONS
		folder = folderPath+File.separator+FileNames.FOLDER_MODULATIONS;
		ModulationsLoader.loadModulations(folder,gesturePack,Role.HERO);
		
		// TRAJECTORIES
		folder = folderPath+File.separator+FileNames.FOLDER_TRAJECTORIES;
		TrajectoriesLoader.loadTrajectories(folder,gesturePack);
		
		// result
		return result;
	}

	/*
	 * complete the base HeroFactory with graphics and bombset
	 */
	public static HeroFactory completeHeroFactory(String folderPath, PredefinedColor color, HeroFactory base) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		HeroFactory result = new HeroFactory();
		Element root = SpriteFactoryLoader.openFile(folderPath);
		String folder;
		
		// GENERAL
		loadGeneralElement(root,result,base);
		GesturePack gesturePack = result.getGesturePack();
		
		// ANIMES
		folder = folderPath+File.separator+FileNames.FOLDER_ANIMES;
		AnimesLoader.loadAnimes(folder,gesturePack,color,HeroFactory.getAnimeReplacements());
		
		// BOMBSET
		result.setBombsetColor(color);

		// result
		initDefaultGestures(gesturePack,Role.HERO);
		return result;
	}
}

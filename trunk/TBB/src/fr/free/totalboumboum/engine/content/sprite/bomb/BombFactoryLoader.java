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

public class BombFactoryLoader extends SpriteFactoryLoader
{	
	/*
	 * load everything except the animes (cf. completeBombFactory)
	 * because the bombset is always loaded in a neutral way, without graphics.
	 * it is then completed depending on the needed colors (but the rest of the features stay the same) 
	 */
	public static BombFactory loadBombFactory(String folderPath, Level level, String bombName/*, PredefinedColor color, Bombset bombset*/, HashMap<String,BombFactory> abstractBombs) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		BombFactory result = new BombFactory(level,bombName);
		Element root = SpriteFactoryLoader.openFile(folderPath);
		String folder;
		
		// GENERAL
		loadGeneralElement(root,result);
		String baseStr = result.getBase();
		GesturePack gesturePack;
		ArrayList<AbstractAbility> abilities;
		Explosion explosion;
		if(baseStr!=null)
		{	BombFactory base = abstractBombs.get(baseStr);
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
		
		// ABILITIES
		folder = folderPath+File.separator+FileTools.FILE_ABILITIES;
		AbilityLoader.loadAbilityPack(folder,level,abilities);
		
		//EXPLOSION
		Explosion exp = loadExplosionElement(root,level);
		if(exp!=null)
			result.setExplosion(exp); 
		
		//MODULATIONS
		folder = folderPath+File.separator+FileTools.FILE_MODULATIONS;
		ModulationsLoader.loadModulations(folder,gesturePack,level);
		
		// TRAJECTORIES
		folder = folderPath+File.separator+FileTools.FILE_TRAJECTORIES;
		TrajectoriesLoader.loadTrajectories(folder,gesturePack,level);
		
		// result
		return result;
	}	

	/*
	 * load the animes only
	 * (complete the sprite depending on the specified color)
	 */
	public static void completeBombFactory(BombFactory result, String folderPath, Level level, PredefinedColor color, Bombset bombset, HashMap<String,BombFactory> abstractBombs) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String folder;
		
		// GENERAL
		GesturePack gesturePack = result.getGesturePack();
		String baseStr = result.getBase();
		if(baseStr!=null)
		{	BombFactory base = abstractBombs.get(baseStr);
			GesturePack gp = base.getGesturePack();
			gesturePack.copyAnimesFrom(gp);
		}
		
		// ANIMES
		folder = folderPath+File.separator+FileTools.FILE_ANIMES;
		if(color==null)
			AnimesLoader.loadAnimes(folder,gesturePack,level,BombFactory.getAnimeReplacements());
		else
			AnimesLoader.loadAnimes(folder,gesturePack,level,color,BombFactory.getAnimeReplacements());
		
		// BOMBSET
		result.setBombset(bombset);
	}	
}

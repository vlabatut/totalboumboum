package fr.free.totalboumboum.engine.content.sprite.hero;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.data.profile.PredefinedColor;
import fr.free.totalboumboum.engine.container.bombset.Bombset;
import fr.free.totalboumboum.engine.container.bombset.BombsetLoader;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.ability.AbilityLoader;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.anime.AnimePack;
import fr.free.totalboumboum.engine.content.feature.anime.AnimePackLoader;
import fr.free.totalboumboum.engine.content.feature.explosion.Explosion;
import fr.free.totalboumboum.engine.content.feature.permission.PermissionPack;
import fr.free.totalboumboum.engine.content.feature.permission.PermissionPackLoader;
import fr.free.totalboumboum.engine.content.feature.trajectory.TrajectoryPack;
import fr.free.totalboumboum.engine.content.feature.trajectory.TrajectoryPackLoader;
import fr.free.totalboumboum.engine.content.sprite.SpriteFactoryLoader;
import fr.free.totalboumboum.engine.content.sprite.fire.FireFactory;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.tools.FileTools;


public class HeroFactoryLoader extends SpriteFactoryLoader
{	
	public static HeroFactory loadHeroFactory(String folderPath, Level level, PredefinedColor color) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		HeroFactory result = new HeroFactory(level);
		Element root = SpriteFactoryLoader.openFile(folderPath);
		String folder;
		
		// ABILITIES
		folder = level.getInstancePath()+File.separator+FileTools.FOLDER_HEROES;
		folder = folder + File.separator+FileTools.FOLDER_ABILITIES;
		ArrayList<AbstractAbility> abilities = AbilityLoader.loadAbilityPack(folder,level);
		result.setAbilities(abilities);
		
		// ANIMES
		folder = folderPath+File.separator+FileTools.FILE_ANIMES;
		AnimePack animePack = AnimePackLoader.loadAnimePack(folder,level,color);
		result.setAnimePack(animePack);
		
		//EXPLOSION
		loadExplosion(root,level,result);
		
		//PERMISSIONS
		folder = level.getInstancePath()+File.separator+FileTools.FOLDER_HEROES;
		folder = folder + File.separator+FileTools.FOLDER_PERMISSIONS;
		PermissionPack permissionPack = PermissionPackLoader.loadPermissionPack(folder,level);
		result.setPermissionPack(permissionPack);
		
		// TRAJECTORIES
		folder = level.getInstancePath()+File.separator+FileTools.FOLDER_HEROES;
		folder = folder + File.separator+FileTools.FOLDER_TRAJECTORIES;
		TrajectoryPack trajectoryPack = TrajectoryPackLoader.loadTrajectoryPack(folder,level);
		result.setTrajectoryPack(trajectoryPack);
		
		// BOMBSET
		folder = level.getInstancePath()+File.separator+FileTools.FOLDER_BOMBS;
		Bombset bombset = BombsetLoader.loadBombset(folder,level,color);
		result.setBombset(bombset);

		// result
		return result;
	}	
}

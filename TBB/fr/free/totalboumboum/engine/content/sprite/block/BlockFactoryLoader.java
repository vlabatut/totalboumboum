package fr.free.totalboumboum.engine.content.sprite.block;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;


import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.container.bombset.Bombset;
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


public class BlockFactoryLoader extends SpriteFactoryLoader
{	
	public static BlockFactory loadBlockFactory(String folderPath, Level level) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		BlockFactory result = new BlockFactory(level);
		Element root = SpriteFactoryLoader.openFile(folderPath);
		String folder;
		
		// ABILITIES
		folder = folderPath+File.separator+FileTools.FILE_ABILITIES;
		ArrayList<AbstractAbility> abilities = AbilityLoader.loadAbilityPack(folder,level);
		result.setAbilities(abilities);
		
		// ANIMES
		folder = folderPath+File.separator+FileTools.FILE_ANIMES;
		AnimePack animePack = AnimePackLoader.loadAnimePack(folder,level);
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
		Bombset bombset = level.getBombset();
		result.setBombset(bombset);

		// result
		return result;
	}	
}

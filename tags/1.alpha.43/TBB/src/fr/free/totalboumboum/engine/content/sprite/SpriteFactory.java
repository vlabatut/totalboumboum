package fr.free.totalboumboum.engine.content.sprite;

import java.util.ArrayList;
import java.util.Iterator;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.container.bombset.Bombset;
import fr.free.totalboumboum.engine.container.level.HollowLevel;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.anime.AnimePack;
import fr.free.totalboumboum.engine.content.feature.explosion.Explosion;
import fr.free.totalboumboum.engine.content.feature.permission.PermissionPack;
import fr.free.totalboumboum.engine.content.feature.trajectory.TrajectoryPack;
import fr.free.totalboumboum.engine.content.manager.AbilityManager;
import fr.free.totalboumboum.engine.content.manager.AnimeManager;
import fr.free.totalboumboum.engine.content.manager.BombsetManager;
import fr.free.totalboumboum.engine.content.manager.ControlManager;
import fr.free.totalboumboum.engine.content.manager.DelayManager;
import fr.free.totalboumboum.engine.content.manager.ExplosionManager;
import fr.free.totalboumboum.engine.content.manager.ItemManager;
import fr.free.totalboumboum.engine.content.manager.PermissionManager;
import fr.free.totalboumboum.engine.content.manager.TrajectoryManager;
import fr.free.totalboumboum.engine.content.sprite.bomb.BombFactory;
import fr.free.totalboumboum.engine.loop.Loop;


public abstract class SpriteFactory<T extends Sprite>
{
	// managers
	protected AnimePack animePack;
	protected TrajectoryPack trajectoryPack;
	protected PermissionPack permissionPack;
	protected Bombset bombset;
	protected Explosion explosion;
	protected ArrayList<AbstractAbility> abilities;
	//
	protected Level level;
	
	public SpriteFactory(Level level)
	{	this.level = level;
	}
	
	
	protected void setCommonManager(Sprite sprite)
	{	// anime
		AnimeManager animeManager = new AnimeManager(sprite);
		animeManager.setAnimePack(animePack);
		sprite.setAnimeManager(animeManager);
		// trajectory
		TrajectoryManager trajectoryManager = new TrajectoryManager(sprite);
		trajectoryManager.setTrajectoryPack(trajectoryPack);
		sprite.setTrajectoryManager(trajectoryManager);
		// bombset
		BombsetManager bombsetManager = new BombsetManager(sprite);
		bombsetManager.setBombset(bombset);
		sprite.setBombsetManager(bombsetManager);
		// explosion
		ExplosionManager explosionManager = new ExplosionManager(sprite);
		explosionManager.setExplosion(explosion);
		sprite.setExplosionManager(explosionManager);
		// permission
		PermissionManager permissionManager = new PermissionManager(sprite);
		permissionManager.setPermissionPack(permissionPack);
		sprite.setPermissionManager(permissionManager);
		// item
		ItemManager itemManager = new ItemManager(sprite);
		sprite.setItemManager(itemManager);
		// ability
		AbilityManager abilityManager = new AbilityManager(sprite);
		abilityManager.addDirectAbilities(abilities);
		sprite.setAbilityManager(abilityManager);
		// delay
		DelayManager delayManager = new DelayManager(sprite);
		sprite.setDelayManager(delayManager);
		// control
		ControlManager controlManager = new ControlManager(sprite);
		sprite.setControlManager(controlManager);
	}
	
	public abstract T makeSprite();


	public AnimePack getAnimePack()
	{	return animePack;
	}
	public void setAnimePack(AnimePack animePack)
	{	this.animePack = animePack;
	}

	public TrajectoryPack getTrajectoryPack()
	{	return trajectoryPack;
	}
	public void setTrajectoryPack(TrajectoryPack trajectoryPack)
	{	this.trajectoryPack = trajectoryPack;
	}

	public PermissionPack getPermissionPack()
	{	return permissionPack;
	}
	public void setPermissionPack(PermissionPack permissionPack)
	{	this.permissionPack = permissionPack;
	}

	public Bombset getBombset()
	{	return bombset;
	}
	public void setBombset(Bombset bombset)
	{	this.bombset = bombset;
	}

	public Explosion getExplosion()
	{	return explosion;
	}
	public void setExplosion(Explosion explosion)
	{	this.explosion = explosion;
	}

	public ArrayList<AbstractAbility> getAbilities()
	{	return abilities;
	}
	public void setAbilities(ArrayList<AbstractAbility> abilities)
	{	this.abilities = abilities;
	}

	public Level getLevel()
	{	return level;	
	}
	
	protected boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// abilities
			{	Iterator<AbstractAbility> it = abilities.iterator();
				while(it.hasNext())
				{	AbstractAbility temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// packs & sets
			bombset.finish();
			bombset = null;
			explosion.finish();
			explosion = null;
			permissionPack.finish();
			permissionPack = null;
			trajectoryPack.finish();
			trajectoryPack = null;
			// misc
			level = null;
			
		}
	}
}
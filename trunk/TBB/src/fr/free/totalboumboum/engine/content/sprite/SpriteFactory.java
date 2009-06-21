package fr.free.totalboumboum.engine.content.sprite;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fr.free.totalboumboum.engine.container.bombset.Bombset;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.explosion.Explosion;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.feature.gesture.GesturePack;
import fr.free.totalboumboum.engine.content.feature.gesture.action.IncompatibleParameterException;
import fr.free.totalboumboum.engine.content.manager.ability.AbilityManager;
import fr.free.totalboumboum.engine.content.manager.anime.AnimeManager;
import fr.free.totalboumboum.engine.content.manager.bombset.BombsetManager;
import fr.free.totalboumboum.engine.content.manager.control.ControlManager;
import fr.free.totalboumboum.engine.content.manager.delay.DelayManager;
import fr.free.totalboumboum.engine.content.manager.explosion.ExplosionManager;
import fr.free.totalboumboum.engine.content.manager.item.ItemManager;
import fr.free.totalboumboum.engine.content.manager.modulation.ModulationManager;
import fr.free.totalboumboum.engine.content.manager.trajectory.TrajectoryManager;

public abstract class SpriteFactory<T extends Sprite>
{
	
	public SpriteFactory(Level level)
	{	this.level = level;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract T makeSprite();

	protected void initSprite(Sprite sprite)
	{	// name
		sprite.setName(name);
		
		// gesture pack
		GesturePack gp = gesturePack;
		sprite.setGesturePack(gp);
		
		// anime
		AnimeManager animeManager = new AnimeManager(sprite);
		sprite.setAnimeManager(animeManager);
		
		// trajectory
		TrajectoryManager trajectoryManager = new TrajectoryManager(sprite);
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
		ModulationManager permissionManager = new ModulationManager(sprite);
		sprite.setModulationManager(permissionManager);
		
		// item
		ItemManager itemManager = new ItemManager(sprite);
		sprite.setItemManager(itemManager);
		
		// ability
		AbilityManager abilityManager = new AbilityManager(sprite);
		try
		{	abilityManager.addDirectAbilities(abilities);
		}
		catch (IncompatibleParameterException e)
		{	e.printStackTrace();
		}
		sprite.setAbilityManager(abilityManager);
		
		// delay
		DelayManager delayManager = new DelayManager(sprite);
		sprite.setDelayManager(delayManager);
		
		// control
		ControlManager controlManager = new ControlManager(sprite);
		sprite.setControlManager(controlManager);
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected String name;

	public String getName()
	{	return name;
	}
	
	public void setName(String name)
	{	this.name = name;
	}
	
	/////////////////////////////////////////////////////////////////
	// GESTURE PACK		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected GesturePack gesturePack;

	public GesturePack getGesturePack()
	{	return gesturePack;
	}
	
	public void setGesturePack(GesturePack gesturePack)
	{	this.gesturePack = gesturePack;
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMBSET			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Bombset bombset;

	public Bombset getBombset()
	{	return bombset;
	}
	
	public void setBombset(Bombset bombset)
	{	this.bombset = bombset;
	}

	/////////////////////////////////////////////////////////////////
	// EXPLOSION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Explosion explosion;

	public Explosion getExplosion()
	{	return explosion;
	}
	
	public void setExplosion(Explosion explosion)
	{	this.explosion = explosion;
	}

	/////////////////////////////////////////////////////////////////
	// ABILITIES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ArrayList<AbstractAbility> abilities;

	public ArrayList<AbstractAbility> getAbilities()
	{	return abilities;
	}
	
	public void setAbilities(ArrayList<AbstractAbility> abilities)
	{	this.abilities = abilities;
	}

	/////////////////////////////////////////////////////////////////
	// LEVEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Level level;
	public Level getLevel()
	{	return level;	
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
			gesturePack.finish();
			gesturePack = null;
			// misc
			level = null;
			name = null;
		}
	}
}

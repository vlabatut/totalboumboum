package org.totalboumboum.engine.content.sprite;

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

import org.totalboumboum.configuration.profile.PredefinedColor;
import org.totalboumboum.engine.container.bombset.Bombset;
import org.totalboumboum.engine.container.explosionset.Explosion;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.gesture.GesturePack;
import org.totalboumboum.engine.content.manager.ability.AbilityManager;
import org.totalboumboum.engine.content.manager.anime.AnimeManager;
import org.totalboumboum.engine.content.manager.bombset.BombsetManager;
import org.totalboumboum.engine.content.manager.control.ControlManager;
import org.totalboumboum.engine.content.manager.delay.DelayManager;
import org.totalboumboum.engine.content.manager.explosion.ExplosionManager;
import org.totalboumboum.engine.content.manager.item.ItemManager;
import org.totalboumboum.engine.content.manager.modulation.ModulationManager;
import org.totalboumboum.engine.content.manager.trajectory.RegularTrajectoryManager;
import org.totalboumboum.engine.content.manager.trajectory.ReplayTrajectoryManager;
import org.totalboumboum.engine.content.manager.trajectory.TrajectoryManager;
import org.totalboumboum.engine.loop.ReplayLoop;
import org.totalboumboum.game.round.RoundVariables;

public abstract class SpriteFactory<T extends Sprite> extends AbstractSpriteFactory<T,GesturePack>
{	
	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected PredefinedColor color = null;
	
	public PredefinedColor getColor()
	{	return color;
	}
	
	public void setColor(PredefinedColor color)
	{	this.color = color;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract T makeSprite(Tile tile);

	protected void initSprite(T sprite)
	{	// name
		sprite.setName(name);
//if(name.equalsIgnoreCase("fireproof"))
//	System.out.println();

		// gesture pack
		GesturePack gp = gesturePack;
		sprite.setGesturePack(gp);
		
		// anime
		AnimeManager animeManager = new AnimeManager(sprite);
		sprite.setAnimeManager(animeManager);
		
		// trajectory
		TrajectoryManager trajectoryManager;
		if(RoundVariables.loop instanceof ReplayLoop)
			trajectoryManager = new ReplayTrajectoryManager(sprite);
		else
			trajectoryManager = new RegularTrajectoryManager(sprite);
		sprite.setTrajectoryManager(trajectoryManager);
		
		// bombset
		BombsetManager bombsetManager;
		if(RoundVariables.loop instanceof ReplayLoop)
			bombsetManager = null;
		else
			bombsetManager = new BombsetManager(sprite);
		bombsetManager.setBombset(bombset);
//if(bombset==null)
//	System.out.println();
		sprite.setBombsetManager(bombsetManager);
		
		// explosion
		ExplosionManager explosionManager;
		if(RoundVariables.loop instanceof ReplayLoop)
			explosionManager = null;
		else
			explosionManager = new ExplosionManager(sprite);
		explosionManager.setExplosion(explosion);
		sprite.setExplosionManager(explosionManager);
		
		// modulations
		ModulationManager modulationManager;
		if(RoundVariables.loop instanceof ReplayLoop)
			modulationManager = null;
		else
			modulationManager = new ModulationManager(sprite);
		sprite.setModulationManager(modulationManager);
		
		// item
		ItemManager itemManager;
		if(RoundVariables.loop instanceof ReplayLoop)
			itemManager = null;
		else
			itemManager = new ItemManager(sprite);
		sprite.setItemManager(itemManager);
		
		// ability
		AbilityManager abilityManager;
		if(RoundVariables.loop instanceof ReplayLoop)
			abilityManager = null;
		else
		{	abilityManager = new AbilityManager(sprite);
			abilityManager.addDirectAbilities(abilities);
		}
		sprite.setAbilityManager(abilityManager);
		
		// delay
		DelayManager delayManager;
		if(RoundVariables.loop instanceof ReplayLoop)
			delayManager = null;
		else
			delayManager = new DelayManager(sprite);
		sprite.setDelayManager(delayManager);
		
		// control
		ControlManager controlManager;
		if(RoundVariables.loop instanceof ReplayLoop)
			controlManager = null;
		else
			controlManager = new ControlManager(sprite);
		sprite.setControlManager(controlManager);
	}
	
	/////////////////////////////////////////////////////////////////
	// INSTANCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Instance instance;
	
	public void setInstance(Instance instance)
	{	// instance
		this.instance = instance;	
		
		// bombset
		bombset = instance.getBombsetMap().getBombset(color);
		
		// explosion
		if(explosionName!=null)
			explosion = instance.getExplosionSet().getExplosion(explosionName);
		else
			explosion = new Explosion();
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMBSET			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Bombset bombset;
	
	public Bombset getBombset()
	{	return bombset;
	}
	
	/////////////////////////////////////////////////////////////////
	// EXPLOSION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Explosion explosion;

	public Explosion getExplosion()
	{	return explosion;
	}
}

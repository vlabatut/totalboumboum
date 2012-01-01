package org.totalboumboum.engine.content.sprite;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import org.totalboumboum.engine.container.bombset.Bombset;
import org.totalboumboum.engine.container.explosionset.Explosion;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.gesture.GesturePack;
import org.totalboumboum.engine.content.manager.ability.AbilityManager;
import org.totalboumboum.engine.content.manager.ability.EmptyAbilityManager;
import org.totalboumboum.engine.content.manager.ability.FullAbilityManager;
import org.totalboumboum.engine.content.manager.anime.AnimeManager;
import org.totalboumboum.engine.content.manager.delay.DelayManager;
import org.totalboumboum.engine.content.manager.delay.EmptyDelayManager;
import org.totalboumboum.engine.content.manager.delay.FullDelayManager;
import org.totalboumboum.engine.content.manager.explosion.EmptyExplosionManager;
import org.totalboumboum.engine.content.manager.explosion.ExplosionManager;
import org.totalboumboum.engine.content.manager.explosion.FullExplosionManager;
import org.totalboumboum.engine.content.manager.modulation.ModulationManager;
import org.totalboumboum.engine.content.manager.modulation.FullModulationManager;
import org.totalboumboum.engine.content.manager.modulation.EmptyModulationManager;
import org.totalboumboum.engine.loop.ReplayLoop;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
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

	protected void initSprite(T result)
	{	// name
		result.setName(name);
//if(name.equalsIgnoreCase("fireproof"))
//	System.out.println();

		// gesture pack
		GesturePack gp = gesturePack;
		result.setGesturePack(gp);
		
		// anime
		AnimeManager animeManager = new AnimeManager(result);
		result.setAnimeManager(animeManager);
		
		// trajectory

		// bombset
		
		// explosion
		ExplosionManager explosionManager;
		if(RoundVariables.loop instanceof ReplayLoop)
			explosionManager = new EmptyExplosionManager(result);
		else
		{	explosionManager = new FullExplosionManager(result);
			explosionManager.setExplosion(explosion);
		}
		result.setExplosionManager(explosionManager);
		
		// modulations
		ModulationManager modulationManager;
		if(RoundVariables.loop instanceof ReplayLoop)
			modulationManager = new EmptyModulationManager(result);
		else
			modulationManager = new FullModulationManager(result);
		result.setModulationManager(modulationManager);
		
		// item

		// ability
		AbilityManager abilityManager;
		if(RoundVariables.loop instanceof ReplayLoop)
			abilityManager = new EmptyAbilityManager(result);
		else
		{	abilityManager = new FullAbilityManager(result);
			abilityManager.addDirectAbilities(abilities);
		}
		result.setAbilityManager(abilityManager);
		
		// delay
		DelayManager delayManager;
		if(RoundVariables.loop instanceof ReplayLoop)
			delayManager = new EmptyDelayManager(result);
		else
			delayManager = new FullDelayManager(result);
		result.setDelayManager(delayManager);
		
		// control
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

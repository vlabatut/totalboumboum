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

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.profile.PredefinedColor;
import org.totalboumboum.engine.container.bombset.Bombset;
import org.totalboumboum.engine.container.explosionset.Explosion;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.gesture.GesturePack;
import org.totalboumboum.engine.content.manager.ability.AbilityManager;
import org.totalboumboum.engine.content.manager.anime.AnimeManager;
import org.totalboumboum.engine.content.manager.bombset.BombsetManager;
import org.totalboumboum.engine.content.manager.control.ControlManager;
import org.totalboumboum.engine.content.manager.delay.DelayManager;
import org.totalboumboum.engine.content.manager.explosion.ExplosionManager;
import org.totalboumboum.engine.content.manager.item.ItemManager;
import org.totalboumboum.engine.content.manager.modulation.ModulationManager;
import org.totalboumboum.engine.content.manager.trajectory.TrajectoryManager;
import org.xml.sax.SAXException;


public abstract class SpriteFactory<T extends Sprite> implements Serializable
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract T makeSprite(Tile tile);

	protected void initSprite(Sprite sprite)
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
		TrajectoryManager trajectoryManager = new TrajectoryManager(sprite);
		sprite.setTrajectoryManager(trajectoryManager);
		
		// bombset
		BombsetManager bombsetManager = new BombsetManager(sprite);
		bombsetManager.setBombset(bombset);
if(bombset==null)
	System.out.println();
		sprite.setBombsetManager(bombsetManager);
		
		// explosion
		ExplosionManager explosionManager = new ExplosionManager(sprite);
		explosionManager.setExplosion(explosion);
		sprite.setExplosionManager(explosionManager);
		
		// modulations
		ModulationManager permissionManager = new ModulationManager(sprite);
		sprite.setModulationManager(permissionManager);
		
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
	
	/////////////////////////////////////////////////////////////////
	// INSTANCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Instance instance;
	
	public void setInstance(Instance instance) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	this.instance = instance;	
		bombset = instance.getBombsetMap().getBombset(bombsetColor);
		if(explosionName!=null)
			explosion = instance.getExplosionSet().getExplosion(explosionName);
		else
			explosion = new Explosion();
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
	// BASE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected String base;

	public String getBase()
	{	return base;
	}
	
	public void setBase(String base)
	{	this.base = base;
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
	protected PredefinedColor bombsetColor;
	
	public Bombset getBombset()
	{	return bombset;
	}
	
	public void setBombsetColor(PredefinedColor bombsetColor)
	{	this.bombsetColor = bombsetColor;
	}

	/////////////////////////////////////////////////////////////////
	// EXPLOSION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Explosion explosion;
	protected String explosionName;

	public Explosion getExplosion()
	{	return explosion;
	}
	
	public String getExplosionName()
	{	return explosionName;
	}
	
	public void setExplosionName(String explosionName)
	{	this.explosionName = explosionName;
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
			name = null;
		}
	}
}
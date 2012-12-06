package org.totalboumboum.engine.content.sprite.item;

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.manager.ability.AbilityManager;
import org.totalboumboum.engine.content.manager.anime.AnimeManager;
import org.totalboumboum.engine.content.manager.bombset.BombsetManager;
import org.totalboumboum.engine.content.manager.control.ControlManager;
import org.totalboumboum.engine.content.manager.delay.DelayManager;
import org.totalboumboum.engine.content.manager.event.EventManager;
import org.totalboumboum.engine.content.manager.explosion.ExplosionManager;
import org.totalboumboum.engine.content.manager.item.ItemManager;
import org.totalboumboum.engine.content.manager.modulation.ModulationManager;
import org.totalboumboum.engine.content.manager.trajectory.TrajectoryManager;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * Represents an item sprite.
 * 
 * @author Vincent Labatut
 */
public class Item extends Sprite
{	
	/**
	 * Builds a new item sprite.
	 */
	public Item()
	{	super();
		itemAbilities = new ArrayList<AbstractAbility>();
	}

	/////////////////////////////////////////////////////////////////
	// ROLE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Role getRole()
	{	return Role.ITEM;
	}

	/////////////////////////////////////////////////////////////////
	// ITEM ABILITIES	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Original abilities given by this item (used to reset the item) */
	private List<AbstractAbility> originalItemAbilities;
	/** Current abilities given by this item */
	private List<AbstractAbility> itemAbilities;
	
	/**
	 * Returns the original state ability whose name
	 * is specified as a paramter, or {@code null} if
	 * it could not be found.
	 *  
	 * @param name
	 * 		Name of the desired ability.
	 * @return
	 * 		The requested ability, or {@code null}. 
	 */
	public StateAbility getOriginalItemAbility(String name)
	{	StateAbility result = null;
		Iterator<AbstractAbility> i = originalItemAbilities.iterator();
		while(i.hasNext() && result==null)
		{	AbstractAbility ab = i.next();
			if(ab instanceof StateAbility)
			{	StateAbility ablt = (StateAbility)ab;
				if(ablt.getName().equals(name))
					result = ablt;
			}
		}
		if(result==null)
			result = new StateAbility(name);
		return result;
	}
	
	/**
	 * Returns the current state ability whose name
	 * is specified as a paramter, or {@code null} if
	 * it could not be found.
	 *  
	 * @param name
	 * 		Name of the desired ability.
	 * @return
	 * 		The requested ability, or {@code null}. 
	 */
	public StateAbility getCurrentItemAbility(String name)
	{	StateAbility result = null;
		Iterator<AbstractAbility> i = itemAbilities.iterator();
		while(i.hasNext() && result==null)
		{	AbstractAbility ab = i.next();
			if(ab instanceof StateAbility)
			{	StateAbility ablt = (StateAbility)ab;
				if(ablt.getName().equals(name))
					result = ablt;
			}
		}
		if(result==null)
			result = new StateAbility(name);
		return result;
	}

	/**
	 * Returns the list of abilities currently
	 * provided by this item.
	 * 
	 * @return
	 * 		List of abilities.
	 */
	public List<AbstractAbility> getItemAbilities()
	{	return itemAbilities;
	}
	
	/**
	 * Initializes the abilities provided by this item.
	 * The specified abilities are added (no copy).
	 * 
	 * @param abilities
	 * 		The ability this item will provide.
	 */
	public void initItemAbilities(List<AbstractAbility> abilities)
	{	originalItemAbilities = abilities;
		Iterator<AbstractAbility> i = abilities.iterator();
		while(i.hasNext())
			addItemAbility(i.next());
	}

	/**
	 * Adds a new provided ability to this item.
	 * The ability is copied.
	 * 
	 * @param ability
	 * 		The concerned ability.
	 */
	private void addItemAbility(AbstractAbility ability)
	{	AbstractAbility copy = ability.copy();
		itemAbilities.add(copy);
	}

	/**
	 * Reset the abilities provided by this item
	 * to their initial values.
	 */
	public void reinitItemAbilities()
	{	// NOTE maybe the old abilities should be killed?
		itemAbilities.clear();
		initItemAbilities(originalItemAbilities);
	}
	
	/////////////////////////////////////////////////////////////////
	// ITEM NAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Name of this item */
	private String itemName;
	
	/**
	 * Returns the name of this item.
	 * 
	 * @return
	 * 		The item name.
	 */
	public String getItemName()
	{	return itemName;
	}
	
	/**
	 * Changes the name of this item.
	 * 
	 * @param itemName
	 * 		New name.
	 */
	public void setItemName(String itemName)
	{	this.itemName = itemName;
	}	

	/////////////////////////////////////////////////////////////////
	// EXECUTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Copies this item, which is supposed to be hidden 
	 * (or to have no gesture), have no delay, no items, 
	 * etc.
	 * 
	 * @return 
	 *  	A copy of this item.
	 */
	public Item copy()
	{	Item result = new Item();
	
		// gesture pack
		result.setGesturePack(gesturePack);
		
		// anime
		AnimeManager animeManager = this.animeManager.copy(result);
		result.setAnimeManager(animeManager);
		
		// trajectory
		TrajectoryManager trajectoryManager = this.trajectoryManager.copy(result);
		result.setTrajectoryManager(trajectoryManager);
		
		// bombset
		BombsetManager bombsetManager = this.bombsetManager.copy(result);
		result.setBombsetManager(bombsetManager);
		
		// explosion
		ExplosionManager explosionManager = this.explosionManager.copy(result);
		result.setExplosionManager(explosionManager);
		
		// modulations
		ModulationManager modulationManager = this.modulationManager.copy(result);
		result.setModulationManager(modulationManager);
		
		// item
		ItemManager itemManager = this.itemManager.copy(result);
		result.setItemManager(itemManager);
		
		// ability
		AbilityManager abilityManager = this.abilityManager.copy(result);
		result.setAbilityManager(abilityManager);
		
		// delay
		DelayManager delayManager = this.delayManager.copy(result);
		result.setDelayManager(delayManager);
		
		// control
		ControlManager controlManager = this.controlManager.copy(result);
		result.setControlManager(controlManager);
		
		// item abilities
		result.originalItemAbilities = originalItemAbilities;
		for(AbstractAbility ability: itemAbilities)
			result.addItemAbility(ability);
		
		// event
		EventManager eventManager = this.eventManager.copy(result);
		result.setEventManager(eventManager);
		
		// misc
		result.setItemName(itemName);
		result.initSprite(tile);

		return result;		
	}
}

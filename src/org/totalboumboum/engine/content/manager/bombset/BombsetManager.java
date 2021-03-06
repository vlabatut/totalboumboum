package org.totalboumboum.engine.content.manager.bombset;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

import java.util.LinkedList;

import org.totalboumboum.engine.container.bombset.Bombset;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.action.drop.SpecificDrop;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;

/**
 * This class is used to handle the bombs dropped by a sprite,
 * generally a hero type. It is associated to a set of bombs,
 * called Bombset. It is controlled by various bomb-related
 * abilities, such as bomb range or bomb number. 
 * 
 * @author Vincent Labatut
 */
public abstract class BombsetManager
{	
	/**
	 * Creates a new bombset manager for
	 * the specified sprite.
	 * 
	 * @param sprite
	 * 		Sprite possessing the new bombset manager.
	 */
	public BombsetManager(Sprite sprite)
	{	this.sprite = sprite;
		bombset = null;
		droppedBombs = new LinkedList<Bomb>();
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMBSET			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Bombset handled by this bombset manager */
	protected Bombset bombset;
	
	/**
	 * Changes the bombset handled by this bombset manager.
	 * 
	 * @param bombset
	 * 		New bombset for this bombset manager.
	 */
	public void setBombset(Bombset bombset)
	{	this.bombset = bombset;		
	}
	
	/**
	 * Returns the bombset handled by this bombset manager.
	 * 
	 * @return
	 * 		Bombset handled by this bombset manager.
	 */
	public Bombset getBombset()
	{	return bombset;
	}

	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Sprite possessing this bombset manager */
	protected Sprite sprite;
	
	/**
	 * Returns the sprite possessing this bombset manager.
	 * 
	 * @return
	 * 		Sprite possessing this bombset manager.
	 */
	public Sprite getSprite()
	{	return sprite;
	}
	
	/**
	 * Processes the current bomb range
	 * for this bombset manager.
	 * 
	 * @return
	 * 		Range of the bombs generated by this bombset manager.
	 */
	public int processBombRange()
	{	StateAbility ability = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_RANGE);
		int result = (int)ability.getStrength();
		ability = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_RANGE_MAX);
		if(ability.isActive())
		{	int limit = (int)ability.getStrength();
			result = Math.min(result, limit);
			result = Math.max(result, 0);
		}
//System.out.println(sprite.getName()+":"+sprite.getColor()+"flameRange: "+result);
		return result;
	}
	
	/**
	 * Processes the number of simultaneous bombs
	 * this bombset manager can handle.
	 * 
	 * @return
	 * 		Number of simultaneous bombs for this bombset manager.
	 */
	public int processBombNumberMax()
	{	StateAbility ability = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_NUMBER);
		int result = (int)ability.getStrength();
		ability = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_RANGE_MAX);
		if(ability.isActive())
		{	int limit = (int)ability.getStrength();
			result = Math.min(result, limit);
			result = Math.max(result, 0);
		}
//System.out.println("droppedBombLimit: "+droppedBombLimit);	
//System.out.println("droppedBombs.size(): "+droppedBombs.size());	
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// DROP				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List of current bombs dropped by this bombset manager */ 
	protected LinkedList<Bomb> droppedBombs;
	
	/**
	 * Makes a new bomb.
	 * 
	 * @return
	 * 		The generated bomb.
	 */
	public abstract Bomb makeBomb();
	
	/**
	 * Drops a bomb.
	 * 
	 * @param dropAction
	 * 		The action containing the bomb to be dropped.
	 */
	public abstract void dropBomb(SpecificDrop dropAction);

	/**
	 * Returns the list of current bombs dropped
	 * through this bombset manager.
	 * 
	 * @return
	 * 		List of dropped bombs.
	 */
	public LinkedList<Bomb> getDroppedBombs()
	{	return droppedBombs;
	}

	/////////////////////////////////////////////////////////////////
	// TRIGGER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Triggers a bomb (generally, the oldest dropped).
	 */
	public abstract void triggerBomb();
	
	/**
	 * Triggers all bombs.
	 */
	public abstract void triggerAllBombs();
	
	/////////////////////////////////////////////////////////////////
	// UPDATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Updates the state of this bombset manager.
	 */
	public abstract void update();

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Makes a copy of this bombset manager for
	 * the specified sprite.
	 * 
	 * @param sprite
	 * 		Sprite to possess the new bombset manager.
	 * @return
	 * 		A copy of this bombset manager.
	 */
	public abstract BombsetManager copy(Sprite sprite);
}

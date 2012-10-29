package org.totalboumboum.engine.content.feature.gesture;

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

import java.io.Serializable;

/**
 * 
 * @author Vincent Labatut
 *
 */
public enum GestureName implements Serializable
{	
	/** the sprite is suddenly appearing in the zone */
	APPEARING,
	
	/** the sprite is bouncing on a wall */
	BOUNCING,
	
	/** the sprite is burning (different from exploding?) */
	BURNING,

	/** the sprite is carrying something */
	CARRYING,
	
	/** the sprite is putting fire to something else */
//	CONSUMING,
	
	/** the sprite is expressing its sadness (hero losing a round) */
	CRYING,
	
	/** the sprite is suddenly disappearing from the zone */
	DISAPPEARING,
	
	/** the sprite is not ingame anymore (automatically generated gesture) */
	ENDED,
	
	/** the sprite is entering the round (kind of like a first APPEARING) (automatically generated gesture) */
	ENTERING,
	
	/** the sprite is expressing its joy (hero winning a round) */
	EXULTING,
	
	/** the sprite is falling from the skies (just before landing) */
	FALLING,
	
	/** the sprite is being held by another sprite */
//	HELD,
	
	/** the sprite is in the zone, but not visible (wall before a spawn) */
	HIDING,
	
	/** the sprite is holding another sprite */
	HOLDING,
	
	/** the sprite is going up on its own */
	JUMPING,
	
	/** the sprite is finishing an aerial move (on its own or not) */
	LANDING,
	
	/** the sprite is not performing any action (hasn't join the zone yet) (automatically generated gesture) */
	NONE,
	
	/** the sprite has been slightly pushed, but not enough to be moved yet */ 
	OSCILLATING,
	
	/** the sprite is both oscillating and failing to explode (for a bomb) */
	OSCILLATING_FAILING,
	
	/** the sprite is being picked by another sprite */
//	PICKED,
	
	/** the sprite is picking another sprite */
	PICKING,
	
	/** the sprite has finished ENTERING the round and is waiting for it to actually start (automatically generated gesture) */
	PREPARED,
	
	/** the sprite is being sent in the air by another sprite */
	PUNCHED,
	
	/** the sprite is punching another sprite to send it in the air */
	PUNCHING,
	
	/** the sprite is pushing another sprite to make it slide on the ground */
	PUSHING,
	
	/** the sprite (an item) is released on the level (by the hero which possessed it until now) */
	RELEASED,
	
	/** the sprite is sliding on the ground */
	SLIDING,
	
	/** the sprite is both sliding and failing to explore (for a bomb) */
	SLIDING_FAILING,
	
	/** the sprite is experiencing difficulties to walk/stand */
	STAGGERING,
	
	/** the sprite is just standing, doing nothing special */
	STANDING,
	
	/** the sprite is both standing and failing to explode (for a bomb) */
	STANDING_FAILING,
	
	/** the sprite is throwing a sprite is was previously carrying or holding */
	THROWING,
	
	/** the sprite is being thrown by another sprite (which was previously carrying or holding it) */
//	THROWN,
	
	/** the sprite is performing is favorite activity while waiting for the player to control it */
	WAITING,
	
	/** the sprite is moving on the ground on its own */
	WALKING;
	
	//TODO synchroniser avec les AIStateName
}

package org.totalboumboum.engine.content.feature.ability;

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
 * names for all predefined StateAbilities.
 * must be Strings, since it's possible to define custom ones in XML files
 * (hence no enum type)
 * 
 * @author Vincent Labatut
 *
 */
public class StateAbilityName implements Serializable
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// GENERAL ABILITIES		/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** higher sprite, should be drawn above (i.e. after) the others */
	public static final String SPRITE_ABOVE = "SPRITE_ABOVE";
	
	/** gesture duration for when the sprite enter a round */
	public static final String SPRITE_ENTRY_DURATION = "SPRITE_ENTRY_DURATION";
	
	/** completely flat sprite */
	public static final String SPRITE_FLAT = "SPRITE_FLAT";
	
	/** sprite temporarily invisible */
	public static final String SPRITE_INVISIBLE = "SPRITE_INVISIBLE";
	
	/** when an obstacle is collided, it helps avoiding it (dedicated to heroes, mainly) */
	public static final String SPRITE_MOVE_ASSISTANCE = "SPRITE_MOVE_ASSISTANCE";
	
	/** cross (some?) bombs */
	public static final String SPRITE_TRAVERSE_BOMB = "SPRITE_TRAVERSE_BOMB";
	
	/** cross (some?) items */
	public static final String SPRITE_TRAVERSE_ITEM = "SPRITE_TRAVERSE_ITEM";
	
	/** cross (some?) walls */
	public static final String SPRITE_TRAVERSE_WALL = "SPRITE_TRAVERSE_WALL";
	
	/** cross (some?) players (actually more about block crushing heroes, sudden-death-style) */
	public static final String SPRITE_TRAVERSE_HERO = "SPRITE_TRAVERSE_HERO";
	
	/** the sprite is twinkling, the strenght corresponds to the twinkling color in RGB integer (0=no twinkling, <0=transparent twinkling) */
	public static final String SPRITE_TWINKLE = "SPRITE_TWINKLE";	
	/** time the sprite is colored (or hidden) when twinkling */
	public static final String SPRITE_TWINKLE_COLOR = "SPRITE_TWINKLE_COLOR";	
	/** time the sprite is normally shown when twinkling */
	public static final String SPRITE_TWINKLE_NORMAL = "SPRITE_TWINKLE_NORMAL";
	
	/////////////////////////////////////////////////////////////////
	// BLOCKS ABILITIES			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** hability re-spawn after the block has been destroyed */
	public static final String BLOCK_SPAWN = "BLOCK_SPAWN";
	
	/////////////////////////////////////////////////////////////////
	// BOMBS ABILITIES			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** latency between when a bomb is touched by a flame and when it actually explodes */
	public static final String BOMB_EXPLOSION_LATENCY = "BOMB_EXPLOSION_LATENCY";
	
	/** maximal failure duration */
	public static final String BOMB_FAILURE_MAXDURATION = "BOMB_FAILURE_MAXDURATION";
	
	/** minimal failure duration (for time bombs only?) */
	public static final String BOMB_FAILURE_MINDURATION = "BOMB_FAILURE_MINDURATION";
	
	/** probability for a bomb to fail to explose when it's triggered */
	public static final String BOMB_FAILURE_PROBABILITY = "BOMB_FAILURE_PROBABILITY";
	
	/** when the owner of this bomb is burnt, the bomb also explodes */
	public static final String BOMB_ON_DEATH_EXPLODE = "BOMB_ON_DEATH_EXPLODE";

	/** delay a hero has to push the bomb for before it actually starts sliding */
	public static final String BOMB_PUSH_LATENCY = "BOMB_PUSH_LATENCY";
	
	/** the bomb explodes if something (?) collides it */
	public static final String BOMB_TRIGGER_COLLISION = "BOMB_TRIGGER_COLLISION";
	
	/** the bomb exploses if it's touched by a flame */
	public static final String BOMB_TRIGGER_COMBUSTION = "BOMB_TRIGGER_COMBUSTION";
	
	/** remote controled bomb */
	public static final String BOMB_TRIGGER_CONTROL = "BOMB_TRIGGER_CONTROL";
	
	/** delay before explosion for a time bomb */
	public static final String BOMB_TRIGGER_TIMER = "BOMB_TRIGGER_TIMER";
	
	/////////////////////////////////////////////////////////////////
	// HEROES ABILITIES			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** bomb constipation: the hero can't drop any bombs */
	public static final String HERO_BOMB_CONSTIPATION = "HERO_BOMB_CONSTIPATION";
	
	/** bomb constipation: the hero can't help dropping bombs */
	public static final String HERO_BOMB_DIARRHEA = "HERO_BOMB_DIARRHEA";
	
	/** minimal delay between two dropped bombs */
	public static final String HERO_BOMB_DROP_LATENCY = "HERO_BOMB_DROP_LATENCY";

	/** number of bombs one can simultaneously drop */
	public static final String HERO_BOMB_NUMBER = "HERO_BOMB_NUMBER";
	/** maximal limit for the number of bombs one can simultaneously drop */
	public static final String HERO_BOMB_NUMBER_MAX = "HERO_BOMB_NUMBER_MAX";
	
	/** length of the flames produced by a bomb */
	public static final String HERO_BOMB_RANGE = "HERO_BOMB_RANGE";
	/** maximal limit for the length of the flames produced by a bomb */
	public static final String HERO_BOMB_RANGE_MAX = "HERO_BOMB_RANGE_MAX";
	
	/** modifier for the time-bomb timer */
	public static final String HERO_BOMB_TIMER_COEFFICIENT = "HERO_BOMB_TIMER_COEFFICIENT";

	/** gesture duration when the hero cries or exults at the end of a round */
	public static final String HERO_CELEBRATION_DURATION = "HERO_CELEBRATION_DURATION";
	
	/** invert the controls, used for disease items */
	public static final String HERO_CONTROL_INVERSION = "HERO_CONTROL_INVERSION";
	
	/** number of times the hero can be bombed before being eliminated */
	public static final String HERO_LIFE = "HERO_LIFE";

	/** gesture duration when a hero punches (a bomb for instance) */
	public static final String HERO_PUNCH_DURATION = "HERO_PUNCH_DURATION";
	
	/** delay before a hero is reborn after he has been bombed */
	public static final String HERO_REBIRTH_DELAY = "HERO_REBIRTH_DELAY";
	
	/** protection duration after a hero has just reborn */
	public static final String HERO_REBIRTH_DURATION = "HERO_REBIRTH_DURATION";
	
	/** delay before an idle hero switch to its waiting gesture */
	public static final String HERO_WAIT_DURATION = "HERO_WAIT_DURATION";

	/** the hero is protected from fire */
	public static final String HERO_FIRE_PROTECTION = "HERO_FIRE_PROTECTION";

	/** change the hero speed  */
	public static final String HERO_WALK_SPEED_MODULATION = "HERO_WALK_SPEED_MODULATION";	
	/** coefficient associated to a positive speed  */
	public static final String HERO_WALK_SPEED_P = "HERO_WALK_SPEED_P";
	/** coefficient associated to a negative speed  */
	public static final String HERO_WALK_SPEED_M = "HERO_WALK_SPEED_M";
		
	public static String getHeroWalkSpeed(int speed)
	{	String result = null;
		if(speed<0)
			result = HERO_WALK_SPEED_M;
		else if(speed>0)
			result = HERO_WALK_SPEED_P;
		if(result!=null)
			result = result+Math.abs(speed);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// ITEM ABILITIES			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indicates the item cancels all previous items from the specified group (using strength) */
	public static final String ITEM_CANCEL_GROUP = "ITEM_CANCEL_GROUP";
	
	/** type of contagion: 0=none 1=share as is 2=share with reinit 3=transmission as is 4=transmission with reinit*/
	public static final String ITEM_CONTAGION_MODE = "ITEM_CONTAGION_MODE";
	public static final int ITEM_CONTAGION_NONE = 0;
	public static final int ITEM_CONTAGION_SHARE_ONLY = 1;
	public static final int ITEM_CONTAGION_SHARE_REINIT = 2;
	public static final int ITEM_CONTAGION_GIVE_ONLY = 3;
	public static final int ITEM_CONTAGION_GIVE_REINIT = 4;

	/** used for the crown mode (just counting points) */
	public static final String ITEM_CROWN = "ITEM_CROWN";

	/** indicates if the item is a part of a group (whose number corresponds to the strength) */
	public static final String ITEM_GROUP = "ITEM_GROUP";
	
	/** used for the crown mode (just counting points) */
	public static final String ITEM_INDESTRUCTIBLE = "ITEM_INDESTRUCTIBLE";

	/** indicates what to do if the holder dies: 0=disapear 1=release as is 2=release with reinit */
	public static final String ITEM_ON_DEATH_ACTION = "ITEM_ON_DEATH_ACTION";
	public static final int ITEM_ON_DEATH_DISAPEAR = 0;
	public static final int ITEM_ON_DEATH_RELEASE_ONLY = 1;
	public static final int ITEM_ON_DEATH_RELEASE_REINIT = 2;
	
	/** indicates what to do if the item is canceled: 0=disapear 1=release as is 2=release with reinit */
	public static final String ITEM_ON_CANCEL_ACTION = "ITEM_ON_CANCEL_ACTION";
	public static final int ITEM_ON_CANCEL_DISAPEAR = 0;
	public static final int ITEM_ON_CANCEL_RELEASE_ONLY = 1;
	public static final int ITEM_ON_CANCEL_RELEASE_REINIT = 2;
}

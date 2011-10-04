package fr.free.totalboumboum.engine.content.feature.ability;

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

/**
 * names for all predefined StateAbilities.
 * must be Strings, since it's possible to define custom ones in XML files
 * (hence no enum type)
 *
 */
public class StateAbilityName
{
	/////////////////////////////////////////////////////////////////
	// GENERAL ABILITIES		/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** completely flat sprite */
	public static final String SPRITE_FLAT = "SPRITE_FLAT";
	
	/** higher sprite, should be drawn above (i.e. after) the others */
	public static final String SPRITE_ABOVE = "SPRITE_ABOVE";
	
	/** when an obstacle is collided, it helps avoiding it (dedicated to heroes, mainly) */
	public static final String SPRITE_MOVE_ASSISTANCE = "SPRITE_MOVE_ASSISTANCE";
	
	/** cross (some?) bombs */
	public static final String SPRITE_TRAVERSE_BOMB = "SPRITE_TRAVERSE_BOMB";
	
	/** cross (some?) items */
	public static final String SPRITE_TRAVERSE_ITEM = "SPRITE_TRAVERSE_ITEM";
	
	/** cross (some?) walls */
	public static final String SPRITE_TRAVERSE_WALL = "SPRITE_TRAVERSE_WALL";
	
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
	
	/** number of bombs simultaneously dropped */
	public static final String BOMB_NUMBER = "BOMB_NUMBER";
	
	/** length of the flames produced by a bomb */
	public static final String BOMB_RANGE = "BOMB_RANGE";
	
	/////////////////////////////////////////////////////////////////
	// HEROES ABILITIES			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** gesture duration when the hero cries or exults at the end of a round */
	public static final String HERO_CELEBRATION_DURATION = "HERO_CELEBRATION_DURATION";
	
	/** gesture duration when the heroes enter a round */
	public static final String HERO_ENTRY_DURATION = "HERO_ENTRY_DURATION";
	
	/** gesture duration when a hero punches (a bomb for instance) */
	public static final String HERO_PUNCH_DURATION = "HERO_PUNCH_DURATION";
	
	/** delay before an idle hero switch to its waiting gesture */
	public static final String HERO_WAIT_DURATION = "HERO_WAIT_DURATION";

}
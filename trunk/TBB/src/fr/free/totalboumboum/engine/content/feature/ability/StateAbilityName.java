package fr.free.totalboumboum.engine.content.feature.ability;

public enum StateAbilityName
{
	/////////////////////////////////////////////////////////////////
	// GENERAL ABILITIES		/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** completely flat sprite */
	SPRITE_FLAT,
	
	/** higher sprite, should be drawn above (i.e. after) the others */
	SPRITE_ABOVE,
	
	/** when an obstacle is collided, it helps avoiding it (dedicated to heroes, mainly) */
	SPRITE_MOVE_ASSISTANCE,
	
	/** cross (some?) bombs */
	SPRITE_TRAVERSE_BOMB,
	
	/** cross (some?) items */
	SPRITE_TRAVERSE_ITEM,
	
	/** cross (some?) walls */
	SPRITE_TRAVERSE_WALL,
	
	/////////////////////////////////////////////////////////////////
	// BLOCKS ABILITIES			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** hability re-spawn after the block has been destroyed */
	BLOCK_SPAWN,
	
	/////////////////////////////////////////////////////////////////
	// BOMBS ABILITIES			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** latency between when a bomb is touched by a flame and when it actually explodes */
	BOMB_EXPLOSION_LATENCY,
	
	/** maximal failure duration */
	BOMB_FAILURE_MAXDURATION,
	
	/** minimal failure duration (for time bombs only?) */
	BOMB_FAILURE_MINDURATION,
	
	/** probability for a bomb to fail to explose when it's triggered */
	BOMB_FAILURE_PROBABILITY,
	
	/** delay a hero has to push the bomb for before it actually starts sliding */
	BOMB_PUSH_LATENCY,
	
	/** the bomb explodes if something (?) collides it */
	BOMB_TRIGGER_COLLISION,
	
	/** the bomb exploses if it's touched by a flame */
	BOMB_TRIGGER_COMBUSTION,
	
	/** remote controled bomb */
	BOMB_TRIGGER_CONTROL,
	
	/** delay before explosion for a time bomb */
	BOMB_TRIGGER_TIMER,
	
	/** number of bombs simultaneously dropped */
	BOMB_NUMBER,
	
	/** length of the flames produced by a bomb */
	BOMB_RANGE,
	
	/////////////////////////////////////////////////////////////////
	// HEROES ABILITIES			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** gesture duration when the hero cries or exults at the end of a round */
	HERO_CELEBRATION_DURATION,
	
	/** gesture duration when the heroes enter a round */
	HERO_ENTRY_DURATION,
	
	/** gesture duration when a hero punches (a bomb for instance) */
	HERO_PUNCH_DURATION,
	
	/** delay before an idle hero switch to its waiting gesture */
	HERO_WAIT_DURATION

}

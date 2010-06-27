package org.totalboumboum.engine.content.feature.event;

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

import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class EngineEvent extends AbstractEvent
{	
    /////////////////////////////////////////////////////////////////
	// CONSTANTS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** a sprite animation has just finished */
	public static final String ANIME_OVER = "ANIME_OVER";
	/** a sprite programmed delay has expired */
	public static final String DELAY_OVER = "DELAY_OVER";
	/** a sprite trajectory has just finished */
	public static final String TRAJECTORY_OVER = "TRAJECTORY_OVER";

	/** a sprite has just been collided */
	public static final String COLLIDED_ON = "COLLIDED_ON"; // le sprite qui le reçoit est percuté par la source
	/** a sprite is no longer collided */
	public static final String COLLIDED_OFF = "COLLIDED_OFF";
	/** a sprite just started colliding another one */
	public static final String COLLIDING_ON = "COLLIDING_ON"; // le sprite qui le reçoit est en train de percuter la cible
	/** a sprite stopped colliding another one */
	public static final String COLLIDING_OFF = "COLLIDING_OFF";

	/** two sprites started intersecting each-other */
	public static final String INTERSECTION_ON = "INTERSECTION_ON"; // symétrique pour les deux sprites concernés
	/** two sprites stopped intersecting each-other */
	public static final String INTERSECTION_OFF = "INTERSECTION_OFF";
	
	/** a sprite is being flown over by another one */
	public static final String OVERFLOWN_ON = "OVERFLOWN_ON"; // le sprite qui le reçoit est survolé par la source
	/** a sprite is not flown over anymore */
	public static final String OVERFLOWN_OFF = "OVERFLOWN_OFF";
	/** a sprite just started flying over another one */
	public static final String OVERFLYING_ON = "OVERFLYING_ON"; // le sprite qui le reçoit survole la cible
	/** a sprite stopped flying over another one */
	public static final String OVERFLYING_OFF = "OVERFLYING_OFF";

	/** a sprite just entered a tile through the ground */
	public static final String TILE_LOW_ENTER = "TILE_LOW_ENTER";
	/** a sprite just entered a tile through the air */
	public static final String TILE_HIGH_ENTER = "TILE_HIGH_ENTER";
	/** a sprite just exit a tile through the ground */
	public static final String TILE_LOW_EXIT = "TILE_LOW_EXIT";
	/** a sprite just exit a tile through the air */
	public static final String TILE_HIGH_EXIT = "TILE_HIGH_EXIT";
	/** a sprite just landed on the ground */
	public static final String TOUCH_GROUND = "TOUCH_GROUND";
	/** a sprite just left the the ground */
	public static final String LEAVE_GROUND = "LEAVE_GROUND";

	/** a player is signaled he lost */
	public static final String CELEBRATION_DEFEAT = "CELEBRATION_DEFEAT";
	/** a player is signaled he won */
	public static final String CELEBRATION_VICTORY = "CELEBRATION_VICTORY";

	/** the engine signals a sprite it must enter the level (i.e. just appear in the level) */
	public static final String ROUND_ENTER = "ROUND_ENTER";
	/** the engine signals a sprite it must start enteracting (i.e. the actual game is starting) */
	public static final String ROUND_START = "ROUND_START";

	/** the engine signals a sprite it must hide */
//	public static final String HIDE_DOWN = "HIDE_DOWN";
	/** the engine signals a sprite it must appear */
//	public static final String SHOW_UP = "SHOW_UP";
	
	/** the engine signals the sprite he as to finish itself */
	public static final String END_SPRITE = "END_SPRITE";
	
    /////////////////////////////////////////////////////////////////
	// CONSTRUCTORS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public EngineEvent(String name)
	{	this.name = name;	
		source = null;
		target = null;
		direction = null;
		stringParameter = null;
	}
	
	public EngineEvent(String name, String stringParameter)
	{	this.name = name;	
		source = null;
		target = null;
		direction = null;
		this.stringParameter = stringParameter;
	}
	
	public EngineEvent(String name, Sprite source, Sprite target, Direction direction)
	{	this.name = name;
		this.source = source;
		this.target = target;
		this.direction = direction;
		stringParameter = null;
	}

    /////////////////////////////////////////////////////////////////
	// NAME					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String name;

	public String getName()
	{	return name;	
	}
		
    /////////////////////////////////////////////////////////////////
	// STRING				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String toString()
	{	return name+"("+source+">"+target+";"+direction+")";	
	}

    /////////////////////////////////////////////////////////////////
	// SOURCE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Sprite source;

	public Sprite getSource()
	{	return source;
	}
	
	public void setSource(Sprite source)
	{	this.source = source;
	}

    /////////////////////////////////////////////////////////////////
	// TARGET				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Sprite target;
	public Sprite getTarget()
	{	return target;
	}
	
	public void setTarget(Sprite target)
	{	this.target = target;
	}
	
    /////////////////////////////////////////////////////////////////
	// DIRECTION			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Direction direction;
	
	public Direction getDirection()
	{	return direction;
	}
	
	public void setDirection(Direction direction)
	{	this.direction = direction;	
	}

    /////////////////////////////////////////////////////////////////
	// STRING PARAMETER		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String stringParameter;

	public String getStringParameter()
	{	return stringParameter;
	}
	
	public void setStringParameter(String parameter)
	{	stringParameter = parameter;	
	}
}

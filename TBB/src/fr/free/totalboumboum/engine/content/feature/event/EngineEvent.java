package fr.free.totalboumboum.engine.content.feature.event;

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

import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.sprite.getModulationStateAbilities;

public class EngineEvent extends AbstractEvent
{	public static final String ANIME_OVER = "ANIME_OVER";
	public static final String DELAY_OVER = "DELAY_OVER";
	public static final String TRAJECTORY_OVER = "TRAJECTORY_OVER";

	public static final String COLLIDED_ON = "COLLIDED_ON"; // le sprite qui le reçoit est percuté par la source
	public static final String COLLIDED_OFF = "COLLIDED_OFF";
	public static final String COLLIDING_ON = "COLLIDING_ON"; // le sprite qui le reçoit est en train de percuter la cible
	public static final String COLLIDING_OFF = "COLLIDING_OFF";

	public static final String INTERSECTION_ON = "INTERSECTION_ON"; // symétrique pour les deux sprites concernés
	public static final String INTERSECTION_OFF = "INTERSECTION_OFF";
	
	public static final String OVERFLOWN_ON = "OVERFLOWN_ON"; // le sprite qui le reçoit est survolé par la source
	public static final String OVERFLOWN_OFF = "OVERFLOWN_OFF";
	public static final String OVERFLYING_ON = "OVERFLYING_ON"; // le sprite qui le reçoit survole la cible
	public static final String OVERFLYING_OFF = "OVERFLYING_OFF";

	public static final String TILE_LOWENTER = "TILE_LOWENTER";
	public static final String TILE_HIGHENTER = "TILE_HIGHENTER";
	public static final String TOUCH_GROUND = "TOUCH_GROUND";

	public static final String VICTORY = "VICTORY";
	public static final String DEFEAT = "DEFEAT";

	private String name;
	private getModulationStateAbilities source;
	private getModulationStateAbilities target;
	private Direction direction;
	private String stringParameter;
	
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
	
	public EngineEvent(String name, getModulationStateAbilities source, getModulationStateAbilities target, Direction direction)
	{	this.name = name;
		this.source = source;
		this.target = target;
		this.direction = direction;
		stringParameter = null;
	}

	public String getName()
	{	return name;	
	}
		
	public String toString()
	{	return name+"("+source+">"+target+";"+direction+")";	
	}

	public getModulationStateAbilities getSource()
	{	return source;
	}
	public void setSource(getModulationStateAbilities source)
	{	this.source = source;
	}

	public getModulationStateAbilities getTarget()
	{	return target;
	}
	public void setTarget(getModulationStateAbilities target)
	{	this.target = target;
	}
	
	public Direction getDirection()
	{	return direction;
	}	

	public String getStringParameter()
	{	return stringParameter;
	}	

	public void finish()
	{	if(!finished)
		{	super.finish();
			// misc
			direction = null;
			source = null;
			target = null;
			stringParameter = null;			
		}
	}
}

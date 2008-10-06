package fr.free.totalboumboum.ai.adapter200809;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public class AiState 
{
	private Sprite sprite;
	
	AiState(Sprite sprite)
	{	this.sprite = sprite;
	}
	
	void update()
	{	// direction
		this.direction = sprite.getActualDirection();
		// name
		String gesture = sprite.getCurrentGesture();
		name = AiStateName.makeNameFromGesture(gesture);		
	}
	
	void finish()
	{	sprite = null;
		direction = null;
		name = null;		
	}
	
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof AiState)
		{	
//			AiState s = (AiState)o;	
//			result = name==s.name && direction==s.direction;
			result = this==o;
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** nom associé à l'état */
	private AiStateName name;
	
	/**
	 * renvoie le nom associé à l'état
	 */
	public AiStateName getName()
	{	return name;
	}
	
	/////////////////////////////////////////////////////////////////
	// DIRECTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** direction associée à l'état (peut être NONE, c'est à dire : l'état n'est pas orienté) */
	private Direction direction;
	
	/**
	 * renvoie la direction associée à l'état,
	 * qui peut être NONE, c'est à dire : l'état n'est pas orienté
	 */
	public Direction getDirection()
	{	return direction;
	}
}

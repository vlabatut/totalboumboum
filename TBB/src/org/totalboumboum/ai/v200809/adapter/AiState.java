package org.totalboumboum.ai.v200809.adapter;

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
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * Décrit un état dans lequel un sprite peut se trouver, c'est
 * à dire essentiellement l'action que le sprite réalise ou qu'il subit.
 * Cet état est décrit par le nom de cette action, et éventuellement la
 * direction dans laquelle elle est effectuée (pour les actions orientées
 * comme le déplacement, par exemple).
 * 
 * @author Vincent Labatut
 *
 */

public class AiState 
{
	/** sprite dont l'état est représenté */
	private Sprite sprite;
	
	/**
	 * construit un objet représentant l'état du sprite passé en paramètre
	 * 
	 * @param sprite	sprite dont on veut représenter l'état
	 */
	AiState(Sprite sprite)
	{	this.sprite = sprite;
	}
	
	/**
	 * met à jour cet état en fonction de l'évolution du sprite de référence
	 */
	void update()
	{	// direction
		this.direction = sprite.getActualDirection();
		// name
		GestureName gesture = sprite.getCurrentGesture().getName();
		name = AiStateName.makeNameFromGesture(gesture);		
	}
	
	/**
	 * termine cet objet et libère les ressources occupées
	 */
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

	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append("(");
		result.append("name.: "+name);
		result.append("dir.: "+direction);
		result.append(")");
		return result.toString();
	}

	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** nom associée à l'état */
	private AiStateName name;
	
	
	/**
	 * renvoie le nom associé à l'état
	 * 
	 * @return	nom associé à l'état
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
	 * 
	 * @return	direction associée à l'état
	 */
	public Direction getDirection()
	{	return direction;
	}
}

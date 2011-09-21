package org.totalboumboum.ai.v201112.adapter.data.internal;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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

import org.totalboumboum.ai.v201112.adapter.data.AiState;
import org.totalboumboum.ai.v201112.adapter.data.AiStateName;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * décrit un état dans lequel un sprite peut se trouver, c'est
 * à dire essentiellement l'action que le sprite réalise ou qu'il subit.
 * Cet état est décrit par le nom de cette action, et éventuellement la
 * direction dans laquelle elle est effectuée (pour les actions orientées
 * comme le déplacement, par exemple).
 * 
 * @author Vincent Labatut
 *
 */
final class AiDataState implements AiState
{
	/** sprite dont l'état est représenté */
	private Sprite sprite;
	
	/**
	 * construit un objet représentant l'état du sprite passé en paramètre
	 * 
	 * @param sprite
	 * 		sprite dont on veut représenter l'état
	 */
	protected AiDataState(Sprite sprite)
	{	this.sprite = sprite;
//		direction = Direction.NONE; //necessary ?
//		name = AiStateName.STANDING;
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * met à jour cet état en fonction de l'évolution du sprite de référence
	 * 
	 * @param elapsedTime
	 * 		temps écoulé depuis la dernière mise à jour
	 */
	protected void update(long elapsedTime)
	{	// direction
		this.direction = sprite.getActualDirection();
		
		// name
		GestureName gesture = sprite.getCurrentGesture().getName();
		AiStateName oldName = name;
		name = AiStateName.makeNameFromGesture(gesture);	
		
		// time
		if(oldName!=name)
			time = elapsedTime;
		else
			time = time + elapsedTime;
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** nom associée à l'état */
	private AiStateName name;
	
	@Override
	public AiStateName getName()
	{	return name;
	}
	
	/**
	 * indique que ce sprite a disparu du jeu
	 */
	protected void setEnded()
	{	name = AiStateName.ENDED;		
	}
	
	/////////////////////////////////////////////////////////////////
	// DIRECTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** direction associée à l'état (peut être NONE, c'est à dire : l'état n'est pas orienté) */
	private Direction direction;
	
	@Override
	public Direction getDirection()
	{	return direction;
	}

	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** compte combien de temps le sprite a passé dans l'état courant */
	private long time = 0;
	
	@Override
	public long getTime()
	{	return time;
	}
	
	/////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof AiDataState)
		{	
//			AiState s = (AiState)o;	
//			result = name==s.name && direction==s.direction;
			result = this==o;
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append("(");
		result.append("name:"+name);
		result.append(" dir:"+direction);
		result.append(")");
		return result.toString();
	}

	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * termine cet objet et lib�re les ressources occupées
	 */
	protected void finish()
	{	sprite = null;
		direction = null;
		name = null;		
	}
}

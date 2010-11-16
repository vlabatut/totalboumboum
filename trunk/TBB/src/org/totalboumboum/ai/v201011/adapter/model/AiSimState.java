package org.totalboumboum.ai.v201011.adapter.model;

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

import org.totalboumboum.ai.v201011.adapter.data.AiSprite;
import org.totalboumboum.ai.v201011.adapter.data.AiState;
import org.totalboumboum.ai.v201011.adapter.data.AiStateName;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Décrit un état simulé dans lequel un sprite peut se trouver, c'est
 * à dire essentiellement l'action que le sprite réalise ou qu'il subit.
 * Cet état est décrit par le nom de cette action, et éventuellement la
 * direction dans laquelle elle est effectuée (pour les actions orientées
 * comme le déplacement, par exemple).
 * 
 * @author Vincent Labatut
 *
 */
final class AiSimState  implements AiState
{
	/**
	 * construit un objet simulant l'état d'un sprite qui vient d'être créé,
	 * i.e. qui ne fait rien (STANDING) dans une direction neutre (NONE).
	 */
	protected AiSimState()
	{	name = AiStateName.STANDING;
		direction = Direction.NONE;
		time = 0;
	}

	/**
	 * construit un objet simulant l'état d'un sprite qui vient d'être créé,
	 * en utilisant les valeurs passées en paramètres
	 * 
	 * @param name
	 * 		nom de l'état
	 * @param direction	
	 * 		direction de l'action
	 * @param time	
	 * 		durée courante de l'état
	 */
	protected AiSimState(AiStateName name, Direction direction, long time)
	{	this.name = name;
		this.direction = direction;
		this.time = time;
	}

	/**
	 * construit un objet simulant l'état du sprite passé en paramètre
	 * 
	 * @param sprite	
	 * 		sprite dont on veut simuler l'état
	 */
	protected AiSimState(AiSprite sprite)
	{	AiState state = sprite.getState();
		name = state.getName();
		direction = state.getDirection();
		time = state.getTime();
	}

	/**
	 * construit un objet simulant l'état passé en paramètre
	 * 
	 * @param state
	 * 		état à reproduire
	 */
	protected AiSimState(AiState state)
	{	name = state.getName();
		direction = state.getDirection();
		time = state.getTime();
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
	
	/**
	 * permet de modifier le temps passé dans cet état.
	 * méthode utilisée exclusivement lors des simulations
	 * 
	 * @param time	
	 * 		le nouveau temps passé dans cet état
	 */
	protected void setTime(long time)
	{	this.time = time;
	}
	
	/////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof AiSimState)
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
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public AiSimState copy()
	{	AiSimState result = new AiSimState();
		result.direction = direction;
		result.name = name;
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * termine cet objet et libère les ressources occupées
	 */
	protected void finish()
	{	direction = null;
		name = null;		
	}
}

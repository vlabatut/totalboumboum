package org.totalboumboum.ai.v201011.adapter.model;

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

import org.totalboumboum.ai.v201011.adapter.data.AiSprite;
import org.totalboumboum.ai.v201011.adapter.data.AiState;
import org.totalboumboum.ai.v201011.adapter.data.AiStateName;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * D�crit un état simul� dans lequel un sprite peut se trouver, c'est
 * à dire essentiellement l'action que le sprite r�alise ou qu'il subit.
 * Cet état est d�crit par le nom de cette action, et �ventuellement la
 * direction dans laquelle elle est effectuée (pour les actions orient�es
 * comme le déplacement, par exemple).
 * 
 * @author Vincent Labatut
 *
 */
final class AiSimState  implements AiState
{
	/**
	 * construit un objet simulant l'�tat d'un sprite qui vient d'�tre cr��,
	 * i.e. qui ne fait rien (STANDING) dans une direction neutre (NONE).
	 */
	protected AiSimState()
	{	name = AiStateName.STANDING;
		direction = Direction.NONE;
		time = 0;
	}

	/**
	 * construit un objet simulant l'�tat d'un sprite qui vient d'�tre cr��,
	 * en utilisant les valeurs pass�es en param�tres
	 * 
	 * @param name
	 * 		nom de l'�tat
	 * @param direction	
	 * 		direction de l'action
	 * @param time	
	 * 		dur�e courante de l'�tat
	 */
	protected AiSimState(AiStateName name, Direction direction, long time)
	{	this.name = name;
		this.direction = direction;
		this.time = time;
	}

	/**
	 * construit un objet simulant l'�tat du sprite pass� en param�tre
	 * 
	 * @param sprite	
	 * 		sprite dont on veut simuler l'�tat
	 */
	protected AiSimState(AiSprite sprite)
	{	AiState state = sprite.getState();
		name = state.getName();
		direction = state.getDirection();
		time = state.getTime();
	}

	/**
	 * construit un objet simulant l'�tat pass� en param�tre
	 * 
	 * @param state
	 * 		�tat à reproduire
	 */
	protected AiSimState(AiState state)
	{	name = state.getName();
		direction = state.getDirection();
		time = state.getTime();
	}

	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** nom associ�e à l'�tat */
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
	/** direction associ�e à l'�tat (peut �tre NONE, c'est à dire : l'�tat n'est pas orient�) */
	private Direction direction;
	
	@Override
	public Direction getDirection()
	{	return direction;
	}

	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** compte combien de temps le sprite a pass� dans l'�tat courant */
	private long time = 0;
	
	@Override
	public long getTime()
	{	return time;
	}
	
	/**
	 * permet de modifier le temps pass� dans cet état.
	 * méthode utilisée exclusivement lors des simulations
	 * 
	 * @param time	
	 * 		le nouveau temps pass� dans cet état
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
	 * termine cet objet et lib�re les ressources occup�es
	 */
	protected void finish()
	{	direction = null;
		name = null;		
	}
}

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
import org.totalboumboum.ai.v201011.adapter.data.AiStateName;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * D�crit un �tat simul� dans lequel un sprite peut se trouver, c'est
 * � dire essentiellement l'action que le sprite r�alise ou qu'il subit.
 * Cet �tat est d�crit par le nom de cette action, et �ventuellement la
 * direction dans laquelle elle est effectu�e (pour les actions orient�es
 * comme le d�placement, par exemple).
 * 
 * @author Vincent Labatut
 *
 */
public class AiSimState 
{
	/**
	 * construit un objet simulant l'�tat d'un sprite qui vient d'�tre cr��,
	 * i.e. qui ne fait rien (STANDING) dans une direction neutre (NONE).
	 */
	AiSimState()
	{	name = AiStateName.STANDING;
		direction = Direction.NONE;
	}

	/**
	 * construit un objet simulant l'�tat du sprite pass� en param�tre
	 * 
	 * @param sprite	sprite dont on veut simuler l'�tat
	 */
	AiSimState(AiSprite<?> sprite)
	{	name = sprite.getState().getName();
		direction = sprite.getState().getDirection();
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** nom associ�e � l'�tat */
	private AiStateName name;
	
	/**
	 * renvoie le nom associ� � l'�tat
	 * 
	 * @return	nom associ� � l'�tat
	 */
	public AiStateName getName()
	{	return name;
	}
	
	void setEnded()
	{	name = AiStateName.ENDED;		
	}
	
	/////////////////////////////////////////////////////////////////
	// DIRECTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** direction associ�e � l'�tat (peut �tre NONE, c'est � dire : l'�tat n'est pas orient�) */
	private Direction direction;
	
	/**
	 * renvoie la direction associ�e � l'�tat,
	 * qui peut �tre NONE, c'est � dire : l'�tat n'est pas orient�
	 * 
	 * @return	direction associ�e � l'�tat
	 */
	public Direction getDirection()
	{	return direction;
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
		result.append("name.: "+name);
		result.append("dir.: "+direction);
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
	void finish()
	{	direction = null;
		name = null;		
	}
}

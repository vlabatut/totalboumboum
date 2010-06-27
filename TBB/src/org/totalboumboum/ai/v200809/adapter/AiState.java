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
 * D�crit un �tat dans lequel un sprite peut se trouver, c'est
 * � dire essentiellement l'action que le sprite r�alise ou qu'il subit.
 * Cet �tat est d�crit par le nom de cette action, et �ventuellement la
 * direction dans laquelle elle est effectu�e (pour les actions orient�es
 * comme le d�placement, par exemple).
 * 
 * @author Vincent Labatut
 *
 */

public class AiState 
{
	/** sprite dont l'�tat est repr�sent� */
	private Sprite sprite;
	
	/**
	 * construit un objet repr�sentant l'�tat du sprite pass� en param�tre
	 * 
	 * @param sprite	sprite dont on veut repr�senter l'�tat
	 */
	AiState(Sprite sprite)
	{	this.sprite = sprite;
	}
	
	/**
	 * met � jour cet �tat en fonction de l'�volution du sprite de r�f�rence
	 */
	void update()
	{	// direction
		this.direction = sprite.getActualDirection();
		// name
		GestureName gesture = sprite.getCurrentGesture().getName();
		name = AiStateName.makeNameFromGesture(gesture);		
	}
	
	/**
	 * termine cet objet et lib�re les ressources occup�es
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
}

package org.totalboumboum.ai.v200910.adapter.data;

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

import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * D�crit un état dans lequel un sprite peut se trouver, c'est
 * à dire essentiellement l'action que le sprite r�alise ou qu'il subit.
 * Cet état est d�crit par le nom de cette action, et �ventuellement la
 * direction dans laquelle elle est effectuée (pour les actions orient�es
 * comme le déplacement, par exemple).
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
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * met à jour cet état en fonction de l'�volution du sprite de r�f�rence
	 */
	void update()
	{	// direction
		this.direction = sprite.getActualDirection();
		// name
		GestureName gesture = sprite.getCurrentGesture().getName();
		name = AiStateName.makeNameFromGesture(gesture);		
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** nom associ�e à l'�tat */
	private AiStateName name;
	
	
	/**
	 * renvoie le nom associ� à l'�tat
	 * 
	 * @return	nom associ� à l'�tat
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
	/** direction associ�e à l'�tat (peut �tre NONE, c'est à dire : l'�tat n'est pas orient�) */
	private Direction direction;
	
	/**
	 * renvoie la direction associ�e à l'�tat,
	 * qui peut �tre NONE, c'est à dire : l'�tat n'est pas orient�
	 * 
	 * @return	direction associ�e à l'�tat
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
		if(o instanceof AiState)
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
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * termine cet objet et lib�re les ressources occup�es
	 */
	void finish()
	{	sprite = null;
		direction = null;
		name = null;		
	}
}

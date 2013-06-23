package org.totalboumboum.ai.v201011.adapter.data.internal;

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

import org.totalboumboum.ai.v201011.adapter.data.AiState;
import org.totalboumboum.ai.v201011.adapter.data.AiStateName;
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
final class AiDataState implements AiState
{
	/** sprite dont l'�tat est repr�sent� */
	private Sprite sprite;
	
	/**
	 * construit un objet repr�sentant l'�tat du sprite pass� en param�tre
	 * 
	 * @param sprite
	 * 		sprite dont on veut repr�senter l'�tat
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
	 * met � jour cet �tat en fonction de l'�volution du sprite de r�f�rence
	 * 
	 * @param elapsedTime
	 * 		temps �coul� depuis la derni�re mise � jour
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
	/** nom associ�e � l'�tat */
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
	/** direction associ�e � l'�tat (peut �tre NONE, c'est � dire : l'�tat n'est pas orient�) */
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
	 * termine cet objet et lib�re les ressources occup�es
	 */
	protected void finish()
	{	sprite = null;
		direction = null;
		name = null;		
	}
}

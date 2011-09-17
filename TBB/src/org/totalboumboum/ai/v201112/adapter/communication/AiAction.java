package org.totalboumboum.ai.v201112.adapter.communication;

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

/**
 * Classe repr�sentant une action qu'un joueur peut effectuer.
 * Elle est utilis�e pour indiquer au jeu quelle est la prochaine
 * action que l'IA veut r�aliser.
 * <p>
 * L'action est d�crite par un nom et �ventuellement une direction.
 * L'action et la direction sont toutes les deux constantes, respectivement
 * de classes AiActionName et Direction.
 * 
 * @author Vincent Labatut
 *
 */
public class AiAction
{
	/**
	 * construit une action non-orient�e (DROP_BOMB,NONE,PUNCH...)
	 * @param name	le nom de l'action
	 */
	public AiAction(AiActionName name)
	{	this(name,Direction.NONE);
	}
	
	/**
	 * construit une action orient�e (MOVE...)
	 * @param name	le nom de l'action
	 * @param direction	la direction de l'action
	 */
	public AiAction(AiActionName name, Direction direction)
	{	// direction
		this.direction = direction;
		// name
		this.name = name;		
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** nom associ� � l'action */
	private AiActionName name;
	
	/**
	 * renvoie le nom associ� � l'action
	 * 
	 * @return	le nom de l'action
	 */
	public AiActionName getName()
	{	return name;
	}
	
	/////////////////////////////////////////////////////////////////
	// DIRECTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** direction associ�e � l'action */
	private Direction direction;
	
	/**
	 * renvoie la direction associ�e � l'action
	 * @return	la direction de l'action
	 */
	public Direction getDirection()
	{	return direction;
	}
}

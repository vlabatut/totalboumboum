package org.totalboumboum.ai.v201213.adapter.communication;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
 * Classe représentant une action qu'un joueur peut effectuer.
 * Elle est utilisée pour indiquer au jeu quelle est la prochaine
 * action que l'agent veut réaliser.
 * <br/>
 * L'action est décrite par un nom et éventuellement une direction.
 * L'action et la direction sont toutes les deux constantes, respectivement
 * de classes {@link AiActionName} et {@link Direction}.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public class AiAction
{
	/**
	 * Construit une action non-orientée (DROP_BOMB,NONE,PUNCH...).
	 * 
	 * @param name	
	 * 		Le nom de l'action.
	 */
	public AiAction(AiActionName name)
	{	this(name,Direction.NONE);
	}
	
	/**
	 * Construit une action orientée (MOVE...).
	 * 
	 * @param name	
	 * 		Le nom de l'action.
	 * @param direction
	 * 		La direction de l'action.
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
	/** Nom associé à l'action */
	private AiActionName name;
	
	/**
	 * Renvoie le nom associé à l'action.
	 * 
	 * @return	
	 * 		Le nom de l'action.
	 */
	public AiActionName getName()
	{	return name;
	}
	
	/////////////////////////////////////////////////////////////////
	// DIRECTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Direction associée à l'action */
	private Direction direction;
	
	/**
	 * Renvoie la direction associée à l'action.
	 * 
	 * @return	
	 * 		La direction de l'action.
	 */
	public Direction getDirection()
	{	return direction;
	}
}

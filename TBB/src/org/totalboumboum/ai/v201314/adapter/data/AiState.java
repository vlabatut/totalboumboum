package org.totalboumboum.ai.v201314.adapter.data;

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

import java.io.Serializable;

import org.totalboumboum.ai.v201314.adapter.data.AiStateName;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Décrit un état dans lequel un sprite peut se trouver, c'est
 * à dire essentiellement l'action que le sprite réalise ou qu'il subit.
 * Cet état est décrit par le nom de cette action, et éventuellement la
 * direction dans laquelle elle est effectuée (pour les actions orientées
 * comme le déplacement, par exemple).
 * 
 * @author Vincent Labatut
 */
public interface AiState extends Serializable
{
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie le nom associé à l'état.
	 * 
	 * @return	
	 * 		Nom associé à l'état.
	 */
	public AiStateName getName();
	
	/////////////////////////////////////////////////////////////////
	// DIRECTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie la direction associée à l'état,
	 * qui peut être NONE, c'est à dire : 
	 * l'état n'est pas orienté.
	 * 
	 * @return	
	 * 		Direction associée à l'état.
	 */
	public Direction getDirection();

	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie la durée que le sprite a passé dans l'état courant.
	 * 
	 * @return	
	 * 		Une durée exprimée en ms.
	 */
	public long getTime();
}

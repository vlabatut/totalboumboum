package org.totalboumboum.ai.v201011.adapter.data;

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

import org.totalboumboum.ai.v201011.adapter.data.AiStateName;
import org.totalboumboum.engine.content.feature.Direction;

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
public interface AiState
{
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie le nom associ� � l'�tat
	 * 
	 * @return	nom associ� � l'�tat
	 */
	public AiStateName getName();
	
	/////////////////////////////////////////////////////////////////
	// DIRECTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la direction associ�e � l'�tat,
	 * qui peut �tre NONE, c'est � dire : l'�tat n'est pas orient�
	 * 
	 * @return	direction associ�e � l'�tat
	 */
	public Direction getDirection();

	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la dur�e que le sprite a pass� dans l'�tat courant
	 * 
	 * @return	une dur�e exprim�e en ms
	 */
	public long getTime();
}

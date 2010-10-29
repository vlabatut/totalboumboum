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

/**
 * Représente un bloc du jeu, c'est à dire généralement un mur
 * (pouvant être détruit ou pas). 
 * 
 * @author Vincent Labatut
 *
 */
public interface AiBlock extends AiSprite
{
	/////////////////////////////////////////////////////////////////
	// DESTRUCTIBLE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie vrai si ce bloc peut être détruit par une bombe, et faux sinon
	 * 
	 * @return	l'indicateur de destructibilité du mur
	 */
	public boolean isDestructible();
}

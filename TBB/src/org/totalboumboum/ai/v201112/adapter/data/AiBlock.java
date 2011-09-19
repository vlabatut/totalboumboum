package org.totalboumboum.ai.v201112.adapter.data;

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

/**
 * Repr�sente un bloc du jeu, c'est à dire g�n�ralement un mur
 * (pouvant �tre détruit ou pas). 
 * 
 * @author Vincent Labatut
 *
 */
public interface AiBlock extends AiSprite
{
	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * indique si ce bloc arr�te les personnages.
	 * <b>ATTENTION :</b> cette méthode ne devrait pas �tre utilisée directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	
	 * 		une valeur AiStopType indiquant si ce bloc arr�te les personnages
	 */
	public AiStopType hasStopHeroes();
	
	/**
	 * indique si ce bloc arr�te les explosions.
	 * <b>ATTENTION :</b> cette méthode ne devrait pas �tre utilisée directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	
	 * 		une valeur AiStopType indiquant si ce bloc arr�te le feu
	 */
	public AiStopType hasStopFires();

	/////////////////////////////////////////////////////////////////
	// DESTRUCTIBLE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie vrai si ce bloc peut �tre détruit par une bombe, et faux sinon
	 * 
	 * @return	
	 * 		l'indicateur de destructibilit� du mur
	 */
	public boolean isDestructible();
}

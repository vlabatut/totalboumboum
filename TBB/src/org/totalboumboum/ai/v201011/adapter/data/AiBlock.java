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
 * Repr�sente un bloc du jeu, c'est � dire g�n�ralement un mur
 * (pouvant �tre d�truit ou pas). 
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
	 * <b>ATTENTION :</b> cette m�thode ne devrait pas �tre utilis�e directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	une valeur AiStopType indiquant si ce bloc arr�te les personnages
	 */
	public AiStopType hasStopHeroes();
	
	/**
	 * indique si ce bloc arr�te les explosions.
	 * <b>ATTENTION :</b> cette m�thode ne devrait pas �tre utilis�e directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	une valeur AiStopType indiquant si ce bloc arr�te le feu
	 */
	public AiStopType hasStopFires();

	/////////////////////////////////////////////////////////////////
	// DESTRUCTIBLE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie vrai si ce bloc peut �tre d�truit par une bombe, et faux sinon
	 * 
	 * @return	l'indicateur de destructibilit� du mur
	 */
	public boolean isDestructible();
}

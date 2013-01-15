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

/**
 * Représente un bloc du jeu, c'est à dire généralement un mur
 * (pouvant être détruit ou pas). 
 * 
 * @author Vincent Labatut
 */
public interface AiBlock extends AiSprite
{
	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Indique si ce bloc arrête les personnages.
	 * <br/>
	 * <b>ATTENTION :</b> cette méthode ne devrait pas être utilisée directement par l'agent,
	 * elle est destinée au calcul des modèles simulant l'évolution du jeu.
	 * utilisez plutot {@link #isCrossableBy}.
	 * 
	 * @return	
	 * 		Une valeur {@link AiStopType} indiquant si ce bloc arrête les personnages.
	 */
	public AiStopType hasStopHeroes();
	
	/**
	 * Indique si ce bloc arrête les explosions.
	 * <br/>
	 * <b>ATTENTION :</b> cette méthode ne devrait pas être utilisée directement par l'agent,
	 * elle est destinée au calcul des modèles simulant l'évolution du jeu.
	 * utilisez plutot {@link #isCrossableBy}.
	 * 
	 * @return	
	 * 		Une valeur AiStopType indiquant si ce bloc arrête le feu.
	 */
	public AiStopType hasStopFires();

	/////////////////////////////////////////////////////////////////
	// DESTRUCTIBLE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie {@code true} si ce bloc peut être détruit par une bombe, 
	 * et {@code false} sinon.
	 * 
	 * @return	
	 * 		L'indicateur de destructibilité du mur.
	 */
	public boolean isDestructible();
}


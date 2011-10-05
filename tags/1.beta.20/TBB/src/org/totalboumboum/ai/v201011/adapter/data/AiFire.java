package org.totalboumboum.ai.v201011.adapter.data;


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
 * repr�sente un feu du jeu, ie une projection mortelle r�sultant (g�n�ralement) 
 * de l'explosion d'une bombe. 
 * 
 * @author Vincent Labatut
 *
 */
public interface AiFire extends AiSprite
{
	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * teste si ce feu est capable de passer � travers les (certains) murs
	 * <b>ATTENTION :</b> cette m�thode ne devrait pas �tre utilis�e directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	
	 * 		vrai si le feu traverse les murs
	 */
	public boolean hasThroughBlocks();

	/**
	 * teste si ce feu est capable de passer � travers les bombes
	 * <b>ATTENTION :</b> cette m�thode ne devrait pas �tre utilis�e directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	
	 * 		vrai si le feu traverse les bombes
	 */
	public boolean hasThroughBombs();

	/**
	 * teste si ce feu est capable de passer � travers les items
	 * <b>ATTENTION :</b> cette m�thode ne devrait pas �tre utilis�e directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	
	 * 		vrai si le feu traverse les items
	 */
	public boolean hasThroughItems();

	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	// TODO � renommer en getElapsedTime, m�me chose pour les bombes (plus changer le getTime actuel en getCountdownTime) 
	public long getTime();	
}
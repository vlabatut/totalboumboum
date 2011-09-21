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
 * représente un feu du jeu, ie une projection mortelle résultant (généralement) 
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
	 * teste si ce feu est capable de passer à travers les (certains) murs
	 * <b>ATTENTION :</b> cette méthode ne devrait pas être utilisée directement par l'IA,
	 * elle est destinée au calcul des modèles simulant l'évolution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	
	 * 		vrai si le feu traverse les murs
	 */
	public boolean hasThroughBlocks();

	/**
	 * teste si ce feu est capable de passer à travers les bombes
	 * <b>ATTENTION :</b> cette méthode ne devrait pas être utilisée directement par l'IA,
	 * elle est destinée au calcul des modèles simulant l'évolution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	
	 * 		vrai si le feu traverse les bombes
	 */
	public boolean hasThroughBombs();

	/**
	 * teste si ce feu est capable de passer à travers les items
	 * <b>ATTENTION :</b> cette méthode ne devrait pas être utilisée directement par l'IA,
	 * elle est destinée au calcul des modèles simulant l'évolution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	
	 * 		vrai si le feu traverse les items
	 */
	public boolean hasThroughItems();

	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	// TODO à renommer en getElapsedTime, même chose pour les bombes (plus changer le getTime actuel en getCountdownTime) 
	public long getTime();	
}

package org.totalboumboum.ai.v201112.adapter.model.partial;

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
 * Représente un obstacle (très général) présent
 * dans une zone de jeu. Pour une case donnée,
 * on distingue ici seulement trois possibilités :
 * <ul>
 * 		<li>: la case contient au moins un obstacle indestructible ;</li>
 * 		<li>: la case contient au moins un obstacle destructible,
 * 			et pas d'obstacle indestructible ;</li>
 * 		<li>: la case ne contient pas d'obstacle.</li>
 * </ul>
 * 
 * @author Vincent Labatut
 */
public enum AiObstacle
{	/** présence d'un obstacle indestructible (mur indestructible)*/
	INDESTRUCTIBLE,
	/** présence d'un obstracle destructible, absence d'obstacle indestructible */
	DESTRUCTIBLE,
	/** absence total d'obstacle */
	NONE;
}

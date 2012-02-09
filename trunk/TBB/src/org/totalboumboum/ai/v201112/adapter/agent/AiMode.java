package org.totalboumboum.ai.v201112.adapter.agent;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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
 * Ce type énuméré représente les différents modes
 * qu'un agent peut adopter : {@code COLLECTING}
 * ou {@code ATTACKING}.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public enum AiMode
{	
	/** l'agent n'a pas assez de puissance et essaie d'en obtenir plus */
	COLLECTING,
	
	/** 
	 * soit il ne reste plus d'item à ramasser, et l'agent n'a pas d'autre choix que d'attaquer les autres joueurs.
	 * soit l'agent a assez de puissance et peut attaquer les autres joueurs. 
	 */
	ATTACKING;
}

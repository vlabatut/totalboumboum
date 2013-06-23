package org.totalboumboum.ai.v201314.adapter.communication;

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
 * Noms donnés aux différentes actions qu'une agent peut effectuer : 
 * NONE (ne rien faire), MOVE (se déplacer, avec la direction à préciser), 
 * DROP_BOMB (poser une bombe), PUNCH (frapper une bombe)... 
 * 
 * @author Vincent Labatut
 */
public enum AiActionName
{	
	/** Poser une bombe */
	DROP_BOMB,
	
	/** Se déplacer */
	MOVE,
	
	/** Ne rien faire du tout */
	NONE,
	
	/** Frapper une bombe */
	PUNCH;	
}

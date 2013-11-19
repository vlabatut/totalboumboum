package org.totalboumboum.ai.v200809.adapter;

import java.io.Serializable;

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
 * 
 * noms donnés aux différentes actions qu'une IA peut effectuer : 
 * NONE (ne rien faire), MOVE (se déplacer, avec la direction à préciser), 
 * DROP_BOMB (poser une bombe), PUNCH (frapper une bombe)... 
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public enum AiActionName implements Serializable
{	/** Poser une bombe */
	DROP_BOMB,
	/** Se déplacer */
	MOVE,
	/** Ne rien faire */
	NONE,
	/** Frapper une bombe */
	PUNCH;	
}

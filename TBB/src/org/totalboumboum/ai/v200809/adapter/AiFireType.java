package org.totalboumboum.ai.v200809.adapter;

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
 * 
 * types de feux reconnus par l'IA.
 * 	
 * @author Vincent Labatut
 *
 */
public enum AiFireType
{
	/** feu normal */
	NORMAL,
	/** feu pénétrant (pas arrété par les les objets destructibles) */
	PENETRATION,
	/** autre type de feu */
	OTHER;	
	
	/**
	 * calcule l'AiFireType correspondant au nom de feu passé en paramètre
	 * 
	 * @param name	nom du feu à traiter
	 * @return	symbole représentant ce type de feu
	 */
	public static AiFireType makeFireType(String name)
	{	AiFireType result;
		if(name.equalsIgnoreCase("normal"))
			result = NORMAL;
		else if(name.equalsIgnoreCase("penetration"))
			result = PENETRATION;
		else
			result = OTHER;
		return result;
	}
}

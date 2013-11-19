package org.totalboumboum.ai.v200910.adapter.data;

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
 * types d'items reconnus par l'IA.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public enum AiItemType implements Serializable 
{
	/** le joueur peut poser une bombe de plus */
	EXTRA_BOMB,
	/** les bombes du joueur explosent plus loin */
	EXTRA_FLAME,
	/** effet négatif sur le joueur (ralentissement, constipation, etc.) */
	MALUS,
	/** le joueur peut frapper dans une bombe et ainsi l'envoyer plus loin */
	PUNCH,
	/** autre type d'item */
	OTHER;	
	
	/**
	 * calcule l'AiItemType correspondant au nom d'item passé en paramètre
	 * 
	 * @param name	nom de l'item à traiter
	 * @return	symbole représentant ce type d'item
	 */
	public static AiItemType makeItemType(String name)
	{	AiItemType result;
		
		if(name.equalsIgnoreCase("extrabomb") || name.equalsIgnoreCase("goldenbomb"))
			result = EXTRA_BOMB;
		
		else if(name.equalsIgnoreCase("extraflame") || name.equalsIgnoreCase("goldenflame"))
			result = EXTRA_FLAME;
		
		else if(name.equalsIgnoreCase("malus") || name.equalsIgnoreCase("disease")
				|| name.equalsIgnoreCase("antibomb") || name.equalsIgnoreCase("nobomb")
				|| name.equalsIgnoreCase("antiflame") || name.equalsIgnoreCase("noflame")
				|| name.equalsIgnoreCase("antispeed") || name.equalsIgnoreCase("nospeed")
				|| name.equalsIgnoreCase("randomnone"))
			result = MALUS;
		
		else if(name.equalsIgnoreCase("punch"))
			result = PUNCH;
		
		else
			result = OTHER;
		
		return result;
	}
}

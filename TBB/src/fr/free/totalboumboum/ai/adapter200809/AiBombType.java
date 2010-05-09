package fr.free.totalboumboum.ai.adapter200809;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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
 * types de bombes reconnus par l'IA.
 * 	
 */
public enum AiBombType
{
	/** bombe normale */
	NORMAL,
	/** bombe p�n�trante (la flamme n'est pas arr�t�e par les les objets destructibles) */
	PENETRATION,
	/** bombe t�l�command�e par le joueur (elle explose quand le joueur le lui demande) */
	REMOTE,
	/** bombe � la fois p�n�trante et t�l�command�e */
	REMOTE_PENTRATION,
	/** autre type de bombes */
	OTHER;	
	
	/**
	 * calcule l'AiBombType correspondant au nom de bombe pass� en param�tre
	 * 
	 * @param name	nom de la bombe � traiter
	 * @return	symbole repr�sentant ce type de bombe
	 */
	public static AiBombType makeBombType(String name)
	{	AiBombType result;
		if(name.equalsIgnoreCase("normal"))
			result = NORMAL;
		else if(name.equalsIgnoreCase("penetration"))
			result = PENETRATION;
		else if(name.equalsIgnoreCase("remotecontrol"))
			result = REMOTE;
		else if(name.equalsIgnoreCase("remotepenetration"))
			result = REMOTE_PENTRATION;
		else
			result = OTHER;
		return result;
	}
}

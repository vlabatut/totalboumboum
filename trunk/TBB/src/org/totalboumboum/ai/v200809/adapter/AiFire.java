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

import org.totalboumboum.engine.content.sprite.fire.Fire;

/**
 * repr�sente un feu du jeu, ie une projection mortelle r�sultant (g�n�ralement) 
 * de l'explosion d'une bombe. 
 * 
 * @author Vincent Labatut
 *
 */
public class AiFire extends AiSprite<Fire>
{
	/**
	 * crée une repr�sentation du feu pass� en param�tre, et contenue dans 
	 * la case pass�e en param�tre.
	 * 
	 * @param tile	case contenant le sprite
	 * @param sprite	sprite à repr�senter
	 */
	AiFire(AiTile tile, Fire sprite)
	{	super(tile,sprite);
		initType();
	}

	@Override
	void update(AiTile tile)
	{	super.update(tile);
	}

	@Override
	void finish()
	{	super.finish();
	}

	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append("Fire: [");
		result.append(super.toString());
		result.append(" - type: "+type);
		result.append(" ]");
		return result.toString();
	}

	/////////////////////////////////////////////////////////////////
	// FIRE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** type du feu */
	private AiFireType type;
	
	/**
	 * renvoie le type du feu
	 * @return	une valeur de type AiFireType repr�sentant le type de feu
	 */
	public AiFireType getType()
	{	return type;	
	}
	
	/**
	 * initialise le type du bombe
	 */
	private void initType()
	{	Fire fire = getSprite();
		type = AiFireType.makeFireType(fire.getFiresetName());		
	}
	
}

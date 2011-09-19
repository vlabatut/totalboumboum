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

import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.feature.action.consume.SpecificConsume;
import org.totalboumboum.engine.content.sprite.block.Block;

/**
 * Repr�sente un bloc du jeu, c'est à dire g�n�ralement un mur
 * (pouvant �tre détruit ou pas). 
 * 
 * @author Vincent Labatut
 *
 */

public class AiBlock extends AiSprite<Block>
{
	/**
	 * crée une repr�sentation du bloc pass� en param�tre, et contenue dans 
	 * la case pass�e en param�tre.
	 * 
	 * @param tile	case contenant le sprite
	 * @param sprite	sprite à repr�senter
	 */
	AiBlock(AiTile tile, Block sprite)
	{	super(tile,sprite);
		updateDestructible();
	}	
	
	@Override
	void update(AiTile tile)
	{	super.update(tile);
		updateDestructible();
	}

	@Override
	void finish()
	{	super.finish();
	}

	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append("Block: [");
		result.append(super.toString());
		result.append(" - destr.: "+destructible);
		result.append(" ]");
		return result.toString();
	}

	/////////////////////////////////////////////////////////////////
	// DESTRUCTIBLE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si ce mur peut �tre détruit par une bombe */
	private boolean destructible;
	
	/** 
	 * initialise l'indicateur de destructibilit� de ce bloc 
	 */
	private void updateDestructible()
	{	Block sprite = getSprite();
		SpecificAction action = new SpecificConsume(sprite);
		destructible = !sprite.isTargetPreventing(action);
	}	

	/**
	 * renvoie vrai si ce bloc peut �tre détruit par une bombe, et faux sinon
	 * 
	 * @return	l'indicateur de destructibilit� du mur
	 */
	public boolean isDestructible()
	{	return destructible;		
	}
}

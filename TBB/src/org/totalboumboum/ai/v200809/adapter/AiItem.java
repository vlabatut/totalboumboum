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

import org.totalboumboum.engine.content.sprite.item.Item;

/**
 * repr�sente un item du jeu, ie un bonus ou un malus que le joueur peut ramasser.
 * un item est caract�ris� par son type, repr�sentant le pouvoir apport� (ou enlev�)
 * par l'item. Ce type est repr�sent�e par une valeur de type AiItemType.
 * 
 * @author Vincent Labatut
 *
 */
public class AiItem extends AiSprite<Item>
{	
	/**
	 * crée une repr�sentation de l'item pass� en param�tre, et contenue dans 
	 * la case pass�e en param�tre.
	 * 
	 * @param tile	case contenant le sprite
	 * @param sprite	sprite à repr�senter
	 */
	AiItem(AiTile tile, Item sprite)
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
		result.append("Item: [");
		result.append(super.toString());
		result.append(" - type: "+type);
		result.append(" ]");
		return result.toString();
	}

	/////////////////////////////////////////////////////////////////
	// TYPE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** type d'item repr�sent� */
	private AiItemType type;
	
	/**
	 * renvoie le type de l'item repr�sent�
	 * 
	 * @return	le type de l'item
	 */
	public AiItemType getType()
	{	return type;	
	}
	
	/**
	 * initialise le type de l'item repr�sent�
	 */
	private void initType()
	{	Item item = getSprite();
		type = AiItemType.makeItemType(item.getItemName());		
	}
	
}

package org.totalboumboum.ai.v201011.adapter.data;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
 * repr�sente un item du jeu, ie un bonus ou un malus que le joueur peut ramasser.
 * un item est caract�ris� par son type, repr�sentant le pouvoir apport� (ou enlev�)
 * par l'item. Ce type est repr�sent�e par une valeur de type AiItemType.
 * 
 * @author Vincent Labatut
 *
 */
public interface AiItem extends AiSprite
{	
	/////////////////////////////////////////////////////////////////
	// TYPE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie le type de l'item repr�sent�
	 * 
	 * @return	le type de l'item
	 */
	public AiItemType getType();
}

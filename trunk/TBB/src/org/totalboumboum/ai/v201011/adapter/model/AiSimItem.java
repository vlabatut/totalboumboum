package org.totalboumboum.ai.v201011.adapter.model;

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

import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiItemType;
import org.totalboumboum.ai.v201011.adapter.data.AiStopType;

/**
 * simule un item du jeu, ie un bonus ou un malus que le joueur peut ramasser.
 * un item est caractérisé par son type, représentant le pouvoir apporté (ou enlevé)
 * par l'item. Ce type est représentée par une valeur de type AiItemType.
 * 
 * @author Vincent Labatut
 *
 */
public class AiSimItem extends AiSimSprite
{	
	/**
	 * crée une simulation de l'item passé en paramètre,
	 * avec les propriétés passées en paramètres.
	 * 
	 * @param tile	case contenant le sprite
	 * @param type	type d'item
	 * @param stopBombs	indique si l'item constitue un obstacle aux bombes
	 * @param stopFire	indique si l'item constitue un obstacle au feu
	 */
	public AiSimItem(AiSimTile tile,  double posX, double posY, double posZ,
			AiItemType type, AiStopType stopBombs, AiStopType stopFires)
	{	super(tile,posX,posY,posZ);
		
		this.type = type;		
		this.stopBombs = stopBombs;
		this.stopFires = stopFires;
	}	

	/**
	 * crée une simulation de l'item passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param sprite	sprite à simuler
	 * @param tile	case contenant le sprite
	 */
	AiSimItem(AiItem sprite, AiSimTile tile)
	{	super(sprite,tile);
		
		type = sprite.getType();		
		stopBombs = sprite.hasStopBombs();
		stopFires = sprite.hasStopFires();
	}

	/**
	 * construit une simulation de l'item passé en paramètre,
	 * (donc une simple copie) et la place dans la case indiquée.
	 * 
	 * @param sprite	simulation à copier
	 * @param tile	simulation de la case devant contenir la copie
	 */
	public AiSimItem(AiSimItem sprite, AiSimTile tile)
	{	super(sprite,tile);
		
		type = sprite.type;		
		stopBombs = sprite.stopBombs;
		stopFires = sprite.stopFires;
	}
	
	/////////////////////////////////////////////////////////////////
	// TYPE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** type d'item simulé */
	private AiItemType type;
	
	/**
	 * renvoie le type de l'item simulé
	 * 
	 * @return	le type de l'item
	 */
	public AiItemType getType()
	{	return type;	
	}
	
	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si ce bloc laisse passer les joueurs */
	private AiStopType stopBombs;
	/** indique si ce bloc laisse passer le feu */
	private AiStopType stopFires;

	@Override
	public boolean isCrossableBy(AiSimSprite sprite)
	{	// par défaut, on bloque
		boolean result = false;
		// si le sprite considéré est un personnage
		if(sprite instanceof AiSimHero)
		{	result = true;
		}
		// si le sprite considéré est un feu
		else if(sprite instanceof AiSimFire)
		{	AiSimFire fire = (AiSimFire) sprite;
			if(stopFires==AiStopType.NO_STOP)
				result = true;
			else if(stopFires==AiStopType.WEAK_STOP)
				result = fire.hasThroughItems();
			else if(stopFires==AiStopType.STRONG_STOP)
				result = false;
		}
		// si le sprite considéré est une bombe
		else if(sprite instanceof AiSimBomb)
		{	AiSimBomb bomb = (AiSimBomb) sprite;
			if(stopBombs==AiStopType.NO_STOP)
				result = true;
			else if(stopBombs==AiStopType.WEAK_STOP)
				result = bomb.hasThroughItems();
			else if(stopBombs==AiStopType.STRONG_STOP)
				result = false;
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	void finish()
	{	super.finish();
	}
}

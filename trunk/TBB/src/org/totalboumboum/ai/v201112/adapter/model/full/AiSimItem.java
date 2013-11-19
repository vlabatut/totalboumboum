package org.totalboumboum.ai.v201112.adapter.model.full;

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

import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiItemType;
import org.totalboumboum.ai.v201112.adapter.data.AiSprite;
import org.totalboumboum.ai.v201112.adapter.data.AiStopType;

/**
 * simule un item du jeu, ie un bonus ou un malus que le joueur peut ramasser.
 * un item est caractérisé par son type, représentant le pouvoir apporté (ou enlevé)
 * par l'item. Ce type est représentée par une valeur de type AiItemType.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public final class AiSimItem extends AiSimSprite implements AiItem
{	/** Id de la classe */
	private static final long serialVersionUID = 1L;
	
	/**
	 * crée une simulation de l'item passé en paramètre,
	 * avec les propriétés passées en paramètres.
	 * 
	 * @param id
	 * 		numéro d'identification de l'item
	 * @param tile
	 * 		case contenant l'item
	 * @param posX
	 * 		abscisse de l'item
	 * @param posY
	 * 		ordonnée de l'item
	 * @param posZ
	 * 		hauteur de l'item
	 * @param state
	 * 		état de l'item
	 * @param burningDuration
	 * 		durée de combustion de l'item
	 * @param currentSpeed
	 * 		vitesse courante de déplacement de l'item
	 * @param type
	 * 		type d'item (extrabomb, extraflame, etc.)
	 * @param stopBombs
	 * 		capacité à bloquer les bombes
	 * @param stopFires
	 * 		capacité à bloquer le feu
	 */
	protected AiSimItem(int id, AiSimTile tile,  double posX, double posY, double posZ,
			AiSimState state, long burningDuration, double currentSpeed,
			AiItemType type, AiStopType stopBombs, AiStopType stopFires)
	{	super(id,tile,posX,posY,posZ,state,burningDuration,currentSpeed);
		
		this.type = type;		
		this.stopBombs = stopBombs;
		this.stopFires = stopFires;
	}	

	/**
	 * crée une simulation de l'item passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param item
	 * 		sprite à simuler
	 * @param tile
	 * 		case contenant le sprite
	 */
	protected AiSimItem(AiItem item, AiSimTile tile)
	{	super(item,tile);
		
		type = item.getType();		
		stopBombs = item.hasStopBombs();
		stopFires = item.hasStopFires();
	}

	/////////////////////////////////////////////////////////////////
	// TYPE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** type d'item simulé */
	private AiItemType type;
	
	@Override
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
	public AiStopType hasStopFires()
	{	return stopFires;
	}

	@Override
	public AiStopType hasStopBombs()
	{	return stopBombs;
	}

	@Override
	public boolean isCrossableBy(AiSprite sprite)
	{	// par défaut, on bloque
		boolean result = false;
		
		// si le sprite considéré est un personnage
		if(sprite instanceof AiHero)
		{	result = true;
		}
		
		// si le sprite considéré est un feu
		else if(sprite instanceof AiFire)
		{	AiFire fire = (AiFire) sprite;
			if(stopFires==AiStopType.NO_STOP)
				result = true;
			else if(stopFires==AiStopType.WEAK_STOP)
				result = fire.hasThroughItems();
			else if(stopFires==AiStopType.STRONG_STOP)
				result = false;
		}
		
		// si le sprite considéré est une bombe
		else if(sprite instanceof AiBomb)
		{	AiBomb bomb = (AiBomb) sprite;
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
	protected void finish()
	{	super.finish();
	}
}

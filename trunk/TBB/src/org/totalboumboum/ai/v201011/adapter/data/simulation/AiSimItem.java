package org.totalboumboum.ai.v201011.adapter.data.simulation;

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
import org.totalboumboum.ai.v201011.adapter.data.AiSprite;
import org.totalboumboum.ai.v201011.adapter.data.AiStopType;

/**
 * simule un item du jeu, ie un bonus ou un malus que le joueur peut ramasser.
 * un item est caract�ris� par son type, repr�sentant le pouvoir apport� (ou enlev�)
 * par l'item. Ce type est repr�sent�e par une valeur de type AiItemType.
 * 
 * @author Vincent Labatut
 *
 */
public final class AiSimItem extends AiSimSprite implements AiItem
{	
	/**
	 * cr�e une simulation de l'item pass� en param�tre,
	 * avec les propri�t�s pass�es en param�tres.
	 * 
	 * @param tile	case contenant le sprite
	 * @param type	type d'item
	 * @param stopBombs	indique si l'item constitue un obstacle aux bombes
	 * @param stopFire	indique si l'item constitue un obstacle au feu
	 */
	protected AiSimItem(AiSimTile tile,  double posX, double posY, double posZ,
			AiItemType type, AiStopType stopBombs, AiStopType stopFires)
	{	super(tile,posX,posY,posZ);
		
		this.type = type;		
		this.stopBombs = stopBombs;
		this.stopFires = stopFires;
	}	

	/**
	 * cr�e une simulation de l'item pass� en param�tre, et contenue dans 
	 * la case pass�e en param�tre.
	 * 
	 * @param sprite	sprite � simuler
	 * @param tile	case contenant le sprite
	 */
	protected AiSimItem(AiItem sprite, AiSimTile tile)
	{	super(sprite,tile);
		
		type = sprite.getType();		
		stopBombs = sprite.hasStopBombs();
		stopFires = sprite.hasStopFires();
	}
	
	/////////////////////////////////////////////////////////////////
	// TYPE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** type d'item simul� */
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
	{	// par d�faut, on bloque
		boolean result = false;
		// si le sprite consid�r� est un personnage
		if(sprite instanceof AiSimHero)
		{	result = true;
		}
		// si le sprite consid�r� est un feu
		else if(sprite instanceof AiSimFire)
		{	AiSimFire fire = (AiSimFire) sprite;
			if(stopFires==AiStopType.NO_STOP)
				result = true;
			else if(stopFires==AiStopType.WEAK_STOP)
				result = fire.hasThroughItems();
			else if(stopFires==AiStopType.STRONG_STOP)
				result = false;
		}
		// si le sprite consid�r� est une bombe
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
	protected void finish()
	{	super.finish();
	}
}

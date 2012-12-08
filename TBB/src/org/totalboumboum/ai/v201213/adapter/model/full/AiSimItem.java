package org.totalboumboum.ai.v201213.adapter.model.full;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiSprite;
import org.totalboumboum.ai.v201213.adapter.data.AiStopType;

/**
 * Simule un item du jeu, ie un bonus ou un malus que le joueur peut ramasser.
 * Un item est caractérisé par son type, représentant le pouvoir apporté (ou enlevé)
 * par l'item. Ce type est représentée par une valeur de type AiItemType.
 * 
 * @author Vincent Labatut
 */
public final class AiSimItem extends AiSimSprite implements AiItem
{
	/**
	 * Crée une simulation de l'item passé en paramètre,
	 * avec les propriétés passées en paramètres.
	 * 
	 * @param id
	 * 		Numéro d'identification de l'item.
	 * @param tile
	 * 		Case contenant l'item.
	 * @param posX
	 * 		Abscisse de l'item.
	 * @param posY
	 * 		Ordonnée de l'item.
	 * @param posZ
	 * 		Hauteur de l'item.
	 * @param state
	 * 		État de l'item.
	 * @param burningDuration
	 * 		Durée de combustion de l'item.
	 * @param currentSpeed
	 * 		Vitesse courante de déplacement de l'item.
	 * @param type
	 * 		Type d'item (extrabomb, extraflame...).
	 * @param strength
	 * 		Force de l'item.
	 * @param stopBombs
	 * 		Capacité à bloquer les bombes.
	 * @param stopFires
	 * 		Capacité à bloquer le feu.
	 * @param contagious
	 * 		Effet contagieux.
	 * @param limitedDuration
	 * 		Effet limité. 
	 * @param normalDuration
	 * 		Durée de l'effet. 
	 * @param elapsedTime 
	 * 		Temps d'effet déjà écoulé.
	 */
	protected AiSimItem(int id, AiSimTile tile,  double posX, double posY, double posZ,
			AiSimState state, long burningDuration, double currentSpeed,
			AiItemType type, double strength, 
			AiStopType stopBombs, AiStopType stopFires,
			boolean contagious,
			boolean limitedDuration, long normalDuration, long elapsedTime)
	{	super(id,tile,posX,posY,posZ,state,burningDuration,currentSpeed);
		
		this.type = type;		
		this.strength = strength;
		this.stopBombs = stopBombs;
		this.stopFires = stopFires;
		this.contagious = contagious;
		this.limitedDuration = limitedDuration;
		this.normalDuration = normalDuration;
		this.elapsedTime = elapsedTime;
	}	

	/**
	 * crée une simulation de l'item passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param tile
	 * 		case contenant le sprite
	 * @param item
	 * 		sprite à simuler
	 */
	protected AiSimItem(AiSimTile tile, AiItem item)
	{	super(tile,item);
		
		type = item.getType();		
		strength = item.getStrength();
		stopBombs = item.hasStopBombs();
		stopFires = item.hasStopFires();
		contagious = item.isContagious();
		limitedDuration = item.hasLimitedDuration();
		normalDuration = item.getNormalDuration();
		elapsedTime = item.getElapsedTime();
	}

	/////////////////////////////////////////////////////////////////
	// TYPE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Type d'item simulé */
	private AiItemType type;
	
	@Override
	public AiItemType getType()
	{	return type;	
	}
	
	/////////////////////////////////////////////////////////////////
	// STRENGTH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Force de l'item */
	private double strength;

	@Override
	public double getStrength()
	{	return strength;
	}
	
	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indique si ce bloc laisse passer les joueurs */
	private AiStopType stopBombs;
	/** Indique si ce bloc laisse passer le feu */
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
	// CONTAGION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indique si cet item a un effet contagieux */
	private boolean contagious = false;
	
	@Override
	public boolean isContagious()
	{	return contagious;
	}
	
	/////////////////////////////////////////////////////////////////
	// DURATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indique si cet item a un effet à durée limitée */
	private boolean limitedDuration;
	/** Durée de l'effet de cet item( s'il a une durée limitée) */
	private long normalDuration;
	/** Durée écoulée depuis que cet item a commencé à faire son effet */
	private long elapsedTime;
	
	@Override
	public boolean hasLimitedDuration()
	{	return limitedDuration;
	}
	
	@Override
	public long getNormalDuration()
	{	return normalDuration;
	}

	@Override
	public long getElapsedTime()
	{	return elapsedTime;
	}
	
	/**
	 * Modifie le temps écoulé pour l'effet de cet item.
	 * Cette fonction est réservée à un usage interne.
	 * 
	 * @param elapsedTime
	 * 		Le nouveau temps écoulé pour l'effet de cet item.
	 */
	protected void setElapsedTime(long elapsedTime)
	{	this.elapsedTime = elapsedTime;
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

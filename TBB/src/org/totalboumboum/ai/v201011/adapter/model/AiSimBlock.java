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

import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiStopType;

/**
 * Simule un bloc du jeu, c'est à dire généralement un mur
 * (pouvant être détruit ou pas). 
 * 
 * @author Vincent Labatut
 *
 */
public class AiSimBlock extends AiSimSprite
{
	/**
	 * crée une simulation du bloc passé en paramètre,
	 * avec les propriétés passées en paramètres.
	 * 
	 * @param tile	case contenant le sprite
	 * @param destructible	indique si la case est destructible par le feu
	 * @param stopHeroes	indique si la case constitue un obstacle aux personnages
	 * @param stopFire	indique si la case constitue un obstacle au feu
	 */
	public AiSimBlock(AiSimTile tile, boolean destructible, AiStopType stopHeroes, AiStopType stopFires)
	{	super(tile);
		
		this.destructible = destructible;
		this.stopsHeroes = stopHeroes;
		this.stopsFires = stopFires;
	}	

	/**
	 * crée une simulation du bloc passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param sprite	sprite à simuler
	 * @param tile	case contenant le sprite
	 */
	AiSimBlock(AiBlock sprite, AiSimTile tile)
	{	super(sprite,tile);
		
		this.destructible = sprite.isDestructible();
		this.stopsHeroes = sprite.isStopsHeroes();
		this.stopsFires = sprite.isStopsFires();
	}	
	
	/**
	 * construit une simulation du sprite passé en paramètre,
	 * (donc une simple copie) et la place dans la case indiquée.
	 * 
	 * @param sprite	simulation à copier
	 * @param tile	simulation de la case devant contenir la copie
	 */
	public AiSimBlock(AiSimBlock sprite, AiSimTile tile)
	{	super(sprite,tile);
		
		destructible = sprite.destructible;
		stopsHeroes = sprite.stopsHeroes;
		stopsFires = sprite.stopsFires;
	}
	
	/////////////////////////////////////////////////////////////////
	// DESTRUCTIBLE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si ce bloc peut être détruit par une bombe */
	private boolean destructible;

	/**
	 * renvoie vrai si ce bloc peut être détruit par une bombe, et faux sinon
	 * 
	 * @return	l'indicateur de destructibilité du mur
	 */
	public boolean isDestructible()
	{	return destructible;		
	}

	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si ce bloc laisse passer les joueurs */
	private AiStopType stopsHeroes;
	/** indique si ce bloc laisse passer le feu */
	private AiStopType stopsFires;
	
	@Override
	public boolean isCrossableBy(AiSimSprite sprite)
	{	// par défaut, on bloque
		boolean result = false;
		// si le sprite considéré est un personnage
		if(sprite instanceof AiSimHero)
		{	AiSimHero hero = (AiSimHero) sprite;
			if(hero.getTile()==getTile()) //simplification
				result = true;
			else if(stopsHeroes==AiStopType.NO_STOP)
				result = true;
			else if(stopsHeroes==AiStopType.WEAK_STOP)
				result = hero.hasThroughBlocks();
			else if(stopsHeroes==AiStopType.STRONG_STOP)
				result = false;
		}
		// si le sprite considéré est un feu
		else if(sprite instanceof AiSimFire)
		{	AiSimFire fire = (AiSimFire) sprite;
			if(stopsFires==AiStopType.NO_STOP)
				result = true;
			else if(stopsFires==AiStopType.WEAK_STOP)
				result = fire.hasThroughBlocks();
			else if(stopsFires==AiStopType.STRONG_STOP)
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
		result.append("Block: [");
		result.append(super.toString());
		result.append(" - destr.: "+destructible);
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

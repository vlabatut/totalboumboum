package org.totalboumboum.ai.v201011.adapter.model;

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

import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiSprite;
import org.totalboumboum.ai.v201011.adapter.data.AiStopType;

/**
 * Simule un bloc du jeu, c'est à dire généralement un mur
 * (pouvant être détruit ou pas). 
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
final class AiSimBlock extends AiSimSprite implements AiBlock
{
	/**
	 * crée une simulation du bloc passé en paramètre,
	 * avec les propriétés passées en paramètres.
	 * 
	 * @param id
	 * 		numéro d'identification du bloc
	 * @param tile
	 * 		case contenant le bloc
	 * @param posX
	 * 		abscisse du bloc
	 * @param posY
	 * 		ordonnée du bloc
	 * @param posZ
	 * 		hauteur du bloc
	 * @param state
	 * 		état du bloc
	 * @param burningDuration
	 * 		durée de combustion du bloc
	 * @param currentSpeed
	 * 		vitesse courante de déplacement du bloc (a priori il est immobile)
	 * @param destructible
	 * 		sensibilité au feu
	 * @param stopHeroes
	 * 		capacité à bloquer les personnages
	 * @param stopFires
	 * 		capacité à bloquer le feu
	 */
	protected AiSimBlock(int id, AiSimTile tile, double posX, double posY, double posZ,
			AiSimState state, long burningDuration, double currentSpeed,
			boolean destructible, AiStopType stopHeroes, AiStopType stopFires)
	{	super(id,tile,posX,posY,posZ,state,burningDuration,currentSpeed);
		
		this.destructible = destructible;
		this.stopHeroes = stopHeroes;
		this.stopFires = stopFires;
	}	

	/**
	 * crée une simulation du bloc passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param block
	 * 		sprite à simuler
	 * @param tile
	 * 		case contenant le sprite
	 */
	protected AiSimBlock(AiBlock block, AiSimTile tile)
	{	super(block,tile);
		
		this.destructible = block.isDestructible();
		this.stopHeroes = block.hasStopHeroes();
		this.stopFires = block.hasStopFires();
	}	
	
	/////////////////////////////////////////////////////////////////
	// DESTRUCTIBLE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si ce bloc peut être détruit par une bombe */
	private boolean destructible;

	@Override
	public boolean isDestructible()
	{	return destructible;		
	}
	
	/**
	 * modifie la destructibilité de ce mur
	 * 
	 * @param destructible
	 * 		nouvelle valeur de l'indicateur de destructibilité du mur
	 */
	public void setDestructible(boolean destructible)
	{	this.destructible = destructible;
	}

	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si ce bloc laisse passer les joueurs */
	private AiStopType stopHeroes;
	/** indique si ce bloc laisse passer le feu */
	private AiStopType stopFires;
	
	@Override
	public AiStopType hasStopHeroes()
	{	return stopHeroes;
	}
	
	@Override
	public AiStopType hasStopFires()
	{	return stopFires;
	}

	@Override
	public boolean isCrossableBy(AiSprite sprite)
	{	// par défaut, on bloque
		boolean result = false;
		
		// si le sprite considéré est un personnage
		if(sprite instanceof AiSimHero)
		{	AiSimHero hero = (AiSimHero) sprite;
			if(hero.getTile()==getTile()) //simplification
				result = true;
			else if(stopHeroes==AiStopType.NO_STOP)
				result = true;
			else if(stopHeroes==AiStopType.WEAK_STOP)
				result = hero.hasThroughBlocks();
			else if(stopHeroes==AiStopType.STRONG_STOP)
				result = false;
		}
		
		// si le sprite considéré est un feu
		else if(sprite instanceof AiSimFire)
		{	AiSimFire fire = (AiSimFire) sprite;
			if(stopFires==AiStopType.NO_STOP)
				result = true;
			else if(stopFires==AiStopType.WEAK_STOP)
				result = fire.hasThroughBlocks();
			else if(stopFires==AiStopType.STRONG_STOP)
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
	protected void finish()
	{	super.finish();
	}
}

package org.totalboumboum.ai.v201112.adapter.model;

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

import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiSprite;
import org.totalboumboum.ai.v201112.adapter.data.AiStopType;

/**
 * Simule un bloc du jeu, c'est � dire g�n�ralement un mur
 * (pouvant �tre d�truit ou pas). 
 * 
 * @author Vincent Labatut
 *
 */
final class AiSimBlock extends AiSimSprite implements AiBlock
{
	/**
	 * cr�e une simulation du bloc pass� en param�tre,
	 * avec les propri�t�s pass�es en param�tres.
	 * 
	 * @param id
	 * 		num�ro d'identification du bloc
	 * @param tile
	 * 		case contenant le bloc
	 * @param posX
	 * 		abscisse du bloc
	 * @param posY
	 * 		ordonn�e du bloc
	 * @param posZ
	 * 		hauteur du bloc
	 * @param state
	 * 		�tat du bloc
	 * @param burningDuration
	 * 		dur�e de combustion du bloc
	 * @param currentSpeed
	 * 		vitesse courante de d�placement du bloc (a priori il est immobile)
	 * @param destructible
	 * 		sensibilit� au feu
	 * @param stopHeroes
	 * 		capacit� � bloquer les personnages
	 * @param stopFires
	 * 		capacit� � bloquer le feu
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
	 * cr�e une simulation du bloc pass� en param�tre, et contenue dans 
	 * la case pass�e en param�tre.
	 * 
	 * @param block
	 * 		sprite � simuler
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
	/** indique si ce bloc peut �tre d�truit par une bombe */
	private boolean destructible;

	@Override
	public boolean isDestructible()
	{	return destructible;		
	}
	
	/**
	 * modifie la destructibilit� de ce mur
	 * 
	 * @param destructible
	 * 		nouvelle valeur de l'indicateur de destructibilit� du mur
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
	{	// par d�faut, on bloque
		boolean result = false;
		
		// si le sprite consid�r� est un personnage
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
		
		// si le sprite consid�r� est un feu
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

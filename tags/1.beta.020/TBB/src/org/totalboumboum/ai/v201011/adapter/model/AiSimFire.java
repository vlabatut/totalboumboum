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

import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiSprite;

/**
 * simule un feu du jeu, ie une projection mortelle r�sultant (g�n�ralement) 
 * de l'explosion d'une bombe. 
 * 
 * @author Vincent Labatut
 *
 */
final class AiSimFire extends AiSimSprite implements AiFire
{
	/**
	 * cr�e une simulation du feu pass� en param�tre,
	 * avec les propri�t�s pass�es en param�tres.
	 * 
	 * @param id
	 * 		num�ro d'identification du feu
	 * @param tile
	 * 		case contenant le feu
	 * @param posX
	 * 		abscisse du feu
	 * @param posY
	 * 		ordonn�e du feu
	 * @param posZ
	 * 		hauteur du feu
	 * @param state
	 * 		�tat du feu
	 * @param burningDuration
	 * 		dur�e de combustion du feu
	 * @param currentSpeed
	 * 		vitesse courante de d�placement du feu (inutile)
	 * @param throughBlocks
	 * 		capacit� � traverser les murs
	 * @param throughBombs
	 * 		capacit� � traverser les bombes
	 * @param throughItems
	 * 		capacit� � traverser les items
	 */
	protected AiSimFire(int id, AiSimTile tile, double posX, double posY, double posZ,
			AiSimState state, long burningDuration, double currentSpeed,
			boolean throughBlocks, boolean throughBombs, boolean throughItems)
	{	super(id,tile,posX,posY,posZ,state,burningDuration,currentSpeed);
		
		this.throughBlocks = throughBlocks;
		this.throughBombs = throughBombs;
		this.throughItems = throughItems;
	}	

	/**
	 * cr�e une simulation du feu pass� en param�tre, et contenue dans 
	 * la case pass�e en param�tre.
	 * 
	 * @param fire	
	 * 		sprite � simuler
	 * @param tile	
	 * 		case contenant le sprite
	 */
	protected AiSimFire(AiFire fire, AiSimTile tile)
	{	super(fire,tile);
		
		this.throughBlocks = fire.hasThroughBlocks();
		this.throughBombs = fire.hasThroughBombs();
		this.throughItems = fire.hasThroughItems();
	}
	
	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si le feu peut traverser les murs */
	private boolean throughBlocks;
	/** indique si le feu peut traverser les bombes */
	private boolean throughBombs;
	/** indique si le feu peut traverser les items */
	private boolean throughItems;
	
	@Override
	public boolean hasThroughBlocks()
	{	return throughBlocks;	
	}

	@Override
	public boolean hasThroughBombs()
	{	return throughBombs;	
	}

	@Override
	public boolean hasThroughItems()
	{	return throughItems;
	}

	@Override
	public boolean isCrossableBy(AiSprite sprite)
	{	// par d�faut, on bloque
		boolean result = false;
		
		// si le sprite consid�r� est un personnage : peut traverser le feu seulement s'il a une protection
		if(sprite instanceof AiSimHero)
		{	AiSimHero hero = (AiSimHero) sprite;
			result = hero.hasThroughFires();
		}
		
		// si c'est une bombe : peut traverser le feu seulement si elle n'explose pas � son contact
		else if(sprite instanceof AiSimHero)
		{	AiSimBomb bomb = (AiSimBomb) sprite;
			result = !bomb.hasExplosionTrigger();
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** temps �coul� depuis que le feu existe, exprim� en ms */
	private long time = 0;
	
	@Override
	public long getTime()
	{	return time;
	}

	/**
	 * modifie le temps �coul�
	 * 
	 * @param time
	 * 		temps �coul�
	 */
	protected void setTime(long time)
	{	this.time = time;
	}
	
	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append("Fire: [");
		result.append(super.toString());
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
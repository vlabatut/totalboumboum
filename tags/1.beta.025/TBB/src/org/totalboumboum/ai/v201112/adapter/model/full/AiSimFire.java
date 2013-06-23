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
import org.totalboumboum.ai.v201112.adapter.data.AiSprite;

/**
 * simule un feu du jeu, ie une projection mortelle résultant (généralement) 
 * de l'explosion d'une bombe. 
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public final class AiSimFire extends AiSimSprite implements AiFire
{
	/**
	 * crée une simulation du feu passé en paramètre,
	 * avec les propriétés passées en paramètres.
	 * 
	 * @param id
	 * 		numéro d'identification du feu
	 * @param tile
	 * 		case contenant le feu
	 * @param posX
	 * 		abscisse du feu
	 * @param posY
	 * 		ordonnée du feu
	 * @param posZ
	 * 		hauteur du feu
	 * @param state
	 * 		état du feu
	 * @param burningDuration
	 * 		durée de combustion du feu
	 * @param currentSpeed
	 * 		vitesse courante de déplacement du feu (inutile)
	 * @param throughBlocks
	 * 		capacité à traverser les murs
	 * @param throughBombs
	 * 		capacité à traverser les bombes
	 * @param throughItems
	 * 		capacité à traverser les items
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
	 * crée une simulation du feu passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param fire	
	 * 		sprite à simuler
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
	{	// par défaut, on bloque
		boolean result = false;
		
		// si le sprite considéré est un personnage : peut traverser le feu seulement s'il a une protection
		if(sprite instanceof AiHero)
		{	AiHero hero = (AiHero) sprite;
			result = hero.hasThroughFires();
		}
		
		// si c'est une bombe : peut traverser le feu seulement si elle n'explose pas à son contact
		else if(sprite instanceof AiBomb)
		{	AiBomb bomb = (AiBomb) sprite;
			result = !bomb.hasExplosionTrigger();
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** temps écoulé depuis que le feu existe, exprimé en ms */
	private long time = 0;
	
	@Override
	public long getTime()
	{	return time;
	}

	/**
	 * modifie le temps écoulé
	 * 
	 * @param time
	 * 		temps écoulé
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

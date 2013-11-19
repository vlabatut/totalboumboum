package org.totalboumboum.ai.v201213.adapter.model.full;

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

import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiSprite;

/**
 * Simule un feu du jeu, ie une projection mortelle résultant (généralement) 
 * de l'explosion d'une bombe. 
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public final class AiSimFire extends AiSimSprite implements AiFire
{	/** Id de la classe */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Crée une simulation du feu passé en paramètre,
	 * avec les propriétés passées en paramètres.
	 * 
	 * @param id
	 * 		Numéro d'identification du feu.
	 * @param tile
	 * 		Case contenant le feu.
	 * @param posX
	 * 		Abscisse du feu.
	 * @param posY
	 * 		Ordonnée du feu.
	 * @param posZ
	 * 		Hauteur du feu.
	 * @param state
	 * 		État du feu.
	 * @param burningDuration
	 * 		Durée de combustion du feu.
	 * @param currentSpeed
	 * 		Vitesse courante de déplacement du feu (inutile).
	 * @param throughBlocks
	 * 		Capacité à traverser les murs.
	 * @param throughBombs
	 * 		Capacité à traverser les bombes.
	 * @param throughItems
	 * 		Capacité à traverser les items.
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
	 * Crée une simulation du feu passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param tile	
	 * 		Case contenant le sprite.
	 * @param fire	
	 * 		Sprite à simuler.
	 */
	protected AiSimFire(AiSimTile tile, AiFire fire)
	{	super(tile,fire);
		
		this.throughBlocks = fire.hasThroughBlocks();
		this.throughBombs = fire.hasThroughBombs();
		this.throughItems = fire.hasThroughItems();
	}
	
	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indique si le feu peut traverser les murs */
	private boolean throughBlocks;
	/** Indique si le feu peut traverser les bombes */
	private boolean throughBombs;
	/** Indique si le feu peut traverser les items */
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
	/** Temps écoulé depuis que le feu existe, exprimé en ms */
	private long time = 0;
	
	@Override
	public long getElapsedTime()
	{	return time;
	}

	/**
	 * Modifie le temps écoulé
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

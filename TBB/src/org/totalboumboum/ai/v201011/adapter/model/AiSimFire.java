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

import org.totalboumboum.ai.v201011.adapter.data.AiFire;

/**
 * simule un feu du jeu, ie une projection mortelle résultant (généralement) 
 * de l'explosion d'une bombe. 
 * 
 * @author Vincent Labatut
 *
 */
public class AiSimFire extends AiSimSprite
{
	/**
	 * crée une simulation du feu passé en paramètre,
	 * avec les propriétés passées en paramètres.
	 * 
	 * @param tile	case contenant le sprite
	 * @param throughBlocks	indique si le feu peut traverser les blocs
	 * @param throughBombs	indique si le feu peut traverser les bombes
	 * @param throughItems	indique si le feu peut traverser les items
	 */
	public AiSimFire(AiSimTile tile, double posX, double posY, double posZ,
			boolean throughBlocks, boolean throughBombs, boolean throughItems)
	{	super(tile,posX,posY,posZ);
		
		this.throughBlocks = throughBlocks;
		this.throughBombs = throughBombs;
		this.throughItems = throughItems;
	}	

	/**
	 * crée une simulation du feu passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param tile	case contenant le sprite
	 * @param sprite	sprite à simuler
	 */
	AiSimFire(AiFire sprite, AiSimTile tile)
	{	super(sprite,tile);
		
		this.throughBlocks = sprite.hasThroughBlocks();
		this.throughBombs = sprite.hasThroughBombs();
		this.throughItems = sprite.hasThroughItems();
	}

	/**
	 * construit une simulation du feu passé en paramètre,
	 * (donc une simple copie) et la place dans la case indiquée.
	 * 
	 * @param sprite	simulation à copier
	 * @param tile	simulation de la case devant contenir la copie
	 */
	public AiSimFire(AiSimFire sprite, AiSimTile tile)
	{	super(sprite,tile);
		
		throughBlocks = sprite.throughBlocks;
		throughBombs = sprite.throughBombs;
		throughItems = sprite.throughItems;
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
	
	/**
	 * teste si ce feu est capable de passer à travers les (certains) murs
	 * <b>ATTENTION :</b> cette méthode ne devrait pas être utilisée directement par l'IA,
	 * elle est destinée au calcul des modèles simulant l'évolution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	vrai si le feu traverse les murs
	 */
	public boolean hasThroughBlocks()
	{	return throughBlocks;	
	}

	/**
	 * teste si ce feu est capable de passer à travers les bombes
	 * <b>ATTENTION :</b> cette méthode ne devrait pas être utilisée directement par l'IA,
	 * elle est destinée au calcul des modèles simulant l'évolution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	vrai si le feu traverse les bombes
	 */
	public boolean hasThroughBombs()
	{	return throughBombs;	
	}

	/**
	 * teste si ce feu est capable de passer à travers les items
	 * <b>ATTENTION :</b> cette méthode ne devrait pas être utilisée directement par l'IA,
	 * elle est destinée au calcul des modèles simulant l'évolution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	vrai si le feu traverse les items
	 */
	public boolean hasThroughItems()
	{	return throughItems;
	}

	@Override
	public boolean isCrossableBy(AiSimSprite sprite)
	{	// par défaut, on bloque
		boolean result = false;
		
		// si le sprite considéré est un personnage : peut traverser le feu seulement s'il a une protection
		if(sprite instanceof AiSimHero)
		{	AiSimHero hero = (AiSimHero) sprite;
			result = hero.hasThroughFires();
		}
		
		// si c'est une bombe : peut traverser le feu seulement si elle n'explose pas à son contact
		else if(sprite instanceof AiSimHero)
		{	AiSimBomb bomb = (AiSimBomb) sprite;
			result = !bomb.hasExplosionTrigger();
		}
		
		return result;
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
	void finish()
	{	super.finish();
	}
}

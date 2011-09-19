package org.totalboumboum.ai.v200910.adapter.data;

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

import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.fire.Fire;

/**
 * représente un feu du jeu, ie une projection mortelle r�sultant (généralement) 
 * de l'explosion d'une bombe. 
 * 
 * @author Vincent Labatut
 *
 */
public class AiFire extends AiSprite<Fire>
{
	/**
	 * crée une représentation du feu passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param tile	case contenant le sprite
	 * @param sprite	sprite à représenter
	 */
	AiFire(AiTile tile, Fire sprite)
	{	super(tile,sprite);
		updateCollisions();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	void update(AiTile tile)
	{	super.update(tile);
		updateCollisions();
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
	 * teste si ce feu est capable de passer
	 * à travers les (certains) murs
	 * 
	 * @return	vrai si le feu traverse les murs
	 */
	public boolean hasThroughBlocks()
	{	return throughBlocks;	
	}

	/**
	 * teste si ce feu est capable de passer
	 * à travers les bombes
	 * 
	 * @return	vrai si le feu traverse les bombes
	 */
	public boolean hasThroughBombs()
	{	return throughBombs;	
	}

	/**
	 * teste si ce feu est capable de passer
	 * à travers les items
	 * 
	 * @return	vrai si le feu traverse les items
	 */
	public boolean hasThroughItems()
	{	return throughItems;
	}

	/**
	 * met à jour les diverse propriétés de ce feu
	 * li�e à la gestion des collisions
	 */
	private void updateCollisions()
	{	Sprite sprite = getSprite();
		StateAbility ability;
		// traverser les murs
		ability = sprite.modulateStateAbility(StateAbilityName.SPRITE_TRAVERSE_WALL);
		throughBlocks = ability.isActive();
		// traverser les bombes
		ability = sprite.modulateStateAbility(StateAbilityName.SPRITE_TRAVERSE_BOMB);
		throughBombs = ability.isActive();
		// traverser les items
		ability = sprite.modulateStateAbility(StateAbilityName.SPRITE_TRAVERSE_ITEM);
		throughItems = ability.isActive();
	}

	public boolean isCrossableBy(AiSprite<?> sprite)
	{	// par défaut, on bloque
		boolean result = false;
		// si le sprite considéré est un personnage
		if(sprite instanceof AiHero)
		{	AiHero hero = (AiHero) sprite;
			result = hero.hasThroughFires();
		}
		else if(sprite instanceof AiHero)
		{	AiBomb bomb = (AiBomb) sprite;
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

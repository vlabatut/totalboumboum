package org.totalboumboum.ai.v201112.adapter.data.internal;

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

import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.data.AiSprite;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.fire.Fire;

/**
 * représente un feu du jeu, ie une projection mortelle résultant (généralement) 
 * de l'explosion d'une bombe. 
 * 
 * @author Vincent Labatut
 *
 */
final class AiDataFire extends AiDataSprite<Fire> implements AiFire
{
	/**
	 * crée une représentation du feu passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param tile
	 * 		case contenant le sprite
	 * @param sprite
	 * 		sprite à représenter
	 */
	protected AiDataFire(AiDataTile tile, Fire sprite)
	{	super(tile,sprite);
		updateCollisions();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void update(AiDataTile tile, long elapsedTime)
	{	super.update(tile,elapsedTime);
		updateTime();
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

	@Override
	public boolean isCrossableBy(AiSprite sprite)
	{	// par défaut, on bloque
		boolean result = false;
		
		// si le sprite considéré est un personnage : peut traverser le feu seulement s'il a une protection
		if(sprite instanceof AiDataHero)
		{	AiDataHero hero = (AiDataHero) sprite;
			result = hero.hasThroughFires();
		}
		
		// si c'est une bombe : peut traverser le feu seulement si elle n'explose pas à son contact
		else if(sprite instanceof AiDataHero)
		{	AiDataBomb bomb = (AiDataBomb) sprite;
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
	 * met à jour le temps écoulé depuis que le feu existe
	 */
	private void updateTime()
	{	long elapsedTime = getTile().getZone().getElapsedTime();
		time = time + elapsedTime;
		//System.out.println(sprite.getId()+":"+time+"/"+burningDuration);
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

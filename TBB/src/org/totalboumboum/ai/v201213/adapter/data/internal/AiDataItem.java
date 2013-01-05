package org.totalboumboum.ai.v201213.adapter.data.internal;

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

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiSprite;
import org.totalboumboum.ai.v201213.adapter.data.AiStopType;
import org.totalboumboum.engine.content.feature.Contact;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.Orientation;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.feature.TilePosition;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.action.Circumstance;
import org.totalboumboum.engine.content.feature.action.GeneralAction;
import org.totalboumboum.engine.content.feature.action.appear.GeneralAppear;
import org.totalboumboum.engine.content.feature.action.movelow.GeneralMoveLow;
import org.totalboumboum.engine.content.sprite.item.Item;

/**
 * Représente un item du jeu, ie un bonus ou un malus que le joueur peut ramasser.
 * un item est caractérisé par son type, représentant le pouvoir apporté (ou enlevé)
 * par l'item. Ce type est représentée par une valeur de type AiItemType.
 * 
 * @author Vincent Labatut
 */
final class AiDataItem extends AiDataSprite<Item> implements AiItem
{	
	/**
	 * Crée une représentation de l'item passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param tile
	 * 		Case contenant le sprite.
	 * @param sprite
	 * 		Sprite à représenter.
	 */
	protected AiDataItem(AiDataTile tile, Item sprite)
	{	super(tile,sprite);
		
		initType();
		initStrength();
		initContagious();
		updateDurations();
		updateCollisions();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void update(AiDataTile tile, long elapsedTime)
	{	super.update(tile,elapsedTime);
		
		updateDurations();
		updateCollisions();
	}

	/////////////////////////////////////////////////////////////////
	// TYPE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Type d'item représenté */
	private AiItemType type;
	
	@Override
	public AiItemType getType()
	{	return type;	
	}
	
	/**
	 * Initialise le type de l'item représenté.
	 */
	private void initType()
	{	Item item = getSprite();
		type = AiItemType.makeItemType(item.getItemName());		
	}
	
	/////////////////////////////////////////////////////////////////
	// STRENGTH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Force de l'item */
	private double strength;

	/**
	 * Initialise la force de cet item.
	 */
	private void initStrength()
	{	if(type==AiItemType.ANTI_BOMB || type==AiItemType.EXTRA_BOMB || type==AiItemType.GOLDEN_BOMB || type==AiItemType.NO_BOMB)
		{	StateAbility ability = sprite.getCurrentItemAbility(StateAbilityName.HERO_BOMB_NUMBER);
			strength = ability.getStrength();
		}
		else if(type==AiItemType.ANTI_FLAME || type==AiItemType.EXTRA_FLAME || type==AiItemType.GOLDEN_FLAME || type==AiItemType.NO_FLAME)
		{	StateAbility ability = sprite.getCurrentItemAbility(StateAbilityName.HERO_BOMB_RANGE);
			strength = ability.getStrength();
		}
		else if(type==AiItemType.ANTI_SPEED || type==AiItemType.EXTRA_SPEED || type==AiItemType.GOLDEN_SPEED || type==AiItemType.NO_SPEED)
		{	StateAbility ability = sprite.getCurrentItemAbility(StateAbilityName.HERO_WALK_SPEED_MODULATION);
			strength = ability.getStrength();
		}
		else
			strength = 0;
	}
	
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

	/** 
	 * Met jour les différentes caractéristiques de ce bloc
	 * concernant la gestion des collisions avec les autres sprites.
	 */
	private void updateCollisions()
	{	Item sprite = getSprite();
		
		// bloque les bombes
		{	GeneralAction generalAction = new GeneralMoveLow();
			generalAction.addActor(Role.BOMB);
			generalAction.addDirection(Direction.RIGHT);
			Circumstance actorCircumstance = new Circumstance();
			actorCircumstance.addContact(Contact.COLLISION);
			actorCircumstance.addOrientation(Orientation.FACE);
			actorCircumstance.addTilePosition(TilePosition.NEIGHBOR);
			Circumstance targetCircumstance = new Circumstance();
			List<AbstractAbility> actorProperties = new ArrayList<AbstractAbility>();
			List<AbstractAbility> targetProperties = new ArrayList<AbstractAbility>();
			boolean temp = sprite.isThirdPreventing(generalAction,actorProperties,targetProperties,actorCircumstance,targetCircumstance);
			if(temp)
			{	StateAbility ability = new StateAbility(StateAbilityName.SPRITE_TRAVERSE_ITEM);
				actorProperties.add(ability);
				temp = sprite.isThirdPreventing(generalAction,actorProperties,targetProperties,actorCircumstance,targetCircumstance);
				if(temp)
					stopBombs = AiStopType.STRONG_STOP;
				else
					stopBombs = AiStopType.WEAK_STOP;
			}
			else
				stopBombs = AiStopType.NO_STOP;
		}

		// bloque le feu
		{	GeneralAction generalAction = new GeneralAppear();
			generalAction.addActor(Role.FIRE);
			generalAction.addDirection(Direction.NONE);
			Circumstance actorCircumstance = new Circumstance();
			actorCircumstance.addContact(Contact.INTERSECTION);
			actorCircumstance.addOrientation(Orientation.NEUTRAL);
			actorCircumstance.addTilePosition(TilePosition.SAME);
			Circumstance targetCircumstance = new Circumstance();
			List<AbstractAbility> actorProperties = new ArrayList<AbstractAbility>();
			List<AbstractAbility> targetProperties = new ArrayList<AbstractAbility>();
			boolean temp = sprite.isThirdPreventing(generalAction,actorProperties,targetProperties,actorCircumstance,targetCircumstance);
			if(temp)
			{	StateAbility ability = new StateAbility(StateAbilityName.SPRITE_TRAVERSE_ITEM);
				actorProperties.add(ability);
				temp = sprite.isThirdPreventing(generalAction,actorProperties,targetProperties,actorCircumstance,targetCircumstance);
				if(temp)
					stopFires = AiStopType.STRONG_STOP;
				else
					stopFires = AiStopType.WEAK_STOP;
			}
			else
				stopFires = AiStopType.NO_STOP;
		}
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
	
	/**
	 * Initialise l'indicateur de contagion.
	 */
	private void initContagious()
	{	Item item = getSprite();
		StateAbility ability = item.getAbility(StateAbilityName.ITEM_CONTAGION_MODE);
		float mode = ability.getStrength();
		contagious = mode!=StateAbilityName.ITEM_CONTAGION_NONE;
	}
	
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
	
	/**
	 * Initialise les champs relatifs aux durées de l'effet
	 * de cet item.
	 */
	private void updateDurations()
	{	Item item = getSprite();
		// NOTE we suppose the temporary item makes the player blink
		StateAbility ability = item.getOriginalItemAbility(StateAbilityName.SPRITE_TWINKLE_COLOR);
		if(ability.isActive())
		{	normalDuration = (long)ability.getTime();
			limitedDuration = normalDuration>-1;
			ability = item.getCurrentItemAbility(StateAbilityName.SPRITE_TWINKLE_COLOR);
			long remainingTime = (long)ability.getTime();
			elapsedTime = normalDuration - remainingTime;
//if(item.getName().equals("No bomb"))
//	System.out.println("normalDuration="+normalDuration+" limitedDuration="+limitedDuration+" elapsedTime="+elapsedTime);
		}
		else
		{	normalDuration = -1;
			limitedDuration = false;
			elapsedTime = -1;
		}
	}
	
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

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append("Item: [");
		result.append(super.toString());
		result.append(" - type: "+type);
		result.append(" - contagious: "+contagious);
		result.append(" - limited: "+limitedDuration);
		result.append(" ("+elapsedTime+"/"+normalDuration+")");
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

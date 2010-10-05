package org.totalboumboum.ai.v201011.adapter.data;

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

import java.util.ArrayList;
import java.util.List;

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
 * représente un item du jeu, ie un bonus ou un malus que le joueur peut ramasser.
 * un item est caractérisé par son type, représentant le pouvoir apporté (ou enlevé)
 * par l'item. Ce type est représentée par une valeur de type AiItemType.
 * 
 * @author Vincent Labatut
 *
 */
public class AiItem extends AiSprite<Item>
{	
	/**
	 * crée une représentation de l'item passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param tile	case contenant le sprite
	 * @param sprite	sprite à représenter
	 */
	AiItem(AiTile tile, Item sprite)
	{	super(tile,sprite);
		initType();
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
	// TYPE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** type d'item représenté */
	private AiItemType type;
	
	/**
	 * renvoie le type de l'item représenté
	 * 
	 * @return	le type de l'item
	 */
	public AiItemType getType()
	{	return type;	
	}
	
	/**
	 * initialise le type de l'item représenté
	 */
	private void initType()
	{	Item item = getSprite();
		type = AiItemType.makeItemType(item.getItemName());		
	}
	
	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si ce bloc laisse passer les joueurs */
	private AiStopType stopBombs;
	/** indique si ce bloc laisse passer le feu */
	private AiStopType stopFires;

	/** 
	 * met jour les différentes caractéristiques de ce bloc
	 * concernant la gestion des collisions avec les autres sprites
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
	public boolean isCrossableBy(AiSprite<?> sprite)
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
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append("Item: [");
		result.append(super.toString());
		result.append(" - type: "+type);
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

package org.totalboumboum.ai.v201213.adapter.data.internal;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
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
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.feature.action.appear.GeneralAppear;
import org.totalboumboum.engine.content.feature.action.consume.SpecificConsume;
import org.totalboumboum.engine.content.feature.action.movelow.GeneralMoveLow;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.sprite.block.Block;

/**
 * Représente un bloc du jeu, c'est à dire généralement un mur
 * (pouvant être détruit ou pas). 
 * 
 * @author Vincent Labatut
 */
final class AiDataBlock extends AiDataSprite<Block> implements AiBlock
{
	/**
	 * Crée une représentation du bloc passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param tile
	 * 		Case contenant le sprite.
	 * @param sprite
	 * 		Sprite à représenter.
	 */
	protected AiDataBlock(AiDataTile tile, Block sprite)
	{	super(tile,sprite);
		updateDestructible();
		updateCollisions();
	}	
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void update(AiDataTile tile, long elapsedTime)
	{	super.update(tile,elapsedTime);
		updateDestructible();
		updateCollisions();
	}

	/////////////////////////////////////////////////////////////////
	// DESTRUCTIBLE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indique si ce bloc peut être détruit par une bombe */
	private boolean destructible;

	/** 
	 * Met jour l'indicateur de destructibilité.
	 */
	private void updateDestructible()
	{	Block sprite = getSprite();
		SpecificAction specificAction = new SpecificConsume(sprite);
		destructible = !sprite.isTargetPreventing(specificAction);
	}

	@Override
	public boolean isDestructible()
	{	return destructible;		
	}

	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indique si ce bloc laisse passer les joueurs */
	private AiStopType stopHeroes;
	/** Indique si ce bloc laisse passer le feu */
	private AiStopType stopFires;
	
	@Override
	public AiStopType hasStopHeroes()
	{	return stopHeroes;
	}
	
	@Override
	public AiStopType hasStopFires()
	{	return stopFires;
	}
	
	/** 
	 * Met jour les différentes caractéristiques de ce bloc
	 * concernant la gestion des collisions avec les autres sprites.
	 */
	private void updateCollisions()
	{	Block sprite = getSprite();
		GestureName gestureName = GestureName.STANDING;
		
		// bloque les personnages
		{	GeneralAction generalAction = new GeneralMoveLow();
			generalAction.addActor(Role.HERO);
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
			{	StateAbility ability = new StateAbility(StateAbilityName.SPRITE_TRAVERSE_WALL);
				actorProperties.add(ability);
				ability.setStrength(1);
				temp = sprite.isThirdPreventing(generalAction,actorProperties,targetProperties,actorCircumstance,targetCircumstance);
				if(temp)
					stopHeroes = AiStopType.STRONG_STOP;
				else
					stopHeroes = AiStopType.WEAK_STOP;
			}
			else
				stopHeroes = AiStopType.NO_STOP;
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
			boolean temp = sprite.wouldThirdPreventing(generalAction,actorProperties,targetProperties,actorCircumstance,targetCircumstance,gestureName);
			if(temp)
			{	StateAbility ability = new StateAbility(StateAbilityName.SPRITE_TRAVERSE_WALL);
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
		{	AiHero hero = (AiHero) sprite;
			if(hero.getTile().equals(getTile())) //simplification
				result = true;
			else if(stopHeroes==AiStopType.NO_STOP)
				result = true;
			else if(stopHeroes==AiStopType.WEAK_STOP)
				result = hero.hasThroughBlocks();
			else if(stopHeroes==AiStopType.STRONG_STOP)
				result = false;
		}
		// si le sprite considéré est un feu
		else if(sprite instanceof AiFire)
		{	AiFire fire = (AiFire) sprite;
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

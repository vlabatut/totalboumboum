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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiFloor;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiItemType;
import org.totalboumboum.ai.v201011.adapter.data.AiSprite;
import org.totalboumboum.ai.v201011.adapter.data.AiState;
import org.totalboumboum.ai.v201011.adapter.data.AiStateName;
import org.totalboumboum.ai.v201011.adapter.data.AiStopType;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class AiModel
{	
	public AiModel()
	{	considerBombDisappearance = false;
		
	}
	
	public AiModel(boolean considerBombDisappearance)
	{	this.considerBombDisappearance = considerBombDisappearance;
		
	}
	
	/////////////////////////////////////////////////////////////////
	// SETTINGS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean considerBombDisappearance;
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public AiZone predictZone(AiZone current, HashMap<AiSprite,AiState> specifiedStates)
	{	// create a new, empty zone
		AiSimZone result = new AiSimZone(current);
		
		// list all sprites
		List<AiSprite> sprites = new ArrayList<AiSprite>();
		sprites.addAll(current.getBlocks());
		sprites.addAll(current.getBombs());
		sprites.addAll(current.getFires());
		sprites.addAll(current.getFloors());
		sprites.addAll(current.getRemainingHeroes());
		sprites.addAll(current.getItems());
		
		// list their incoming state : new (specified) or same than before,
		// also process the minimal time needed for a sprite state change
		HashMap<AiSprite,AiSimState> statesMap = new HashMap<AiSprite, AiSimState>();
		long minTime = Long.MAX_VALUE;
		for(AiSprite sprite: sprites)
		{	AiSimState state;
			// get the specified new state for this sprite
			AiState temp = specifiedStates.get(sprite);
			if(temp!=null)
				state = new AiSimState(temp);
			// or get an automatically processed one if no specified state is available
			else
				state = processNewState(sprite);
			// add to map
			statesMap.put(sprite,state);
			// then process the time remaining before the next state change
			if(state.getName()!=AiStateName.ENDED && state.getName()!=AiStateName.STANDING)
			{	long changeTime = processChangeTime(current,sprite,state);
				if(changeTime>0 && changeTime<minTime) //zero means there's nothing to do, eg: moving towards an obstacle
					minTime = changeTime;
			}
		}
		
		// apply events for the resulting minimal time
		for(Entry<AiSprite,AiSimState> entry: statesMap.entrySet())
		{	AiSprite sprite0 = entry.getKey();
			AiSimState state = entry.getValue();
			applyState(sprite0,state,result,minTime);
		}
		
		// update the resulting zone
		
		return result;
	}
	
	/**
	 * calcule le nouvel état du sprite passé en paramètre,
	 * quand aucun état n'a été explicitement spécifié pour lui.
	 * 
	 * @param sprite	le sprite à traiter
	 * @return	son nouvel état
	 */
	private AiSimState processNewState(AiSprite sprite)
	{	// previous state
		AiState state0 = sprite.getState();
		long time0 = state0.getTime();
		AiStateName name0 = state0.getName();
		Direction direction0 = state0.getDirection();
		// result
		AiStateName name = name0;
		Direction direction = direction0;
		long time = time0;
		
		// sprite might have to disappear (block, bomb, fire, hero, item) after finishing burning
		if(name0==AiStateName.BURNING)
		{	long burningDuration = sprite.getBurningDuration();
			if(time0>=burningDuration) //NOTE problem for re-spawning sprites (but it's only an approximation, after all...)
			{	name = AiStateName.ENDED;
				direction = Direction.NONE;
				time = 0;
			}
		}
		
		// a bomb might have to explode
		else if(sprite instanceof AiBomb)
		{	AiBomb bomb = (AiBomb) sprite;
			long normalDuration = bomb.getNormalDuration();
			if(normalDuration>0) //only for time bombs
			{	if(time0>=normalDuration)
				{	name = AiStateName.BURNING;
					direction = Direction.NONE;
					time = 0;
				}
			}
		}
		
		AiSimState result = new AiSimState(name,direction,time);
		return result;
	}
	
	/**
	 * calcule combien de temps il va falloir au sprite spécifié pour sortir
	 * de l'état qui lui a été assigné. si le sprite brule, il s'agit de savoir pendant
	 * combien de temps encore. s'il se déplace, il s'agit de savoir combien de
	 * temps il va lui falloir pour changer de case. s'il ne fait rien, il n'y a
	 * pas de limite particulière à son activité.
	 * 
	 * @param current	la zone courante
	 * @param sprite	le sprite à traiter
	 * @param state	le nouvel état de ce sprite
	 * @return	la durée pendant laquelle le sprite va rester à cet état
	 */
	private long processChangeTime(AiZone current, AiSprite sprite, AiState state)
	{	long result = Long.MAX_VALUE;
		AiStateName name = state.getName();
		
		// sprite burns: how long before it finishes burning?
		if(name==AiStateName.BURNING)
		{	long burningDuration = sprite.getBurningDuration();
			long elapsedTime = state.getTime();
			result = burningDuration - elapsedTime;
		}
		
		// sprite ended : should not be considered anymore
		else if(name==AiStateName.ENDED)
		{	result = Long.MAX_VALUE;
		}

		// sprite moves (on the ground or in the air): how long before it reaches the next tile
		else if(name==AiStateName.FLYING || name==AiStateName.MOVING)
		{	Direction direction = state.getDirection();
			if(direction==Direction.NONE)
				result = Long.MAX_VALUE;
			else
			{	//NOTE simplification here: we suppose the levels are all grids, 
				// meaning at least one coordinate is at the center of a tile
				int dir[] = direction.getIntFromDirection();
				AiTile tile = sprite.getTile();
				double tileSize = tile.getSize();
				double posX = sprite.getPosX();
				double posY = sprite.getPosY();
				double tileX = tile.getPosX();
				double tileY = tile.getPosY();
				AiTile neighborTile = tile.getNeighbor(direction);
				double offset = 0;
				if(neighborTile.isCrossableBy(sprite)) //deal with obstacles
					offset = tileSize/2;
				double goalX = current.normalizePositionX(tileX+dir[0]*offset);
				double goalY = current.normalizePositionY(tileY+dir[1]*offset);
				double manDist = Math.abs(posX-goalX)+Math.abs(posY-goalY);
				result = (long)(manDist/sprite.getCurrentSpeed());
			}
		}
		
		// sprites just stands doing nothing special
		else if(name==AiStateName.STANDING)
		{	result = Long.MAX_VALUE;
		}
	
		return result;
	}
	
	/**
	 * calcule l'état du sprite à la fin de la durée spécifiée,
	 * à partir de l'état courant.
	 * 
	 * @param sprite0	le sprite concerné (sa représentation initiale)
	 * @param state	le nouvel état de ce sprite (à appliquer jusqu'à la fin du temps imparti)
	 * @param result	la zone à mettre à jour
	 * @param time	la durée à prendre en compte
	 */
	private void applyState(AiSprite sprite0, AiSimState state, AiSimZone result, long duration)
	{	// previous state
		AiState state0 = sprite0.getState();
		AiTile tile0 = sprite0.getTile();
		int line0 = tile0.getLine();
		int col0 = tile0.getCol();
		long burningDuration = sprite0.getBurningDuration();
		double currentSpeed = sprite0.getCurrentSpeed();
		double posX0 = sprite0.getPosX();
		double posY0 = sprite0.getPosY();
		double posZ0 = sprite0.getPosZ();
		
		// next state
		AiStateName name = state.getName();
		long time = state.getTime();
		Direction direction = state.getDirection();
		int line = line0;
		int col = col0;
		double posX = posX0;
		double posY = posY0;
		double posZ = posZ0;
		AiSimTile tile = result.getTile(line,col);
		AiSimSprite sprite;
		
		if(name==AiStateName.BURNING)
		{	time = time + duration;
			state = new AiSimState(name,direction,time);
			sprite = applyStateSprite(sprite0,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
			result.addSprite(sprite);
		}
		
		else if(name==AiStateName.ENDED)
		{	// nothing to do
		}
		
		else if(name==AiStateName.FLYING || name==AiStateName.MOVING)
		{	
			
		}
		
		else if(name==AiStateName.STANDING)
		{	time = time + duration;
			state = new AiSimState(name,direction,time);
			sprite = applyStateSprite(sprite0,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
			result.addSprite(sprite);
		}
	}
	
	/**
	 * crée une nouvelle version du sprite en appliquant le nouvel état au sprite existant
	 * 
	 * @param sprite	sprite existant
	 * @param tile	case du sprite existant
	 * @param duration	durée du nouvel état
	 * @param posX	nouvelle abscisse
	 * @param posY nouvelle ordonnée
	 * @param posZ nouvelle altitude
	 * @param state	nouvel état
	 * @param burningDuration	nouvelle durée de combustion
	 * @param currentSpeed	nouvelle vitesse courante
	 * @return	le sprite correspondant aux nouvelles descriptions passées en paramètres
	 */
	private AiSimSprite applyStateSprite(AiSprite sprite, AiSimTile tile, long duration, 
			double posX, double posY, double posZ, 
			AiSimState state, long burningDuration, double currentSpeed)
	{	AiSimSprite result = null;
		
		if(sprite instanceof AiBlock)
		{	AiBlock block = (AiBlock)sprite;
			result = applyStateBlock(block,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
		}
		else if(sprite instanceof AiBomb)
		{	AiBomb bomb = (AiBomb)sprite;
			result = applyStateBomb(bomb,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
		}
		else if(sprite instanceof AiFire)
		{	AiFire fire = (AiFire)sprite;
			result = applyStateFire(fire,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
		}
		else if(sprite instanceof AiFloor)
		{	AiFloor floor = (AiFloor)sprite;
			result = applyStateFloor(floor,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
		}
		else if(sprite instanceof AiHero)
		{	AiHero hero = (AiHero)sprite;
			result = applyStateHero(hero,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
		}
		else if(sprite instanceof AiItem)
		{	AiItem item = (AiItem)sprite;
			result = applyStateItem(item,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
		}
	
		return result;
	}
	
	/**
	 * crée une nouvelle version du block en appliquant le nouvel état au sprite existant
	 * 
	 * @param sprite	block existant
	 * @param tile	case du sprite existant
	 * @param duration	durée du nouvel état
	 * @param posX	nouvelle abscisse
	 * @param posY nouvelle ordonnée
	 * @param posZ nouvelle altitude
	 * @param state	nouvel état
	 * @param burningDuration	nouvelle durée de combustion
	 * @param currentSpeed	nouvelle vitesse courante
	 * @return	le block correspondant aux nouvelles descriptions passées en paramètres
	 */
	private AiSimBlock applyStateBlock(AiBlock block, AiSimTile tile, long duration,
			double posX, double posY, double posZ, 
			AiSimState state, long burningDuration, double currentSpeed)
	{	boolean destructible = block.isDestructible();
		AiStopType stopHeroes = block.hasStopHeroes();
		AiStopType stopFires = block.hasStopFires();
		AiSimBlock result = new AiSimBlock(tile,posX,posY,posZ,state,burningDuration,currentSpeed,
				destructible,stopHeroes,stopFires);
		return result;
	}

	/**
	 * crée une nouvelle version de la bombe en appliquant le nouvel état au sprite existant
	 * 
	 * @param bombe	bombe existant
	 * @param tile	case du sprite existant
	 * @param duration	durée du nouvel état
	 * @param posX	nouvelle abscisse
	 * @param posY nouvelle ordonnée
	 * @param posZ nouvelle altitude
	 * @param state	nouvel état
	 * @param burningDuration	nouvelle durée de combustion
	 * @param currentSpeed	nouvelle vitesse courante
	 * @return	la bombe correspondant aux nouvelles descriptions passées en paramètres
	 */
	private AiSimBomb applyStateBomb(AiBomb bomb, AiSimTile tile, long duration,
			double posX, double posY, double posZ, 
			AiSimState state, long burningDuration, double currentSpeed)
	{	boolean countdownTrigger = bomb.hasCountdownTrigger();
		boolean remoteControlTrigger = bomb.hasRemoteControlTrigger();
		boolean explosionTrigger = bomb.hasExplosionTrigger();
		long normalDuration = bomb.getNormalDuration();
		long explosionDuration = bomb.getExplosionDuration();
		long latencyDuration = bomb.getLatencyDuration();
		float failureProbability = bomb.getFailureProbability();
		AiStopType stopHeroes = bomb.hasStopHeroes();
		AiStopType stopFires = bomb.hasStopFires();
		boolean throughItems = bomb.hasThroughItems();
		int range = bomb.getRange();
		boolean penetrating = bomb.isPenetrating();
		PredefinedColor color = bomb.getColor();
		boolean working = bomb.isWorking();
		long time = bomb.getTime() + duration;
		
		AiSimBomb result = new AiSimBomb(tile,posX,posY,posZ,state,burningDuration,currentSpeed,
				countdownTrigger,remoteControlTrigger,explosionTrigger,
				normalDuration,explosionDuration,latencyDuration,failureProbability,
				stopHeroes,stopFires,throughItems,range,penetrating,color,working,time);
		return result;
	}

	/**
	 * crée une nouvelle version du feu en appliquant le nouvel état au sprite existant
	 * 
	 * @param feu	feu existant
	 * @param tile	case du sprite existant
	 * @param duration	durée du nouvel état
	 * @param posX	nouvelle abscisse
	 * @param posY nouvelle ordonnée
	 * @param posZ nouvelle altitude
	 * @param state	nouvel état
	 * @param burningDuration	nouvelle durée de combustion
	 * @param currentSpeed	nouvelle vitesse courante
	 * @return	le feu correspondant aux nouvelles descriptions passées en paramètres
	 */
	private AiSimFire applyStateFire(AiFire fire, AiSimTile tile, long duration,
			double posX, double posY, double posZ, 
			AiSimState state, long burningDuration, double currentSpeed)
	{	boolean throughBlocks = fire.hasThroughBlocks();
		boolean throughBombs = fire.hasThroughBombs();
		boolean throughItems = fire.hasThroughItems();
		AiSimFire result = new AiSimFire(tile,posX,posY,posZ,state,burningDuration,currentSpeed,
			throughBlocks,throughBombs,throughItems);
		return result;
	}

	/**
	 * crée une nouvelle version du sol en appliquant le nouvel état au sprite existant
	 * 
	 * @param sol	sol existant
	 * @param tile	case du sprite existant
	 * @param duration	durée du nouvel état
	 * @param posX	nouvelle abscisse
	 * @param posY nouvelle ordonnée
	 * @param posZ nouvelle altitude
	 * @param state	nouvel état
	 * @param burningDuration	nouvelle durée de combustion
	 * @param currentSpeed	nouvelle vitesse courante
	 * @return	le sol correspondant aux nouvelles descriptions passées en paramètres
	 */
	private AiSimFloor applyStateFloor(AiFloor floor, AiSimTile tile, long duration,
			double posX, double posY, double posZ, 
			AiSimState state, long burningDuration, double currentSpeed)
	{	AiSimFloor result = new AiSimFloor(tile,posX,posY,posZ,state,burningDuration,currentSpeed);
		return result;
	}

	/**
	 * crée une nouvelle version du personnage en appliquant le nouvel état au sprite existant
	 * 
	 * @param personnage	personnage existant
	 * @param tile	case du sprite existant
	 * @param duration	durée du nouvel état
	 * @param posX	nouvelle abscisse
	 * @param posY nouvelle ordonnée
	 * @param posZ nouvelle altitude
	 * @param state	nouvel état
	 * @param burningDuration	nouvelle durée de combustion
	 * @param currentSpeed	nouvelle vitesse courante
	 * @return	le personnage correspondant aux nouvelles descriptions passées en paramètres
	 */
	private AiSimHero applyStateHero(AiHero hero, AiSimTile tile, long duration,
			double posX, double posY, double posZ, 
			AiSimState state, long burningDuration, double currentSpeed)
	{	int bombRange = hero.getBombRange();
		int bombNumber = hero.getBombNumber();
		int bombCount = hero.getBombCount();
		boolean throughBlocks = hero.hasThroughBlocks();
		boolean throughBombs = hero.hasThroughBombs();
		boolean throughFires = hero.hasThroughFires();
		PredefinedColor color = hero.getColor();
		double walkingSpeed = hero.getWalkingSpeed();
		
		AiSimHero result = new AiSimHero(tile,posX,posY,posZ,state,burningDuration,currentSpeed,
			bombRange,bombNumber,bombCount,
			throughBlocks,throughBombs,throughFires,
			color,walkingSpeed);
		return result;
	}

	/**
	 * crée une nouvelle version de l'item en appliquant le nouvel état au sprite existant
	 * 
	 * @param item	item existant
	 * @param tile	case du sprite existant
	 * @param duration	durée du nouvel état
	 * @param posX	nouvelle abscisse
	 * @param posY nouvelle ordonnée
	 * @param posZ nouvelle altitude
	 * @param state	nouvel état
	 * @param burningDuration	nouvelle durée de combustion
	 * @param currentSpeed	nouvelle vitesse courante
	 * @return	le item correspondant aux nouvelles descriptions passées en paramètres
	 */
	private AiSimItem applyStateItem(AiItem item, AiSimTile tile, long duration,
			double posX, double posY, double posZ, 
			AiSimState state, long burningDuration, double currentSpeed)
	{	AiItemType type = item.getType();
		AiStopType stopBombs = item.hasStopBombs();
		AiStopType stopFires = item.hasStopFires();
		AiSimItem result = new AiSimItem(tile,posX,posY,posZ,state,burningDuration,currentSpeed,
			type,stopBombs,stopFires);
		return result;
	}
}

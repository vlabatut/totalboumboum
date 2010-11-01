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

import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiSprite;
import org.totalboumboum.ai.v201011.adapter.data.AiState;
import org.totalboumboum.ai.v201011.adapter.data.AiStateName;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

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
	{	AiSimZone result = new AiSimZone(current);
		
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
		{	// get the specified new state for this sprite
			AiState temp = specifiedStates.get(sprite);
			AiSimState state = new AiSimState(temp);
			// or get an automatically processed one if no specified state is available
			if(state==null)
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
			AiState state = entry.getValue();
			applyState(sprite0,state,result);
		}
		
		return result;
	}
	
	/**
	 * calcule le nouvel �tat du sprite pass� en param�tre,
	 * quand aucun �tat n'a �t� explicitement sp�cifi� pour lui.
	 * 
	 * @param sprite	le sprite � traiter
	 * @return	son nouvel �tat
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
	 * calcule combien de temps il va falloir au sprite sp�cifi� pour sortir
	 * de l'�tat qui lui a �t� assign�. si le sprite brule, il s'agit de savoir pendant
	 * combien de temps encore. s'il se d�place, il s'agit de savoir combien de
	 * temps il va lui falloir pour changer de case. s'il ne fait rien, il n'y a
	 * pas de limite particuli�re � son activit�.
	 * 
	 * @param current	la zone courante
	 * @param sprite	le sprite � traiter
	 * @param state	le nouvel �tat de ce sprite
	 * @return	la dur�e pendant laquelle le sprite va rester � cet �tat
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
	
	private void applyState(AiSprite sprite0, AiState state, AiSimZone result)
	{
		// TODO
	}
}

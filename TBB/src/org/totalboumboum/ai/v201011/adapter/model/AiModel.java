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
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiSprite;
import org.totalboumboum.ai.v201011.adapter.data.AiState;
import org.totalboumboum.ai.v201011.adapter.data.AiStateName;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.calculus.CalculusTools;

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
		HashMap<AiSprite,AiState> statesMap = new HashMap<AiSprite, AiState>();
		for(AiSprite sprite: sprites)
		{	AiState state = specifiedStates.get(sprite);
			if(state==null)
				state = processNewState(current,sprite);
			if(state.getName()!=AiStateName.ENDED && state.getName()!=AiStateName.STANDING)
			{	statesMap.put(sprite,state);
				long changeTime = processChangeTime(current,sprite,state);
				
			}
		}
		
		// specified ones
		// and unspecified ones
		
		// apply specified events
		for(AiEvent event: specifiedStates)
			applyEvent(event,current,result);
		
		// apply unspecified events
		for(int line=0;line<result.getHeight();line++)
			for(int col=0;col<result.getWidth();col++)
				predictTile(line,col,current,result,specifiedStates);
		
		return result;
	}
	
	private AiState processNewState(AiZone current, AiSprite sprite)
	{	AiStateName name;
		Direction direction;
		long time;
	
		// sprite might have to disappear (block, bomb, fire, hero, item) after finishing burning
		if()
		{
			
		}
		// a bomb might have to explode
		else if(sprite instanceof AiBomb)
		{	
			
		}
		// otherwise, we just keep on going with the same state
		else
		{	AiState temp = sprite.getState();
			name = temp.getName();
			direction = temp.getDirection();
			time = temp.getTime();
		}
		
		AiState result = new AiSimState(name,direction,time);
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
		AiState state0 = sprite.getState();
		AiStateName name0 = state0.getName();
		
		// sprite burns: how long before it finishes burning?
		if(name==AiStateName.BURNING)
		{	long burningDuration = sprite.getBurningDuration();
			// the sprite was already burning before
			if(name0==name)
			{	long elapsedTime = state0.getTime();
				result = burningDuration - elapsedTime;
			}
			// the sprite starts burning now
			else
				result = burningDuration;
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
			{	AiTile tile = sprite.getTile();
				double pos = 0;
				double goal = 0;
				if(direction.isHorizontal())
				{	pos = sprite.getPosX();
					goal = tile.getPosX();
				}
				else if(direction.isVertical())
				{	pos = sprite.getPosY();
					goal = tile.getPosY();
				}
				double dist = Math.abs(pos-goal);
				result = (long)(dist/sprite.getCurrentSpeed());
			}
		}
		
		// sprites justs stands doing nothing special
		else if(name==AiStateName.STANDING)
		{	result = Long.MAX_VALUE;
		}
	
		return result;
	}
	
	private void applyEvent(AiEvent event, AiZone current, AiSimZone result)
	{
		// TODO
	}
	
	private void predictTile(int line, int col, AiZone current,AiSimZone result, List<AiEvent> events)
	{
		// TODO
	}
	
}

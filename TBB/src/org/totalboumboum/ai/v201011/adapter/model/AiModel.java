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
		
		// list their incoming state : new (specified) or same than before
		HashMap<AiSprite,AiState> statesMap = new HashMap<AiSprite, AiState>();
		for(AiSprite sprite: sprites)
		{	AiState state = specifiedStates.get(sprite);
			if(state==null)
				state = sprite.getState();
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
	
	private long processChangeTime(AiZone current, AiSprite sprite, AiState state)
	{	long result = Long.MAX_VALUE;
		
		AiStateName name = state.getName();
		if(name==AiStateName.BURNING)
		{	if(sprite instanceof AiBomb)
				
			
		}
		// equivalent gesture: BURNING
		/** le sprite est en train de brûler */
		BURNING,
		// equivalent gesture: ENDED
		/** le sprite n'est plus en jeu */
		ENDED,
		// equivalent gesture: BOUNCING, JUMPING, LANDING, PUNCHED
		/** le sprite est en l'air (en train de sauter ou de rebondir sur les murs) */
		FLYING,
		// equivalent gesture: APPEARING, CRYING, ENTERING, EXULTING, OSCILLATING, OSCILLATING_FAILING, PREPARED, PUNCHING, STANDING, STANDING_FAILING, WAITING
		/** le sprite ne fait rien ou bien réalise une action qui ne nécessite pas de déplacement */ 
		STANDING,
		// equivalent gesture: PUSHING, SLIDING, SLIDING_FAILING, WALKING
		/** le sprite se déplace sur le sol */
		MOVING;
	
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

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

import java.util.HashMap;

import org.totalboumboum.ai.v201011.adapter.data.AiSprite;
import org.totalboumboum.ai.v201011.adapter.data.AiState;
import org.totalboumboum.ai.v201011.adapter.data.AiStateName;
import org.totalboumboum.ai.v201011.adapter.data.AiStopType;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
public final class AiModelTest
{
	public static void main(String args[])
	{	RoundVariables.scaledTileDimension = 100;
		AiSimZone zone;
		AiSimTile tile;
		AiSimState state;
		AiSimBlock block;
		AiSimFire firePrototype;
		AiSimHero hero;
		AiSimBomb bomb,bombPrototype;
		int bombRange=3,bombNumber=1,bombCount=0,id=0;
		double posX, posY, posZ=0,currentSpeed,walkingSpeed=100;
		float failureProbability=0;
		long burningDuration=100,normalDuration=1000,latencyDuration=10;
		boolean destructible,throughBlocks=false,throughBombs=false,throughFires=false,throughItems=false,countdownTrigger=true,remoteControlTrigger=false,explosionTrigger=true,penetrating=false,working=true;
		AiStopType stopHeroes, stopFires;
		PredefinedColor color; 

		// zone
		currentSpeed = 0;
		state = new AiSimState(AiStateName.STANDING,Direction.NONE,0);
		color = PredefinedColor.WHITE;
		zone = new AiSimZone(7,7);
		
		// fire prototype
		firePrototype = new AiSimFire(id++,null,0,0,0,
				state,burningDuration,0,
				false,true,false);
		
		// bomb prototype
		stopHeroes = AiStopType.WEAK_STOP;
		stopFires = AiStopType.WEAK_STOP;
		state = new AiSimState(AiStateName.STANDING,Direction.NONE,0);
		color = PredefinedColor.WHITE;
		bombPrototype = new AiSimBomb(id++,null,0,0,0,
				state,burningDuration,0,walkingSpeed,
				countdownTrigger,remoteControlTrigger,explosionTrigger,
				normalDuration,latencyDuration,failureProbability,firePrototype, 
				stopHeroes,stopFires,throughItems,bombRange,penetrating,
				color,working,0);

		// hero
		tile = zone.getTile(1,1);
		posX = tile.getPosX();
		posY = tile.getPosY();
		state = new AiSimState(AiStateName.STANDING,Direction.NONE,0);
		color = PredefinedColor.WHITE;
		hero = new AiSimHero(id++,tile,posX,posY,posZ, 
				state,burningDuration,currentSpeed,
				bombPrototype,bombNumber,bombCount, 
				throughBlocks,throughBombs,throughFires, 
				color,walkingSpeed);
		zone.addHero(hero,true);

		// bomb
		stopHeroes = AiStopType.WEAK_STOP;
		stopFires = AiStopType.WEAK_STOP;
		tile = zone.getTile(5,5);
		posX = tile.getPosX();
		posY = tile.getPosY();
		state = new AiSimState(AiStateName.STANDING,Direction.NONE,0);
		color = PredefinedColor.WHITE;
		bomb = new AiSimBomb(id++,tile,posX,posY,posZ,state,
				burningDuration,currentSpeed,walkingSpeed,
				countdownTrigger,remoteControlTrigger,explosionTrigger,
				normalDuration,latencyDuration,failureProbability,firePrototype,
				stopHeroes,stopFires,throughItems,bombRange,penetrating,
				color,working,0);
		zone.addSprite(bomb);

		// hardwalls
		currentSpeed = 0;
		destructible = false;
		stopHeroes = AiStopType.STRONG_STOP;
		stopFires = AiStopType.STRONG_STOP;
		int lines[]={0,0,0,0,0,0,0,1,1,2,2,2,2,3,3,4,4,4,4,5,5,6,6,6,6,6,6,6};
		int cols[]= {0,1,2,3,4,5,6,0,6,0,2,4,6,0,6,0,2,4,6,0,6,0,1,2,3,4,5,6};
		for(int i=0;i<lines.length;i++)
		{	tile = zone.getTile(lines[i],cols[i]);
			posX = tile.getPosX();
			posY = tile.getPosY();
			state = new AiSimState(AiStateName.STANDING,Direction.NONE,0);
			block = new AiSimBlock(id++,tile,posX,posY,posZ,state,
					burningDuration,currentSpeed,destructible,stopHeroes,stopFires);
			zone.addSprite(block);
		}
		
		System.out.println("initial zone:\n"+zone);
		AiModel model = new AiModel(zone);
		
		HashMap<AiSprite, AiState> specifiedStates = new HashMap<AiSprite, AiState>();
		state = new AiSimState(AiStateName.MOVING,Direction.RIGHT,0);
		specifiedStates.put(hero,state);
		
		long duration = 0;
		int iteration = 0;
		do
		{	model.simulateOnce(specifiedStates);
			duration = model.getDuration();
			
			System.out.println("iteration "+iteration);
			System.out.println("duration:"+duration);
			System.out.println(model.getCurrentZone());
			
			iteration++;
		}
		while(duration!=0);
	}
}

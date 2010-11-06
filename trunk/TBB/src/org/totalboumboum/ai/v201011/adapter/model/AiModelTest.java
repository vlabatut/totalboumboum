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
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
@SuppressWarnings("unused")
public final class AiModelTest
{
/*	
	public static void main(String args[])
	{	RoundVariables.scaledTileDimension = 100;
		AiSimZone zone;
		AiSimTile tile;
		AiSimState state;
		AiSimBlock block;
		AiSimHero hero;
		AiSimBomb bomb;
		int bombRange=3,bombNumber=1,bombCount=0;
		double posX, posY, posZ=0,currentSpeed,walkingSpeed=100;
		float failureProbability=0;
		long burningDuration=100,normalDuration=1000,explosionDuration=100,latencyDuration=10;
		boolean destructible,throughBlocks=false,throughBombs=false,throughFires=false,throughItems=false,countdownTrigger=true,remoteControlTrigger=false,explosionTrigger=true,penetrating=false,working=true;
		AiStopType stopHeroes, stopFires;
		PredefinedColor color; 

		// zone
		currentSpeed = 0;
		state = new AiSimState(AiStateName.STANDING,Direction.NONE,0);
		color = PredefinedColor.WHITE;
		hero = new AiSimHero(null,0,0,0,state,burningDuration,currentSpeed,bombRange,bombNumber,bombCount,throughBlocks,throughBombs,throughFires,color,walkingSpeed);
		zone = new AiSimZone(7,7,hero);

		// hero
		tile = zone.getTile(1,1);
		posX = tile.getPosX();
		posY = tile.getPosY();
		state = new AiSimState(AiStateName.STANDING,Direction.NONE,0);
		color = PredefinedColor.WHITE;
		hero = new AiSimHero(tile,posX,posY,posZ,state,burningDuration,currentSpeed,
				bombRange,bombNumber,bombCount,
				throughBlocks,throughBombs,throughFires,color,walkingSpeed);
		zone.addSprite(hero);

		// bomb
		stopHeroes = AiStopType.WEAK_STOP;
		stopFires = AiStopType.WEAK_STOP;
		tile = zone.getTile(5,5);
		posX = tile.getPosX();
		posY = tile.getPosY();
		state = new AiSimState(AiStateName.STANDING,Direction.NONE,0);
		color = PredefinedColor.WHITE;
		bomb = new AiSimBomb(tile,posX,posY,posZ,state,burningDuration,currentSpeed, 
				countdownTrigger,remoteControlTrigger,explosionTrigger,normalDuration,explosionDuration,latencyDuration,failureProbability,
				stopHeroes,stopFires,throughItems,bombRange,penetrating,color,working,0);
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
			block = new AiSimBlock(tile,posX,posY,posZ,state,burningDuration,currentSpeed,destructible,stopHeroes,stopFires);
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
		{	model.predictZone(specifiedStates);
			duration = model.getDuration();
			
			System.out.println("iteration "+iteration);
			System.out.println("duration:"+duration);
			System.out.println(model.getCurrentZone());
			
			iteration++;
		}
		while(duration!=0);
	}
*/	
}

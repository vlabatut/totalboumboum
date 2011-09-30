package org.totalboumboum.ai.v201011.adapter.model;

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

import org.totalboumboum.ai.v201011.adapter.data.AiItemType;
import org.totalboumboum.ai.v201011.adapter.data.AiStateName;
import org.totalboumboum.ai.v201011.adapter.data.AiStopType;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * classe utilis�e pour tester les fonctionnalit�s de ce package,
 * en particulier que AiModel r�alise des simulations correctes.
 * on cr�e une zone fictive en faisant varier les sprites et leurs
 * actions, et on affiche le r�sultat des simulation pas-�-pas.<br/>
 * 
 * <b>Note :</b> la classe mod�le n'est pas d�finie pour repr�senter une zone
 * fictive comme ici, mais pour repr�senter la zone de jeu r�elle.
 * ceci explique les limites impos�es sur son constructeur.
 * 
 * @author Vincent Labatut
 *
 */
@SuppressWarnings("unused")
public final class AiModelTest
{
/*	public static void main(String args[])
	{	
		RoundVariables.scaledTileDimension = 100;
		AiSimZone zone;
		AiSimTile tile;
		AiSimState state;
		AiSimBlock block;
		AiSimFire firePrototype;
		AiSimHero hero;
		AiSimBomb bomb,bombPrototype;
		AiSimItem item;
		AiItemType itemType;
		int bombRange=3,bombNumber=1,bombCount=0,id=0;
		double posX, posY, posZ=0,currentSpeed,walkingSpeed=100;
		float failureProbability=0;
		long burningDuration,normalDuration=3000,latencyDuration=10;
		boolean destructible,throughBlocks=false,throughBombs=false,throughFires=false,throughItems=false,countdownTrigger=true,remoteControlTrigger=false,explosionTrigger=true,penetrating=false,working=true;
		AiStopType stopHeroes,stopFires,stopBombs;
		PredefinedColor color; 

		// zone
		currentSpeed = 0;
		state = new AiSimState(AiStateName.STANDING,Direction.NONE,0);
		color = PredefinedColor.WHITE;
		zone = new AiSimZone(7,7);
		zone.hiddenItemsCount = 1;
		zone.hiddenItemsCounts.put(AiItemType.EXTRA_BOMB,1);
		
		// fire prototype
		burningDuration = 200;
		firePrototype = new AiSimFire(id++,null,0,0,0,
				state,burningDuration,0,
				false,true,false);
		
		// bomb prototype
		burningDuration = 100;
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
		burningDuration = 100;
		tile = zone.getTile(1,1);
		posX = tile.getPosX();
		posY = tile.getPosY();
		state = new AiSimState(AiStateName.MOVING,Direction.RIGHT,0);
		color = PredefinedColor.WHITE;
		hero = new AiSimHero(id++,tile,posX,posY,posZ, 
				state,burningDuration,currentSpeed,
				bombPrototype,bombNumber,bombCount, 
				throughBlocks,throughBombs,throughFires, 
				color,walkingSpeed);
		zone.addHero(hero,true);

		// bomb
		burningDuration = 100;
		stopHeroes = AiStopType.WEAK_STOP;
		stopFires = AiStopType.WEAK_STOP;
		tile = zone.getTile(5,5);
		posX = tile.getPosX();
		posY = tile.getPosY();
		state = new AiSimState(AiStateName.STANDING,Direction.NONE,0);
		color = null;
		bomb = new AiSimBomb(id++,tile,posX,posY,posZ,state,
				burningDuration,currentSpeed,walkingSpeed,
				countdownTrigger,remoteControlTrigger,explosionTrigger,
				normalDuration,latencyDuration,failureProbability,firePrototype,
				stopHeroes,stopFires,throughItems,bombRange,penetrating,
				color,working,0);
		zone.addSprite(bomb);

		// item 1
		burningDuration = 100;
		stopBombs = AiStopType.WEAK_STOP;
		stopFires = AiStopType.WEAK_STOP;
		tile = zone.getTile(1,3);
		posX = tile.getPosX();
		posY = tile.getPosY();
		state = new AiSimState(AiStateName.STANDING,Direction.NONE,0);
		itemType = AiItemType.EXTRA_BOMB;
		item = new AiSimItem(id++,tile,posX,posY,posZ,state,
				burningDuration,0,itemType,stopBombs,stopFires);
		zone.addSprite(item);
		
		// item 2
//		burningDuration = 100;
//		stopBombs = AiStopType.WEAK_STOP;
//		stopFires = AiStopType.WEAK_STOP;
//		tile = zone.getTile(3,5);
//		posX = tile.getPosX();
//		posY = tile.getPosY();
//		state = new AiSimState(AiStateName.STANDING,Direction.NONE,0);
//		itemType = AiItemType.EXTRA_BOMB;
//		item = new AiSimItem(id++,tile,posX,posY,posZ,state,
//				burningDuration,0,itemType,stopBombs,stopFires);
//		zone.addSprite(item);

		// softwall
		burningDuration = 100;
		currentSpeed = 0;
		destructible = true;
		stopHeroes = AiStopType.WEAK_STOP;
		stopFires = AiStopType.WEAK_STOP;
		tile = zone.getTile(3,5);
		posX = tile.getPosX();
		posY = tile.getPosY();
		state = new AiSimState(AiStateName.STANDING,Direction.NONE,0);
		block = new AiSimBlock(id++,tile,posX,posY,posZ,state,
				burningDuration,currentSpeed,destructible,stopHeroes,stopFires);
		zone.addSprite(block);
		
		// hardwalls
		burningDuration = 100;
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
		
		// display initial zone
		System.out.println("initial zone:\n"+zone);
		AiModel model = new AiModel(zone);
		model.setSimulateItemsAppearing(true);
		
		// first simulation
		long duration = 0;
		int iteration = 0;
		do
		{	// process simulation
			model.simulate();
			duration = model.getDuration();
			// display result
			System.out.println("iteration "+iteration);
			System.out.println("duration:"+duration);
			System.out.println(model.getCurrentZone());
			// update iteration
			iteration++;
		}
		while(duration!=0);
		
		// change hero direction
		System.out.println("change hero direction: LEFT>DOWN");
		model.applyChangeHeroDirection(hero,Direction.DOWN);
		
		// simulate until the hero undergoes some change
		model.simulate(hero);
		System.out.println(model.getDuration());
		System.out.println(model.getCurrentZone());
		model.simulate(hero);
		System.out.println(model.getDuration());
		System.out.println(model.getCurrentZone());
		
		// drop a bomb
		System.out.println("hero drops a bomb");
		model.applyDropBomb(hero);
		
		// change hero direction
		System.out.println("change hero direction: DOWN>LEFT");
		model.applyChangeHeroDirection(hero,Direction.LEFT);
		
		// simulate
		model.simulate();
		System.out.println(model.getDuration());
		System.out.println(model.getCurrentZone());
		model.simulate();
		System.out.println(model.getDuration());
		System.out.println(model.getCurrentZone());
		
		// change hero direction
		System.out.println("change hero direction: LEFT>UP");
		model.applyChangeHeroDirection(hero,Direction.UP);
		
		// simulate
		model.simulate();
		System.out.println(model.getDuration());
		System.out.println(model.getCurrentZone());
		model.simulate();
		System.out.println(model.getDuration());
		System.out.println(model.getCurrentZone());
		model.simulate();
		System.out.println(model.getDuration());
		System.out.println(model.getCurrentZone());
	}
*/	
}
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
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
public final class AiModelTest
{
	
	public static void main(String args[])
	{
		AiSimZone zone = new AiSimZone(7,7);
		AiSimTile tile;
		AiSimState state;
		AiSimBlock block;
		AiSimHero hero;
		int bombRange=3,bombNumber=1,bombCount=0;
		double posX, posY, posZ=0,currentSpeed,walkingSpeed=100;
		long burningDuration=100;
		boolean destructible,throughBlocks=false,throughBombs=false,throughFires=false;
		AiStopType stopHeroes, stopFires;
		PredefinedColor color;
		
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
		
		// hero
		tile = zone.getTile(1,1);
		posX = tile.getPosX();
		posY = tile.getPosY();
		state = new AiSimState(AiStateName.STANDING,Direction.NONE,0);
		color = PredefinedColor.WHITE;
		hero = new AiSimHero(tile,posX,posY,posZ,state,burningDuration,currentSpeed,bombRange,bombNumber,bombCount,throughBlocks,throughBombs,throughFires,color,walkingSpeed);
		zone.addSprite(hero);
		
		System.out.println(zone);
	}
	
}

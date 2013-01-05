package org.totalboumboum.ai.v201112.adapter.test;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiItemType;
import org.totalboumboum.ai.v201112.adapter.model.full.AiSimHero;
import org.totalboumboum.ai.v201112.adapter.model.full.AiSimTile;
import org.totalboumboum.ai.v201112.adapter.model.full.AiSimZone;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Classe utilisée pour tester les fonctionnalités de l'API IA.
 * Cette classe permet d'initialiser une zone et une fausse ia.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public final class InitData
{
	/**
	 * Initialise la zone de manière à obtenir le résultat suivant :<br/>
	 * <pre>
	 *   0 1 2 3 4 5 6
	 *  ┌─┬─┬─┬─┬─┬─┬─┐
	 * 0│█│█│█│█│█│█│█│
	 *  ├─┼─┼─┼─┼─┼─┼─┤
	 * 1│█│☺│ │□│ │ │█│
	 *  ├─┼─┼─┼─┼─┼─┼─┤
	 * 2│█│ │█│ │█│ │█│
	 *  ├─┼─┼─┼─┼─┼─┼─┤
	 * 3│█│ │ │ │ │▒│█│
	 *  ├─┼─┼─┼─┼─┼─┼─┤
	 * 4│█│ │█│ │█│ │█│
	 *  ├─┼─┼─┼─┼─┼─┼─┤
	 * 5│█│ │ │ │ │●│█│
	 *  ├─┼─┼─┼─┼─┼─┼─┤
	 * 6│█│█│█│█│█│█│█│
	 *  └─┴─┴─┴─┴─┴─┴─┘
	 * </pre>
	 * @return 
	 * 		Zone
	 */
	public static AiSimZone initZone()
	{	// zone
		AiSimZone zone = new AiSimZone(7,7);
		int hiddenItemsCount = 1;
		HashMap<AiItemType, Integer> hiddenItemsCounts = new HashMap<AiItemType, Integer>();
		hiddenItemsCounts.put(AiItemType.EXTRA_BOMB,1);
		zone.setHidenItemCount(hiddenItemsCount,hiddenItemsCounts);
		
		// hero
		AiSimHero hero;
		{	AiSimTile tile = zone.getTile(1,1);
			int bombRange = 3;
			int bombNumber = 1;
			//state = new AiSimState(AiStateName.MOVING,Direction.RIGHT,0);
			PredefinedColor color = PredefinedColor.WHITE;
			hero = zone.createHero(tile,color,bombNumber,bombRange,true);
		}

		// bomb #1
		{	AiSimTile tile = zone.getTile(5,5);
			zone.createBomb(tile,hero);
		}

		// bomb #2
//		{	AiSimTile tile = zone.getTile(1,3);
//			zone.createBomb(tile,hero);
//		}
		
		// item 1
		{	AiSimTile tile = zone.getTile(1,3);
			AiItemType itemType = AiItemType.EXTRA_BOMB;
			zone.createItem(tile,itemType);
		}
		
		// item 2
//		{	AiSimTile tile = zone.getTile(3,5);
//			AiItemType itemType = AiItemType.EXTRA_BOMB;
//			zone.createItem(tile,itemType);
//		}

		// softwall
		{	AiSimTile tile = zone.getTile(3,5);
			boolean destructible = true;
			zone.createBlock(tile,destructible);
		}
		
		
		// hardwalls
		{	boolean destructible = false;
			int rows[]={0,0,0,0,0,0,0,1,1,2,2,2,2,3,3,4,4,4,4,5,5,6,6,6,6,6,6,6};
			int cols[]= {0,1,2,3,4,5,6,0,6,0,2,4,6,0,6,0,2,4,6,0,6,0,1,2,3,4,5,6};
			for(int i=0;i<rows.length;i++)
			{	AiSimTile tile = zone.getTile(rows[i],cols[i]);
				zone.createBlock(tile,destructible);
			}
		}
		
		return zone;
	}
	
	/**
	 * Initialise la zone de manière à obtenir le résultat suivant :<br/>
	 * <pre>
	 *   0 1 2 3 4 5 6
	 *  ┌─┬─┬─┬─┬─┬─┬─┐
	 * 0│█│█│█│█│█│█│█│
	 *  ├─┼─┼─┼─┼─┼─┼─┤
	 * 1│█│☻│□│ │▒│ │█│
	 *  ├─┼─┼─┼─┼─┼─┼─┤
	 * 2│█│ │█│ │█│ │█│
	 *  ├─┼─┼─┼─┼─┼─┼─┤
	 * 3│█│ │ │ │ │ │█│
	 *  ├─┼─┼─┼─┼─┼─┼─┤
	 * 4│█│ │█│ │█│ │█│
	 *  ├─┼─┼─┼─┼─┼─┼─┤
	 * 5│█│ │ │ │ │ │█│
	 *  ├─┼─┼─┼─┼─┼─┼─┤
	 * 6│█│█│█│█│█│█│█│
	 *  └─┴─┴─┴─┴─┴─┴─┘
	 * </pre>
	 * @return 
	 * 		Zone
	 */
	public static AiSimZone initZone2()
	{	// zone
		AiSimZone zone = new AiSimZone(7,7);
		int hiddenItemsCount = 1;
		HashMap<AiItemType, Integer> hiddenItemsCounts = new HashMap<AiItemType, Integer>();
		hiddenItemsCounts.put(AiItemType.EXTRA_BOMB,1);
		zone.setHidenItemCount(hiddenItemsCount,hiddenItemsCounts);
		
		// hero
		{	AiSimTile tile = zone.getTile(1,1);
			int bombRange = 3;
			int bombNumber = 1;
			//state = new AiSimState(AiStateName.MOVING,Direction.RIGHT,0);
			PredefinedColor color = PredefinedColor.WHITE;
			zone.createHero(tile,color,bombNumber,bombRange,true);
		}

		// bomb #1
		{	AiSimTile tile = zone.getTile(1,1);
			// la bombe va bientôt exploser
			zone.createBomb(tile,3,2200);
		}

		// item 1
		{	AiSimTile tile = zone.getTile(1,2);
			AiItemType itemType = AiItemType.EXTRA_BOMB;
			zone.createItem(tile,itemType);
		}
		
		// softwall
		{	AiSimTile tile = zone.getTile(1,4);
			boolean destructible = true;
			zone.createBlock(tile,destructible);
		}
		
		
		// hardwalls
		{	boolean destructible = false;
			int rows[]={0,0,0,0,0,0,0,1,1,2,2,2,2,3,3,4,4,4,4,5,5,6,6,6,6,6,6,6};
			int cols[]= {0,1,2,3,4,5,6,0,6,0,2,4,6,0,6,0,2,4,6,0,6,0,1,2,3,4,5,6};
			for(int i=0;i<rows.length;i++)
			{	AiSimTile tile = zone.getTile(rows[i],cols[i]);
				zone.createBlock(tile,destructible);
			}
		}
		
		return zone;
	}
	
	/**
	 * Construit une fausse IA pour l'exemple
	 * (nécessaire pour certaines méthodes et classes).
	 * Vous pouvez bien sûr utilisez la votre à la place.
	 * 
	 * @return
	 * 		Une fausse IA.
	 */
	public static ArtificialIntelligence initAi()
	{	ArtificialIntelligence result = new ArtificialIntelligence()
		{	@Override
			protected void updatePercepts() throws StopRequestException{}
			@Override
			protected void initPercepts() throws StopRequestException{}
			@Override
			protected void initHandlers() throws StopRequestException{}
			@Override
			protected AiUtilityHandler<?> getUtilityHandler() throws StopRequestException{return null;}
			@Override
			protected AiMoveHandler<?> getMoveHandler() throws StopRequestException{return null;}
			@Override
			protected AiModeHandler<?> getModeHandler() throws StopRequestException{return null;}
			@Override
			protected AiBombHandler<?> getBombHandler() throws StopRequestException{return null;}
		};
		return result;
	}
}

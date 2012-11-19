package org.totalboumboum.ai.v201213.adapter.test;

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
import java.util.HashMap;
import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiSprite;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.model.full.AiSimHero;
import org.totalboumboum.ai.v201213.adapter.model.full.AiSimTile;
import org.totalboumboum.ai.v201213.adapter.model.full.AiSimZone;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Classe utilisée pour tester les fonctionnalités de l'API IA.
 * Cette classe permet d'initialiser des zones et des faux agents.
 * 
 * @author Vincent Labatut
 */
public final class InitData
{
	/////////////////////////////////////////////////////////////////
	// ZONES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
	 * 
	 * @return
	 * 		La zone décrite ci-dessus. 
	 */
	public static AiSimZone initZone1()
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
	 * 		La zone décrite ci-dessus.
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
			// la bombe va bientôt exploser (2200 = age de la bombe)
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
	 * Initialise la zone de manière à obtenir le résultat suivant :<br/>
	 * <pre>
	 *   0 1 2 3 4 5 6
	 *  ┌─┬─┬─┬─┬─┬─┬─┐
	 * 0│█│█│█│█│█│█│█│
	 *  ├─┼─┼─┼─┼─┼─┼─┤
	 * 1│█│☺│ │ │ │ │█│
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
	 * avec une mort subite provoquant les apparitions suivantes :
	 * <ul>
	 * 		<li>t=1000ms: bloc destructible en (4,1)</li>
	 * 		<li>t=1500ms: bloc indestructible en (4,1)</li>
	 * 		<li>t=2000ms: bloc destructible en (1,5)</li>
	 * 		<li>t=3000ms: bloc destructible en (1,4) et bombe en (2,5)</li>
	 * 		<li>t=3800ms: bombe en (3,5)</li>
	 * 		<li>t=4000ms: item en (4,5) et bloc destructible en (3,4)</li>
	 * 		<li>t=4500mss: bloc destructible en (3,1)</li>
	 * 		<li>t=5000mss: bloc destructible en (1,1)</li>
	 * </ul>
	 * 
	 * @return
	 * 		La zone décrite ci-dessus. 
	 */
	public static AiSimZone initZone3()
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
		
		// sudden death events
		{	AiTile tile;
			long time;
			HashMap<AiTile, List<AiSprite>> sprites;
			List<AiSprite> lst;
			{	// t=1000ms: bloc destructible en (4,1)
				boolean destructible = true;
				AiBlock block = zone.createBlock(null,destructible);
				tile = zone.getTile(4,1);
				time = 1000;
				lst = new ArrayList<AiSprite>();
				lst.add(block);
				sprites = new HashMap<AiTile, List<AiSprite>>();
				sprites.put(tile,lst);
				zone.createSuddenDeathEvent(time, sprites);
			}
			{	// t=1500ms: bloc indestructible en (4,1)
				boolean destructible = false;
				AiBlock block = zone.createBlock(null,destructible);
				tile = zone.getTile(4,1);
				time = 1500;
				lst = new ArrayList<AiSprite>();
				lst.add(block);
				sprites = new HashMap<AiTile, List<AiSprite>>();
				sprites.put(tile,lst);
				zone.createSuddenDeathEvent(time, sprites);
			}
			{	// t=2000ms: bloc destructible en (1,5)
				time = 2000;
				boolean destructible = true;
				AiBlock block = zone.createBlock(null,destructible);
				tile = zone.getTile(1,5);
				lst = new ArrayList<AiSprite>();
				lst.add(block);
				sprites = new HashMap<AiTile, List<AiSprite>>();
				sprites.put(tile,lst);
				zone.createSuddenDeathEvent(time, sprites);
			}
			{	// t=3000ms: bloc destructible en (1,4) et bombe en (2,5)
				time = 3000;
				sprites = new HashMap<AiTile, List<AiSprite>>();
				{	boolean destructible = true;
					AiBlock block = zone.createBlock(null,destructible);
					tile = zone.getTile(1,4);
					lst = new ArrayList<AiSprite>();
					lst.add(block);
					sprites.put(tile,lst);
				}
				{	int range = 2;
					long elapsed = 2000; 
					AiBomb bomb = zone.createBomb(null,range,elapsed);
					tile = zone.getTile(2,5);
					lst = new ArrayList<AiSprite>();
					lst.add(bomb);
					sprites.put(tile,lst);
				}
				zone.createSuddenDeathEvent(time, sprites);
			}
			{	// t=3800s: bombe en (3,5)
				time = 3800;
				int range = 1;
				long elapsed = 0; 
				AiBomb bomb = zone.createBomb(null,range,elapsed);
				tile = zone.getTile(3,5);
				lst = new ArrayList<AiSprite>();
				lst.add(bomb);
				sprites = new HashMap<AiTile, List<AiSprite>>();
				sprites.put(tile,lst);
				zone.createSuddenDeathEvent(time, sprites);
			}
			{	// t=4000ms: item en (4,5) et bloc destructible en (3,4)
				time = 4000;
				sprites = new HashMap<AiTile, List<AiSprite>>();
				{	AiItemType itemType = AiItemType.EXTRA_BOMB;
					AiItem item = zone.createItem(null,itemType);
					tile = zone.getTile(4,5);
					lst = new ArrayList<AiSprite>();
					lst.add(item);
					sprites.put(tile,lst);
				}
				{	boolean destructible = true;
					AiBlock block = zone.createBlock(null,destructible);
					tile = zone.getTile(3,4);
					lst = new ArrayList<AiSprite>();
					lst.add(block);
					sprites.put(tile,lst);
				}
				zone.createSuddenDeathEvent(time, sprites);
			}
			{	// t=4500ms: bloc destructible en (3,1)
				boolean destructible = true;
				AiBlock block = zone.createBlock(null,destructible);
				tile = zone.getTile(3,1);
				time = 4500;
				lst = new ArrayList<AiSprite>();
				lst.add(block);
				sprites = new HashMap<AiTile, List<AiSprite>>();
				sprites.put(tile,lst);
				zone.createSuddenDeathEvent(time, sprites);
			}
			{	// t=5000ms: bloc destructible en (1,1)
				boolean destructible = true;
				AiBlock block = zone.createBlock(null,destructible);
				tile = zone.getTile(1,1);
				time = 5000;
				lst = new ArrayList<AiSprite>();
				lst.add(block);
				sprites = new HashMap<AiTile, List<AiSprite>>();
				sprites.put(tile,lst);
				zone.createSuddenDeathEvent(time, sprites);
			}
		}
		
		return zone;
	}

	
	/////////////////////////////////////////////////////////////////
	// AGENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Construit un faux agent pour l'exemple
	 * (nécessaire pour certaines méthodes et classes).
	 * Vous devez bien sûr utiliser le vôtre à la place.
	 * 
	 * @return
	 * 		Un faux agent.
	 */
	public static ArtificialIntelligence initAi()
	{	ArtificialIntelligence result = new ArtificialIntelligence()
		{	protected void updatePercepts() throws StopRequestException{}
			protected void initPercepts() throws StopRequestException{}
			protected void initHandlers() throws StopRequestException{}
			protected AiUtilityHandler<?> getUtilityHandler() throws StopRequestException{return null;}
			protected AiMoveHandler<?> getMoveHandler() throws StopRequestException{return null;}
			protected AiModeHandler<?> getModeHandler() throws StopRequestException{return null;}
			protected AiBombHandler<?> getBombHandler() throws StopRequestException{return null;}
		};
		return result;
	}
}

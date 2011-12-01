package org.totalboumboum.ai.v201112.adapter;

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

import java.util.HashMap;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.path.astar.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItemType;
import org.totalboumboum.ai.v201112.adapter.data.AiSprite;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.model.full.AiFullModel;
import org.totalboumboum.ai.v201112.adapter.model.full.AiSimHero;
import org.totalboumboum.ai.v201112.adapter.model.full.AiSimTile;
import org.totalboumboum.ai.v201112.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201112.adapter.model.partial.AiPartialModel;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201112.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201112.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.cost.TileCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.heuristic.TileHeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Classe utilisée pour tester les fonctionnalités de ce package,
 * en particulier que AiModel réalise des simulations correctes.
 * on crée une zone fictive en faisant varier les sprites et leurs
 * actions, et on affiche le résultat des simulation pas-à-pas.
 * <br/>
 * <b>Note :</b> la classe modèle n'est pas définie pour représenter une zone
 * fictive comme ici, mais pour représenter la zone de jeu réelle.
 * ceci explique les limites imposées sur son constructeur.
 * 
 * @author Vincent Labatut
 */
public final class AiTest
{
	/**
	 * Cette méthode permet de tester
	 * le système de simulation inclus
	 * dans l'API d'IA.
	 * 
	 * @param args
	 * 		Aucun paramètre nécessaire.
	 */
	public static void main(String args[])
	{	
		// démonstration de simulation utilisant AiFullModel
//		simulateFullModel();
		// démonstration de simulation utilisant AiPartialModel
//		simulatePartialModel();
		
		// démonstration de l'utilisation de A*
		applyAstar(); 
	}
	
    /////////////////////////////////////////////////////////////////
	// INITIALIZATION			/////////////////////////////////////
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
	 */
	private static AiSimZone initZone()
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
	
    /////////////////////////////////////////////////////////////////
	// FULL MODEL SIMULATION	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Affiche le contenu du modèle passé en paramètre,
	 * ainsi que d'autres informations permettant de
	 * suivre la simulation.
	 * 
	 * @param model
	 * 		Le modèle à afficher.
	 */
	private static void displayModelSimulationStep(AiFullModel model)
	{	AiZone zone = model.getCurrentZone();
		long duration = model.getDuration();
		List<AiSprite> limitSprites = model.getLimitSprites();
		AiHero hero = zone.getHeroByColor(PredefinedColor.WHITE);

		System.out.println("limits: ");
		for(AiSprite sprite: limitSprites)
			System.out.println("     "+sprite);
		System.out.println("duration: "+duration);
		System.out.println("hero: "+hero);
		System.out.println(zone);
	}
	
	/**
	 * Réalise la simulation en utilisant
	 * les différentes méthodes
	 * proposées par {@link AiFullModel}.
	 */
	private static void simulateFullModel()
	{	AiSimZone zone = initZone();
		AiHero hero = zone.getHeroByColor(PredefinedColor.WHITE);
		
		// display initial zone
		System.out.println("initial zone:\n"+zone);
		AiFullModel model = new AiFullModel(zone);
		model.setSimulateItemsAppearing(true);
		model.applyChangeHeroDirection(hero,Direction.RIGHT);
		System.out.println("hero: "+hero);
		
		// first simulation
		long duration = 0;
		int iteration = 0;
		do
		{	// process simulation
			model.simulate();
//			model.simulate(hero);
//			model.simulateUntilFire();
			// display result
			duration = model.getDuration();
			System.out.println("iteration "+iteration);
			displayModelSimulationStep(model);
			// update iteration
			iteration++;
		}
		while(duration!=0);
		
		// change hero direction
		System.out.println("change hero direction: LEFT>DOWN");
		model.applyChangeHeroDirection(hero,Direction.DOWN);
		
		// simulate until the hero undergoes some change
		{	model.simulate(hero);
			displayModelSimulationStep(model);
		}
		{	model.simulate(hero);
			displayModelSimulationStep(model);
		}		
		// drop a bomb
		System.out.println("hero drops a bomb");
		model.applyDropBomb(hero,true);
		
		// change hero direction
		System.out.println("change hero direction: DOWN>LEFT");
		model.applyChangeHeroDirection(hero,Direction.LEFT);
		
		// simulate
		{	model.simulate();
			displayModelSimulationStep(model);
		}
		{	model.simulate();
			displayModelSimulationStep(model);
		}
		
		// change hero direction
		System.out.println("change hero direction: LEFT>UP");
		model.applyChangeHeroDirection(hero,Direction.UP);
		
		// simulate
		iteration = 0;
		do
		{	// process simulation
			model.simulate();
			// display result
			duration = model.getDuration();
			System.out.println("iteration "+iteration);
			displayModelSimulationStep(model);
			// update iteration
			iteration++;
		}
		while(duration!=0);
	}
	
    /////////////////////////////////////////////////////////////////
	// FULL MODEL SIMULATION	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Affiche le contenu du modèle passé en paramètre,
	 * ainsi que d'autres informations permettant de
	 * suivre la simulation.
	 * 
	 * @param model
	 * 		Le modèle à afficher.
	 */
	private static void displayModelSimulationStep(AiPartialModel model)
	{	long duration = model.getDuration();
		AiLocation ownLocation = model.getOwnLocation();

		System.out.println("duration: "+duration);
		System.out.println("own location: "+ownLocation);
		System.out.println(model);
		System.out.println("explosion start times:\n"+model.toStringDelays(true));
		System.out.println("explosion end times:\n"+model.toStringDelays(false));
	}
	
	/**
	 * Réalise la simulation en utilisant
	 * les différentes méthodes
	 * proposées par {@link AiFullModel}.
	 */
	private static void simulatePartialModel()
	{	AiSimZone zone = initZone();
		
		// display initial zone
		AiPartialModel model = new AiPartialModel(zone);
		System.out.println("initial zone:\n"+zone);
		displayModelSimulationStep(model);
		
		// first simulation
		long duration = 0;
		int iteration = 0;
		do
		{	// process simulation
			model.simulateMove(Direction.RIGHT);
			// display result
			duration = model.getDuration();
			System.out.println("iteration "+iteration);
			displayModelSimulationStep(model);
			// update iteration
			iteration++;
		}
		while(duration!=0);
		
		// wait for bomb to explode
		model.simulateWait(1120);
		displayModelSimulationStep(model);

		// wait for wall to explode
		model.simulateWait(640);
		displayModelSimulationStep(model);

		// change hero direction
		System.out.println("change hero direction: LEFT>DOWN");
		{	model.simulateMove(Direction.DOWN);
			displayModelSimulationStep(model);
		}
		{	model.simulateMove(Direction.DOWN);
			displayModelSimulationStep(model);
		}		
		
		// change hero direction
		System.out.println("change hero direction: DOWN>LEFT");
		{	model.simulateMove(Direction.LEFT);
			displayModelSimulationStep(model);
		}
		{	model.simulateMove(Direction.LEFT);
			displayModelSimulationStep(model);
		}
		
		// simulate wait
		model.simulateWait(1000);
		displayModelSimulationStep(model);
	}

    /////////////////////////////////////////////////////////////////
	// A STAR		/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static void applyAstar()
	{	// on utilise ici une ai anonyme, mais vous pouvez utiliser la vôtre à la place
		ArtificialIntelligence ai = new ArtificialIntelligence()
		{	protected void updatePercepts() throws StopRequestException{}
			protected void initPercepts() throws StopRequestException{}
			protected void initHandlers() throws StopRequestException{}
			protected AiUtilityHandler<?> getUtilityHandler() throws StopRequestException{return null;}
			protected AiMoveHandler<?> getMoveHandler() throws StopRequestException{return null;}
			protected AiModeHandler<?> getModeHandler() throws StopRequestException{return null;}
			protected AiBombHandler<?> getBombHandler() throws StopRequestException{return null;}
		};
		
		// on initialise la zone
		AiSimZone zone = initZone();
		ai.setZone(zone);
		AiHero hero = zone.getHeroByColor(PredefinedColor.WHITE);
		
		///////////////////// EXEMPLE 1		/////////////////////////////
		// utilisation simple
		{	// on crée l'objet a* avec les fonctions les plus simples
			CostCalculator costCalculator = new TileCostCalculator(ai);
			HeuristicCalculator heuristicCalculator = new TileHeuristicCalculator(ai);
			SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
			Astar astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
			astar.setVerbose(true); // pour affichier les détails du traitement
			
			// on applique l'algorithme pour trouver une chemin entre la position
			// courante et une case de destination
			AiLocation startLocation = new AiLocation(hero);
			AiTile endTile = zone.getTile(5,3);
			try
			{	astar.processShortestPath(startLocation,endTile);
			}
			catch (StopRequestException e)
			{	e.printStackTrace();
			}
			catch (LimitReachedException e)
			{	// le fait de lever cette exception indique qu'A* a
				// développé un arbre de recherche trop grand : il existe
				// peut-être une solution, mais elle n'a pas été trouvée
				e.printStackTrace();
			}
		}
	}
}

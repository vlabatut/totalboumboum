package org.totalboumboum.ai.v201314.adapter.test;

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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.data.AiSuddenDeathEvent;
import org.totalboumboum.ai.v201314.adapter.model.full.AiFullModel;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201314.adapter.model.partial.AiPartialModel;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe utilisée pour tester les fonctionnalités de l'API IA.
 * Cette classe permet de réaliser des simulations en utilisant
 * le modèle partiel.
 * 
 * @author Vincent Labatut
 */
public final class PartialModelSimulation
{
	/**
	 * Teste les fonctionalités de l'API IA
	 * propres au modèle partiel.
	 * 
	 * @param args
	 * 		Pas utilisé.
	 */
	public static void main(String args[])
	{	// utilisation de base
		example1();
		// mort subite
		example2();
	}
	
	/**
	 * Réalise la simulation en utilisant
	 * les différentes méthodes
	 * proposées par {@link AiFullModel}.
	 */
	public static void example1()
	{	AiSimZone zone = InitData.initZone1();
		
		// display initial zone
		AiPartialModel model = new AiPartialModel(zone);
		System.out.println("initial zone:\n"+zone);
		displayModelSimulationStep(model);
		
		// first simulation
		System.out.println("series of simulations");
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
		System.out.println("wait for bomb to explode");
		model.simulateWait(1120);
		displayModelSimulationStep(model);

		// wait for wall to explode
		System.out.println("wait for wall to explode");
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
		System.out.println("simulate wait");
		model.simulateWait(1000);
		displayModelSimulationStep(model);
	}

	/**
	 * Réalise une simulation impliquant
	 * une mort subite.
	 */
	public static void example2()
	{	AiSimZone zone = InitData.initZone3();
		
		// display initial zone
		AiPartialModel model = new AiPartialModel(zone);
		System.out.println("initial zone:\n"+zone);
		displayModelSimulationStep(model);

		// display sudden death events
		List<AiSuddenDeathEvent> events = model.getOriginalZone().getAllSuddenDeathEvents();
		if(!events.isEmpty())
		{	System.out.println("sudden death events: ");
			for(AiSuddenDeathEvent event: events)
				System.out.println(event);
		}

		long duration = 0;
		long totalDuration = 0;
		int iteration = 0;
		boolean result = true;
		List<Direction> directions = Arrays.asList
		(	Direction.RIGHT, Direction.LEFT, Direction.DOWN, 
			Direction.UP, Direction.DOWN, Direction.RIGHT
		);
		Iterator<Direction> it = directions.iterator();
		while(it.hasNext() && result)
		{	Direction direction = it.next();
			do
			{	// process simulation
				result = model.simulateMove(direction);
				// display result
				duration = model.getDuration();
				totalDuration = model.getTotalDuration();
				System.out.println("iteration "+iteration+" ["+totalDuration+"]");
				displayModelSimulationStep(model);
				// update iteration
				iteration++;
			}
			while(duration!=0 && result);
		}
	}
	
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
		AiLocation ownLocation = model.getCurrentLocation();

		System.out.println("duration: "+duration);
		System.out.println("own location: "+ownLocation);
		System.out.println(model);
		System.out.println("explosion start times:\n"+model.toStringDelays(true));
		System.out.println("explosion end times:\n"+model.toStringDelays(false));
	}
}

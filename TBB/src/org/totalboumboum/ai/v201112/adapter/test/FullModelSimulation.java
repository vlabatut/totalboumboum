package org.totalboumboum.ai.v201112.adapter.test;

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

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiSprite;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.model.full.AiFullModel;
import org.totalboumboum.ai.v201112.adapter.model.full.AiSimZone;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Classe utilisée pour tester les fonctionnalités de l'API IA.
 * Cette classe permet de réaliser des simulation en utilisant
 * le modèle complet.
 * 
 * @author Vincent Labatut
 */
public final class FullModelSimulation
{
	/**
	 * Réalise la simulation en utilisant
	 * les différentes méthodes
	 * proposées par {@link AiFullModel}.
	 */
	public static void main(String args[])
	{	AiSimZone zone = InitZone.initZone();
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
}

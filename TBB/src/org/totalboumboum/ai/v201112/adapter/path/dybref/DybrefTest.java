package org.totalboumboum.ai.v201112.adapter.path.dybref;

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

import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.AiAction;
import org.totalboumboum.ai.v201112.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.model.AiModelTest;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Classe utilisée pour tester les fonctionnalités de ce package,
 * en particulier que AiModel réalise des simulations correctes.
 * on crée une zone fictive en faisant varier les sprites et leurs
 * actions, et on affiche le résultat des simulation pas-à-pas.<br/>
 * 
 * <b>Note :</b> la classe modèle n'est pas définie pour représenter une zone
 * fictive comme ici, mais pour représenter la zone de jeu réelle.
 * ceci explique les limites imposées sur son constructeur.
 * 
 * @author Vincent Labatut
 *
 */
public final class DybrefTest
{
	/**
	 * Cette méthode permet de tester
	 * l'algorithme Dybref, qui trouve
	 * les plus courts chemins pour une 
	 * zone donnée.
	 * 
	 * @param args
	 * 		Aucun paramètre n'est nécessaire.
	 */
	public static void main(String args[])
	{	// init
		AiZone zone = AiModelTest.initZone();
		AiHero hero = zone.getHeroByColor(PredefinedColor.WHITE);
		ArtificialIntelligence ai = new ArtificialIntelligence()
		{	// IA bidon
			@Override
			public AiAction processAction() throws StopRequestException
			{	AiAction result = new AiAction(AiActionName.NONE);
				return result;
			}
		};
		
		/*
		 * Pour rappel, la tête de la zone obtenue :
		 * ┌─┬─┬─┬─┬─┬─┬─┐
		 * │█│█│█│█│█│█│█│
		 * ├─┼─┼─┼─┼─┼─┼─┤
		 * │█│☺│ │□│ │ │█│
		 * ├─┼─┼─┼─┼─┼─┼─┤
		 * │█│ │█│ │█│ │█│
		 * ├─┼─┼─┼─┼─┼─┼─┤
		 * │█│ │ │ │ │▒│█│
		 * ├─┼─┼─┼─┼─┼─┼─┤
		 * │█│ │█│ │█│ │█│
		 * ├─┼─┼─┼─┼─┼─┼─┤
		 * │█│ │ │ │ │●│█│
		 * ├─┼─┼─┼─┼─┼─┼─┤
		 * │█│█│█│█│█│█│█│
		 * └─┴─┴─┴─┴─┴─┴─┘
		 */
		
		// application de l'algo
		Dybref dybref = new Dybref(ai,hero);
		DybrefMatrix matrix = null;
		try
		{	matrix = dybref.processShortestPaths(zone);
		}
		catch (StopRequestException e)
		{	
			e.printStackTrace();
		}
		catch (LimitReachedException e)
		{	e.printStackTrace();
		}
		
		// affichage du résultat
		if(matrix!=null)
			System.out.println("Result :\n"+matrix);
	}
}

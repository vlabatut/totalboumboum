package org.totalboumboum.ai.v201314.ais._simplet;

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

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;

/**
 * Cet agent est très simple : sa stratégie d'attaque consiste juste à
 * poser une bombe de manière à menacer son adversaire (stratégie très
 * peu efficace !). Donc il sera quasiment tout le temps en mode
 * attaque.
 * 
 * @author Vincent Labatut
 */
public class ModeHandler extends AiModeHandler<Agent>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected ModeHandler(Agent ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Zone de jeu */
	private AiZone zone;
	/** Personnage contrôlé par l'agent */
	private AiHero ownHero;
	
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{	ai.checkInterruption();
		
		int range = ownHero.getBombRange();
		int bombs = ownHero.getBombNumberMax();
		print("      range="+range+" & bombs="+bombs);
		
		// en raison de son attaque très (trop) simple,
		// cet agent a simplement besoin d'au moins une
		// bombe, de portée non-nulle.
		boolean result = bombs>0 && range>0;
		
    	return result;
	}
	
	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	ai.checkInterruption();
		
		// on vérifie s'il reste des items apparents
		// simplifications : 
		//		- on ne vérifie pas s'ils sont accessibles
		// 		- ni même si on a le temps de les ramasser
		int hiddenItems = zone.getHiddenItemsCount();
		print("      hiddenItems="+hiddenItems);
		boolean result = hiddenItems>0;
		
   		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Mise à jour de la sortie graphique.
	 * 
	 * @throws StopRequestException 
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// rien de spécial à afficher ici
	}
}

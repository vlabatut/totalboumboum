package org.totalboumboum.ai.v201112.ais._simplet;

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

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent.
 * Le traitement est simpliste : on pose une bombe si on 
 * est arrivé à destination, ou bien si on doit faire exploser
 * un mur génant. 
 * 
 * @author Vincent Labatut
 */
public class BombHandler extends AiBombHandler<Simplet>
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
	protected BombHandler(Simplet ai) throws StopRequestException
    {	super(ai);
    	ai.checkInterruption();
		
    	// on aura souvent besoin du personnage
    	ownHero = ai.getZone().getOwnHero();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Personnage contrôlé par cet agent */
	private AiHero ownHero;
	
	@Override
	protected boolean considerBombing() throws StopRequestException
	{	ai.checkInterruption();
		
		// à noter qu'il est peut-être préférable de tester si on peut poser 
		// une bombe même quand on n'est pas arrivé à destination...
		
		boolean result = false;
		AiTile currentTile = ownHero.getTile();

		// on vérifie si c'est possible de poser une bombe dans cette case
		AiBomb bomb = ownHero.getBombPrototype();
		boolean bombAbsence = currentTile.isCrossableBy(bomb);
		print("    bombAbsence="+bombAbsence);
		if(bombAbsence)
		{	// on pose une bombe si on est arrivé à destination, et que l'objectif était de bomber
			AiTile currentDestination = ai.moveHandler.getCurrentDestination();
			Boolean bombDestination = ai.moveHandler.bombDestination;
			boolean bombPrimaryDestination = currentTile.equals(currentDestination) 
				&& bombDestination;
			print("      bombPrimaryDestination="+bombPrimaryDestination);
			
			// ou doit bomber un objectif secondaire
			boolean secondaryBombing = ai.moveHandler.secondaryBombing;
			print("      secondaryBombing="+secondaryBombing);
			
			// et si poser une bombe ne nous met pas en danger
			boolean canBomb = true;
			
			if(canBomb && (bombPrimaryDestination || secondaryBombing))
			{	// on pourrait tester si l'agent ne vas pas se bloquer/tuer
				result = true;
				// on réinitialise l'éventuel chemin de fuite présent dans le gestionnaire de 
				// déplacement, car le fait de poser une bombe change tout calcul précédent.
				ai.moveHandler.safeDestination = null;
			}
		}
	
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// rien à afficher ici...
	}
}

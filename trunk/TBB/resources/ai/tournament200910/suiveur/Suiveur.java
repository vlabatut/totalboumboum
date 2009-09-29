package tournament200910.suiveur;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import fr.free.totalboumboum.ai.adapter200910.ArtificialIntelligence;
import fr.free.totalboumboum.ai.adapter200910.communication.AiAction;
import fr.free.totalboumboum.ai.adapter200910.communication.AiActionName;
import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;
import fr.free.totalboumboum.ai.adapter200910.data.AiHero;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.data.AiZone;
import fr.free.totalboumboum.engine.content.feature.Direction;

/**
 * cette classe implémente une IA relativement stupide, qui choisit une cible
 * (un autre joueur), puis essaie de la rejoindre, et enfin se contente de la
 * suivre partout où elle va.
 */
public class Suiveur extends ArtificialIntelligence 
{
	private boolean first = true;
	
	public AiAction processAction() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE

		zone = getPercepts();
		ownHero = zone.getOwnHero();				
		AiAction result = new AiAction(AiActionName.NONE);
		
		// si ownHero est null, c'est que l'IA est morte : inutile de continuer
		if(ownHero!=null)
		{	// on met à jour la position de l'ia dans la zone
			currentTile = ownHero.getTile();
			currentX = ownHero.getPosX();
			currentY = ownHero.getPosY();
			
			// premier appel : on initialise		
			if(first)
			{	first = false;
				AiTile destination = zone.getTile(6,8);
				pathManager = new PathManager(this,destination);
				safetyManager = new SafetyManager(this);
			}
			
			// on met à jour le gestionnaire de sécurité
			safetyManager.update();
			// on calcule la direction de déplacement
			Direction moveDir = pathManager.update();
			result = new AiAction(AiActionName.MOVE,moveDir);
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// PATH MANAGER				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** classe chargée du déplacement du personnage */
	private PathManager pathManager = null;

	/////////////////////////////////////////////////////////////////
	// SAFETY MANAGER				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** classe chargée de déterminer quelles cases sont sûres */
	private SafetyManager safetyManager = null;

	public SafetyManager getSafetyManager() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return safetyManager;		
	}
	
	public double getSafetyLevel(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return safetyManager.getSafetyLevel(tile);		
	}
	
	public boolean isSafe(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
			
		return safetyManager.isSafe(tile);
	}
	
	/////////////////////////////////////////////////////////////////
	// CURRENT TILE				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** la case occupée actuellement par le personnage */
	private AiTile currentTile = null;

	public AiTile getCurrentTile() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return currentTile;
	}

	/////////////////////////////////////////////////////////////////
	// CURRENT LOCATION			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** la position en pixels occupée actuellement par le personnage */
	private double currentX;
	/** la position en pixels occupée actuellement par le personnage */
	private double currentY;

	public double getCurrentX() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return currentX;
	}
	
	public double getCurrentY() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return currentY;
	}

	/////////////////////////////////////////////////////////////////
	// OWN HERO					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** le personnage dirigé par cette IA */
	private AiHero ownHero;

	public AiHero getOwnHero() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return ownHero;
	}

	/////////////////////////////////////////////////////////////////
	// PERCEPTS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** la zone de jeu */
	private AiZone zone;

	public AiZone getZone() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return zone;
	}
}

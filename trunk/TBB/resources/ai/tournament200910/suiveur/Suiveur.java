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

import java.util.ArrayList;

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
	@Override
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
			Direction moveDir = Direction.NONE;
			
			// premier appel : on initialise		
			if(first)
				init();
			
			// on met à jour le gestionnaire de sécurité
			safetyManager.update();
			// si on est en train de fuir : on continue
			if(escapeManager!=null)
			{	if(escapeManager.hasArrived())
					escapeManager = null;
				else
					moveDir = escapeManager.update();
			}
			// sinon si on est en danger : on commence à fuir
			else if(!safetyManager.isSafe(currentTile))
			{	escapeManager = new EscapeManager(this);
				moveDir = escapeManager.update();
			}
			// sinon on se déplace vers la cible
			else
			{	updateTarget();
				if(target!=null)
					moveDir = targetManager.update();			
			}
			result = new AiAction(AiActionName.MOVE,moveDir);
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// INITIALISATION			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** variable utilisée uniquement lors de l'initialisation */
	private boolean first = true;

	private void init() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE

		first = false;
		safetyManager = new SafetyManager(this);
		targetManager = new PathManager(this,currentTile);
	}

	/////////////////////////////////////////////////////////////////
	// PATH MANAGERS			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** classe chargée du déplacement vers la cible */
	private PathManager targetManager = null;
	/** classe chargée de la fuite du personnage */
	private EscapeManager escapeManager = null;
	
	/////////////////////////////////////////////////////////////////
	// SAFETY MANAGER				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** classe chargée de déterminer quelles cases sont sûres */
	private SafetyManager safetyManager = null;

	/**
	 * renvoie le gestionnaire de sécurité
	 */
	public SafetyManager getSafetyManager() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return safetyManager;		
	}
	
	/**
	 * renvoie le niveau de sécurité de la case passée en paramètre
	 */
	public double getSafetyLevel(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return safetyManager.getSafetyLevel(tile);		
	}
	
	/**
	 * détermine si la case passée en paramètre est sûre
	 */
	public boolean isSafe(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
			
		return safetyManager.isSafe(tile);
	}
	
	/////////////////////////////////////////////////////////////////
	// CURRENT TILE				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** la case occupée actuellement par le personnage */
	private AiTile currentTile = null;

	/**
	 * renvoie la case courante
	 */
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

	/**
	 * renvoie l'abscisse courante (en pixels)
	 */
	public double getCurrentX() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return currentX;
	}
	
	/**
	 * renvoie l'ordonnée courante (en pixels)
	 */
	public double getCurrentY() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return currentY;
	}

	/////////////////////////////////////////////////////////////////
	// OWN HERO					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** le personnage dirigé par cette IA */
	private AiHero ownHero;

	/**
	 * renvoie le personnage contrôlé par cette IA
	 */
	public AiHero getOwnHero() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return ownHero;
	}

	/////////////////////////////////////////////////////////////////
	// PERCEPTS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** la zone de jeu */
	private AiZone zone;

	/**
	 * renvoie la zone de jeu
	 */
	public AiZone getZone() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return zone;
	}

	/////////////////////////////////////////////////////////////////
	// TARGET					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** la cible à suivre (ou null si aucune cible n'existe) */
	private AiHero target;
	/** case précédente de la cible */
	private AiTile targetPreviousTile;
	
	/**
	 * choisit aléatoirement un joueur comme cible à suivre
	 */
	private void chooseTarget() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		target = null;
		ArrayList<AiHero> heroes = new ArrayList<AiHero>(zone.getHeroes());
		heroes.remove(ownHero);
		if(!heroes.isEmpty())
		{	int index = (int)Math.random()*heroes.size();
			target = heroes.get(index);
		}
	}

	/**
	 * met à jour la cible, et éventuellement le chemin jusqu'à elle
	 */
	private void updateTarget() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		if(target==null || target.hasEnded())
		{	chooseTarget();
			if(target!=null)
			{	AiTile targetCurrentTile = target.getTile();
				targetManager.setDestination(targetCurrentTile);
				targetPreviousTile = targetCurrentTile; 
			}
		}
		else
		{	AiTile targetCurrentTile = target.getTile();
			if(targetCurrentTile==currentTile)
			{	double targetX = target.getPosX();
				double targetY = target.getPosY();
				targetManager.setDestination(targetX,targetY);				
			}
			else if(targetCurrentTile!=targetPreviousTile)
			{	targetManager.setDestination(targetCurrentTile);
				targetPreviousTile = targetCurrentTile;				
			}
			
		}
	}
}

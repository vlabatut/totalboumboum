package org.totalboumboum.ai.v201112.ais._suiveur;

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

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * cette classe implémente une IA relativement stupide, qui choisit une cible
 * (un autre joueur), puis essaie de la rejoindre, et enfin se contente de la
 * suivre partout où elle va.
 * 
 * @author Vincent Labatut
 *
 */
public class Suiveur extends ArtificialIntelligence
{	
	/** interrupteur permettant d'afficher la trace du traitement */
	private boolean verbose = false;

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
	
	private void updateLocation() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		currentTile = ownHero.getTile();
		currentX = ownHero.getPosX();
		currentY = ownHero.getPosY();
				
	}

	/////////////////////////////////////////////////////////////////
	// OWN HERO					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** le personnage dirigé par cette IA */
	private AiHero ownHero = null;

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
	@Override
	public void init() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		ownHero = getZone().getOwnHero();
	
		updateLocation();
		
		safetyManager = new SafetyManager(this);
		targetManager = new PathManager(this,currentTile);
	}
	
	@Override
	public void updatePercepts() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		// on met à jour la position de l'ia dans la zone
		updateLocation();
		if(verbose)
			System.out.println(ownHero.getColor()+": ("+currentTile.getRow()+","+currentTile.getCol()+") ("+currentX+","+currentY+")");
		
		// on met à jour le gestionnaire de sécurité
		safetyManager.update();
	}

	@Override
	public Direction considerMoving() throws StopRequestException
	{	Direction result = Direction.NONE;			
		
		// si on est en train de fuir : on continue
		if(escapeManager!=null)
		{	if(escapeManager.hasArrived())
				escapeManager = null;
			else
				result = escapeManager.update();
		}
		
		// sinon si on est en danger : on commence à fuir
		else if(!safetyManager.isSafe(currentTile))
		{	escapeManager = new EscapeManager(this);
			result = escapeManager.update();
		}
		
		// sinon on se déplace vers la cible
		else
		{	updateTarget();
			if(target!=null)
				result = targetManager.update();
		}
		
		return result;
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
		List<AiHero> heroes = new ArrayList<AiHero>(getZone().getRemainingHeroes());
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
package tournament200910.adatepeozbek.v1;

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

import org.totalboumboum.ai.adapter200910.ArtificialIntelligence;
import org.totalboumboum.ai.adapter200910.communication.AiAction;
import org.totalboumboum.ai.adapter200910.communication.AiActionName;
import org.totalboumboum.ai.adapter200910.communication.StopRequestException;
import org.totalboumboum.ai.adapter200910.data.AiHero;
import org.totalboumboum.ai.adapter200910.data.AiTile;
import org.totalboumboum.ai.adapter200910.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe impl�mente une IA relativement stupide, qui choisit une cible
 * (un autre joueur), puis essaie de la rejoindre, et enfin se contente de la
 * suivre partout o� elle va.
 */
public class AdatepeOzbek extends ArtificialIntelligence 
{	
	@Override
	public AiAction processAction() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		if(ownHero == null)
			init();
	
		AiAction result = new AiAction(AiActionName.NONE);
		
		if(!ownHero.hasEnded())
		{	
			updateLocation();
			
			Direction moveDir = Direction.NONE;
			
			
			safetyManager.update();
			// si on est en train de fuir : on continue
			if(!safetyManager.isSafe(currentTile))
			{	
				moveDir = escapeManager.update();
			}
			
			// on met � jour la direction renvoy�e au moteur du jeu
			result = new AiAction(AiActionName.MOVE,moveDir);
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// INITIALISATION			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void init() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE

		zone = getPercepts();
		ownHero = zone.getOwnHero();
	
		updateLocation();
		
		safetyManager = new SafetyManager(this);
		escapeManager = new EscapeManager(this);
	}

	/////////////////////////////////////////////////////////////////
	// PATH MANAGERS			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** classe charg�e du d�placement vers la cible */
//	private PathManager targetManager = null;
	/** classe charg�e de la fuite du personnage */
	private EscapeManager escapeManager = null;
	
	/////////////////////////////////////////////////////////////////
	// SAFETY MANAGER				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** classe charg�e de d�terminer quelles cases sont s�res */
	private SafetyManager safetyManager = null;

	/**
	 * renvoie le gestionnaire de s�curit�
	 */
	public SafetyManager getSafetyManager() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return safetyManager;		
	}
	
	/**
	 * renvoie le niveau de s�curit� de la case pass�e en param�tre
	 */
	public double getSafetyLevel(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return safetyManager.getSafetyLevel(tile);		
	}
	
	/**
	 * d�termine si la case pass�e en param�tre est s�re
	 */
	public boolean isSafe(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
			
		return safetyManager.isSafe(tile);
	}
	
	/////////////////////////////////////////////////////////////////
	// CURRENT TILE				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** la case occup�e actuellement par le personnage */
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
	/** la position en pixels occup�e actuellement par le personnage */
	private double currentX;
	/** la position en pixels occup�e actuellement par le personnage */
	private double currentY;

	/**
	 * renvoie l'abscisse courante (en pixels)
	 */
	public double getCurrentX() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return currentX;
	}
	
	/**
	 * renvoie l'ordonn�e courante (en pixels)
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
	/** le personnage dirig� par cette IA */
	private AiHero ownHero = null;

	/**
	 * renvoie le personnage contr�l� par cette IA
	 */
	public AiHero getOwnHero() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return ownHero;
	}

	/////////////////////////////////////////////////////////////////
	// PERCEPTS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** la zone de jeu */
	private AiZone zone = null;

	/**
	 * renvoie la zone de jeu
	 */
	public AiZone getZone() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return zone;
	}
}

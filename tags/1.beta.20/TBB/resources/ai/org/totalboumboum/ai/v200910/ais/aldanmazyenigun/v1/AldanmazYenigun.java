package org.totalboumboum.ai.v200910.ais.aldanmazyenigun.v1;

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

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiItem;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @version 1
 * 
 * @author Cansin Aldanmaz
 * @author Yalcin Yenigun
 *
 */
public class AldanmazYenigun extends ArtificialIntelligence 
{	

	@Override
	public AiAction processAction() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE

		// premier appel : on initialise		
		if(ownHero == null)
			init();
	
		AiAction result = new AiAction(AiActionName.NONE);
		
		// si le personnage control� a �t� �limin�, inutile de continuer
		if(!ownHero.hasEnded())
		{	// on met � jour la position de l'ia dans la zone
			updateLocation();
			/*
			if(verbose)
				System.out.println(ownHero.getColor()+": ("+currentTile.getLine()+","+currentTile.getCol()+") ("+currentX+","+currentY+")");*/
			Direction moveDir = Direction.NONE;
			
			
			// on met � jour le gestionnaire de s�curit�
			safetyManager.update();
			// si on est en train de fuir : on continue
			if(escapeManager!=null)
			{	if(escapeManager.hasArrived())
					escapeManager = null;
				else
					moveDir = escapeManager.update();
			}
			
			// sinon si on est en danger : on commence � fuir
			else if(!safetyManager.isSafe(currentTile))
			{	escapeManager = new EscapeManager(this);
				moveDir = escapeManager.update();
			}
			
			// sinon on se d�place vers la cible
			else
			{	updateTarget();
				if(targetItem!=null)
					moveDir = targetManager.update();
				else if(targetWall != null)
					moveDir = targetManager.update();
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
		targetManager = new PathManager(this,currentTile);
	}

	/////////////////////////////////////////////////////////////////
	// PATH MANAGERS			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** classe charg�e du d�placement vers la cible */
	private PathManager targetManager = null;
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

	/////////////////////////////////////////////////////////////////
	// TARGET					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** la cible � suivre (ou null si aucune cible n'existe) */
	private AiItem targetItem;
	/** case pr�c�dente de la cible */
	private AiTile targetPreviousTile;
	
	private AiBlock targetWall;

	/**
	 * met � jour la cible, et �ventuellement le chemin jusqu'� elle
	 */
	private void updateTarget() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		if(targetItem==null || targetItem.hasEnded())
		{	chooseBonusWay();
			if(targetItem!=null)
			{	AiTile targetCurrentTile = targetItem.getTile();
				targetManager.setDestination(targetCurrentTile);
				targetPreviousTile = targetCurrentTile; 
			}
		}
		/*
		else if(targetWall==null && targetWall.hasEnded())
		{	
			chooseWallWay();
			if(targetWall!=null)
			{	AiTile targetCurrentTile = targetWall.getTile();
				targetManager.setDestination(targetCurrentTile);
				targetPreviousTile = targetCurrentTile;
			}
		}
		*/
		else
		{
			
			AiTile targetCurrentTile = targetItem.getTile();
			if(targetCurrentTile==currentTile)
			{	double targetX = targetItem.getPosX();
				double targetY = targetItem.getPosY();
				targetManager.setDestination(targetX,targetY);				
			}
			else if(targetCurrentTile!=targetPreviousTile)
			{	targetManager.setDestination(targetCurrentTile);
				targetPreviousTile = targetCurrentTile;				
			}
			
		}
	}
	
	private void chooseBonusWay() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		targetItem = null;
		List<AiItem> items = new ArrayList<AiItem>(zone.getItems());

		if(!items.isEmpty())
		{
		int index = (int)Math.random()*items.size();
			targetItem = items.get(index);
		}
	}
	
	/*
	private void chooseWallWay() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		targetWall = null;
		List<AiBlock> walls = zone.getBlocks();

		if(!walls.isEmpty()){
			int index = (int)Math.random()*walls.size();
			targetWall = walls.get(index);
	}
	}
	*/
	
	
//	@SuppressWarnings("static-access")
	public AiAction laisser() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		AiAction bombe = null;
		if (isSafe(currentTile)) 
			
		bombe = new AiAction(AiActionName.DROP_BOMB);
		
		return bombe;
	}
	
}
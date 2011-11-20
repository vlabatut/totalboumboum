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

import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;

/**
 * cette classe implémente une IA relativement stupide, qui choisit une cible
 * (un autre joueur), puis essaie de la rejoindre, et enfin se contente de la
 * suivre partout où elle va.
 * 
 * @author Vincent Labatut
 * @version 2 - version adaptée à l'API 2011-2012
 */
public class Suiveur extends ArtificialIntelligence
{	/** interrupteur permettant d'afficher la trace du traitement */
	private boolean verbose = false;

	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** le personnage dirigé par cette IA */
	protected AiHero ownHero = null;
	/** la case occupée actuellement par le personnage */
	protected AiTile currentTile = null;
	/** la position en pixels occupée actuellement par le personnage */
	protected double currentX;
	/** la position en pixels occupée actuellement par le personnage */
	protected double currentY;

	@Override
	protected void initPercepts() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		ownHero = getZone().getOwnHero();
	
		updateLocation();
	}
	
	@Override
	public void updatePercepts() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		// on met à jour la position de l'ia dans la zone
		updateLocation();
		if(verbose) System.out.println(ownHero.getColor()+": ("+currentTile.getRow()+","+currentTile.getCol()+") ("+currentX+","+currentY+")");
		
		// on met à jour le gestionnaire de sécurité
		safetyHandler.update();
		
		// on met à jour le gestionnaire de déplacement
		updateMoveHandler();
	}

	private void updateLocation() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		currentTile = ownHero.getTile();
		currentX = ownHero.getPosX();
		currentY = ownHero.getPosY();
				
	}

	/////////////////////////////////////////////////////////////////
	// HANDLERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** gestionnaire de déplacement vers la cible */
	protected TargetHandler targetHandler;
	/** gestionnaire de fuite */
	protected EscapeHandler escapeHandler;
	/** gestionnaire de sûreté */
	protected SafetyHandler safetyHandler;
	/** gestionnaire de déplacement */
	protected AiMoveHandler<Suiveur> moveHandler;
	/** gestionnaire de mode (bidon) */
	protected AiModeHandler<Suiveur> modeHandler;
	/** gestionnaire d'utilité (bidon) */
	protected AiUtilityHandler<Suiveur> utilityHandler;
	/** gestionnaire de bombage (bidon) */
	protected AiBombHandler<Suiveur> bombHandler;
	
	@Override
	protected void initHandlers() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		modeHandler = new AiModeHandler<Suiveur>(this)
		{	@Override
			protected boolean isCollectPossible() throws StopRequestException
			{	return true;
			}
			
			@Override
			protected boolean hasEnoughItems() throws StopRequestException
			{	return false;
			}
		};
		
		utilityHandler = new AiUtilityHandler<Suiveur>(this)
		{	@Override
			protected Set<AiTile> selectTiles() throws StopRequestException
			{	return new TreeSet<AiTile>();
			}

			@Override
			protected void initCriteria() throws StopRequestException
			{	
			}

			@Override
			protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
			{	return null;
			}
		};
		
		bombHandler = new AiBombHandler<Suiveur>(this)
		{	@Override
			protected boolean considerBombing() throws StopRequestException
			{	return false;
			}
		};
		
		targetHandler = new TargetHandler(this);
		escapeHandler = new EscapeHandler(this);
		safetyHandler = new SafetyHandler(this);
	}

	@Override
	protected AiModeHandler<Suiveur> getModeHandler() throws StopRequestException
	{	return modeHandler;
	}

	@Override
	protected AiUtilityHandler<Suiveur> getUtilityHandler() throws StopRequestException
	{	return utilityHandler;
	}

	@Override
	protected AiBombHandler<Suiveur> getBombHandler() throws StopRequestException
	{	return bombHandler;
	}

	@Override
	protected AiMoveHandler<Suiveur> getMoveHandler() throws StopRequestException
	{	return moveHandler;
	}

	private void updateMoveHandler() throws StopRequestException
	{	// si on est en train de fuir : on continue
		if(moveHandler==escapeHandler)
		{	if(escapeHandler.hasArrived())
				moveHandler = targetHandler;
		}
		
		// sinon si on est en danger : on commence à fuir
		else if(!safetyHandler.isSafe(currentTile))
		{	moveHandler = escapeHandler;
			escapeHandler.reset();
		}
		
		// sinon on se déplace vers la cible
		else
		{	moveHandler = targetHandler;
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput() throws StopRequestException
	{	checkInterruption();

		moveHandler.updateOutput();
		safetyHandler.updateOutput();
	}
}
